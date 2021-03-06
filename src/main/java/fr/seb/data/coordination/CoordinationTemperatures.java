package fr.seb.data.coordination;

import java.util.Collection;

import fr.seb.data.beans.Chart;
import fr.seb.entities.TemperatureRecord;
import fr.seb.entities.ThermostatSetPoint;

public interface CoordinationTemperatures {
	public Chart getChartFromTemperature(Collection<TemperatureRecord> temperatures, Collection<ThermostatSetPoint> thermostatSetPoint,
			Collection<ThermostatSetPoint> thermostatHeating);
}
