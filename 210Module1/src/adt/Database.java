package adt;

import adt.HashMap;

/** 
 * This class is a hash map alias providing
 * a Table Name -> Table Object mapping.
 * 
 * Additional non-map features can be implemented.
 * Schema requirements are defined in the project.
 */
public class Database extends HashMap<String, Table> {
	public Database() {
		super();
	}

	public Database(Database database) {
		super(database);
	}
}