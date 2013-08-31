package fr.seb.pages;

import java.util.HashMap;
import java.util.LinkedList;
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

		Map<Sensor, LinkedList<TemperatureRecord>> plots = new HashMap<Sensor, LinkedList<TemperatureRecord>>();

		for (TemperatureRecord temperatureRecord : temperatureRecords) {
			Sensor sensor = temperatureRecord.getSensor();

			if (plots.containsKey(sensor)) {
				plots.get(sensor).add(temperatureRecord);
			} else {
				LinkedList<TemperatureRecord> temperatureRecordsBySensor = new LinkedList<TemperatureRecord>();
				temperatureRecordsBySensor.add(temperatureRecord);
				plots.put(sensor, temperatureRecordsBySensor);
			}
		}

		chart.setPlots(plots);

		@SuppressWarnings("unchecked")
		List<ThermostatState> thermostatStates = session.createCriteria(ThermostatState.class).list();

		chart.setThermostatStates(new LinkedList<ThermostatState>(thermostatStates));

		return chart;
	}

}
