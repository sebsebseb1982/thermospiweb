package fr.seb.services.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import fr.seb.entities.TemperatureRecord;
import fr.seb.entities.ThermostatSetPoint;
import fr.seb.entities.ThermostatStatus;
import fr.seb.enumerations.SensorType;
import fr.seb.predicates.status.AfterDate;
import fr.seb.predicates.status.BeforeDate;
import fr.seb.predicates.status.BetweenTwoDates;
import fr.seb.services.DataService;

public class DataServiceImpl implements DataService {

	@Inject
	private Session session;

	public List<ThermostatSetPoint> getLastThermostatSetPoint(int lastUnit, int lastAmount) {
		Date now = new Date();

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(now);
		calendar.add(lastUnit, lastAmount * -1);
		Criteria createCriteria =

		session.createCriteria(ThermostatSetPoint.class).add(Restrictions.gt("date", calendar.getTime())).addOrder(Order.asc("date"));
		@SuppressWarnings("unchecked")
		List<ThermostatSetPoint> thermostatStates = createCriteria.list();

		// Ajout d'une fin au même état que le plus récent
		ThermostatSetPoint end = new ThermostatSetPoint();
		end.setDate(now);
		end.setValue(thermostatStates.get(thermostatStates.size() - 1).getValue());
		thermostatStates.add(end);

		return thermostatStates;
	}

	public List<ThermostatSetPoint> getLastHourThermostatSetPoint() {
		return getLastThermostatSetPoint(Calendar.HOUR_OF_DAY, 1);
	}

	public List<ThermostatSetPoint> getLast24hThermostatSetPoint() {
		return getLastThermostatSetPoint(Calendar.DAY_OF_YEAR, 1);
	}

	public List<ThermostatSetPoint> getLastMonthThermostatSetPoint() {
		return getLastThermostatSetPoint(Calendar.MONTH, 1);
	}

	public List<ThermostatSetPoint> getLastYearThermostatSetPoint() {
		return getLastThermostatSetPoint(Calendar.YEAR, 1);
	}

	public List<TemperatureRecord> getLastTemperatures(int lastUnit, int lastAmount) {
		Date now = new Date();

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(now);
		calendar.add(lastUnit, lastAmount * -1);
		Criteria createCriteria = session.createCriteria(TemperatureRecord.class).add(Restrictions.gt("date", calendar.getTime())).addOrder(Order.asc("date"));
		@SuppressWarnings("unchecked")
		List<TemperatureRecord> temperatureRecord = createCriteria.list();
		return temperatureRecord;
	}

	public TemperatureRecord getTemperatureActuelle(SensorType capteur) {
		Criteria createCriteria = session.createCriteria(TemperatureRecord.class).add(Restrictions.eq("sensor.id", capteur.getId()))
				.addOrder(Order.desc("date")).setMaxResults(1);
		return (TemperatureRecord) createCriteria.list().get(0);
	}

	public List<ThermostatStatus> getLastThermostatStatus(int lastUnit, int lastAmount) {
		Date now = new Date();

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(now);
		calendar.add(lastUnit, lastAmount * -1);
		Criteria createCriteria = session.createCriteria(ThermostatStatus.class).add(Restrictions.gt("date", calendar.getTime())).addOrder(Order.asc("date"));
		@SuppressWarnings("unchecked")
		List<ThermostatStatus> thermostatStatus = createCriteria.list();
		return thermostatStatus;
	}

	public List<ThermostatStatus> getLastHourThermostatStatus() {
		return getLastThermostatStatus(Calendar.HOUR_OF_DAY, 1);
	}

	public List<ThermostatStatus> getLast24hThermostatStatus() {
		return getLastThermostatStatus(Calendar.HOUR_OF_DAY, 24);
	}

	public List<ThermostatStatus> getLastMonthThermostatStatus() {
		return getLastThermostatStatus(Calendar.MONTH, 1);
	}

	public List<ThermostatStatus> getLastYearThermostatStatus() {
		return getLastThermostatStatus(Calendar.YEAR, 1);
	}

	public List<ThermostatSetPoint> filterEffectiveThermostatSetPoint(int lastUnit, int lastAmount) {

		List<ThermostatStatus> lastThermostatStatus = getEffectiveThermostatStatus(getLastThermostatStatus(lastUnit, lastAmount));

		List<ThermostatSetPoint> lastThermostatSetPoint = getEffectiveThermostatSetPoint(getLastThermostatSetPoint(lastUnit, lastAmount), lastThermostatStatus);

		return lastThermostatSetPoint;
	}

