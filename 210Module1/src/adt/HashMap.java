package adt;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
public class HashMap<K,V> implements Map<K,V> {
	
	private final int initialSize = 1003;
	private int numElements = 0;
	private int hashSize;
	
	SinglyLinkedList<Tuple<K,V>>[] listArray;
	@SuppressWarnings("unchecked")
	public HashMap() {
		super();
		 listArray =  new SinglyLinkedList[initialSize];
		 hashSize = listArray.length;
		 
	}
	public HashMap(Map<? extends K,? extends V> map) {
		this();
		this.putAll(map);
	}
	public class SinglyLinkedList<T> implements Iterable<T>
	{
	   private Node head;
	   private int size;
	   public SinglyLinkedList()
	   {
	      head = null;
	   }
	
	   public boolean isEmpty()
	   {
	      return head == null;
	   }
	
	   public T getFirst()
	   {
	      if(head == null) throw new NoSuchElementException();

	      return head.data;
	   }
	   public void addFirst(T item)
	   {
	      head = new Node(item, head);
	     
	   }
	   public T removeFirst()
	   {
	      T temp = getFirst();
	      head = head.next;
	      return temp;
	   }

	   public void addLast(T item)
	   {
	      if( head == null)
	         addFirst(item);
	      else
	      {
	         Node temp = head;
	         while(temp.next != null) temp = temp.next;

	         temp.next = new Node(item, null);
	      }
	   }
	 
	   public T getLast()
	   {
	      if(head == null) throw new NoSuchElementException();

	      Node temp = head;
	      while(temp.next != null) temp = temp.next;

	      return temp.data;
	   }
	
	   public void clear()
	   {
	      head = null;
	   }
	
	   public boolean contains(T a)
	   {
	      for(T temp : this)
	         if(temp.equals(a)) return true;

	      return false;
	   }
	
	   public T get(int dex)
	   {
	      if (head == null) throw new IndexOutOfBoundsException();

	      Node temp = head;
	      for (int i = 0; i < dex; i++) 
	    	  temp = temp.next;

	      if( temp == null) 
	    	  throw new IndexOutOfBoundsException();

	      return temp.data;
	   }
	  
	   public void remove(T key)
	   {
	      if(head == null)
	         throw new RuntimeException("cannot delete");

	      if( head.data.equals(key) )
	      {
	         head = head.next;
	         return;
	      }

	      Node current  = head;
	      Node previous = null;

	      while(current != null && !current.data.equals(key) )
	      {
	         previous = current;
	         current = current.next;
	      }

	      if(current == null)
	         throw new RuntimeException("cannot delete");

	      //delete cur node
	      previous.next = current.next;
	   }
	
	   private  class Node
	   {
	      private T data;
	      private Node next;

	      public Node(T data, Node next)
	      {
	         this.data = data;
	         this.next = next;
	      }
	   }
	   public Iterator<T> iterator()
	   {
	      return new LinkedListIterator<T>();
	   }

	   @SuppressWarnings("hiding")
	private class LinkedListIterator<T>  implements Iterator<T>
	   {
	      private Node nextNode;

	      public LinkedListIterator()
	      {
	         nextNode = head;
	      }

	      public boolean hasNext()
	      {
	         return nextNode != null;
	      }
	      @SuppressWarnings("unchecked")
		public T next()
	      {
	         if (!hasNext()) throw new NoSuchElementException();
	         T res = (T) nextNode.data;
	         nextNode = nextNode.next;
	         return res;
	      }

	      public void remove() { throw new UnsupportedOperationException(); }
	   }
	}
	public class Tuple<k,v> implements Map.Entry<K, V> {
		  private K key;
		  private V value;

		  public Tuple(K key, V value) {
		    this.key = key;
		    this.value = value;
		  }
		  @SuppressWarnings("unchecked")
		public Tuple(@SuppressWarnings("rawtypes") Tuple copied)
		  {
			  this.key = (K) copied.key;
			  this.value = (V) copied.value;
		  }
		  public K getKey() 
		  { 
			  return key; 
		  }
		  public V getValue() 
		  { 
			  return value; 
		  }
		  public boolean checkNullVal()
		  {
			  return (value == null);
		  }
		  public String toString()
		  {
			  return key + "=" + value;
		  }
		@Override
		public V setValue(V value) {
	        V old = this.value;
	        this.value = value;
	        return old;
	    }
	}
	
