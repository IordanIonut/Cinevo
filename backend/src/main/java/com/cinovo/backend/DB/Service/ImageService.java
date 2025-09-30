package com.cinovo.backend.DB.Service;

import com.cinovo.backend.DB.Model.Image;
import com.cinovo.backend.DB.Repository.ImageRepository;
import com.cinovo.backend.DB.Util.TMDBLogically;
import com.cinovo.backend.TMDB.DTO.ImageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ImageService implements TMDBLogically<Integer, Image> {
    @Autowired
    private ImageRepository imageRepository;
    @Autowired
    private com.cinovo.backend.TMDB.Service service;

    public Image findImageById(final Integer id) throws Exception {
        Optional<Image> image = this.imageRepository.findImageById(id);
        if (image.isEmpty()) {
            return this.onConvertTMDB(id);
        }
        return image.get();
    }

    @Override
    public Image onConvertTMDB(Integer id) throws Exception {
        ImageResponse response = this.service.getImage(id);

        Image image = new Image();
        List<Image.BackDrop> backdropList = new ArrayList<>();
        List<Image.Poster> posterList = new ArrayList<>();

        image.setId(response.getId());
        for (ImageResponse.BackDrop backdrop : response.getBackdrops()) {
            Image.BackDrop backDrop = new Image.BackDrop();
            backDrop.setAspect_radio(backdrop.getAspect_ratio());
            backDrop.setHeight(backdrop.getHeight());
            backDrop.setWidth(backdrop.getWidth());
            backDrop.setIso(backdrop.getIso_639_1());
            backDrop.setFile_path(backdrop.getFile_path());
            backDrop.setVote_average(backdrop.getVote_average());
            backDrop.setVote_count(backdrop.getVote_count());
            backDrop.setImage(image);
            backdropList.add(backDrop);
        }

        for (ImageResponse.Poster pos : response.getPosters()) {
            Image.Poster poster = new Image.Poster();
            poster.setAspect_radio(pos.getAspect_ratio());
            poster.setHeight(pos.getHeight());
            poster.setWidth(pos.getWidth());
            poster.setIso(pos.getIso_639_1());
            poster.setFile_path(pos.getFile_path());
            poster.setVote_average(pos.getVote_average());
            poster.setVote_count(pos.getVote_count());
            poster.setImage(image);
            posterList.add(poster);
        }
        image.setBackdrops(backdropList);
        image.setPosters(posterList);
        this.imageRepository.save(image);

        return image;
    }
}
