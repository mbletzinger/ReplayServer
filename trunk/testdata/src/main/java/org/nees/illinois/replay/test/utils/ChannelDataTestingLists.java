package org.nees.illinois.replay.test.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nees.illinois.replay.data.TableType;

public class ChannelDataTestingLists {
	public enum ChannelListType {
		OM, DAQ, Query1, Query2, OM2, DAQ2, Query3, Query4
	};

	private final Map<ChannelListType, TableType> cl2tt = new HashMap<ChannelDataTestingLists.ChannelListType, TableType>();
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
		List<String> chnls2 = new ArrayList<String>();
		chnls2.addAll(chnls);
		chnls2.add("OM/CntrlSensor/D_West_Z_4");
		chnls2.add("OM/CntrlSensor/D_East_X_1");
		chnls2.add("OM/CntrlSensor/D_North_Y_2");
		channels.put(ChannelListType.OM2, chnls2);
		cl2tt.put(ChannelListType.OM2, TableType.OM);

		chnls = new ArrayList<String>();
		chnls.add("DAQ/DisplacementSensor/WestFlange/FirstFloor/DTV02F1A_W7_LinPot05_0");
		chnls.add("DAQ/StrainGauge/Steel/Web/SecondFloor/SGWWF2WL05K_W7_SG_K5_2");
		chnls.add("DAQ/StrainGauge/Steel/WestFlange/FirstFloor/SGWFF1WL03B_W7_SG_B3_3");
		chnls.add("DAQ/StrainGauge/Steel/Web/ThirdFloor/SGWWF2WL05K_W7_SG_K12_4");
		chnls.add("DAQ/StrainGauge/Steel/WestFlange/FirstFloor/SGWFF1WL03B_W7_SG_B23_5");
		channels.put(ChannelListType.DAQ, chnls);
		cl2tt.put(ChannelListType.DAQ, TableType.DAQ);
		chnls2.clear();
		chnls2.addAll(chnls);
		chnls2.add("DAQ/StrainGauge/Steel/Web/ThirdFloor/SGWWF2WL05K_W7_SG_K12_4");
		chnls2.add("DAQ/StrainGauge/Steel/Web/ThirdFloor/SGWWF2WL06K_W7_SG_K13_2");
		chnls2.add("DAQ/StrainGauge/Steel/Web/ThirdFloor/SGWWF2WL07K_W7_SG_K14_7");
		chnls2.add("DAQ/StrainGauge/Steel/Web/ThirdFloor/SGWWF2WL08K_W7_SG_K18_9");
		channels.put(ChannelListType.DAQ2, chnls2);
		cl2tt.put(ChannelListType.DAQ2, TableType.DAQ);

		chnls = new ArrayList<String>();
		chnls.add("OM/CntrlSensor/D_West_X_3");
		chnls.add("OM/Cmd/LBCB1/Actuator/C_LBCB1_X1_0");
		channels.put(ChannelListType.Query1, chnls);
		chnls2.clear();
		chnls2.addAll(chnls);
		chnls2.add("OM/CntrlSensor/D_West_Z_4");
		channels.put(ChannelListType.Query3, chnls2);

		chnls = new ArrayList<String>();
		chnls.add("OM/CntrlSensor/D_West_X_3");
		chnls.add("OM/Cmd/LBCB1/Actuator/C_LBCB1_X1_0");
		chnls.add("DAQ/StrainGauge/Steel/WestFlange/FirstFloor/SGWFF1WL03B_W7_SG_B3_3");
		channels.put(ChannelListType.Query2, chnls);
		chnls2.clear();
		chnls2.addAll(chnls);
		chnls2.add("DAQ/StrainGauge/Steel/Web/ThirdFloor/SGWWF2WL06K_W7_SG_K13_2");
		chnls2.add("DAQ/StrainGauge/Steel/Web/ThirdFloor/SGWWF2WL07K_W7_SG_K14_7");
		channels.put(ChannelListType.Query4, chnls2);

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
