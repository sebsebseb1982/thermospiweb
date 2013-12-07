package fr.seb.services;

import java.util.Collection;

import fr.seb.entities.TemperatureRecord;

public interface DataService {

	public Collection<TemperatureRecord> getLastTemperatures(int lastUnit, int lastAmount);

}