    public  int myHash(String k) {
        
    	int initial = -2128831035;
        final int len = k.length();
        for(int i = 0; i < len; i++) {
            initial ^= k.charAt(i);
            initial *= 16777619;
        }
        if(initial < 0)
        	return -initial % hashSize;
        return initial % hashSize;
    }
	
	@Override
	public V put(K key, V value) {
		
		Tuple<K,V> t = new Tuple<K,V>(key, value);
		int hashValue = 0;
		if (key instanceof String)
			hashValue = myHash((String)key);
		if(key == null){}
		else
			hashValue = Math.abs(key.hashCode() % hashSize);
		
		if(listArray[hashValue] == null || listArray[hashValue].size == 0)
		{
			SinglyLinkedList<Tuple<K,V>> l = new SinglyLinkedList<Tuple<K,V>>();
			if(t != null)
			l.addFirst(t);
			l.size++;
			listArray[hashValue] = l;
			numElements++;
			return null;
		}
		
		else
		{
			SinglyLinkedList<Tuple<K,V>> l = listArray[hashValue];
			
			Iterator<HashMap<K, V>.Tuple<K, V>> itr = l.iterator();
			boolean stillLooking = true;
		    Tuple<K,V> current = null;
		    while(stillLooking && itr.hasNext())
		{
			current = (HashMap<K, V>.Tuple<K, V>) itr.next();
			if(current != null && current.getKey() == null)
			{
				stillLooking = false;
				
			}
			else  if(current != null  && current.getKey().equals(key))
			{
				stillLooking = false;
				
			}
		}
		if(stillLooking)
		{
			l.addFirst(new Tuple<K,V>(key, value));
			l.size++;
			listArray[hashValue] = l;
			numElements++;
			if(numElements / listArray.length > 5)
				listArray = reSize();
			return null;
		}
		else
		{
			V retVal = (V) current.getValue();
			l.remove(current);
			l.addFirst(new Tuple<K,V>(key, value));
			listArray[hashValue] = l;
			return retVal;
		}
			
			
		}
	}

	
	@Override
	public V remove(Object key)
	{	boolean nullKey = false;
		if(!(key instanceof String))
			nullKey = true;
		int hashValue = 0;
		if (key instanceof String)
			hashValue = myHash((String)key);
		if(key == null)hashValue = 0;
		else
			hashValue = Math.abs(key.hashCode() % hashSize);
		
			boolean stillLooking = true;
			if(listArray[hashValue] == null || listArray[hashValue].size < 1 )
				return null;
			SinglyLinkedList<Tuple<K,V>> l = listArray[hashValue];
			if(l == null || l.size <= 0)
				return null;
			
		Tuple<K,V> ret = null;
		Tuple<K,V> current = null;
		Iterator<HashMap<K, V>.Tuple<K, V>> itr = l.iterator();
		if(itr.hasNext() == false)
			return null;
		while(itr.hasNext() )
		{
			current = (Tuple<K,V>)itr.next();
			if(nullKey)
			{
				if(current !=null && current.getKey() == null && nullKey)
				{
				
					ret = new Tuple<K,V>(current);
					l.remove(current);
					listArray[hashValue] = l;
					stillLooking = false;
					break;
				}
			}
			if(current !=null && current.getKey() != null && current.getKey().equals(key))
			{
				ret = new Tuple<K,V>(current);
				l.remove(current);
				//System.out.print("test: " + l.toString());
				listArray[hashValue] = l;
				stillLooking = false;
				break;
			}
		}
		if(stillLooking)
			return null;
		
		numElements--;
		l.size--;
		return (V) ret.getValue();
		
	}
	@SuppressWarnings("null")
	@Override
	public V get(Object key)
	{
		
		int hashValue = 0;
		if (key instanceof String)
			hashValue = myHash((String)key);
		if(key == null){}
		else
			hashValue = Math.abs(key.hashCode() % hashSize);
		boolean stillLooking = true;
		if(listArray[hashValue] == null || listArray[hashValue].size < 1 )
			return null;
		SinglyLinkedList<Tuple<K,V>> l = listArray[hashValue];
		if(l == null || l.size == 0 )
		{
			return null;
		}
	Tuple<K,V> ret = null;
	Iterator<HashMap<K, V>.Tuple<K, V>> itr = l.iterator();
	while(stillLooking && itr.hasNext())
	{
		Tuple<K,V> current = (HashMap<K, V>.Tuple<K, V>) itr.next();
		if (current == null) return null;
		if (current.getKey() == null && key == null){
			ret = new Tuple<K,V>(current);
		return (V) ret.getValue();
		}
		else if( current.getKey() != null && current.getKey().equals(key))
		{
			ret = new Tuple<K,V>(current);
			return (V) ret.getValue();
		}
	}
	if(stillLooking)
		return null;
	return (V) ret.getValue();
	}
		

	
	@Override
	public void clear() {
		for(SinglyLinkedList<Tuple<K,V>>d  : listArray)
		{		
			if (d == null || d.size == 0)
				continue;
			else
			{
				d.clear();
				
				}
			d.size = 0;
		}
		numElements = 0;
	}
	
	
	@Override
	public boolean containsKey(Object key) {
		
		int hashValue = 0;
		if (key instanceof String)
			hashValue = myHash((String)key);
		if(key == null){}
		else
			hashValue = Math.abs(key.hashCode() % hashSize);
		boolean stillLooking = true;
		if(listArray[hashValue] == null || listArray[hashValue].size < 1 )
			return false;
		SinglyLinkedList<Tuple<K,V>> l = listArray[hashValue];
		if(l.size == 0)
			return false;
		Iterator<HashMap<K, V>.Tuple<K, V>> itr = l.iterator();
	while(stillLooking && itr.hasNext())
	{
		Tuple<K,V> current = (HashMap<K, V>.Tuple<K, V>) itr.next();
		if(current.getKey() == null && key == null)
			return true;
		if(current != null && current.getKey().equals(key))
		{
			return true;
		}
	}
	return false;
		
	}
	
