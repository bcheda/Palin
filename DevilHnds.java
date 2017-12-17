import java.io.*;
import java.util.*;
public class DevilHnds {
		private static String trimHeader(String input) 
		{
			//Removes the title line in the sequence file
			return input.substring(input.indexOf('\n') +1);
		}
		private static String Accession(String input)
		{
			int left = input.indexOf(">");
			int right = input.indexOf(" ");
			String Acc = input.substring(left+1, right);
			return Acc;
		}
		public static ArrayList<String> FASTA()throws FileNotFoundException 
		{
			//Reads in the fasta file into a list
			File file = new File("IAV_ALL.txt");
			Scanner sc = new Scanner(file);
			sc.useDelimiter(">");
			ArrayList<String> FASTAIn = new ArrayList<String>();
			while(sc.hasNext()) FASTAIn.add(sc.next());
			return(FASTAIn);
		}
		static String ChangingDNA(String DNA2mRNA) 
		{
			StringBuilder Complement = new StringBuilder();
			
			for (int i = 0; i < DNA2mRNA.length(); i++) {
				String DNA2RNA = DNA2mRNA.substring(i, i+1);
				switch (DNA2RNA) {
					case "A":
					case "a":
						Complement.append("T");
						break;
					case "t":
					case "T":
						Complement.append("A");
						break;
					case "G":
					case "g":
						Complement.append("C");
						break;
					case "C":
					case "c":
						Complement.append("G");
						break;
					case "_":
					case " ":
						Complement.append("");
						break;
					default:
						//doNothing
				}
			}
			return Complement.toString();
		}
		public static ArrayList<String> Palindromes(String string)throws StringIndexOutOfBoundsException
		{
			ArrayList<String> Palindromes = new ArrayList<String>();
			int SequenceLen = string.length();
			//Breaks down the sequence into characters
			for(int Seq_char_position = 0;Seq_char_position < SequenceLen; Seq_char_position++) 
			{
				//Breaks down the subsequence so that it's analyzed in a range of 4-31
				for(int Palindrome_len = 4; Palindrome_len > 3 && Palindrome_len < 41; Palindrome_len++) 
				{
					//Defines the substring to begin at the first character in the index and ending
					int combined = Seq_char_position+Palindrome_len;
					if(combined < string.length()) 
					{
					String Substring = string.substring(Seq_char_position,Seq_char_position+Palindrome_len);
						if(Substring.length() %2 ==0) 
						{
							//System.out.println("The substring is: " +Substring);
							//Halves the substring
							String Sub1 = Substring.substring(0, (Substring.length()/2));
							String Sub2 = Substring.substring(Substring.length()/2);
							//Flips Sub2, the second half of the substring
							StringBuilder Sub2Flipped = new StringBuilder();
							Sub2Flipped.append(Sub2);
							Sub2Flipped = Sub2Flipped.reverse();
							Sub2 = Sub2Flipped.toString();
							//System.out.println("The sub2Flipped is: "+Sub2);
							Sub2 = ChangingDNA(Sub2);
							//System.out.println("The complemented sub2 is: " + Sub2);
								if(Sub1.equals(Sub2)) 
								{
								//System.out.println(Substring);
								Palindromes.add(Substring);
								return Palindromes;
								}
						}
					}
				}
			}
			return Palindromes;
		}
		private static String findSubstringIndexes(String inputString, String stringToFind){
		    String indexes = "";
		    int index = inputString.indexOf(stringToFind);
		    while (index >= 0){
		        indexes+= (indexes.equals("")) ? String.valueOf(index) : ", " + String.valueOf(index);
		        index = inputString.indexOf(stringToFind, index + stringToFind.length())   ;
		    }
		    return indexes;
		}
		private static void writeListToFile(String filePath, ArrayList<String> list, boolean... appendToFile) {
			System.out.println("Reached writeListToFile");
		    boolean appendFile = false;
		    if (appendToFile.length > 0) { appendFile = appendToFile[0]; }

		    try {
		        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, appendFile))) {
		            for (int i = 0; i < list.size(); i++) {
		                bw.append(list.get(i) + System.lineSeparator());
		            }
		        }
		    } catch (IOException ex) {
		        ex.printStackTrace();
		    }

		}
		private static ArrayList<String> getPalMatches(ArrayList<String> Palindromes) {
			System.out.println("Reached getpalmatches");
		    ArrayList<String> accMatching = new ArrayList<>();
		   // System.out.println("Reached first for loop");
		    for (int i = 0; i < Palindromes.size(); i++) {
		        String matches = "";
		       // System.out.println("Reached string[] split1");
		        String[] split1 = Palindromes.get(i).split("\\s+");
		       // System.out.println("Reached string pal1");
		        String pal1 = split1[0];
		        // Make sure the current Pal hasn't already been listed.
		        boolean alreadyListed = false;
		       // System.out.println("Reached inner loop");
		        for (int there = 0; there < accMatching.size(); there++) 
		        {
		        //	System.out.println("Reached String[] th");
		            String[] th = accMatching.get(there).split("\\s+");
		          //  System.out.println("Reached if(th[0]");
		            if (th[0].equals(pal1)) 
		            {
		          //  	System.out.println("Reached alreadylisted inside if");
		                alreadyListed = true;
		                break;
		            }
		        
		        }
		       // System.out.println("Reached if(aleadyListed)");
		        if (alreadyListed) { continue; }
		       // System.out.println("Reached for int j");
		        for (int j = 0; j < Palindromes.size(); j++) {
		        //	System.out.println("Reached split2");
		            String[] split2 = Palindromes.get(j).split("\\s+");
		           // System.out.println("Reached pal2");
		            String pal2 = split2[0];
		            
		           // System.out.println("Reached if (pal1.equals(pal2)");
		            if (pal1.equals(pal2)) {
		                // Using Ternary Operator to build the matches string
		            //	System.out.println("Reached matches");
		                matches+= (matches.equals("")) ? pal1 + " was found in the following Accessions: " + split2[3] : ", " + split2[3];
		            
		            }
		        }
		        if (!matches.equals("")) {
		            accMatching.add(matches);
		            writeListToFile("PB2Out.txt", accMatching);
		        }
		    }
		    System.out.println("Reached return");
		    return accMatching;
		}
		public static ArrayList<String> PB2Scan(String filePath, ArrayList<String> Pal, int resultType) 
                throws FileNotFoundException, IOException {
// Make sure the supplied result type is either 
// 0, 1, or 2. If not then default to 0.
			if (resultType < 0 || resultType > 2) 
			{
				resultType = 0;
			}
			ArrayList<String> PalindromesSpotted = new ArrayList<>();

			File file = new File(filePath);
			Scanner sc = new Scanner(file);
			sc.useDelimiter(">");
			//initializes the ArrayList
			ArrayList<String> Gene1 = new ArrayList<>();
			//Loads the Array List
			while (sc.hasNext()) 
			{
				Gene1.add(sc.next());
			}
			sc.close(); // Close the read in text file.
			for (int i = 0; i < Gene1.size(); i++) {
				//Acc breaks down the title so the element:
				//>AX225014 Equine influenza virus H3N8 // 1 (PB2)
				//ATGAAGACAACCATTATTTTGATACTACTGACCCATTGGGTCTACAGTCAAAACCCAACCAGTGGCAACA
				//GGCATGTCCGCAAACGATTTGCAGACCAAGAACTGGGTGATGCCCCATTCCTTGACCGGCTTCGCCGAGA
				//comes out as AX225014
				String Acc = Accession(Gene1.get(i));
				//seq takes the same element as above and returns only
				//ATGAAGACAACCATTATTTTGATACTACTGACCCATTGGGTCTACAGTCAAAACCCAACCAGTGGCAACA
				//GGCATGTCCGCAAACGATTTGCAGACCAAGAACTGGGTGATGCCCCATTCCTTGACCGGCTTCGCCGAGA
				String seq = trimHeader(Gene1.get(i));
				for (int x = 0; x < Pal.size(); x++) 
				{
					if (seq.contains(Pal.get(x))) 
					{
						String match = Pal.get(x) + " in organism: " + Acc + " Was found in position(s): " + seq.indexOf(Pal.get(x));
						PalindromesSpotted.add(match);
					}
				}
			}
			// If there is nothing to work with get outta here.
			if (PalindromesSpotted.isEmpty()) 
			{
				return PalindromesSpotted;
			}

			// Sort the ArrayList
			Collections.sort(PalindromesSpotted);
			// Another ArrayList for matching Pal's to Acc's
			ArrayList<String> accMatchingPal = new ArrayList<>();
			switch (resultType) 
			{
			case 0: // if resultType is 0 is supplied
			{
				writeListToFile("PB2Out.txt", PalindromesSpotted);
				return PalindromesSpotted;
			}
			case 1: // if resultType is 1 is supplied
			{
				accMatchingPal = getPalMatches(PalindromesSpotted);
				writeListToFile("PB2Out.txt", accMatchingPal);
				return accMatchingPal;
			}
			default: // if resultType is 2 is supplied
				accMatchingPal = getPalMatches(PalindromesSpotted);
				
				ArrayList<String> fullList = new ArrayList<>();
				
				fullList.addAll(PalindromesSpotted);
				// Create a Underline made of = signs in the list.
				fullList.add(String.join("", Collections.nCopies(70, "=")));
				fullList.addAll(accMatchingPal);
				writeListToFile("PB2Out.txt", fullList);
				return fullList;
			}
}
		public static ArrayList<String> RemoveDuplicates(ArrayList<String> Palindrome)
		{
			Set<String> RemoveDuplicates = new HashSet<>();
			RemoveDuplicates.addAll(Palindrome);
			Palindrome.clear();
			Palindrome.addAll(RemoveDuplicates);
			Collections.sort(Palindrome);
			System.out.println(Palindrome);
			return Palindrome;
		}
		public static void main(String[] args) throws IOException 
		{
		ArrayList<String> ReadFile = FASTA();
		ArrayList<String> Palindrome  = new ArrayList<String>();
		System.out.println("Running,please wait");
		for(int i = 0; i < ReadFile.size(); i++) 
			{
			String in =trimHeader(ReadFile.get(i));
			Palindrome.addAll(Palindromes(in));
			}
		Palindrome = RemoveDuplicates(Palindrome);
		System.out.println("The number of unique palindromes is: " +Palindrome.size());
		System.out.println("Analyzing sequences");
		ArrayList<String> PB2 = new ArrayList<String>();
		PB2 = PB2Scan("IAV_PB2_32640.txt", Palindrome, 2);
		System.out.println("Program Terminated");
		}
	}