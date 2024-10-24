package com.project.main.service;

import com.project.main.service.DataService;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class DataServiceTest {

    @Test
    public void testGetPaperIDs() throws IOException {
        String jsonString = "[" +
                "{\"paperId\": \"123\", \"title\": \"Paper One\"}," +
                "{\"paperId\": \"456\", \"title\": \"Paper Two\"}," +
                "{\"title\": \"Paper Without ID\"}," +
                "{\"paperId\": \"789\", \"title\": \"Paper Three\"}" +
                "]";

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode allPapers = objectMapper.readTree(jsonString);

        JSONArray result = DataService.getPaperIDs(allPapers);

        JSONArray expected = new JSONArray();
        expected.put("123");
        expected.put("456");
        expected.put("789");

        Assertions.assertEquals(expected.toString(), result.toString());
    }

    @Test
    public void testGetPaperIDsWithNonArrayInput() throws IOException {
        String jsonString = "{\"paperId\": \"123\", \"title\": \"Single Paper\"}";

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode allPapers = objectMapper.readTree(jsonString);

        JSONArray result = DataService.getPaperIDs(allPapers);

        JSONArray expected = new JSONArray();
        Assertions.assertEquals(expected.toString(), result.toString());
    }
}
