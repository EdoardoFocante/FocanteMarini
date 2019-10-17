package com.FM.OOPProject.utilities;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;
import com.FM.OOPProject.model.City;

/**
 * Classe che si occupa di convertire il file tsv in un' ArrayList di oggetti
 * City
 */
public class TSVParse {
	public TSVParse() {

	}

	/**
	 * Metodo che effettua il parsing del tsv in un' ArrayList di oggetti di tipo
	 * City
	 * 
	 * @param Cities Arraylist di City che andrà a contenere il risultato del
	 *               parsing
	 * @throws IOException per problemi con l'input e l'output del file
	 */
	public static void ParseTSV(ArrayList<City> Cities) throws IOException {
		BufferedReader in3 = new BufferedReader(new FileReader("t1.tsv"));
		String line = in3.readLine(); // scarto la prima riga
		while ((line = in3.readLine()) != null) {
			String[] ss = line.split("\t");
			String[] ss1 = ss[0].split(",");
			float[] cpy = new float[29];
			for (int i = 1; i < ss.length; i++) {
				if (Pattern.matches("\\d+\\.\\d+ ", ss[i])) { // numeri con virgola con lettera;
					cpy[i - 1] = Float.parseFloat(ss[i].split(" ")[0]);
				} else if (Pattern.matches("\\d+ ", ss[i])) { // interi con lettera
					cpy[i - 1] = Float.parseFloat(ss[i].split(" ")[0]);
				} else if (Pattern.matches("\\d+", ss[i])) // numeri interi con o senza virgola
					cpy[i - 1] = Float.parseFloat(ss[i]);
				else
					cpy[i - 1] = -1; // se non ho match il campo è invalido e inserisco un -1 che ignorerò nelle
										

			}

			Cities.add(new City(ss1[0], ss1[1], cpy));
		}

		in3.close();

	}
}
