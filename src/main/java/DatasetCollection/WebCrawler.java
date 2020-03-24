package DatasetCollection;

import java.io.*;
import java.util.ArrayList;

public class WebCrawler {

    /*
    * Read URLs from CSV file from a certain Column
    * */
    public ArrayList<String> ReadFromCSVFile(String FileName, int ColNum){

        ArrayList<String> arrLinks = new ArrayList<>();
        File flcsv = new File("../Datasets/"+ FileName);

        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(flcsv))) {
            String strLn;
            String header = bufferedReader.readLine();
            while((strLn = bufferedReader.readLine()) != null){
                String[] tokens = strLn.split(",");
                arrLinks.add(tokens[ColNum -1]);
            }
        }  catch (IOException e) {
            e.printStackTrace();
        }
        return arrLinks;
    }

}
