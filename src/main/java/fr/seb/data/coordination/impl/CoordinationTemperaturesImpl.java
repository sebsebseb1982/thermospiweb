package fr.seb.data.coordination.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.seb.data.beans.Chart;
import fr.seb.data.coordination.CoordinationTemperatures;
import fr.seb.entities.Sensor;
import fr.seb.entities.TemperatureRecord;
import fr.seb.entities.ThermostatState;

public class CoordinationTemperaturesImpl implements CoordinationTemperatures {

	public Chart getChartFromTemperature(Collection<TemperatureRecord> temperatures, Collection<ThermostatState> thermostatStates) {

		Chart chart = new Chart();

		Map<Sensor, List<TemperatureRecord>> plots = new HashMap<Sensor, List<TemperatureRecord>>();

		for (TemperatureRecord temperatureRecord : temperatures) {
			Sensor sensor = temperatureRecord.getSensor();

			if (plots.containsKey(sensor)) {
				plots.get(sensor).add(temperatureRecord);
			} else {
				List<TemperatureRecord> temperatureRecordsBySensor = new ArrayList<TemperatureRecord>();
				temperatureRecordsBySensor.add(temperatureRecord);
				plots.put(sensor, temperatureRecordsBySensor);
			}
		}

		chart.setPlots(plots);

		chart.setThermostatStates(thermostatStates);

		return chart;
	}

}
