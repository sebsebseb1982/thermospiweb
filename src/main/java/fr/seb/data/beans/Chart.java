package fr.seb.data.beans;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import fr.seb.entities.Sensor;
import fr.seb.entities.TemperatureRecord;
import fr.seb.entities.ThermostatSetPoint;

public class Chart {
	private Map<Sensor, List<TemperatureRecord>> plots;

	private Collection<ThermostatSetPoint> thermostatStates;

	public Map<Sensor, List<TemperatureRecord>> getPlots() {
		return plots;
	}

	public void setPlots(Map<Sensor, List<TemperatureRecord>> plots) {
		this.plots = plots;
	}

	public Collection<ThermostatSetPoint> getThermostatStates() {
		return thermostatStates;
	}

	public void setThermostatStates(Collection<ThermostatSetPoint> thermostatStates) {
		this.thermostatStates = thermostatStates;
	}
}
