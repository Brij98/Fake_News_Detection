package FeatureExtraction;

import DatasetCollection.ReadAndWriteCSVFile;

import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TF_IDF_Vectorizer {

    private final static int NUMTHREADS = 15;

    // stores the calculated term frequency vector of each article
    public static List<HashMap<String, Double>> DocumentTfList = Collections.synchronizedList(new ArrayList<>());

    private static HashMap<String, Double> idfHashmap = new HashMap<>(); // stores unique words and the corresponding IDF score.

    private SortedSet<String> wordList = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);

    private double[][] tfidfMat;

    public TF_IDF_Vectorizer(String csvFileToRead, int colNumToRead, String saveto) throws Exception {
        ReadAndWriteCSVFile.readText.clear();
        ReadAndWriteCSVFile.ReadFromCSVFile(csvFileToRead, colNumToRead);

        long startTime = System.nanoTime(); // debug
        MultithreadedTfCalc();
        long endTime   = System.nanoTime();
        long totalTime = endTime - startTime;
        System.out.println(totalTime);


        PopulateWordList();
        CalculateIdf();
        TfIdfCalculator();
        write_TfIdfMat_to_CSV(saveto);

    }

    // Create threads for each document to calculate its term frequency
    private void MultithreadedTfCalc(){
        ExecutorService executorService = Executors.newFixedThreadPool(NUMTHREADS);
        for(String text : ReadAndWriteCSVFile.readText){
            DocumentTF documentTF = new DocumentTF(text);
            executorService.execute(documentTF);
        } // end of for each loop
        executorService.shutdown();
        while (!executorService.isTerminated()){} // wait for it to terminate

        ReadAndWriteCSVFile.readText.clear();
    }

    // Add all the unique words to the word list
    private void PopulateWordList(){
        synchronized (DocumentTfList){
            for(HashMap<String, Double> hashMap : DocumentTfList){
                Set<String> stringArrayList = hashMap.keySet();
                for (String str : stringArrayList) {
                    if(!wordList.contains(str)) wordList.add(str);
                } // end of inner for each loop
            } // end of for each loop
        }
    }

    // Calculating and storing IDF for each word
    private void CalculateIdf(){
        int numofDocs = DocumentTfList.size();
        double wordCnt;
        for (String word : wordList) {
            wordCnt = 0;
            for(int k = 0; k < numofDocs; k++){
                if(DocumentTfList.get(k).containsKey(word)){
                    wordCnt++;
                } // end of if
            } // end of for loop
            double idfCalc = Math.log(numofDocs / wordCnt);
            idfHashmap.put(word, idfCalc);
        } // end of for each loop
    }

    private void TfIdfCalculator(){
        tfidfMat = new double[DocumentTfList.size()][wordList.size()];

        int intdoc = 0;
        for(HashMap<String, Double> hashMap : DocumentTfList){
            int intword = 0;
            for(String word : wordList){
                if(hashMap.containsKey(word)){
                    double tf = hashMap.get(word);
                    double idf = idfHashmap.get(word);
                    tfidfMat[intdoc][intword] = tf * idf;
                }else tfidfMat[intdoc][intword] = 0;
                intword++;
            } // end of inner loop
            intdoc++;
        } // end of outer loop

        /*int intword = 0;
        for(String word : wordList){
            int intdoc = 0;
            for (HashMap<String, Double> hashMap : DocumentTfList){
                if(hashMap.containsKey(word)){
                    double tf = hashMap.get(word);
                    double idf = idfHashmap.get(word);
                    tfidfMat[intword][intdoc] = tf * idf;
                }else tfidfMat[intword][intdoc] = 0;
                intdoc++;
            } // end of inner loop
            intword++;
        } // end of outer loop*/
    }

    private void write_TfIdfMat_to_CSV(String fileLoc){

        System.out.println("idfmap size: "+ idfHashmap.size());
        System.out.println("wordlist size: "+ wordList.size());

        File fl = new File(fileLoc+".csv");

        try(FileWriter fileWriter = new FileWriter(fl, true)){
            StringBuilder stringBuilder = new StringBuilder();
            for (String word : wordList) {
                stringBuilder.append(word);
                stringBuilder.append(",");
            }
            String toWriteStr = stringBuilder.toString().replaceAll(",$", "\n");
            fileWriter.write(toWriteStr);

            for(int d = 0; d < DocumentTfList.size(); d++){
                StringBuilder strbldr = new StringBuilder();
                for(int w = 0; w < wordList.size(); w++){
                    strbldr.append(tfidfMat[d][w]);
                    strbldr.append(",");
                } // end of inner loop
                String str = strbldr.toString().replaceAll(",$", "\n");
                fileWriter.write(str);
            } // end of outer loop

            fileWriter.flush();
            /*for(int w = 0; w < wordList.size(); w++){
                for(int d = 0; d < DocumentTfList.size(); d++){
                    stringBuilder.append(tfidfMat[w][])
                }
            }*/

        }catch (FileNotFoundException ex){
            System.err.println("FILE NOT FOUND IN write_TfIdfMat_to_CSV()");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*private void PopulateIDFmap(){

        for(String article : ReadAndWriteCSVFile.readText){
            if(article.isEmpty()) { continue;}

            String[] arrWords = article.split(" ");
            for(String word : arrWords){

            } // end of inner for each loop
        } // end of outer for each loop
    }*/

}
