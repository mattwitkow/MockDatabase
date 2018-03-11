package driver;
import adt.Response;
import adt.Table;
import adt.Row;
import core.Server;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TSelect implements Driver {
	private static final Pattern pattern;
	static {
		pattern = Pattern.compile(
			"select\\s+((?:\\*\\s+[a-z0-9_-]*)|(?:\\s*(?:(?:[a-z0-9_-]+(?:(?:\\s+as\\s+[a-z0-9_-]+)?)*))(?:(?:\\s*,\\s*)(?:[a-z0-9_-]+(?:(?:\\s+as\\s+[a-z0-9_-]+)?)*))*))\\s*from\\s+([a-z0-9_-]+)".replaceAll("\\s+", " "),
			Pattern.CASE_INSENSITIVE
			
		);
	}
	//create table t (primary string a, boolean b, integer c); insert into t values ("f", true, 3); insert into t values ("j", false, 2); select a as 1, b as 2, c from t
	//create table t (primary string a, boolean b, integer c); insert into t values ("f", true, 3); insert into t values ("j", false, 2); select a,b,c from t
	//create table test_table (primary string ps); select ps as primary from test_table
	//create table t (primary string a, boolean b, integer c); insert into t values ("f", true, 3); insert into t (a,b) values ("j", false); SELECT a AS 1, b as 2, c FROM t
	@SuppressWarnings("unchecked")
	@Override
	public Response execute(Server server, String query) {
		query = query.trim();
		query = query.replaceAll("\\s+", " ");	
		Matcher matcher = pattern.matcher(query);
		if(!matcher.matches())
			return null;
		Table displayedT = new Table();
		String tableName = matcher.group(2).trim();
		//check to see if table in database
		if(!(server.database().containsKey(tableName)))
			return new Response(false, "table attempted to select from is not in database");
		if(query.toLowerCase().equals("select * from "+tableName ))
		{
			Row schema = new Row();
			schema.put("table_name", null);
			schema.put("primary_column_name", server.database().get(tableName).get(null).get("primary_column_name"));
			schema.put("column_types", server.database().get(tableName).get(null).get("column_types"));
			schema.put("column_names", server.database().get(tableName).get(null).get("column_names"));
			displayedT.put(null, schema);
			//displayedT = (Table) server.database().get(tableName).clone();
			Table dT = new Table(displayedT);
			dT.remove(schema.get("table_name"));
			dT.put(null,schema);
			
			return new Response(true, "Source table name: " + tableName + " Rows: " + displayedT.size(), dT);
		}
		//check all col names selected to see if in table && check to make sure that the displayed table won't ever have two columns names be the same
		String entriesArray[] = matcher.group(1).trim().split(",");
		ArrayList<String>cNames = new ArrayList<>();
		cNames.addAll((ArrayList<String>)server.database().get(tableName).get(null).get("column_names"));
		String col;
		String primaryKeyName = (String)server.database().get(tableName).get(null).get("primary_column_name");
		primaryKeyName = primaryKeyName.trim();
		int primaryCount = 0;
		ArrayList<String> displayedC = new ArrayList<String>();
		ArrayList<Object> keys = new ArrayList<Object>();
		ArrayList<String> selectedC = new ArrayList<String>();
		ArrayList<String> types = new ArrayList<String>();
		types.addAll((ArrayList<String>)server.database().get(tableName).get(null).get("column_types"));
		
		for(Object b: server.database().get(tableName).keySet())
		{
			if(b != null)
			keys.add((String) b);
		}
		for(String e: entriesArray)
		{
			e = e.trim();
			if(e.toLowerCase().contains(" as ")){
				col = e.substring(0, e.indexOf(" "));
				selectedC.add(col);
				if(displayedC.contains(e.substring(e.toLowerCase().indexOf("as") + 3)))
					return new Response(false, "same column name would be displayed twice");
				displayedC.add(e.substring(e.toLowerCase().indexOf("as") + 3));
			}
			else{
				col = e;
				if(displayedC.contains(col))
					return new Response(false, "same column name would be displayed twice");
				displayedC.add(col);
				selectedC.add(col);
				}
			if(col.equals(primaryKeyName))
				primaryCount++;
			if(!(cNames.contains(col.trim())))
			{
				
				return new Response(false, "at least one of the columns selected does not exist in the selected table");
			}
		}
		if(primaryCount < 1)
			return new Response(false, "no primary key column selected");
		//find value of rows being inserted from source table
		ArrayList<Row> rowList = new ArrayList<Row>();
		for(Object entry: keys)
		{
			rowList.add(server.database().get(tableName).get((String)entry));
		}
		ArrayList<String> k = new ArrayList<String>();
		k.addAll(selectedC);
		ArrayList <Row> rowL = null;
			Row insertedRow = new Row();
			Set p = server.database().get(tableName).keySet();
			ArrayList<String> primaryKeys = new ArrayList<String>();
			primaryKeys.addAll(p);
			rowL = new ArrayList<Row>();
			ArrayList<String>rowKeys = null;
			for(Row r: rowList)
			{
				Row temp = new Row();
				for(int i = 0; i < displayedC.size(); i++)
				{
					insertedRow.put(displayedC.get(i), r.get(selectedC.get(i)));
					if(r.get(selectedC.get(i)) == null)
							insertedRow.remove(displayedC.get(i), r.get(selectedC.get(i)));
					temp = new Row(insertedRow);
				}
				rowL.add(temp);
			}
			int keysIndex = 1;
			for(Row rows: rowL)
			{
				displayedT.put(primaryKeys.get(keysIndex), rows);
				keysIndex++;
			}
			Row schema = new Row();
			schema.put("table_name", null);
			String unAliased = (String) server.database().get(tableName).get(null).get("primary_column_name");
			int posP;
			for(posP = 0; posP < selectedC.size(); posP++)
			{
				if(selectedC.get(posP).equals(primaryKeyName) )
					break;
			}
			
				schema.put("primary_column_name", displayedC.get(posP));
			
			schema.put("column_names", displayedC );
			ArrayList<String> oldCols = null;
			ArrayList<String> newTypes = new ArrayList<String>();
			oldCols = (ArrayList<String>)server.database().get(tableName).get(null).get("column_names");
			ArrayList<Integer> newP = new ArrayList<Integer>();
			for(int i = 0; i < selectedC.size(); i++)
			{
				for(int j = 0; j < selectedC.size(); j++)
					if(selectedC.get(i).equals(oldCols.get(j)))
					{
						newP.add(j);
						break;
					}
			}
			for(int m = 0; m < selectedC.size(); m++)
			{
				newTypes.add(types.get(newP.get(m)));
			}
			schema.put("column_types", newTypes);
			displayedT.put(null, schema);
		return new Response(true, "Source table name: " + tableName + " Rows: "+ displayedT.size(), displayedT);	
	}
}