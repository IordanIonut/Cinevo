package com.cinovo.backend.DB.Service;

import com.cinovo.backend.DB.Model.Enum.MediaType;
import com.cinovo.backend.DB.Model.External;
import com.cinovo.backend.DB.Repository.ExternalRepository;
import com.cinovo.backend.DB.Util.Shared;
import com.cinovo.backend.DB.Util.TMDBLogically;
import com.cinovo.backend.TMDB.Response.Common.MediaExternalIdResponse;
import com.cinovo.backend.TMDB.Response.PersonExternalIdsResponse;
import lombok.extern.jbosslog.JBossLog;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@JBossLog
public class ExternalService
{
    private final ExternalRepository externalRepository;
    private final com.cinovo.backend.TMDB.Service service;

    public ExternalService(ExternalRepository externalRepository, com.cinovo.backend.TMDB.Service service)
    {
        this.externalRepository = externalRepository;
        this.service = service;
    }

    public void setExternalByMediaType(final Integer tmdb_id, final String cinevo_id, final MediaType media_type, final Integer season_number,
            final Integer episode_number) throws Exception
    {
        MediaExternalIdResponse mediaExternalIdsResponse = new MediaExternalIdResponse();
        switch(media_type)
        {
            case MediaType.PERSON ->
            {
                PersonExternalIdsResponse personExternalResponse = this.service.getPersonExternalIds(tmdb_id);
                this.externalRepository.updateOrInsertExternal(Shared.generateCinevoId(this.externalRepository.findByFindCinevoIdOnFK(cinevo_id)),
                        personExternalResponse.getImdb_id(), personExternalResponse.getFreebase_mid(), personExternalResponse.getFreebase_id(), null,
                        personExternalResponse.getTvrage_id(), personExternalResponse.getWikidata_id(), personExternalResponse.getFacebook_id(),
                        personExternalResponse.getInstagram_id(), personExternalResponse.getTwitter_id(), personExternalResponse.getYoutube_id(),
                        personExternalResponse.getTiktok_id(), null, cinevo_id, null, null);
            }
            case MediaType.MOVIE ->
            {
                mediaExternalIdsResponse = service.getMovieExternalIds(tmdb_id);
                this.helpInsert(mediaExternalIdsResponse, cinevo_id, null, null, null);
            }
            case MediaType.TV ->
            {
                mediaExternalIdsResponse = service.getTvExternalIds(tmdb_id);
                this.helpInsert(mediaExternalIdsResponse, cinevo_id, null, null, null);
            }
            case MediaType.TV_EPISODE ->
            {
                mediaExternalIdsResponse = service.getTvSeasonEpisodeExternal(tmdb_id, season_number, episode_number);
                this.helpInsert(mediaExternalIdsResponse, null, null, null, cinevo_id);

            }
            case MediaType.TV_SEASON ->
            {
                mediaExternalIdsResponse = service.getTvSeasonExternalId(tmdb_id, season_number);
                this.helpInsert(mediaExternalIdsResponse, null, null, cinevo_id, null);
            }
            default ->
            {
                log.error("external " + tmdb_id + " will not find in any case");
            }
        }
    }

    private void helpInsert(final MediaExternalIdResponse mediaExternal, final String media_cinevo_id, final String person_cinevo_id,
            final String season_cinevo_id, final String episode_cinevo_id)
    {
        this.externalRepository.updateOrInsertExternal(Shared.generateCinevoId(this.externalRepository.findByFindCinevoIdOnFK(media_cinevo_id != null
                        ? media_cinevo_id
                        : person_cinevo_id != null ? person_cinevo_id : season_cinevo_id != null ? season_cinevo_id : episode_cinevo_id)),
                mediaExternal.getImdb_id(), mediaExternal.getFreebase_mid(), mediaExternal.getFreebase_id(), mediaExternal.getTvdb_id(),
                mediaExternal.getTvrage_id(), mediaExternal.getWikidata_id(), mediaExternal.getFacebook_id(), mediaExternal.getInstagram_id(),
                mediaExternal.getTwitter_id(), null, null, media_cinevo_id, person_cinevo_id, season_cinevo_id, episode_cinevo_id);
    }
}
