package com.FM.OOPProject.model;

import java.lang.Math;

import com.FM.OOPProject.model.City;

import java.util.ArrayList;

/**
 * Classe che racchiude tutte le statistiche del singolo anno e i metodi per calcolarle
 */
public class Statistics {
	private int year;
	private float max;
	private float min;
	private float sum;
	private int count;
	private float avg;
	private float dev_std;
	/**
	 * Metodo costruttore che crea un oggetto Statistics contenente tutte le statistiche
	 * di un' ArrayList di oggetti City per un determinato anno
	 * @param records Array di Oggetti City su cui calcolare le statistiche
	 * @param year anno per il quale voglio calcolare le statistiche
	 */
	public Statistics(ArrayList<City> records, int year) {
		this.year = year;
		ArrayList<Float> column = new ArrayList<Float>();
		for (City item : records) {
			if (item.getYearData(year) > 0) {
				column.add(item.getYearData(year));
			}

		}
		float[] data = new float[column.size()];
		for (int i = 0; i < data.length; i++) {
			data[i] = column.get(i);
		}
		this.max = max(data);
		this.min = min(data);
		this.sum = sum(data);
		this.count = data.length;
		this.avg = avg(data);
		this.dev_std = dev_std(data);
	}
	/**
	 * Metodo che calcola il valore massimo contenuto in un array
	 * @param data vettore di cui calcolare il massimo
	 * @return valore massimo nel vettore
	 */
	private float max(float[] data) {
		float max = 0;
		for (int i = 0; i < data.length; i++) {
			if (data[i] > max) {
				max = data[i];
			}

		}
		return max;
	}

	private float min(float[] data) {
		if (data.length > 0) {
			float min = data[0];
			for (int i = 0; i < data.length; i++) {
				if (data[i] < min) {
					min = data[i];
				}

			}
			return min;
		} else
			return 0; // se il vettore data non ha valori ritorno 0
	}

	private float sum(float[] data) {
		float tot = 0;
		for (int i = 0; i < data.length; i++)
			tot += data[i];

		return tot;
	}

	private float avg(float[] data) {
		return sum(data) / data.length;
	}

	private float dev_std(float[] data) {
		return (float) Math.sqrt((double) avg(data));
	}

	public int getYear() {
		return year;
	}

	public float getMax() {
		return max;
	}
	
	public float getMin() {
		return min;
	}

	public float getSum() {
		return sum;
	}

	public int getCount() {
		return count;
	}

	public float getAvg() {
		return avg;
	}

	public float getDev_std() {
		return dev_std;
	}

}
