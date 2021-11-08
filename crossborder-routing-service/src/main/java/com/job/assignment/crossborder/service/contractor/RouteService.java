package com.job.assignment.crossborder.service.contractor;

import com.job.assignment.crossborder.service.algorithm.dijkstra.Node;

public interface RouteService {
    Node getborders(String origin, String destination);
}
