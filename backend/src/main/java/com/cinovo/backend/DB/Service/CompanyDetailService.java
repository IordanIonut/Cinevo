package com.cinovo.backend.DB.Service;

import com.cinovo.backend.DB.Model.CompanyDetail;
import com.cinovo.backend.DB.Repository.CompanyDetailRepository;
import com.cinovo.backend.DB.Util.TMDBLogically;
import com.cinovo.backend.TMDB.DTO.CompanyDetailsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CompanyDetailService implements TMDBLogically<Integer, CompanyDetail> {
    @Autowired
    private CompanyDetailRepository companyDetailRepository;
    @Autowired
    private com.cinovo.backend.TMDB.Service service;

    public CompanyDetail findCompanyDetailById(final Integer id) throws Exception {
        Optional<CompanyDetail> companyDetail = this.companyDetailRepository.findCompanyDetailById(id);
        if (companyDetail.isEmpty()) {
            return this.onConvertTMDB(id);
        }
        return companyDetail.get();
    }

    @Override
    public CompanyDetail onConvertTMDB(Integer id) throws Exception {
        CompanyDetailsResponse response = this.service.getCompanyDetail(id);
        CompanyDetail companyDetail = new CompanyDetail();
        companyDetail.setId(response.getId());
        companyDetail.setDescription(response.getDescription());
        companyDetail.setHeadquarters(response.getHeadquarters());
        companyDetail.setHomepage(response.getHomepage());
        companyDetail.setLogo_path(response.getLogo_path());
        companyDetail.setName(response.getName());
        companyDetail.setOrigin_country(response.getOrigin_country());
        companyDetail.setParent_company(null);

        this.companyDetailRepository.save(companyDetail);
        return companyDetail;
    }
}
