package org.nees.mustsim.replay;

import org.apache.log4j.Logger;

public class DbTableUpdate {
	private final DbTableCreation creation;
	private final Logger log = Logger.getLogger(DbTableUpdate.class);

	public DbTableUpdate(DbTableCreation creation) {
		super();
		this.creation = creation;
	}

	public String update(TableType table, RateType rate, double[] data) {
		int actual = data.length;
		int expected = creation.recordLength(table, rate);
		if (actual != expected) {
			log.error("Data for table " + table + " rate " + rate
					+ " is incorrect is " + actual + " should be " + expected);
			return "";
		}
		if (rate.equals(RateType.CONT)) {
			return updateCont(table, data);
		} else {
			return updateStep(table, data);
		}

	}

	private String updateCont(TableType table, double[] data) {
		String result = "insert into "
				+ creation.tableName(table, RateType.CONT) + " values(";
		for (int i = 0; i < data.length; i++) {
			result += (i == 0 ? "" : ", ") + Double.toString(data[i]);
		}
		result += ");";
		return result;
	}

	private String updateStep(TableType table, double[] data) {
		String result = "insert into "
				+ creation.tableName(table, RateType.STEP) + " values(";
		result += Double.toString(data[0]);
		for (int i = 1; i < 4; i++) {
			result += ", " + Integer.toString((int) Math.round(data[i]));
		}
		for (int i = 4; i < data.length; i++) {
			result += ", " + Double.toString(data[i]);
		}
		result += ");";
		return result;
	}

}
