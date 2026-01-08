package com.cinovo.backend.DB.Service;

import com.cinovo.backend.DB.Model.Country;
import com.cinovo.backend.DB.Model.Enum.MediaType;
import com.cinovo.backend.DB.Repository.CountryRepository;
import com.cinovo.backend.DB.Util.TMDBLogically;
import com.cinovo.backend.TMDB.Response.CertificationResponse;
import lombok.extern.jbosslog.JBossLog;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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
        return this.countryRepository.findCountryByMediaTypeAndCode(type.name(), code).orElse(null);
    }

    @Override
    @Transactional
    public List<Country> onConvertTMDB(MediaType type) throws Exception
    {
        for(Map.Entry<String, List<CertificationResponse.Certification>> country : this.service.getCertification(type).getCertifications().entrySet())
        {
            //            Country de = this.countryRepository.findCountryByMediaTypeAndCode(type.name(), country.getKey()).orElse(new Country());
            //            de.setCode(country.getKey());
            //            de.setType(type);
            //            de.setLastUpdate(LocalDate.now());
            //            List<Country.Certification> deCerts = new ArrayList<>();

            String countryId = UUID.randomUUID().toString();
            this.countryRepository.updateOrInsertCountry(countryId, country.getKey(), type.name());

            for(CertificationResponse.Certification certification : country.getValue())
            {
                //                Country.Certification cert =
                //                        this.countryRepository.findCountryCertificationByCountryCinevoId(de.getCinevo_id()).orElse(new Country.Certification());
                //                cert.setCertification(certification.getCertification());
                //                cert.setMeaning(certification.getMeaning());
                //                cert.setOrder(certification.getOrder());
                //                cert.setCountry(de);
                //
                //                deCerts.add(cert);
                this.countryRepository.updateOrInsertCertification(UUID.randomUUID().toString(), certification.getCertification(),
                        certification.getMeaning(), certification.getOrder(), countryId);
            }

            //            de.setCertifications(deCerts);
            //            countryRepository.save(de);
        }
        return this.findCountryByMediaType(type);
    }
}
