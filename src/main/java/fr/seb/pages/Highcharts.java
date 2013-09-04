package fr.seb.pages;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import fr.seb.components.ThermostatStateComponent;
import fr.seb.data.beans.Chart;
import fr.seb.entities.Sensor;
import fr.seb.entities.TemperatureRecord;
import fr.seb.entities.ThermostatState;
import fr.seb.services.DataService;

@Import(library = { "context:js/lib/highcharts/prototype-adapter.js", "context:js/lib/highcharts/highcharts.js" }, stylesheet = "context:css/chart.css")
public class Highcharts {

	@Environmental
	private JavaScriptSupport javaScriptSupport;

	@Inject
	private Session session;

	@Inject
	@Property
	private DataService dataService;

	@SuppressWarnings("unused")
	@Component(parameters = { "id=literal:lastHour", "title=message:chart.title.thermostats-state-last-hour",
			"thermostatStates=dataService.lastHourThermostatState" })
	private ThermostatStateComponent lastHour;

	@SuppressWarnings("unused")
	@Component(parameters = { "id=literal:last24h", "title=message:chart.title.thermostats-state-last-24h",
			"thermostatStates=dataService.last24hThermostatState" })
	private ThermostatStateComponent last24h;

	@SuppressWarnings("unused")
	@Component(parameters = { "id=literal:lastMonth", "title=message:chart.title.thermostats-state-last-month",
			"thermostatStates=dataService.lastMonthThermostatState" })
	private ThermostatStateComponent lastMonth;

	@SuppressWarnings("unused")
	@Component(parameters = { "id=literal:lastYear", "title=message:chart.title.thermostats-state-last-year",
			"thermostatStates=dataService.lastYearThermostatState" })
	private ThermostatStateComponent lastYear;

	@AfterRender
	public void afterRender() {

		Chart chart = getChart();

		StringBuilder javascript = new StringBuilder();

		javascript.append("var chart1 = new Highcharts.Chart({");
		javascript.append("		chart: {");
		javascript.append("			renderTo: 'temperatureChart',");
		javascript.append("			type: 'line'");
		javascript.append("		},");
		javascript.append("		title: {");
		javascript.append("			text: 'Températures par capteur et état du thermostat'");
		javascript.append("		},");
		javascript.append("		xAxis: {");
		javascript.append("			type: 'datetime',");
		// javascript.append("			dateTimeLabelFormats: { // don't display the dummy year");
		// javascript.append("				month: '%e. %b',");
		// javascript.append("				year: '%b'");
		// javascript.append("			}");
		javascript.append("		},");
		javascript.append("		yAxis: {");
		javascript.append("			title: {");
		javascript.append("				 text: 'Température (°C)'");
		javascript.append("			},");
		javascript.append("			plotBands: [{");
		javascript.append("			    from: 24,");
		javascript.append("			    to: 26,");
		javascript.append("			    color: 'rgba(50, 50, 50, 0.1)',");
		javascript.append("			}]");
		javascript.append("		},");
		javascript.append("		series: [");
		for (Sensor sensor : chart.getPlots().keySet()) {
			javascript.append(writeTemperatureSerie(sensor, chart.getPlots().get(sensor)));
			javascript.append(",");
		}
		javascript.append(writeThermostatStateSerie(chart.getThermostatStates()));
		javascript.append("		]");
		javascript.append("});");

		javaScriptSupport.addScript(javascript.toString());
	}

	private Object writeThermostatStateSerie(List<ThermostatState> thermostatStates) {
		StringBuilder javascript = new StringBuilder();

		javascript.append("{");
		javascript.append("		name: 'Thermostat',");
		javascript.append("		step: true,");
		javascript.append("		data: [");
		for (ThermostatState thermostatState : thermostatStates) {
			javascript.append("		[Date.UTC(");
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(thermostatState.getDate());
			javascript.append(calendar.get(Calendar.YEAR));
			javascript.append("		,");
			javascript.append(calendar.get(Calendar.MONTH));
			javascript.append("		,");
			javascript.append(calendar.get(Calendar.DAY_OF_MONTH));
			javascript.append("		,");
			javascript.append(calendar.get(Calendar.HOUR_OF_DAY));
			javascript.append("		,");
			javascript.append(calendar.get(Calendar.MINUTE));
			javascript.append("		,");
			javascript.append(calendar.get(Calendar.SECOND));
			javascript.append("		),");
			javascript.append(thermostatState.isState() ? "30" : "10");
			javascript.append("		],");
		}
		javascript.append("		]");
		javascript.append("}");

		return javascript.toString();
	}

	private String writeTemperatureSerie(Sensor sensor, List<TemperatureRecord> temperatures) {
		StringBuilder javascript = new StringBuilder();

		javascript.append("{");
		javascript.append("		name: '").append(sensor.getLabel()).append("',");
		javascript.append("		marker: {");
		javascript.append("		    enabled: false,");
		javascript.append("		    states: {");
		javascript.append("		        hover: {");
		javascript.append("		            enabled: true");
		javascript.append("		        }");
		javascript.append("		    }");
		javascript.append("		},");
		javascript.append("		data: [");
		for (TemperatureRecord temperatureRecord : temperatures) {
			javascript.append("		[Date.UTC(");
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(temperatureRecord.getDate());
			javascript.append(calendar.get(Calendar.YEAR));
			javascript.append("		,");
			javascript.append(calendar.get(Calendar.MONTH));
			javascript.append("		,");
			javascript.append(calendar.get(Calendar.DAY_OF_MONTH));
			javascript.append("		,");
			javascript.append(calendar.get(Calendar.HOUR_OF_DAY));
			javascript.append("		,");
			javascript.append(calendar.get(Calendar.MINUTE));
			javascript.append("		,");
			javascript.append(calendar.get(Calendar.SECOND));
			javascript.append("		),");
			javascript.append(temperatureRecord.getValue());
			javascript.append("		],");
		}
		javascript.append("		]");
		javascript.append("}");

		return javascript.toString();
	}

	public Chart getChart() {
		Chart chart = new Chart();
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		Criteria createCriteria = session.createCriteria(TemperatureRecord.class).add(Restrictions.gt("date", calendar.getTime()));
		@SuppressWarnings("unchecked")
		List<TemperatureRecord> temperatureRecords = createCriteria.list();

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

		chart.setThermostatStates(dataService.getLast24hThermostatState());

		return chart;
	}
}
