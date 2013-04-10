/**
 * 
 */
package org.nees.illinois.replay.common.types;

/**
 * Interface to access the full-name of a database table. The naming convention
 * is most likely NEES site-specific and has to follow database naming
 * conventions. This interface takes the guesswork out of this process.
 * 
 * @author Michael Bletzinger
 * 
 */
public interface TableIdentityI {
	/**
	 * 
	 * @return full name of table
	 */
	public String getFullTableName();
}
