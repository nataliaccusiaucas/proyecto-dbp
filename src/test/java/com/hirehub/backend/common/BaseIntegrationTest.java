package com.hirehub.backend.common;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.lang.NonNull;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SuppressWarnings("resource")
@Testcontainers
@ContextConfiguration(initializers = BaseIntegrationTest.Initializer.class)
public abstract class BaseIntegrationTest {

    @Container
    public static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("hirehub_test")
            .withUsername("postgres")
            .withPassword("postgres");

    @BeforeAll
    static void startContainer() {
        POSTGRES.start();
        System.out.println("PostgreSQL container iniciado: " + POSTGRES.getJdbcUrl());
    }

    @AfterAll
    static void stopContainer() {
        POSTGRES.stop();
        System.out.println("PostgreSQL container detenido.");
    }

    @Configuration
    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
    public void initialize(@NonNull ConfigurableApplicationContext applicationContext) {
            TestPropertyValues.of(
                    "spring.datasource.url=" + POSTGRES.getJdbcUrl(),
                    "spring.datasource.username=" + POSTGRES.getUsername(),
                    "spring.datasource.password=" + POSTGRES.getPassword(),
                    "spring.datasource.driver-class-name=org.postgresql.Driver",
                    "spring.jpa.hibernate.ddl-auto=create-drop"
            ).applyTo(applicationContext.getEnvironment());
        }
    }
}
