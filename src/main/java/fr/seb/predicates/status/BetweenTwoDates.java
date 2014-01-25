package fr.seb.predicates.status;

import java.util.Date;

import org.apache.commons.collections.Predicate;

import fr.seb.entities.ThermostatStatus;

public class BetweenTwoDates implements Predicate {

	private int priority;
	private Date after;
	private Date before;

	public BetweenTwoDates(int priority, Date after, Date before) {
		this.priority = priority;
		this.after = after;
		this.before = before;
	}

	public boolean evaluate(Object object) {
		ThermostatStatus thermostatStatus = (ThermostatStatus) object;
		return thermostatStatus.getDate().before(before) && thermostatStatus.getDate().after(after) && thermostatStatus.getPriority() < priority;
	}
}
