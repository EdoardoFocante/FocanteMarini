package com.FM.OOPProject;

import java.util.ArrayList;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.FM.OOPProject.model.City;
import com.FM.OOPProject.utilities.TSVDownload;
import com.FM.OOPProject.utilities.TSVParse;


@SpringBootApplication
public class OopProjectApplication {
	public static ArrayList<City> Cities;
	public static void main(String[] args) throws Exception {
		//ArrayList<City> Cities = new ArrayList<City>();
		Cities= new ArrayList<City>();
		TSVDownload.GetTSV();
		TSVParse.ParseTSV(Cities);
		SpringApplication.run(OopProjectApplication.class, args);
	}

}
