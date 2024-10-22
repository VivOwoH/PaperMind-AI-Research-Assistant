package com.project.main.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.JsonNode;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.Iterator;


@Service
public class DataService {

    public static JSONArray getPaperIDs(JsonNode allPapers) {
        JSONArray jsonArray = new JSONArray();

        if (allPapers.isArray()) {
            for (JsonNode node : allPapers) {
                JsonNode paperIdNode = node.get("paperId");

                if (paperIdNode != null) {
                    jsonArray.put(paperIdNode.asText());
                }
            }
        }

        return jsonArray;
    }
}