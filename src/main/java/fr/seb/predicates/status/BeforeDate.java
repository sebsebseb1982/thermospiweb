package fr.seb.predicates.status;

import java.util.Date;

import org.apache.commons.collections.Predicate;

import fr.seb.entities.ThermostatStatus;

public class BeforeDate implements Predicate {

	private int priority;

	private Date before;

	public BeforeDate(int priority, Date before) {
		this.priority = priority;
		this.before = before;
	}

	public boolean evaluate(Object object) {
		ThermostatStatus thermostatStatus = (ThermostatStatus) object;
		return thermostatStatus.getDate().before(before) && thermostatStatus.getPriority() < priority;
	}
}
