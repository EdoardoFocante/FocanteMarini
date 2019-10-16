package com.FM.OOPProject.model;

import java.util.Arrays;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;
/**
 * Classe che modella il singolo record del tsv importato
 */
public class City {
	/**
	 * Codice associato ad un indicatore della qualità della vita
	 */
	@JsonPropertyDescription("Codice associato ad un indicatore della qualità della vita")
	private String indic_ur;
	/**
	 * Codice della città europea a cui sono riferiti i dati del record
	 */
	@JsonPropertyDescription("Codice della città europea a cui sono riferiti i dati del record")
	private String citycode;
	/**
	 * Array contenente i valori dell'indice per la rispettiva città negli anni dal 1990 al 2018
	 */
	@JsonPropertyDescription("Array contenente i valori dell'indice per la rispettiva città negli anni dal 1990 al 2018")
	private float[] data;

	/**
	 * Costruttore della classe city
	 * @param indic_ur Codice associato ad un indicatore della qualità della vita
	 * @param citycode Codice della città europea a cui sono riferiti i dati del record
	 * @param data Array contenente i valori dell'indice per la rispettiva città negli anni dal 1990 al 2018
	 */
	public City(String indic_ur, String citycode, float[] data) {
		super();
		this.indic_ur = indic_ur;
		this.citycode = citycode;
		this.data = data;
	}
	/**
	 * Converte l'oggetto City in una stringa
	 * @return String Oggetto City sottoforma di stringa
	 */
	@Override
	public String toString() {
		return "City [indic_ur=" + indic_ur + ", citycode=" + citycode + ", data=" + Arrays.toString(data) + "]";
	}
	/**
	 * Metodo equals
	 * @param obj Oggetto con cui effettuo il confronto
	 * @return boolean true o false a seconda del risultato dell'uguaglianza
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		City other = (City) obj;
		if (citycode == null) {
			if (other.citycode != null)
				return false;
		} else if (!citycode.equals(other.citycode))
			return false;
		if (!Arrays.equals(data, other.data))
			return false;
		if (indic_ur == null) {
			if (other.indic_ur != null)
				return false;
		} else if (!indic_ur.equals(other.indic_ur))
			return false;
		return true;
	}
	/**
	 * Metodo get per l'attributo indic_ur
	 * @return attributo indic_ur dell'oggetto
	 */
	public String getIndic_ur() {
		return indic_ur;
	}
	/**
	 * Metodo set per l'attributo indic_ur
	 * @param indic_ur valore di indic_ur da inserire nell'oggetto
	 */
	public void setIndic_ur(String indic_ur) {
		this.indic_ur = indic_ur;
	}
	/**
	 * Metodo get per l'attributo citycode
	 * @return attributo citycode dell'oggetto
	 */
	public String getCitycode() {
		return citycode;
	}
	/**
	 * Metodo set per l'attributo citycode
	 * @param citycode valore di citycode da inserire nell'oggetto
	 */
	public void setCitycode(String citycode) {
		this.citycode = citycode;
	}
	/**
	 * Metodo get per l'attributo data
	 * @return 
	 */
	public float[] getData() {
		return data;
	}
	/**
	 * Metodo che ritorna il valore del vettore data per un certo anno
	 * @param year anno per il quale voglio sapere il valore di data
	 * @return valore di data per un determinato anno
	 */
	public float getYearData(int year) {
		return data[yeartoIndex(year)];
	}
	/**
	 * Metodo che converte il numero dell'anno nel rispettivo indice del vettore data
	 * @param year anno da convertire
	 * @return corrispettivo indice del vettore data 
	 * @throws ResponseStatusException Lanciata se l'anno non è nell'intervallo considerato dai dati
	 */
	private int yeartoIndex(int year) {
		if (year < 2019 && year >= 1990)
			return 2018 - year;
		else
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Year must be between 2018 and 1990");
	}
	/**
	 * Metodo set per il vettore data
	 * @param data valore di data da inserire nell'oggetto
	 */
	public void setData(float[] data) {
		this.data = data;
	}
}
