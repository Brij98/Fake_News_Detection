package DatasetCollection;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ReadAndWriteCSVFile {

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
                    fileWriter.append(RemoveSpecialCharacter(arrstr[1]));
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
            replaceChar = replaceChar.replaceAll("[^a-zA-Z\\s+]", "");
            }
            return replaceChar;
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
