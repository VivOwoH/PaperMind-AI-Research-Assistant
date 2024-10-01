package com.project.main.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.util.List;  // Import java.util.List
import java.util.Collections;

@Service
public class SemanticService {

    @Autowired
    private RestTemplate restTemplate; // for HTTP request

    private final String API_URL = "https://api.semanticscholar.org/graph/v1/paper/search/bulk";

    public String paperRelevanceSearch(String query) {
        String fields = "title,url,authors,citationCount,abstract,publicationTypes,publicationDate,openAccessPdf";

        UriComponents fullUrl = UriComponentsBuilder.fromHttpUrl(API_URL)
                .queryParam("query", UriUtils.encode(query, StandardCharsets.UTF_8))
                .queryParam("fields", UriUtils.encode(fields, StandardCharsets.UTF_8))
                .build(true);

        System.out.println(fullUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(fullUrl.toUri(), HttpMethod.GET, entity, String.class);

        try {
            // parse JSON into java.util.Map<String, Object>
            ObjectMapper objectMapper = new ObjectMapper();
            java.util.Map<String, Object> responseBody = objectMapper.readValue(response.getBody(), new TypeReference<java.util.Map<String, Object>>() {});

            // extract the list of papers from the response
            List<java.util.Map<String, Object>> papers = (List<java.util.Map<String, Object>>) responseBody.get("data");

            if (papers != null && !papers.isEmpty()) {

                // sort by mostly cited 
                papers.sort((p1, p2) -> {
                    Integer c1 = (Integer) p1.get("citationCount");
                    Integer c2 = (Integer) p2.get("citationCount");
                    if (c1 != null && c2 != null) {
                        return c2.compareTo(c1);  // descending order
                    }
                    return 0; // handling null values 
                });

                // convert the sorted list back to JSON string
                return objectMapper.writeValueAsString(papers);
            } else {
                return "No papers found for the query.";
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
