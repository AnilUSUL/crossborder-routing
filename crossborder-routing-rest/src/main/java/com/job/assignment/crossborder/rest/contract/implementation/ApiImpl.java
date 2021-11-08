package com.job.assignment.crossborder.rest.contract.implementation;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.job.assignment.crossborder.rest.constant.RestControllerConstant;
import com.job.assignment.crossborder.rest.contract.Api;
import com.job.assignment.crossborder.rest.response.Resp;
import com.job.assignment.crossborder.rest.response.ResponseHandler;
import com.job.assignment.crossborder.service.algorithm.dijkstra.Node;
import com.job.assignment.crossborder.service.contractor.impl.RouteServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiImpl implements Api {
    @Autowired
    RouteServiceImpl routeService;

    @ResponseBody
    @Override
    public ResponseEntity<Object> sayHello() {
        return ResponseHandler.generateResponse(RestControllerConstant.SUCCESSFUL_READ_MESSAGE,HttpStatus.OK,"Hello");
    }

    @ResponseBody
    @Override
    public ResponseEntity<Object> getRouteFromService(String source, String destination) {
        Node resultNode = routeService.getborders(source, destination);
        ResponseEntity<Object> responseHandler;
        if(resultNode.getShortestPath().size() > 0){
            responseHandler = ResponseHandler.generateResponse(RestControllerConstant.SUCCESSFUL_READ_MESSAGE,HttpStatus.OK,
                    resultNode.getShortestPath().stream().map(c -> c.getName()).collect(Collectors.toList()));
        }else{
            responseHandler = ResponseHandler.generateResponse(RestControllerConstant.UNSUCCESSFUL_READ_MESSAGE,HttpStatus.BAD_REQUEST,new ArrayList<>());
        }
        return responseHandler;
    }
}
