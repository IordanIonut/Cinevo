package com.cinovo.backend.DB.Service;

import com.cinovo.backend.DB.Model.ProductionCountry;
import com.cinovo.backend.DB.Repository.ProductionCountryRepository;
import com.cinovo.backend.DB.Util.Shared;
import com.cinovo.backend.DB.Util.TMDBLogically;
import com.cinovo.backend.TMDB.Response.ConfigurationCountryResponse;
import jakarta.validation.constraints.Null;
import lombok.extern.jbosslog.JBossLog;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@JBossLog
public class ProductionCountryService implements TMDBLogically<Null, List<ProductionCountry>>
{
    private final ProductionCountryRepository productionCountryRepository;
    private final com.cinovo.backend.TMDB.Service service;

    public ProductionCountryService(ProductionCountryRepository productionCountryRepository, com.cinovo.backend.TMDB.Service service)
    {
        this.productionCountryRepository = productionCountryRepository;
        this.service = service;
    }

    public ProductionCountry getByIso(final String iso)
    {
        return this.productionCountryRepository.getByIso(iso).orElse(new ProductionCountry());
    }

    public List<ProductionCountry> findAllProductionCountry() throws Exception
    {
        Optional<List<ProductionCountry>> productionCountries = this.productionCountryRepository.findAllProductionCountry();
        if(productionCountries.isEmpty() || productionCountries.get().isEmpty())
        {
            return (List<ProductionCountry>) onConvertTMDB(null);
        }
        return productionCountries.get();
    }

    @Override
    @Transactional
    public List<ProductionCountry> onConvertTMDB(Null input) throws Exception
    {
        ConfigurationCountryResponse[] countryResponses = this.service.getConfigurationCountries("en-US");
        List<ProductionCountry> productionCountries = new ArrayList<>();

        for(ConfigurationCountryResponse c : countryResponses)
        {
            this.productionCountryRepository.updateOrInsert(Shared.generateCinevoId(this.productionCountryRepository.getByIso(c.getIso_3166_1())),
                    c.getIso_3166_1(), c.getEnglish_name());
            productionCountries.add(this.productionCountryRepository.getByIso(c.getIso_3166_1()).get());
        }

        return productionCountries;
    }
}
