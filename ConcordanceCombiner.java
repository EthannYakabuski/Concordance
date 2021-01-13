import java.io.File;
import java.io.FileNotFoundException; 
import java.util.Scanner; 
import java.util.Collection;
import java.util.HashMap;
import java.util.ArrayList; 
import java.util.Comparator; 
import java.util.Collections; 
import java.io.FileWriter; 
import java.io.IOException; 


import java.util.regex.Pattern; 


import static com.tutego.jrtf.Rtf.rtf;
import static com.tutego.jrtf.RtfHeader.font;
import static com.tutego.jrtf.RtfPara.*;
import static com.tutego.jrtf.RtfText.*;
import static com.tutego.jrtf.RtfUnit.CM;
import java.awt.Desktop;
import java.io.*;
import java.util.Date;
import com.tutego.jrtf.*;

import java.util.concurrent.TimeUnit; 




class LocationTagExtended {
	 
	String word; 
	String paragraphCollection; 
	String filename;
	
	LocationTagExtended(String w, String pC, String fn) {
		word = w; 
		paragraphCollection = pC; 
		filename = fn; 
		
	}
	
	String getWord() {
		return word; 
	}
	String getParagraphCollectionString() {
		return paragraphCollection; 
	}
	String getFilename() {
		return filename; 
	}
	
	
	public static Comparator<LocationTagExtended> WordComparator = new Comparator<LocationTagExtended>() {
		
		public int compare(LocationTagExtended l1, LocationTagExtended l2) {
			
			String l1Word = l1.getWord();
			String l2Word = l2.getWord();
			
			
			return l1Word.compareTo(l2Word);
		
		
		
    }};
	
}
	

class ConcordanceCombiner {
	
