package com.FM.OOPProject.utilities;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import com.FM.OOPProject.model.City;

@Service
public class TSVParse {
	public TSVParse() {

	}
	
	

	public static void ParseTSV(ArrayList<City> Cities) throws IOException {
		BufferedReader in3 = new BufferedReader(new FileReader("t1.tsv"));
		String line = in3.readLine(); // scarto la prima riga
		while ((line = in3.readLine()) != null) {
			String[] ss = line.split("\t");
			String[] ss1 = ss[0].split(",");
			float[] cpy = new float[29];
			for (int i = 1; i < ss.length; i++) {
				if (Pattern.matches("\\d+\\.\\d+ ", ss[i])) {
					cpy[i - 1] = Float.parseFloat(ss[i].split(" ")[0]); //numeri con virgola con lettera;
				} else if (Pattern.matches("\\d+ ", ss[i])) {
						cpy[i - 1] = Float.parseFloat(ss[i].split(" ")[0]); //interi con lettera
					} else if (Pattern.matches("\\d+", ss[i]))
							cpy[i - 1] = Float.parseFloat(ss[i]);
						else
							cpy[i - 1] = -1;

					}
				
			

			Cities.add(new City(ss1[0], ss1[1], cpy));
		}

		in3.close();

	}
}
