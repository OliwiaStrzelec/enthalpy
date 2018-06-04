package com.enthalpy;

import com.enthalpy.model.Enthalpy;
import com.enthalpy.model.Vector;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.util.Scanner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EnthalpyApplicationTests {

	@Test
	public void contextLoads() {
	}

	@Test
	public void transition1() {
		Vector vector = getVector();
		double tempStart = 1420.;
		double tempEnd = 1460.;
		double H = 20.;
		Vector transitionVector = Enthalpy.transitionVector(vector, tempStart, tempEnd, H, "linear");
		System.out.println("Wektor po pierwszej przemianie");
		vector.printVector();
	}

	@Test
	public void transition2(){
		Vector vector = getVector();
		double tempStart = 1495.;
		double tempEnd = 1520.;
		double H = 150.;
		Vector transitionVector = Enthalpy.transitionVector(vector, tempStart, tempEnd, H, "exponential");
		System.out.println("Wektor po dwóch przemianach");
		vector.printVector();
	}

	private Vector getVector() {
		String path = new File("src/main/resources/static/Specific_Heat.txt").getAbsolutePath();
		Scanner scanner = Vector.getScanner(path);
		return Vector.getTemperatureAndCp(scanner, 5);
	}

}
