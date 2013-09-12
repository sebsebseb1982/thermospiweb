package fr.seb.components;

import java.util.List;

import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.joda.time.Duration;
import org.joda.time.Interval;

import fr.seb.base.HighchartsComponent;
import fr.seb.entities.ThermostatState;

@Import(library = { "context:js/lib/highcharts/prototype-adapter.js", "context:js/lib/highcharts/highcharts.js" }, stylesheet = "context:css/chart.css")
public class ThermostatStateComponent extends HighchartsComponent {

	@Parameter(required = true)
	@Property
	private String id;

	@Parameter(required = true)
	@Property
	private String title;

	@Parameter(required = true)
	private List<ThermostatState> thermostatStates;

	@AfterRender
	public void afterRender() {

		StringBuilder javascript = new StringBuilder();

		javascript.append("var ").append(id).append(" = new Highcharts.Chart({");
		javascript.append("		chart: {");
		javascript.append("			renderTo: '").append(id).append("',");
		javascript.append("			plotBackgroundColor: null,");
		javascript.append("			plotBorderWidth: null,");
		javascript.append("			plotShadow: false");
		javascript.append("		},");
		javascript.append("		title: {");
		javascript.append("			text: '").append(title).append("'");
		javascript.append("		},");
		javascript.append("		tooltip: {");
		javascript.append("			pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'");
		javascript.append("		},");
		javascript.append("		plotOptions: {");
		javascript.append("			pie: {");
		javascript.append("				allowPointSelect: true,");
		javascript.append("				cursor: 'pointer',");
		javascript.append("				dataLabels: {");
		javascript.append("					enabled: true,");
		javascript.append("					color: '#000000',");
		javascript.append("					connectorColor: '#000000',");
		javascript.append("					format: '<b>{point.name}</b>: {point.percentage:.1f} %'");
		javascript.append("				}");
		javascript.append("			}");
		javascript.append("		},");
		javascript.append("		series: [{");
		javascript.append("			type: 'pie',");
		javascript.append("			name: 'Répartition chauffage',");
		javascript.append("			data: [");
		javascript.append(writeData());
		javascript.append("			]");
		javascript.append("		}]");
		javascript.append("});");

		javaScriptSupport.addScript(javascript.toString());
	}

	private String writeData() {

		float thermostatHighStatePercentage = getThermostatHighStatePercentage();

		StringBuilder data = new StringBuilder();

		data.append("{");
		data.append("	name: 'Allumé',");
		data.append("	y: ").append(thermostatHighStatePercentage).append(",");
		data.append("	sliced: true,");
		data.append("	color: '#D12A2A',");
		data.append("	selected: true");
		data.append("},");
		data.append("{");
		data.append("	name: 'Eteint',");
		data.append("	color: '#3B3EE3',");
		data.append("	y: ").append(100.0 - thermostatHighStatePercentage).append(",");
		data.append("}");

		return data.toString();
	}

	private float getThermostatHighStatePercentage() {
		Duration highState = new Duration(0);
		Duration lowState = new Duration(0);

		ThermostatState lastThermostatState = null;

		for (int i = 0; i < thermostatStates.size(); i++) {
			ThermostatState thermostate = thermostatStates.get(i);
			if (lastThermostatState != null) {
				boolean noMoreThermostatState = i == thermostatStates.size() - 1;
				if (lastThermostatState.isState() != thermostate.isState() || noMoreThermostatState) {
					Interval interval = new Interval(lastThermostatState.getDate().getTime(), thermostate.getDate().getTime());

					if (lastThermostatState.isState()) {
						highState = highState.plus(interval.toDurationMillis());
					} else {
						lowState = lowState.plus(interval.toDurationMillis());
					}

					lastThermostatState = thermostate;
				} else {
					continue;
				}
			} else {
				lastThermostatState = thermostate;
			}
		}
		return (float) (highState.getMillis() * 100) / (highState.getMillis() + lowState.getMillis());
	}
}
