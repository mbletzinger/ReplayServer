package org.nees.illinois.replay.test.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nees.illinois.replay.common.registries.ChannelNameRegistry;
import org.nees.illinois.replay.common.registries.TableIdentityRegistry;
import org.nees.illinois.replay.common.registries.TableType;
import org.nees.illinois.replay.data.RateType;

/**
 * This class provides mock channel lists used to fill in registries used by
 * unit tests.
 * @author Michael Bletzinger
 */
public class ChannelListTestMaps {
	/**
	 * Map of channel lists for each channel list type.
	 */
	private final Map<ChannelListType, List<String>> cl2Channels = new HashMap<ChannelListType, List<String>>();
	/**
	 * Map of mock merged channel lists for each channel list type.
	 */
	private final Map<ChannelListType, QueryChannelLists> cl2q = new HashMap<ChannelListType, QueryChannelLists>();
	/**
	 * Map of table types for each channel list type.
	 */
	private final Map<ChannelListType, TableType> cl2tt = new HashMap<ChannelListType, TableType>();
	/**
	 * Name of mock experiment.
	 */
	private final String experiment;
	/**
	 * List of channel list types which are used for mock queries.
	 */
	private final List<ChannelListType> queryTypes = new ArrayList<ChannelListType>();
	/**
	 * Flag to indicate that the alternate mock channel list set should be used.
	 */
	private final boolean second;
	/**
	 * Map of table types for each channel list type.
	 */
	private final List<ChannelListType> tableTypes = new ArrayList<ChannelListType>();

	/**
	 * Constructor.
	 * @param second
	 *            Flag to indicate that the alternate mock channel list set
	 *            should be used.
	 * @param experiment
	 *            Name of mock experiment.
	 */
	public ChannelListTestMaps(final boolean second, final String experiment) {
		super();
		this.second = second;
		this.experiment = experiment;
		initTableLists();
		initQueryLists();
	}

	public void fillTblIdr(TableIdentityRegistry tblIdr) {
		for (ChannelListType type : tableTypes) {
			tblIdr.addTable(experiment, getTableName(type), getTt(type),
					RateType.TIME);
			tblIdr.addTable(experiment, getTableName(type), getTt(type),
					RateType.EVENT);
		}
	}

	public void fillCnr(ChannelNameRegistry cnr) {
		ChannelListType[] types = { ChannelListType.OM, ChannelListType.DAQ };
		for (ChannelListType type : types) {
			for (String c : getChannels(type)) {
				cnr.addChannel(getTableName(type), c);
			}
		}
	}

	public QueryChannelLists getChannelLists(ChannelListType typ) {
		return cl2q.get(typ);
	};

	public List<String> getChannels(ChannelListType listType) {
		List<String> result = new ArrayList<String>();
		result.addAll(cl2Channels.get(listType));
		return result;
	}

	/**
	 * @return the experiment
	 */
	public String getExperiment() {
		return experiment;
	}

	/**
	 * @return the queryTypes
	 */
	public List<ChannelListType> getQueryTypes() {
		return queryTypes;
	}

	/**
	 * @return the tableTypes
	 */
	public List<ChannelListType> getTableTypes() {
		return tableTypes;
	}

	public TableType getTt(ChannelListType type) {
		return cl2tt.get(type);
	}

	public String getTableName(ChannelListType type) {
		return type.toString() + "name";
	}

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
		cl2q.put(ChannelListType.QueryOm, qOmCtl);
		cl2Channels.put(ChannelListType.QueryOm, omChnls);

		QueryChannelLists qDaqCtl = new QueryChannelLists(
				MatrixMixType.AddAfter, null, daqChnls, "DAQ");
		cl2q.put(ChannelListType.QueryDaq, qDaqCtl);
		cl2Channels.put(ChannelListType.QueryDaq, daqChnls);

		QueryChannelLists query = new QueryChannelLists(
				MatrixMixType.AddBefore, qOmCtl, daqChnls,
				ChannelListType.QueryBefore.toString());
		cl2q.put(ChannelListType.QueryBefore, query);
		cl2Channels.put(ChannelListType.QueryBefore, query.combine());

		query = new QueryChannelLists(MatrixMixType.AddAfter, qOmCtl,
				daqChnls, ChannelListType.QueryAfter.toString());
		cl2q.put(ChannelListType.QueryAfter, query);
		cl2Channels.put(ChannelListType.QueryAfter, query.combine());

		query = new QueryChannelLists(MatrixMixType.AddMiddle, qOmCtl,
				daqChnls, ChannelListType.QueryMiddle.toString());
		cl2q.put(ChannelListType.QueryMiddle, query);
		cl2Channels.put(ChannelListType.QueryMiddle, query.combine());

		query = new QueryChannelLists(MatrixMixType.AddInterleaved, qOmCtl,
				daqChnls, ChannelListType.QueryMixed.toString());
		cl2q.put(ChannelListType.QueryMixed, query);
		cl2Channels.put(ChannelListType.QueryMixed, query.combine());

		query = new QueryChannelLists(MatrixMixType.AddMiddle, query,
				daqChnls, ChannelListType.QueryTriple.toString());
		cl2q.put(ChannelListType.QueryTriple, query);
		cl2Channels.put(ChannelListType.QueryTriple, query.combine());

		tableTypes.add(ChannelListType.OM);
		tableTypes.add(ChannelListType.DAQ);

		queryTypes.add(ChannelListType.QueryDaq);
		queryTypes.add(ChannelListType.QueryOm);
		queryTypes.add(ChannelListType.QueryAfter);
		queryTypes.add(ChannelListType.QueryBefore);
		queryTypes.add(ChannelListType.QueryMiddle);
		queryTypes.add(ChannelListType.QueryMixed);
	}

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
		cl2Channels.put(ChannelListType.OM, chnls);
		cl2tt.put(ChannelListType.OM, TableType.Control);

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
		cl2Channels.put(ChannelListType.DAQ, chnls);
		cl2tt.put(ChannelListType.DAQ, TableType.DAQ);
	}

	/**
	 * @return the second
	 */
	public boolean isSecond() {
		return second;
	}
}
