package com.cinovo.backend.DB.Service;

import com.cinovo.backend.DB.Model.Enum.Gender;
import com.cinovo.backend.DB.Model.Enum.MediaType;
import com.cinovo.backend.DB.Model.Enum.TimeWindow;
import com.cinovo.backend.DB.Model.Person;
import com.cinovo.backend.DB.Model.View.MediaView;
import com.cinovo.backend.DB.Model.View.PersonView;
import com.cinovo.backend.DB.Repository.PersonRepository;
import com.cinovo.backend.DB.Util.Helper.JobHelper;
import com.cinovo.backend.DB.Util.Shared;
import com.cinovo.backend.DB.Util.TMDBLogically;
import com.cinovo.backend.Schedule.Job;
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
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@JBossLog
public class PersonService implements TMDBLogically<Object, Object>
{
    private final PersonRepository personRepository;
    private final com.cinovo.backend.TMDB.Service service;
    private final JobHelper jobHelper;
    private final MediaService mediaService;
    private final ImageService imageService;
    private final ExternalService externalService;

    public PersonService(PersonRepository personRepository, com.cinovo.backend.TMDB.Service service, JobHelper jobHelper,
            @Lazy MediaService mediaService, @Lazy ImageService imageService, ExternalService externalService)
    {
        this.personRepository = personRepository;
        this.service = service;
        this.jobHelper = jobHelper;
        this.mediaService = mediaService;
        this.imageService = imageService;
        this.externalService = externalService;
    }

    public void updateOrInsertPersonMedia(final String media_cinevo_id, final String person_cinevo_id)
    {
        this.personRepository.updateOrInsertPersonMedia(media_cinevo_id, person_cinevo_id);
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

    public List<PersonView> getPersonUsingTrending(final TimeWindow time_window, final String language) throws Exception
    {
        Optional<List<PersonView>> persons = this.personRepository.getPersonUsingTrending(Job.configurationUrlImages,
                this.jobHelper.getJobList(MediaType.PERSON, time_window, null));
        if(persons.isEmpty() || persons.get().isEmpty())
        {
            this.onConvertTMDB(MediaType.TRENDING + Shared.REGEX + time_window.name().toLowerCase() + Shared.REGEX + language);
            return null;
        }
        return persons.get();
    }

    @Override
    public Object onConvertTMDB(Object obj) throws Exception
    {
        String[] parts = Shared.onSplitObject(obj);

        if(obj instanceof Integer)
        {
            return this.generatePerson((Integer) obj);
        }
        List<Person> persons = new ArrayList<>();
        switch(MediaType.valueOf(parts[0]))
        {
            case MediaType.POPULARITY ->
            {
                PeoplePopularResponse peoplePopularResponse = this.service.getPeoplePopular("en-US", Shared.onStringParseToInteger(parts[1]));
                for(PeopleResponse person : peoplePopularResponse.getResults())
                {
                    Person per = this.generatePerson(person.getId());
                    persons.add(per);
                }
            }
            case MediaType.SEARCH ->
            {
                SearchResponse<?> rawResponse =
                        this.service.getSearchPeopleResponse(Shared.onStringEqualsWithNull(parts[1]), Boolean.parseBoolean(parts[2]),
                                Shared.onStringEqualsWithNull(parts[3]), Integer.parseInt(parts[4]));

                persons = this.onParseSearchResponseToPeopleResponse(rawResponse);
            }
            case MediaType.TRENDING ->
            {
                SearchResponse<?> rawResponse =
                        this.service.getTrendingPeopleResponse(Shared.onStringEqualsWithNull(parts[1]), Shared.onStringEqualsWithNull(parts[2]));

                persons = this.onParseSearchResponseToPeopleResponse(rawResponse);

                List<Integer> ids = persons.stream().map(Person::getTmdb_id).toList();
                TimeWindow time_window = TimeWindow.valueOf(Shared.onStringEqualsWithNull(parts[1].toUpperCase()));
                this.jobHelper.updateJobList(MediaType.PERSON, time_window, ids, null);
            }
            default ->
            {
                log.error("Type case is not foud: " + obj);
                return null;
            }
        }

        return persons;
    }

    @Transactional
    protected Person generatePerson(final Integer id) throws Exception
    {
        PeopleResponse peopleResponse = this.service.getPeopleDetail(id, "en-US");
        if(peopleResponse.getId() == null)
        {
            return null;
        }

        String person_cinevo_id = Shared.generateCinevoId(personRepository.findByTmdbId(id));
        this.personRepository.updateOrInsertPerson(person_cinevo_id, peopleResponse.getId(), peopleResponse.getAdult(),
                Gender.fromCode(peopleResponse.getGender()).name(), peopleResponse.getKnown_for_department(), peopleResponse.getOriginal_name(),
                peopleResponse.getName(), peopleResponse.getProfile_path(), peopleResponse.getPopularity(), peopleResponse.getBiography(),
                Shared.onStringParseDate(peopleResponse.getBirthday()), Shared.onStringParseDate(peopleResponse.getDeathday()),
                peopleResponse.getHomepage(), peopleResponse.getPlace_of_birth());

        this.externalService.setExternalByMediaType(peopleResponse.getId(), person_cinevo_id, MediaType.PERSON, null, null);

        for(String also_know_as : peopleResponse.getAlso_known_as())
        {
            this.personRepository.updateOrInsertPersonAlsoKnowAs(also_know_as, person_cinevo_id);
        }

        imageService.findByTmdbIdAndMediaType(id, MediaType.PERSON);
        return this.personRepository.findByTmdbId(peopleResponse.getId()).get();
    }

    private List<Person> onParseSearchResponseToPeopleResponse(final SearchResponse<?> rawResponse) throws Exception
    {
        ObjectMapper mapper = new ObjectMapper();
        List<Person> peoples = new ArrayList<>();
        for(Object object : rawResponse.getResults())
        {
            PeopleResponse mr = mapper.convertValue(object, PeopleResponse.class);
            Person per = this.generatePerson(mr.getId());
            peoples.add(per);
        }

        return peoples;
    }
}
