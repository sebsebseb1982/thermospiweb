package fr.seb.data.beans;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import fr.seb.entities.Sensor;
import fr.seb.entities.TemperatureRecord;
import fr.seb.entities.ThermostatState;

public class Chart {
	private Map<Sensor, List<TemperatureRecord>> plots;

	private Collection<ThermostatState> thermostatStates;

	public Map<Sensor, List<TemperatureRecord>> getPlots() {
		return plots;
	}

	public void setPlots(Map<Sensor, List<TemperatureRecord>> plots) {
		this.plots = plots;
	}

	public Collection<ThermostatState> getThermostatStates() {
		return thermostatStates;
	}

	public void setThermostatStates(Collection<ThermostatState> thermostatStates) {
		this.thermostatStates = thermostatStates;
	}
}
