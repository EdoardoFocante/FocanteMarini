package com.FM.OOPProject.utilities;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;



public class TSVDownload {
	public TSVDownload() {

	}

	public static void GetTSV() throws Exception {
		//if (!Files.exists(Paths.get("t.tsv"))) {
			JSONObject obj = new JSONObject(readurl("http://data.europa.eu/euodp/data/api/3/action/package_show?id=ojAmzVahjBnws2njEN0qhQ"));
			JSONArray arr = obj.getJSONObject("result").getJSONArray("resources");
			String html = new String();
			for (int i = 0; i < arr.length(); i++) {
				// System.out.println(i + " ");
				if (arr.getJSONObject(i).getString("format").equals("http://publications.europa.eu/resource/authority/file-type/TSV")) {
					html = readurl(arr.getJSONObject(i).getString("url"));
					break;
					// }
				}
			}
			Document document = Jsoup.parse(html);
			download(document.select("a").first().attr("href"), "t1.tsv");
		//}

		//else {
			//System.out.println("File Already Exists");
		//}
	}

	public static void download(String url, String fileName) throws Exception {
		try (InputStream in = URI.create(url).toURL().openStream()) {
			Files.copy(in, Paths.get(fileName),StandardCopyOption.REPLACE_EXISTING); //StandardCopyOption.REPLACE_EXISTING
		}

	}
	
	public static String readurl(String link) throws Exception {
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