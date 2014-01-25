package fr.seb.pages;

import java.util.Calendar;
import java.util.Collection;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;

import fr.seb.components.TemperatureChart;
import fr.seb.data.beans.Chart;
import fr.seb.data.coordination.CoordinationTemperatures;
import fr.seb.entities.TemperatureRecord;
import fr.seb.entities.ThermostatSetPoint;
import fr.seb.services.DataService;

@Import(stylesheet = "context:css/thermospiweb.css")
public class Highcharts {
	@Property
	@Persist
	private Integer lastUnit;

	@Property
	@Persist
	private Integer lastAmount;

	@InjectComponent
	private Zone temperatureZone;

	@Inject
	@Property
	private DataService dataService;

	@Inject
	private CoordinationTemperatures coordinationTemperatures;

	public void onActivate() {
		if (lastUnit == null) {
			lastUnit = Calendar.DAY_OF_YEAR;
		}
		if (lastAmount == null) {
			lastAmount = 1;
		}
	}

	@SuppressWarnings("unused")
	@Component(parameters = { "id=literal:temperatureChart", "title=message:chart.title.temperatures", "chart=temperatureChart" })
	private TemperatureChart temperatureChart;

	public Chart getTemperatureChart() {
		Collection<TemperatureRecord> temperatures = dataService.getLastTemperatures(lastUnit, lastAmount);
		Collection<ThermostatSetPoint> effectiveThermostatSetPoint = dataService.getThermostatSetPoint(lastUnit, lastAmount);
		Collection<ThermostatSetPoint> effectiveThermostatHeating = dataService.getThermostatHeating(lastUnit, lastAmount);
		return coordinationTemperatures.getChartFromTemperature(temperatures, effectiveThermostatSetPoint, effectiveThermostatHeating);
	}

	Object onActionFromOneDay() {
		lastUnit = Calendar.DAY_OF_YEAR;
		lastAmount = 1;

		return temperatureZone.getBody();
	}

	Object onActionFromThreeDays() {
		lastUnit = Calendar.DAY_OF_YEAR;
		lastAmount = 3;

		return temperatureZone.getBody();
	}

	Object onActionFromFiveDays() {
		lastUnit = Calendar.DAY_OF_YEAR;
		lastAmount = 5;

		return temperatureZone.getBody();
	}
}
