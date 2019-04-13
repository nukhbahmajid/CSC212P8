package edu.smith.cs.csc212.p8;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class CheckSpelling {
	/**
	 * Read all lines from the UNIX dictionary.
	 * @return a list of words!
	 */
	public static List<String> loadDictionary() {
		long start = System.nanoTime();
		List<String> words;
		try {
			// Read from a file:
			words = Files.readAllLines(new File("src/main/resources/words").toPath());
		} catch (IOException e) {
			throw new RuntimeException("Couldn't find dictionary.", e);
		}
		long end = System.nanoTime();
		double time = (end - start) / 1e9;
		System.out.println("Loaded " + words.size() + " entries in " + time +" seconds.");
		return words;
	}
	
	public static List<String> loadBook() {
		long start = System.nanoTime();
		List<String> wordsList;
		String bookAsString = new String();
		try {	
			// Read from a file:
			wordsList = Files.readAllLines(new File("src/main/resources/dracula.txt").toPath());
		} catch (IOException e) {
			throw new RuntimeException("Couldn't find book.", e);
		}
		
		for (String w : wordsList) {
			bookAsString = bookAsString + w + " ";
		}
		long end = System.nanoTime();
		double time = (end - start) / 1e9;
		System.out.println("Loaded " + wordsList.size() + " entries of the book in " + time +" seconds.");
		return WordSplitter.splitTextToWords(bookAsString);
	}
	
	
	public static List<String> misspelled(List<String> toTest, Collection<String> dictionary) {
		List<String> misspelledWords = new ArrayList<>();
		for(String w : toTest) {
			if(dictionary.contains(w)) {
				continue;
			} else {
				misspelledWords.add(w);
			}
		}
		return misspelledWords;
	} 
	
	/**
	 * This method looks for all the words in a dictionary.
	 * @param words - the "queries"
	 * @param dictionary - the data structure.
	 */
	public static void timeLookup(List<String> words, Collection<String> dictionary) {
		long startLookup = System.nanoTime();
		
		int found = 0;
		for (String w : words) {
			if (dictionary.contains(w)) {
				found++;
			}
		}
		
		long endLookup = System.nanoTime();
		double fractionFound = found / (double) words.size();
		double timeSpentPerItem = (endLookup - startLookup) / ((double) words.size());
		int nsPerItem = (int) timeSpentPerItem;
		System.out.println(dictionary.getClass().getSimpleName()+": Lookup of items found = "+fractionFound+" time = "+nsPerItem+" ns/item");
	}
	
	public static List<String> createMixedDataset(List<String> yesWords, int numSamples, double fractionYes) {
		// Hint to the ArrayList that it will need to grow to numSamples size:
		List<String> output = new ArrayList<>(numSamples);
		// todo: select numSamples * fractionYes words from yesWords; create the rest as no words.
		int hitsRequired = (int) (numSamples * fractionYes);
		// counter to distinguish whether you have add the required number of yes words in output yet or not.
		int counter = 0; 
		while(counter < numSamples) {
			if (counter < hitsRequired) {
				output.add(yesWords.get(counter));
				counter++;
			} else {
				output.add(yesWords.get(counter) + "xyzz");
				counter++;
			}
		}
		return output;
	}
	
	
	public static void main(String[] args) {
		// --- Load the dictionary.
		List<String> listOfWords = loadDictionary();
		
		// --- Create a bunch of data structures for testing:
		TreeSet<String> treeOfWords = new TreeSet<>(listOfWords);
		HashSet<String> hashOfWords = new HashSet<>(listOfWords);
		SortedStringListSet bsl = new SortedStringListSet(listOfWords);
		CharTrie trie = new CharTrie();
		for (String w : listOfWords) {
			trie.insert(w);
		}
		LLHash hm100k = new LLHash(100000);
		for (String w : listOfWords) {
			hm100k.add(w);
		}
		
		// --- Make sure that every word in the dictionary is in the dictionary:
		timeLookup(listOfWords, treeOfWords);
		timeLookup(listOfWords, hashOfWords);
		timeLookup(listOfWords, bsl);
		timeLookup(listOfWords, trie);
		timeLookup(listOfWords, hm100k);
		
		System.out.println();
		for (int i=0; i<11; i++) {
			// --- Create a dataset of mixed hits and misses with p=i/10.0
			List<String> hitsAndMisses = createMixedDataset(listOfWords, 10_000, i/10.0);
			
			System.out.println("For i = "+i+"; in other words the lookup time for words in the dictionary if " +(i*10)+ "% of them are correct" );
			// --- Time the data structures.
			timeLookup(hitsAndMisses, treeOfWords);
			timeLookup(hitsAndMisses, hashOfWords);
			timeLookup(hitsAndMisses, bsl);
			timeLookup(hitsAndMisses, trie);
			timeLookup(hitsAndMisses, hm100k);
			System.out.println("\n");
		}
			

		
		// --- linear list timing:
		// Looking up in a list is so slow, we need to sample:
		System.out.println("Start of list: ");
		timeLookup(listOfWords.subList(0, 1000), listOfWords);
		System.out.println("End of list: ");
		timeLookup(listOfWords.subList(listOfWords.size()-100, listOfWords.size()), listOfWords);
		
	
		// --- print statistics about the data structures:
		System.out.println("Count-Nodes: "+trie.countNodes());
		System.out.println("Count-Items: "+hm100k.size());

		System.out.println("Count-Collisions[100k]: "+hm100k.countCollisions());
		System.out.println("Count-Used-Buckets[100k]: "+hm100k.countUsedBuckets());
		System.out.println("Load-Factor[100k]: "+hm100k.countUsedBuckets() / 100000.0);

		
		System.out.println("log_2 of listOfWords.size(): "+listOfWords.size());
		
		
		System.out.println("\n");
		System.out.println("-------------- Dracula by Bram Stoker ----------------");
		List<String> book = loadBook();
		timeLookup(book,treeOfWords);
		timeLookup(book, hashOfWords);
		timeLookup(book, bsl);
		timeLookup(book, trie);
		timeLookup(book, hm100k);
	
		
		double ratioMisspelled = (double) misspelled(book, hashOfWords).size()/ (double) book.size();
		System.out.println("The number of mis-spelled words is: " + misspelled(book, hashOfWords).size());
		System.out.println("The number of total words in the book is: " + book.size());
		System.out.println("Example of some Mispelled words: ");
		for(int i = 0; i < 11; i++) {
			System.out.println("\n\t["+i+"] "+misspelled(book,hashOfWords).get(i));
		}
		
		System.out.println("\nThe ratio of mispelled words in the book is: " + ratioMisspelled);
		System.out.println("-------------------------------------");
		
		
		System.out.println("Done!");
	}
}
