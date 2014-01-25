package fr.seb.predicates.status;

import java.util.Date;

import org.apache.commons.collections.Predicate;

import fr.seb.entities.ThermostatStatus;

public class AfterDate implements Predicate {

	private int priority;

	private Date after;

	public AfterDate(int priority, Date after) {
		this.priority = priority;
		this.after = after;
	}

	public boolean evaluate(Object object) {
		ThermostatStatus thermostatStatus = (ThermostatStatus) object;
		return thermostatStatus.getDate().after(after) && thermostatStatus.getPriority() < priority;
	}

}
