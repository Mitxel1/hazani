package com.utng.edu.prueba;

import com.sun.tools.javac.Main;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.info.BuildProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.boot.SpringApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@SpringBootApplication
@EnableScheduling
@Slf4j
@EnableAsync
@EnableCaching

public class AppMain implements CommandLineRunner {
    @Autowired
    private ApplicationContext context;
    public static void main(String[] args) {
        SpringApplication.run(AppMain.class, args);
    }

    private static void displayInfo(BuildProperties buildProperties) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
                .withZone(ZoneId.systemDefault());
        String out = formatter.format(buildProperties.getTime());

        log.info("Nombre artefacto: " + buildProperties.getName() +
                ", Versión: " + buildProperties.getVersion() +
                ", Fecha Compilación: " + out +
                ", Artefacto: " + buildProperties.getArtifact() +
                ", Grupo: " + buildProperties.getGroup());
    }

    @Override
    public void run(String... args) {
        BuildProperties buildProperties = context.getBean(BuildProperties.class);
        displayInfo(buildProperties);
    }
}
