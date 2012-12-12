package org.nees.illinois.replay.test.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nees.illinois.replay.data.TableType;


public class ChannelLists {
	public enum ChannelListType { OM, DAQ, Query1, Query2 };
	private final Map<ChannelListType,TableType> cl2tt = new HashMap<ChannelLists.ChannelListType, TableType>();
	private final Map<ChannelListType, List<String>> channels = new HashMap<ChannelListType, List<String>>();
	{
		List<String> chnls = new ArrayList<String>();
		chnls.add("OM/Cmd/LBCB1/Actuator/C_LBCB1_X1_0");
		chnls.add("OM/Disp/LBCB1/Cartesian/D_LBCB1_RY_1");
		chnls.add("OM/Load/LBCB1/Actuator/L_LBCB2_Z1_2");
		chnls.add("OM/CntrlSensor/D_West_X_3");
		chnls.add("OM/Cmd/LBCB1/Actuator/C_LBCB1_Z1_4");
		chnls.add("OM/Disp/LBCB1/Cartesian/D_LBCB1_RZ_5");
		channels.put(ChannelListType.OM, chnls);
		cl2tt.put(ChannelListType.OM, TableType.OM);


		chnls = new ArrayList<String>();
		chnls.add("DAQ/DisplacementSensor/WestFlange/FirstFloor/DTV02F1A_W7_LinPot05_0");
		chnls.add("DAQ/StrainGauge/Steel/Web/SecondFloor/SGWWF2WL05K_W7_SG_K5_2");
		chnls.add("DAQ/StrainGauge/Steel/WestFlange/FirstFloor/SGWFF1WL03B_W7_SG_B3_3");
		chnls.add("DAQ/StrainGauge/Steel/Web/ThirdFloor/SGWWF2WL05K_W7_SG_K12_4");
		chnls.add("DAQ/StrainGauge/Steel/WestFlange/FirstFloor/SGWFF1WL03B_W7_SG_B23_5");
		channels.put(ChannelListType.DAQ, chnls);
		cl2tt.put(ChannelListType.DAQ, TableType.DAQ);

		chnls = new ArrayList<String>();
		chnls.add("OM/CntrlSensor/D_West_X_3");
		chnls.add("OM/Cmd/LBCB1/Actuator/C_LBCB1_X1_0");
		channels.put(ChannelListType.Query1, chnls);

		chnls = new ArrayList<String>();
		chnls.add("OM/CntrlSensor/D_West_X_3");
		chnls.add("OM/Cmd/LBCB1/Actuator/C_LBCB1_X1_0");
		chnls.add("DAQ/StrainGauge/Steel/WestFlange/FirstFloor/SGWFF1WL03B_W7_SG_B3_3");
		channels.put(ChannelListType.Query2, chnls);

	};
	
	public List<String> getChannels(ChannelListType listType) {
		List<String> result = new ArrayList<String>();
		result.addAll(channels.get(listType));
		return result;
	}
	public TableType getTt(ChannelListType type) {
		return cl2tt.get(type);
	}
}
