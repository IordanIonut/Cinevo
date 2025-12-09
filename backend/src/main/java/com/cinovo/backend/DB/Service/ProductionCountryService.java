package com.cinovo.backend.DB.Service;

import com.cinovo.backend.DB.Model.ProductionCountry;
import com.cinovo.backend.DB.Repository.ProductionCountryRepository;
import com.cinovo.backend.DB.Util.TMDBLogically;
import com.cinovo.backend.TMDB.Response.ConfigurationCountryResponse;
import jakarta.validation.constraints.Null;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductionCountryService implements TMDBLogically<Null, List<ProductionCountry>>
{
    private final ProductionCountryRepository productionCountryRepository;
    private final com.cinovo.backend.TMDB.Service service;

    public ProductionCountryService(ProductionCountryRepository productionCountryRepository, com.cinovo.backend.TMDB.Service service)
    {
        this.productionCountryRepository = productionCountryRepository;
        this.service = service;
    }

    public ProductionCountry getProductionCountryById(final String iso)
    {
        return this.productionCountryRepository.getProductionCountryById(iso).orElse(new ProductionCountry());
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
            ProductionCountry country = this.productionCountryRepository.getProductionCountryById(c.getIso_3166_1()).orElse(new ProductionCountry());
            country.setIso_3166_1(c.getIso_3166_1());
            country.setName(c.getNative_name());

            productionCountries.add(country);
        }

        this.productionCountryRepository.saveAll(productionCountries);
        return productionCountries;
    }
}
