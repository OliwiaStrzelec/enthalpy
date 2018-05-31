package com.enthalpy.service;

import com.enthalpy.model.Vector;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Scanner;

@Service
public class VectorService {
    @Getter
    @Setter
    private Vector vector;

    public void newVector() {
            String path = new File("src/main/resources/static/Specific_Heat.txt").getAbsolutePath();
            Scanner scanner = Vector.getScanner(path);
            this.setVector(Vector.getTemperatureAndCp(scanner, 5));
        }
    }
