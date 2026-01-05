package com.cinovo.backend.DB.Service;

import com.cinovo.backend.DB.Model.Enum.Gender;
import com.cinovo.backend.DB.Model.Enum.MediaType;
import com.cinovo.backend.DB.Model.Image;
import com.cinovo.backend.DB.Model.Person;
import com.cinovo.backend.DB.Repository.PersonRepository;
import com.cinovo.backend.DB.Util.Shared;
import com.cinovo.backend.DB.Util.TMDBLogically;
import com.cinovo.backend.TMDB.Response.Common.MediaResponse;
import com.cinovo.backend.TMDB.Response.Common.PeopleResponse;
import com.cinovo.backend.TMDB.Response.PeoplePopularResponse;
import com.cinovo.backend.TMDB.Response.PersonExternalIdsResponse;
import com.cinovo.backend.TMDB.Response.SearchResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.jbosslog.JBossLog;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
@JBossLog
public class PersonService implements TMDBLogically<Object, Object>
{
    private final PersonRepository personRepository;
    private final com.cinovo.backend.TMDB.Service service;
    private final MediaService mediaService;
    private final ImageService imageService;

    private final ConcurrentHashMap<Integer, Object> locks = new ConcurrentHashMap<>();

    public PersonService(PersonRepository personRepository, com.cinovo.backend.TMDB.Service service, @Lazy MediaService mediaService,
            @Lazy ImageService imageService)
    {
        this.personRepository = personRepository;
        this.service = service;
        this.mediaService = mediaService;
        this.imageService = imageService;
    }

    public void saveOrUpdate(final Person person)
    {
        this.personRepository.save(person);
    }

    public Person findByTmdbId(final Integer tmdb_id) throws Exception
    {
        Optional<Person> person = personRepository.findByTmdbId(tmdb_id);
        if(person.isPresent())
        {
            return person.get();
        }
        return (Person) this.onConvertTMDB(tmdb_id);
    }

    public List<Person> getPeoplePopularity(final Integer page) throws Exception
    {
        return (List<Person>) this.onConvertTMDB(MediaType.POPULARITY + Shared.REGEX + page);
    }

    public List<Person> getPersonUsingSearch(final String query, final Boolean include_adult, final String language, final Integer page)
            throws Exception
    {
        return (List<Person>) this.onConvertTMDB(
                MediaType.SEARCH + Shared.REGEX + query + Shared.REGEX + include_adult + Shared.REGEX + language + Shared.REGEX + page);
    }

    public List<Person> getPersonUsingTrending(final String time_window, final String language) throws Exception
    {
        return (List<Person>) this.onConvertTMDB(MediaType.TRENDING + Shared.REGEX + time_window + Shared.REGEX + language);
    }

    @Override
    public Object onConvertTMDB(Object obj) throws Exception
    {
        String[] parts = Shared.onSplitObject(obj);

        if(obj instanceof Integer)
        {
            return this.generatePerson((Integer) obj, null);
        }
        List<Person> persons = new ArrayList<>();
        if(MediaType.valueOf(parts[0]) == MediaType.POPULARITY)
        {
            PeoplePopularResponse peoplePopularResponse = this.service.getPeoplePopular("en-US", Shared.onStringParseToInteger(parts[1]));
            for(PeopleResponse person : peoplePopularResponse.getResults())
            {
                Person per = this.generatePerson(person.getId(), person.getKnown_for());
                persons.add(per);
            }
        }
        else if(MediaType.valueOf(parts[0]) == MediaType.SEARCH)
        {
            SearchResponse<?> rawResponse =
                    this.service.getSearchPeopleResponse(Shared.onStringEqualsWithNull(parts[1]), Boolean.parseBoolean(parts[2]),
                            Shared.onStringEqualsWithNull(parts[3]), Integer.parseInt(parts[4]));

            persons = this.onParseSearchResponseToPeopleResponse(rawResponse);
        }
        else if(MediaType.valueOf(parts[0]) == MediaType.TRENDING)
        {
            SearchResponse<?> rawResponse =
                    this.service.getTrendingPeopleResponse(Shared.onStringEqualsWithNull(parts[1]), Shared.onStringEqualsWithNull(parts[2]));

            persons = this.onParseSearchResponseToPeopleResponse(rawResponse);
        }
        log.error("Type case is not foud: " + obj);

        this.personRepository.saveAll(persons);
        return persons;
    }

