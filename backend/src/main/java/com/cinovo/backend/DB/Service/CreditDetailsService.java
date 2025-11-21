package com.cinovo.backend.DB.Service;

import com.cinovo.backend.DB.Model.CreditDetails;
import com.cinovo.backend.DB.Model.Person;
import com.cinovo.backend.DB.Repository.CreditDetailsRepository;
import com.cinovo.backend.DB.Util.TMDBLogically;
import com.cinovo.backend.Enum.Gender;
import com.cinovo.backend.Enum.MediaType;
import com.cinovo.backend.TMDB.Response.DetailsResponse;
import lombok.extern.jbosslog.JBossLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@JBossLog
@Service
public class CreditDetailsService implements TMDBLogically<String, CreditDetails>
{
    private final CreditDetailsRepository creditDetailsRepository;
    private final GenreService genreService;
    private final com.cinovo.backend.TMDB.Service service;

    public CreditDetailsService(CreditDetailsRepository creditDetailsRepository, GenreService genreService, com.cinovo.backend.TMDB.Service service)
    {
        this.creditDetailsRepository = creditDetailsRepository;
        this.service = service;
        this.genreService = genreService;
    }

    public CreditDetails findCreditDetailsById(final String id) throws Exception
    {
        Optional<CreditDetails> creditDetails = this.creditDetailsRepository.findCreditDetailsById(id);
        if(creditDetails.isEmpty())
        {
            return this.onConvertTMDB(id);
        }
        return creditDetails.get();
    }

    @Override
    public CreditDetails onConvertTMDB(String id) throws Exception
    {
        DetailsResponse detailsResponse = this.service.getCreditsDetails(id);
        CreditDetails creditDetails = new CreditDetails();
        creditDetails.setCredit_type(detailsResponse.getCredit_type());
        creditDetails.setDepartment(detailsResponse.getDepartment());
        creditDetails.setJob(detailsResponse.getJob());
        creditDetails.setType(MediaType.valueOf(detailsResponse.getMedia_type().toUpperCase()));
        creditDetails.setId(detailsResponse.getId());
        this.creditDetailsRepository.save(creditDetails);

        CreditDetails.Person person = new CreditDetails.Person();
        person.setMedia_type(MediaType.valueOf(detailsResponse.getPerson().getMedia_type().toUpperCase()));
        person.setCredit_details(creditDetails);

        Person per = new Person();
        per.setId(detailsResponse.getPerson().getId());
        per.setPopularity(detailsResponse.getPerson().getPopularity());
        per.setAdult(detailsResponse.getPerson().getAdult());
        per.setName(detailsResponse.getPerson().getName());
        per.setOriginal_name(detailsResponse.getPerson().getOriginal_name());
        per.setGender(Gender.fromCode(detailsResponse.getPerson().getGender()));
        per.setKnown_for_department(detailsResponse.getPerson().getKnown_for_department());
        per.setProfile_file(detailsResponse.getPerson().getProfile_path());
        person.setPerson(per);

        CreditDetails.Media media = new CreditDetails.Media();
        media.setAdult(detailsResponse.getMedia().getAdult());
        media.setId(detailsResponse.getMedia().getId());
        media.setName(detailsResponse.getMedia().getName());
        media.setOrigin_country(detailsResponse.getMedia().getOriginal_name());
        media.setOverview(detailsResponse.getMedia().getOverview());
        media.setOriginal_name(detailsResponse.getMedia().getOriginal_name());
        media.setBackdrop_path(detailsResponse.getMedia().getBackdrop_path());
        media.setPoster_path(detailsResponse.getMedia().getPoster_path());
        media.setType(MediaType.valueOf(detailsResponse.getMedia().getMedia_type().toUpperCase()));
        media.setOriginal_language(detailsResponse.getMedia().getOriginal_name());
        media.setGenres(this.genreService.parsLongToObjects(detailsResponse.getMedia().getGenre_ids(), media.getType()));
        media.setPopularity(detailsResponse.getMedia().getPopularity());
        media.setFirst_air_date(detailsResponse.getMedia().getFirst_air_date());
        media.setVote_average(detailsResponse.getMedia().getVote_average());
        media.setVote_count(detailsResponse.getMedia().getVote_count());
        media.setOrigin_country(detailsResponse.getMedia().getOriginal_name());
        media.setCharacter(detailsResponse.getMedia().getCharacter());
        media.setCredit_details(creditDetails);

        List<CreditDetails.Media.Episode> episodes = new ArrayList<>();
        for(DetailsResponse.Media.Episodes episode : detailsResponse.getMedia().getEpisodes())
        {
            CreditDetails.Media.Episode ep = new CreditDetails.Media.Episode();
            ep.setId(episode.getId());
            ep.setName(episode.getName());
            ep.setOverview(episode.getOverview());
            ep.setMedia_type(MediaType.valueOf(episode.getMedia_type().toUpperCase()));
            ep.setVote_average(episode.getVote_average());
            ep.setVote_count(episode.getVote_count());
            ep.setAir_date(episode.getAir_date() != null && !episode.getAir_date().isEmpty() ? LocalDate.parse(episode.getAir_date()) : null);
            ep.setEpisode_number(episode.getEpisode_number());
            ep.setEpisode_type(episode.getEpisode_type());
            ep.setProduction_code(episode.getProduction_code());
            ep.setRuntime(episode.getRuntime());
            ep.setSeason_number(episode.getSeason_number());
            ep.setShow_id(episode.getShow_id());
            ep.setStill_path(episode.getStill_path());
            ep.setMedia(media);
            episodes.add(ep);
        }

        List<CreditDetails.Media.Season> seasons = new ArrayList<>();
        for(DetailsResponse.Media.Seasons season : detailsResponse.getMedia().getSeasons())
        {
            CreditDetails.Media.Season se = new CreditDetails.Media.Season();
            se.setId(season.getId());
            se.setName(season.getName());
            se.setOverview(season.getOverview());
            se.setPoster_path(season.getPoster_path());
            se.setMedia_type(MediaType.valueOf(season.getMedia_type().toUpperCase()));
            se.setVote_average(season.getVote_average());
            se.setAir_date(season.getAir_date() != null && !season.getAir_date().isEmpty() ? LocalDate.parse(season.getAir_date()) : null);
            se.setSeason_number(season.getSeason_number());
            se.setShow_id(season.getShow_id());
            se.setEpisode_count(season.getEpisode_count());
            se.setMedia(media);
            seasons.add(se);
        }

        media.setSeasons(seasons);
        media.setEpisodes(episodes);
        creditDetails.setMedia(media);
        creditDetails.setPerson(person);
        this.creditDetailsRepository.save(creditDetails);
        return creditDetails;
    }
}
