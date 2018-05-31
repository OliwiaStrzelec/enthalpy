package com.enthalpy.model;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Vector {

    private List<Double> temperature;
    private List<Double> cp;
    private List<Double> enthalpy = new ArrayList<>();

    public Vector(List<Double> temperature, List<Double> cp) {
        this.temperature = temperature;
        this.cp = cp;
        countEnthalpy();
    }

    public Vector(List<Double> tempVector, List<Double> cpVector, List<Double> enthalpyVector) {
        this.setCp(cpVector);
        this.setEnthalpy(enthalpyVector);
        this.setTemperature(tempVector);
    }

    public void countEnthalpy() {
        countEnthalpy(0);
    }

    public void countEnthalpy(int startIndex) {
        for (int i = startIndex; i < this.temperature.size(); i++) {
            if (i == 0) {
                this.enthalpy.add(i, this.cp.get(i) * this.temperature.get(i));
                continue;
            }
            Double ent = this.enthalpy.get(i - 1) + (this.temperature.get(i) - this.temperature.get(i - 1)) * (this.cp.get(i) + this.cp.get(i - 1)) / 2;
            this.enthalpy.add(i, ent);
        }
    }


    public void printVector() {
        StringBuilder sb = new StringBuilder();
        sb.append("Index\t\tTemperature\t\tCp\t\tEnthalpy\n");
        for (int i = 0; i < this.temperature.size(); i++) {
            sb.append(i + "\t\t" + this.temperature.get(i) + "\t\t" + this.cp.get(i) + "\t\t" + this.enthalpy.get(i) + "\n");
        }
        System.out.println(sb);
    }

    public void insertVector(int startIndex, int endIndex, Vector vector) {
        if (startIndex < 0) {
            return;
        }
        if (startIndex > this.temperature.size() || endIndex > this.temperature.size()) {
            return;
        }
        if (endIndex < startIndex) {
            return;
        }
        if (null == vector) {
            return;
        }
        removeVector(startIndex, endIndex);
        addVector(startIndex, vector);
        updateEnthalpy(endIndex);

    }

    private void updateEnthalpy(int startIndex) {
        for (int i = startIndex; i < enthalpy.size(); i++) {
            Double ent = this.enthalpy.get(i - 1) + (this.temperature.get(i) - this.temperature.get(i - 1)) * (this.cp.get(i) + this.cp.get(i - 1)) / 2;
            this.enthalpy.set(i, ent);
        }
    }

    private void addVector(int startIndex, Vector vector) {
        if (null == vector.temperature || null == vector.cp || null == vector.enthalpy) {
            return;
        }
        this.temperature.addAll(startIndex, vector.temperature);
        this.cp.addAll(startIndex, vector.cp);
        this.enthalpy.addAll(startIndex, vector.enthalpy);
    }

    private void removeVector(int startIndex, int endIndex) {
        for (int i = startIndex; i < endIndex; i++) {
            this.enthalpy.remove(startIndex);
            this.temperature.remove(startIndex);
            this.cp.remove(startIndex);
        }
    }

    public List<Double> cpVector(double tempStart, double tempEnd, int size) {
        int startIndex = this.getIndex(tempStart);
        int endIndex = this.getIndex(tempEnd);
        double cp = (this.cp.get(startIndex) + this.cp.get(endIndex)) / 2;
        List<Double> cpVector = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            cpVector.add(cp);
        }
        return cpVector;
    }


    public int getIndex(double temp) {
        int index = Collections.binarySearch(this.getTemperature(), temp);
        if (index < 0) {
            index = Math.abs(index) - 1;
        }
        return index;
    }

    //TODO
    public void linear(double H) {
        double partialH = H / this.enthalpy.size();
        for (int i = 0; i < this.enthalpy.size(); i++) {
            enthalpy.set(i, enthalpy.get(i) + (i + 1) * partialH);
        }
    }

    public void jump(double H) {
        for(int i = (int)this.enthalpy.size()/2; i < this.enthalpy.size(); i++) {
            enthalpy.set(i, enthalpy.get(i) + H);
        }
    }

    public void exponential(double H) {
        double partialH = H/2;
        double sum = partialH;
        for (int i = 0; i < this.enthalpy.size() - 1; i++){
            enthalpy.set(i, enthalpy.get(i) + sum);
            partialH /= 2;
            sum += partialH;
        }
        enthalpy.set(this.enthalpy.size() - 1, enthalpy.get(this.enthalpy.size() - 1) + sum);
    }

    public List<Double> enthalpyVector(double tempStart, List<Double> tempDivisions, List<Double> cpVector) {
        int startIndex = this.getIndex(tempStart);
        List<Double> enthalpyVector = new ArrayList<>();
        enthalpyVector.add(0, this.enthalpy.get(startIndex));
        for (int i = 1; i < tempDivisions.size(); i++) {
            Double ent = enthalpyVector.get(i - 1) + (tempDivisions.get(i) - tempDivisions.get(i - 1)) * (cpVector.get(i) + cpVector.get(i - 1)) / 2;
            enthalpyVector.add(i, ent);
        }
        return enthalpyVector;
    }

    public Vector clone(){
        return this;
    }

    public static Vector getTemperatureAndCp(Scanner scanner, int rowsToSkip) {
        int i = 0;
        List<Double> temperature = new ArrayList<>();
        List<Double> cp = new ArrayList<Double>();
        while (scanner.hasNextLine()) {
            i++;
            if (i > rowsToSkip) {
                String string = scanner.nextLine();
                String[] split = string.split(" ");
                temperature.add(Double.valueOf(split[0]));
                cp.add(Double.valueOf(split[1]));
            } else {
                scanner.nextLine();
            }
        }
        return new Vector(temperature, cp);
    }

    public static Scanner getScanner(String path) {
        Scanner scanner = null;
        try {
            scanner = new Scanner(new FileReader(path));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return scanner;
    }

    public String getTemperatureAsJsonObject()
    {
        return new Gson().toJson(this.temperature);
    }

    public String getEnthalpyAsJsonObject()
    {
        return new Gson().toJson(this.enthalpy);
    }

}
