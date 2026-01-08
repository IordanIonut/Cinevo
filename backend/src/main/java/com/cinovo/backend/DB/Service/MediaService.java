package com.cinovo.backend.DB.Service;

import com.cinovo.backend.DB.Model.*;
import com.cinovo.backend.DB.Model.Enum.EpisodeType;
import com.cinovo.backend.DB.Model.Enum.MediaStatus;
import com.cinovo.backend.DB.Model.Enum.MediaType;
import com.cinovo.backend.DB.Model.Enum.TimeWindow;
import com.cinovo.backend.DB.Repository.MediaRepository;
import com.cinovo.backend.DB.Util.Helper.JobHelper;
import com.cinovo.backend.DB.Util.Resolver.MediaResolver;
import com.cinovo.backend.DB.Util.Shared;
import com.cinovo.backend.DB.Util.TMDBLogically;
import com.cinovo.backend.Schedule.Job;
import com.cinovo.backend.TMDB.Response.*;
import com.cinovo.backend.TMDB.Response.Common.MediaExternalIdResponse;
import com.cinovo.backend.TMDB.Response.Common.MediaResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.jbosslog.JBossLog;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@JBossLog
@Service
public class MediaService implements TMDBLogically<Object, Object>
{
    private final MediaRepository mediaRepository;
    private final com.cinovo.backend.TMDB.Service service;
    private final GenreService genreService;
    private final PersonService personService;
    private final KeywordService keywordService;
    private final SpokenLanguageService spokenLanguageService;
    private final ProductionCountryService productionCountryService;
    private final CompanyService companyService;
    private final NetworkService networkService;
    private final CollectionService collectionService;
    private final CreditService creditService;
    private final MediaResolver mediaResolver;
    private final JobHelper jobHelper;

    public MediaService(MediaRepository mediaRepository, com.cinovo.backend.TMDB.Service service, GenreService genreService,
            PersonService personService, KeywordService keywordService, SpokenLanguageService spokenLanguageService,
            ProductionCountryService productionCountryService, CompanyService companyService, NetworkService networkService,
            CollectionService collectionService, CreditService creditService, MediaResolver mediaResolver, JobHelper jobHelper)
    {
        this.mediaRepository = mediaRepository;
        this.service = service;
        this.genreService = genreService;
        this.personService = personService;
        this.keywordService = keywordService;
        this.spokenLanguageService = spokenLanguageService;
        this.productionCountryService = productionCountryService;
        this.companyService = companyService;
        this.networkService = networkService;
        this.collectionService = collectionService;
        this.creditService = creditService;
        this.mediaResolver = mediaResolver;
        this.jobHelper = jobHelper;
    }

    public Media.Season getSeasonBySeasonTmdbId(final Integer season_tmdb_id)
    {
        Optional<Media.Season> season = this.mediaRepository.getSeasonBySeasonTmdbId(season_tmdb_id);
        if(season.isEmpty())
        {
            return new Media.Season();
        }
        return season.get();
    }

    public Media.Season.Episode findEpisodeByTmdbId(final Integer episode_id)
    {
        Optional<Media.Season.Episode> episode = this.mediaRepository.findEpisodeByTmdbId(episode_id);
        if(episode.isEmpty())
        {
            return new Media.Season.Episode();
        }
        return episode.get();
    }

    public List<Media> findMediaByDiscovery(final MediaType mediaType) throws Exception
    {
        return (List<Media>) this.onConvertTMDB(MediaType.DISCOVER.name() + Shared.REGEX + mediaType.name());
    }

    public List<Media> findRecommendationByIdAndType(final Integer id, final MediaType mediaType) throws Exception
    {
        return (List<Media>) this.onConvertTMDB(MediaType.RECOMMENDATION.name() + Shared.REGEX + id + Shared.REGEX + mediaType);
    }

