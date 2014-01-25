package fr.seb.services;

import java.util.List;

import fr.seb.entities.TemperatureRecord;
import fr.seb.entities.ThermostatSetPoint;
import fr.seb.entities.ThermostatStatus;
import fr.seb.enumerations.SensorType;

public interface DataService {

	List<TemperatureRecord> getLastTemperatures(int lastUnit, int lastAmount);

	TemperatureRecord getTemperatureActuelle(SensorType capteur);

	List<ThermostatSetPoint> getLastThermostatSetPoint(int lastUnit, int lastAmount);

	List<ThermostatSetPoint> getLastHourThermostatSetPoint();

	List<ThermostatSetPoint> getLast24hThermostatSetPoint();

	List<ThermostatSetPoint> getLastMonthThermostatSetPoint();

	List<ThermostatSetPoint> getLastYearThermostatSetPoint();

	List<ThermostatStatus> getLastThermostatStatus(int lastUnit, int lastAmount);

	List<ThermostatStatus> getLastHourThermostatStatus();

	List<ThermostatStatus> getLast24hThermostatStatus();

	List<ThermostatStatus> getLastMonthThermostatStatus();

	List<ThermostatStatus> getLastYearThermostatStatus();

	List<ThermostatSetPoint> filterEffectiveThermostatSetPoint(int lastUnit, int lastAmount);
}
