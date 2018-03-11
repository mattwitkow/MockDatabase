package adt;

import java.util.Map;

/** 
 * This is currently just an alias for a built-in
 * implementation of HashMap and it is therefore
 * noncompliant with the Final Module specification.
 * 
 * However, it will temporarily satisfy the needs of all
 * other modules until an original HashMap is implemented.
 */
public class HashMapp<K,V>
	extends java.util.HashMap<K,V>
	implements Map<K,V> 
{
	public HashMapp() {
		super();
	}
	
	public HashMapp(Map<? extends K, ? extends V> copy) {
		super(copy);
	}
}