    public List<Media> findMediaBySimilar(final Integer id, final MediaType mediaType) throws Exception
    {
        return (List<Media>) this.onConvertTMDB(MediaType.SIMILAR.name() + Shared.REGEX + id + Shared.REGEX + mediaType);
    }

    public Media getMediaByTmdbIdAndMediaType(final Integer id, final MediaType type) throws Exception
    {
        Optional<Media> movie = this.mediaRepository.getMediaByTmdbIdAndMediaType(id, type.name());
        if(movie.isEmpty())
        {
            return (Media) onConvertTMDB(type + Shared.REGEX + id);
        }
        return movie.get();
    }

    @Transactional
    public Media saveAndUpdate(final Media media)
    {
        this.mediaRepository.save(media);
        return media;
    }

    public Media.Season getSeasonByMediaTmdbIdAndSeasonNumber(final Integer media_tmdb_id, final Integer season_number) throws Exception
    {
        Optional<Media.Season> season = this.mediaRepository.getSeasonByMediaTmdbIdAndSeasonNumber(media_tmdb_id, season_number);
        if(season.isEmpty())
        {
            return (Media.Season) onConvertTMDB(MediaType.TV + Shared.REGEX + media_tmdb_id);
        }
        return season.get();
    }

    public Media.Season.Episode getEpisodeByMediaTmdbIdAndSeasonNumberAndEpisodeNumber(final Integer media_tmdb_id, final Integer season_number,
            final Integer episode_number) throws Exception
    {
        Optional<Media.Season.Episode> episode =
                this.mediaRepository.getEpisodeByMediaTmdbIdAndSeasonNumberAndEpisodeNumber(media_tmdb_id, season_number, episode_number);
        if(episode.isEmpty())
        {
            return (Media.Season.Episode) onConvertTMDB(MediaType.TV + Shared.REGEX + media_tmdb_id);
        }
        return episode.get();
    }

    public List<Media> getMediaUsingSearch(final MediaType type, final String query, final Boolean include_adult, final String language,
            final String primary_release_year, final Integer page, final String region, final Integer year) throws Exception
    {
        return (List<Media>) this.onConvertTMDB(
                MediaType.SEARCH + Shared.REGEX + type + Shared.REGEX + query + Shared.REGEX + include_adult + Shared.REGEX + language + Shared.REGEX
                        + primary_release_year + Shared.REGEX + page + Shared.REGEX + region + Shared.REGEX + year);
    }

    public MediaType findFirstMediaTypeByName(final String title, final String original_title, final Integer id) throws Exception
    {
        for(MediaType type : new MediaType[] { MediaType.MOVIE, MediaType.TV })
        {
            Media media = this.getMediaByTmdbIdAndMediaType(id, type);
            if(media != null && (title.equals(media.getTitle()) || original_title.equals(media.getOriginal_title())))
            {
                return media.getType();
            }
        }
        return null;
    }

    public List<Media> getMediaUsingTrending(final MediaType type, final TimeWindow time_window, final String language) throws Exception
    {
        return (List<Media>) this.onConvertTMDB(
                MediaType.TRENDING + Shared.REGEX + type + Shared.REGEX + time_window.getLabel() + Shared.REGEX + language);
    }

