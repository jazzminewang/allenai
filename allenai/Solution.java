package allenai;
import java.io.*;
import java.util.*;

public class Solution {
	//global variable for hashmap
	//key (file name) -> hashmap<key is word, frequency>
	static HashMap<String, HashMap<String, Integer>> filesWithTheirWords = new HashMap<String, HashMap<String, Integer>>();
	static HashMap<String, Integer> wordsWithTFScores = new HashMap<>();
	
	public static void readFile(String fileName) throws IOException {
		BufferedReader sampleFile = new BufferedReader(new FileReader (fileName));

		String nextLine = sampleFile.readLine();
		
		while (nextLine != null) {
			String[] cleanWords = cleanUp(nextLine);
			buildWordFrequencies(fileName, cleanWords);
		}
	}
	
	private static String[] cleanUp(String word) {
		//strip punctuation, whitespace, and convert to lower case
		String[] words = word.replaceAll("[^a-zA-Z ]", "").toLowerCase().split("\\s+");
		return words;
	}
	
	private static void buildWordFrequencies(String fileName, String[] line) {
		//initialize hashmap if it file is not presented
		HashMap<String, Integer> wordCounter;
		
		if (!filesWithTheirWords.containsKey(fileName)) {
			wordCounter = new HashMap<>();
		} else {
			wordCounter = filesWithTheirWords.get(fileName);
		}
		//add words to the counter
		for (String word: line) {
			if (!wordCounter.containsKey(word)) {
				//add the word
				wordCounter.put(word, 1);
			} else {
				wordCounter.put(word, wordCounter.get(word) + 1);
			}
		}
	}
	
	public static void setTFScores() {
		//calculate TFScores from counts
		for (String file : filesWithTheirWords.keySet()) {
			int total = 0;
			//iterate through entire hashmap to get number
			for (String word : wordsWithTFScores.keySet()) {
				total += wordsWithTFScores.get(word);
			}
			//iterate again through hashmap, resetting scores to TFSScore
			for (String word : wordsWithTFScores.keySet()) {
				int wordCount = wordsWithTFScores.get(word);
				int score = wordCount/total;
				wordsWithTFScores.put(word, score);
			}
		}
	}
	
	/*
	 *  
	static HashMap<String, HashMap<String, Integer>> filesWithTheirWords = new HashMap<String, HashMap<String, Integer>>();
	static HashMap<String, Integer> wordsWithTFScores = new HashMap<>();
	 */
	
	public static String getHighestDocument(String word) {
		String highestDocument = "";
		int highestScore = Integer.MIN_VALUE;
		
		for (String file : filesWithTheirWords.keySet()) {
			if (wordsWithTFScores.containsKey(word)) {
				//compare against current value
				int currCount = wordsWithTFScores.get(word);
				highestScore = Math.max(highestScore, currCount);
				if (highestScore == currCount) {
					highestDocument = file;
				}
			}
		}
		return highestDocument;
	}
		
	public static int getHighestScore(String word) {
		String fileName = getHighestDocument(word);
		int highest = filesWithTheirWords.get(fileName).get(word);
		return highest;
	}
	
	public static void main(String[] args) throws IOException {
		//construct hashmaps
		Scanner readInput = new Scanner(System.in);
		
		System.out.print("Enter valid file name, or -1 to quit: "); 
		String file = readInput.nextLine();
		while (file.equals("mobydick-chapter1.txt") || file.equals("mobydick-chapter2.txt") || file.equals("mobydick-chapter3.txt") || file.equals("mobydick-chapter4.txt") || file.equals("mobydick-chapter5.txt")) {
			readFile(file);
			//reinitialize
			System.out.print("Enter file name: "); 
			file = readInput.nextLine();
		}
		
		setTFScores();
		
		//pass in the words we want standard scores for
		System.out.println("Document w highest score: " + getHighestDocument("queequeg") + " TF score for queequeg: " + getHighestScore("queequeg"));
		System.out.println("Document w highest score: " + getHighestDocument("whale") + " TF score for whale: " + getHighestScore("whale"));
		System.out.println("Document w highest score: " + getHighestDocument("sea") + " TF score for sea: " + getHighestScore("sea"));		
		
		//prompt users for words to check for
		System.out.print("Enter word, or enter -1 to quit: "); 
		String word = readInput.nextLine();
		while (word != "-1" && word != "") {
			//find 
			System.out.println("Document with highest score: " + getHighestDocument(word) + " TF score for " + word + " : " + getHighestScore(word));
			//reinitialize
			System.out.print("Enter word, or enter -1 to quit: "); 
			word = readInput.nextLine();
		}		
	}
	
}
