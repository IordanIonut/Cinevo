package com.cinovo.backend.DB.Service;

import com.cinovo.backend.DB.Model.Company;
import com.cinovo.backend.DB.Repository.CompanyRepository;
import com.cinovo.backend.DB.Util.Shared;
import com.cinovo.backend.DB.Util.TMDBLogically;
import com.cinovo.backend.TMDB.Response.Common.CompanyResponse;
import com.cinovo.backend.TMDB.Response.SearchResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CompanyService implements TMDBLogically<Object, Object>
{
    private final CompanyRepository companyRepository;
    private final com.cinovo.backend.TMDB.Service service;

    public CompanyService(CompanyRepository companyRepository, com.cinovo.backend.TMDB.Service service)
    {
        this.companyRepository = companyRepository;
        this.service = service;
    }

    public Company findCompanyByTmdbId(final Integer tmdb_id) throws Exception
    {
        Optional<Company> companyDetail = this.companyRepository.findCompanyByTmdbId(tmdb_id);
        if(companyDetail.isEmpty())
        {
            return (Company) this.onConvertTMDB(tmdb_id);
        }
        return companyDetail.get();
    }

    public List<Company> getCompanyUsingSearch(final String query, final Integer page) throws Exception
    {
        //TODO: Add query to my database to find by query
        return (List<Company>) this.onConvertTMDB(this.service.getSearchCompanyResponse(query, page));
    }

    @Override
    public Object onConvertTMDB(Object input) throws Exception
    {
        if(input instanceof SearchResponse<?>)
        {
            SearchResponse<?> rawResponse = (SearchResponse<?>) input;
            ObjectMapper mapper = new ObjectMapper();
            List<Company> companies = new ArrayList<>();
            for(Object obj : rawResponse.getResults())
            {
                CompanyResponse companyResponse = mapper.convertValue(obj, CompanyResponse.class);
                Company collection = this.generateCompany(companyResponse);
                companies.add(collection);
            }
            return companies;
        }
        else if(input instanceof Integer)
        {
            CompanyResponse response = this.service.getCompanyDetail((Integer) input);
            return this.generateCompany(response);
        }
        throw new IllegalArgumentException("Input of type object not find: " + input);
    }

    @Transactional
    public Company generateCompany(CompanyResponse company) throws Exception
    {
        this.companyRepository.upsertOrInsert(Shared.generateCinevoId(this.companyRepository.findCompanyByTmdbId(company.getId())), company.getId(),
                company.getDescription(), company.getHeadquarters(), company.getHomepage(), company.getLogo_path(), company.getName(),
                company.getOrigin_country(), null);
        return this.companyRepository.findCompanyByTmdbId(company.getId()).get();
    }

}
