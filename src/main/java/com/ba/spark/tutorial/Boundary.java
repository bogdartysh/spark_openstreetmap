package com.ba.spark.tutorial;

import de.topobyte.osm4j.core.model.iface.OsmNode;
import java.io.Serializable;

/**
 * Created by bartyushenko on 04.07.16.
 */
class Boundary implements Serializable {
	public double latitudeMin = Double.MAX_VALUE;
	public double latitudeMax = Double.MIN_VALUE;
	public double longitudeMin = Double.MAX_VALUE;
	public double longitudeMax = Double.MIN_VALUE;

	public static Boundary EMPTY_BOUNDARY = new Boundary();

	public void includeNode(OsmNode node) {
		latitudeMin = Math.min(latitudeMin, node.getLatitude());
		latitudeMax = Math.max(latitudeMax, node.getLatitude());
		longitudeMin = Math.min(longitudeMin, node.getLongitude());
		longitudeMax = Math.max(longitudeMax, node.getLongitude());

	}

	@Override
	public String toString() {
		return "Boundary={" +
				"latitudeMin=" + latitudeMin +
				", latitudeMax=" + latitudeMax +
				", longitudeMin=" + longitudeMin +
				", longitudeMax=" + longitudeMax +
				'}';
	}

	public static boolean isNull(Boundary b) {
		return EMPTY_BOUNDARY.equals(b);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Boundary boundary = (Boundary) o;

		if (Double.compare(boundary.latitudeMin, latitudeMin) != 0)
			return false;
		if (Double.compare(boundary.latitudeMax, latitudeMax) != 0)
			return false;
		if (Double.compare(boundary.longitudeMin, longitudeMin) != 0)
			return false;
		return Double.compare(boundary.longitudeMax, longitudeMax) == 0;

	}

	@Override
	public int hashCode() {
		int result;
		long temp;
		temp = Double.doubleToLongBits(latitudeMin);
		result = (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(latitudeMax);
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(longitudeMin);
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(longitudeMax);
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		return result;
	}
}
