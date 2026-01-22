package com.cinovo.backend.DB.Service;

import com.cinovo.backend.DB.Model.Country;
import com.cinovo.backend.DB.Model.Enum.MediaType;
import com.cinovo.backend.DB.Repository.CountryRepository;
import com.cinovo.backend.DB.Util.Shared;
import com.cinovo.backend.DB.Util.TMDBLogically;
import com.cinovo.backend.TMDB.Response.CertificationResponse;
import lombok.extern.jbosslog.JBossLog;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@JBossLog
public class CountryService implements TMDBLogically<MediaType, List<Country>>
{
    private final CountryRepository countryRepository;
    private final com.cinovo.backend.TMDB.Service service;

    public CountryService(CountryRepository countryRepository, com.cinovo.backend.TMDB.Service service)
    {
        this.countryRepository = countryRepository;
        this.service = service;
    }

    public List<Country> findCountryByMediaType(final MediaType type) throws Exception
    {
        Optional<List<Country>> countries = this.countryRepository.findCountryByMediaType(type.name());
        if(countries.isEmpty() || countries.get().isEmpty())
        {
            return this.onConvertTMDB(type);
        }
        return countries.get();
    }

    public Country findCountryByMediaTypeAndCode(final MediaType type, final String code)
    {
        Optional<Country> country = this.countryRepository.findCountryByMediaTypeAndCode(type.name(), code);
        if(country.isEmpty())
        {
            this.countryRepository.updateOrInsertCountry(UUID.randomUUID().toString(), code, type.name());
            return this.countryRepository.findCountryByMediaTypeAndCode(type.name(), code).get();
        }
        return country.get();
    }

    @Override
    @Transactional
    public List<Country> onConvertTMDB(MediaType type) throws Exception
    {
        for(Map.Entry<String, List<CertificationResponse.Certification>> country : this.service.getCertification(type).getCertifications().entrySet())
        {
            String country_cinevo_id = Shared.generateCinevoId(this.countryRepository.findCountryByMediaTypeAndCode(type.name(), country.getKey()));
            this.countryRepository.updateOrInsertCountry(country_cinevo_id, country.getKey(), type.name());

            for(CertificationResponse.Certification certification : country.getValue())
            {
                this.countryRepository.updateOrInsertCertification(
                        Shared.generateCinevoId(this.countryRepository.findCountryCertificationByCountryCinevoId(country_cinevo_id)),
                        certification.getCertification(), certification.getMeaning(), certification.getOrder(), country_cinevo_id);
            }
        }
        return this.findCountryByMediaType(type);
    }
}
