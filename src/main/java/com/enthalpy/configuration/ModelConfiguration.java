package com.enthalpy.configuration;

import com.enthalpy.model.Vector;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.util.Scanner;

@Configuration
public class ModelConfiguration {

    @Bean
    public Vector vector(){
        String path = new File("src/main/resources/static/Specific_Heat.txt").getAbsolutePath();
        Scanner scanner = Vector.getScanner(path);
        Vector vector = Vector.getTemperatureAndCp(scanner, 5);
        return vector;
    }
}
