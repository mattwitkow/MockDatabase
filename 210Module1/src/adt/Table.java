package adt;

import adt.HashMap;

/** 
 * This class is a hash map alias providing
 * a Primary Key Value -> Row mapping.
 * 
 * Additional non-map features can be implemented.
 * Schema requirements are defined in the project.
 */
public class Table extends HashMap<Object, Row> {
	public Table() {
		super();
	}

	public Table(Table table) {
		super(table);
	}
}