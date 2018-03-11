package driver;
import adt.Database;
import adt.Response;
import adt.Table;
import adt.Row;
import core.Server;

import java.util.List;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.util.*;

import java.util.regex.Pattern;

public class TDrop implements Driver {
	
		private static final Pattern pattern;
		static {
			pattern = Pattern.compile(
					"drop table \\w+", 
					Pattern.CASE_INSENSITIVE
			);
		}
		@Override
		public Response execute(Server server, String query) {
			query = query.replaceAll("\\s+", " ");
			query = query.trim();
			Matcher matcher = pattern.matcher(query);
			boolean spacesInName = false;
			if(query.length() > 12)
			{
				spacesInName = query.substring(11).contains(" ");
			}
			if(!matcher.matches())
			{
				return null;
			}
			if(spacesInName)
			{
				return null;
			}
			Table table = new Table();
				String tableName = query.substring(11).trim();
				if( server.database().containsKey(tableName))
				{
					table = server.database().remove(tableName);
					int size = table.size();
					server.database().remove(tableName);
					return new Response(true, "dropping tablename: " + tableName + "number rows: " + size, table );
				}
				else
				{
					return new Response(false, "Table was never created");
				
				}	
		}
}