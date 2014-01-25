package fr.seb.predicates.setpoints;

import java.util.Date;

import org.apache.commons.collections.Predicate;

import fr.seb.entities.ThermostatSetPoint;

public class AfterDate implements Predicate {

	private Date after;

	public AfterDate(Date after) {
		this.after = after;
	}

	public boolean evaluate(Object thermostatSetPoint) {
		return ((ThermostatSetPoint) thermostatSetPoint).getDate().after(after);
	}

}
