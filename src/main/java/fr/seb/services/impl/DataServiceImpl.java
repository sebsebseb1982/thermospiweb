package fr.seb.services.impl;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import fr.seb.entities.TemperatureRecord;
import fr.seb.entities.ThermostatSetPoint;
import fr.seb.enumerations.SensorType;
import fr.seb.services.DataService;

public class DataServiceImpl implements DataService {

	@Inject
	private Session session;

	public Collection<ThermostatSetPoint> getLastThermostatState(int lastUnit, int lastAmount) {
		Date now = new Date();

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(now);
		calendar.add(lastUnit, lastAmount * -1);
		Criteria createCriteria =

		session.createCriteria(ThermostatSetPoint.class).add(Restrictions.gt("date", calendar.getTime())).addOrder(Order.asc("date"));
		@SuppressWarnings("unchecked")
		List<ThermostatSetPoint> thermostatStates = createCriteria.list();

		// if (thermostatStates.isEmpty()) {
		// // On récupère le dernier état du thermostat
		// ThermostatSetPoint lastKnownThermostatStates = (ThermostatSetPoint)
		//
		// session.createCriteria(ThermostatSetPoint.class).addOrder(Order.desc("date")).setMaxResults(1).list().get(0);
		//
		// // On l'ajoute au début de la période demandée
		// ThermostatSetPoint start = new ThermostatSetPoint();
		// start.setDate(calendar.getTime());
		// start.setState(lastKnownThermostatStates.isState());
		// thermostatStates.add(0, start);
		// } else {
		// // Ajout d'un début à l'état inverse du plus ancien
		// ThermostatSetPoint start = new ThermostatSetPoint();
		// start.setDate(calendar.getTime());
		// start.setValue(thermostatStates.get(0).getValue());
		// thermostatStates.add(0, start);
		// }

		// Ajout d'une fin au même état que le plus récent
		ThermostatSetPoint end = new ThermostatSetPoint();
		end.setDate(now);
		end.setValue(thermostatStates.get(thermostatStates.size() - 1).getValue());
		thermostatStates.add(end);

		return thermostatStates;
	}

	public Collection<ThermostatSetPoint> getLastHourThermostatState() {
		return getLastThermostatState(Calendar.HOUR_OF_DAY, 1);
	}

	public Collection<ThermostatSetPoint> getLast24hThermostatState() {
		return getLastThermostatState(Calendar.DAY_OF_YEAR, 1);
	}

	public Collection<ThermostatSetPoint> getLastMonthThermostatState() {
		return getLastThermostatState(Calendar.MONTH, 1);
	}

	public Collection<ThermostatSetPoint> getLastYearThermostatState() {
		return getLastThermostatState(Calendar.YEAR, 1);
	}

	public Collection<TemperatureRecord> getLastTemperatures(int lastUnit, int lastAmount) {
		Date now = new Date();

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(now);
		calendar.add(lastUnit, lastAmount * -1);
		Criteria createCriteria = session.createCriteria(TemperatureRecord.class).add(Restrictions.gt("date", calendar.getTime())).addOrder(Order.asc("date"));
		@SuppressWarnings("unchecked")
		List<TemperatureRecord> temperatureRecord = createCriteria.list();
		return temperatureRecord;
	}

	public TemperatureRecord getTemperatureActuelle(SensorType capteur) {
		Criteria createCriteria = session.createCriteria(TemperatureRecord.class).add(Restrictions.eq("sensor.id", capteur.getId()))
				.addOrder(Order.desc("date")).setMaxResults(1);
		return (TemperatureRecord) createCriteria.list().get(0);
	}

}
