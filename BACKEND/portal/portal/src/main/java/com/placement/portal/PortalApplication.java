package com.placement.portal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PortalApplication {

    public static void main(String[] args) {
        SpringApplication.run(PortalApplication.class, args);
        System.out.println("===========================================");
        System.out.println("  Smart Placement Management System STARTED");
        System.out.println("  Running on: http://localhost:8080");
        System.out.println("===========================================");
    }

}