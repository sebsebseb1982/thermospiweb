package fr.seb.services;

import java.util.List;

import fr.seb.entities.ThermostatState;

public interface DataService {
	public List<ThermostatState> getLastThermostatState(int lastUnit, int lastAmount);

	public List<ThermostatState> getLastHourThermostatState();

	public List<ThermostatState> getLast24hThermostatState();

	public List<ThermostatState> getLastMonthThermostatState();

	public List<ThermostatState> getLastYearThermostatState();
}
