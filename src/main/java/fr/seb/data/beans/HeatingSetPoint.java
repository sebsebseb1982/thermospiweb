package fr.seb.data.beans;

public class HeatingSetPoint {
	public float setPoint;

	public float hysteresis;

	public HeatingSetPoint(float setPoint, float hysteresis) {
		this.setPoint = setPoint;
		this.hysteresis = hysteresis;
	}

	public float getSetPoint() {
		return setPoint;
	}

	public float getHysteresis() {
		return hysteresis;
	}

}
