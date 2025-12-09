package com.cinovo.backend.DB.Service;

import com.cinovo.backend.DB.Model.*;
import com.cinovo.backend.DB.Repository.MediaRepository;
import com.cinovo.backend.DB.Util.Resolver.MediaResolver;
import com.cinovo.backend.DB.Util.Shared;
import com.cinovo.backend.DB.Util.TMDBLogically;
import com.cinovo.backend.Enum.EpisodeType;
import com.cinovo.backend.Enum.MediaStatus;
import com.cinovo.backend.Enum.MediaType;
import com.cinovo.backend.TMDB.Response.*;
import com.cinovo.backend.TMDB.Response.Common.MediaExternalIdResponse;
import com.cinovo.backend.TMDB.Response.Common.MediaResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.jbosslog.JBossLog;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

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

    public MediaService(MediaRepository mediaRepository, com.cinovo.backend.TMDB.Service service, GenreService genreService,
            PersonService personService, KeywordService keywordService, SpokenLanguageService spokenLanguageService,
            ProductionCountryService productionCountryService, CompanyService companyService, NetworkService networkService,
            CollectionService collectionService, CreditService creditService, MediaResolver mediaResolver)
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
    }

    public Media.Season getSeasonById(final Integer season_id)
    {
        Optional<Media.Season> season = this.mediaRepository.getSeasonById(season_id);
        if(season.isEmpty())
        {
            return new Media.Season();
        }
        return season.get();
    }

    public Media.Season.Episode findEpisodeById(final Integer episode_id)
    {
        Optional<Media.Season.Episode> episode = this.mediaRepository.findEpisodeById(episode_id);
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

    public Media getMediaByIdAndType(final Integer id, final MediaType type) throws Exception
    {
        Optional<Media> movie = this.mediaRepository.getMediaByIdAndType(id, type.name());
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

    public Media.Season getSeasonByMediaIdAndSeasonNumber(final Integer id, final Integer season_number) throws Exception
    {
        Optional<Media.Season> season = this.mediaRepository.getSeasonByMediaIdAndSeasonNumber(id, season_number);
        if(season.isEmpty())
        {
            return (Media.Season) onConvertTMDB(MediaType.TV + Shared.REGEX + id);
        }
        return season.get();
    }

    public Media.Season.Episode getEpisodeByMediaIdAndSeasonNumberAndEpisode(final Integer id, final Integer season_number,
            final Integer episode_number) throws Exception
    {
        Optional<Media.Season.Episode> episode = this.mediaRepository.getEpisodeByMediaIdAndSeasonNumberAndEpisode(id, season_number, episode_number);
        if(episode.isEmpty())
        {
            return (Media.Season.Episode) onConvertTMDB(MediaType.TV + Shared.REGEX + id);
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
            Media media = this.getMediaByIdAndType(id, type);
            if(media != null && (title.equals(media.getTitle()) || original_title.equals(media.getOriginal_title())))
            {
                return media.getType();
            }
        }
        return null;
    }

    public List<Media> getMediaUsingTrending(final MediaType type, final String time_window, final String language) throws Exception
    {
        return (List<Media>) this.onConvertTMDB(MediaType.TRENDING + Shared.REGEX + type + Shared.REGEX + time_window + Shared.REGEX + language);
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

    @Transactional
    private Media onConvertMediaById(final Integer id, final MediaType type) throws Exception
    {
        MediaResponse mediaResponse = type.equals(MediaType.MOVIE) ? service.getMovieDetails(id, "en-US") : service.getTvDetails(id, null, null);

        if(mediaResponse.getId() == null)
            return null;

        MediaExternalIdResponse mediaExternalIdsResponse =
                type.equals(MediaType.MOVIE) ? service.getMovieExternalIds(id) : service.getTvExternalIds(id);

        MediaKeywordResponse mediaKeywordResponse = type.equals(MediaType.MOVIE) ? service.getKeywordMovie(id) : service.getTvKeyword(id);

        Media media = mediaRepository.getMediaByIdAndType(id, type.name()).orElse(new Media());
        media.setId(mediaResponse.getId());
        media.setType(type != null ? type : MediaType.valueOf(mediaResponse.getMedia_type().toUpperCase()));

        media.setAdult(mediaResponse.getAdult());
        media.setBackdrop_path(mediaResponse.getBackdrop_path());
        media.setBudget(mediaResponse.getBudget());
        media.setHomepage(mediaResponse.getHomepage());
        media.setImdb_id(mediaExternalIdsResponse.getImdb_id());
        media.setFreebase_mid(mediaExternalIdsResponse.getFreebase_mid());
        media.setFreebase_id(mediaExternalIdsResponse.getFreebase_id());
        media.setTvdb_id(mediaExternalIdsResponse.getTvdb_id());
        media.setTvrage_id(mediaExternalIdsResponse.getTvrage_id());
        media.setWikidata_id(mediaExternalIdsResponse.getWikidata_id());
        media.setFacebook_id(mediaExternalIdsResponse.getFacebook_id());
        media.setInstagram_id(mediaExternalIdsResponse.getInstagram_id());
        media.setTwitter_id(mediaExternalIdsResponse.getTwitter_id());
        saveAndUpdate(media);

        if(mediaKeywordResponse.getKeywords() != null)
        {
            List<Keyword> keywords = mediaKeywordResponse.getKeywords().stream().map(k -> {
                try
                {
                    return keywordService.findKeywordById(k.getId());
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

        if(mediaResponse.getGenre_ids() != null)
        {
            media.setGenres(genreService.parsLongToObjects(mediaResponse.getGenre_ids(), media.getType()));
        }
        else if(mediaResponse.getGenres() != null)
        {
            media.setGenres(genreService.parsGenreToObjects(mediaResponse.getGenres(), media.getType()));
        }

        if(mediaResponse.getSeasons() != null)
        {
            List<Media.Season> managedSeasons = media.getSeasons();
            if(managedSeasons == null)
            {
                managedSeasons = new ArrayList<>();
                media.setSeasons(managedSeasons);
            }

            Map<Integer, Media.Season> seasonById =
                    managedSeasons.stream().filter(s -> s.getId() != null).collect(Collectors.toMap(Media.Season::getId, Function.identity()));

            for(MediaResponse.Season seasonDto : mediaResponse.getSeasons())
            {
                Media.Season ses =
                        seasonById.getOrDefault(seasonDto.getId(), mediaRepository.getSeasonById(seasonDto.getId()).orElse(new Media.Season()));

                ses.setId(seasonDto.getId());
                ses.setAir_date(Shared.onStringParseDate(seasonDto.getAir_date()));
                ses.setEpisode_count(seasonDto.getEpisode_count());
                ses.setName(seasonDto.getName());
                ses.setOverview(seasonDto.getOverview());
                ses.setPoster_path(seasonDto.getPoster_path());
                ses.setSeason_number(seasonDto.getSeason_number());
                ses.setVote_average(seasonDto.getVote_average());
                ses.setMedia(media);

                MediaExternalIdResponse externalIdResponse = service.getTvSeasonExternalId(media.getId(), seasonDto.getSeason_number());
                ses.setImdb_id(externalIdResponse.getImdb_id());
                ses.setFreebase_mid(externalIdResponse.getFreebase_mid());
                ses.setFreebase_id(externalIdResponse.getFreebase_id());
                ses.setTvdb_id(externalIdResponse.getTvdb_id());
                ses.setTvrage_id(externalIdResponse.getTvrage_id());
                ses.setWikidata_id(externalIdResponse.getWikidata_id());
                ses.setFacebook_id(externalIdResponse.getFacebook_id());
                ses.setInstagram_id(externalIdResponse.getInstagram_id());
                ses.setTwitter_id(externalIdResponse.getTwitter_id());

                List<Media.Season.Episode> managedEpisodes = ses.getEpisodes();
                if(managedEpisodes == null)
                {
                    managedEpisodes = new ArrayList<>();
                    ses.setEpisodes(managedEpisodes);
                }

                Map<Integer, Media.Season.Episode> epById = managedEpisodes.stream().filter(e -> e.getId() != null)
                        .collect(Collectors.toMap(Media.Season.Episode::getId, Function.identity()));

                TvSeasonDetailsResponse tvSeasonDetailsResponse = service.getTvSeasonDetails(id, seasonDto.getSeason_number(), null, null);
                Set<Integer> incomingEpIds = new HashSet<>();

                for(TvSeasonDetailsResponse.Episode episodeDto : tvSeasonDetailsResponse.getEpisodes())
                {
                    incomingEpIds.add(episodeDto.getId());
                    Media.Season.Episode epi = epById.getOrDefault(episodeDto.getId(),
                            mediaRepository.getEpisodeById(episodeDto.getId()).orElse(new Media.Season.Episode()));

                    epi.setId(episodeDto.getId());
                    epi.setAir_date(Shared.onStringParseDate(episodeDto.getAir_date()));
                    epi.setEpisode_number(episodeDto.getEpisode_number());
                    epi.setEpisode_type(EpisodeType.valueOf(episodeDto.getEpisode_type().toUpperCase()));
                    epi.setOverview(episodeDto.getOverview());
                    epi.setProduction_code(episodeDto.getProduction_code());
                    epi.setRuntime(episodeDto.getRuntime());
                    epi.setStill_path(episodeDto.getStill_path());
                    epi.setVote_average(episodeDto.getVote_average());
                    epi.setVote_count(episodeDto.getVote_count());
                    epi.setSeason(ses);

                    MediaExternalIdResponse externalId =
                            service.getTvSeasonEpisodeExternal(media.getId(), ses.getSeason_number(), episodeDto.getEpisode_number());
                    epi.setImdb_id(externalId.getImdb_id());
                    epi.setFreebase_mid(externalId.getFreebase_mid());
                    epi.setFreebase_id(externalId.getFreebase_id());
                    epi.setTvdb_id(externalId.getTvdb_id());
                    epi.setTvrage_id(externalId.getTvrage_id());
                    epi.setWikidata_id(externalId.getWikidata_id());
                    epi.setFacebook_id(externalId.getFacebook_id());
                    epi.setInstagram_id(externalId.getInstagram_id());
                    epi.setTwitter_id(externalId.getTwitter_id());

                    if(!managedEpisodes.contains(epi))
                    {
                        managedEpisodes.add(epi);
                    }
                }

                managedEpisodes.removeIf(e -> e.getId() != null && !incomingEpIds.contains(e.getId()));

                if(!managedSeasons.contains(ses))
                {
                    managedSeasons.add(ses);
                }
            }
        }

        List<Person> persons = new ArrayList<>();
        if(type.equals(MediaType.TV))
        {
            for(MediaResponse.CreatedBy createdBy : mediaResponse.getCreated_by())
            {
                Person person = this.personService.findPersonById(createdBy.getId());
                if(person.getMedias() == null)
                {
                    person.setMedias(new ArrayList<>());
                }

                if(person.getMedias().stream().noneMatch(m -> m.getId().equals(media.getId())))
                {
                    person.getMedias().add(media);
                }

                persons.add(person);
            }
        }

        media.setCreated_by(persons);
        this.mediaResolver.generateDateAsync(id, type);

        return saveAndUpdate(media);
    }

    //    @Transactional
    //    private Media onConvertMediaById(final Integer id, final MediaType type) throws Exception
    //    {
    //        MediaResponse mediaResponse = new MediaResponse();
    //        MediaExternalIdResponse mediaExternalIdsResponse = new MediaExternalIdResponse();
    //        MediaKeywordResponse mediaKeywordResponse = new MediaKeywordResponse();
    //        if(type.equals(MediaType.MOVIE))
    //        {
    //            mediaResponse = this.service.getMovieDetails(id, "en-US");
    //            mediaExternalIdsResponse = this.service.getMovieExternalIds(id);
    //            mediaKeywordResponse = this.service.getKeywordMovie(id);
    //        }
    //        else if(type.equals(MediaType.TV))
    //        {
    //            mediaResponse = this.service.getTvDetails(id, null, null);
    //            mediaExternalIdsResponse = this.service.getTvExternalIds(id);
    //            mediaKeywordResponse = this.service.getTvKeyword(id);
    //        }
    //
    //        if(mediaResponse.getId() == null)
    //            return null;
    //
    //        Media media = this.mediaRepository.getMediaByIdAndType(id, type.name()).orElse(new Media());
    //
    //        media.setAdult(mediaResponse.getAdult());
    //        media.setBackdrop_path(mediaResponse.getBackdrop_path());
    //        media.setBudget(mediaResponse.getBudget());
    //        media.setHomepage(mediaResponse.getHomepage());
    //        media.setId(mediaResponse.getId());
    //        media.setType(type == null ? MediaType.valueOf(mediaResponse.getMedia_type().toUpperCase()) : type);
    //        this.mediaRepository.save(media);
    //
    //        media.setImdb_id(mediaExternalIdsResponse.getImdb_id());
    //        media.setFreebase_mid(mediaExternalIdsResponse.getFreebase_mid());
    //        media.setFreebase_id(mediaExternalIdsResponse.getFreebase_id());
    //        media.setTvdb_id(mediaExternalIdsResponse.getTvdb_id());
    //        media.setTvrage_id(mediaExternalIdsResponse.getTvrage_id());
    //        media.setWikidata_id(mediaExternalIdsResponse.getWikidata_id());
    //        media.setFacebook_id(mediaExternalIdsResponse.getFacebook_id());
    //        media.setInstagram_id(mediaExternalIdsResponse.getInstagram_id());
    //        media.setTwitter_id(mediaExternalIdsResponse.getTwitter_id());
    //
    //        if(mediaKeywordResponse.getKeywords() != null)
    //        {
    //            List<Keyword> keywords = new ArrayList<>();
    //            for(KeywordsResponse keyword : mediaKeywordResponse.getKeywords())
    //            {
    //                keywords.add(this.keywordService.findKeywordById(keyword.getId()));
    //            }
    //            media.setKeywords(keywords);
    //        }
    //        this.saveAndUpdate(media);
    //
    //        media.setOrigin_country(mediaResponse.getOrigin_country());
    //        media.setOriginal_language(mediaResponse.getOriginal_language());
    //        media.setOriginal_title(mediaResponse.getOriginal_title());
    //        media.setOverview(mediaResponse.getOverview());
    //        media.setPopularity(mediaResponse.getPopularity());
    //        media.setPoster_path(mediaResponse.getPoster_path());
    //        media.setRelease_date(mediaResponse.getRelease_date() != null && !mediaResponse.getRelease_date().isBlank() ? LocalDate.parse(
    //                mediaResponse.getRelease_date()) : null);
    //        media.setFirst_air_date(mediaResponse.getFirst_air_date() != null && !mediaResponse.getFirst_air_date().isBlank() ? LocalDate.parse(
    //                mediaResponse.getFirst_air_date()) : null);
    //        media.setRevenue(mediaResponse.getRevenue());
    //        media.setRuntime(mediaResponse.getRuntime());
    //        media.setStatus(MediaStatus.fromLabel(mediaResponse.getStatus()));
    //        media.setTagline(mediaResponse.getTagline());
    //        media.setTitle(mediaResponse.getTitle());
    //        media.setVideo(mediaResponse.getVideo());
    //        media.setVote_average(mediaResponse.getVote_average());
    //        media.setVote_count(mediaResponse.getVote_count());
    //        media.setGenres(mediaResponse.getGenre_ids() != null
    //                ? genreService.parsLongToObjects(mediaResponse.getGenre_ids(), media.getType())
    //                : (mediaResponse.getGenres() != null ? genreService.parsGenreToObjects(mediaResponse.getGenres(), media.getType()) : null));
    //        this.saveAndUpdate(media);
    //
    //        if(mediaResponse.getBelongs_to_collection() != null)
    //        {
    //            media.setBelong_to_collection(this.collectionService.findCollectionById(mediaResponse.getBelongs_to_collection().getId()));
    //        }
    //
    //        if(mediaResponse.getProduction_companies() != null)
    //        {
    //            List<Company> productionCompanies = new ArrayList<>();
    //            for(MediaResponse.ProductionCompany company : mediaResponse.getProduction_companies())
    //            {
    //                productionCompanies.add(this.companyService.findCompanyById(company.getId()));
    //            }
    //            media.setProduction_companies(productionCompanies);
    //        }
    //
    //        if(mediaResponse.getProduction_countries() != null)
    //        {
    //            List<ProductionCountry> productionCountries = new ArrayList<>();
    //            for(MediaResponse.ProductionCountry country : mediaResponse.getProduction_countries())
    //            {
    //                ProductionCountry productionCountry = this.productionCountryService.getProductionCountryById(country.getIso_3166_1());
    //                productionCountry.setIso_3166_1(country.getIso_3166_1());
    //                productionCountry.setName(country.getName());
    //                productionCountries.add(productionCountry);
    //            }
    //            media.setProduction_countries(productionCountries);
    //        }
    //
    //        if(mediaResponse.getSpoken_languages() != null)
    //        {
    //            List<SpokenLanguage> spokenLanguages = new ArrayList<>();
    //            for(MediaResponse.SpokenLanguage language : mediaResponse.getSpoken_languages())
    //            {
    //                SpokenLanguage spokenLanguage = this.spokenLanguageService.getSpokenLanguageById(language.getIso_639_1());
    //                spokenLanguage.setEnglish_name(language.getEnglish_name());
    //                spokenLanguage.setIso_639_1(language.getIso_639_1());
    //                spokenLanguage.setName(language.getName());
    //                spokenLanguages.add(spokenLanguage);
    //            }
    //            media.setSpoken_languages(spokenLanguages);
    //        }
    //
    //        if(mediaResponse.getNetworks() != null)
    //        {
    //            List<Network> networks = new ArrayList<>();
    //            for(MediaResponse.Network network : mediaResponse.getNetworks())
    //            {
    //                networks.add(this.networkService.getNetworkById(network.getId()));
    //            }
    //            media.setNetworks(networks);
    //        }
    //
    //        if(mediaResponse.getSeasons() != null)
    //        {
    //            List<Media.Season> seasons = new ArrayList<>();
    //            for(MediaResponse.Season season : mediaResponse.getSeasons())
    //            {
    //                Media.Season ses = this.mediaRepository.getSeasonById(season.getId()).orElse(new Media.Season());
    //                MediaExternalIdResponse externalIdResponse = this.service.getTvSeasonExternalId(media.getId(), season.getSeason_number());
    //                ses.setId(season.getId());
    //                ses.setAir_date(season.getAir_date() != null ? LocalDate.parse(season.getAir_date()) : null);
    //                ses.setEpisode_count(season.getEpisode_count());
    //                ses.setName(season.getName());
    //                ses.setOverview(season.getOverview());
    //                ses.setPoster_path(season.getPoster_path());
    //                ses.setSeason_number(season.getSeason_number());
    //                ses.setVote_average(season.getVote_average());
    //
    //                ses.setImdb_id(externalIdResponse.getImdb_id());
    //                ses.setFreebase_mid(externalIdResponse.getFreebase_mid());
    //                ses.setFreebase_id(externalIdResponse.getFreebase_id());
    //                ses.setTvdb_id(externalIdResponse.getTvdb_id());
    //                ses.setTvrage_id(externalIdResponse.getTvrage_id());
    //                ses.setWikidata_id(externalIdResponse.getWikidata_id());
    //                ses.setFacebook_id(externalIdResponse.getFacebook_id());
    //                ses.setInstagram_id(externalIdResponse.getInstagram_id());
    //                ses.setTwitter_id(externalIdResponse.getTwitter_id());
    //
    //                List<Media.Season.Episode> episodes = new ArrayList<>();
    //                TvSeasonDetailsResponse tvSeasonDetailsResponse = this.service.getTvSeasonDetails(id, season.getSeason_number(), null, null);
    //                for(TvSeasonDetailsResponse.Episode episode : tvSeasonDetailsResponse.getEpisodes())
    //                {
    //                    MediaExternalIdResponse externalId =
    //                            this.service.getTvSeasonEpisodeExternal(media.getId(), ses.getSeason_number(), episode.getEpisode_number());
    //                    Media.Season.Episode epi = this.mediaRepository.getEpisodeById(episode.getId()).orElse(new Media.Season.Episode());
    //                    epi.setAir_date(
    //                            (episode.getAir_date() != null && !episode.getAir_date().isBlank()) ? LocalDate.parse(episode.getAir_date()) : null);
    //                    epi.setEpisode_number(episode.getEpisode_number());
    //                    epi.setEpisode_type(EpisodeType.valueOf(episode.getEpisode_type().toUpperCase()));
    //                    epi.setId(episode.getId());
    //                    epi.setOverview(episode.getOverview());
    //                    epi.setProduction_code(episode.getProduction_code());
    //                    epi.setRuntime(episode.getRuntime());
    //                    epi.setStill_path(episode.getStill_path());
    //                    epi.setVote_average(episode.getVote_average());
    //                    epi.setVote_count(episode.getVote_count());
    //                    epi.setSeason(ses);
    //
    //                    epi.setImdb_id(externalId.getImdb_id());
    //                    epi.setFreebase_mid(externalId.getFreebase_mid());
    //                    epi.setFreebase_id(externalId.getFreebase_id());
    //                    epi.setTvdb_id(externalId.getTvdb_id());
    //                    epi.setTvrage_id(externalId.getTvrage_id());
    //                    epi.setWikidata_id(externalId.getWikidata_id());
    //                    epi.setFacebook_id(externalId.getFacebook_id());
    //                    epi.setInstagram_id(externalId.getInstagram_id());
    //                    epi.setTwitter_id(externalId.getTwitter_id());
    //
    //                    episodes.add(epi);
    //                }
    //                ses.setEpisodes(episodes);
    //                ses.setMedia(media);
    //                seasons.add(ses);
    //            }
    //            media.setSeasons(seasons);
    //        }
    //        this.saveAndUpdate(media);
    //
    //        if(mediaResponse.getLast_episode_to_air() != null)
    //        {
    //            Media.EpisodeToAir episodeToAir =
    //                    this.mediaRepository.getEpisodeToAirById(mediaResponse.getLast_episode_to_air().getId()).orElse(new Media.EpisodeToAir());
    //            episodeToAir.setId(mediaResponse.getLast_episode_to_air().getId());
    //            episodeToAir.setName(mediaResponse.getLast_episode_to_air().getName());
    //            episodeToAir.setOverview(mediaResponse.getLast_episode_to_air().getOverview());
    //            episodeToAir.setVote_average(mediaResponse.getLast_episode_to_air().getVote_average());
    //            episodeToAir.setVote_count(mediaResponse.getLast_episode_to_air().getVote_count());
    //            episodeToAir.setAir_date(mediaResponse.getLast_episode_to_air().getAir_date() != null ? LocalDate.parse(
    //                    mediaResponse.getLast_episode_to_air().getAir_date()) : null);
    //            episodeToAir.setEpisode_number(mediaResponse.getLast_episode_to_air().getEpisode_number());
    //            episodeToAir.setEpisode_type(EpisodeType.valueOf(mediaResponse.getLast_episode_to_air().getEpisode_type().toUpperCase()));
    //            episodeToAir.setProduction_code(mediaResponse.getLast_episode_to_air().getProduction_code());
    //            episodeToAir.setRuntime(mediaResponse.getLast_episode_to_air().getRuntime());
    //            episodeToAir.setSeason_number(mediaResponse.getLast_episode_to_air().getSeason_number());
    //            episodeToAir.setStill_path(mediaResponse.getLast_episode_to_air().getStill_path());
    //            episodeToAir.setMedia(media);
    //
    //            media.setLast_episode_to_air(episodeToAir);
    //        }
    //
    //        if(mediaResponse.getNext_episode_to_air() != null)
    //        {
    //            Media.EpisodeToAir episodeToAir =
    //                    this.mediaRepository.getEpisodeToAirById(mediaResponse.getNext_episode_to_air().getId()).orElse(new Media.EpisodeToAir());
    //            episodeToAir.setId(mediaResponse.getNext_episode_to_air().getId());
    //            episodeToAir.setName(mediaResponse.getNext_episode_to_air().getName());
    //            episodeToAir.setOverview(mediaResponse.getNext_episode_to_air().getOverview());
    //            episodeToAir.setVote_average(mediaResponse.getNext_episode_to_air().getVote_average());
    //            episodeToAir.setVote_count(mediaResponse.getNext_episode_to_air().getVote_count());
    //            episodeToAir.setAir_date(mediaResponse.getNext_episode_to_air().getAir_date() != null ? LocalDate.parse(
    //                    mediaResponse.getNext_episode_to_air().getAir_date()) : null);
    //            episodeToAir.setEpisode_number(mediaResponse.getNext_episode_to_air().getEpisode_number());
    //            episodeToAir.setEpisode_type(EpisodeType.valueOf(mediaResponse.getNext_episode_to_air().getEpisode_type().toUpperCase()));
    //            episodeToAir.setProduction_code(mediaResponse.getNext_episode_to_air().getProduction_code());
    //            episodeToAir.setRuntime(mediaResponse.getNext_episode_to_air().getRuntime());
    //            episodeToAir.setSeason_number(mediaResponse.getNext_episode_to_air().getSeason_number());
    //            episodeToAir.setStill_path(mediaResponse.getNext_episode_to_air().getStill_path());
    //            episodeToAir.setMedia(media);
    //
    //            media.setNext_episode_to_air(episodeToAir);
    //        }
    //        List<Person> persons = new ArrayList<>();
    //        if(type.equals(MediaType.TV))
    //        {
    //            for(MediaResponse.CreatedBy createdBy : mediaResponse.getCreated_by())
    //            {
    //                Person person = this.personService.findPersonById(createdBy.getId());
    //                if(person.getMedias() == null)
    //                {
    //                    person.setMedias(new ArrayList<>());
    //                }
    //
    //                if(person.getMedias().stream().noneMatch(m -> m.getId().equals(media.getId())))
    //                {
    //                    person.getMedias().add(media);
    //                }
    //
    //                persons.add(person);
    //            }
    //        }
    //        media.setCreated_by(persons);
    //        this.saveAndUpdate(media);
    //
    //        this.mediaResolver.generateDateAsync(id, type);
    //        return media;
    //    }

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
