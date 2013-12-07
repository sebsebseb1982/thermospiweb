package fr.seb.components;

import java.util.Calendar;
import java.util.List;

import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

import fr.seb.data.beans.Chart;
import fr.seb.data.beans.HeatingSetPoint;
import fr.seb.entities.Sensor;
import fr.seb.entities.TemperatureRecord;

@Import(library = { "context:js/lib/highcharts/prototype-adapter.js", "context:js/lib/highcharts/highcharts.js" }, stylesheet = "context:css/chart.css")
public class TemperatureChart {

	@Parameter(required = true)
	@Property
	private String id;

	@Parameter(required = true)
	@Property
	private String title;

	@Environmental
	private JavaScriptSupport javaScriptSupport;

	@Parameter(required = true)
	private Chart chart;

	@Parameter
	private HeatingSetPoint heatingSetPoint;

	@AfterRender
	public void afterRender() {

		StringBuilder javascript = new StringBuilder();

		javascript.append("var chart1 = new Highcharts.Chart({");
		javascript.append("		chart: {");
		javascript.append("			renderTo: '").append(id).append("',");
		javascript.append("			type: 'line'");
		javascript.append("		},");
		javascript.append("		rangeSelector: {");
		javascript.append("		    selected: 1");
		javascript.append("		},");
		javascript.append("		scrollbar: {");
		javascript.append("			barBackgroundColor: 'gray',");
		javascript.append("			barBorderRadius: 7,");
		javascript.append("			barBorderWidth: 0,");
		javascript.append("			buttonBackgroundColor: 'gray',");
		javascript.append("			buttonBorderWidth: 0,");
		javascript.append("			buttonBorderRadius: 7,");
		javascript.append("			trackBackgroundColor: 'none',");
		javascript.append("			trackBorderWidth: 1,");
		javascript.append("			trackBorderRadius: 8,");
		javascript.append("			trackBorderColor: '#CCC'");
		javascript.append("		},");
		javascript.append("		title: {");
		javascript.append("			text: '").append(title).append("'");
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
		javascript.append(writeHeatingSetPoint());
		javascript.append("		},");
		javascript.append("		series: [");
		for (Sensor sensor : chart.getPlots().keySet()) {
			javascript.append(writeTemperatureSerie(sensor, chart.getPlots().get(sensor)));
			javascript.append(",");
		}
		// javascript.append(writeThermostatStateSerie(chart.getThermostatStates()));
		javascript.append("		]");
		javascript.append("});");

		javaScriptSupport.addScript(javascript.toString());
	}

	private String writeHeatingSetPoint() {
		StringBuilder javascript = new StringBuilder();

		if (heatingSetPoint != null) {
			javascript.append("			plotBands: [{");
			javascript.append("			    from: ").append(heatingSetPoint.getSetPoint() - heatingSetPoint.getHysteresis()).append(",");
			javascript.append("			    to: ").append(heatingSetPoint.getSetPoint() + heatingSetPoint.getHysteresis()).append(",");
			javascript.append("			    color: 'rgba(50, 50, 50, 0.1)',");
			javascript.append("			}]");
		}

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

	// private Object writeThermostatStateSerie(Collection<ThermostatSetPoint>
	// thermostatStates) {
	// StringBuilder javascript = new StringBuilder();
	//
	// javascript.append("{");
	// javascript.append("		name: 'Thermostat',");
	// javascript.append("		step: true,");
	// javascript.append("		data: [");
	// for (ThermostatSetPoint thermostatState : thermostatStates) {
	// javascript.append("		[Date.UTC(");
	// Calendar calendar = Calendar.getInstance();
	// calendar.setTime(thermostatState.getDate());
	// javascript.append(calendar.get(Calendar.YEAR));
	// javascript.append("		,");
	// javascript.append(calendar.get(Calendar.MONTH));
	// javascript.append("		,");
	// javascript.append(calendar.get(Calendar.DAY_OF_MONTH));
	// javascript.append("		,");
	// javascript.append(calendar.get(Calendar.HOUR_OF_DAY));
	// javascript.append("		,");
	// javascript.append(calendar.get(Calendar.MINUTE));
	// javascript.append("		,");
	// javascript.append(calendar.get(Calendar.SECOND));
	// javascript.append("		),");
	// javascript.append(thermostatState.isState() ? "30" : "10");
	// javascript.append("		],");
	// }
	// javascript.append("		]");
	// javascript.append("}");
	//
	// return javascript.toString();
	// }
}
