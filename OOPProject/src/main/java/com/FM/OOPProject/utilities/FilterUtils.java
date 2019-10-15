package com.FM.OOPProject.utilities;

import static com.FM.OOPProject.OopProjectApplication.Cities;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.regex.Pattern;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.FM.OOPProject.model.City;
import com.FM.OOPProject.model.Statistics;

public class FilterUtils {
	public static boolean check(Object value, String operator, Object... par) throws Exception {
		if (par.length==1) {
			if (value instanceof Number && par[0] instanceof Number) {
				Double valueD = ((Number)value).doubleValue();
				Double parD = ((Number)par[0]).doubleValue();
				switch (operator) {
				case "$eq":
				case "$in":
					return valueD == parD;
				case "$nin":
				case "$not":
					return !(valueD == parD);
				case "$gt":
					return valueD > parD;
				case "$gte":
					return valueD >= parD;
				case "$lt":
					return valueD < parD;
				case "$lte":
					return valueD <= parD;
				case "$bt":
					throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"between operator \"$bt\" requires two parameters");
				default:
					throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid operator");
				}
			} else if (value instanceof String && par[0] instanceof String) {
				switch (operator) {
				case "$eq":
				case "$in":
					return (value.equals(par[0]));
				case "$not":
				case "$nin":
					return !(value.equals(par[0]));
				default:
					throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Operator "+ operator+" is not compatible with string type");
				} 
			} else throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid parameter format");
		} else { // par is made of more than one element
			Double valueD = ((Number)value).doubleValue();
			Double parD = ((Number)par[0]).doubleValue();
			switch (operator) {
			case "$in":
				return Arrays.asList(par).contains(value);
			case "$nin":
				return !(Arrays.asList(par).contains(value));
			case "$bt":
				if(par.length==2) {
					Double ub = ((Number)par[1]).doubleValue(); //Upper Bound
					return (valueD <= ub && valueD >=parD);
				}
			default: 
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Operator "+ operator+" not compatible with parameters");


			}
		}
	}

	public Collection<City> select(Collection<City> src, String fieldName, String operator, ArrayList<City> in, Object... value ) throws Exception {
		Collection<City> out = in;
		selectAct(src,out,fieldName,operator,value);
		return out;
	}


	private void selectAct(Collection<City> src, Collection<City> out, String fieldName, String operator, Object... value) throws Exception {
		for(City item:src) {
			try {
				Object tmp = new Object();
				if (Pattern.matches("^\\d+$",fieldName)){
					int year = Integer.parseInt((String) fieldName);
					tmp = item.getYearData(year);
				} 
				else {
					Method m = item.getClass().getMethod("get"+fieldName.substring(0,1).toUpperCase()+fieldName.substring(1), null);
					tmp = m.invoke(item);
				}
				if(FilterUtils.check(tmp, operator, value) && !(out.contains(item))) {
					out.add(item);
				}

			}catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			}					
		}
	} 
}
