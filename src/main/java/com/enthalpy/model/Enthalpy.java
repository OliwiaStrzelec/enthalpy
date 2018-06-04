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

    public static Vector transitionVector(Vector mainVector, double tempStart, double tempEnd, double H, String function) {
        if (!checkInput(tempStart, tempEnd, H)) {
            return null;
        }
        Vector transitionVector = getTransitionVector(mainVector, tempStart, tempEnd);
        return transitionVector;
//        if (function.equals("linear")) {
//            transitionVector.linear(H);
//        }
//        if(function.equals("exponential")){
//            transitionVector.exponential(H);
//        }
//        if(function.equals("jump")){
//            transitionVector.jump(H);
//        }
//        return transitionVector;
    }

    private static Vector getTransitionVector(Vector mainVector, double tempStart, double tempEnd) {
        int startIndex = mainVector.getIndex(tempStart);
        int endIndex = mainVector.getIndex(tempEnd);
        List<Double> tempVector = mainVector.getTemperature().subList(startIndex,endIndex);
        List<Double> cpVector = mainVector.getCp().subList(startIndex, endIndex);
        List<Double> enthalpyVector = mainVector.getEnthalpy().subList(startIndex, endIndex);
        return new Vector(tempVector, cpVector, enthalpyVector);
    }

}

