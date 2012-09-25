package org.nees.mustsim.replay.test;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.nees.mustsim.replay.db.query.DbSelect;
import org.nees.mustsim.replay.db.table.DbTableCreation;
import org.nees.mustsim.replay.db.table.DbTableUpdate;
import org.nees.mustsim.replay.db.table.RateType;
import org.nees.mustsim.replay.db.table.TableType;

public class TestTableStatements {
	private DbTableCreation dbT = new DbTableCreation("TESTDB");

	@Before
	public void setUp() throws Exception {
		List<String> channels = new ArrayList<String>();
		channels.add("OM_Cmd_LBCB1_Actuator_C__LBCB1__X1");
		channels.add("OM_Disp_LBCB1_Cartesian_D__LBCB1__RY");
		channels.add("OM_Load_LBCB1_Actuator_L__LBCB2__Z1");
		channels.add("OM_CntrlSensor_D__West__X");
		dbT.addTable(TableType.OM, channels);
		channels.clear();
		channels.add("DAQ_DisplacementSensor_WestFlange_FirstFloor_DTV02F1A__W7__LinPot05");
		channels.add("DAQ_StrainGauge_Steel_Web_SecondFloor_SGWWF2WL05K__W7__SG__K5");
		channels.add("DAQ_StrainGauge_Steel_WestFlange_FirstFloor_SGWFF1WL03B__W7__SG__B3");
		dbT.addTable(TableType.DAQ, channels);
	}

	@Test
	public void testCreateTableStatement() {
		String result = dbT.createTableStatement(TableType.OM, RateType.CONT);
		Assert.assertEquals("CREATE TABLE TESTDB.OM_CONT("
				+ "Time double NOT NULL,"
				+ " OM_Cmd_LBCB1_Actuator_C__LBCB1__X1 double NOT NULL,"
				+ " OM_Disp_LBCB1_Cartesian_D__LBCB1__RY double NOT NULL,"
				+ " OM_Load_LBCB1_Actuator_L__LBCB2__Z1 double NOT NULL,"
				+ " OM_CntrlSensor_D__West__X double NOT NULL,"
				+ " PRIMARY KEY (Time))", result);
		result = dbT.createTableStatement(TableType.OM, RateType.STEP);
		Assert.assertEquals("CREATE TABLE TESTDB.OM_STEP(" + ""
				+ "Time double NOT NULL," + " Step int NOT NULL,"
				+ " Substep int NOT NULL," + " CorrectionStep int NOT NULL,"
				+ " OM_Cmd_LBCB1_Actuator_C__LBCB1__X1 double NOT NULL,"
				+ " OM_Disp_LBCB1_Cartesian_D__LBCB1__RY double NOT NULL,"
				+ " OM_Load_LBCB1_Actuator_L__LBCB2__Z1 double NOT NULL,"
				+ " OM_CntrlSensor_D__West__X double NOT NULL,"
				+ " PRIMARY KEY (Step))", result);
		result = dbT.createTableStatement(TableType.DAQ, RateType.CONT);
		Assert.assertEquals(
				"CREATE TABLE TESTDB.DAQ_CONT("
						+ "Time double NOT NULL,"
						+ " DAQ_DisplacementSensor_WestFlange_FirstFloor_DTV02F1A__W7__LinPot05 double NOT NULL,"
						+ " DAQ_StrainGauge_Steel_Web_SecondFloor_SGWWF2WL05K__W7__SG__K5 double NOT NULL,"
						+ " DAQ_StrainGauge_Steel_WestFlange_FirstFloor_SGWFF1WL03B__W7__SG__B3 double NOT NULL,"
						+ " PRIMARY KEY (Time))", result);
		result = dbT.createTableStatement(TableType.DAQ, RateType.STEP);
		Assert.assertEquals(
				"CREATE TABLE TESTDB.DAQ_STEP("
						+ "Time double NOT NULL,"
						+ " Step int NOT NULL,"
						+ " Substep int NOT NULL,"
						+ " CorrectionStep int NOT NULL,"
						+ " DAQ_DisplacementSensor_WestFlange_FirstFloor_DTV02F1A__W7__LinPot05 double NOT NULL,"
						+ " DAQ_StrainGauge_Steel_Web_SecondFloor_SGWWF2WL05K__W7__SG__K5 double NOT NULL,"
						+ " DAQ_StrainGauge_Steel_WestFlange_FirstFloor_SGWFF1WL03B__W7__SG__B3 double NOT NULL,"
						+ " PRIMARY KEY (Step))", result);
	}

