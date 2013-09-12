package fr.seb.services;

import java.util.Collection;

import fr.seb.data.beans.HeatingSetPoint;
import fr.seb.entities.TemperatureRecord;
import fr.seb.entities.ThermostatState;

public interface DataService {
	public Collection<ThermostatState> getLastThermostatState(int lastUnit, int lastAmount);

	public Collection<ThermostatState> getLastHourThermostatState();

	public Collection<ThermostatState> getLast24hThermostatState();

	public Collection<ThermostatState> getLastMonthThermostatState();

	public Collection<ThermostatState> getLastYearThermostatState();

	public Collection<TemperatureRecord> getLastTemperatures(int lastUnit, int lastAmount);

	public HeatingSetPoint getHeatingSetPoint();
}