    @Override
    public Object onConvertTMDB(Object obj) throws Exception
    {
        String[] parts = Shared.onSplitObject(obj);
        if((MediaType.valueOf(parts[0]) == MediaType.MOVIE || MediaType.valueOf(parts[0]) == MediaType.TV
                || MediaType.valueOf((parts[0])) == MediaType.TV_SEASON) && Shared.onStringParseToInteger(parts[1]) != null)
        {
            return onConvertMediaById(Integer.parseInt(parts[1]), MediaType.valueOf(parts[0]));
        }
        else if(MediaType.valueOf(parts[0]) == MediaType.RECOMMENDATION)
        {
            SearchResponse<?> rawResponse = this.service.getMediaRecommendation(Integer.parseInt(parts[1]), MediaType.valueOf(parts[2]), "en-US", 1);
            return onConvertMedia(this.onParseSearchResponseToMediaResponse(rawResponse), MediaType.valueOf(parts[2]));
        }
        else if(MediaType.valueOf(parts[0]) == MediaType.DISCOVER)
        {
            SearchResponse<?> rawResponse =
                    MediaType.valueOf(parts[1]) == MediaType.MOVIE ? this.service.getDiscoverMovie() : this.service.getDiscoverTV();
            return onConvertMedia(this.onParseSearchResponseToMediaResponse(rawResponse), MediaType.valueOf(parts[1]));
        }
        else if(MediaType.valueOf(parts[0]) == MediaType.SIMILAR)
        {
            SearchResponse<?> rawRowResponse = this.service.getMediaSimilar(Integer.parseInt(parts[1]), MediaType.valueOf(parts[2]), "en-US", 1);
            return onConvertMedia(this.onParseSearchResponseToMediaResponse(rawRowResponse), MediaType.valueOf(parts[2]));
        }
        else if(MediaType.valueOf(parts[0]) == MediaType.SEARCH)
        {
            SearchResponse<?> rawResponse =
                    this.service.getSearchMediaResponse(parts[1].equals("null") ? null : MediaType.valueOf(parts[1].toUpperCase()),
                            Shared.onStringEqualsWithNull(parts[2]), Boolean.parseBoolean(parts[3]), Shared.onStringEqualsWithNull(parts[4]),
                            Shared.onStringEqualsWithNull(parts[5]), Integer.parseInt(parts[6]), Shared.onStringEqualsWithNull(parts[7]),
                            Shared.onStringParseToInteger(parts[8]));

            return this.onConvertMedia(this.onParseSearchResponseToMediaResponse(rawResponse),
                    parts[1].equals("null") ? null : MediaType.valueOf(parts[1].toUpperCase()));
        }
        else if(MediaType.valueOf(parts[0]) == MediaType.TRENDING)
        {
            SearchResponse<?> rawResponse =
                    this.service.getTrendingMediaResponse(parts[1].equals("null") ? null : MediaType.valueOf(parts[1].toUpperCase()),
                            Shared.onStringEqualsWithNull(parts[2]), Shared.onStringEqualsWithNull(parts[3]));

            List<MediaResponse> responses = this.onParseSearchResponseToMediaResponse(rawResponse);
            this.callJobHelper(responses, Shared.onStringEqualsWithNull(parts[1]), Shared.onStringEqualsWithNull(parts[2]));

            return this.onConvertMedia(responses, parts[1].equals("null") ? null : MediaType.valueOf(parts[1].toUpperCase()));
        }

        throw new IllegalArgumentException("Unsupported input type");
    }

    private List<Media> onConvertMedia(List<MediaResponse> mediaResponse, MediaType type) throws Exception
    {
        List<Media> medias = new ArrayList<>();
        for(MediaResponse result : mediaResponse)
        {
            Media movie = this.onConvertMediaById(result.getId(), type == null ? MediaType.valueOf(result.getMedia_type().toUpperCase()) : type);
            medias.add(movie);
        }

        this.mediaRepository.saveAll(medias);
        return medias;
    }

