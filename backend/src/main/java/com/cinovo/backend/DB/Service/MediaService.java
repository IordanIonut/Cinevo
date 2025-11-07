package com.cinovo.backend.DB.Service;

import com.cinovo.backend.DB.Model.Keyword;
import com.cinovo.backend.DB.Model.Media;
import com.cinovo.backend.DB.Model.Person;
import com.cinovo.backend.DB.Repository.MediaRepository;
import com.cinovo.backend.DB.Util.Shared;
import com.cinovo.backend.DB.Util.TMDBLogically;
import com.cinovo.backend.Enum.EpisodeType;
import com.cinovo.backend.Enum.MediaStatus;
import com.cinovo.backend.Enum.MediaType;
import com.cinovo.backend.TMDB.Response.*;
import com.cinovo.backend.TMDB.Response.Common.KeywordsResponse;
import com.cinovo.backend.TMDB.Response.Common.MediaResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.jbosslog.JBossLog;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@JBossLog
@Service
public class MediaService implements TMDBLogically<Object, Object>
{
    private final MediaRepository mediaRepository;
    private final com.cinovo.backend.TMDB.Service service;
    private final GenreService genreService;
    private final ImageService imageService;
    private final VideoService videoService;
    private final WatchProviderService watchProviderService;
    private final PersonService personService;

    public MediaService(MediaRepository mediaRepository, com.cinovo.backend.TMDB.Service service, GenreService genreService,
            ImageService imageService, VideoService videoService, WatchProviderService watchProviderService, PersonService personService)
    {
        this.mediaRepository = mediaRepository;
        this.service = service;
        this.genreService = genreService;
        this.imageService = imageService;
        this.videoService = videoService;
        this.watchProviderService = watchProviderService;
        this.personService = personService;
    }

    public List<Media> findMediaByDiscovery() throws Exception
    {
        return (List<Media>) this.onConvertTMDB(MediaType.DISCOVER.name());
    }

    public List<Media> findMediaByRecommendation(final Integer id) throws Exception
    {
        return (List<Media>) this.onConvertTMDB(MediaType.RECOMMENDATION.name() + Shared.REGEX + id);
    }

    public List<Media> findMediaBySimilar(final Integer id) throws Exception
    {
        return (List<Media>) this.onConvertTMDB(MediaType.SIMILAR.name() + Shared.REGEX + id);
    }

    public Media getMediaByIdAndType(final Integer id, final MediaType type) throws Exception
    {
        Optional<Media> movie = this.mediaRepository.getMediaByIdAndType(id, type.name());
        if(movie.isEmpty())
        {
            return (Media) onConvertTMDB(type + Shared.REGEX + id);
        }
        return movie.get();
    }

    public List<Media> getMediaUsingSearch(final MediaType type, final String query, final Boolean include_adult, final String language,
            final String primary_release_year, final Integer page, final String region, final Integer year) throws Exception
    {
        return (List<Media>) this.onConvertTMDB(
                MediaType.SEARCH + Shared.REGEX + type + Shared.REGEX + query + Shared.REGEX + include_adult + Shared.REGEX + language + Shared.REGEX
                        + primary_release_year + Shared.REGEX + page + Shared.REGEX + region + Shared.REGEX + year);
    }

    public List<Media> getMediaUsingTrending(final MediaType type, final String time_window, final String language) throws Exception
    {
        return (List<Media>) this.onConvertTMDB(MediaType.TRENDING + Shared.REGEX + type + Shared.REGEX + time_window + Shared.REGEX + language);
    }

