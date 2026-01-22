package com.cinovo.backend.DB.Service;

import com.cinovo.backend.DB.Model.Enum.MediaType;
import com.cinovo.backend.DB.Model.Enum.ProviderType;
import com.cinovo.backend.DB.Model.Media;
import com.cinovo.backend.DB.Model.WatchProvider;
import com.cinovo.backend.DB.Repository.WatchProviderRepository;
import com.cinovo.backend.DB.Util.Resolver.MediaResolver;
import com.cinovo.backend.DB.Util.Shared;
import com.cinovo.backend.DB.Util.TMDBLogically;
import com.cinovo.backend.TMDB.Response.MediaWatchProvidersResponse;
import lombok.extern.jbosslog.JBossLog;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@JBossLog
public class WatchProviderService implements TMDBLogically<Object, List<WatchProvider>>
{
    private final WatchProviderRepository watchProviderRepository;
    private final com.cinovo.backend.TMDB.Service service;
    private final MediaService mediaService;

    public WatchProviderService(WatchProviderRepository watchProviderRepository, com.cinovo.backend.TMDB.Service service,
            @Lazy MediaService mediaService)
    {
        this.watchProviderRepository = watchProviderRepository;
        this.service = service;
        this.mediaService = mediaService;
    }

    public List<WatchProvider> findByMediaTmdbIdAndMediaType(final Integer media_tmdb_id, final MediaType type) throws Exception
    {
        Optional<List<WatchProvider>> watchProviders = this.watchProviderRepository.findByMediaTmdbId(media_tmdb_id);
        if(watchProviders.isEmpty() || watchProviders.get().isEmpty())
        {
            return this.onConvertTMDB(type + Shared.REGEX + media_tmdb_id);
        }
        return watchProviders.get();
    }

    public List<WatchProvider> findByMediaTmdbIdAndSeasonNumber(final Integer media_tmdb_id, final Integer season, final MediaType type)
            throws Exception
    {
        Optional<List<WatchProvider>> watchProviders = this.watchProviderRepository.findByMediaTmdbIdAndSeasonNumber(media_tmdb_id, season);
        if(watchProviders.isEmpty() || watchProviders.get().isEmpty())
        {
            return this.onConvertTMDB(type + Shared.REGEX + media_tmdb_id + Shared.REGEX + season);
        }
        return watchProviders.get();
    }

    @Override
    @Transactional
    public List<WatchProvider> onConvertTMDB(Object obj) throws Exception
    {
        String[] parts = Shared.onSplitObject(obj);

        List<WatchProvider> watchProviders = new ArrayList<>();
        MediaWatchProvidersResponse watchProvidersResponse =
                MediaType.valueOf(parts[0]) == MediaType.TV_SEASON
                        ? this.service.getTvSeasonMediaWatchProvider(Shared.onStringParseToInteger(parts[1]), Shared.onStringParseToInteger(parts[2]),
                        "en-US")
                        : this.service.getMediaWatchProvider(Shared.onStringParseToInteger(parts[1]), MediaType.valueOf(parts[0]));
        Media media = MediaResolver.resolveMedia(mediaService, parts);
        Media.Season season = MediaResolver.resolveSeason(mediaService, parts);

        if(watchProvidersResponse.getResults() != null)
        {
            for(Map.Entry<String, MediaWatchProvidersResponse.WatchProvider> entry : watchProvidersResponse.getResults().entrySet())
            {
                if(entry.getValue().getBuy() != null)
                {
                    for(MediaWatchProvidersResponse.WatchProvider.Possibility possibility : entry.getValue().getBuy())
                    {
                        if(this.conditionInsert(entry.getKey()))
                        {
                            watchProviders.add(
                                    findByMediaCinevoIdOrSeasonCinevoIdAndTypeAndProviderTypeAndLocationAndProviderId(possibility, entry.getKey(),
                                            ProviderType.BUY, MediaType.valueOf(parts[0]), media, season));
                        }
                    }
                }

                if(entry.getValue().getFlatrate() != null)
                {
                    for(MediaWatchProvidersResponse.WatchProvider.Possibility possibility : entry.getValue().getFlatrate())
                    {
                        if(this.conditionInsert(entry.getKey()))
                        {
                            watchProviders.add(
                                    findByMediaCinevoIdOrSeasonCinevoIdAndTypeAndProviderTypeAndLocationAndProviderId(possibility, entry.getKey(),
                                            ProviderType.FLATRATE, MediaType.valueOf(parts[0]), media, season));
                        }
                    }
                }

                if(entry.getValue().getRent() != null)
                {
                    for(MediaWatchProvidersResponse.WatchProvider.Possibility possibility : entry.getValue().getRent())
                    {
                        if(this.conditionInsert(entry.getKey()))
                        {
                            watchProviders.add(
                                    findByMediaCinevoIdOrSeasonCinevoIdAndTypeAndProviderTypeAndLocationAndProviderId(possibility, entry.getKey(),
                                            ProviderType.RENT, MediaType.valueOf(parts[0]), media, season));
                        }
                    }
                }

                if(entry.getValue().getAds() != null)
                {
                    for(MediaWatchProvidersResponse.WatchProvider.Possibility possibility : entry.getValue().getAds())
                    {
                        if(this.conditionInsert(entry.getKey()))
                        {
                            watchProviders.add(
                                    findByMediaCinevoIdOrSeasonCinevoIdAndTypeAndProviderTypeAndLocationAndProviderId(possibility, entry.getKey(),
                                            ProviderType.ADS, MediaType.valueOf(parts[0]), media, season));
                        }
                    }
                }

                if(entry.getValue().getFree() != null)
                {
                    for(MediaWatchProvidersResponse.WatchProvider.Possibility possibility : entry.getValue().getFree())
                    {
                        if(this.conditionInsert(entry.getKey()))
                        {
                            watchProviders.add(
                                    findByMediaCinevoIdOrSeasonCinevoIdAndTypeAndProviderTypeAndLocationAndProviderId(possibility, entry.getKey(),
                                            ProviderType.FREE, MediaType.valueOf(parts[0]), media, season));
                        }
                    }
                }
            }

            //            this.watchProviderRepository.saveAll(watchProviders);
        }
        return watchProviders;
    }

    private WatchProvider findByMediaCinevoIdOrSeasonCinevoIdAndTypeAndProviderTypeAndLocationAndProviderId(
            final MediaWatchProvidersResponse.WatchProvider.Possibility possibility, final String location, final ProviderType provider_type,
            final MediaType type, final Media media, final Media.Season season) throws InterruptedException
    {
        this.watchProviderRepository.updateOrInsert(Shared.generateCinevoId(
                        this.watchProviderRepository.findByMediaCinevoIdOrSeasonCinevoIdAndTypeAndProviderTypeAndLocationAndProviderId(
                                media == null ? null : media.getCinevo_id(), season == null ? null : season.getCinevo_id(), type.name(), provider_type.name(),
                                location, possibility.getProvider_id())), type.name(), provider_type.name(), location, possibility.getLogo_path(),
                possibility.getProvider_id(), possibility.getProvider_name(), possibility.getDisplay_priority(), Shared.idOf(media),
                Shared.idOf(season));

        return this.watchProviderRepository.findByMediaCinevoIdOrSeasonCinevoIdAndTypeAndProviderTypeAndLocationAndProviderId(Shared.idOf(media),
                Shared.idOf(season), type.name(), provider_type.name(), location, possibility.getProvider_id()).get();
    }

    private Boolean conditionInsert(final String key)
    {
        return key.equals("US");
    }
}

