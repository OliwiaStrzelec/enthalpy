package com.enthalpy.model;

import com.enthalpy.model.Enthalpy;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

public class Main {


    public static void main(String[] args){
        //Pobieram dane z pliku
        String path = new File("src/main/resources/static/Specific_Heat.txt").getAbsolutePath();
        //Tworzę wektor z danych z pliku
        Scanner scanner = Vector.getScanner(path);
        Vector vector = Vector.getTemperatureAndCp(scanner, 5);
        //Drukuję początkowy wektor
        System.out.println("Wektor początkowy");
        vector.printVector();
        vector.applyLagrange();
        System.out.println("Stosowanie lagrangea");
        vector.printVector();
        double tempStart = 1420.;
        double tempEnd = 1460.;
        double H = 20.;
        Vector transitionVector = Enthalpy.transitionVector(vector, tempStart, tempEnd, H, "linear");
        vector.insertVector(vector.getIndex(tempStart), vector.getIndex(tempEnd),transitionVector);
        System.out.println("Wektor po pierwszej przemianie");
        vector.printVector();

        tempStart = 1495.;
        tempEnd = 1520.;
        H = 150.;
        transitionVector = Enthalpy.transitionVector(vector, tempStart, tempEnd, H, "exponential");
        vector.insertVector(vector.getIndex(tempStart), vector.getIndex(tempEnd),transitionVector);
        System.out.println("Wektor po dwóch przemianach");
        vector.printVector();
    }
}
