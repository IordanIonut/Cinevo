package com.cinovo.backend.DB.Service;

import com.cinovo.backend.DB.Model.Enum.FileType;
import com.cinovo.backend.DB.Model.Network;
import com.cinovo.backend.DB.Repository.NetworkRepository;
import com.cinovo.backend.DB.Util.Shared;
import com.cinovo.backend.DB.Util.TMDBLogically;
import com.cinovo.backend.TMDB.Response.NetworkAlternativeNameResponse;
import com.cinovo.backend.TMDB.Response.NetworkDetailResponse;
import com.cinovo.backend.TMDB.Response.NetworkImageResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class NetworkService implements TMDBLogically<Integer, Network>
{
    private final NetworkRepository networkRepository;
    private final com.cinovo.backend.TMDB.Service service;

    public NetworkService(final NetworkRepository networkRepository, final com.cinovo.backend.TMDB.Service service)
    {
        this.networkRepository = networkRepository;
        this.service = service;
    }

    public Network getNetworkByTmdbId(final Integer tmdb_id) throws Exception
    {
        Optional<Network> network = this.networkRepository.getNetworkByTmdbId(tmdb_id);
        if(network.isEmpty())
        {
            return this.onConvertTMDB(tmdb_id);
        }
        return network.get();
    }

    @Override
    @Transactional
    public Network onConvertTMDB(Integer input) throws Exception
    {
        NetworkDetailResponse networkDetailResponse = this.service.getNetworkDetail(input);
        NetworkAlternativeNameResponse networkAlternativeNameResponse = this.service.getNetworkAlternativeName(input);
        NetworkImageResponse networkImageResponse = this.service.getNetworkImage(input);
        String network_cinevo_id = Shared.generateCinevoId(this.networkRepository.getNetworkByTmdbId(networkDetailResponse.getId()));

        this.networkRepository.updateOrInsertNetwork(network_cinevo_id, networkDetailResponse.getId(), networkDetailResponse.getHeadquarters(),
                networkDetailResponse.getHeadquarters(), networkDetailResponse.getLogo_path(), networkDetailResponse.getName(),
                networkDetailResponse.getOrigin_country());

        for(NetworkAlternativeNameResponse.Name name : networkAlternativeNameResponse.getResults())
        {
            this.networkRepository.updateOrInsertNetworkAlternativeName(UUID.randomUUID().toString(), name.getName(), name.getType(),
                    network_cinevo_id);
        }

        for(NetworkImageResponse.Logo logo : networkImageResponse.getLogos())
        {

            this.networkRepository.updateOrInsertNetworkImage(UUID.randomUUID().toString(), logo.getId(), logo.getAspect_ratio(), logo.getFile_path(),
                    logo.getHeight(), logo.getWidth(), FileType.fromLabel(logo.getFile_type()).name(), logo.getVote_average(), logo.getVote_count(),
                    network_cinevo_id);
        }

        return this.networkRepository.getNetworkByTmdbId(networkDetailResponse.getId()).get();
    }
}
