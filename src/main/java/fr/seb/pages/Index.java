package fr.seb.pages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Session;

import fr.seb.components.TemperatureChart;
import fr.seb.data.beans.Chart;
import fr.seb.entities.Sensor;
import fr.seb.entities.TemperatureRecord;
import fr.seb.entities.ThermostatState;

/**
 * Start page of application thermospi.
 */
public class Index {

	@Inject
	private Session session;

	@SuppressWarnings("unused")
	@Component(parameters = { "chart=chart" })
	private TemperatureChart temperatureChart;

	public Chart getChart() {
		Chart chart = new Chart();
		@SuppressWarnings("unchecked")
		List<TemperatureRecord> temperatureRecords = session.createCriteria(TemperatureRecord.class).list();

		Map<Sensor, List<TemperatureRecord>> plots = new HashMap<Sensor, List<TemperatureRecord>>();

		for (TemperatureRecord temperatureRecord : temperatureRecords) {
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

		@SuppressWarnings("unchecked")
		List<ThermostatState> thermostatStates = session.createCriteria(ThermostatState.class).list();

		chart.setThermostatStates(new ArrayList<ThermostatState>(thermostatStates));

		return chart;
	}

}