    @Transactional
    protected Media onConvertMediaById(final Integer id, final MediaType type) throws Exception
    {
        MediaResponse mediaResponse = type.equals(MediaType.MOVIE) ? service.getMovieDetails(id, "en-US") : service.getTvDetails(id, null, null);

        if(mediaResponse.getId() == null)
            return null;

        MediaKeywordResponse mediaKeywordResponse = type.equals(MediaType.MOVIE) ? service.getKeywordMovie(id) : service.getTvKeyword(id);

        Media media = mediaRepository.getMediaByTmdbIdAndMediaType(id, type.name()).orElse(new Media());
        media.setTmdb_id(mediaResponse.getId());
        media.setType(type != null ? type : MediaType.valueOf(mediaResponse.getMedia_type().toUpperCase()));
        media.setAdult(mediaResponse.getAdult());
        media.setBackdrop_path(mediaResponse.getBackdrop_path());
        media.setBudget(mediaResponse.getBudget());
        media.setHomepage(mediaResponse.getHomepage());
        saveAndUpdate(media);

        if(mediaKeywordResponse.getKeywords() != null)
        {
            List<Keyword> keywords = mediaKeywordResponse.getKeywords().stream().map(k -> {
                try
                {
                    return keywordService.findByTmdbId(k.getId());
                }
                catch(Exception e)
                {
                    throw new RuntimeException(e);
                }
            }).collect(Collectors.toList());
            media.setKeywords(keywords);
        }

        media.setOrigin_country(mediaResponse.getOrigin_country());
        media.setOriginal_language(mediaResponse.getOriginal_language());
        media.setOriginal_title(mediaResponse.getOriginal_title());
        media.setLanguages(mediaResponse.getLanguages());
        media.setEpisode_run_time(mediaResponse.getEpisode_run_time());
        media.setOverview(mediaResponse.getOverview());
        media.setPopularity(mediaResponse.getPopularity());
        media.setPoster_path(mediaResponse.getPoster_path());
        media.setRelease_date(Shared.onStringParseDate(mediaResponse.getRelease_date()));
        media.setFirst_air_date(Shared.onStringParseDate(mediaResponse.getFirst_air_date()));
        media.setRevenue(mediaResponse.getRevenue());
        media.setRuntime(mediaResponse.getRuntime());
        media.setStatus(MediaStatus.fromLabel(mediaResponse.getStatus()));
        media.setTagline(mediaResponse.getTagline());
        media.setTitle(mediaResponse.getTitle());
        media.setVideo(mediaResponse.getVideo());
        media.setVote_average(mediaResponse.getVote_average());
        media.setVote_count(mediaResponse.getVote_count());
        this.saveAndUpdate(media);

        if(mediaResponse.getBelongs_to_collection() != null)
        {
            media.setBelong_to_collection(this.collectionService.findByTmdbId(mediaResponse.getBelongs_to_collection().getId()));
        }

        if(mediaResponse.getProduction_countries() != null)
        {
            List<ProductionCountry> productionCountries = new ArrayList<>();
            for(MediaResponse.ProductionCountry country : mediaResponse.getProduction_countries())
            {
                ProductionCountry productionCountry = this.productionCountryService.getByIso(country.getIso_3166_1());
                productionCountry.setIso_3166_1(country.getIso_3166_1());
                productionCountry.setName(country.getName());
                productionCountries.add(productionCountry);
            }
            media.setProduction_countries(productionCountries);
        }

        if(mediaResponse.getProduction_companies() != null)
        {
            List<Company> productionCompanies = new ArrayList<>();
            for(MediaResponse.ProductionCompany company : mediaResponse.getProduction_companies())
            {
                productionCompanies.add(this.companyService.findCompanyByTmdbId(company.getId()));
            }
            media.setProduction_companies(productionCompanies);
        }

        if(mediaResponse.getSpoken_languages() != null)
        {
            List<SpokenLanguage> spokenLanguages = new ArrayList<>();
            for(MediaResponse.SpokenLanguage language : mediaResponse.getSpoken_languages())
            {
                SpokenLanguage spokenLanguage = this.spokenLanguageService.getByIso(language.getIso_639_1());
                spokenLanguage.setEnglish_name(language.getEnglish_name());
                spokenLanguage.setIso_639_1(language.getIso_639_1());
                spokenLanguage.setName(language.getName());
                spokenLanguages.add(spokenLanguage);
            }
            media.setSpoken_languages(spokenLanguages);
        }

        if(mediaResponse.getNetworks() != null)
        {
            List<Network> networks = new ArrayList<>();
            for(MediaResponse.Network network : mediaResponse.getNetworks())
            {
                networks.add(this.networkService.getNetworkByTmdbId(network.getId()));
            }
            media.setNetworks(networks);
        }

        if(mediaResponse.getGenre_ids() != null)
        {
            media.setGenres(genreService.parsLongToObjects(mediaResponse.getGenre_ids(), media.getType()));
        }
        else if(mediaResponse.getGenres() != null)
        {
            media.setGenres(genreService.parsGenreToObjects(mediaResponse.getGenres(), media.getType()));
        }
        this.saveAndUpdate(media);

        if(mediaResponse.getSeasons() != null)
        {
            List<Media.Season> seasons = new ArrayList<>();
            for(MediaResponse.Season seasonDto : mediaResponse.getSeasons())
            {
                Media.Season ses = this.mediaRepository.getSeasonBySeasonTmdbId(seasonDto.getId()).orElse(new Media.Season());

                ses.setTmdb_id(seasonDto.getId());
                ses.setAir_date(Shared.onStringParseDate(seasonDto.getAir_date()));
                ses.setEpisode_count(seasonDto.getEpisode_count());
                ses.setName(seasonDto.getName());
                ses.setOverview(seasonDto.getOverview());
                ses.setPoster_path(seasonDto.getPoster_path());
                ses.setSeason_number(seasonDto.getSeason_number());
                ses.setVote_average(seasonDto.getVote_average());

                List<Media.Season.Episode> episodes = new ArrayList<>();
                TvSeasonDetailsResponse tvSeasonDetailsResponse = this.service.getTvSeasonDetails(id, ses.getSeason_number(), null, null);
                for(TvSeasonDetailsResponse.Episode episode : tvSeasonDetailsResponse.getEpisodes())
                {
                    Media.Season.Episode epi = this.mediaRepository.getEpisodeByEpisodeTmdbId(episode.getId()).orElse(new Media.Season.Episode());

                    epi.setTmdb_id(episode.getId());
                    epi.setAir_date(Shared.onStringParseDate(episode.getAir_date()));
                    epi.setEpisode_number(episode.getEpisode_number());
                    epi.setEpisode_type(EpisodeType.valueOf(episode.getEpisode_type().toUpperCase()));
                    epi.setOverview(episode.getOverview());
                    epi.setProduction_code(episode.getProduction_code());
                    epi.setRuntime(episode.getRuntime());
                    epi.setStill_path(episode.getStill_path());
                    epi.setVote_average(episode.getVote_average());
                    epi.setVote_count(episode.getVote_count());
                    epi.setSeason(ses);

                    episodes.add(epi);
                }
                ses.setEpisodes(episodes);
                ses.setMedia(media);
                seasons.add(ses);
            }
            media.setSeasons(seasons);
        }
        this.saveAndUpdate(media);

        if(mediaResponse.getLast_episode_to_air() != null)
        {
            Media.EpisodeToAir episodeToAir =
                    this.mediaRepository.getEpisodeToAirByTmdbId(mediaResponse.getLast_episode_to_air().getId()).orElse(new Media.EpisodeToAir());
            episodeToAir.setTmdb_id(mediaResponse.getLast_episode_to_air().getId());
            episodeToAir.setName(mediaResponse.getLast_episode_to_air().getName());
            episodeToAir.setOverview(mediaResponse.getLast_episode_to_air().getOverview());
            episodeToAir.setVote_average(mediaResponse.getLast_episode_to_air().getVote_average());
            episodeToAir.setVote_count(mediaResponse.getLast_episode_to_air().getVote_count());
            episodeToAir.setAir_date(mediaResponse.getLast_episode_to_air().getAir_date() != null ? LocalDate.parse(
                    mediaResponse.getLast_episode_to_air().getAir_date()) : null);
            episodeToAir.setEpisode_number(mediaResponse.getLast_episode_to_air().getEpisode_number());
            episodeToAir.setEpisode_type(EpisodeType.valueOf(mediaResponse.getLast_episode_to_air().getEpisode_type().toUpperCase()));
            episodeToAir.setProduction_code(mediaResponse.getLast_episode_to_air().getProduction_code());
            episodeToAir.setRuntime(mediaResponse.getLast_episode_to_air().getRuntime());
            episodeToAir.setSeason_number(mediaResponse.getLast_episode_to_air().getSeason_number());
            episodeToAir.setStill_path(mediaResponse.getLast_episode_to_air().getStill_path());
            episodeToAir.setMedia(media);

            media.setLast_episode_to_air(episodeToAir);
        }

        if(mediaResponse.getNext_episode_to_air() != null)
        {
            Media.EpisodeToAir episodeToAir =
                    this.mediaRepository.getEpisodeToAirByTmdbId(mediaResponse.getNext_episode_to_air().getId()).orElse(new Media.EpisodeToAir());
            episodeToAir.setTmdb_id(mediaResponse.getNext_episode_to_air().getId());
            episodeToAir.setName(mediaResponse.getNext_episode_to_air().getName());
            episodeToAir.setOverview(mediaResponse.getNext_episode_to_air().getOverview());
            episodeToAir.setVote_average(mediaResponse.getNext_episode_to_air().getVote_average());
            episodeToAir.setVote_count(mediaResponse.getNext_episode_to_air().getVote_count());
            episodeToAir.setAir_date(mediaResponse.getNext_episode_to_air().getAir_date() != null ? LocalDate.parse(
                    mediaResponse.getNext_episode_to_air().getAir_date()) : null);
            episodeToAir.setEpisode_number(mediaResponse.getNext_episode_to_air().getEpisode_number());
            episodeToAir.setEpisode_type(EpisodeType.valueOf(mediaResponse.getNext_episode_to_air().getEpisode_type().toUpperCase()));
            episodeToAir.setProduction_code(mediaResponse.getNext_episode_to_air().getProduction_code());
            episodeToAir.setRuntime(mediaResponse.getNext_episode_to_air().getRuntime());
            episodeToAir.setSeason_number(mediaResponse.getNext_episode_to_air().getSeason_number());
            episodeToAir.setStill_path(mediaResponse.getNext_episode_to_air().getStill_path());
            episodeToAir.setMedia(media);

            media.setNext_episode_to_air(episodeToAir);
        }

        List<Person> persons = new ArrayList<>();
        if(type.equals(MediaType.TV))
        {
            for(MediaResponse.CreatedBy createdBy : mediaResponse.getCreated_by())
            {
                Person person = this.personService.findByTmdbId(createdBy.getId());
                if(person.getMedias() == null)
                {
                    person.setMedias(new ArrayList<>());
                }

                if(person.getMedias().stream().noneMatch(m -> m.getTmdb_id().equals(media.getTmdb_id())))
                {
                    person.getMedias().add(media);
                }

                persons.add(person);
            }
        }
        this.saveAndUpdate(media);

        media.setCreated_by(persons);
        this.mediaResolver.generateDateAsync(id, type);

        return saveAndUpdate(media);
    }

    private List<MediaResponse> onParseSearchResponseToMediaResponse(final SearchResponse<?> rawResponse)
    {
        ObjectMapper mapper = new ObjectMapper();
        List<MediaResponse> mediaResponses = new ArrayList<>();
        for(Object object : rawResponse.getResults())
        {
            MediaResponse mr = mapper.convertValue(object, MediaResponse.class);
            mediaResponses.add(mr);
        }

        return mediaResponses;
    }

    private void callJobHelper(List<MediaResponse> responses, String media_type, String window)
    {
        List<Integer> ids = responses.stream().map(MediaResponse::getId).toList();
        MediaType type = MediaType.valueOf(media_type.toUpperCase());
        TimeWindow time_window = TimeWindow.valueOf(window.toUpperCase());
        this.jobHelper.updateTrendingList(type, time_window, ids);
    }
}
