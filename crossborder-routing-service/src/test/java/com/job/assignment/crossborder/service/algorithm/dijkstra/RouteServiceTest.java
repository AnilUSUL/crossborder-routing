package com.job.assignment.crossborder.service.algorithm.dijkstra;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.job.assignment.crossborder.service.contractor.impl.RouteServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.stream.Collectors;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RouteServiceTest  {

    RouteServiceImpl routeService;

    LinkedList<JsonCountry> preparedData;
    
    @BeforeAll
    public void intialize() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        InputStream input = getClass().getClassLoader().getResource("data/countryJson.json").openStream();
        LinkedList<JsonCountry> resultList = objectMapper.readValue(input, new TypeReference<LinkedList<JsonCountry>>() {
        });
        routeService = new RouteServiceImpl(resultList);
    }
    @Test
    public void whenShortestHappyPathComplete_thenCorrect() {
        Node result = routeService.getborders("TUR", "FIN");
        Assertions.assertEquals("[TUR, GEO, RUS]", result.getShortestPath().stream().map(c -> c.getName()).collect(Collectors.toList()).toString());
    }

}
