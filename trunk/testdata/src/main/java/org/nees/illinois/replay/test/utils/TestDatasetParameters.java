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
import org.nees.illinois.replay.test.utils.data.QueryChannelLists;
import org.nees.illinois.replay.test.utils.types.MatrixMixType;
import org.nees.illinois.replay.test.utils.types.TestDatasetType;

/**
 * This class provides mock channel lists used to fill in registries used by
 * unit tests.
 * @author Michael Bletzinger
 */
public class TestDatasetParameters {
	/**
	 * Map of channel lists for each channel list type.
	 */
	private final Map<TestDatasetType, List<String>> cl2Channels = new HashMap<TestDatasetType, List<String>>();
	/**
	 * Map of mock merged channel lists for each channel list type.
	 */
	private final Map<TestDatasetType, QueryChannelLists> cl2q = new HashMap<TestDatasetType, QueryChannelLists>();
	/**
	 * Map of test table types to start times.
	 */
	private final Map<TestDatasetType, Double> cl2StartTime = new HashMap<TestDatasetType, Double>();
	/**
	 * Map of table types for each channel list type.
	 */
	private final Map<TestDatasetType, TableType> cl2tt = new HashMap<TestDatasetType, TableType>();
	/**
	 * Name of mock experiment.
	 */
	private final String experiment;
	/**
	 * List of channel list types which are used for mock queries.
	 */
	private final List<TestDatasetType> queryTypes = new ArrayList<TestDatasetType>();
	/**
	 * Flag to indicate that the alternate mock channel list set should be used.
	 */
	private final boolean second;
	/**
	 * Map of table types for each channel list type.
	 */
	private final List<TestDatasetType> tableTypes = new ArrayList<TestDatasetType>();

