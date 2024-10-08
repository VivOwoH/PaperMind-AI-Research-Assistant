package com.project.main.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class SemanticService {

    @Autowired
    private RestTemplate restTemplate; // for HTTP request

    private final String SEARCH_API_URL = "https://api.semanticscholar.org/graph/v1/paper/search/bulk";
    private final int PAPER_LIMIT = 20; // limit to 20 papers
    private final int CITATION_LIMIT = 5; // limit to 5 citations per paper

    public ResponseEntity<JsonNode> paperRelevanceSearch(String query) {
        String fields = "title,url,authors,citationCount,abstract,publicationTypes,publicationDate,openAccessPdf";

        UriComponents fullUrl = UriComponentsBuilder.fromHttpUrl(SEARCH_API_URL)
                .queryParam("query", UriUtils.encode(query, StandardCharsets.UTF_8))
                .queryParam("sort", UriUtils.encode("citationCount:desc", StandardCharsets.UTF_8))
                .queryParam("fields", UriUtils.encode(fields, StandardCharsets.UTF_8))
                .build(true);

        System.out.println("SearchAPI:" + fullUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(fullUrl.toUri(), HttpMethod.GET, entity, String.class);

        try {
            // parse JSON into java.util.Map<String, Object>
            ObjectMapper objectMapper = new ObjectMapper();
            java.util.Map<String, Object> responseBody = objectMapper.readValue(response.getBody(), new TypeReference<java.util.Map<String, Object>>() {});

            // extract the list of papers from the response
            List<java.util.Map<String, Object>> papers = objectMapper.convertValue(responseBody.get("data"), 
                                                    new TypeReference<List<java.util.Map<String, Object>>>() {});

            if (papers != null && !papers.isEmpty()) {
                if (papers.size() > PAPER_LIMIT) {
                    papers = papers.subList(0, 20);
                }
                JsonNode papersNode = objectMapper.convertValue(papers, JsonNode.class);
                return ResponseEntity.ok(papersNode);

            } else {
                return ResponseEntity.ok(objectMapper.createObjectNode().put("message", "No papers found for the query."));
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    public ResponseEntity<JsonNode> citationSearch(String paperID) {
        final String CITATION_API_URL = String.format("https://api.semanticscholar.org/graph/v1/paper/%s/citations", paperID);

        String fields = "authors,citationCount,abstract,publicationTypes,publicationDate,openAccessPdf";

        UriComponents fullUrl = UriComponentsBuilder.fromHttpUrl(CITATION_API_URL)
                .queryParam("fields", UriUtils.encode(fields, StandardCharsets.UTF_8))
                .queryParam("offset", UriUtils.encode("0", StandardCharsets.UTF_8))
                .queryParam("limit", UriUtils.encode(Integer.toString(CITATION_LIMIT), StandardCharsets.UTF_8))
                .build(true);

        System.out.println("CitationAPI: " + fullUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(fullUrl.toUri(), HttpMethod.GET, entity, String.class);

        try {
            ObjectMapper objectMapper = new ObjectMapper();

            java.util.Map<String, Object> responseBody = objectMapper.readValue(response.getBody(), new TypeReference<java.util.Map<String, Object>>() {});
            List<java.util.Map<String, Object>> data = objectMapper.convertValue(responseBody.get("data"), 
                                                    new TypeReference<List<java.util.Map<String, Object>>>() {});

            if (data != null && !data.isEmpty()) {
                JsonNode citationNode = objectMapper.convertValue(data, JsonNode.class);
                return ResponseEntity.ok(citationNode);

            } else {
                return ResponseEntity.ok(objectMapper.createObjectNode().put("message", "No citations found for the paper."));
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}

