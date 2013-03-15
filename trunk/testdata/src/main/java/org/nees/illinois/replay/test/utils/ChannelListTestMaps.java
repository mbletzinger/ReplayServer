package org.nees.illinois.replay.test.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nees.illinois.replay.data.TableType;

public class ChannelListTestMaps {
	private final Map<ChannelListType, List<String>> cl2Channels = new HashMap<ChannelListType, List<String>>();
	private final Map<ChannelListType, ChannelTestingList> cl2q = new HashMap<ChannelListType, ChannelTestingList>();

	private final Map<ChannelListType, TableType> cl2tt = new HashMap<ChannelListType, TableType>();
	private final String experiment;
	private final List<ChannelListType> queryTypes = new ArrayList<ChannelListType>();

	private final boolean second;
	private final List<ChannelListType> tableTypes = new ArrayList<ChannelListType>();

	public ChannelListTestMaps(boolean second, String experiment) {
		super();
		this.second = second;
		this.experiment = experiment;
		initialize();
	}
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
	};

	public ChannelTestingList getChannelLists(ChannelListType typ) {
		return cl2q.get(typ);
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
	};

	public TableType getTt(ChannelListType type) {
		return cl2tt.get(type);
	}

	private void initialize() {
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
		cl2tt.put(ChannelListType.OM, TableType.OM);

		chnls = new ArrayList<String>();
		chnls.add("DAQ/DisplacementSensor/WestFlange/FirstFloor/DTV02F1A_W7_LinPot05_0");
		chnls.add("DAQ/StrainGauge/Steel/Web/SecondFloor/SGWWF2WL05K_W7_SG_K5_2");
		chnls.add("DAQ/StrainGauge/Steel/WestFlange/FirstFloor/SGWFF1WL03B_W7_SG_B3_3");
		chnls.add("DAQ/StrainGauge/Steel/Web/ThirdFloor/SGWWF2WL05K_W7_SG_K12_4");
		chnls.add("DAQ/StrainGauge/Steel/WestFlange/FirstFloor/SGWFF1WL03B_W7_SG_B23_5");
		if (second) {
			chnls.add("DAQ/StrainGauge/Steel/Web/ThirdFloor/SGWWF2WL05K_W7_SG_K12_4");
			chnls.add("DAQ/StrainGauge/Steel/Web/ThirdFloor/SGWWF2WL06K_W7_SG_K13_2");
			chnls.add("DAQ/StrainGauge/Steel/Web/ThirdFloor/SGWWF2WL07K_W7_SG_K14_7");
			chnls.add("DAQ/StrainGauge/Steel/Web/ThirdFloor/SGWWF2WL08K_W7_SG_K18_9");
		}
		cl2Channels.put(ChannelListType.DAQ, chnls);
		cl2tt.put(ChannelListType.DAQ, TableType.DAQ);

		
		
		
		
		List<String> omChnls = new ArrayList<String>();
		omChnls.add("OM/CntrlSensor/D_West_X_3");
		omChnls.add("OM/Cmd/LBCB1/Actuator/C_LBCB1_X1_0");
		omChnls.add("OM/CntrlSensor/D_West_Z_4");
		omChnls.add("OM/Disp/LBCB1/Cartesian/D_LBCB1_RZ_5");
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
			daqChnls.add("DAQ/StrainGauge/Steel/Web/ThirdFloor/SGWWF2WL07K_W7_SG_K14_7");
			daqChnls.add("DAQ/StrainGauge/Steel/Web/ThirdFloor/SGWWF2WL08K_W7_SG_K18_9");
		}
		ChannelTestingList qctl = new ChannelTestingList(MatrixMixType.AddAfter, null, omChnls, "OM");

		ChannelTestingList query = new ChannelTestingList(MatrixMixType.AddBefore, qctl, daqChnls, ChannelListType.QueryBefore.toString());
		cl2q.put(ChannelListType.QueryBefore, query);
		cl2Channels.put(ChannelListType.QueryBefore, query.combine());

		query = new ChannelTestingList(MatrixMixType.AddAfter, qctl, daqChnls, ChannelListType.QueryAfter.toString());
		cl2q.put(ChannelListType.QueryAfter, query);
		cl2Channels.put(ChannelListType.QueryAfter, query.combine());

		query = new ChannelTestingList(MatrixMixType.AddMiddle, qctl, daqChnls, ChannelListType.QueryMiddle.toString());
		cl2q.put(ChannelListType.QueryMiddle, query);
		cl2Channels.put(ChannelListType.QueryMiddle, query.combine());

		query = new ChannelTestingList(MatrixMixType.AddInterleaved, qctl, daqChnls, ChannelListType.QueryMixed.toString());
		cl2q.put(ChannelListType.QueryMixed, query);
		cl2Channels.put(ChannelListType.QueryMixed, query.combine());
		
		query = new ChannelTestingList(MatrixMixType.AddMiddle, query, daqChnls, ChannelListType.QueryTriple.toString());
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
	/**
	 * @return the second
	 */
	public boolean isSecond() {
		return second;
	}
}
