package driver;
import adt.Response;
import adt.Row;
import adt.Table;
import core.Server;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonStructure;
import javax.json.JsonWriter;

public class TImport implements Driver {
	private static final Pattern pattern;
	static {
		pattern = Pattern.compile(
			"IMPORT\\s+([a-z][a-z0-9_!]*)".replaceAll("\\s+", " "),
			Pattern.CASE_INSENSITIVE
		);
	}
	public static boolean isNumeric(String str)  
	{  
	  try  
	  {  
	    double d = Double.parseDouble(str);  
	  }  
	  catch(NumberFormatException nfe)  
	  {  
	    return false;  
	  }  
	  return true;  
	}
	@Override
	public Response execute(Server server, String query) {
		Matcher matcher = pattern.matcher(query.trim());
		if(!matcher.matches())
			return null;
		String fileData = null;
		
		String targetFile = matcher.group(1).trim()+ ".json";
		Table t = new Table();
		String tableName = matcher.group(1).trim();
		if(server.database().containsKey(tableName))
			return new Response(false, "table already in db");
		try {
			FileInputStream fstream = new FileInputStream(targetFile);
			BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
			InputStream in = new FileInputStream(targetFile);
		    JsonReader reader = Json.createReader(in);
		    JsonObjectBuilder objbuilder = Json.createObjectBuilder();
		    Row schema = new Row();
		    schema.put("table_name", tableName);
		    fileData = br.readLine();
		    String step1Cols = fileData.substring(fileData.indexOf("["), fileData.indexOf("]"));
		    String step2Cols = step1Cols.substring(step1Cols.indexOf("{") + 1, step1Cols.indexOf("}"));
		    String step3Cols[] = step2Cols.split(",");
		    ArrayList<String> colNames = new ArrayList<String>();
		    ArrayList<String> colValues = new ArrayList<String>();
		    
		    
		    for(int i = 0; i < step3Cols.length;i++)//only does first rows values
		    {
		    	int equals = step3Cols[i].indexOf("=");
		    	colNames.add(step3Cols[i].substring(0, equals));
		    	colValues.add(step3Cols[i].substring(equals+1).trim());
		    }
		    ArrayList<String> colTypes = new ArrayList<String>();
		    for(int i = 0; i < colNames.size(); i++)
		    {
		    	if(colValues.get(i).toLowerCase().equals("true") ||colValues.get(i).toLowerCase().equals("false"))
		    	{
		    		colTypes.add("boolean");
		    	}
		    	else if(isNumeric(colValues.get(i)))
		    	{
		    		colTypes.add("integer");
		    	}
		    	else{
		    		colTypes.add("string");
		    	}		
		    }
		    String primaryColumnName = colNames.get(0);
		    String primaryColValue = colValues.get(0);
		    schema.put("column_names", colNames);
		    schema.put("column_types", colTypes);
		    schema.put("primary_column_name", primaryColumnName);
		    
		    Row insertedRow = new Row();
		    for(int i = 0 ; i < colNames.size(); i++)
		    {
		    	insertedRow.put(colNames.get(i), colValues.get(i));
		    }
		   
		    t.put(null, schema);
		    t.put(primaryColValue, insertedRow);
		    server.database().put(tableName.trim(), t);
		    	   
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		return new Response(true, fileData);
	}
}
	