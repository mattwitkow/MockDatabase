package driver;

import java.util.Map.Entry;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import adt.Database;
import adt.Response;
import adt.Row;
import adt.Table;
import core.Server;

public class TShow implements Driver {
private static final Pattern pattern;

static{
	pattern = Pattern.compile("SHOW TABLES", Pattern.CASE_INSENSITIVE);
}

@Override
public Response execute(Server server, String query) 
{
	String numTablesResponse = null;
	int num_tables;
	Table table = new Table();
	Matcher matcher = pattern.matcher(query.trim().replaceAll("\\s+", " "));
	if(!matcher.matches())
	{
		return null;
	}	
	num_tables = server.database().size();
	numTablesResponse = "There are: " + num_tables + " tables"; 
	for(Entry<String, Table> entry: server.database().entrySet())
	{
	   
	    Table tbl = entry.getValue();
	    String tbl_name = entry.getKey();
		int column_count = tbl.size()-1;
		Row row = new Row(); 
		row.put("Table Name", tbl_name);
		row.put("Row Count", column_count);
		table.put(tbl_name,row);
	
	}
	return new Response(true, numTablesResponse, table);
}}