	static int paragraph = 0; 
	static boolean incrementThisRound = false; 
    static ArrayList<LocationTagExtended> locationTags = new ArrayList<LocationTagExtended>(); 
	static ArrayList<String> finalLines = new ArrayList<String>();
	static ArrayList<File> filesGiven = new ArrayList<File>(); 
	static ArrayList<String> wordsAlreadyUsed = new ArrayList<String>();

public static boolean isNumeric(String str) { 
  try {  
    Integer.parseInt(str);  
    return true;
  } catch(NumberFormatException e){  
    return false;  
  }  
}


public static void main(String[] args) throws IOException {
	
	//System.out.println("Here"); 


    //first determine how many files the program is being fed
	int fileArguments = args.length; 
	
	System.out.println(fileArguments); 
	
	//for each file given to the program, make a new file and 
	//save it to the arraylist
	for(int i = 0; i < fileArguments; i++) {
		
		File tempFile = new File(args[i]); 
		filesGiven.add(tempFile); 
		
		
	}
	
	boolean flop = true; 
	for(int i = 0; i < fileArguments; i++) {
		
		try {
			
			Scanner reader = new Scanner(filesGiven.get(i), "UTF-8"); 
			
			while(reader.hasNextLine()) {
				
				String data = reader.nextLine(); 
				
				//System.out.println(data); 
				
				String [] wordsOnLine = data.split(" "); 
				//System.out.println(wordsOnLine.length); 
				
				if(wordsOnLine.length > 1) {
					locationTags.add(new LocationTagExtended(wordsOnLine[0],wordsOnLine[1],filesGiven.get(i).getName().replace(".txt",""))); 
					
				} 
				
			}
			reader.close(); 
			
		} catch (FileNotFoundException error) {
		  System.out.println("Error"); 
		  error.printStackTrace();
		}
		
		
	}
	
	
	//now we have built up our collection of location tags
	System.out.println(locationTags.size()); 
	
	Collections.sort(locationTags, LocationTagExtended.WordComparator);
	
	
	//now to build up the final lines arraylist
	
	for(int i = 0; i < locationTags.size(); i++) {
	    String buildString = ""; 
		
		//buildString = buildString + locationTags.get(i).getWord(); 
		//buildString = buildString + " "; 
		
		finalLines.add(" "); 
		finalLines.add(locationTags.get(i).getWord()); 
		
		buildString = buildString + locationTags.get(i).getFilename();
		//buildString = buildString + ": "; 
		
		buildString = buildString + locationTags.get(i).getParagraphCollectionString(); 
		
		finalLines.add(buildString); 
		
		//finalLines.add(" "); 
		
	}
	
	
	
	//now we have to remove the duplicate entries of words, while still preserving the order and both neighbours of the line being removed
	
	
	//for each line, if the line has a ":" in it, then you know that this is a "word" line
	//check if this line has already been added, if so remove it
	for(int i = 0; i < finalLines.size(); i++) {
		
		if(finalLines.get(i).indexOf(":") >= 0) {
			
			//this line is a word line - check if you already have it
			if(wordsAlreadyUsed.indexOf(finalLines.get(i)) >= 0) {
				
				//already have this line, should delete it
				finalLines.remove(i); 
				
			} else {
			    //this line is new
				wordsAlreadyUsed.add(finalLines.get(i)); 
				
			}
			
			
			
		} 
		
	}
	
	
	for(int i = 0; i < finalLines.size(); i++) {
		
		try {
			FileWriter writer = new FileWriter("combinedOutput.txt", true); 
		
			
			writer.write(finalLines.get(i)); 
			writer.write("\r\n"); 
			writer.close();
			
			
		} catch (IOException error) {
			System.out.println("error"); 
			error.printStackTrace(); 
			
		}
		
		
	}
	
	
	
	//set a small delay due to a potential race condition between creating the file above and accessing it below
	
	//System.out.println("taking a little break"); 
	//TimeUnit.SECONDS.sleep(3); 
	
	
	
	System.out.println("starting to convert to a .rtf file"); 
	
	
	//now take this text file and convert it to a rich text file using the rtf library
	
	ArrayList<RtfPara> rtfParagraphsCollection = new ArrayList<RtfPara>(); 
	
	
	
	try {
		
		File concordanceCombineOutput = new File("combinedOutput.txt"); 
		
		
		Scanner documentScanner = new Scanner(concordanceCombineOutput); 
		
		
		
		File finalOutput = new File("finalCombinedOutput.rtf"); 
		
		String finalData = ""; 
		
		
		
		while(documentScanner.hasNextLine()) {
			
			
			String data = documentScanner.nextLine();
			
			//get the index of ":" in the line
			int colonIndex = data.indexOf(":"); 
			
			
			
			//get the index of "[" in the line
			int bracketIndex = data.indexOf("["); 
			
			
			
			
			//this data line is a word line
			if(colonIndex > 0) {
				
				String wordData = ""; 
				
				
				wordData = data; 
				
				
				//this line in the output file is just going to be (in bold)-    theword: 
				RtfTextPara tempPara = p(bold(wordData), text("\n"));
				
				
				rtfParagraphsCollection.add(tempPara); 
				
				
			}
			
			
			//this data line is a concordance line
			if(bracketIndex > 0) {
				
				
				int lengthOfString = data.length(); 
				
				
				int indexOfBracket = data.indexOf("["); 
				
				
				String firstPart = ""; 
				
				String lastPart = ""; 
				
				
				//the title of the file
				firstPart = data.substring(0,indexOfBracket); 
				
				//the concordance portion of the file
				lastPart = data.substring(indexOfBracket, lengthOfString); 
				
				
				
				RtfTextPara tempPara = p(bold(firstPart), text(lastPart), text("\n")); 
				
				rtfParagraphsCollection.add(tempPara); 
				
				
				
				
				
			}
			
			
			
			
		}
		
		
		//write the entire collection of RtfTextPara to the output rtf file
		
		FileWriter fw = new FileWriter(finalOutput); 
		
		
		rtf().
			section(
				rtfParagraphsCollection
				).out(new FileWriter(finalOutput)); 
				
				
				
		documentScanner.close(); 
		
		
		
	} catch (FileNotFoundException e) {
		System.out.println("An error occured"); 
		
		e.printStackTrace(); 
		
		
	}
	
	
	
}












}