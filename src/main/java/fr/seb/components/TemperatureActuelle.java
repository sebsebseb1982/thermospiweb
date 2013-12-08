package fr.seb.components;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.ioc.annotations.Inject;

import fr.seb.entities.TemperatureRecord;
import fr.seb.enumerations.SensorType;
import fr.seb.services.DataService;

@Import(stylesheet = "context:css/components/TemperatureActuelle.css")
public class TemperatureActuelle {

	@Inject
	private DataService dataService;

	public String getTemperatureActuelleEtage() {
		return afficheTemperature(dataService.getTemperatureActuelle(SensorType.ETAGE));
	}

	public String getTemperatureActuelleRDC() {
		return afficheTemperature(dataService.getTemperatureActuelle(SensorType.RDC));
	}

	public String getTemperatureActuelleExterieur() {
		return afficheTemperature(dataService.getTemperatureActuelle(SensorType.EXTERIEUR));
	}

	private String afficheTemperature(TemperatureRecord temperatureActuelle) {
		BigDecimal value = BigDecimal.valueOf(temperatureActuelle.getValue()).setScale(1, RoundingMode.HALF_UP);
		return value + "°C";
	}

}
