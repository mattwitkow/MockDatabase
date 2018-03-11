package driver;
import adt.Response;
import adt.Table;
import adt.Row;
import core.Server;

import java.util.List;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.util.*;
//insert into [-a-z0-9_]+ \s*\((?:\s*[-a-z0-9_]+\s*(?:,)?[-a-z0-9_]?)+\s*[-a-z0-9_]\)(?:\s*values\s*(?:\((\s*(?:\"[-a-z0-9_]+\"|true|false|\+[0-9]+|null|-[0-9]+|[0-9]+|)+\s*(?:,)?)+)\s*(?:\"[-a-z0-9_]+\"|true|false|\+[0-9]+|null|-[0-9]+|[0-9]+|)?)+\s*\)?
public class TCreate implements Driver {
	private static final Pattern pattern;
	static {
		pattern = Pattern.compile(
			"CREATE\\s+TABLE\\s+([a-z](?:[-a-z0-9_])*)\\s*\\((\\s*(?:(?:Primary)?\\s*(?:String|Boolean|Integer)\\s*[a-z](?:[-a-z0-9_\\s*])*)(?:,?(?:\\s*(?:Primary)?\\s*(?:String|Boolean|Integer)\\s*[a-z](?:[-a-z0-9_\\s*])*))*)\\)",
			Pattern.CASE_INSENSITIVE
		);
	}
	
	String noPrimary;
	int seperation;
	List<String> types;
	String tableName;
	ArrayList<String> tableNames;
	@Override
	public Response execute(Server server, String query) {
		ArrayList<String> columnNames = new ArrayList<String>();
		tableNames = new ArrayList<String>();
		String [] myEntries;
		query = query.trim();
		String trimmedQ;
		Matcher matcher = pattern.matcher(query);
		query = query.replaceAll("\\s+", " ");
		if(matcher.matches())
		{
			int i = 0;
			int whiteCount = 0;
			while( whiteCount < 3)
			{
				if(query.charAt(i) == ' ')
					{
						whiteCount++;
					}
				i++;
			}
			trimmedQ = query.substring(i);
			//tableName = query.substring(query.indexOf(" ", 12), query.indexOf(" ",13));
			tableName = query.substring(13, query.indexOf("("));
			if(server.database().containsKey(tableName.trim()))
			{
				return new Response(false,"Table already in database!" );	
			}
			else 
			{
				
				tableNames.add(tableName);
			}
			String inParentheses = trimmedQ.substring(trimmedQ.indexOf('(')+1, trimmedQ.indexOf(')'));
			myEntries = inParentheses.split(",");
			int primaryCount = 0;
		
			for(String element: myEntries)
			{
				if(element.toLowerCase().contains("primary") && element.toLowerCase().trim().replaceAll("\\s+", " ").startsWith("primary")) 
				{
					primaryCount++;
				}
				if(primaryCount > 1)
				{
					return new Response(false,"Cannot have multiple primary keys" );
				}
			}
			if(primaryCount < 1)
			{
				return new Response(false,"Need to specify primary key" );
			}
			columnNames = new ArrayList<String>();
			types = new ArrayList<String>();
			for(String e : myEntries)
			{
				if(e.toLowerCase().contains("primary"))
				{
					e = e.trim();
					noPrimary = e.substring(8);
					seperation = noPrimary.indexOf(' ') + 1;
					if(!(columnNames.contains(noPrimary.substring(seperation))))
					{
						columnNames.add(noPrimary.substring(seperation));
						types.add(noPrimary.substring(0, noPrimary.indexOf(' ')).toLowerCase());
					}
					else
					{
						return new Response(false,"No duplicate column names please" );
					}
				}
				else
				{
					e = e.trim();
					if(!(columnNames.contains(e)))
					{
						String noType = e.substring(e.indexOf(' '));
						noType = noType + ",";
						noType = noType.trim();
						columnNames.add(noType.substring(0, noType.indexOf(",")));
						types.add(e.substring(0, e.indexOf(" ")).toLowerCase());
					}
					else
					{
						return new Response(false,"No duplicate column names please" );
					}		
				}
			}
		Table table = new Table();
		Row schema = new Row();
		schema.put("table_name", tableName.trim());
		schema.put("column_names", columnNames);
		schema.put("column_types", types);
		schema.put("primary_column_name", noPrimary.substring(seperation));
	
		table.put(null, schema);
		
		for (int j = 0; i < columnNames.size(); j++) { 
			Row row = new Row();
			row.put(columnNames.get(j), null); //???
			table.put(j, row);
		}
		server.database().put(tableName.trim(), table);
		int cols = columnNames.size();
		return new Response(true, "tableName: " + tableName + " tableColumns: " + cols, table);
		}
		else return null;
	}
	
}
	