package com.job.assignment.crossborder.rest.contract;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface Api {
    @GetMapping(value = "/helloworld")
    public ResponseEntity<Object> sayHello();
    @GetMapping(value = "/routing/{source}/{destination}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getRouteFromService(@PathVariable String origin, @PathVariable String destination);
}
