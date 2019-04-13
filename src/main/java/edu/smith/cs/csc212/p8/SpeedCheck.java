package edu.smith.cs.csc212.p8;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class SpeedCheck {
	
	public static void main(String[] w) {
		List<String> sample = new ArrayList<>();
		sample.add("a");
		sample.add("b");
		sample.add("c");
		sample.add("d");
		sample.add("e");
		
		
		/*
		 * Compare the following: 
		 * 1. HashSet and TreeSet with and without a for loop
		 * 2. Creation speeds for all DS. 
		 */
		
		long startHashSetFillTime = System.nanoTime();
		Set<String> hashSet1 = new HashSet<>(5);
		for(String string : sample) {
			hashSet1.add(string);
		}
		long hashSetFillTime = System.nanoTime() - startHashSetFillTime;
		
		
		
		long startTreeSetFillTime = System.nanoTime();
		Set<String> treeSet1 = new TreeSet<>();
		for(String string : sample) {
			treeSet1.add(string);
		}
		long treeSetFillTime = System.nanoTime() - startTreeSetFillTime;
		
		
		
		// now without for loop (HashSet and TreeSet): 
		long createHashSetStart = System.nanoTime();
		Set<String> hashSet2 = new HashSet<>(sample);
		long createHashSetFillTime = System.nanoTime() - createHashSetStart;
		
		
		
		long createTreeSetStart = System.nanoTime();
		Set<String> treeSet2 = new TreeSet<>(sample);
		long createTreeSetFillTime = System.nanoTime() - createTreeSetStart;
		
		
		
		long startFillSSLS = System.nanoTime();
		Set<String> sortedStrListSet = new SortedStringListSet(sample);
		long fillTimeSSLS = System.nanoTime() - startFillSSLS;
		
		
		
		long startFillCharTrie = System.nanoTime();
		CharTrie charTrie = new CharTrie();
		for(String string: sample) {
			charTrie.insert(string);
		}
		long fillCharTrieTime = System.nanoTime() - startFillCharTrie;
		
		
		
		long startLLHashFill = System.nanoTime();
		LLHash llHash = new LLHash(5);
		for(String string:sample) {
			llHash.add(string);
		}
		long llHashFill = System.nanoTime() - startLLHashFill;
		
		
		
		/*
		 * print time needed to fill each data structure:
		 */
		System.out.println("\n---------------------------------------------------------------------------\n");
		System.out.println("Time needed to: "
				+"\n fill a HashSet: " + hashSetFillTime/1000 +"μs"
				+"\n fill a TreeSet: "+ treeSetFillTime/1000 +"μs"
				+"\n fill a SortedStringListSet: "+fillTimeSSLS/1000 +"μs"
				+"\n fill a CharTrie: "+fillCharTrieTime/1000 +"μs"
				+"\n fill a LLHash: "+llHashFill/1000+"μs");
		System.out.println("\nTime needed to: "
				+"\n construct a HashSet with its input data: "+ createHashSetFillTime/1000+"μs"
				+"\n construct a HashSet by calling add in a for loop: "+ hashSetFillTime/1000+"μs"
				+"\n construct a TreeSet with its input data: "+ createTreeSetFillTime/1000+"μs"
				+"\n construct a TreeSet by calling add in a for loop: "+treeSetFillTime/1000+"μs");
		System.out.println("\n---------------------------------------------------------------------------\n");
		
		
		
		/*
		 * Now determine insertion time/ per element for all DS
		 */
		
		String testString = "xyz";
		List<String> testList = new ArrayList<>();
		testList.add(testString);
		
		
		long insertStartHashSet = System.nanoTime();
		hashSet1.add(testString);
		long insertHashSetTime = System.nanoTime() - insertStartHashSet;
		
		
		long insertStartTreeSet = System.nanoTime();
		treeSet1.add(testString);
		long insertTreeSetTime = System.nanoTime() - insertStartTreeSet;
		
		
		long insertStartCharTrie = System.nanoTime();
		charTrie.insert(testString);
		long insertCharTrieTime = System.nanoTime() - insertStartCharTrie; 
		
		
		long insertStartSSLS = System.nanoTime();
		Set<String> ssls = new SortedStringListSet(testList);
		long insertSSLSTime = System.nanoTime() - insertStartSSLS; 
		
		
		long insertStartLLHash = System.nanoTime();
		llHash.add(testString);
		long insertLLHashTime = System.nanoTime() - insertStartLLHash;
		
		
		
		System.out.println("Insertion time per element for: "
				+"\n\tHashSet: "+insertHashSetTime/1000+"μs"
				+"\n\tTreeSet: "+insertTreeSetTime/1000+"μs"
				+"\n\tCharTrie: "+insertCharTrieTime/1000+"μs"
				+"\n\tLLHash: "+insertLLHashTime/1000+"μ2"
				+"\n\tSortedStringListSet: "+insertSSLSTime/1000+"μs");
		System.out.println("\n---------------------------------------------------------------------------\n");
		
			
		
		
	}//endMain

}//endClass
