package org.nees.illinois.replay.test.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nees.illinois.replay.common.registries.ChannelNameRegistry;
import org.nees.illinois.replay.common.registries.TableDefiner;
import org.nees.illinois.replay.common.registries.TableRegistry;
import org.nees.illinois.replay.common.registries.TableType;
import org.nees.illinois.replay.common.types.TableDef;
import org.nees.illinois.replay.common.types.TableDefinitionI;
import org.nees.illinois.replay.test.utils.gen.TestCompositeQuery;
import org.nees.illinois.replay.test.utils.types.MatrixMixType;
import org.nees.illinois.replay.test.utils.types.QueryTestCases;
import org.nees.illinois.replay.test.utils.types.TestDataSource;

/**
 * This class provides mock channel lists used to fill in registries used by
 * unit tests.
 * @author Michael Bletzinger
 */
public class TestDatasetParameters {
	/**
	 * Map of channel lists for each channel list type.
	 */
	private final Map<TestDataSource, List<String>> cl2Channels = new HashMap<TestDataSource, List<String>>();
	/**
	 * Map of mock merged channel lists for each channel list type.
	 */
	private final Map<QueryTestCases, TestCompositeQuery> cl2q = new HashMap<QueryTestCases, TestCompositeQuery>();
	/**
	 * Map of table types for each channel list type.
	 */
	private final Map<TestDataSource, TableType> cl2tt = new HashMap<TestDataSource, TableType>();
	/**
	 * Name of mock experiment.
	 */
	private final String experiment;
	/**
	 * Flag to indicate that the alternate mock channel list set should be used.
	 */
	private final boolean second;

	/**
	 * Constructor.
	 * @param second
	 *            Flag to indicate that the alternate mock channel list set
	 *            should be used.
	 * @param experiment
	 *            Name of mock experiment.
	 */
	public TestDatasetParameters(final boolean second,final String experiment) {
		super();
		this.second = second;
		this.experiment = experiment;
		initTableLists();
		initQueryLists();
	}

	/**
	 * Fill in the {@link ChannelNameRegistry} with the mock channel names.
	 * @param cnr
	 *            Reference to the registry.
	 */
	public final void fillCnr(final ChannelNameRegistry cnr) {
		TestDataSource[] types = { TestDataSource.OM, TestDataSource.DAQ };
		for (TestDataSource type : types) {
			for (String c : getChannels(type)) {
				cnr.addChannel(getTt(type), c);
			}
		}
	}

	/**
	 * Fill in the {@link TableRegistry} with the mock tables.
	 * @param tblr
	 *            Reference to the table registry.
	 */
	public final void fillTblr(final TableRegistry tblr) {
		for (TestDataSource type : TestDataSource.values()) {
			List<String> channels = cl2Channels.get(type);
			TableDefinitionI tc = new TableDef(channels, getTableName(type), type.toString());
			tblr.setTable(getTableName(type), tc);
		}
	}

	/**
	 * Get the consolidated channel list for a given type.
	 * @param listType
	 *            Type identifying the test list.
	 * @return The list.
	 */
	public final List<String> getChannels(final TestDataSource listType) {
		List<String> result = new ArrayList<String>();
		result.addAll(cl2Channels.get(listType));
		return result;
	};

	/**
	 * @return the experiment
	 */
	public final String getExperiment() {
		return experiment;
	}

	/**
	 * @param type
	 *            Test type.
	 * @param cnr
	 *            Channel Name Registry containing the table channels.
	 * @param tr
	 *            Table Registry where the table is located.
	 * @return A table definition based on a test type.
	 */
	public final TableDefinitionI getTableDefinition(final TestDataSource type,
			final ChannelNameRegistry cnr, final TableRegistry tr) {
		TableDefiner tdr = new TableDefiner(cnr, tr);
		return tdr.define(getTableName(type), getTt(type), getChannels(type));
	}

	/**
	 * Get the table name associated with a test type.
	 * @param type
	 *            Test list type.
	 * @return Table name.
	 */
	public final String getTableName(final TestDataSource type) {
		return type.toString() + "dbTable";
	}

	/**
	 * Get the test query for a given type.
	 * @param typ
	 *            Type identifying the test query.
	 * @return The test query.
	 */
	public final TestCompositeQuery getTestQuery(
			final QueryTestCases typ) {
		return cl2q.get(typ);
	}

