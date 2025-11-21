package com.cinovo.backend.DB.Service;

import com.cinovo.backend.DB.Model.Media;
import com.cinovo.backend.DB.Model.WatchProvider;
import com.cinovo.backend.DB.Repository.WatchProviderRepository;
import com.cinovo.backend.DB.Util.MediaResolver;
import com.cinovo.backend.DB.Util.Shared;
import com.cinovo.backend.DB.Util.TMDBLogically;
import com.cinovo.backend.Enum.MediaType;
import com.cinovo.backend.Enum.ProviderType;
import com.cinovo.backend.TMDB.Response.MediaWatchProvidersResponse;
import lombok.extern.jbosslog.JBossLog;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

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

    public List<WatchProvider> findWatchProviderByMediaIdAndType(final Integer id, final MediaType type) throws Exception
    {
        Optional<List<WatchProvider>> watchProviders = this.watchProviderRepository.findWatchProviderByMovieId(id);
        if(watchProviders.isEmpty() || watchProviders.get().isEmpty())
        {
            return this.onConvertTMDB(type + Shared.REGEX + id);
        }
        return watchProviders.get();
    }

    public List<WatchProvider> findWatchProviderBySeasonIdAndType(final Integer id, final Integer season, final MediaType type) throws Exception
    {
        Optional<List<WatchProvider>> watchProviders = this.watchProviderRepository.findWatchProviderBySeasonIdAndType(id, season);
        if(watchProviders.isEmpty() || watchProviders.get().isEmpty())
        {
            return this.onConvertTMDB(type + Shared.REGEX + id + Shared.REGEX + season);
        }
        return watchProviders.get();
    }

    @Override
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
                        watchProviders.add(
                                createWatchProvider(possibility, entry.getKey(), ProviderType.BUY, MediaType.valueOf(parts[0]), media, season));
                    }
                }

                if(entry.getValue().getFlatrate() != null)
                {
                    for(MediaWatchProvidersResponse.WatchProvider.Possibility possibility : entry.getValue().getFlatrate())
                    {
                        watchProviders.add(
                                createWatchProvider(possibility, entry.getKey(), ProviderType.FLATRATE, MediaType.valueOf(parts[0]), media, season));
                    }
                }

                if(entry.getValue().getRent() != null)
                {
                    for(MediaWatchProvidersResponse.WatchProvider.Possibility possibility : entry.getValue().getRent())
                    {
                        watchProviders.add(
                                createWatchProvider(possibility, entry.getKey(), ProviderType.RENT, MediaType.valueOf(parts[0]), media, season));
                    }
                }

                if(entry.getValue().getAds() != null)
                {
                    for(MediaWatchProvidersResponse.WatchProvider.Possibility possibility : entry.getValue().getAds())
                    {
                        watchProviders.add(
                                createWatchProvider(possibility, entry.getKey(), ProviderType.ADS, MediaType.valueOf(parts[0]), media, season));
                    }
                }

                if(entry.getValue().getFree() != null)
                {
                    for(MediaWatchProvidersResponse.WatchProvider.Possibility possibility : entry.getValue().getFree())
                    {
                        watchProviders.add(
                                createWatchProvider(possibility, entry.getKey(), ProviderType.FREE, MediaType.valueOf(parts[0]), media, season));
                    }
                }
            }

            this.watchProviderRepository.saveAll(watchProviders);
        }
        return watchProviders;
    }

    private WatchProvider createWatchProvider(final MediaWatchProvidersResponse.WatchProvider.Possibility possibility, final String location,
            final ProviderType provider_type, final MediaType type, final Media media, final Media.Season season)
    {
        WatchProvider watchProvider = this.watchProviderRepository.findWatchProviderById(media == null ? null : media.getCinevo_id(),
                        season == null ? null : season.getCinevo_id(), type.name(), provider_type.name(), location, possibility.getProvider_id())
                .orElse(new WatchProvider());
        watchProvider.setType(type);
        watchProvider.setProvider_type(provider_type);
        watchProvider.setLocation(location);
        watchProvider.setProvider_id(possibility.getProvider_id());
        watchProvider.setLogo_path(possibility.getLogo_path());
        watchProvider.setProvider_name(possibility.getProvider_name());
        watchProvider.setDisplay_priority(possibility.getDisplay_priority());
        watchProvider.setMedia(media);
        watchProvider.setSeason(season);

        return watchProvider;
    }
}