	@Override
	public boolean containsValue(Object value) {
		for(SinglyLinkedList<Tuple<K,V>>d  : listArray)
		{		
			if (d == null || d.size == 0)
				continue;
			else
			{
				Iterator<HashMap<K, V>.Tuple<K, V>> itr = d.iterator();
				while(itr.hasNext())
				{
							Tuple<K,V> current = (Tuple<K,V>) itr.next();
							
							if(current != null && current.checkNullVal() == true && value == null)
								return true;
							
							if(current != null &&  current.checkNullVal() == false && current.getValue().equals(value))
								return true;
						
				}
			}

		}
		return false;
	}
	

	public Set<java.util.Map.Entry<K, V>> entrySetNullsIncluded() {
		Set<java.util.Map.Entry<K, V>> entries = new HashSet<java.util.Map.Entry<K, V>>();
		for(SinglyLinkedList<Tuple<K,V>>d  : listArray)
		{		
			if (d == null || d.size == 0)
				continue;
			else
			{
				Iterator<HashMap<K, V>.Tuple<K, V>> itr = d.iterator();
				while(itr.hasNext())
				{
							
								Tuple<K,V> current = (Tuple<K,V>) itr.next();
								
								java.util.Map.Entry<K, V> e = current;
								if(current != null)
								entries.add(e);
				}
							}
								
					}
		return entries;
	}
	public Set<java.util.Map.Entry<K, V>> entrySet() {
		Set<java.util.Map.Entry<K, V>> entries = new HashSet<java.util.Map.Entry<K, V>>();
		for(SinglyLinkedList<Tuple<K,V>>d  : listArray)
		{		
			if (d == null || d.size == 0)
				continue;
			else
			{
				Iterator<HashMap<K, V>.Tuple<K, V>> itr = d.iterator();
				while(itr.hasNext())
				{
							
								Tuple<K,V> current = (Tuple<K,V>) itr.next();
								
								java.util.Map.Entry<K, V> e = current;
								if(e != null && e.getKey() != null )
								entries.add(e);
								
				}
							}
								
					}
		return entries;
	}
		
	
	
