package core;
import adt.HashMap;
import adt.HashMap.Tuple;
import adt.Row;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
public class Sandbox {
	public static void main(String[] args) {
		Row auto = new Row();
		Row autoEqualityTest = new Row();
		Row testPutAll = new Row();
		testPutAll.put("b", false);
		testPutAll.put("K", true);
		
		Random random = new Random();
		ArrayList<Integer> randomNumbers = new ArrayList<Integer>(); 
		for (int i = 0; i < 16; i++) {
			int temp = random.nextInt(9);
			randomNumbers.add(temp);
		}
		String[] randoms = new String[16];
		for(int j = 0; j < 16; j++)
		{
			randoms[j] = randomString(randomNumbers.get(j));
		}
		
	    
		for(int m = 0; m < 15; m= m+2)
		{
			auto.put(randoms[m], randoms[m+1]);	
			autoEqualityTest.put(randoms[m], randoms[m+1]);	
		}
		int FNV_32_INIT = 0x811c9dc5;
		System.out.println("val" + FNV_32_INIT);
		int FNV_32_PRIME = 0x01000193;
		System.out.println("val" + FNV_32_PRIME);
		
		System.out.println(auto.entrySet());
//		Map a = auto;
//		Map b = autoEqualityTest;
		System.out.println(auto.reSize());
		
		
		System.out.println("size" + auto.size());
		
		System.out.println("success: " + auto.equals(autoEqualityTest));
		auto.remove("");
		System.out.println("success: " + auto.equals(autoEqualityTest));
	//	System.out.println(x);
//		boolean ret = true;
//		for(Object e: a.entrySet())
//		{
//			for(Object be: b.entrySet())
//			{
//				Entry eEntry = (Entry)e;
//				
//				Entry beEntry = (Entry) be;
//				System.out.println(eEntry.getKey() + "  " + eEntry.getKey());
//				System.out.println(eEntry.getValue() + "  " + eEntry.getValue());
//				
//				if(eEntry.getKey().equals(beEntry.getKey()) && (eEntry.getValue().equals(beEntry.getValue()) ))
//				
//					
//					break;
//				else ret = false;
//			}
//		}

	
		
		
//		System.out.println("b as a map" + a.entrySet());
//		//Map b = autoEqualityTest;
//		System.out.println("map approach" + a.equals(b));
//		System.out.println("fudge: " +auto.equals(autoEqualityTest));
//		System.out.println("equality test: " + auto.equalSets(autoEqualityTest));
//		for(Object a: auto.entrySet())
//		{
//			Tuple printed = (Tuple) a;
//			System.out.print("ES:" + printed + " ");
//		}
		
		for (int i = 0; i < 16; i++) {
			int temp = random.nextInt(9);
			randomNumbers.add(temp);
		}
		String[] randoms2 = new String[16];
		for(int j = 0; j < 16; j++)
		{
			randoms2[j] = randomString(randomNumbers.get(j));
		}
//		auto.put("fdasf", true);
//		auto.put(null, false);
//		auto.put("a null val", null);
		for(int m = 0; m < 15; m= m + 2)
		{
//			System.out.println("Key: " + randoms[m] +"  " + "Value: " + randoms[m+1]);
//			
//			System.out.println("Remove: "+ auto.remove(randoms[m])+" ");	
//			System.out.println("get: " +auto.get(randoms[m])+"got here " + m);
//			auto.remove(randoms[m]);
//			System.out.println("Key : " + randoms[m]);
		}
//		System.out.println("getting null key's value: " + auto.get(null));
//		System.out.println("checking null value" + auto.containsValue(null));
//		System.out.println("checking null key" + auto.containsKey(null));
//		for(int m = 0; m < 15; m++)
//		{
//			System.out.println(auto.get(randoms[m]));
//		}
		
//		for(Object a: auto.entrySetNullsIncluded())
//		{
//			Tuple printed = (Tuple) a;
//			System.out.print("ES:" + printed + " ");
//		}
//		System.out.println();
//		for(Object a: autoEqualityTest.entrySetNullsIncluded())
//		{
//			Tuple printed = (Tuple) a;
//			System.out.print("ES:" + printed + " ");
//		}
//		auto.viewStructure();
//		auto.seeAllEntries();
//		auto.putAll(testPutAll);
//		System.out.println("post put all");
//		auto.put(null, "blarg");
//		for(Object a: auto.entrySet())
//		{
//			Tuple printed = (Tuple) a;
//			System.out.print("ES:" + printed + " ");
//		}
//		
//		System.out.println(auto.equals(testPutAll));
//		auto.seeAllEntries();
	}
	static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	static SecureRandom rnd = new SecureRandom();

	static String randomString( int len ){
	   StringBuilder sb = new StringBuilder( len );
	   for( int i = 0; i < len; i++ ) 
	      sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
	   return sb.toString();
	}
	
  
	   }
