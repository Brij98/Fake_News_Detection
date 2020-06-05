package DatasetCollection;

import opennlp.tools.stemmer.PorterStemmer;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ReadAndWriteCSVFile {

    public static final List<String> stopwords = Arrays.asList("a", "about", "above", "after", "again", "against",
            "ain", "all", "am", "an", "and", "any", "are", "aren", "aren't", "as", "at", "be", "because", "been",
            "before", "being", "below", "between", "both", "but", "by", "can", "couldn", "couldn't", "d", "did",
            "didn", "didn't", "do", "does", "doesn", "doesn't", "doing", "don", "don't", "down", "during", "each",
            "few", "for", "from", "further", "had", "hadn", "hadn't", "has", "hasn", "hasn't", "have", "haven",
            "haven't", "having", "he", "her", "here", "hers", "herself", "him", "himself", "his", "how", "i", "if",
            "in", "into", "is", "isn", "isn't", "it", "it's", "its", "itself", "just", "ll", "m", "ma", "me",
            "mightn", "mightn't", "more", "most", "mustn", "mustn't", "my", "myself", "needn", "needn't", "no",
            "nor", "not", "now", "o", "of", "off", "on", "once", "only", "or", "other", "our", "ours", "ourselves",
            "out", "over", "own", "re", "s", "same", "shan", "shan't", "she", "she's", "should", "should've",
            "shouldn", "shouldn't", "so", "some", "such", "t", "than", "that", "that'll", "the", "their", "theirs",
            "them", "themselves", "then", "there", "these", "they", "this", "those", "through", "to", "too", "under",
            "until", "up", "ve", "very", "was", "wasn", "wasn't", "we", "were", "weren", "weren't", "what", "when",
            "where", "which", "while", "who", "whom", "why", "will", "with", "won", "won't", "wouldn", "wouldn't",
            "y", "you", "you'd", "you'll", "you're", "you've", "your", "yours", "yourself", "yourselves", "could",
            "he'd", "he'll", "he's", "here's", "how's", "i'd", "i'll", "i'm", "i've", "let's", "ought", "she'd",
            "she'll", "that's", "there's", "they'd", "they'll", "they're", "they've", "we'd", "we'll", "we're",
            "we've", "what's", "when's", "where's", "who's", "why's", "would"/*,
            "arent", "couldnt", "didnt", "doesnt", "dont", "hadnt", "hasnt", "havent", "isnt"*/

            );

    // stores the info read from CSV file
    public static List<String> readText = new ArrayList<>();

    // Stores the processed data to write to CSV file
    public static List<String[]> processedText = Collections.synchronizedList(new ArrayList<String[]>());

    public static void ReadFromCSVFile(String FileName, int ColNum) throws Exception{
        //File flcsv = new File("../Datasets/"+ FileName);
        File flcsv = new File(FileName); // Test purpose

        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(flcsv))) {
            String strLn;
            String header = bufferedReader.readLine();
            while((strLn = bufferedReader.readLine()) != null){
                String[] tokens = strLn.split(",");
                readText.add(tokens[ColNum -1]);
            }
        }  catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<String[]> readMultipleText(String filename, int label, int text){
        ArrayList<String[]> list_toret = new ArrayList<String[]>();
        File csv_file = new File(filename);

        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(csv_file))) {
            String strLn;
            String header = bufferedReader.readLine();
            while((strLn = bufferedReader.readLine()) != null){
                String[] tokens = strLn.split(",");
                String[] arr_elems = new String[2];
                arr_elems[0] = tokens[label -1]; // read labels
                arr_elems[1] = tokens[text -1]; // read text
                list_toret.add(arr_elems);
            }
        }  catch (IOException e) {
            e.printStackTrace();
        }  catch (Exception e){
            e.printStackTrace();
        }
        return list_toret;
    }

    public static void writeToCSV(String fileName) throws IOException {
        File outputCSVfile = new File(fileName);

        FileWriter fileWriter = new FileWriter(outputCSVfile, true);
        if(outputCSVfile.length() == 0){
            fileWriter.append("Link");
            fileWriter.append(",");
            fileWriter.append("Text");
            fileWriter.append(",");
            fileWriter.append("Status");
            fileWriter.append("\n");
        }

        synchronized (processedText){
            Iterator iterator = processedText.iterator();
            while (iterator.hasNext()){
                String[] arrstr = (String[]) iterator.next();
                if(arrstr.length > 0){

                    //StringBuilder stringBuilder = new StringBuilder();
                    fileWriter.append(arrstr[0]);
                    fileWriter.append(",");
                    //fileWriter.append(RemoveSpecialCharacter(arrstr[1]));
                    fileWriter.append(arrstr[1]);
                    fileWriter.append(",");
                    fileWriter.append(arrstr[2]);
                    fileWriter.append("\n");
                }
            }
            fileWriter.flush();
            fileWriter.close();
        }
    }

    public static String RemoveSpecialCharacter(String str){
        String replaceChar = "";
        if(!str.isEmpty()){
            replaceChar = str.replaceAll("\\r", " ");
            replaceChar = replaceChar.replaceAll("'[a-zA-Z0-9]*", ""); // remove after apostrophe
            replaceChar = replaceChar.replaceAll("[^a-zA-Z\\s+]", ""); // remove any special character
            replaceChar = replaceChar.replaceAll("[\\s{2, }]", " "); // remove more than one spaces
            }
            return replaceChar;
    }

    public static String RemoveStopwords(String inputStr) {

        if(!inputStr.isEmpty()){
            ArrayList<String> arrWrds = Stream.of(inputStr.toLowerCase().split(" "))
                    .collect(Collectors.toCollection(ArrayList::new));

            arrWrds.removeAll(stopwords); // removing all the stop words

            return arrWrds.stream().collect(Collectors.joining(" "));
        }
        return "";
    }

    public static String ProcessString(String inputstr){
        if(!inputstr.isEmpty()){

            inputstr = inputstr.toLowerCase(); // change to lower case

            inputstr = inputstr.replaceAll("\n", " "); // replace new line character with space

            inputstr = inputstr.replaceAll("'[a-zA-Z0-9]*", ""); // remove after apostrophe

            inputstr = inputstr.replaceAll("’[a-zA-Z0-9]*", ""); // remove after apostrophe

            inputstr = inputstr.replaceAll("`[a-zA-Z0-9]*", ""); // remove after apostrophe

            inputstr = inputstr.replaceAll("[^a-zA-Z\\s+]", ""); // remove any special character

            inputstr = inputstr.replaceAll("\\s{2,}", " "); // remove more than one spaces

            List<String> stringList = new ArrayList<>(Arrays.asList(inputstr.split(" ")));
            stringList.removeAll(ReadAndWriteCSVFile.stopwords);

            String nonstopword = String.join(" ", stringList);
            nonstopword = nonstopword.replaceAll("\\s{2,}", " "); // remove more than one spaces

            // experiment
            StringBuilder stringBuilder = new StringBuilder();
            PorterStemmer porterStemmer = new PorterStemmer();
            String[] strArr = nonstopword.split(" ");
            String strToRet = "";
            for (String word :
                    strArr) {
                strToRet += porterStemmer.stem(word) + " ";
            }
            strToRet += strToRet.substring(0, strToRet.length() -1);
            strToRet = strToRet.replaceAll("\\s{2,}", " ");
            return strToRet;
            // experiment end

            //return nonstopword;
        }
        return "";
    }

    public static void PrintProcessedData(){
        System.out.println("Size of : "+ processedText.size()); // debug
        synchronized (processedText){
            Iterator iterator = processedText.iterator();
            while (iterator.hasNext()){
                String[] arrstr = (String[]) iterator.next();
                System.out.println(arrstr[0] + " : "+ arrstr[1]);
            }
        }
    }

    public static void ShuffleProcessedData(){
        Collections.shuffle(processedText);
    }

}
