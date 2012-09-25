package org.nees.mustsim.replay.test.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nees.mustsim.replay.db.table.TableType;

public class ChannelLists {
	private final Map<TableType, List<String>> channels = new HashMap<TableType, List<String>>();
	private final Map<TableType, List<String>> extras = new HashMap<TableType, List<String>>();
	{
		List<String> chnls = new ArrayList<String>();
		chnls.add("OM/Cmd/LBCB1/Actuator/C_LBCB1_X1");
		chnls.add("OM/Disp/LBCB1/Cartesian/D_LBCB1_RY");
		chnls.add("OM/Load/LBCB1/Actuator/L_LBCB2_Z1");
		chnls.add("OM/CntrlSensor/D_West_X");
		channels.put(TableType.OM, chnls);

		chnls = new ArrayList<String>();
		chnls.add("OM/Cmd/LBCB1/Actuator/C_LBCB1_Z1");
		chnls.add("OM/Disp/LBCB1/Cartesian/D_LBCB1_RZ");
		extras.put(TableType.OM, chnls);

		chnls = new ArrayList<String>();
		chnls.add("DAQ/DisplacementSensor/WestFlange/FirstFloor/DTV02F1A_W7_LinPot05");
		chnls.add("DAQ/StrainGauge/Steel/Web/SecondFloor/SGWWF2WL05K_W7_SG_K5");
		chnls.add("DAQ/StrainGauge/Steel/WestFlange/FirstFloor/SGWFF1WL03B_W7_SG_B3");
		channels.put(TableType.DAQ, chnls);

		chnls = new ArrayList<String>();
		chnls.add("DAQ/StrainGauge/Steel/Web/ThirdFloor/SGWWF2WL05K_W7_SG_K12");
		chnls.add("DAQ/StrainGauge/Steel/WestFlange/FirstFloor/SGWFF1WL03B_W7_SG_B23");
		extras.put(TableType.DAQ, chnls);

	};
	
	public List<String> getChannels(TableType table, boolean includeExtras) {
		List<String> result = new ArrayList<String>();
		result.addAll(channels.get(table));
		if(includeExtras) {
			result.addAll(extras.get(table));
		}
		return result;
	}
	
}
