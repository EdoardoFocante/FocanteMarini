package com.FM.OOPProject.controller;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;

import com.FM.OOPProject.model.City;
import com.FM.OOPProject.utilities.FilterUtils;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import com.fasterxml.jackson.module.jsonSchema.JsonSchemaGenerator;

import static com.FM.OOPProject.OopProjectApplication.Cities;



@RestController
public class Controller {
	
	public FilterUtils filter;

	@RequestMapping(value="/data", method = { RequestMethod.POST, RequestMethod.GET })
	public ArrayList<City> GetData() {
		return Cities;
				
	}
	@RequestMapping(value="/metadata", method = { RequestMethod.POST, RequestMethod.GET }) 
	public JsonSchema GetMeta() throws JsonMappingException {
		ObjectMapper mapper = new ObjectMapper();
		JsonSchemaGenerator schemaGen = new JsonSchemaGenerator(mapper);
		JsonSchema schema = schemaGen.generateSchema(City.class);
		return schema;
		
	}
	
	@RequestMapping(value="/stats", produces="application/json")
	public ArrayList<City> givestats(@RequestBody() String jsonfilter) throws Exception { //@RequestParam(name="field",defaultvalue="");
		return filteract(Cities,jsonfilter); //Parsing del request body
}
	
	private ArrayList<City> filteract(ArrayList<City> data, String jsonfilter) throws Exception {
		ArrayList<City> filtered= new ArrayList<City>(); // inizializzato perchè funzioni nel caso in cui non fi fosse una or
		JSONObject f  = new JSONObject(jsonfilter);
		JSONArray arr;//Parsing del request body
		if(f.has("$or")) {  
		arr = f.getJSONArray("$or");
		for (int i=0; i<arr.length();i ++) {
			filtered=filteract1(arr.getJSONObject(i),data,filtered); // per l'or filtro partendo da una stringa già filtrata e aggiungo tutto gli elementi non già contenuti che soddisfano il filtro
		}
	} else filtered = data;
		if (f.has("$and")) {  
			arr=f.getJSONArray("$and");
			for (int i=0; i<arr.length(); i++) {
				ArrayList<City> emptystart = new ArrayList<City>();
				filtered=filteract1(arr.getJSONObject(i),filtered, emptystart); // per l'and filtro la stessa stringa più volte perchè rispetti tutte le condizioni
			}
		}
		if(f.has("$or") || f.has("$and")) return filtered;
		else throw new Exception("You need to add an and or an or section to this JSON");
	
	}
	private ArrayList<City> filteract1(JSONObject Condition, ArrayList<City> src, ArrayList<City> in ) throws Exception {
		FilterUtils filter = new FilterUtils();
		String fieldname = Condition.names().get(0).toString();	
		String op= Condition.getJSONObject(fieldname).names().get(0).toString();
		Object value= Condition.getJSONObject(fieldname).getString(op);
		System.out.println(fieldname);
		System.out.println(op);
		System.out.println(value);
		return (ArrayList<City>) filter.select(src, fieldname, op, in , value);
	}
}
