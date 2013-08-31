package fr.seb.components;

import java.util.LinkedList;

import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

import fr.seb.data.beans.Chart;
import fr.seb.entities.Sensor;
import fr.seb.entities.TemperatureRecord;
import fr.seb.entities.ThermostatState;

@Import(library = { "context:js/TemperatureChart.js", "context:js/flotr-0.2.0-alpha.js", "context:js/lib/base64.js", "context:js/lib/canvas2image.js",
		"context:js/lib/canvastext.js", "context:js/lib/excanvas.js" }, stylesheet = "context:css/TemperatureChart.css")
public class TemperatureChart {

	private static final int HIGH_STATE = 30;
	private static final int LOW_STATE = 10;

	@Environmental
	private JavaScriptSupport javaScriptSupport;

	@Parameter(required = true)
	private Chart chart;

	@AfterRender
	public void afterRender() {

		StringBuilder javascript = new StringBuilder();

		int dataIndex = 0;

		for (Sensor sensor : chart.getPlots().keySet()) {
			javascript.append("var d" + dataIndex + " = " + computeDataString(chart.getPlots().get(sensor)) + ";");
			dataIndex++;
		}

		if (!chart.getThermostatStates().isEmpty()) {
			javascript.append("var thermostatStates = " + computeThermostatStatesString(chart.getThermostatStates()) + ";");
		}

		dataIndex = 0;

		javascript.append("var f = Flotr.draw(");
		javascript.append("$('charts'),[ ");
		for (Sensor sensor : chart.getPlots().keySet()) {
			javascript.append("{data:d" + dataIndex + ", label:'" + sensor.getLabel() + "', lines:{fill:false}},");
			dataIndex++;
		}
		if (!chart.getThermostatStates().isEmpty()) {
			javascript.append("{data:thermostatStates, label:'Thermostat', lines:{fill:true}},");
		}
		javascript.append("],");
		javascript.append("{mouse:{");
		javascript.append("track: true,");
		javascript.append("color: 'purple',");
		javascript.append("sensibility: 1,");
		javascript.append("trackDecimals: 2,");
		javascript.append("trackFormatter: function(obj){ return 'x = ' + obj.x +', y = ' + obj.y; }");
		javascript.append("}}");
		// javascript.append("{");
		// javascript.append("xaxis:{");
		// javascript.append("noTicks: 7,");
		// javascript.append("tickFormatter: timestampHMTickFormatter");
		// javascript.append("},");
		// javascript.append("yaxis:{");
		// javascript.append("noTicks: 3");
		// javascript.append("}");
		// javascript.append("}");
		javascript.append(");");

		javaScriptSupport.addScript(javascript.toString());
	}

	private String computeThermostatStatesString(LinkedList<ThermostatState> thermostatStates) {
		StringBuilder data = new StringBuilder();

		Boolean lastState = null;

		data.append("[");
		for (ThermostatState state : thermostatStates) {
			if (lastState != null && lastState != state.isState()) {
				data.append("[");
				data.append(state.getDate().getTime() - 1);
				data.append(",");
				data.append(lastState ? HIGH_STATE : LOW_STATE);
				data.append("],");
			}

			data.append("[");
			data.append(state.getDate().getTime());
			data.append(",");
			data.append(state.isState() ? HIGH_STATE : LOW_STATE);
			data.append("],");

			lastState = state.isState();
		}
		data.append("]");

		return data.toString();
	}

	private String computeDataString(LinkedList<TemperatureRecord> records) {

		StringBuilder data = new StringBuilder();

		data.append("[");
		for (TemperatureRecord record : records) {
			data.append("[");
			data.append(record.getDate().getTime());
			data.append(",");
			data.append(record.getValue());
			data.append("],");
		}
		data.append("]");

		return data.toString();
	}
}
