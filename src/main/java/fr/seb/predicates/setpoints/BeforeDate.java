package fr.seb.predicates.setpoints;

import java.util.Date;

import org.apache.commons.collections.Predicate;

import fr.seb.entities.ThermostatSetPoint;

public class BeforeDate implements Predicate {

	private Date before;

	public BeforeDate(Date before) {
		this.before = before;
	}

	public boolean evaluate(Object object) {
		return ((ThermostatSetPoint) object).getDate().before(before);
	}

}
