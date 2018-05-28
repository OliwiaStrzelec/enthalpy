package com.enthalpy.model;

import java.util.ArrayList;
import java.util.List;

public class Enthalpy {

    public static boolean checkInput(double tempStart, double tempEnd, double enthalpy) {
        boolean b = false;
        if (tempStart < 56.) {
            return b;
        }
        if (tempStart > tempEnd) {
            return b;
        }
        if (tempEnd > 1550.) {
            return b;
        }
        if (enthalpy <= 0) {
            return b;
        }
        return true;
    }

    public static List<Double> countTempDivisions(double tempStart, double tempEnd, double degrees) {
        List<Double> temperature = new ArrayList<>();
        double temp = tempStart;
        while (temp < tempEnd) {
            temperature.add(temp);
            temp += degrees;
        }
        temperature.add(tempEnd);
        return temperature;
    }

    public static Vector transitionVector(Vector mainVector, double tempStart, double tempEnd, double H, String function) {
        if (!checkInput(tempStart, tempEnd, H)) {
            return null;
        }
        double degrees = 2.;
        Vector transitionVector = getTransitionVector(mainVector, tempEnd, degrees, tempStart);
        if (function.equals("linear")) {
            transitionVector.linear(H);
        }
        if(function.equals("exponential")){
            transitionVector.exponential(H);
        }
        return transitionVector;
    }

    private static Vector getTransitionVector(Vector mainVector, double tempEnd, double degrees, double tempStart) {
        List<Double> tempDivisions = countTempDivisions(tempStart, tempEnd, degrees);
        List<Double> cpVector = mainVector.cpVector(tempStart, tempEnd, tempDivisions.size());
        List<Double> enthalpyVector = mainVector.enthalpyVector(tempStart, tempDivisions, cpVector);
        return new Vector(tempDivisions, cpVector, enthalpyVector);
    }

}