	@SuppressWarnings("unchecked")
	private List<ThermostatSetPoint> getEffectiveThermostatSetPoint(List<ThermostatSetPoint> thermostatSetPoints, List<ThermostatStatus> thermostatStatuses) {

		List<ThermostatSetPoint> thermostatSetPointToRemove = new ArrayList<ThermostatSetPoint>();
		List<ThermostatSetPoint> thermostatSetPointFakeBoundariesToAdd = new ArrayList<ThermostatSetPoint>();

		ThermostatStatus previousLowSignal = null;

		for (ThermostatStatus thermostatStatus : thermostatStatuses) {
			// Si remontée, on doit supprimer les précédents état de
			// priorité inférieure
			if (thermostatStatus.isStatus()) {
				// Si nous avons déja rencontré une descente
				if (previousLowSignal != null) {

					Predicate predicate = new fr.seb.predicates.setpoints.BetweenTwoDates(previousLowSignal.getDate(), thermostatStatus.getDate());
					List<ThermostatSetPoint> select = (List<ThermostatSetPoint>) CollectionUtils.select(thermostatSetPoints, predicate);

					ThermostatSetPoint startBoundaries = new ThermostatSetPoint();
					startBoundaries.setDate(previousLowSignal.getDate());
					startBoundaries.setValue(0);
					thermostatSetPointFakeBoundariesToAdd.add(startBoundaries);

					if (!select.isEmpty()) {
						ThermostatSetPoint endBoundaries = new ThermostatSetPoint();
						endBoundaries.setDate(thermostatStatus.getDate());
						endBoundaries.setValue(select.get(select.size() - 1).getValue());
						thermostatSetPointFakeBoundariesToAdd.add(endBoundaries);
					} else {
						ThermostatSetPoint endBoundaries = new ThermostatSetPoint();
						endBoundaries.setDate(thermostatStatus.getDate());
						endBoundaries.setValue(getPreviousThermostatSetPoint(thermostatSetPoints, previousLowSignal.getDate()).getValue());
						thermostatSetPointFakeBoundariesToAdd.add(endBoundaries);
					}

					thermostatSetPointToRemove.addAll(select);
				} else {
					Predicate predicate = new fr.seb.predicates.setpoints.BeforeDate(thermostatStatus.getDate());
					List<ThermostatSetPoint> select = (List<ThermostatSetPoint>) CollectionUtils.select(thermostatSetPoints, predicate);

					if (!select.isEmpty()) {
						ThermostatSetPoint endBoundaries = new ThermostatSetPoint();
						endBoundaries.setDate(thermostatStatus.getDate());
						endBoundaries.setValue(select.get(select.size() - 1).getValue());
						thermostatSetPointFakeBoundariesToAdd.add(endBoundaries);
					}

					thermostatSetPointToRemove.addAll(select);
				}

				// On nettoie les bornes pour relancer l'algo
				previousLowSignal = null;
			}
			// Sinon, on sauvegarde la descente en attendant la
			// prochaine remontée
			else {
				previousLowSignal = thermostatStatus;
			}
		}

		// Si nous n'avons pas trouvé l'état remontant
		if (previousLowSignal != null) {
			Predicate predicate = new fr.seb.predicates.setpoints.AfterDate(previousLowSignal.getDate());
			List<ThermostatSetPoint> select = (List<ThermostatSetPoint>) CollectionUtils.select(thermostatSetPoints, predicate);

			if (!select.isEmpty()) {
				ThermostatSetPoint startBoundaries = new ThermostatSetPoint();
				startBoundaries.setDate(previousLowSignal.getDate());
				startBoundaries.setValue(0);
				thermostatSetPointFakeBoundariesToAdd.add(startBoundaries);

				ThermostatSetPoint endBoundaries = new ThermostatSetPoint();
				endBoundaries.setDate(new Date());
				endBoundaries.setValue(0);
				thermostatSetPointFakeBoundariesToAdd.add(endBoundaries);
			}

			thermostatSetPointToRemove.addAll(select);
		}

		thermostatSetPoints.removeAll(thermostatSetPointToRemove);
		thermostatSetPoints.addAll(thermostatSetPointFakeBoundariesToAdd);

		Collections.sort(thermostatSetPoints);

		return thermostatSetPoints;
	}

	private ThermostatSetPoint getPreviousThermostatSetPoint(List<ThermostatSetPoint> thermostatSetPoints, Date date) {
		Collections.sort(thermostatSetPoints);

		ThermostatSetPoint previousThermostatSetPoint = null;

		for (ThermostatSetPoint thermostatSetPoint : thermostatSetPoints) {
			if (thermostatSetPoint.getDate().before(date)) {
				previousThermostatSetPoint = thermostatSetPoint;
			} else {
				break;
			}
		}

		return previousThermostatSetPoint;
	}

	@SuppressWarnings("unchecked")
	private List<ThermostatStatus> getEffectiveThermostatStatus(List<ThermostatStatus> thermostatStatuses) {

		List<ThermostatStatus> thermostatStatusesToRemove = new ArrayList<ThermostatStatus>();

		for (int currentPriority = 3; currentPriority > 1; currentPriority--) {
			ThermostatStatus previousLowSignal = null;

			for (ThermostatStatus thermostatStatus : thermostatStatuses) {
				// Si on rencotre un état de la priorité courante
				if (thermostatStatus.getPriority() == currentPriority) {
					// Si remontée, on doit supprimer les précédents état de
					// priorité inférieure
					if (thermostatStatus.isStatus()) {
						// Si nous avons déja rencontré une descente
						if (previousLowSignal != null) {
							Predicate predicate = new BetweenTwoDates(currentPriority, previousLowSignal.getDate(), thermostatStatus.getDate());
							thermostatStatusesToRemove.addAll((List<ThermostatStatus>) CollectionUtils.select(thermostatStatuses, predicate));
						} else {
							Predicate predicate = new BeforeDate(currentPriority, thermostatStatus.getDate());
							thermostatStatusesToRemove.addAll((List<ThermostatStatus>) CollectionUtils.select(thermostatStatuses, predicate));
						}

						// On nettoie les bornes pour relancer l'algo
						previousLowSignal = null;
					}
					// Sinon, on sauvegarde la descente en attendant la
					// prochaine remontée
					else {
						previousLowSignal = thermostatStatus;
					}
				}
			}

			// Si nous n'avons pas trouvé l'état remontant
			if (previousLowSignal != null) {
				Predicate predicate = new AfterDate(currentPriority, previousLowSignal.getDate());
				thermostatStatusesToRemove.addAll((List<ThermostatStatus>) CollectionUtils.select(thermostatStatuses, predicate));
			}
		}

		thermostatStatuses.removeAll(thermostatStatusesToRemove);

		return thermostatStatuses;
	}

	private void titre(String titre) {
		System.out.println("-------------------------------------------------------------------------");
		System.out.println(titre);
		System.out.println("-------------------------------------------------------------------------");
	}

}
