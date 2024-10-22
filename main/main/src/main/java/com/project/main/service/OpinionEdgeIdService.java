package com.project.main.service;

import org.springframework.stereotype.Service;
import java.util.Random;

@Service
public class OpinionEdgeIdService {

    public Integer generateRandomEdgeId() {
        Random random = new Random();
        return random.nextInt(Integer.MAX_VALUE);
    }
}
