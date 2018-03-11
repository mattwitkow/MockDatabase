package core;
import driver.*;
import adt.Response;
import adt.Row;
import adt.Table;
import adt.Database;
import static java.nio.file.StandardOpenOption.*;
import java.nio.file.*;
import java.io.*;
import java.util.List;
import java.util.Set;
import java.util.Spliterator;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
//create table c (primary integer a, string b, boolean c);insert into c values (34, "dfa", true); insert into c values (3, "d", false); insert into c values (1, "dfadsafkl", true)
//stop
//select * from c
//create table b (primary integer a, string b, boolean c); insert into b values (34, "dfa", true)
//stop
//select * from b
//stop
//create table aaaaa (primary integer a, string b, boolean c); insert into aaaaa values (2,"lkdf", true); insert into aaaaa values (3,"lf", false)
//export aaaaa a
//drop table aaaaa
//select * from aaaaa
//import a
//select * from a
//stop

/** 
 * This class implements a server with an active
 * connection to a backing database.
 * 
 * Finishing implementing the required features
 * but do not modify the protocols.
 */
public class Server implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Database database;
	String fileName;
	File queryLog;
	public Server() throws IOException, ClassNotFoundException {
		
		fileName= "queryLog.txt";
		queryLog = new File(fileName);
		database = new Database();
		FileInputStream fs = new FileInputStream("queryLog.txt");
		BufferedReader b = new BufferedReader(new InputStreamReader(fs));
		
		if(queryLog.length() != 0&& b.readLine().equals("stop"))
			r();
		b.close();

		ArrayList<String> rebuild = new ArrayList<String>();
		BufferedReader br = null;
        String strLine = "";
        try {
            br = new BufferedReader( new FileReader(fileName));
            while( (strLine = br.readLine()) != null){
               rebuild.add(strLine);
            }
            PrintWriter wat = new PrintWriter(queryLog);
			wat.print("");
            wat.close();
        } catch (FileNotFoundException e) {
            System.err.println("Unable to find the file: fileName");
        } catch (IOException e) {
            System.err.println("Unable to read the file: fileName");
        }  
        br.close();
		
		//System.out.println(rebuild.toString());
		b.close();
		rebuild.remove("stop");
		 if(rebuild.size() != 0)
		 {
		for(String e: rebuild)
		  {
		//	System.out.println("interpreted");
			  interpret(e);
		  }
		
		 }
	}
	public Database database() {
		return database;
	}
	public Set getTableNames()
	{
		Set<String> set = database().keySet();
		return set;
	}
	
	public void s() throws IOException
	{
		Set s = getTableNames();
		int nameCount = 0;
		File tNames = new File("tNames.txt");
		FileWriter fw = new FileWriter("tNames.txt",true); 
		
		for(Object name: s)
		{
			String primary = (String) database().get(name).get(null).get("primary_column_name");
			name = (String) name;
			fw.write(name + "\n");
			nameCount++;
			ObjectOutputStream stream = null;
			FileOutputStream file = null;
			
			file = new FileOutputStream((String)name + ".txt");
			stream = new ObjectOutputStream(file);
			String st = "Hello World! ";
		    byte data[] = st.getBytes();
		    Path p = Paths.get("./" + name + ".txt");
		    
		    try (OutputStream out = new BufferedOutputStream(
		      Files.newOutputStream(p, CREATE, APPEND))) {
		      out.write(data, 0, data.length);
		    } catch (IOException x) {
		      System.err.println(x);
		    }
		   int rows = database().get(name).size() - 1;
		   ArrayList<String> schemaCNames = new ArrayList<String>();
		   schemaCNames.addAll((ArrayList<String>)database().get(name).get(null).get("column_names"));
		   stream.writeInt(schemaCNames.size());  //num column
		   stream.writeObject(primary); 	      //p col name
		   stream.writeInt(rows);                 //num rows
		  
		   ArrayList<String> schemaTypes = new ArrayList<String>();
		   schemaTypes.addAll((ArrayList<String>)database().get(name).get(null).get("column_types"));
		    for(Object o: schemaCNames)
		    {
		    	stream.writeObject(o);
		    }
		    for(Object o: schemaTypes)
		    {
		    	stream.writeObject(o);
		    }
		    
		    for(Object o:database().get(name).keySet())
		    {
		    	if(o != null)
		    	{
		    	String primaryKey = (String) o;
		    	Row r = database().get(name).get(primaryKey);
		    	String rowToString = r.toString();
		    	String[]split = rowToString.split("=");
		    	ArrayList<String> splitAList= new ArrayList<String>();		
		    	String[]entries = Arrays.toString(split).split(",");
		    	int i = 0;
		    	for(String ei: entries)
		    	{
		    		
		    		String e = ei;
		    		
		    		e = e.trim();
		    		if(i == 0 )
		    		{
		    			splitAList.add(e.substring(2));
		    		}
		    		else if(i == entries.length - 1 )
		    		{
		    			splitAList.add(e.substring(0,e.indexOf("}")));
		    		}
		    		else splitAList.add(e);
		    		i++;
		    	}
		    	
		    	for(int j = 0; j < splitAList.size()  ; j++)
		    	{
		    		if(j % 2 != 0)
		    		{
		    			stream.writeObject(splitAList.get(j));
		    		}
		    	}
		    	}
		    }   	
		    stream.close();
		}	
		fw.close();
	}
	public void r() throws ClassNotFoundException, IOException
	{
		ArrayList<String> s  = new ArrayList<String>();
		s.add("a");
		FileInputStream fstream = new FileInputStream("tNames.txt");
		BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
		String strLine;
		ArrayList<String> tn = new ArrayList<String>();
		while ((strLine = br.readLine()) != null)   
		{
					  tn.add(strLine.trim());
		}
		for(String n: tn)
		{
		
		String name = (String) n;
		Table t = new Table();
		ObjectInputStream stream = null;
		FileInputStream file = null;
		ArrayList<String> colNames = new ArrayList<String>();
	    ArrayList<String> colValues = new ArrayList<String>();
	    ArrayList<String> colTypes = new ArrayList<String>();
	    
		//create table a (primary integer a, string b, boolean c);insert into a values (34, "dfa", true);
		//insert into a values (33, "dfa", true)
		
		try {
			//System.out.println("2");
			file = new FileInputStream(name + ".txt");
		    stream = new ObjectInputStream(file);
			int size = stream.readInt();
			String primary = (String) stream.readObject();
			int rows = stream.readInt();
			
		//	System.out.printf("%18s",primary);
		//	System.out.println();
			for(int i = 0; i < size ; i++)
			{
				Object put = stream.readObject();
			//	System.out.printf("%18s",put);
				colNames.add((String) put);
			}
			int dexPrimaryCol = 0;
			for(int i = 0; i < size; i++)
			{
				if(colNames.get(i).equals(primary))
					dexPrimaryCol = i;
			}
		//	System.out.println();
			for(int i = size; i < size * 2 ; i++)
			{
				Object put = stream.readObject();
		//		System.out.printf("%18s",put);
				colTypes.add((String) put);
			}
	//		System.out.println();
			for (int j = 0; j < rows; j++)
			{
				Row insertedRow = new Row();
				String primaryColVal = null;
			for(int i = 0; i < size; i++)
			{
				Object put = stream.readObject();
		//		System.out.printf("%18s", put);
				insertedRow.put(colNames.get(i), put);
				if(i == dexPrimaryCol)
					primaryColVal = (String)put;
			}
			t.put(primaryColVal, insertedRow);
		//	System.out.println();
			}
			stream.close();
			file.close();
			Row schema = new Row();
			schema.put("table_name", name);
			schema.put("column_names", colNames);
			schema.put("column_types", colTypes);
			schema.put("primary_column_name", primary);
			t.put(null, schema);
			database().put(name.trim(), t);
	//		System.out.println("reached further");
			//System.out.println("test:" +database().containsKey(name.trim()));
			
		} catch (IOException i) {
			i.printStackTrace();
		}
		br.close();
		File f = new File("tNames.txt");
		PrintWriter writer = new PrintWriter(f);
		writer.print("");
		writer.close();
		}
		
	}
	public List<Response> interpret(String script) throws UnsupportedEncodingException, FileNotFoundException, IOException {
		getTableNames();
		ArrayList queries = new ArrayList<String>();
		String query = script;
		String [] myScript = script.split(";");
		List<Response> responses = new ArrayList<Response>();
		List<Driver> driverList = new ArrayList<Driver>();
		Driver CREATETABLE = new TCreate();
		driverList.add(CREATETABLE);
		Driver DROPTABLE = new TDrop();
		driverList.add(DROPTABLE);
		Driver SHOWTABLE = new TShow();
		driverList.add(SHOWTABLE);
		Driver INSERTINTOTABLE = new TInsert();
		driverList.add(INSERTINTOTABLE);
		Driver SELECTTABLE = new TSelect();
		driverList.add(SELECTTABLE);
		Driver EXPORTTABLE = new TExport();
		driverList.add(EXPORTTABLE);
		Driver IMPORTTABLE = new TImport();
		driverList.add(IMPORTTABLE);
		
		for (String element: myScript)
		{
		
			Response createResponse = CREATETABLE.execute(this, query);
			Response dropResponse = DROPTABLE.execute(this, query);
			Response showResponse = SHOWTABLE.execute(this, query);
			Response insertResponse = INSERTINTOTABLE.execute(this, query);
			Response selectResponse = SELECTTABLE.execute(this, query);
			Response exportResponse = EXPORTTABLE.execute(this, query);
			Response importResponse = IMPORTTABLE.execute(this,query);
			
		
		if(createResponse != null)
		{
			responses.add(createResponse);
			try
			{
			   
			    FileWriter fw = new FileWriter(fileName,true); 
			    fw.write(element.trim() + "\n");
			    fw.close();
			}
			catch(IOException ioe)
			{
			    System.err.println("IOException: " + ioe.getMessage());
			}
		}
		
		else if(dropResponse != null)
		{
			responses.add(dropResponse);
			try
			{
			    FileWriter fw = new FileWriter(fileName,true); //the true will append the new data
			    fw.write(element.trim() + "\n");//appends the string to the file
			    fw.close();
			}
			catch(IOException ioe)
			{
			    System.err.println("IOException: " + ioe.getMessage());
			}
		}
		
		else if(showResponse != null)
		{
			responses.add(showResponse);
		}
		else if(insertResponse!= null)
		{
			responses.add(insertResponse);
			try
			{
			   
			    FileWriter fw = new FileWriter(fileName,true); //the true will append the new data
			    fw.write(element.trim() + "\n");//appends the string to the file
			    fw.close();
			}
			catch(IOException ioe)
			{
			    System.err.println("IOException: " + ioe.getMessage());
			}
		}
		else if(selectResponse!= null)
		{
			responses.add(selectResponse);
		}
		else if(exportResponse!= null)
		{
			responses.add(exportResponse);
		}
		else if(importResponse!= null)
		{
			responses.add(importResponse);
		}
		
		else 
		{
			responses.add( new Response( false,"Unrecognized syntax. None of the drivers were triggered") );
		}
		}
		
		return responses;
	}

}
