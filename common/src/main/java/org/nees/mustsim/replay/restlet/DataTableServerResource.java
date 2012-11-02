package org.nees.mustsim.replay.restlet;

import java.io.InputStream;
import java.util.List;

import org.nees.mustsim.replay.conversions.InputStream2ChannelList;
import org.nees.mustsim.replay.conversions.InputStream2Double;
import org.nees.mustsim.replay.data.DataUpdatesI;
import org.nees.mustsim.replay.data.DoubleMatrix;
import org.nees.mustsim.replay.data.RateType;
import org.nees.mustsim.replay.data.TableType;
import org.nees.mustsim.replay.restlet.DataTableResource;
import org.restlet.resource.Post;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;

import com.google.inject.Inject;

public class DataTableServerResource extends ServerResource implements
		DataTableResource {
	private final DataUpdatesI updates;

	@Override
	@Put
	public void set(String table, InputStream channels) {
		InputStream2ChannelList is2lst = new InputStream2ChannelList(channels);
		List<String> list = is2lst.getChannels();
		TableType tbl = TableType.valueOf(table);
		updates.createTable(tbl, list);

	}
	@Inject
	public DataTableServerResource(DataUpdatesI updates) {
		super();
		this.updates = updates;
	}

	@Override
	@Post
	public void update(String table, String rate, InputStream data) {
		InputStream2Double is2dbl = new InputStream2Double(data);
		List<List<Double>> doubles = is2dbl.getNumbers();
		DoubleMatrix dm = new DoubleMatrix(doubles, doubles.get(0).size());
		TableType tbl = TableType.valueOf(table);
		RateType rt = RateType.valueOf(rate);
		updates.update(tbl, rt, dm.getData());
	}

}