	@Override
	public boolean isEmpty() {

		return (numElements == 0);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Set<K> keySet() {
		Set<K> keys = new HashSet<K>();

		for(SinglyLinkedList<Tuple<K,V>>d  : listArray)
		{		
			if (d == null || d.size == 0)
				continue;
			else
			{
				Iterator<HashMap<K, V>.Tuple<K, V>> itr = d.iterator();
				while(itr.hasNext())
				{
							Tuple<K,V> current = (Tuple<K,V>) itr.next();
							if(current != null && current.getKey() != null )
							{
								Object e = current.getKey();
								keys.add((K)e);
								
							}
							
					}
				}

		}
		return keys;
		
	}
	

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		
		
		
		for(java.util.Map.Entry<? extends K, ? extends V>  e: m.entrySet())
		{
			
			K key =  e.getKey();
			V value = e.getValue();
			this.put(key, value);
		}

	}
	
	@Override
	public int size() {
		return numElements;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Collection<V> values() {
		Collection<V> values = new HashSet<V>();

		for(SinglyLinkedList<Tuple<K,V>>d  : listArray)
		{		
			if (d == null || d.size == 0)
				continue;
			else
			{
				Iterator<HashMap<K, V>.Tuple<K, V>> itr = d.iterator();
				for(int j = 0; j < d.size; j++)
				{
							Tuple<K,V> current = (Tuple<K,V>) itr.next();
							if(current != null )
							{
								Object e = current.getValue();
								values.add((V)e);
							}
					}
				}

		}
		return values;
	}
	@SuppressWarnings("unchecked")
	public boolean equals(Object m) 
	{
		if(!(m instanceof Map))
		return false;
		if(((Map<K,V>) m).size() != this.numElements)
			return false;
		Set<Object> first = new HashSet<Object>();
		ArrayList<Object> second = new ArrayList<Object>();
		for(Object e: (this).entrySet())
		{
			Entry<K,V> eEntry = (Entry<K,V>)e;
			first.add( eEntry.getKey());
			second.add( eEntry.getValue());
		}
		Set <Object> third = new HashSet<Object>();
		ArrayList<Object> fourth = new ArrayList<Object>();
		for(Object e:  ((Map<K,V>) m).entrySet())
		{
			Entry<K,V> eEntry = (Entry<K,V>)e;
			third.add( eEntry.getKey());
			fourth.add( eEntry.getValue());
		}
		return first.equals(third) && second.containsAll(fourth);
	}
	
	
	public void viewStructure()
	{
		ArrayList<String> viewAll = new ArrayList<String>();
		String entries = null;
		for(SinglyLinkedList<Tuple<K,V>>d  : listArray)
		{		
			if (d == null ){
				viewAll.add(entries);
			}
			else
			{
					Iterator<HashMap<K, V>.Tuple<K, V>> itr = d.iterator();
				
				while(itr.hasNext())
					{
						
							Tuple<K,V> current = (Tuple<K,V>) itr.next();
							entries += current.toString();
						
					}	
				}
			
			viewAll.add(entries);
			entries = "";
				}
		System.out.println(viewAll);
	}
	public SinglyLinkedList<Tuple<K,V>>[] reSize()
	{
		{
			
			@SuppressWarnings("unchecked")
			SinglyLinkedList<Tuple<K,V>>[] ret = (SinglyLinkedList<Tuple<K,V>>[])new SinglyLinkedList[listArray.length * 3];
//			SinglyLinkedList<Tuple<K,V>>[] ret;
//			if(ret instanceof SinglyLinkedList<T>);
			for(int i = 0; i < listArray.length; i++)
			{
				if (listArray[i] == null || listArray[i].size == 0)
					continue;
				else
				{
					ret[i] = listArray[i];
				}			
		}
			return ret;
	}
	}
}