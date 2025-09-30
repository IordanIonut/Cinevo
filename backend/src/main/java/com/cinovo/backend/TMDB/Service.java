package com.cinovo.backend.TMDB;


import com.cinovo.backend.Enum.Type;
import com.cinovo.backend.TMDB.DTO.*;
import org.springframework.stereotype.Component;

@Component
@org.springframework.stereotype.Service
public class Service {
    private final Client client;

    public Service(Client client) {
        this.client = client;
    }

    public GenresResponse getGenres(final Type type) throws Exception {
        return this.client.getGenres(type);
    }

    public CertificationResponse getCertification(final Type type) throws Exception {
        return this.client.getCertification(type);
    }

    public TranslationResponse getTranslate(final Integer id) throws Exception {
        return this.client.getTranslation(id);
    }

    public ImageResponse getImage(final Integer id) throws Exception {
        return this.client.getImage(id);
    }

    public CollectionDetailsResponse getCollectionDetail(final Integer id) throws Exception {
        return this.client.getCollectionDetail(id);
    }

    public CompanyDetailsResponse getCompanyDetail(final Integer id) throws Exception {
        return this.client.getCompanyDetail(id);
    }

    public TimezonesResponse[] getTimezones() throws Exception {
        return this.client.getTimezones();
    }

    public String[] getPrimaryTranslations() throws Exception{
        return this.client.getPrimaryTranslations();
    }

    public LanguageResponse[] getLanguages() throws Exception{
        return this.client.getLanguages();
    }
}