	/**
	 * Get the {@link TableType} associated with the test list.
	 * @param type
	 *            Test list type.
	 * @return The table type.
	 */
	public final TableType getTt(final TestDataSource type) {
		return cl2tt.get(type);
	}

	/**
	 * Initialize all of the lists.
	 */
	private void initQueryLists() {
		List<String> omChnls = new ArrayList<String>();
		omChnls.add("OM/CntrlSensor/D_West_X_3");
		omChnls.add("OM/Cmd/LBCB1/Actuator/C_LBCB1_X1_0");
		omChnls.add("OM/Disp/LBCB1/Cartesian/D_LBCB1_RZ_5");
		omChnls.add("OM/Load/LBCB1/Actuator/L_LBCB2_Z1_2");
		if (second) {
			omChnls.add("OM/CntrlSensor/D_West_Z_4");
			omChnls.add("OM/CntrlSensor/D_North_Y_2");
		}

		List<String> om2Chnls = new ArrayList<String>();
		om2Chnls.add("OM2/Disp/LBCB2/Cartesian/D_LBCB2_RY_1");
		om2Chnls.add("OM2/CntrlSensor/D_West_X_3");
		om2Chnls.add("OM2/Cmd/LBCB2/Actuator/C_LBCB2_Z1_4");

		List<String> daqChnls = new ArrayList<String>();
		daqChnls.add("DAQ/StrainGauge/Steel/WestFlange/FirstFloor/SGWFF1WL03B_W7_SG_B3_3");
		daqChnls.add("DAQ/StrainGauge/Steel/Web/ThirdFloor/SGWWF2WL06K_W7_SG_K13_2");
		daqChnls.add("DAQ/StrainGauge/Steel/Web/ThirdFloor/SGWWF2WL07K_W7_SG_K14_7");
		if (second) {
			daqChnls.add("DAQ/StrainGauge/Steel/Web/ThirdFloor/SGWWF2WL05K_W7_SG_K12_4");
			daqChnls.add("DAQ/StrainGauge/Steel/WestFlange/FirstFloor/SGWFF1WL03B_W7_SG_B23_5");
			daqChnls.add("DAQ/StrainGauge/Steel/Web/ThirdFloor/SGWWF2WL08K_W7_SG_K18_9");
		}

		List<String> daqChnls2 = new ArrayList<String>();
		daqChnls2
		.add("DAQ2/StrainGauge/Steel/WestFlange/FirstFloor/SGWFF1WL03B_W7_SG_B3_3");
		daqChnls2
		.add("DAQ2/StrainGauge/Steel/Web/ThirdFloor/SGWWF2WL06K_W7_SG_K13_2");
		daqChnls2
		.add("DAQ2/StrainGauge/Steel/Web/ThirdFloor/SGWWF2WL07K_W7_SG_K14_7");

		TestCompositeQuery qOmCtl = new TestCompositeQuery(
				MatrixMixType.AddAfter, null, omChnls, "OM", TestDataSource.OM);
		cl2q.put(QueryTestCases.QueryOm, qOmCtl);

		TestCompositeQuery qOm2Ctl = new TestCompositeQuery(
				MatrixMixType.AddAfter, null, om2Chnls, "OM2", TestDataSource.OM2);
		cl2q.put(QueryTestCases.QueryOm2, qOm2Ctl);

		TestCompositeQuery qDaqCtl = new TestCompositeQuery(
				MatrixMixType.AddAfter, null, daqChnls, "DAQ", TestDataSource.DAQ);
		cl2q.put(QueryTestCases.QueryDaq, qDaqCtl);

		TestCompositeQuery qDaq2Ctl = new TestCompositeQuery(
				MatrixMixType.AddAfter, null, daqChnls2, "DAQ", TestDataSource.DAQ2);
		cl2q.put(QueryTestCases.QueryDaq2, qDaq2Ctl);
		List<String>kChnls = new ArrayList<String>();
		kChnls.add("Krypton1/4/X/4_1");
		kChnls.add("Krypton1/4/X/4_4");
		kChnls.add("Krypton1/4/X/4_5");
		kChnls.add("Krypton1/4/Z/4_1");
		kChnls.add("Krypton1/4/Z/4_5");
		TestCompositeQuery qKCtl = new TestCompositeQuery(
				MatrixMixType.AddAfter, null, kChnls, "Krypton1", TestDataSource.Krypton1);
		cl2q.put(QueryTestCases.QueryKrypton1, qKCtl);

		List<String>k2Chnls = new ArrayList<String>();
		k2Chnls.add("Krypton2/4/X/4_4");
		k2Chnls.add("Krypton2/4/X/4_5");
		k2Chnls.add("Krypton2/3/Y/3_1");
		k2Chnls.add("Krypton2/4/Y/4_3");
		k2Chnls.add("Krypton2/4/Y/4_4");
		k2Chnls.add("Krypton2/4/Y/4_5");
		k2Chnls.add("Krypton2/3/Z/3_1");
		k2Chnls.add("Krypton2/3/Z/3_2");
		TestCompositeQuery qK2Ctl = new TestCompositeQuery(
				MatrixMixType.AddAfter, null, k2Chnls, "Krypton2", TestDataSource.Krypton2);
		cl2q.put(QueryTestCases.QueryKrypton2, qK2Ctl);

		TestCompositeQuery query = new TestCompositeQuery(
				MatrixMixType.AddBefore, qOmCtl, daqChnls,
				QueryTestCases.QueryBefore.toString(), TestDataSource.DAQ);
		cl2q.put(QueryTestCases.QueryBefore, query);

		query = new TestCompositeQuery(MatrixMixType.AddAfter, qOmCtl,
				daqChnls, QueryTestCases.QueryAfter.toString(), TestDataSource.DAQ);
		cl2q.put(QueryTestCases.QueryAfter, query);

		query = new TestCompositeQuery(MatrixMixType.AddMiddle,
				qOmCtl, daqChnls, QueryTestCases.QueryMiddle.toString(), TestDataSource.DAQ);
		cl2q.put(QueryTestCases.QueryMiddle, query);

		query = new TestCompositeQuery(MatrixMixType.AddInterleaved,
				qOmCtl, daqChnls, QueryTestCases.QueryMixed.toString(), TestDataSource.DAQ);
		cl2q.put(QueryTestCases.QueryMixed, query);

		query = new TestCompositeQuery(MatrixMixType.AddMiddle, query,
				daqChnls2, QueryTestCases.QueryTriple.toString(), TestDataSource.DAQ2);
		cl2q.put(QueryTestCases.QueryTriple, query);
	}

