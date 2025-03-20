package com.effix.api.service.externalAPIs;

import com.effix.api.exception.ServerException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
public class DistanceAPI {
    @Autowired
    private RestTemplate restTemplate;

    @Value("${distance.api.url}")
    private String apiURL;

    @Value("${distance.api.key}")
    private String apiKey;

    @Value("${distance.api.host}")
    private String apiHost;

    public Float[] getLandAndAirDistance(String source, String destination) throws ServerException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-RapidAPI-Key", apiKey);
            headers.set("X-RapidAPI-Host", apiHost);
            headers.set("Content-Type", "application/json");

            // Create the route JSON structure
            List<Route> route = new ArrayList<>();
            route.add(new Route(source));
            route.add(new Route(destination));

            // Create the request body JSON
            RouteRequest requestBody = new RouteRequest(route);

            // Convert request body to JSON string
            ObjectMapper mapper = new ObjectMapper();
            String jsonBody = mapper.writeValueAsString(requestBody);

            // Create request entity with headers
            HttpEntity<String> requestEntity = new HttpEntity<>(jsonBody, headers);

            // Send request with headers
            ResponseEntity<ApiResponse> response = restTemplate.exchange(apiURL, HttpMethod.POST, requestEntity, ApiResponse.class);

            if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
                log.error("Unable to fetch distance data for source {} and destination {}", source, destination);
                throw new ServerException("Unable to fetch distance data");
            }

            ApiResponse apiResponse = response.getBody();
            if (apiResponse.getRoute() == null) {
                throw new ServerException("Unable to fetch distance data");
            }

            float airDistance = apiResponse.getRoute().getHaversine();
            float landDistance = apiResponse.getRoute().getCar().getDistance();

            return new Float[]{airDistance, landDistance};
        } catch (HttpClientErrorException e) {
            if (HttpStatus.NOT_FOUND.equals(e.getStatusCode())) {
                throw new ServerException("Exception occurred while fetching company data");
            }
        } catch (Exception e) {
            log.error("Exception occurred while fetching company data for Netherlands with message {}", e.getMessage(), e);
            throw new ServerException("Exception occurred while fetching company data");
        }
        throw new ServerException("Exception occurred while fetching company data");
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    static class ApiResponse {
        private RouteApiResponse route;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    static class RouteApiResponse {
        private float haversine;
        private CarApiResponse car;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    static class CarApiResponse {
        private float distance;
        private String status;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    static class Route {
        private String name;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    static class RouteRequest {
        private List<Route> route;
    }
}
