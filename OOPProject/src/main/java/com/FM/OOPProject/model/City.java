package com.FM.OOPProject.model;

import java.util.Arrays;

public class City {
	private String indic_ur;
	private String campo2;
	private int[] citiesperyear;
	
	public City(String indic_ur, String campo2, int[] citiesperyear) {
		super();
		this.indic_ur = indic_ur;
		this.campo2 = campo2;
		this.citiesperyear = citiesperyear;
	}

	@Override
	public String toString() {
		return "City [indic_ur=" + indic_ur + ", campo2=" + campo2 + ", citiesperyear=" + Arrays.toString(citiesperyear)
				+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((campo2 == null) ? 0 : campo2.hashCode());
		result = prime * result + Arrays.hashCode(citiesperyear);
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
		if (campo2 == null) {
			if (other.campo2 != null)
				return false;
		} else if (!campo2.equals(other.campo2))
			return false;
		if (!Arrays.equals(citiesperyear, other.citiesperyear))
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

	public String getCampo2() {
		return campo2;
	}

	public void setCampo2(String campo2) {
		this.campo2 = campo2;
	}

	public int[] getCitiesperyear() {
		return citiesperyear;
	}

	public void setCitiesperyear(int[] citiesperyear) {
		this.citiesperyear = citiesperyear;
	}
}
