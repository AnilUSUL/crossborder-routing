package com.job.assignment.crossborder.service.algorithm.dijkstra;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.List;

public class JsonCountry {
    String id;
    List<BigDecimal> latlng;
    List<String> borders;

    public List<BigDecimal> getLatlng() {
        return latlng;
    }
    public List<String> getBorders() {
        return borders;
    }
    @JsonProperty("cca3")
    public String getId() {
        return id;
    }
    public void setBorders(List<String> borders) {
        this.borders = borders;
    }
    @JsonProperty("cca3")
    public void setId(String id) {
        this.id = id;
    }
    public void setLatlng(List<BigDecimal> latlng) {
        this.latlng = latlng;
    }
}
