package fr.seb.data.beans;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import fr.seb.entities.Sensor;
import fr.seb.entities.TemperatureRecord;
import fr.seb.entities.ThermostatSetPoint;

public class Chart {
	private Map<Sensor, List<TemperatureRecord>> plots;

	private Collection<ThermostatSetPoint> thermostatSetPoints;

	private Collection<ThermostatSetPoint> thermostatHeating;

	public Map<Sensor, List<TemperatureRecord>> getPlots() {
		return plots;
	}

	public void setPlots(Map<Sensor, List<TemperatureRecord>> plots) {
		this.plots = plots;
	}

	public Collection<ThermostatSetPoint> getThermostatSetPoints() {
		return thermostatSetPoints;
	}

	public void setThermostatSetPoints(Collection<ThermostatSetPoint> thermostatSetPoints) {
		this.thermostatSetPoints = thermostatSetPoints;
	}

	public Collection<ThermostatSetPoint> getThermostatHeating() {
		return thermostatHeating;
	}

	public void setThermostatHeating(Collection<ThermostatSetPoint> thermostatHeating) {
		this.thermostatHeating = thermostatHeating;
	}
}
