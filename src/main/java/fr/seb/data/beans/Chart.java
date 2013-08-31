package fr.seb.data.beans;

import java.util.LinkedList;
import java.util.Map;

import fr.seb.entities.Sensor;
import fr.seb.entities.TemperatureRecord;
import fr.seb.entities.ThermostatState;

public class Chart {
	private Map<Sensor, LinkedList<TemperatureRecord>> plots;

	private LinkedList<ThermostatState> thermostatStates;

	public Map<Sensor, LinkedList<TemperatureRecord>> getPlots() {
		return plots;
	}

	public void setPlots(Map<Sensor, LinkedList<TemperatureRecord>> plots) {
		this.plots = plots;
	}

	public LinkedList<ThermostatState> getThermostatStates() {
		return thermostatStates;
	}

	public void setThermostatStates(LinkedList<ThermostatState> thermostatStates) {
		this.thermostatStates = thermostatStates;
	}
}
