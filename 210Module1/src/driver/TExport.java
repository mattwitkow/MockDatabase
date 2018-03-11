package driver;
import adt.Response;
import adt.Row;
import core.Server;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonWriter;


public class TExport implements Driver {
	private static final Pattern pattern;
	static {
		pattern = Pattern.compile(
			"EXPORT\\s+([a-z][a-z0-9_!]*)\\s+([a-z][a-z0-9_!]*)".replaceAll("\\s+", " "),
			Pattern.CASE_INSENSITIVE
		);
	}

	@Override
	public Response execute(Server server, String query) {
		
		Matcher matcher = pattern.matcher(query.trim());
		if(!matcher.matches())
			return null;
		String targetTable = matcher.group(1);
		String targetFile = matcher.group(2) + ".json";
		
		//create table aaaaa (primary integer a, string b, boolean c); insert into aaaaa values (2,"lkdf", true); insert into aaaaa values (3,"lf", false)
		//export aaaaa a
		
		try {
			OutputStream out = new FileOutputStream(targetFile);
		    JsonWriter writer = Json.createWriter(out);
		    JsonObjectBuilder objbuilder = Json.createObjectBuilder();
//		    JsonArrayBuilder arrbuilder = Json.createArrayBuilder();
		    ArrayList<String> list = new ArrayList<String>();
		    String schema = null;
		    for(Object r: server.database().get(targetTable).keySet())
		    {
		    	if (r == null)
		    	{
		    		Row s = new Row();
		    		s = server.database().get(targetTable).get(null);
		    		schema = s.toString();
		    	}
		    	if(r != null)
		    	{
		    		list.add((String)r);
		    	}
		    }
		   
		    Collections.reverse(list);
		    Collection<Row> rowSet = server.database().get(targetTable).values();
		    ArrayList <Row> rowList = new ArrayList<Row>();
		    for(Row r: rowSet)
		    {
		    	rowList.add(r);
		    }
		   
		    for(String o: list)
		    {
		    	int i = 0;
		    	JsonArrayBuilder arrbuilder = Json.createArrayBuilder();
		    	for(int j = 0; j < rowList.size(); j++)
		    	{
		    		if(i > 1) break;
		    		if(!rowList.get(j).toString().contains(("primary_column_name")))
		    		{
		    			arrbuilder.add(rowList.get(j).toString());
		    		}	rowList.remove(j);
		    	}
		    	
		    	if(o != null)
		    	objbuilder.add(o, arrbuilder.build());
		    	objbuilder.add("null", schema);
		    }
		 
		    
		    	
		
//		    String a =(String) server.database().get(targetTable).get(null).get("primary_column_name");
//		    System.out.println(a);
		    
//		    for (List<String> sublist: list) {
//		    	JsonArrayBuilder arrbuilder = Json.createArrayBuilder();
//		    	for (String value: sublist) {
//		    		arrbuilder.add((String) value);
//		    	}
//		    	objbuilder.add((String) sublist.get(0), arrbuilder.build());
//		    }
//		    
//		    for(Object r: server.database().get(targetTable).keySet())
//		{
//			String rowName = (String)r;
//			if(r != null){
//				Row row = server.database().get(targetTable).get(rowName);
//				String rowString = row.toString();
//				arrbuilder.add(rowString);
//			}
//		}
		
		
//		objbuilder.add(targetTable, arrbuilder.build());
		writer.writeObject(objbuilder.build());
		writer.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return new Response(true, "Table exported");
	}
}