	/**
	 * Initialize the test tables.
	 */
	private void initTableLists() {
		List<String> chnls = new ArrayList<String>();
		chnls.add("OM/Cmd/LBCB1/Actuator/C_LBCB1_X1_0");
		chnls.add("OM/Disp/LBCB1/Cartesian/D_LBCB1_RY_1");
		chnls.add("OM/Load/LBCB1/Actuator/L_LBCB2_Z1_2");
		chnls.add("OM/CntrlSensor/D_West_X_3");
		chnls.add("OM/Cmd/LBCB1/Actuator/C_LBCB1_Z1_4");
		chnls.add("OM/Disp/LBCB1/Cartesian/D_LBCB1_RZ_5");
		if (second) {
			chnls.add("OM/CntrlSensor/D_West_Z_4");
			chnls.add("OM/CntrlSensor/D_East_X_1");
			chnls.add("OM/CntrlSensor/D_North_Y_2");
		}
		cl2Channels.put(TestDataSource.OM, chnls);
		cl2tt.put(TestDataSource.OM, TableType.Control);

		chnls = new ArrayList<String>();
		chnls.add("OM2/Cmd/LBCB2/Actuator/C_LBCB2_X1_0");
		chnls.add("OM2/Disp/LBCB2/Cartesian/D_LBCB2_RY_1");
		chnls.add("OM2/Load/LBCB2/Actuator/L_LBCB2_Z1_2");
		chnls.add("OM2/CntrlSensor/D_West_X_3");
		chnls.add("OM2/Cmd/LBCB2/Actuator/C_LBCB2_Z1_4");
		chnls.add("OM2/Disp/LBCB2/Cartesian/D_LBCB2_RZ_5");
		cl2Channels.put(TestDataSource.OM2, chnls);
		cl2tt.put(TestDataSource.OM2, TableType.Control);

		chnls = new ArrayList<String>();
		chnls.add("DAQ/DisplacementSensor/WestFlange/FirstFloor/DTV02F1A_W7_LinPot05_0");
		chnls.add("DAQ/StrainGauge/Steel/Web/SecondFloor/SGWWF2WL05K_W7_SG_K5_2");
		chnls.add("DAQ/StrainGauge/Steel/WestFlange/FirstFloor/SGWFF1WL03B_W7_SG_B3_3");
		chnls.add("DAQ/StrainGauge/Steel/Web/ThirdFloor/SGWWF2WL06K_W7_SG_K13_2");
		chnls.add("DAQ/StrainGauge/Steel/Web/ThirdFloor/SGWWF2WL07K_W7_SG_K14_7");
		if (second) {
			chnls.add("DAQ/StrainGauge/Steel/Web/ThirdFloor/SGWWF2WL05K_W7_SG_K12_4");
			chnls.add("DAQ/StrainGauge/Steel/Web/ThirdFloor/SGWWF2WL08K_W7_SG_K18_9");
			chnls.add("DAQ/StrainGauge/Steel/WestFlange/FirstFloor/SGWFF1WL03B_W7_SG_B23_5");
		}
		cl2Channels.put(TestDataSource.DAQ, chnls);
		cl2tt.put(TestDataSource.DAQ, TableType.DAQ);

		chnls = new ArrayList<String>();
		chnls.add("DAQ2/DisplacementSensor/WestFlange/FirstFloor/DTV02F1A_W7_LinPot05_0");
		chnls.add("DAQ2/StrainGauge/Steel/Web/SecondFloor/SGWWF2WL05K_W7_SG_K5_2");
		chnls.add("DAQ2/StrainGauge/Steel/WestFlange/FirstFloor/SGWFF1WL03B_W7_SG_B3_3");
		chnls.add("DAQ2/StrainGauge/Steel/Web/ThirdFloor/SGWWF2WL06K_W7_SG_K13_2");
		chnls.add("DAQ2/StrainGauge/Steel/Web/ThirdFloor/SGWWF2WL07K_W7_SG_K14_7");
		cl2Channels.put(TestDataSource.DAQ2, chnls);
		cl2tt.put(TestDataSource.DAQ2, TableType.DAQ);

		chnls = new ArrayList<String>();
		chnls.add("Krypton1/4/X/4_1");
		chnls.add("Krypton1/4/X/4_2");
		chnls.add("Krypton1/4/X/4_3");
		chnls.add("Krypton1/4/X/4_4");
		chnls.add("Krypton1/4/X/4_5");
		chnls.add("Krypton1/4/Y/4_1");
		chnls.add("Krypton1/4/Y/4_2");
		chnls.add("Krypton1/4/Y/4_3");
		chnls.add("Krypton1/4/Y/4_4");
		chnls.add("Krypton1/4/Y/4_5");
		chnls.add("Krypton1/4/Z/4_1");
		chnls.add("Krypton1/4/Z/4_2");
		chnls.add("Krypton1/4/Z/4_3");
		chnls.add("Krypton1/4/Z/4_4");
		chnls.add("Krypton1/4/Z/4_5");
		cl2Channels.put(TestDataSource.Krypton1, chnls);
		cl2tt.put(TestDataSource.Krypton1, TableType.Krypton);

		chnls = new ArrayList<String>();
		chnls.add("Krypton2/3/X/3_1");
		chnls.add("Krypton2/3/X/3_2");
		chnls.add("Krypton2/4/X/4_3");
		chnls.add("Krypton2/4/X/4_4");
		chnls.add("Krypton2/4/X/4_5");
		chnls.add("Krypton2/3/Y/3_1");
		chnls.add("Krypton2/3/Y/3_2");
		chnls.add("Krypton2/4/Y/4_3");
		chnls.add("Krypton2/4/Y/4_4");
		chnls.add("Krypton2/4/Y/4_5");
		chnls.add("Krypton2/3/Z/3_1");
		chnls.add("Krypton2/3/Z/3_2");
		chnls.add("Krypton2/4/Z/4_3");
		chnls.add("Krypton2/4/Z/4_4");
		chnls.add("Krypton2/4/Z/4_5");
		cl2Channels.put(TestDataSource.Krypton2, chnls);
		cl2tt.put(TestDataSource.Krypton2, TableType.Krypton);

	}
}
