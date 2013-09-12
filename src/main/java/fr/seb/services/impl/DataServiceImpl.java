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

import fr.seb.data.beans.HeatingSetPoint;
import fr.seb.entities.Constant;
import fr.seb.entities.TemperatureRecord;
import fr.seb.entities.ThermostatState;
import fr.seb.services.DataService;

public class DataServiceImpl implements DataService {

	@Inject
	private Session session;

	public Collection<ThermostatState> getLastThermostatState(int lastUnit, int lastAmount) {
		Date now = new Date();

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(now);
		calendar.add(lastUnit, lastAmount * -1);
		Criteria createCriteria = session.createCriteria(ThermostatState.class).add(Restrictions.gt("date", calendar.getTime())).addOrder(Order.asc("date"));
		@SuppressWarnings("unchecked")
		List<ThermostatState> thermostatStates = createCriteria.list();

		if (thermostatStates.isEmpty()) {
			// On récupère le dernier état du thermostat
			ThermostatState lastKnownThermostatStates = (ThermostatState) session.createCriteria(ThermostatState.class).addOrder(Order.desc("date"))
					.setMaxResults(1).list().get(0);

			// On l'ajoute au début de la période demandée
			ThermostatState start = new ThermostatState();
			start.setDate(calendar.getTime());
			start.setState(lastKnownThermostatStates.isState());
			thermostatStates.add(0, start);
		} else {
			// Ajout d'un début à l'état inverse du plus ancien
			ThermostatState start = new ThermostatState();
			start.setDate(calendar.getTime());
			start.setState(!thermostatStates.get(0).isState());
			thermostatStates.add(0, start);
		}

		// Ajout d'une fin au même état que le plus récent
		ThermostatState end = new ThermostatState();
		end.setDate(now);
		end.setState(thermostatStates.get(thermostatStates.size() - 1).isState());
		thermostatStates.add(end);

		return thermostatStates;
	}

	public Collection<ThermostatState> getLastHourThermostatState() {
		return getLastThermostatState(Calendar.HOUR_OF_DAY, 1);
	}

	public Collection<ThermostatState> getLast24hThermostatState() {
		return getLastThermostatState(Calendar.DAY_OF_YEAR, 1);
	}

	public Collection<ThermostatState> getLastMonthThermostatState() {
		return getLastThermostatState(Calendar.MONTH, 1);
	}

	public Collection<ThermostatState> getLastYearThermostatState() {
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

	public HeatingSetPoint getHeatingSetPoint() {
		float consigne = Float.parseFloat(getConstant("consigne").getValue());
		float hysteresis = Float.parseFloat(getConstant("hysteresis").getValue());

		return new HeatingSetPoint(consigne, hysteresis);
	}

	private Constant getConstant(String name) {
		return (Constant) session.createCriteria(Constant.class).add(Restrictions.eq("name", name)).list().get(0);
	}
}
