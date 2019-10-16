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

@RestController
public class Controller {

	@RequestMapping(value = "/data", method = { RequestMethod.POST, RequestMethod.GET })
	public ArrayList<City> getData() {
		return Cities;

	}

	@RequestMapping(value = "/metadata", method = { RequestMethod.POST,
			RequestMethod.GET }, produces = "application/json")
	public JsonSchema GetMeta() throws JsonMappingException {
		ObjectMapper mapper = new ObjectMapper();
		JsonSchemaGenerator schemaGen = new JsonSchemaGenerator(mapper);
		JsonSchema schema = schemaGen.generateSchema(City.class);
		return schema;
	}

	@RequestMapping(value = "/filter", produces = "application/json")
	public ArrayList<City> getFiltered(@RequestBody() String jsonfilter) throws Exception {
		return filteract(Cities, jsonfilter); // Parsing del request body
	}

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

	private ArrayList<City> filteract1(JSONObject Condition, ArrayList<City> src, ArrayList<City> in) throws Exception {
		if (Condition.names().length() < 2) { // controllo se ho un array di condizioni non racchiuso in un and o un or
			FilterUtils util = new FilterUtils();
			String fieldname = Condition.names().get(0).toString();
			System.out.println(fieldname);
			String op = Condition.getJSONObject(fieldname).names().get(0).toString();
			if (Condition.getJSONObject(fieldname).get(op) instanceof JSONArray) {
				JSONArray value = Condition.getJSONObject(fieldname).getJSONArray(op);
				Object[] objvalue = new Object[value.length()];
				for (int i = 0; i < value.length(); i++) { // converto il valore il jsonarray in un array di oggetti
					objvalue[i] = value.get(i);
				}
				return (ArrayList<City>) util.select(src, fieldname, op, in, objvalue);

			} else {
				Object objvalue = Condition.getJSONObject(fieldname).get(op);

				return (ArrayList<City>) util.select(src, fieldname, op, in, objvalue);
			}
		} else
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"To apply a filter with more than one condition add an \"and\" or an \"or\"section to your filter");

	}
}
