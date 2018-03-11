package driver;
import adt.Response;
import adt.Table;
import adt.Row;
import core.Server;
import java.util.List;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
//insert into [-a-z0-9_]+ \\s*\\((?:\\s*[-a-z0-9_]+\\s*(?:,)?[-a-z0-9_]?)+\\s*[-a-z0-9_]?\\)(?:\\s*values\\s*(?:\\((\\s*(?:\"[-a-z0-9_\ !+]+\"|true|false|\\+[0-9]+|null|-[0-9]+|[0-9]+|)+\\s*(?:,)?)+)\\s*(?:\"[-a-z0-9_!+ ]+\"|true|false|\\+[0-9]+|null|-[0-9]+|[0-9]+|)?)+\\s*\\)?
public class TInsert implements Driver {
	ArrayList <String> primaryColVals;
	private static final Pattern pattern;
	static {
		pattern = Pattern.compile(
			"insert into [-a-z0-9_]+ \\s*(?:\\((?:\\s*[-a-z0-9_]+\\s*(?:,)?[-a-z0-9_]?)+\\s*[-a-z0-9_]?\\))?(?:\\s*values\\s*(?:\\((\\s*(?:\"[-a-z0-9_ +!]*\"|true|false|\\+[0-9]+|null|-[0-9]+|[0-9]+|)+\\s*(?:,)?)+)\\s*(?:\"[-a-z0-9_ +!]*\"|true|false|\\+[0-9]+|null|-[0-9]+|[0-9]+|)?)+\\s*\\)?",
			Pattern.CASE_INSENSITIVE
		);
	}
	//create table test_table (primary String ps, boolean b, integer i); INSERT INTO test_table (ps, b, i) VALUES ("a3", false, 4)
	//create table test_table (primary String ps, integer i, boolean b); INSERT INTO test_table (ps, i, b) VALUES ("g3", 4, true); INSERT INTO test_table (ps, i, b) VALUES ("g3", 5, false)
	
	
	@SuppressWarnings("unchecked")
	
