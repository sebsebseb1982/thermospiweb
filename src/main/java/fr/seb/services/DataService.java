package fr.seb.services;

import java.util.Collection;

import fr.seb.entities.TemperatureRecord;
import fr.seb.entities.ThermostatSetPoint;
import fr.seb.enumerations.SensorType;

public interface DataService {

	Collection<TemperatureRecord> getLastTemperatures(int lastUnit, int lastAmount);

	TemperatureRecord getTemperatureActuelle(SensorType capteur);

	Collection<ThermostatSetPoint> getLastThermostatState(int lastUnit, int lastAmount);

	Collection<ThermostatSetPoint> getLastHourThermostatState();

	Collection<ThermostatSetPoint> getLast24hThermostatState();

	Collection<ThermostatSetPoint> getLastMonthThermostatState();

	Collection<ThermostatSetPoint> getLastYearThermostatState();
}