    @Override
    public Object onConvertTMDB(Object obj) throws Exception
    {
        String[] parts = Shared.onSplitObject(obj);

        if((MediaType.valueOf(parts[0]) == MediaType.MOVIE || MediaType.valueOf(parts[0]) == MediaType.TV)
                && Shared.onStringParseToInteger(parts[1]) != null)
        {
            return onConvertMediaById(Integer.parseInt(parts[1]), MediaType.valueOf(parts[0]));
        }
        else if(MediaType.valueOf(parts[0]) == MediaType.RECOMMENDATION)
        {
            return onConvertMedia(this.service.getMovieRecommendation(Integer.parseInt(parts[1]), "en-US", 1).getResults(),
                    MediaType.valueOf(parts[1]));
        }
        else if(MediaType.valueOf(parts[0]) == MediaType.DISCOVER)
        {
            return onConvertMedia(this.service.getDiscoverMovie().getResults(), MediaType.valueOf(parts[1]));
        }
        else if(MediaType.valueOf(parts[0]) == MediaType.SIMILAR)
        {
            return onConvertMedia(this.service.getMovieSimilar(Integer.parseInt(parts[1]), "en-US", 1).getResults(), MediaType.valueOf(parts[1]));
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

            return this.onConvertMedia(this.onParseSearchResponseToMediaResponse(rawResponse),
                    parts[1].equals("null") ? null : MediaType.valueOf(parts[1].toUpperCase()));
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

    private Media onConvertMediaById(final Integer id, final MediaType type) throws Exception
    {
        MediaResponse mediaResponse = new MediaResponse();
        MovieExternalIdsResponse mediaExternalIdsResponse = new MovieExternalIdsResponse();
        MovieKeywordResponse mediaKeywordResponse = new MovieKeywordResponse();

        if(type.equals(MediaType.MOVIE))
        {
            mediaResponse = this.service.getMovieDetails(id, "en-US");
            mediaExternalIdsResponse = this.service.getMovieExternalIds(id);
            mediaKeywordResponse = this.service.getKeywordMovie(id);
        }
        else if(type.equals(MediaType.TV))
        {
            mediaResponse = this.service.getTvDetails(id, null, null);
        }

        Media media = this.mediaRepository.getMediaByIdAndType(id, type.name()).orElse(new Media());
        if(mediaResponse.getId() != null)
        {
            media.setAdult(mediaResponse.getAdult());
            media.setBackdrop_path(mediaResponse.getBackdrop_path());
            media.setBudget(mediaResponse.getBudget());
            media.setHomepage(mediaResponse.getHomepage());
            media.setId(mediaResponse.getId());
            media.setImdb_id(mediaExternalIdsResponse.getImdb_id());
            media.setWikidata_id(mediaExternalIdsResponse.getWikidata_id());
            media.setFacebook_id(mediaExternalIdsResponse.getFacebook_id());
            media.setInstagram_id(mediaExternalIdsResponse.getInstagram_id());
            media.setTwitter_id(mediaExternalIdsResponse.getTwitter_id());
            media.setOrigin_country(mediaResponse.getOrigin_country());
            media.setOriginal_language(mediaResponse.getOriginal_language());
            media.setOriginal_title(mediaResponse.getOriginal_title());
            media.setOverview(mediaResponse.getOverview());
            media.setPopularity(mediaResponse.getPopularity());
            media.setPoster_path(mediaResponse.getPoster_path());
            media.setType(type == null ? MediaType.valueOf(mediaResponse.getMedia_type().toUpperCase()) : type);
            media.setRelease_date(mediaResponse.getRelease_date() != null && !mediaResponse.getRelease_date().isBlank() ? LocalDate.parse(
                    mediaResponse.getRelease_date()) : null);
            media.setFirst_air_date(mediaResponse.getFirst_air_date() != null ? LocalDate.parse(mediaResponse.getFirst_air_date()) : null);
            media.setRevenue(mediaResponse.getRevenue());
            media.setRuntime(mediaResponse.getRuntime());
            media.setStatus(MediaStatus.fromLabel(mediaResponse.getStatus()));
            media.setTagline(mediaResponse.getTagline());
            media.setTitle(mediaResponse.getTitle());
            media.setVideo(mediaResponse.getVideo());
            media.setVote_average(mediaResponse.getVote_average());
            media.setVote_count(mediaResponse.getVote_count());
            media.setGenres(mediaResponse.getGenre_ids() != null
                    ? genreService.parsLongToObjects(mediaResponse.getGenre_ids(), media.getType())
                    : genreService.parsGenreToObjects(mediaResponse.getGenres(), media.getType()));

            if(mediaResponse.getBelongs_to_collection() != null)
            {
                Media.BelongToCollection belongToCollection = new Media.BelongToCollection();
                belongToCollection.setId(mediaResponse.getBelongs_to_collection().getId());
                belongToCollection.setName(mediaResponse.getBelongs_to_collection().getName());
                belongToCollection.setPoster_path(mediaResponse.getBelongs_to_collection().getPoster_path());
                belongToCollection.setBackdrop_path(mediaResponse.getBelongs_to_collection().getBackdrop_path());

                media.setBelong_to_collection(belongToCollection);
            }

            if(mediaResponse.getProduction_companies() != null)
            {
                List<Media.ProductionCompany> productionCompanies = new ArrayList<>();
                for(MediaResponse.ProductionCompany company : mediaResponse.getProduction_companies())
                {
                    Media.ProductionCompany productionCompany = new Media.ProductionCompany();
                    productionCompany.setId(company.getId());
                    productionCompany.setName(company.getName());
                    productionCompany.setLogo_path(company.getLogo_path());
                    productionCompany.setOrigin_country(company.getOrigin_country());
                    productionCompanies.add(productionCompany);
                }
                media.setProduction_companies(productionCompanies);
            }

            if(mediaResponse.getProduction_countries() != null)
            {
                List<Media.ProductionCountry> productionCountries = new ArrayList<>();
                for(MediaResponse.ProductionCountry country : mediaResponse.getProduction_countries())
                {
                    Media.ProductionCountry productionCountry = new Media.ProductionCountry();
                    productionCountry.setIso_3166_1(country.getIso_3166_1());
                    productionCountry.setName(country.getName());
                    productionCountries.add(productionCountry);
                }
                media.setProduction_countries(productionCountries);
            }

            if(mediaResponse.getSpoken_languages() != null)
            {
                List<Media.SpokenLanguage> spokenLanguages = new ArrayList<>();
                for(MediaResponse.SpokenLanguage language : mediaResponse.getSpoken_languages())
                {
                    Media.SpokenLanguage spokenLanguage = new Media.SpokenLanguage();
                    spokenLanguage.setEnglish_name(language.getEnglish_name());
                    spokenLanguage.setIso_639_1(language.getIso_639_1());
                    spokenLanguage.setName(language.getName());
                    spokenLanguages.add(spokenLanguage);
                }
                media.setSpoken_languages(spokenLanguages);
            }

            if(mediaKeywordResponse.getKeywords() != null)
            {
                List<Keyword> keywords = new ArrayList<>();
                for(KeywordsResponse keyword : mediaKeywordResponse.getKeywords())
                {
                    Keyword key = new Keyword();
                    key.setId(keyword.getId());
                    key.setName(keyword.getName());
                    keywords.add(key);
                }
                media.setKeywords(keywords);
            }

            if(mediaResponse.getNetworks() != null)
            {
                List<Media.Network> networks = new ArrayList<>();
                for(MediaResponse.Network network : mediaResponse.getNetworks())
                {
                    Media.Network net = new Media.Network();
                    net.setId(network.getId());
                    net.setLogo_path(network.getLogo_path());
                    net.setName(network.getName());
                    net.setOrigin_country(network.getOrigin_country());
                    networks.add(net);
                }
                media.setNetworks(networks);
            }

            if(mediaResponse.getSeasons() != null)
            {
                List<Media.Season> seasons = new ArrayList<>();
                for(MediaResponse.Season season : mediaResponse.getSeasons())
                {
                    Media.Season ses = new Media.Season();
                    ses.setId(season.getId());
                    ses.setAir_date(season.getAir_date() != null ? LocalDate.parse(season.getAir_date()) : null);
                    ses.setEpisode_count(season.getEpisode_count());
                    ses.setName(season.getName());
                    ses.setOverview(season.getOverview());
                    ses.setPoster_path(season.getPoster_path());
                    ses.setSeason_number(season.getSeason_number());
                    ses.setVote_average(season.getVote_average());
                    seasons.add(ses);
                }
                media.setSeasons(seasons);
            }

            if(mediaResponse.getLast_episode_to_air() != null)
            {
                Media.LastEpisodeToAir lastEpisodeToAir = new Media.LastEpisodeToAir();
                lastEpisodeToAir.setId(mediaResponse.getLast_episode_to_air().getId());
                lastEpisodeToAir.setName(mediaResponse.getLast_episode_to_air().getName());
                lastEpisodeToAir.setOverview(mediaResponse.getLast_episode_to_air().getOverview());
                lastEpisodeToAir.setVote_average(mediaResponse.getLast_episode_to_air().getVote_average());
                lastEpisodeToAir.setVote_count(mediaResponse.getLast_episode_to_air().getVote_count());
                lastEpisodeToAir.setAir_date(mediaResponse.getLast_episode_to_air().getAir_date() != null ? LocalDate.parse(
                        mediaResponse.getLast_episode_to_air().getAir_date()) : null);
                lastEpisodeToAir.setEpisode_number(mediaResponse.getLast_episode_to_air().getEpisode_number());
                lastEpisodeToAir.setEpisode_type(EpisodeType.valueOf(mediaResponse.getLast_episode_to_air().getEpisode_type().toUpperCase()));
                lastEpisodeToAir.setProduction_code(mediaResponse.getLast_episode_to_air().getProduction_code());
                lastEpisodeToAir.setRuntime(mediaResponse.getLast_episode_to_air().getRuntime());
                lastEpisodeToAir.setSeason_number(mediaResponse.getLast_episode_to_air().getSeason_number());
                lastEpisodeToAir.setStill_path(mediaResponse.getLast_episode_to_air().getStill_path());
                lastEpisodeToAir.setMedia(media);

                media.setLast_episode_to_air(lastEpisodeToAir);
            }

            mediaRepository.save(media);
            //stop cycle between movie, image, video, watch provider
            List<Person> persons = new ArrayList<>();
            if(type.equals(MediaType.TV))
            {
                for(MediaResponse.CreatedBy createdBy : mediaResponse.getCreated_by())
                {
                    persons.add(this.personService.findPersonById(createdBy.getId()));
                }
            }

            media.setImages(this.imageService.findImageById(id, media.getType()));
            media.setVideos(this.videoService.findVideosByMovieId(id, media.getType()));
            media.setWatch_providers(this.watchProviderService.findWatchProviderByMovieId(id, media.getType()));
            media.setCreated_by(persons);
            mediaRepository.save(media);
        }
        return media;
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
}
