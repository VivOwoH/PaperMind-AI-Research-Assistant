package com.project.main.service;

import com.project.main.service.OpinionEdgeIdService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class OpinionEdgeIdServiceTest {

    private OpinionEdgeIdService opinionEdgeIdService;

    @BeforeEach
    void setUp() {
        opinionEdgeIdService = new OpinionEdgeIdService();
    }

    @Test
    void testGenerateRandomEdgeIdNotNull() {
        Integer edgeId = opinionEdgeIdService.generateRandomEdgeId();
        assertNotNull(edgeId, "Generated edge ID should not be null");
    }

    @Test
    void testGenerateRandomEdgeIdWithinRange() {
        Integer edgeId = opinionEdgeIdService.generateRandomEdgeId();
        assertTrue(edgeId >= 0 && edgeId < Integer.MAX_VALUE, "Generated edge ID should be within valid range");
    }

    @Test
    void testGenerateRandomEdgeIdRandomness() {
        Integer edgeId1 = opinionEdgeIdService.generateRandomEdgeId();
        Integer edgeId2 = opinionEdgeIdService.generateRandomEdgeId();
        assertNotEquals(edgeId1, edgeId2, "Generated edge IDs should generally be different");
    }
}
