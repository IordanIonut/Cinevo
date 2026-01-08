package com.cinovo.backend.DB.Service;

import com.cinovo.backend.DB.Model.Enum.FileType;
import com.cinovo.backend.DB.Model.Network;
import com.cinovo.backend.DB.Repository.NetworkRepository;
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
        //        List<Network.NetworkAlternativeName> networkAlternativeNames = new ArrayList<>();
        //        List<Network.NetworkImage> networkImages = new ArrayList<>();
        //
        //        Network network = this.networkRepository.getNetworkByTmdbId(networkDetailResponse.getId()).orElse(new Network());
        //        network.setTmdb_id(networkDetailResponse.getId());
        //        network.setHeadquarters(networkDetailResponse.getHeadquarters());
        //        network.setHomepage(networkDetailResponse.getHomepage());
        //        network.setLogo_path(networkDetailResponse.getLogo_path());
        //        network.setName(networkDetailResponse.getName());
        //        network.setOrigin_country(networkDetailResponse.getOrigin_country());

        String network_cinevo_id = UUID.randomUUID().toString();

        this.networkRepository.updateOrInsertNetwork(network_cinevo_id, networkDetailResponse.getId(), networkDetailResponse.getHeadquarters(),
                networkDetailResponse.getHeadquarters(), networkDetailResponse.getLogo_path(), networkDetailResponse.getName(),
                networkDetailResponse.getOrigin_country());

        for(NetworkAlternativeNameResponse.Name name : networkAlternativeNameResponse.getResults())
        {
            //            Network.NetworkAlternativeName networkAlternativeName = new Network.NetworkAlternativeName();
            //            networkAlternativeName.setName(name.getName());
            //            networkAlternativeName.setType(name.getType());
            //            networkAlternativeName.setNetwork(network);
            //
            //            networkAlternativeNames.add(networkAlternativeName);

            this.networkRepository.updateOrInsertNetworkAlternativeName(UUID.randomUUID().toString(), name.getName(), name.getType(),
                    network_cinevo_id);
        }
        //        network.setAlternative_names(networkAlternativeNames);

        for(NetworkImageResponse.Logo logo : networkImageResponse.getLogos())
        {
            //            Network.NetworkImage networkImage = new Network.NetworkImage();
            //            networkImage.setTmdb_id(logo.getId());
            //            networkImage.setAspect_ratio(logo.getAspect_ratio());
            //            networkImage.setFile_path(logo.getFile_path());
            //            networkImage.setHeight(logo.getHeight());
            //            networkImage.setWidth(logo.getWidth());
            //            networkImage.setFile_type(FileType.fromLabel(logo.getFile_type()));
            //            networkImage.setVote_average(logo.getVote_average());
            //            networkImage.setVote_count(logo.getVote_count());
            //            networkImage.setNetwork(network);
            //
            //            networkImages.add(networkImage);

            this.networkRepository.updateOrInsertNetworkImage(UUID.randomUUID().toString(), logo.getId(), logo.getAspect_ratio(), logo.getFile_path(),
                    logo.getHeight(), logo.getWidth(), FileType.fromLabel(logo.getFile_type()).name(), logo.getVote_average(), logo.getVote_count(),
                    network_cinevo_id);
        }
        //        network.setImages(networkImages);
        //
        //        return this.networkRepository.save(network);

        return this.networkRepository.getNetworkByTmdbId(networkDetailResponse.getId()).get();
    }
}
