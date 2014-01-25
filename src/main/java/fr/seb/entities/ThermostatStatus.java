package fr.seb.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import fr.seb.interfaces.Printable;

@Entity(name = "status")
public class ThermostatStatus implements Comparable<ThermostatStatus>, Printable {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@Column(name = "date")
	private Date date;

	@Column(name = "status")
	private boolean status;

	@Column(name = "priority")
	private int priority;

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

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public int compareTo(ThermostatStatus o) {
		return o.getDate().compareTo(getDate());
	}

	public String print() {
		StringBuilder result = new StringBuilder();

		result.append(date).append('|').append(getPriority()).append('|').append(isStatus());

		return result.toString();
	}
}
