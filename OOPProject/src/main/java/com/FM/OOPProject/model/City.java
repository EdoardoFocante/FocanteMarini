package com.FM.OOPProject.model;

import java.util.Arrays;

public class City {
	private String indic_ur;
	private String citycode;
	private float[] data;
	
	public City(String indic_ur, String citycode, float[] data) {
		super();
		this.indic_ur = indic_ur;
		this.citycode = citycode;
		this.data = data;
	}

	@Override
	public String toString() {
		return "City [indic_ur=" + indic_ur + ", citycode=" + citycode + ", data=" + Arrays.toString(data)
				+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((citycode == null) ? 0 : citycode.hashCode());
		result = prime * result + Arrays.hashCode(data);
		result = prime * result + ((indic_ur == null) ? 0 : indic_ur.hashCode());
		return result;
	}

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

	public String getIndic_ur() {
		return indic_ur;
	}

	public void setIndic_ur(String indic_ur) {
		this.indic_ur = indic_ur;
	}

	public String getcitycode() {
		return citycode;
	}

	public void setcitycode(String citycode) {
		this.citycode = citycode;
	}

	public float[] getdata() {
		return data;
	}

	public void setdata(float[] data) {
		this.data = data;
	}
}
