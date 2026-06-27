package com.placement.portal.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Full company profile shape sent to the frontend.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanyProfileResponse {
    private Long id;
    private String companyName;
    private String email;
    private String website;
    private String industry;
    private String description;
    private String contactPersonName;
    private String contactPhoneNumber;
    private String role;
}