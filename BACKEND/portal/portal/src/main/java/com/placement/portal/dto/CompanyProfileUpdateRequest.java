package com.placement.portal.dto;

import lombok.Data;

/**
 * Shape of the JSON sent when a company updates its profile.
 * PUT /api/company/profile
 */
@Data
public class CompanyProfileUpdateRequest {
    private String companyName;
    private String website;
    private String industry;
    private String description;
    private String contactPersonName;
    private String contactPhoneNumber;
}