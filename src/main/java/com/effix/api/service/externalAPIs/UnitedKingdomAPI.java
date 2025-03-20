package com.effix.api.service.externalAPIs;

import com.effix.api.dto.response.profile.CompanyData;
import com.effix.api.enums.Country;
import com.effix.api.exception.ServerException;
import com.effix.api.service.CompanyDataService;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class UnitedKingdomAPI implements CompanyDataService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${uk.api.live.url}")
    private String apiURL;

    @Value("${uk.api.live.key}")
    private String apiKey;

    @Override
    public String getCountry() {
        return Country.UK.getCountry();
    }

    @Override
    public List<CompanyData> getCompanyData(String prefix) throws ServerException {
        try {
            String finalAPIURL = apiURL + "?company_name_includes=" + prefix + "&size=5000";
            HttpHeaders headers = new HttpHeaders();

            // Set Basic Authentication
            headers.setBasicAuth(apiKey, "");

            // Create request entity with headers
            HttpEntity<String> requestEntity = new HttpEntity<>(headers);

            // Send request with headers
            ResponseEntity<ApiResponse> response = restTemplate.exchange(finalAPIURL, HttpMethod.GET, requestEntity, ApiResponse.class);

            if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
                log.error("Unable to fetch company data for UK for prefix {}", prefix);
                return new ArrayList<>();
            }

            ApiResponse apiResponse = response.getBody();
            if (apiResponse.getItems().isEmpty()) {
                return new ArrayList<>();
            }
            return apiResponse.getItems().stream().map(data -> {
                String location = null;
                String name = data.getCompanyName();
                ApiResponseCompanyAddress addressMap = data.getRegisteredOfficeAddress();

                if (addressMap != null) {
                    if (addressMap.getLocality() != null && !addressMap.getLocality().isEmpty()) {
                        location = addressMap.getLocality();
                    }
                }
                return CompanyData.builder()
                        .companyName(name)
                        .location(location)
                        .country(getCountry().toLowerCase())
                        .build();
            }).toList();

        }
        catch (HttpClientErrorException e) {
            if(HttpStatus.NOT_FOUND.equals(e.getStatusCode())){
                return new ArrayList<>();
            }
        }
        catch (Exception e) {
            log.error("Exception occurred while fetching company data for UK with message {}", e.getMessage(), e);
            throw new ServerException("Exception occurred while fetching company data");
        }
        return new ArrayList<>();
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    static class ApiResponse {
        private List<ApiResponseCompany> items;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    static class ApiResponseCompany {
        private String companyName;
        private ApiResponseCompanyAddress registeredOfficeAddress;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    static class ApiResponseCompanyAddress {
        private String locality;
    }
}
