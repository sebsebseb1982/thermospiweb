package fr.seb.predicates.setpoints;

import java.util.Date;

import org.apache.commons.collections.Predicate;

import fr.seb.entities.ThermostatSetPoint;

public class BetweenTwoDates implements Predicate {

	private Date after;
	private Date before;

	public BetweenTwoDates(Date after, Date before) {
		this.after = after;
		this.before = before;
	}

	public boolean evaluate(Object thermostatSetPoint) {
		return ((ThermostatSetPoint) thermostatSetPoint).getDate().after(after) && ((ThermostatSetPoint) thermostatSetPoint).getDate().before(before);
	}

}
