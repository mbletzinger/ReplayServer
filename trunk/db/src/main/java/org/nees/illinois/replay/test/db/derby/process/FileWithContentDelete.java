package org.nees.illinois.replay.test.db.derby.process;

import java.io.File;
import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Extension of the Java File class that allows you to delete a directory that
 * is not empty.
 * @author Michael Bletzinger
 */
public class FileWithContentDelete extends File {

	/**
	 * required by eclipse.
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Logger.
	 */
	private final Logger log = LoggerFactory
			.getLogger(FileWithContentDelete.class);

	/**
	 * Constructor.
	 * @param parent
	 *            directory.
	 * @param child
	 *            name of subdirectory.
	 */
	public FileWithContentDelete(final File parent,final String child) {
		super(parent, child);
	}

	/**
	 * Constructor.
	 * @param pathname
	 *            to the directory.
	 */
	public FileWithContentDelete(final String pathname) {
		super(pathname);
	}

	/**
	 * Constructor.
	 * @param parent
	 *            directory.
	 * @param child
	 *            name of the subdirectory.
	 */
	public FileWithContentDelete(final String parent,final String child) {
		super(parent, child);
	}

	/**
	 * Constructor.
	 * @param uri
	 *            to the directory.
	 */
	public FileWithContentDelete(final URI uri) {
		super(uri);
	}

	/*
	 * (non-Javadoc)
	 * @see java.io.File#delete()
	 */
	@Override
	public final boolean delete() {
		log.debug("deleting " + getAbsolutePath());
		if (isFile()) {
			return super.delete();
		}
		if (list() == null) {
			return super.delete();
		}
		for (String f : list()) {
			FileWithContentDelete sub = new FileWithContentDelete(
					getAbsoluteFile(), f);
			sub.delete();
		}
		return super.delete();
	}

}
