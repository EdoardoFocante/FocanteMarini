package com.FM.OOPProject.utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * Classe che si occupa del download del TSV a partire dal JSON assegnatoci 
 */
public class TSVDownload {
	
	/**
	 * Costruttore vuoto
	 */
	public TSVDownload() {
}
	/**
	 * Funzione che effettua il download del tsv dall'url fornito nel progetto
	 * 
	 * @throws IOException Per problemi con l'IO
	 * @throws MalformedURLException Per problemi con l'URL
	 * @throws JSONException Per problemi con il parsing del JSON
	 * 
	 * 
	 */
	public static void GetTSV() throws JSONException, MalformedURLException, IOException {
		if (!Files.exists(Paths.get("t.tsv"))) {
			JSONObject obj = new JSONObject(readurl("http://data.europa.eu/euodp/data/api/3/action/package_show?id=ojAmzVahjBnws2njEN0qhQ"));
			JSONArray arr = obj.getJSONObject("result").getJSONArray("resources");
			String html = new String();
			for (int i = 0; i < arr.length(); i++) {
				if (arr.getJSONObject(i).getString("format").equals("http://publications.europa.eu/resource/authority/file-type/TSV")) {
					html = readurl(arr.getJSONObject(i).getString("url"));  //leggo il file html come stringa
					break;
				}
			}
			Document document = Jsoup.parse(html); //parsing del file html
			download(document.select("a").first().attr("href"), "t1.tsv"); // estraggo il link al tsv e scarico il file
		}

	}
	/**
	 * Funzione che effettua il download di un file da url e lo salva in filename
	 * @param url Indirizzo da cui viene scaricato il file
	 * @param fileName Nome del file in cui verrà salvato il file scaricato
	 * @throws MalformedURLException per 
	 * @throws IOException 
	 */
	public static void download(String url, String fileName) throws MalformedURLException, IOException {
		try (InputStream in = URI.create(url).toURL().openStream()) {
			Files.copy(in, Paths.get(fileName),StandardCopyOption.REPLACE_EXISTING);
		}

	}
	/**
	 * 
	 * @param link
	 * @return Stringa contenente il tsv
	 * @throws MalformedURLException Per problemi con l'URL
	 * @throws IOException Per problemi di I/O
	 */
	public static String readurl(String link) throws MalformedURLException,IOException {
		URL url = new URL(link);
		BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
		String line = new String();
		String result = new String();
		while ((line = in.readLine()) != null) {
			result += line;
		}
		in.close();
		return result;

	}
}
