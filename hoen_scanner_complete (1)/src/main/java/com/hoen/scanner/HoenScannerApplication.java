package com.hoen.scanner;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class HoenScannerApplication extends Application<io.dropwizard.Configuration> {

    public static void main(String[] args) throws Exception {
        new HoenScannerApplication().run(args);
    }

    @Override
    public void initialize(Bootstrap<io.dropwizard.Configuration> bootstrap) {
    }

    @Override
    public void run(io.dropwizard.Configuration configuration, Environment environment) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        List<SearchResult> searchResults = new ArrayList<>();

        try (InputStream rentalCarsStream = getClass().getClassLoader().getResourceAsStream("rental_cars.json");
             InputStream hotelsStream = getClass().getClassLoader().getResourceAsStream("hotels.json")) {

            List<SearchResult> rentalCars = mapper.readValue(rentalCarsStream, new TypeReference<List<SearchResult>>() {});
            List<SearchResult> hotels = mapper.readValue(hotelsStream, new TypeReference<List<SearchResult>>() {});

            searchResults.addAll(rentalCars);
            searchResults.addAll(hotels);
        }

        final SearchResource resource = new SearchResource(searchResults);
        environment.jersey().register(resource);
    }
}
