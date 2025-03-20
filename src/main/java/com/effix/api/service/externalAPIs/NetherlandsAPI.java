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
import java.util.HashMap;
import java.util.List;

@Service
@Slf4j
public class NetherlandsAPI implements CompanyDataService {
    @Autowired
    private RestTemplate restTemplate;

    @Value("${netherlands.api.test.url}")
    private String apiURL;

    @Value("${netherlands.api.test.key}")
    private String apiKey;

    @Override
    public String getCountry() {
        return Country.NETHERLANDS.getCountry();
    }

    @Override
    public List<CompanyData> getCompanyData(String prefix) throws ServerException {
        try {
            String finalAPIURL = apiURL + "?naam=" + prefix + "&resultatenPerPagina=100";

            HttpHeaders headers = new HttpHeaders();
            headers.set("apikey", apiKey);
            headers.set("Content-Type", "application/json");

            // Create request entity with headers
            HttpEntity<String> requestEntity = new HttpEntity<>(headers);

            // Send request with headers
            ResponseEntity<ApiResponse> response = restTemplate.exchange(finalAPIURL, HttpMethod.GET, requestEntity, ApiResponse.class);

            if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
                log.error("Unable to fetch company data for netherlands for prefix {}", prefix);
                return new ArrayList<>();
            }

            ApiResponse apiResponse = response.getBody();
            if (apiResponse.getResultaten().isEmpty()) {
                return new ArrayList<>();
            }
            return apiResponse.getResultaten().stream().map(data -> {

                String location = null;
                String name = data.getNaam();
                HashMap<String, ApiResponseCompanyAddress> addressMap = data.getAdres();
                if (addressMap != null && !addressMap.isEmpty()) {
                    if (addressMap.containsKey("binnenlandsAdres")) {
                        ApiResponseCompanyAddress address = addressMap.get("binnenlandsAdres");
                        if (address != null) {
                            String addressType = address.getType();
                            if ("bezoekadres".equalsIgnoreCase(addressType)) {
                                location = address.getPlaats();
                            }
                        }
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
            log.error("Exception occurred while fetching company data for Netherlands with message {}", e.getMessage(), e);
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
        private List<ApiResponseCompany> resultaten;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    static class ApiResponseCompany {
        private String naam;
        private HashMap<String, ApiResponseCompanyAddress> adres;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    static class ApiResponseCompanyAddress {
        private String type;
        private String plaats;
    }
}