	public Response execute(Server server, String query) {
		primaryColVals = new ArrayList<String>();
		query = query.replaceAll("\\s+", " ");	
		query = query.trim();
		Matcher matcher = pattern.matcher(query);
		
		
		if(!matcher.matches())
			return null;
		if(!(matcher.group().equals(query)))//suspect
			return new Response( false, "unrecognized syntax");
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
		String trimmedQ = query.substring(i).trim();
	
		int count = query.length() - query.replace("(", "").length();
		int secondPPos = query.indexOf("(", query.indexOf("(") + 1) + 1;
		String values = query.substring(secondPPos, query.length()-1);
		String[] valueEntries = values.split(",");
		
		if(count > 1)
		{
			String tableName = query.substring(12, query.indexOf("("));
			tableName = tableName.trim();
			if(!(server.database().containsKey(tableName)))
				return new Response(false, "table not in database");
		String primaryCol = (String)server.database().get(tableName).get(null).get("primary_column_name");
		List<String> columnNames = new ArrayList<String>();
		String[] myEntries;
		String inParentheses = trimmedQ.substring(trimmedQ.indexOf('(')+1, trimmedQ.indexOf(')'));
		myEntries = inParentheses.split(",");

		for(String entry: myEntries)
		{
			if(columnNames.contains(entry.trim()))
				return new Response(false, "duplicate column names");
				
			columnNames.add(entry.trim());	
		}
		if(server.database().get(tableName) != null)
		{					
				int primaryCount = 0;

				ArrayList<String>cNames = new ArrayList<>();
				cNames.addAll((ArrayList<String>)server.database().get(tableName).get(null).get("column_names"));
				for(int h = 0; h < columnNames.size(); h++)
				{
					if(primaryCol.trim().equals(columnNames.get(h).trim()))
						primaryCount++;
					if(!(cNames.contains(columnNames.get(h))))
					{
						return new Response(false, "trying to insert into a row that does not exist");
					}
					
				}
				if(primaryCount != 1)
				{
					return new Response(false, "incorrect number primaries");
				}
				
				
				 secondPPos = query.indexOf("(", query.indexOf("(") + 1) + 1;
				 values = query.substring(secondPPos, query.length()-1);
				 valueEntries = values.split(",");
				if(valueEntries.length != myEntries.length)
					return new Response(false, "there is not a 1 to 1 ratio of column names to values");
				
				
				Row insertedRow = new Row();
				Table displayedTable = new Table();
				boolean numeric;
				
				ArrayList<String>typesSchema = new ArrayList<>();
				typesSchema.addAll((ArrayList<String>)server.database().get(tableName).get(null).get("column_types"));
				primaryCol = (String)server.database().get(tableName).get(null).get("primary_column_name");
				
				int q;
				for( q = 0; q < valueEntries.length; q++)
				{
					
					if(columnNames.get(q).equals(primaryCol))
						break;
				}
				
				String primaryColVal = valueEntries[q].replace("\"", "").trim();
				
				
				for(int n = 0; n < columnNames.size(); n++)
					{
						for(int m = 0; m < cNames.size(); m++)
						{
							
						if(cNames.get(m).equals(columnNames.get(n)))
						{
							String type = typesSchema.get(m).trim();
							int d = 0;
							try  
							  {  
							    d = Integer.parseInt(valueEntries[n].trim());  
							    numeric = true;
							  }  
				
							catch(NumberFormatException nfe)  
							  {  
							     numeric = false;  
							  }  
//							System.out.println((primaryColVals.contains(valueEntries[n].replace("\"", "").trim())));
//							System.out.println(primaryColVals.toString());
//							
//							if((primaryColVals.contains(valueEntries[n].replace("\"", "").trim())))
//							{
//								return new Response(false, "multiple primary key cannot have the same vals");
//							}
							if(valueEntries[n].trim().startsWith("\"") && valueEntries[n].trim().endsWith("\"") && type.equals("string") )
							{
								insertedRow.put(columnNames.get(n), valueEntries[n].replace("\"", "").trim());
							}
							else if(valueEntries[n].trim().toLowerCase().equals("true") && type.equals("boolean")&& !(primaryColVals.contains(valueEntries[n].trim())))
							{
								insertedRow.put(columnNames.get(n), true);
							}
							else if(valueEntries[n].trim().toLowerCase().equals("false")  && type.equals("boolean")&& !(primaryColVals.contains(valueEntries[n].trim())))
							{
								insertedRow.put(columnNames.get(n), false);
							}
							
							else if(numeric && type.equals("integer") && !((valueEntries[n].trim().startsWith("0") &&valueEntries[n].trim().length() > 1))&& !(primaryColVals.contains(valueEntries[n].trim())))
							{
								insertedRow.put(columnNames.get(n), d);
							}
							else if(valueEntries[n].trim().toLowerCase().equals("null") && (!cNames.get(n).trim().equals(primaryCol.trim())))
							{
							
							}
							else
								return new Response(false, "mismatched input types to columns");
						}
						
					}
				}
				
				primaryColVals.add(primaryColVal.replace("\"", "").trim());
				//System.out.println(primaryColVals.toString());
				displayedTable.put(primaryColVal, insertedRow);
				Row schemaRow = server.database().get(tableName).get(null);
				Row newSchemaRow = new Row(schemaRow);
				newSchemaRow.remove("table_name");
				newSchemaRow.put("table_name", null);
				displayedTable.put(null, newSchemaRow);
				if(server.database().get(tableName).containsKey(primaryColVal))
					return new Response(false, "primary key cannot have the same val");
				server.database().get(tableName).put(primaryColVal, insertedRow);
				server.database().get(tableName).get(null).put("table_name", tableName);
				return new Response(true, "Destination: " + tableName + " 1 row inserted",displayedTable);
		}
		else{
			return new Response(false, "table not in database");
		}
		}//end of if(count > 1)
		else{
			int i2 = 0;
			int whiteCount2 = 0;
			while( whiteCount2 < 3)
			{
				if(query.charAt(i2) == ' ')
					{
						whiteCount2++;
					}
				i2++;
			}
			String tableName = query.substring(12, i2).trim();
			tableName = tableName.trim();
			if(!(server.database().containsKey(tableName)))
				return new Response(false, "table not in database");
			Row insertedRow = new Row();
			String inParentheses = trimmedQ.substring(trimmedQ.indexOf('(')+1, trimmedQ.indexOf(')')).trim();
			String[] vals = inParentheses.split(",");
			ArrayList<String> measure = (ArrayList<String>)server.database().get(tableName).get(null).get("column_types");
			
			//create table test_table (primary string a, integer b, boolean c, boolean d); INSERT INTO test_table VALUES ("h3", 4, false, null)
			
			if(measure.size() != vals.length )
			{
				return new Response(false, "mismatch between number of cols and vals");
			}
			ArrayList<String> oldCols = new ArrayList<>();
			oldCols.addAll((ArrayList<String>)server.database().get(tableName).get(null).get("column_names"));
			String primaryCol = (String)server.database().get(tableName).get(null).get("primary_column_name");
			int q;
			for( q = 0; q < vals.length; q++)
			{
				if(oldCols.get(q).equals(primaryCol))
					break;
			}
			String primaryColVal = vals[q].replace("\"", "").trim();
			if(primaryColVal.equals("null"))
				return new Response(false, "Cant store null vals in primary keys");
			ArrayList<String>typesSchema = new ArrayList<>();
			typesSchema.addAll((ArrayList<String>)server.database().get(tableName).get(null).get("column_types"));
			boolean numeric;
			for(int m = 0; m < vals.length; m++)
			{
				int d = 0;
				if(vals[m] != null)
				{
					try  
					  {  
					    d = Integer.parseInt(vals[m].trim());  
					    numeric = true;
					  }  
					  catch(NumberFormatException nfe)  
					  {  
					     numeric = false;  
					  }  
					if(numeric)
					{
						if(!(typesSchema.get(m).equals("integer")))
							return new Response(false, "mismatch between column types and values");
						insertedRow.put(oldCols.get(m), d);		
					}
					if(vals[m].trim().startsWith("\"") && vals[m].trim().endsWith("\"") )
					{
						if(!(typesSchema.get(m).equals("string")))
							return new Response(false, "mismatch between column types and values");
						insertedRow.put(oldCols.get(m), vals[m].trim().replaceAll("\"", ""));//need better check in case string of string
					}
					if(vals[m].trim().toLowerCase().equals("true")  )
					{
						if(!(typesSchema.get(m).equals("boolean")))
							return new Response(false, "mismatch between column types and values");
						
						insertedRow.put(oldCols.get(m), true);
					}
					if(vals[m].trim().toLowerCase().equals("false")  )
					{
						if(!(typesSchema.get(m).equals("boolean")))
							return new Response(false, "mismatch between column types and values");
						
						insertedRow.put(oldCols.get(m), false);
					}		
				}
			}
			Table displayedTable = new Table();
			primaryCol = (String)server.database().get(tableName).get(null).get("primary_column_name");
			displayedTable.put(primaryColVal, insertedRow);
			Row schemaRow = server.database().get(tableName).get(null);
			Row newSchemaRow = new Row(schemaRow);
			newSchemaRow.remove("table_name");
			newSchemaRow.put("table_name", null);
			//change ^ to null
			displayedTable.put(null, newSchemaRow);
		
			if(server.database().get(tableName).containsKey(primaryColVal))
				return new Response(false, "primary key cannot have the same val");
			server.database().get(tableName).put(primaryColVal, insertedRow);
		//	System.out.println(displayedTable);
			return new Response(true, "Destination: " + tableName + "1 row inserted", displayedTable);
		}
	}
}