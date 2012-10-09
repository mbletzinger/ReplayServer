package org.nees.mustsim.replay.db.data;


public class Interpolate {
	private final DoubleMatrix indata;
	public enum ColScanType { Full, Gaps, Late, Early, Empty };

	public Interpolate(DoubleMatrix indata) {
		super();
		this.indata = indata;
	}
	public void fix() {
		int [] sizes = indata.sizes();
		for (int c = 0; c < sizes[1]; c++) {
			ColScanType scan = columnScan(c);
			while(scan.equals(ColScanType.Full) == false && scan.equals(ColScanType.Empty) == false) {
				if(scan.equals(ColScanType.Late) || scan.equals(ColScanType.Early)) {
					extrapolate(scan, c);
					scan = columnScan(c);
					continue;
				}
				int r = 0;
				while(indata.isNull(r, c) == false) {
					r++;
				}
				interpolate(c, r);
				scan = columnScan(c);
			}
		}
		
	}
	public ColScanType columnScan(int column) {
		boolean hasContents = false;
		boolean hasNulls = false;
		int [] sizes = indata.sizes();
		boolean nullStart = indata.isNull(0, column);
		boolean nullEnd = indata.isNull(sizes[0], column);
		for (int r = 0; r < sizes[0]; r++) {
			if(indata.isNull(r, column)) {
				hasNulls = true;
			} else {
				hasContents = true;
			}
		}
		if(hasContents) {
			if(nullStart) {
				return ColScanType.Late;
			}
			
			if(nullEnd) {
				return ColScanType.Early;
			}
			if(hasNulls) {
				return ColScanType.Gaps;
			}
			return ColScanType.Full;
		}
		return ColScanType.Empty;
	}

	public void extrapolate(ColScanType scan, int column) {
		int [] sizes = indata.sizes();
		int start= 0;
		int interval = 1;
		int end = sizes[0];
		if(scan.equals(ColScanType.Early)) {
			start = sizes[0] - 1;
			interval = -1;
			end = 0;
		}
		// find non-zero value
		int row = start;
		while(indata.isNull(row, column) && row != end) {
			row += interval;
		}
		int r = row - 1;
		double value = indata.value(row, column);
		while(r != start) {
			indata.set(r, column, value);
		}
	}
	public void interpolate(int column, int firstNull) {		
		int nonNull = firstNull;
		
		while(indata.isNull(nonNull, column)) {
			nonNull++;
		}
		
		double startVal = indata.value(firstNull - 1, column);
		double stopVal = indata.value(nonNull, column);
		double increment = (stopVal - startVal)/(nonNull - firstNull-1);
		for (int i = firstNull; i < nonNull;i++) {
			indata.set(i, column, (startVal + increment * (i - firstNull + 1)));
		}

	}
	
}
