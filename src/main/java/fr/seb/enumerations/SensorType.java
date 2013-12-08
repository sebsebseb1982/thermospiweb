package fr.seb.enumerations;

public enum SensorType {
	ETAGE(2), EXTERIEUR(1), RDC(3);

	private int id;

	private SensorType(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
}
