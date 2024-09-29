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

import java.nio.charset.StandardCharsets;

@Service
public class SemanticService {

    @Autowired
    private RestTemplate restTemplate; // for HTTP request

    private final String API_URL ="https://api.semanticscholar.org/graph/v1/paper/search/bulk";

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

        // System.out.println("Response Status Code: " + response.getStatusCode());
        // System.out.println("Response Headers: " + response.getHeaders());
        // System.out.println("Response Body: " + response.getBody());

        try {
            return response.getBody();
        } 
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
}
