
package core;

import adt.Response;
import adt.Row;

import java.util.List;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/** 
 * This class implements an interactive console
 * for a database server.
 * 
 * Finishing implementing the required features
 * but do not modify the protocols.
 */
//M4 tablename, truncation for both, distinguishable types, empty cells = null, correct column order according to schema, aliases are correct, indicator of primary, "pretty"
//create table t (string abcdefgldanflksadflska, primary integer b, boolean c)
//insert into t values ("ddlkfdlkajfsdlkfjsdfdska", 800, false)
//select abcdefgldanflksadflska as aliased, b, c from t
public class Console {
	public static void main(String[] args) throws UnsupportedEncodingException, FileNotFoundException, IOException, ClassNotFoundException {
		prompt(new Server(), System.in, System.out);
	}
	
	public static void prompt (Server server, InputStream input, OutputStream output) throws UnsupportedEncodingException, FileNotFoundException, IOException, ClassNotFoundException {
		
		Scanner in = new Scanner(input);
		PrintStream out = new PrintStream(output);
		
		/*
		 * TODO: Use a REPL to allow the user type in a
		 * single query script or a semicolon-delimited
		 * script of multiple queries and pass the entire
		 * script to the server to be interpreted. It is
		 * also permissible to pre-split the script and
		 * pass each individual query in sequence.
		 * 
		 * TODO: The console must terminate when the
		 * sentinel word STOP is encountered, which is
		 * not a query to be passed to the server.
		 */
		
		boolean end = false;
		while(!end)
		{
		out.print(">> ");
		String line = in.nextLine();
		
		if(line.toLowerCase().equals("stop"))
		{
			end = true;
			File file = new File("queryLog.txt");
			PrintWriter writer = new PrintWriter(file);
			writer.println("stop");
			writer.close();
			server.s();
		}
		else{
		for (int i = 0; i < line.split(";").length; i++)
		{
		List<Response> responses = server.interpret(line.split(";")[i]);
		
//		@SuppressWarnings("unchecked")
//		ArrayList<String> schemaC = (ArrayList<String>) responses.get(0).table().get(null).get("column_names");
		out.println("Success: " + responses.get(0).success());
		out.println("Message: " + responses.get(0).message());
//		@SuppressWarnings("unchecked")
//		
//	
//		ArrayList<String> keys = new ArrayList();
//		ArrayList<Row> rowList = new ArrayList<Row>();
//		for(Object b: responses.get(0).table().keySet())
//		{
//			if(b != null)
//			keys.add((String) b);
//		}
//		
//		for(Object entry: keys)
//		{
//			rowList.add(responses.get(0).table().get((String)entry));
//		}
//	
//		System.out.println(responses.get(0).table().get(null).get("table_name"));
//		for(int p = 0; p < (schemaC.size()+1) * 22 - 5; p++)
//		{
//			System.out.print("-");
//		}
//		System.out.println();
//		if(rowList.size() ==0 )
//		{
//			for(int h = 0; h < schemaC.size(); h++)
//			{
//				if(schemaC.get(h).equals(responses.get(0).table().get(null).get("primary_column_name")))
//				{
//					if(schemaC.get(h).length() > 8 )
//					System.out.printf("%22s", schemaC.get(h).substring(0, 8) + "..."+"*");
//					else
//						System.out.printf("%22s", schemaC.get(h) + "*");
//				}
//				else
//				{
//					if(schemaC.get(h).length() > 8 )
//					System.out.printf("%22s", schemaC.get(h).substring(0, 8) + "...");
//					else
//						System.out.printf("%22s", schemaC.get(h));		
//				}
//			}
//			System.out.println();
//			for(int p = 0; p < (schemaC.size()+1) * 22 - 5; p++)
//			{
//				System.out.print("-");
//			}
//			System.out.println();
//		}
//		
//		for(int k = 0; k < rowList.size(); k ++)
//		{
//			for(int j = 0; j < schemaC.size();j++)
//			{
//				if(j == 0 & k == 0)
//				{
//					for(int q = 0; q < schemaC.size(); q++)
//					{
//						if(schemaC.get(q).equals(responses.get(0).table().get(null).get("primary_column_name")))
//						{
//							
//							if(schemaC.get(q).length() < 8)
//							System.out.printf("%22s",schemaC.get(q)+ "*" );
//							else
//								System.out.printf("%22s", schemaC.get(q).substring(0,8)+"..."+ "*" );
//						}	
//						else{
//							
//							if(schemaC.get(q).length() < 8)
//								System.out.printf("%22s",schemaC.get(q));
//								else
//									System.out.printf("%22s",schemaC.get(q).substring(0,8)+"..."+ "*");
//						}
//					}
//					
//					System.out.println();
//					for(int p = 0; p < (schemaC.size()+1) * 22 - 5; p++)
//					{
//						System.out.print("-");
//					}
//					System.out.println();
//				}
//				
//				
//				if(rowList.get(k).get(schemaC.get(j)).equals(null))
//					System.out.printf("%22s","");
//				else if( rowList.get(k).get(schemaC.get(j)) != null && (rowList.get(k).get(schemaC.get(j)).equals(true) ))
//					System.out.printf("%22s","true");
//				else if(rowList.get(k).get(schemaC.get(j)) != null && (rowList.get(k).get(schemaC.get(j)).equals(false) ))
//					System.out.printf("%22s","false");
//				else if(rowList.get(k).get(schemaC.get(j)) != null && rowList.get(k).get(schemaC.get(j)) instanceof Integer)
////					if(!((Integer)rowList.get(k).get(schemaC.get(j)) > 10000000) ||(Integer)rowList.get(k).get(schemaC.get(j)) < -10000000 )
//						System.out.printf("%22s",rowList.get(k).get(schemaC.get(j)));
////					else
////					{
////						String num = "" + rowList.get(k).get(schemaC.get(j));
////						System.out.printf("%22s",num.substring(0,8) + "...");
////					}
//				
//				else 
//				{
//	
//				String printed = (String)rowList.get(k).get(schemaC.get(j));
//				if(printed.length() < 8)
//				System.out.printf("%22s","\""+rowList.get(k).get(schemaC.get(j))+ "\"");
//				else
//				{
//					printed = (String)rowList.get(k).get(schemaC.get(j));
//					System.out.printf("%22s","\""+printed.substring(0, 8) + "...\"");
//				}
//				}		
//			}
//			System.out.println();
//			for(int p = 0; p < (schemaC.size()+1) * 22 - 5; p++)
//			{
//				System.out.print("-");
//			}
//			System.out.println();
//		}
		
		
	 out.println("Table:   " + responses.get(0).table());
		}
		}
		// TODO: Tables must eventually support tabular view.
	}
}

}
