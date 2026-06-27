package com.placement.portal.service;

import com.placement.portal.dto.CompanyProfileResponse;
import com.placement.portal.dto.CompanyProfileUpdateRequest;
import com.placement.portal.entity.Company;
import com.placement.portal.exception.ApiException;
import com.placement.portal.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;

    private Company getCompanyByEmail(String email) {
        return companyRepository.findByEmail(email)
                .orElseThrow(() -> new ApiException("Company not found"));
    }

    public CompanyProfileResponse getProfile(String email) {
        return toResponse(getCompanyByEmail(email));
    }

    public CompanyProfileResponse updateProfile(String email, CompanyProfileUpdateRequest request) {
        Company company = getCompanyByEmail(email);

        if (request.getCompanyName() != null) company.setCompanyName(request.getCompanyName());
        if (request.getWebsite() != null) company.setWebsite(request.getWebsite());
        if (request.getIndustry() != null) company.setIndustry(request.getIndustry());
        if (request.getDescription() != null) company.setDescription(request.getDescription());
        if (request.getContactPersonName() != null) company.setContactPersonName(request.getContactPersonName());
        if (request.getContactPhoneNumber() != null) company.setContactPhoneNumber(request.getContactPhoneNumber());

        Company saved = companyRepository.save(company);
        return toResponse(saved);
    }

    private CompanyProfileResponse toResponse(Company c) {
        return new CompanyProfileResponse(
                c.getId(),
                c.getCompanyName(),
                c.getEmail(),
                c.getWebsite(),
                c.getIndustry(),
                c.getDescription(),
                c.getContactPersonName(),
                c.getContactPhoneNumber(),
                c.getRole()
        );
    }
}