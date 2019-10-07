package com.FM.OOPProject.utilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import com.FM.OOPProject.model.City;

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
					throw new Exception("$bt operates with two values");
				default:
					throw new Exception("Invalid operator");
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
					throw new Exception("Operator in not compatible with string type");
				} 
			} else throw new Exception("Invalid parameter format");
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
				throw new Exception("Operator not compatible with parameters");


			}
		}
	}
	/*public Collection<T> select(Collection<T> src, String fieldName, String operator, Object... value) throws Exception {
		Collection<T> out = new ArrayList<T>();
		selectAct(src,out,fieldName, operator,value);
		return out;

	}*/
	public Collection<City> select(Collection<City> src, String fieldName, String operator, ArrayList<City> in, Object... value ) throws Exception {
		Collection<City> out = in;
		selectAct(src,out,fieldName,operator,value);
		return out;
	}


	private void selectAct(Collection<City> src, Collection<City> out, Object fieldName, String operator, Object... value) throws Exception {
		for(City item:src) {
			try {
				if (fieldName instanceof Number) {
					int year=((Number) fieldName).intValue();
					Object tmp = item.getYearData(year);
					if(FilterUtils.check(tmp, operator, value) && !(out.contains(item))) {
						out.add(item);
					}
				}else if (fieldName instanceof String) {
					Object tmp = item.getCitycode();
					if(FilterUtils.check(tmp, operator, value) && !(out.contains(item))) {
						out.add(item);
					}
				} 
			}catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}					
		}
	}
}