	@Test
	public void testUpdateTableStatement() {
		DbTableUpdate update = new DbTableUpdate(dbT);
		double contData = 1234563.234;
		double[] stepData = { 1234563.234, 2.0, 3.0, 123.0 };
		double[] almostStepData = { 1234563.234, 1.999, 2.9934, 122.699 };
		double[] omData = { 1.2, 2.3, 3.4, 4.5 };
		double[] daqData = { .012, .023, .034 };

		double[] result = new double[omData.length + 1];
		System.arraycopy(omData, 0, result, 1, omData.length);
		result[0] = contData;
		String sresult = update.update(TableType.OM, RateType.CONT, result);
		Assert.assertEquals(
				"insert into TESTDB.OM_CONT values(1234563.234, 1.2, 2.3, 3.4, 4.5)",
				sresult);

		result = new double[omData.length + stepData.length];
		System.arraycopy(omData, 0, result, stepData.length, omData.length);
		System.arraycopy(stepData, 0, result, 0, stepData.length);
		sresult = update.update(TableType.OM, RateType.STEP, result);
		Assert.assertEquals(
				"insert into TESTDB.OM_STEP values(1234563.234, 2, 3, 123, 1.2, 2.3, 3.4, 4.5)",
				sresult);

		result = new double[daqData.length + 1];
		System.arraycopy(daqData, 0, result, 1, daqData.length);
		result[0] = contData;
		sresult = update.update(TableType.DAQ, RateType.CONT, result);
		Assert.assertEquals(
				"insert into TESTDB.DAQ_CONT values(1234563.234, 0.012, 0.023, 0.034)",
				sresult);

		result = new double[daqData.length + stepData.length];
		System.arraycopy(daqData, 0, result, stepData.length, daqData.length);
		System.arraycopy(stepData, 0, result, 0, stepData.length);
		sresult = update.update(TableType.DAQ, RateType.STEP, result);
		Assert.assertEquals(
				"insert into TESTDB.DAQ_STEP values(1234563.234, 2, 3, 123, 0.012, 0.023, 0.034)",
				sresult);

		System.arraycopy(almostStepData, 0, result, 0, almostStepData.length);
		sresult = update.update(TableType.DAQ, RateType.STEP, result);
		Assert.assertEquals(
				"insert into TESTDB.DAQ_STEP values(1234563.234, 2, 3, 123, 0.012, 0.023, 0.034)",
				sresult);
	}

	@Test
	public void testQueryStatements() {
		List<String> channels = new ArrayList<String>();
		channels.add("OM_Cmd_LBCB1_Actuator_C__LBCB1__X1");
		channels.add("OM_Load_LBCB1_Actuator_L__LBCB2__Z1");
		channels.add("OM_CntrlSensor_D__West__X");
		DbSelect query = new DbSelect(channels, "Query1", dbT);
		String[] expected = {
				"SELECT Time OM_Cmd_LBCB1_Actuator_C__LBCB1__X1, OM_Load_LBCB1_Actuator_L__LBCB2__Z1, OM_CntrlSensor_D__West__X FROM OM_CONT",
				"SELECT Step OM_Cmd_LBCB1_Actuator_C__LBCB1__X1, OM_Load_LBCB1_Actuator_L__LBCB2__Z1, OM_CntrlSensor_D__West__X FROM OM_STEP" };
		for (RateType r : RateType.values()) {
			Assert.assertEquals(expected[r.ordinal()], query.getSelect(r));
		}
		channels.add("DAQ_StrainGauge_Steel_Web_SecondFloor_SGWWF2WL05K__W7__SG__K5");
		channels.add("DAQ_StrainGauge_Steel_WestFlange_FirstFloor_SGWFF1WL03B__W7__SG__B3");
		query = new DbSelect(channels, "Query2", dbT);
		String[] expected2 = {
				"SELECT Time OM_Cmd_LBCB1_Actuator_C__LBCB1__X1, OM_Load_LBCB1_Actuator_L__LBCB2__Z1, OM_CntrlSensor_D__West__X FROM OM_CONT"
				+ " UNION SELECT Time DAQ_StrainGauge_Steel_Web_SecondFloor_SGWWF2WL05K__W7__SG__K5, DAQ_StrainGauge_Steel_WestFlange_FirstFloor_SGWFF1WL03B__W7__SG__B3 FROM DAQ_CONT",
				"SELECT Step OM_Cmd_LBCB1_Actuator_C__LBCB1__X1, OM_Load_LBCB1_Actuator_L__LBCB2__Z1, OM_CntrlSensor_D__West__X FROM OM_STEP"
				+ " UNION SELECT Step DAQ_StrainGauge_Steel_Web_SecondFloor_SGWWF2WL05K__W7__SG__K5, DAQ_StrainGauge_Steel_WestFlange_FirstFloor_SGWFF1WL03B__W7__SG__B3 FROM DAQ_STEP" };
		for (RateType r : RateType.values()) {
			Assert.assertEquals(expected2[r.ordinal()], query.getSelect(r));
		}

	}
}
