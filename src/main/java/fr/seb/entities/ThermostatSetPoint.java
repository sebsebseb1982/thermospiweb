package fr.seb.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import fr.seb.interfaces.Printable;

@Entity(name = "setpoints")
public class ThermostatSetPoint implements Printable, Comparable<ThermostatSetPoint> {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@Column(name = "date")
	private Date date;

	@Column(name = "value")
	private float value;

	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		this.value = value;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String print() {
		StringBuilder result = new StringBuilder();

		result.append(date).append('|').append(getValue());

		return result.toString();
	}

	public int compareTo(ThermostatSetPoint thermostatSetPoint) {
		return getDate().compareTo(thermostatSetPoint.getDate());
	}

}
