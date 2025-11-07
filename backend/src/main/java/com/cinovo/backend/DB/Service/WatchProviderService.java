package com.cinovo.backend.DB.Service;

import com.cinovo.backend.DB.Model.Media;
import com.cinovo.backend.DB.Model.WatchProvider;
import com.cinovo.backend.DB.Repository.WatchProviderRepository;
import com.cinovo.backend.DB.Util.Shared;
import com.cinovo.backend.DB.Util.TMDBLogically;
import com.cinovo.backend.Enum.MediaType;
import com.cinovo.backend.Enum.ProviderType;
import com.cinovo.backend.TMDB.Response.MovieWatchProvidersResponse;
import lombok.extern.jbosslog.JBossLog;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@JBossLog
public class WatchProviderService implements TMDBLogically<Object, List<WatchProvider>>
{
    private final WatchProviderRepository watchProviderRepository;
    private final MediaService movieService;
    private final com.cinovo.backend.TMDB.Service service;

    public WatchProviderService(WatchProviderRepository watchProviderRepository, @Lazy MediaService movieService,
            com.cinovo.backend.TMDB.Service service)
    {
        this.watchProviderRepository = watchProviderRepository;
        this.movieService = movieService;
        this.service = service;
    }

    public List<WatchProvider> findWatchProviderByMovieId(final Integer id, final MediaType type) throws Exception
    {
        Optional<List<WatchProvider>> watchProviders = this.watchProviderRepository.findWatchProviderByMovieId(id);
        if(watchProviders.isEmpty() || watchProviders.get().isEmpty())
        {
            return this.onConvertTMDB(id + Shared.REGEX + type);
        }
        return watchProviders.get();
    }

    @Override
    public List<WatchProvider> onConvertTMDB(Object obj) throws Exception
    {
        String[] parts = Shared.onSplitObject(obj);

        List<WatchProvider> watchProviders = new ArrayList<>();
        MovieWatchProvidersResponse watchProvidersResponse = this.service.getMovieWatchProvider(Integer.parseInt(parts[0]));
        Media media = this.movieService.getMediaByIdAndType(Integer.parseInt(parts[0]), MediaType.valueOf(parts[1]));

        for(Map.Entry<String, MovieWatchProvidersResponse.WatchProvider> entry : watchProvidersResponse.getResults().entrySet())
        {
            if(entry.getValue().getBuy() != null)
            {
                for(MovieWatchProvidersResponse.WatchProvider.Possibility possibility : entry.getValue().getBuy())
                {
                    watchProviders.add(createWatchProvider(possibility, entry.getKey(), ProviderType.BUY, MediaType.valueOf(parts[1]), media));
                }
            }

            if(entry.getValue().getFlatrate() != null)
            {
                for(MovieWatchProvidersResponse.WatchProvider.Possibility possibility : entry.getValue().getFlatrate())
                {
                    watchProviders.add(createWatchProvider(possibility, entry.getKey(), ProviderType.FLATRATE, MediaType.valueOf(parts[1]), media));
                }
            }

            if(entry.getValue().getRent() != null)
            {
                for(MovieWatchProvidersResponse.WatchProvider.Possibility possibility : entry.getValue().getRent())
                {
                    watchProviders.add(createWatchProvider(possibility, entry.getKey(), ProviderType.RENT, MediaType.valueOf(parts[1]), media));
                }
            }

            if(entry.getValue().getAds() != null)
            {
                for(MovieWatchProvidersResponse.WatchProvider.Possibility possibility : entry.getValue().getAds())
                {
                    watchProviders.add(createWatchProvider(possibility, entry.getKey(), ProviderType.ADS, MediaType.valueOf(parts[1]), media));
                }
            }

            if(entry.getValue().getFree() != null)
            {
                for(MovieWatchProvidersResponse.WatchProvider.Possibility possibility : entry.getValue().getFree())
                {
                    watchProviders.add(createWatchProvider(possibility, entry.getKey(), ProviderType.FREE, MediaType.valueOf(parts[1]), media));
                }
            }
        }

        this.watchProviderRepository.saveAll(watchProviders);
        return watchProviders;
    }

    private WatchProvider createWatchProvider(final MovieWatchProvidersResponse.WatchProvider.Possibility possibility, final String location,
            final ProviderType providerType, final MediaType type, final Media media)
    {
        WatchProvider watchProvider = new WatchProvider();
        watchProvider.setType(type);
        watchProvider.setProvider_type(providerType);
        watchProvider.setLocation(location);
        watchProvider.setProvider_id(possibility.getProvider_id());
        watchProvider.setLogo_path(possibility.getLogo_path());
        watchProvider.setProvider_name(possibility.getProvider_name());
        watchProvider.setDisplay_priority(possibility.getDisplay_priority());
        watchProvider.setMedia(media);

        return watchProvider;
    }
}

