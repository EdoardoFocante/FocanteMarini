package com.FM.OOPProject.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import com.FM.OOPProject.model.City;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import com.fasterxml.jackson.module.jsonSchema.JsonSchemaGenerator;

import static com.FM.OOPProject.OopProjectApplication.Cities;

@RestController
public class Controller {

	@GetMapping("/data")
	public ArrayList<City> GetData() {
		return Cities;
				
	}
	@GetMapping("/metadata") 
	public JsonSchema GetMeta() throws JsonMappingException {
		ObjectMapper mapper = new ObjectMapper();
		JsonSchemaGenerator schemaGen = new JsonSchemaGenerator(mapper);
		JsonSchema schema = schemaGen.generateSchema(City.class);
		return schema;
		
	}
	
}
