package com.cinovo.backend.DB.Service;

import com.cinovo.backend.Config.LockManager;
import com.cinovo.backend.DB.Model.*;
import com.cinovo.backend.DB.Model.Enum.EpisodeType;
import com.cinovo.backend.DB.Model.Enum.MediaStatus;
import com.cinovo.backend.DB.Model.Enum.MediaType;
import com.cinovo.backend.DB.Model.Enum.TimeWindow;
import com.cinovo.backend.DB.Model.View.MediaView;
import com.cinovo.backend.DB.Repository.MediaRepository;
import com.cinovo.backend.DB.Util.Helper.JobHelper;
import com.cinovo.backend.DB.Util.Resolver.MediaResolver;
import com.cinovo.backend.DB.Util.Shared;
import com.cinovo.backend.DB.Util.TMDBLogically;
import com.cinovo.backend.Schedule.Job;
import com.cinovo.backend.TMDB.Response.*;
import com.cinovo.backend.TMDB.Response.Common.KeywordsResponse;
import com.cinovo.backend.TMDB.Response.Common.MediaResponse;
import com.cinovo.backend.Utils.RetryUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.jbosslog.JBossLog;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
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
    private final CountryService countryService;
    private final WatchProviderService watchProviderService;
    private final MediaResolver mediaResolver;
    private final JobHelper jobHelper;
    private final LockManager lockManager;

    public MediaService(MediaRepository mediaRepository, com.cinovo.backend.TMDB.Service service, GenreService genreService,
            PersonService personService, KeywordService keywordService, SpokenLanguageService spokenLanguageService,
            ProductionCountryService productionCountryService, CompanyService companyService, NetworkService networkService,
            CollectionService collectionService, CreditService creditService, CountryService countryService, WatchProviderService watchProviderService, MediaResolver mediaResolver,
            JobHelper jobHelper, LockManager lockManager)
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
        this.countryService = countryService;
        this.watchProviderService = watchProviderService;
        this.mediaResolver = mediaResolver;
        this.jobHelper = jobHelper;
        this.lockManager = lockManager;
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

    public List<MediaView> getMediaUsingTrending(final MediaType type, final TimeWindow time_window, final String language) throws Exception
    {
        Optional<List<MediaView>> medias =
                this.mediaRepository.getMediaUsingIds(Job.configurationUrlImages, type.name(), this.jobHelper.getJobList(type, time_window, null));
        if(medias.isEmpty() || medias.get().isEmpty())
        {
            this.onConvertTMDB(MediaType.TRENDING + Shared.REGEX + type + Shared.REGEX + time_window.getLabel() + Shared.REGEX + language);
            return null;
        }
        return medias.get();
    }

    public List<MediaView> getFreeToWatchByMediaType(final MediaType media_type, final Boolean isScheduleJob)
    {
        if(!isScheduleJob)
        {
            Optional<List<MediaView>> medias = this.mediaRepository.getMediaUsingIds(Job.configurationUrlImages, media_type.name(),
                    media_type == MediaType.MOVIE ? Job.freeToWatchMovie : Job.freeToWatchTV);
            return medias.get();
        }
        else
        {
            List<MediaView> medias = this.mediaRepository.getRandomByMediaType(Job.configurationUrlImages,media_type.name()).get();
            List<Integer> ids = medias.stream().map(MediaView::getTmdb_id).filter(Objects::nonNull).collect(Collectors.toList());
            this.jobHelper.updateJobList(MediaType.FREE_TO_WATCH, null, ids, media_type);

            return medias;
        }
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

        //        this.mediaRepository.saveAll(medias);
        return medias;
    }

    @Transactional()
    protected Media onConvertMediaById(final Integer id, final MediaType type) throws Exception
    {
        MediaResponse mediaResponse = type.equals(MediaType.MOVIE) ? service.getMovieDetails(id, "en-US") : service.getTvDetails(id, null, null);

        if(mediaResponse.getId() == null)
        {
            return null;
        }
        String media_cinevo_id = Shared.generateCinevoId(mediaRepository.getMediaByTmdbIdAndMediaType(id, type.name()));
        Integer media_tmdb_id = mediaResponse.getId();

        MediaKeywordResponse mediaKeywordResponse = type.equals(MediaType.MOVIE) ? service.getKeywordMovie(id) : service.getTvKeyword(id);
        MediaType mediaType = mediaResponse.getMedia_type() == null ? type : MediaType.valueOf(mediaResponse.getMedia_type().toUpperCase());

        String media_key = "MEDIA_" + mediaResponse.getId() + "_" + type.name();
        synchronized(lockManager.getLock(media_key))
        {
            RetryUtil.retry(
                    () -> this.mediaRepository.updateOrInsertMedia(media_cinevo_id, mediaResponse.getId(), mediaType.name(), mediaResponse.getAdult(),
                            mediaResponse.getBackdrop_path(), mediaResponse.getBudget(), mediaResponse.getHomepage(),
                            mediaResponse.getOriginal_language(), mediaResponse.getOriginal_title(), mediaResponse.getOverview(),
                            mediaResponse.getPopularity(), mediaResponse.getPoster_path(), Shared.onStringParseDate(mediaResponse.getRelease_date()),
                            Shared.onStringParseDate(mediaResponse.getFirst_air_date()), mediaResponse.getRevenue(), mediaResponse.getRuntime(),
                            MediaStatus.fromLabel(mediaResponse.getStatus()).name(), mediaResponse.getTagline(), mediaResponse.getTitle(),
                            mediaResponse.getVideo(), mediaResponse.getVote_average(), mediaResponse.getVote_count()));
        }

        if(mediaResponse.getLanguages() != null)
        {
            for(String key : mediaResponse.getLanguages())
            {
                this.mediaRepository.updateOrInsertLanguage(media_cinevo_id, key);
            }
        }

        this.watchProviderService.findByMediaTmdbIdAndMediaType(media_tmdb_id, type);


        if(mediaResponse.getEpisode_run_time() != null)
        {
            for(Integer time : mediaResponse.getEpisode_run_time())
            {
                this.mediaRepository.updateOrInsertEpisodeRunTime(media_cinevo_id, time);
            }
        }

        if(mediaKeywordResponse.getKeywords() != null)
        {
            for(KeywordsResponse keywordsResponse : mediaKeywordResponse.getKeywords())
            {
                this.mediaRepository.updateOrInsertMediaKeyword(media_cinevo_id,
                        this.keywordService.findByTmdbId(keywordsResponse.getId()).getCinevo_id());
            }
        }

        if(mediaResponse.getOrigin_country() != null)
        {
            for(String key : mediaResponse.getOrigin_country())
            {
                this.mediaRepository.updateOrInsertMediaOriginCountry(media_cinevo_id,
                        this.countryService.findCountryByMediaTypeAndCode(type, key).getCinevo_id());
            }
        }

        if(mediaResponse.getBelongs_to_collection() != null)
        {
            this.mediaRepository.updateBelongToCollectionId(media_cinevo_id,
                    this.collectionService.findByTmdbId(mediaResponse.getBelongs_to_collection().getId()).getCinevo_id());
        }

        if(mediaResponse.getProduction_companies() != null)
        {
            for(MediaResponse.ProductionCountry country : mediaResponse.getProduction_countries())
            {
                this.mediaRepository.updateOrInsertMediaProductionCountry(media_cinevo_id,
                        this.productionCountryService.getByIso(country.getIso_3166_1()).getCinevo_id());
            }
        }

        if(mediaResponse.getProduction_companies() != null)
        {
            for(MediaResponse.ProductionCompany company : mediaResponse.getProduction_companies())
            {
                this.mediaRepository.updateOrInsertMediaProductionCompany(media_cinevo_id,
                        this.companyService.findCompanyByTmdbId(company.getId()).getCinevo_id());
            }
        }

        if(mediaResponse.getSpoken_languages() != null)
        {
            for(MediaResponse.SpokenLanguage language : mediaResponse.getSpoken_languages())
            {
                this.mediaRepository.updateOrInsertMediaSpokenLanguage(media_cinevo_id,
                        this.spokenLanguageService.getByIso(language.getIso_639_1()).getCinevo_id());
            }
        }

        if(mediaResponse.getNetworks() != null)
        {
            for(MediaResponse.Network network : mediaResponse.getNetworks())
            {
                this.mediaRepository.updateOrInsertMediaNetwork(media_cinevo_id,
                        this.networkService.getNetworkByTmdbId(network.getId()).getCinevo_id());
            }
        }

        if(mediaResponse.getGenre_ids() != null)
        {
            for(Genre genre : this.genreService.parsLongToObjects(mediaResponse.getGenre_ids(), mediaType))
            {
                this.mediaRepository.updateOrInsertMediaGenre(media_cinevo_id, genre.getCinevo_id());
            }
        }

        if(mediaResponse.getGenres() != null)
        {
            for(Genre genre : this.genreService.parsGenreToObjects(mediaResponse.getGenres(), mediaType))
            {
                this.mediaRepository.updateOrInsertMediaGenre(media_cinevo_id, genre.getCinevo_id());
            }
        }

        if(mediaResponse.getSeasons() != null)
        {
            for(MediaResponse.Season ses : mediaResponse.getSeasons())
            {
                String season_cinevo_id = Shared.generateCinevoId(
                        this.mediaRepository.getSeasonByMediaTmdbIdAndSeasonNumber(mediaResponse.getId(), ses.getSeason_number()));

                String season_key = "SEASON_" + ses.getId();
                synchronized(lockManager.getLock(season_key))
                {
                    RetryUtil.retry(() -> this.mediaRepository.updateOrInsertSeason(season_cinevo_id, ses.getId(),
                            Shared.onStringParseDate(ses.getAir_date()), ses.getEpisode_count(), ses.getName(), ses.getOverview(),
                            ses.getPoster_path(), ses.getSeason_number(), ses.getVote_average(), media_cinevo_id));
                }
                this.watchProviderService.findByMediaTmdbIdAndSeasonNumber(media_tmdb_id, ses.getSeason_number(), MediaType.TV_SEASON);

                TvSeasonDetailsResponse tvSeasonDetailsResponse = this.service.getTvSeasonDetails(id, ses.getSeason_number(), null, null);
                for(TvSeasonDetailsResponse.Episode episode : tvSeasonDetailsResponse.getEpisodes())
                {
                    String episode_cinevo_id = Shared.generateCinevoId(
                            this.mediaRepository.getEpisodeByMediaTmdbIdAndSeasonNumberAndEpisodeNumber(mediaResponse.getId(), ses.getSeason_number(),
                                    episode.getEpisode_number()));

                    String episode_key = "EPISODE_" + episode.getId();
                    synchronized(lockManager.getLock(episode_key))
                    {
                        RetryUtil.retry(() -> this.mediaRepository.updateOrInsertEpisode(episode_cinevo_id, episode.getId(),
                                Shared.onStringParseDate(episode.getAir_date()), episode.getEpisode_number(),
                                EpisodeType.valueOf(episode.getEpisode_type().toUpperCase()).name(), episode.getOverview(), episode.getRuntime(),
                                episode.getStill_path(), episode.getVote_average(), episode.getVote_count(), season_cinevo_id));
                    }
                    this.mediaRepository.updateEpisodeWithProductionCode(episode_cinevo_id,
                            this.productionCountryService.getByIso(episode.getProduction_code()).getCinevo_id());
                }
            }
        }

        if(mediaResponse.getLast_episode_to_air() != null)
        {
            this.generateEpisodeToAir(media_cinevo_id, mediaResponse.getLast_episode_to_air());
        }

        if(mediaResponse.getNext_episode_to_air() != null)
        {
            this.generateEpisodeToAir(media_cinevo_id, mediaResponse.getNext_episode_to_air());
        }

        if(type.equals(MediaType.TV))
        {
            for(MediaResponse.CreatedBy createdBy : mediaResponse.getCreated_by())
            {
                Person person = this.personService.findByTmdbId(createdBy.getId());
                this.personService.updateOrInsertPersonMedia(media_cinevo_id, person.getCinevo_id());
            }
        }

        this.mediaResolver.generateDateAsync(id, type);

        return this.mediaRepository.getMediaByTmdbIdAndMediaType(id, type.name()).get();
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
        this.jobHelper.updateJobList(type, time_window, ids, null);
    }

    private void generateEpisodeToAir(final String media_cinevo_id, final MediaResponse.EpisodeToAir episode_to_air)
    {
        String episode_to_air_cinevo_id = Shared.generateCinevoId(this.mediaRepository.findEpisodeToAirByTmdbId(episode_to_air.getId()));

        String episode_key = "EPISODE_TO_AIR_" + episode_to_air.getId();
        synchronized(lockManager.getLock(episode_key))
        {
            RetryUtil.retry(
                    () -> this.mediaRepository.updateOrInsertEpisodeToAir(episode_to_air_cinevo_id, episode_to_air.getId(), episode_to_air.getName(),
                            episode_to_air.getOverview(), episode_to_air.getVote_average(), episode_to_air.getVote_count(),
                            Shared.onStringParseDate(episode_to_air.getAir_date()), episode_to_air.getEpisode_number(),
                            EpisodeType.valueOf(episode_to_air.getEpisode_type().toUpperCase()).name(), episode_to_air.getRuntime(),
                            episode_to_air.getSeason_number(), episode_to_air.getStill_path(), media_cinevo_id));
        }

        this.mediaRepository.updateEpisodeToAirWithProductionCountry(episode_to_air_cinevo_id,
                this.productionCountryService.getByIso(episode_to_air.getProduction_code()).getCinevo_id());
    }
}
