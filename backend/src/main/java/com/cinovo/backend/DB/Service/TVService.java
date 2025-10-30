package com.cinovo.backend.DB.Service;

import com.cinovo.backend.DB.Model.TV;
import com.cinovo.backend.DB.Repository.TVRepository;
import com.cinovo.backend.DB.Util.TMDBLogically;
import com.cinovo.backend.Enum.Type;
import com.cinovo.backend.TMDB.Response.DiscoverTVResponse;
import jakarta.validation.constraints.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class TVService implements TMDBLogically<Null, List<TV>> {
    @Autowired
    private TVRepository tvRepository;
    @Autowired
    private com.cinovo.backend.TMDB.Service service;
    @Autowired
    private GenreService genreService;

    public List<TV> findTVForDiscovery() throws Exception {
        //TODO: add logically when find a id in database to stop to insert if not find continue to populate the database
        return this.onConvertTMDB(null);
    }

    @Override
    public List<TV> onConvertTMDB(Null input) throws Exception {
        DiscoverTVResponse discoverTVResponse = this.service.getDiscoverTV();
        List<TV> tvList = new ArrayList<>();
        for(DiscoverTVResponse.Result result: discoverTVResponse.getResults()){
            TV tv = new TV();
            tv.setAdult(result.getAdult());
            tv.setBackdrop_path(result.getBackdrop_path());
            tv.setGenres(this.genreService.parsLongToObjects(result.getGenre_ids(), Type.TV));
            tv.setId(result.getId());
            tv.setOrigin_country(String.join(", ", result.getOrigin_country()));
            tv.setOriginal_language(result.getOriginal_language());
            tv.setOriginal_name(result.getOriginal_name());
            tv.setOverview(result.getOverview());
            tv.setPopularity(result.getPopularity());
            tv.setPoster_path(result.getPoster_path());
            tv.setFirst_air_date(LocalDate.parse(result.getFirst_air_date()));
            tv.setName(result.getName());
            tv.setVote_average(result.getVote_average());
            tv.setVote_count(result.getVote_count());

            tvList.add(tv);
        }
        this.tvRepository.saveAll(tvList);
        return tvList;
    }
}
