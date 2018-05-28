package com.enthalpy.configuration;

import com.enthalpy.model.Vector;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;

@Configuration
public class ModelConfiguration {

    @Bean
    public Vector vector(){
        String path = new File("src/main/resources/static/Specific_Heat.txt").getAbsolutePath();
        Vector vector = Vector.getTemperatureAndCp(path, 5);
        return vector;
    }
}
