package com.FM.OOPProject.controller;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import java.util.ArrayList;
import com.FM.OOPProject.model.City;
import com.FM.OOPProject.model.Statistics;
import com.FM.OOPProject.utilities.FilterUtils;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import com.fasterxml.jackson.module.jsonSchema.JsonSchemaGenerator;

import static com.FM.OOPProject.OopProjectApplication.Cities;

/**
 * Controller che gestisce le richieste REST
 */
@RestController
public class Controller {
	/**
	 * Metodo che restituisce l'intero dataset in formato JSON
	 * @return Dataset non filtrato in formato JSON
	 */
	@RequestMapping(value = "/data", method = { RequestMethod.POST, RequestMethod.GET })
	public ArrayList<City> getData() {
		return Cities;

	}
	/**
	 * Metodo che restituisce i metadati in formato JSON
	 * @return metadati in formato JSON
	 * @throws JsonMappingException Lanciata se si verificano errori nel processing dei file JSON
	 */
	@RequestMapping(value = "/metadata", method = { RequestMethod.POST,
			RequestMethod.GET }, produces = "application/json")
	public JsonSchema GetMeta() throws JsonMappingException {
		ObjectMapper mapper = new ObjectMapper();
		JsonSchemaGenerator schemaGen = new JsonSchemaGenerator(mapper);
		JsonSchema schema = schemaGen.generateSchema(City.class);
		return schema;
	}
	/**
	 * Metodo che restituisce un'ArrayList filtrata in formato JSON 
	 * @param jsonfilter Filtro in formato JSON ottenuto attraverso il body della richiesta POST
	 * @return ArrayList contenente i record conformi al filtro inserito
	 * @throws Exception Eccezioni lanciate dal metodo filteract
	 */
	@RequestMapping(value = "/filter", produces = "application/json")
	public ArrayList<City> getFiltered(@RequestBody() String jsonfilter) throws Exception {
		return filteract(Cities, jsonfilter); // Parsing del request body
	}
	/**
	 * Metodo che restituisce le statistiche di una 
	 * @param year Anno per la quale si vogliono ottenere le statistiche. Se omesso viene restituito il calcolo anno per anno
	 * @param jsonfilter Filtro in formato JSON ottenuto attraverso il body della richiesta POST
	 * @return Statistiche in formato JSON ottenute da una lista filtrata
	 * @throws Exception Eccezioni lanciate dal metodo filteract
	 */
	@RequestMapping(value = "/stats", produces = "application/json")
	public ArrayList<Statistics> getStats(@RequestParam(required = false, defaultValue = "-1") int year,
			@RequestBody() String jsonfilter) throws Exception {
		ArrayList<Statistics> result = new ArrayList<Statistics>();
		ArrayList<City> filtered = filteract(Cities, jsonfilter);
		if (year == -1) { // anno non inserito, statistiche fatte per ogni anno
			for (int i = 1990; i < 2019; i++) {
				result.add(new Statistics(filtered, i));
			}

		} else { // ritorno la statistica sull'anno inserito
			result.add(new Statistics(filtered, year));
		}
		return result;
	}
	/**
	 * Metodo che effettua il parsing delle condizioni and e or del filtro e le attua.
	 * Se non sono presenti richiama solamente la funzione successiva
	 * @param data ArrayList dei dati su cui operare il filtro
	 * @param jsonfilter filtro in formato JSON ottenuto attraverso il body della richiesta POST
	 * @return Lista filtrata
	 * @throws Exception Eccezzioni lanciate dal metodo filteract1
	 */
	private ArrayList<City> filteract(ArrayList<City> data, String jsonfilter) throws Exception {
		ArrayList<City> filtered = new ArrayList<City>();
		JSONObject f = new JSONObject(jsonfilter);
		JSONArray arr; // Parsing del request body
		if (f.has("$or")) {
			arr = f.getJSONArray("$or");
			for (int i = 0; i < arr.length(); i++) {
				filtered = filteract1(arr.getJSONObject(i), data, filtered);
				// per l'or filtro parto dal risultato e aggiungo tutti gli
				// elementi non già contenuti che soddisfano la nuova condizione

			}
		} else
			filtered = data; // se non ho una parte or, il vettore di partenza per le and è l'intera
								// arraylist dei dati
		if (f.has("$and")) {
			arr = f.getJSONArray("$and");
			for (int i = 0; i < arr.length(); i++) {
				ArrayList<City> emptystart = new ArrayList<City>();
				filtered = filteract1(arr.getJSONObject(i), filtered, emptystart); 
				// per l'and filtro la stessa stringa più volte perchè rispetti tutte
				// le condizioni, aggiungo i nuovi record in una stringa vuota ad ogni iterazione
			}
		}
		if (f.has("$or") || f.has("$and")) {
			return filtered;
		} else
			filtered = filteract1(f, data, new ArrayList<City>());
		return filtered;
	}
	/**
	 * Metodo che effettua il parsing delle singole condizioni del filtro e le attua
	 * @param Condition JSONObject contenente la singola condizione
	 * @param src ArrayList dei dati da filtrare
	 * @param in ArrayList di partenza a cui aggiungere i nuovi record conformi al filtro se non già presenti
	 * @return ArrayList di partenza con aggiunti i nuovi record conformi al filtro
	 * @throws Exception Eccezioni lanciate dal metodo select, e ResponseStatusException se vengono inserite più istruzioni senza
	 *	racchiuderle in un and oppure un or
	 */
	private ArrayList<City> filteract1(JSONObject Condition, ArrayList<City> src, ArrayList<City> in) throws Exception {
		if (Condition.names().length() < 2) { // controllo se ho un array di condizioni non racchiuso in un and o un or
			String fieldname = Condition.names().get(0).toString();
			String op = Condition.getJSONObject(fieldname).names().get(0).toString();
			if (Condition.getJSONObject(fieldname).get(op) instanceof JSONArray) {
				JSONArray value = Condition.getJSONObject(fieldname).getJSONArray(op);
				Object[] objvalue = new Object[value.length()];
				for (int i = 0; i < value.length(); i++) { // converto il valore il jsonarray in un array di oggetti
					objvalue[i] = value.get(i);
				}
				return (ArrayList<City>) FilterUtils.select(src, in, op, fieldname, objvalue);

			} else {
				Object objvalue = Condition.getJSONObject(fieldname).get(op);

				return FilterUtils.select(src, in, op,fieldname , objvalue);
			}
		} else
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"To apply a filter with more than one condition add an \"and\" or an \"or\"section to your filter");

	}
}
