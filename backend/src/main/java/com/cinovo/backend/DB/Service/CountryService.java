package com.cinovo.backend.DB.Service;

import com.cinovo.backend.DB.Model.Country;
import com.cinovo.backend.DB.Repository.CountryRepository;
import com.cinovo.backend.DB.Util.TMDBLogically;
import com.cinovo.backend.Enum.Type;
import com.cinovo.backend.TMDB.DTO.CertificationResponse;
import lombok.extern.jbosslog.JBossLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@JBossLog
public class CountryService implements TMDBLogically<Type, List<Country>> {
    @Autowired
    private CountryRepository countryRepository;
    @Autowired
    private com.cinovo.backend.TMDB.Service service;

    public List<Country> findCountryByType(final Type type) throws Exception {
        Optional<List<Country>> countries = this.countryRepository.findCountryByType(type.name());
        if (countries.isEmpty() || countries.get().isEmpty()) {
            return this.onConvertTMDB(type);
        }
        return countries.get();
    }

    public Country findCountryByTypeAndCode(final Type type, final String code) {
        return this.countryRepository.findCountryByTypeAndCode(type.name(), code);
    }

    @Override
    public List<Country> onConvertTMDB(Type type) throws Exception {
        for (Map.Entry<String, List<CertificationResponse.Certification>> country : this.service.getCertification(type).getCertifications().entrySet()) {
            Country de = new Country();
            de.setCode(country.getKey());
            de.setType(type);
            de.setLastUpdate(LocalDate.now());
            List<Country.Certification> deCerts = new ArrayList<>();

            for (CertificationResponse.Certification certification : country.getValue()) {
                Country.Certification cert = new Country.Certification();
                cert.setCertification(certification.getCertification());
                cert.setMeaning(certification.getMeaning());
                cert.setOrder(certification.getOrder());
                cert.setCountry(de);
                deCerts.add(cert);
            }

            de.setCertifications(deCerts);
            countryRepository.save(de);
        }
        return this.countryRepository.findCountryByType(type.name()).get();
    }
}
