package com.FM.OOPProject.utilities;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import com.FM.OOPProject.model.City;

/**
 * Classe che implementa tutte le funzioni di filtraggio
 */
public class FilterUtils {
	/**
	 * Funzione che verifica una determinata condizione del filtro
	 * 
	 * @param value    valore del record da confrontare con il parametro del filtro
	 * @param operator operatore da utilizzare per il confronto
	 * @param par      parametro o array di parametri del filtro
	 * @return true se la condizione è verificata, false se non lo è
	 * @throws ResponseStatusException se i parametri della richiesta non sono
	 *                                 adatti al rispettivo operatore
	 */
	private static boolean check(Object value, String operator, Object... par) {
		if (par.length == 1) { // se il parametro della condizione è costituito da un solo elemento
			if (value instanceof Number && par[0] instanceof Number) {
				Float valueF = ((Number) value).floatValue();
				Float parF = ((Number) par[0]).floatValue();
				switch (operator) {
				case "$eq":
				case "$in":
					return valueF == parF;
				case "$nin":
				case "$not":
					return !(valueF == parF);
				case "$gt":
					return valueF > parF;
				case "$gte":
					return valueF >= parF;
				case "$lt":
					return (valueF < parF && valueF > 0);
				case "$lte":
					return (valueF <= parF && valueF > 0);
				case "$bt":
					throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
							"between operator $bt requires two parameters");
				default:
					throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid operator");
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
					throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
							"Operator " + operator + " is not valid or is not compatible with string type");
				}
			} else
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid parameter format");
		} else { // se la condizione ha un array di parametri
			switch (operator) {
			case "$in":
				return Arrays.asList(par).contains(value);
			case "$nin":
				return !(Arrays.asList(par).contains(value));
			case "$bt":
				if (par.length == 2 && par[0] instanceof Number && par[1] instanceof Number) {
					Float valueF = ((Number) value).floatValue();
					Float parF = ((Number) par[0]).floatValue();
					Float par2F = ((Number) par[1]).floatValue(); // Secondo Parametro
					return (valueF <= Math.max(parF, par2F) && valueF >= Math.min(parF, par2F));
				} else
					throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
							"$bt operator requires 2 numeric parameters");
			default:
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
						"Operator " + operator + " not valid or incompatible with parameters");

			}
		}
	}

	/**
	 * Metodo che aggiunge alla lista in tutti i record della sorcente src che
	 * soddisfano i parametri in ingresso
	 * 
	 * @param src       Collection di oggetti di tipo City su cui operare il
	 *                  filtraggio
	 * @param in        ArrayList di partenza a cui aggiungere i nuovi elementi che
	 *                  soddisfano i parametri
	 * @param fieldName Nome dell'attributo su cui effettuare il confronto
	 * @param operator  Operatore con cui effetuare il confronto
	 * @param value     Parametro o array di parametri del fitro
	 * @throws IllegalAccessException    Lanciata la funzione invoke non ha accesso
	 *                                   alla definizione del metodo m
	 * @throws IllegalArgumentException  Lanciata se una funzione riceve un
	 *                                   parametro non appropriato
	 * @throws InvocationTargetException Lanciata se il metodo che invoco tramite
	 *                                   invoke lancia a sua volta un Exception
	 * @throws SecurityException         Lanciata se getMethod prova ad accedere ad
	 *                                   una risorsa per cui non ha sufficienti
	 *                                   autorizzazioni
	 * @throws NoSuchMethodException     Lanciata se viene fatto un riferimento ad
	 *                                   un metodo non esistente
	 */
	public static ArrayList<City> select(ArrayList<City> src, ArrayList<City> in, String fieldName, String operator,
			Object... value) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException {
		ArrayList<City> out = in;
		for (City item : src) {
			Object tmp = new Object();
			if (Pattern.matches("^\\d+$", fieldName)) { //se field è un anno uso il metodo getYearData
				int year = Integer.parseInt((String) fieldName);
				tmp = item.getYearData(year);
			} else { // se field è "citycode" o "indic_ur" uso il metodo get apposito
				Method m = item.getClass()
						.getMethod("get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1), null);
				tmp = m.invoke(item);
			}
			if (FilterUtils.check(tmp, operator, value) && !(out.contains(item))) {
				out.add(item);
			}

		}
		return out;
	}
}