	/**
	 * Constructor.
	 * @param second
	 *            Flag to indicate that the alternate mock channel list set
	 *            should be used.
	 * @param experiment
	 *            Name of mock experiment.
	 */
	public TestDatasetParameters(final boolean second, final String experiment) {
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
		TestDatasetType[] types = { TestDatasetType.OM, TestDatasetType.DAQ };
		for (TestDatasetType type : types) {
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
		for (TestDatasetType type : tableTypes) {
			List<String> channels = cl2Channels.get(type);
			TableDefinitionI tc = new TableDef(channels, getTableName(type));
			tblr.setTable(getTableName(type), tc);
		}
	}

	/**
	 * Get the consolidated channel list for a given type.
	 * @param listType
	 *            Type identifying the test list.
	 * @return The list.
	 */
	public final List<String> getChannels(final TestDatasetType listType) {
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
	 * @return the queryTypes
	 */
	public final List<TestDatasetType> getQueryTypes() {
		return queryTypes;
	}

	/**
	 * Get the start time for the dataset type.
	 * @param typ
	 *            dataset type.
	 * @return the start time.
	 */
	public final double getStartTime(final TestDatasetType typ) {
		return cl2StartTime.get(typ);
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
	public final TableDefinitionI getTableDefinition(
			final TestDatasetType type, final ChannelNameRegistry cnr,
			final TableRegistry tr) {
		TableDefiner tdr = new TableDefiner(cnr, tr);
		return tdr.define(getTableName(type), getTt(type), getChannels(type));
	}

	/**
	 * Get the table name associated with a test type.
	 * @param type
	 *            Test list type.
	 * @return Table name.
	 */
	public final String getTableName(final TestDatasetType type) {
		return type.toString() + "dbTable";
	}

	/**
	 * @return the tableTypes
	 */
	public final List<TestDatasetType> getTableTypes() {
		return tableTypes;
	}

	/**
	 * Get the test query for a given type.
	 * @param typ
	 *            Type identifying the test query.
	 * @return The test query.
	 */
	public final QueryChannelLists getTestQuery(final TestDatasetType typ) {
		return cl2q.get(typ);
	}

	/**
	 * Get the {@link TableType} associated with the test list.
	 * @param type
	 *            Test list type.
	 * @return The table type.
	 */
	public final TableType getTt(final TestDatasetType type) {
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

		List<String> daqChnls = new ArrayList<String>();
		daqChnls.add("DAQ/StrainGauge/Steel/WestFlange/FirstFloor/SGWFF1WL03B_W7_SG_B3_3");
		daqChnls.add("DAQ/StrainGauge/Steel/Web/ThirdFloor/SGWWF2WL06K_W7_SG_K13_2");
		daqChnls.add("DAQ/StrainGauge/Steel/Web/ThirdFloor/SGWWF2WL07K_W7_SG_K14_7");
		if (second) {
			daqChnls.add("DAQ/StrainGauge/Steel/Web/ThirdFloor/SGWWF2WL05K_W7_SG_K12_4");
			daqChnls.add("DAQ/StrainGauge/Steel/WestFlange/FirstFloor/SGWFF1WL03B_W7_SG_B23_5");
			daqChnls.add("DAQ/StrainGauge/Steel/Web/ThirdFloor/SGWWF2WL08K_W7_SG_K18_9");
		}

		QueryChannelLists qOmCtl = new QueryChannelLists(
				MatrixMixType.AddAfter, null, omChnls, "OM");
		cl2q.put(TestDatasetType.QueryOm, qOmCtl);
		cl2Channels.put(TestDatasetType.QueryOm, omChnls);

		QueryChannelLists qDaqCtl = new QueryChannelLists(
				MatrixMixType.AddAfter, null, daqChnls, "DAQ");
		cl2q.put(TestDatasetType.QueryDaq, qDaqCtl);
		cl2Channels.put(TestDatasetType.QueryDaq, daqChnls);

		QueryChannelLists query = new QueryChannelLists(
				MatrixMixType.AddBefore, qOmCtl, daqChnls,
				TestDatasetType.QueryBefore.toString());
		cl2q.put(TestDatasetType.QueryBefore, query);
		cl2Channels.put(TestDatasetType.QueryBefore, query.combine());

		query = new QueryChannelLists(MatrixMixType.AddAfter, qOmCtl, daqChnls,
				TestDatasetType.QueryAfter.toString());
		cl2q.put(TestDatasetType.QueryAfter, query);
		cl2Channels.put(TestDatasetType.QueryAfter, query.combine());

		query = new QueryChannelLists(MatrixMixType.AddMiddle, qOmCtl,
				daqChnls, TestDatasetType.QueryMiddle.toString());
		cl2q.put(TestDatasetType.QueryMiddle, query);
		cl2Channels.put(TestDatasetType.QueryMiddle, query.combine());

		query = new QueryChannelLists(MatrixMixType.AddInterleaved, qOmCtl,
				daqChnls, TestDatasetType.QueryMixed.toString());
		cl2q.put(TestDatasetType.QueryMixed, query);
		cl2Channels.put(TestDatasetType.QueryMixed, query.combine());

		List<String> daqChnls2 = new ArrayList<String>();
		daqChnls2
				.add("DAQ2/StrainGauge/Steel/WestFlange/FirstFloor/SGWFF1WL03B_W7_SG_B3_3");
		daqChnls2
				.add("DAQ2/StrainGauge/Steel/Web/ThirdFloor/SGWWF2WL06K_W7_SG_K13_2");
		daqChnls2
				.add("DAQ2/StrainGauge/Steel/Web/ThirdFloor/SGWWF2WL07K_W7_SG_K14_7");
		query = new QueryChannelLists(MatrixMixType.AddMiddle, query,
				daqChnls2, TestDatasetType.QueryTriple.toString());
		cl2q.put(TestDatasetType.QueryTriple, query);
		cl2Channels.put(TestDatasetType.QueryTriple, query.combine());

		queryTypes.add(TestDatasetType.QueryDaq);
		queryTypes.add(TestDatasetType.QueryOm);
		queryTypes.add(TestDatasetType.QueryAfter);
		queryTypes.add(TestDatasetType.QueryBefore);
		queryTypes.add(TestDatasetType.QueryMiddle);
		queryTypes.add(TestDatasetType.QueryMixed);
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
		cl2Channels.put(TestDatasetType.OM, chnls);
		cl2tt.put(TestDatasetType.OM, TableType.Control);

		chnls = new ArrayList<String>();
		chnls.add("OM/Cmd/LBCB2/Actuator/C_LBCB2_X1_0");
		chnls.add("OM/Disp/LBCB2/Cartesian/D_LBCB2_RY_1");
		chnls.add("OM/Load/LBCB2/Actuator/L_LBCB2_Z1_2");
		chnls.add("OM/CntrlSensor/D_West_X_3");
		chnls.add("OM/Cmd/LBCB2/Actuator/C_LBCB2_Z1_4");
		chnls.add("OM/Disp/LBCB2/Cartesian/D_LBCB2_RZ_5");
		cl2Channels.put(TestDatasetType.OM2, chnls);
		cl2tt.put(TestDatasetType.OM2, TableType.Control);

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
		cl2Channels.put(TestDatasetType.DAQ, chnls);
		cl2tt.put(TestDatasetType.DAQ, TableType.DAQ);

		chnls = new ArrayList<String>();
		chnls.add("DAQ2/DisplacementSensor/WestFlange/FirstFloor/DTV02F1A_W7_LinPot05_0");
		chnls.add("DAQ2/StrainGauge/Steel/Web/SecondFloor/SGWWF2WL05K_W7_SG_K5_2");
		chnls.add("DAQ2/StrainGauge/Steel/WestFlange/FirstFloor/SGWFF1WL03B_W7_SG_B3_3");
		chnls.add("DAQ2/StrainGauge/Steel/Web/ThirdFloor/SGWWF2WL06K_W7_SG_K13_2");
		chnls.add("DAQ2/StrainGauge/Steel/Web/ThirdFloor/SGWWF2WL07K_W7_SG_K14_7");
		cl2Channels.put(TestDatasetType.DAQ2, chnls);
		cl2tt.put(TestDatasetType.DAQ2, TableType.DAQ);

		chnls = new ArrayList<String>();
		chnls.add("4/X/4_1");
		chnls.add("4/X/4_2");
		chnls.add("4/X/4_3");
		chnls.add("4/X/4_4");
		chnls.add("4/X/4_5");
		chnls.add("4/Y/4_1");
		chnls.add("4/Y/4_2");
		chnls.add("4/Y/4_3");
		chnls.add("4/Y/4_4");
		chnls.add("4/Y/4_5");
		chnls.add("4/Z/4_1");
		chnls.add("4/Z/4_2");
		chnls.add("4/Z/4_3");
		chnls.add("4/Z/4_4");
		chnls.add("4/Z/4_5");
		cl2Channels.put(TestDatasetType.Krypton, chnls);
		cl2tt.put(TestDatasetType.Krypton, TableType.Krypton);

		tableTypes.add(TestDatasetType.OM);
		tableTypes.add(TestDatasetType.OM2);
		tableTypes.add(TestDatasetType.DAQ);
		tableTypes.add(TestDatasetType.DAQ2);
		tableTypes.add(TestDatasetType.Krypton);
		final double startTime = 221.23;
		final double timeDiff = 1.44;
		double stime = startTime;
		cl2StartTime.put(TestDatasetType.OM, new Double(stime));
		stime += timeDiff;
		cl2StartTime.put(TestDatasetType.OM2, new Double(stime));
		stime += timeDiff;
		cl2StartTime.put(TestDatasetType.DAQ, new Double(stime));
		stime += timeDiff;
		cl2StartTime.put(TestDatasetType.DAQ2, new Double(stime));
		stime += timeDiff;
		cl2StartTime.put(TestDatasetType.Krypton, new Double(stime));
	}
}
