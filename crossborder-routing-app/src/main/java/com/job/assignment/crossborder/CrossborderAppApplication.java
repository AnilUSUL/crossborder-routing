package com.job.assignment.crossborder;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.job.assignment.crossborder.constant.CrossboarderApplicationConstant;
import com.job.assignment.crossborder.service.algorithm.dijkstra.JsonCountry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;

@SpringBootApplication
@ComponentScan(basePackages={"com.job.assignment.crossborder"})
public class CrossborderAppApplication  {

	public static void main(String[] args) {
		SpringApplication.run(CrossborderAppApplication.class, args);
	}

	@Bean
	public LinkedList<JsonCountry> preparedData(String[] args) throws IOException {

		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		InputStream input = null;
		LinkedList<JsonCountry> resultList;
		try{
			input = getClass().getClassLoader().getResource(CrossboarderApplicationConstant.JSON_RESOURCE_PATH).openStream();
			resultList = objectMapper.readValue(input, new TypeReference<LinkedList<JsonCountry>>() {
			});
		}finally {
			input.close();
		}
		System.out.println("Processing Json into preparedData bean is completed.");

		return resultList;
	}
}