    @Transactional
    protected Person generatePerson(final Integer id, List<MediaResponse> knownForMedias) throws Exception
    {
        //fixed the connection to not give errors
        PeopleResponse peopleResponse = this.service.getPeopleDetail(id, "en-US");
        PersonExternalIdsResponse personExternalIdsResponse = this.service.getPersonExternalIds(id);
        if(peopleResponse.getId() == null)
        {
            return null;
        }

        Person per = personRepository.findByTmdbId(id).orElse(new Person());
        per.setTmdb_id(peopleResponse.getId());
        per.setAdult(peopleResponse.getAdult());
        per.setGender(Gender.fromCode(peopleResponse.getGender()));
        per.setKnown_for_department(peopleResponse.getKnown_for_department());
        per.setName(peopleResponse.getName());
        per.setPopularity(peopleResponse.getPopularity());
        per.setProfile_file(peopleResponse.getProfile_path());
        per.setOriginal_name(peopleResponse.getOriginal_name());
        per.setAlso_known_as(peopleResponse.getAlso_known_as());
        per.setBiography(peopleResponse.getBiography());
        per.setBirthday(Shared.onStringParseDate(peopleResponse.getBirthday()));
        per.setDeathday(Shared.onStringParseDate(peopleResponse.getDeathday()));
        per.setHomepage(peopleResponse.getHomepage());
        per.setPlace_of_birth(peopleResponse.getPlace_of_birth());

        per.setFreebase_mid(personExternalIdsResponse.getFreebase_mid());
        per.setImdb_id(personExternalIdsResponse.getImdb_id());
        per.setFreebase_id(personExternalIdsResponse.getFreebase_id());
        per.setTvrage_id(personExternalIdsResponse.getTvrage_id());
        per.setWikidata_id(personExternalIdsResponse.getWikidata_id());
        per.setFacebook_id(personExternalIdsResponse.getFacebook_id());
        per.setTwitter_id(personExternalIdsResponse.getTwitter_id());
        per.setYoutube_id(personExternalIdsResponse.getYoutube_id());
        per.setInstagram_id(personExternalIdsResponse.getInstagram_id());
        per.setTiktok_id(personExternalIdsResponse.getTiktok_id());

        List<Image> images = imageService.findByTmdbIdAndMediaType(id, MediaType.PERSON);
        if(per.getImages() == null)
        {
            per.setImages(new ArrayList<>());
        }
        per.getImages().clear();

        for(Image img : images)
        {
            img.setPerson(per);
            per.getImages().add(img);
        }

        return this.personRepository.save(per);
    }

    //    @Transactional
    //    private Person generatePerson(final Integer id, List<MediaResponse> know_for) throws Exception
    //    {
    //        PeopleResponse peopleResponse = this.service.getPeopleDetail(id, "en-US");
    //        PersonExternalIdsResponse personExternalIdsResponse = this.service.getPersonExternalIds(id);
    //        if(peopleResponse.getId() == null)
    //            return null;
    //
    //        Person per = this.personRepository.findPersonById(id).orElse(new Person());
    //        per.setAdult(peopleResponse.getAdult());
    //        per.setGender(Gender.fromCode(peopleResponse.getGender()));
    //        per.setId(peopleResponse.getId());
    //        per.setKnown_for_department(peopleResponse.getKnown_for_department());
    //        per.setName(peopleResponse.getName());
    //        per.setPopularity(peopleResponse.getPopularity());
    //        per.setProfile_file(peopleResponse.getProfile_path());
    //        per.setOriginal_name(peopleResponse.getOriginal_name());
    //        per.setAlso_known_as(peopleResponse.getAlso_known_as());
    //        per.setBiography(peopleResponse.getBiography());
    //        this.personRepository.save(per);
    //
    //        per.setBirthday(peopleResponse.getBirthday() != null && !peopleResponse.getBirthday().isEmpty()
    //                ? LocalDate.parse(peopleResponse.getBirthday())
    //                : null);
    //        per.setDeathday(peopleResponse.getDeathday() != null && !peopleResponse.getDeathday().isEmpty()
    //                ? LocalDate.parse(peopleResponse.getDeathday())
    //                : null);
    //        per.setHomepage(peopleResponse.getHomepage());
    //        per.setPlace_of_birth(peopleResponse.getPlace_of_birth());
    //        per.setFreebase_mid(personExternalIdsResponse.getFreebase_mid());
    //        per.setImdb_id(personExternalIdsResponse.getImdb_id());
    //        per.setFreebase_id(personExternalIdsResponse.getFreebase_id());
    //        per.setTvrage_id(personExternalIdsResponse.getTvrage_id());
    //        per.setWikidata_id(personExternalIdsResponse.getWikidata_id());
    //        per.setFacebook_id(personExternalIdsResponse.getFacebook_id());
    //        per.setTwitter_id(personExternalIdsResponse.getTwitter_id());
    //        per.setYoutube_id(personExternalIdsResponse.getYoutube_id());
    //        per.setInstagram_id(personExternalIdsResponse.getInstagram_id());
    //        per.setTiktok_id(personExternalIdsResponse.getTiktok_id());
    //
    //        per.setImages(this.imageService.findImageByMediaIdAndMediaType(id, MediaType.PERSON));

    /// /        if(know_for != null) /        { /            List<Media> updatedMedias = new ArrayList<>(); /            for(MediaResponse
    /// mediaResponse : know_for) /            { /                Media media = / this.mediaService.getMediaByIdAndType(mediaResponse.getId(),
    /// MediaType.valueOf(mediaResponse.getMedia_type().toUpperCase())); / updatedMedias.add(media); /            } /            List<Media>
    /// currentMedias = per.getMedias(); /            if(currentMedias == null) / { /                per.setMedias(updatedMedias); /            } /
    /// else /            { /                List<Media> toRemove = new ArrayList<>(); /                for(Media oldMedia : currentMedias) / { /
    /// if(!updatedMedias.contains(oldMedia)) /                    { /                        toRemove.add(oldMedia); / } / } /
    /// currentMedias.removeAll(toRemove); / /                for(Media newMedia : updatedMedias) /                { /
    /// if(!currentMedias.contains(newMedia)) /                    { /                        currentMedias.add(newMedia); / } /                } /
    /// per.setMedias(currentMedias); /            } /        }
    //
    //        this.personRepository.save(per);
    //        return per;
    //    }
    private List<Person> onParseSearchResponseToPeopleResponse(final SearchResponse<?> rawResponse) throws Exception
    {
        ObjectMapper mapper = new ObjectMapper();
        List<Person> peoples = new ArrayList<>();
        for(Object object : rawResponse.getResults())
        {
            PeopleResponse mr = mapper.convertValue(object, PeopleResponse.class);
            Person per = this.generatePerson(mr.getId(), mr.getKnown_for());
            peoples.add(per);
        }

        return peoples;
    }
}
