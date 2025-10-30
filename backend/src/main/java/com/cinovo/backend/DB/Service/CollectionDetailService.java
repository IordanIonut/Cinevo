package com.cinovo.backend.DB.Service;

import com.cinovo.backend.DB.Model.CollectionDetail;
import com.cinovo.backend.DB.Repository.CollectionDetailRepository;
import com.cinovo.backend.DB.Util.TMDBLogically;
import com.cinovo.backend.Enum.Type;
import com.cinovo.backend.TMDB.Response.CollectionDetailsResponse;
import lombok.extern.jbosslog.JBossLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@JBossLog
public class CollectionDetailService implements TMDBLogically<Integer, CollectionDetail> {
    @Autowired
    private CollectionDetailRepository detailRepository;
    @Autowired
    private GenreService genreService;
    @Autowired
    private com.cinovo.backend.TMDB.Service service;

    public CollectionDetail findDetailById(final Integer id) throws Exception {
        Optional<CollectionDetail> detail = this.detailRepository.findCollectionDetailById(id);
        if (detail.isEmpty()) {
            return this.onConvertTMDB(id);
        }
        return detail.get();
    }

    @Override
    public CollectionDetail onConvertTMDB(Integer id) throws Exception {
        CollectionDetailsResponse response = this.service.getCollectionDetail(id);
        CollectionDetail detail = new CollectionDetail();
        detail.setId(response.getId());
        detail.setName(response.getName());
        detail.setOverview(response.getOverview());
        detail.setPoster_path(response.getPoster_path());
        detail.setBackdrop_path(response.getBackdrop_path());
        List<CollectionDetail.Part> parts = new ArrayList<>();
        for (CollectionDetailsResponse.Part part : response.getParts()) {
            CollectionDetail.Part detailPart = new CollectionDetail.Part();
            detailPart.setAdult(part.getAdult());
            detailPart.setBackdrop_path(part.getBackdrop_path());
            detailPart.setId(part.getId());
            detailPart.setTitle(part.getTitle());
            detailPart.setOriginal_title(part.getOriginal_title());
            detailPart.setOverview(part.getOverview());
            detailPart.setPoster_path(part.getPoster_path());
            detailPart.setType(Type.valueOf(part.getMedia_type().toUpperCase()));
            detailPart.setOriginal_language(part.getOriginal_language());
            detailPart.setPopularity(part.getPopularity());
            detailPart.setRelease_date(LocalDate.parse(part.getRelease_date()));
            detailPart.setVideo(part.getVideo());
            detailPart.setVote_average(part.getVote_average());
            detailPart.setVote_count(part.getVote_count());
            detailPart.setDetail(detail);
            detailPart.setGenres(this.genreService.parsLongToObjects(part.getGenre_ids(), detailPart.getType()));

            parts.add(detailPart);
        }
        detail.setParts(parts);
        this.detailRepository.save(detail);
        return detail;
    }
}
