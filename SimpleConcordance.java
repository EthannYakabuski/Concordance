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


import java.io.FileOutputStream;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph; 
import org.apache.poi.xwpf.usermodel.XWPFRun; 


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


class LocationTag {
	
	ArrayList<Integer> paragraphNum = new ArrayList<Integer>(); 
	String word; 
	
	LocationTag(String w, int paragraph) {
		word = w; 
		paragraphNum.add(paragraph); 
		
	}
	
	String getWord() {
		return word; 
	}
	
	ArrayList<Integer> getParagraphNums() {
		return paragraphNum; 
	}
	
	public void printMe() {
		System.out.println("word: " + word + " is in paragraph: " + paragraphNum); 
	}
	
	
	public static void main(String[] args) {
		//System.out.println("Here"); 
	}
	
	
	public static Comparator<LocationTag> WordComparator = new Comparator<LocationTag>() {
		
		public int compare(LocationTag l1, LocationTag l2) {
			
			String l1Word = l1.getWord();
			String l2Word = l2.getWord();
			
			
			return l1Word.compareTo(l2Word);
		
		
		
    }};
	
}
	

class SimpleConcordance {
	
	static int paragraph = 0; 
	static boolean incrementThisRound = false; 
	static ArrayList<LocationTag> locationTags = new ArrayList<LocationTag>(); 
	static ArrayList<String> finalLines = new ArrayList<String>();

public static boolean isNumeric(String str) { 
  try {  
    Integer.parseInt(str);  
    return true;
  } catch(NumberFormatException e){  
    return false;  
  }  
}


public static void main(String[] args) throws IOException {
  System.out.println("Hello welcome to SimpleConcordance");
  
  
  ArrayList<RtfPara> rtfParagraphsCollection = new ArrayList<RtfPara>(); 
  
  
  //if there is a command line argument (assuming a file)
  if(args.length > 0) {
	  File file = new File(args[0]); 
	  
	  //System.out.println("here"); 
	  
	  try {
		//System.out.println(file); 
		Scanner reader = new Scanner(file, "UTF-8"); 
	    //System.out.println("here"); 
		
		System.out.println(reader.hasNextLine()); 
		while(reader.hasNextLine()) {
			String data = reader.nextLine();
			//System.out.println(data); 
			
			//System.out.println("here"); 
			
			String [] wordsOnTheLine = data.split(" ");
			
			 
			
			//for each word on the given line
			for(String word:wordsOnTheLine) {
				//System.out.println(word);
				
				
				//determine if there is a new paragraph - we know that the word is always '#.' ,when there is a new paragraph
				//first remove the . so that we can transform it into a digit
				String noPeriodString = word.replace(".", ""); 
				
				boolean newParagraph = isNumeric(noPeriodString); 
				
				//this means that we have got a new paragraph started by a '#.' character
				if(newParagraph) {
					//System.out.println("this was the word that initiated the paragraph change: " + word);
					
					//sometimes a number in reference to a year will slip by such as '1964', this should
					//still be added to the concordance.
					
					//checking to make sure that the integer value of the string, is = paragraph + 1 otherwise it doesn't make sense
					try {  
                      int paragraphDelimiter = Integer.parseInt(noPeriodString);  
                      
					  //if this paragraph number is equal to our paragraph counter, we know this is correct positioning
					  if(paragraphDelimiter == paragraph+1) {
						  //System.out.println("correct positioning");
						  incrementThisRound = true; 
					  } else {
						  //System.out.println("wrong positioning - not incrementing");
						  incrementThisRound = false;
						  
						  
						  //however we still do need to create a tag and add this number to the concordance
						  
					  }
					  
                    } catch(NumberFormatException e){  
                     
                    }  
					
					if(incrementThisRound) { paragraph = paragraph + 1; }
					System.out.println("consuming paragraph ': " + paragraph);  
				} else { //otherwise this is a normal word that needs to be added to the concordance
				
					//sometimes there is garbage punctuation attached to the word - need to first remove this
					
					//System.out.println(word);
					
				
					//old solution start
					//does not account for world's as different than worlds
					//String trimmedWord = word.replaceAll("[^a-zA-Z ]", "").toLowerCase(); 
					//old solution finished
					
					
					
					//String trimmedWord = word.replaceAll("'", "xxxx"); 
					
					String trimmedWord = word.replaceAll("[^a-zA-Z' ]", "").toLowerCase(); 
					
					//System.out.println(word);
					
					//String trimmedWord = word.replaceAll(Pattern.quote("?s"), "xxxs");
					
					//System.out.println(trimmedWord); 
					
					//trimmedWord = trimmedWord.replaceAll("[^a-zA-Z ]", "").toLowerCase();
					
					
					//String trimmedWord = word.replaceAll("[\\[\\]\\{\\}\\/,_\"-.!?:;)(]", "");
					
					//String trimmedWord = word.replaceAll("[^a-zA-Z0-9'\\s]+",""); 
					
					
					//String trimmedWord = word.replaceAll("\\?","_");
					
					//System.out.println(trimmedWord + "---------------------"); 
					
					//trimmedWord = trimmedWord.replaceAll("[^a-zA-Z_ ]", "").toLowerCase(); 
					
					//System.out.println(trimmedWord + "--------------------------------------"); 
        
					//trimmedWord = trimmedWord.replaceAll("[\\p{Punct}&&[^_]]+", "").toLowerCase();  
					
					//trimmedWord = word.replaceAll("[\\p{Punct}&&[^_-]]+", "");
					
					
					
					
					
					//now we want to check if the indexOf the apostrophe (') character is the last index in the string
					//if the ' character is the last index in the string, we can remove just the apostrophe character from the string
					
					int lengthOfString = trimmedWord.length(); 
					
					
					int indexOfApostrophe = trimmedWord.indexOf("'"); 
					
					
					if(indexOfApostrophe != -1) {
					  if(lengthOfString-1 == indexOfApostrophe) {
						
						
						  String newString = trimmedWord.substring(0,indexOfApostrophe); 
						
						  trimmedWord = newString; 
						
						
					  }
					}
					
					
					
					
					//now we want to check if the indexOf the apostrophe (') character is the first index in the string
					//if the ' character is the first index in the string, we can remove just the apostrophe character from the string
					
					lengthOfString = trimmedWord.length(); 
					
					indexOfApostrophe = trimmedWord.indexOf("'"); 
					
					
					
					if(indexOfApostrophe == 0) {
						
						String newString = trimmedWord.substring(1,lengthOfString); 
						
						trimmedWord = newString; 
						
						
					}
					
					
					
					
					
					
					locationTags.add(new LocationTag(trimmedWord, paragraph)); 
					
					//System.out.println(trimmedWord); 
					
					/*
					//check to see if this word is already in the arraylist
					boolean alreadyHaveWord = false; 
					for(int i = 0; i < locationTags.size(); i++) {
						
						
						if(locationTags.get(i).getWord().equals(trimmedWord)) {
							System.out.println("This word is already in the list"); 
							alreadyHaveWord = true;
							//add a new paragraph marker to this words location tag
							locationTags.get(i).getParagraphNums().add(paragraph); 
							break; 
						} else {
							//make a new location tag and add this to the arraylist
							//System.out.println("New word adding to the list"); 
							LocationTag tempTag = new LocationTag(trimmedWord, paragraph); 
							locationTags.add(tempTag); 
						}
						
						
						
						
					}
					*/
					
					/*
					
					//if this is our first word
					if(locationTags.size() == 0) {
						//make a new location tag and add this to the arraylist
					    //System.out.println("New word adding to the list"); 
					    LocationTag tempTag = new LocationTag(trimmedWord, paragraph); 
						locationTags.add(tempTag); 
						
					}
					
					*/
					
					
					
					
				}
					
				
				
			}
			
			
			
			
			//System.out.println("/n"); 
		}
		reader.close();
	  } catch (FileNotFoundException error) {
		  System.out.println("Error"); 
		  error.printStackTrace();
	  }
	  
	  
	  
	  
	//at this point we have an array list with a location tag for each word, however
	//duplicate entries may exist like <Word, 6>, <Word, 7>, <Word, 22> .. but they won't
	//be in order, our task here is now to sort this array list by word
	System.out.println("locationTags.size(): " + locationTags.size()); 
	
	
	//using the custom comparator sort this arraylist
	System.out.println("Sorting these words alphabetically"); 
	Collections.sort(locationTags, LocationTag.WordComparator); 
	
	
	System.out.println("Sorting completed"); 
	
	System.out.println("Removing white space characters - some remain this is okay"); 
    
	for(int i = 0; i < locationTags.size(); i++) {
		
		if(locationTags.get(i).getWord().equals("")) {
			locationTags.remove(i); 
		}
	}
	
	//not actually removing all of the white space characters for some reason?
	System.out.println("White space characters removed"); 
	
	/*
	System.out.println("The first few entries in the arraylist are: ");
	
	for(int i = 0; i < 500; i++) {
		locationTags.get(i).printMe(); 
	}
	*/
	
	System.out.println("Creating Concordance ArrayList"); 
	
	//now to make a file that is the concordance of this arraylist 
	String lastString = ""; 
	String buildString = ""; 
	for(int i = 1; i < locationTags.size(); i++) {
		
		
		String tempString = locationTags.get(i).getWord(); 
		
		if(tempString.equals(lastString)) {
			//if we are in this case, we need to start logging paragraph numbers
			
			//System.out.println("Same word"); 
			buildString = buildString + "[";
			buildString = buildString + locationTags.get(i).getParagraphNums().get(0); 
			buildString = buildString + "]";
			
		} else {
			//System.out.println("Different word"); 
			
			//we are now encountering a different word, we need to save buildString
			finalLines.add(buildString); 
			
			//set build string to now be the new word
			buildString = locationTags.get(i).getWord();
			buildString = buildString + ": "; 
			
			//add the first paragraph instance to the string
			buildString = buildString + "["; 
			buildString = buildString + locationTags.get(i).getParagraphNums().get(0); 
			buildString = buildString + "]";
			
			//set the last string to now be the new word
			lastString = locationTags.get(i).getWord(); 
			
		}
		
		/*
		//if this current word, is the same as the last one we are on the same word
		if(locationTags.get(i).getWord().equals(locationTags.get(i-i).getWord())) {
			System.out.println("Same word"); 
			
			
		} else { //the current word is different from the last, new line
			
			try {
				FileWriter writer = new FileWriter("lumenGentiumConcordance.txt", true); 
				writer.write(locationTags.get(i).getWord()); 
				writer.close();
				
			} catch (IOException error) {
				System.out.println("Error"); 
				error.printStackTrace(); 
			}
			
			
		}
		*/
		
		
	}
	
	/*
	//check on the finalLines arrayList
	for(int i = 0; i < finalLines.size(); i++) {
		System.out.println(finalLines.get(i)); 
	}
	*/
	
	System.out.println("Writing Concordance ArrayList to file"); 
	
    for(int i = 0; i < finalLines.size(); i++) {
		
		try {
			FileWriter writer = new FileWriter("concordanceOutput.txt", true); 
			writer.write(finalLines.get(i)); 
			writer.write("\n"); 
			writer.close();
			
			
		} catch (IOException error) {
			System.out.println("error"); 
			error.printStackTrace(); 
			
		}
		
		
	}
	  
	
	System.out.println("Writing to file finished"); 
	
	
	
	
	//now take the concordanceOutput.txt and convert it to a word document
	System.out.println("Starting to convert to a word document"); 
	
	
	try { 
		File concordanceOutput = new File("concordanceOutput.txt"); 
	
		Scanner documentScanner = new Scanner(concordanceOutput);
		
		
		//System.out.println("here"); 
		
		File finalOutput = new File("finalOutput.rtf"); 
		
		String finalData = ""; 
		
		while(documentScanner.hasNextLine()) {
		
			String data = documentScanner.nextLine();
			
			//System.out.println(data);
			
			//take this line -
		    //remove just the word and make it bold
			//dont make the rest of it bold
			
			String[] splitted = data.split(":");
			
			String wordData = ""; 
			String concordanceData = ""; 
			
			
			if(splitted.length > 1) {
			  wordData = splitted[0]; 
			  concordanceData = splitted[1]; 
			} else {
		      
			}
			
			
			RtfTextPara tempPara = p(bold(wordData), text(" : "), text(concordanceData), text("\n"));
			
		
			rtfParagraphsCollection.add(tempPara);
			
			
			//finalData = finalData + data + "\n" + "\n"; 
			
			
					
			
		}
		
		
		
		/*
		rtf().
				header( font( RtfHeaderFont.WINDINGS ).at(1) ).
				section(
					p(finalData)
					).out( new FileWriter( finalOutput ) );
		*/
		
		
		//now rtfParagraphsCollection has a collection of rtf paragraphs that need to each be written to the word document
		FileWriter fw = new FileWriter(finalOutput); 
		
		
		
		//RtfTextPara finalPara = collToParagraph(rtfParagraphsCollection); 
		
		//RtfText finalText = text(rtfParagraphsCollection); 
		
		
		rtf().
			section(
				rtfParagraphsCollection
				).out(new FileWriter(finalOutput));
				
				
				
		
		
		
		//for(int i = 0; i < rtfParagraphsCollection.size(); i++) {
			//System.out.println("adding a line"); 
			
			
			
			
			
			/*
			rtf().
				section(
					rtfParagraphsCollection.get(i)
					).out( new FileWriter(finalOutput) );
					*/
			
			/*
			rtf().
				header( font( RtfHeaderFont.WINDINGS ).at(1) ).
				section(
					rtfParagraphsCollection.get(i)
					).out( new FileWriter( finalOutput ) );
			*/
			
			
			
		//}
	
		
		/*
		rtf().
				header( font( RtfHeaderFont.WINDINGS ).at(1) ).
				section(
					rtfParagraphsCollection.get(i)
					).out( new FileWriter( finalOutput ) );
		*/
		
		documentScanner.close();
		
		
	} catch (FileNotFoundException e) {
		System.out.println("An error occurred"); 
		
		e.printStackTrace();
		
		
	}

	
	
	
	
	/*
	//take the concordance output and iterate through each line of the document
	try { 
		File concordanceOutput = new File("concordanceOutput.txt"); 
	
		Scanner documentScanner = new Scanner(concordanceOutput);
	
	    FileOutputStream out = new FileOutputStream( new File("wordFinalOutput.rtf")); 
		
		XWPFParagraph paragraph = wordDocument.createParagraph();
	    XWPFRun run = paragraph.createRun();
	
		while(documentScanner.hasNextLine()) {
		
			String data = documentScanner.nextLine();
			
			System.out.println(data);
		
			//System.out.println(data.getClass().equals(String.class)); 
			
			
			run.setText(data); 
			run.setFontFamily("Courier");
			run.setFontSize(14);
			run.setColor("009933"); 
			
			
			
			try {
				wordDocument.write(out);
			} catch (IOException e) {
				
				System.out.println("write error"); 
			}
		}
		
		documentScanner.close();
		
		try {
			out.close();
		} catch (IOException e) {
			
			System.out.println("Close error");
		}
		
	} catch (FileNotFoundException e) {
		System.out.println("An error occurred"); 
		
		e.printStackTrace();
		
		
	}
	*/
	
	
	
	
	  
  }

}

}