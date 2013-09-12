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
import fr.seb.components.ThermostatStateComponent;
import fr.seb.data.beans.Chart;
import fr.seb.data.coordination.CoordinationTemperatures;
import fr.seb.entities.TemperatureRecord;
import fr.seb.entities.ThermostatState;
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
	@Component(parameters = { "id=literal:temperatureChart", "title=message:chart.title.temperatures", "chart=temperatureChart",
			"heatingSetPoint=dataService.heatingSetPoint" })
	private TemperatureChart temperatureChart;

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

	public Chart getTemperatureChart() {
		Collection<TemperatureRecord> temperatures = dataService.getLastTemperatures(lastUnit, lastAmount);
		Collection<ThermostatState> thermostatStates = dataService.getLastThermostatState(lastUnit, lastAmount);
		return coordinationTemperatures.getChartFromTemperature(temperatures, thermostatStates);
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
