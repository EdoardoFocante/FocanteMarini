package com.FM.OOPProject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import org.json.JSONException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.FM.OOPProject.model.City;
import com.FM.OOPProject.utilities.TSVDownload;
import com.FM.OOPProject.utilities.TSVParse;

/**
 * Applicazione SpringBoot
 */
@SpringBootApplication
public class OopProjectApplication {
	/**
	 * attributo pubblico nella quale inserisco i dati del tsv dopo aver fatto il
	 * parsing
	 */
	public static ArrayList<City> Cities;

	/**
	 * 
	 * @param args argomenti della funzione main (non utilizzati)
	 * @throws IOException           Lanciata nel caso di errori di I/O
	 * @throws MalformedURLException Lanciata in caso di errori nella creazione
	 *                               dell'URL
	 * @throws JSONException         Lanciata in caso di errori nel parsing del JSON
	 */
	public static void main(String[] args) throws JSONException, MalformedURLException, IOException {
		Cities = new ArrayList<City>();
		TSVDownload.GetTSV(); // Download del tsv
		TSVParse.ParseTSV(Cities); // Parsing dei dati contenuti nel tsv in un array list di record
		SpringApplication.run(OopProjectApplication.class, args);
	}

}
