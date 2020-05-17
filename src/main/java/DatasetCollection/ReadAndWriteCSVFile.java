package DatasetCollection;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ReadAndWriteCSVFile {

    public static final List<String> stopwords = Arrays.asList("i", "me", "my", "myself", "we", "our", "ours",
            "ourselves", "you", "youre", "youve", "youll", "youd", "your", "yours", "yourself", "yourselves",
            "he", "hes", "him", "his", "himself", "she", "shes", "her", "hers", "herself", "it", "its", "its",
            "itself", "they", "them", "their", "theirs", "themselves", "what", "which", "who", "whom", "this",
            "that", "thatll", "these", "those", "am", "is", "are", "was", "were", "be", "been", "being", "have",
            "has", "had", "having", "do", "does", "did", "doing", "a", "an", "the", "and", "but", "if", "or",
            "because", "as", "until", "while", "of", "at", "by", "for", "with", "about", "against", "between",
            "into", "through", "during", "before", "after", "above", "below", "to", "from", "up", "down", "in",
            "out", "on", "off", "over", "under", "again", "further", "then", "once", "here", "there", "when",
            "where", "why", "how", "all", "any", "both", "each", "few", "more", "most", "other", "some", "such",
            "no", "nor", "not", "only", "own", "same", "so", "than", "too", "very", "s", "t", "can", "will",
            "just", "don", "dont", "should", "shouldve", "now", "d", "ll", "m", "o", "re", "ve", "y", "ain",
            "aren", "arent", "couldn", "couldnt", "didn", "didnt", "doesn", "doesnt", "hadn", "hadnt", "hasn",
            "hasnt", "haven", "havent", "isn", "isnt", "ma", "mightn", "mightnt", "mustn", "mustnt", "needn",
            "neednt", "shan", "shant", "shouldn", "shouldnt", "wasn", "wasnt", "weren", "werent", "won",
            "wont", "wouldn", "wouldnt");

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
            replaceChar = replaceChar.replaceAll("'[a-zA-Z0-9]*", "");
            replaceChar = replaceChar.replaceAll("[^a-zA-Z\\s+]", "");
            }
            return replaceChar;
    }

    public static String RemoveStopwords(String inputStr) {

        if(!inputStr.isEmpty()){
            ArrayList<String> arrWrds = Stream.of(inputStr.toLowerCase().split(" "))
                    .collect(Collectors.toCollection(ArrayList::new));

            arrWrds.removeAll(stopwords);

            return arrWrds.stream().collect(Collectors.joining(" "));
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
