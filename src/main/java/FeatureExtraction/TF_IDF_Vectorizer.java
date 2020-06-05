package FeatureExtraction;

import DatasetCollection.ReadAndWriteCSVFile;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TF_IDF_Vectorizer {

    private final static int NUMTHREADS = 15;

    public static List<Document_Info> List_Doc_info = Collections.synchronizedList(new ArrayList<>());  // experiment

    private static HashMap<String, Double> idfHashmap = new HashMap<>(); // stores unique words and the corresponding IDF score.

    private SortedSet<String> wordList = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);

    private ArrayList<String[]> txt_label = new ArrayList<String[]>();

    //private double[][] tfidfMat;
    private BigDecimal[][] tfidfMat; // experiment

    public TF_IDF_Vectorizer(String csvFileToRead, int label, int text, String saveto) throws Exception {
        ReadAndWriteCSVFile.readText.clear();

        txt_label = ReadAndWriteCSVFile.readMultipleText(csvFileToRead, label, text);

        //long startTime = System.nanoTime(); // debug

        MultithreadedTfCalc();

        /*long endTime   = System.nanoTime();
        long totalTime = endTime - startTime;
        System.out.println(totalTime);*/

        PopulateWordList();
        CalculateIdf();
        TfIdfCalculator();
        write_TfIdfMat_to_CSV(saveto);
    }

    // Create threads for each document to calculate its term frequency
    private void MultithreadedTfCalc(){
        ExecutorService executorService = Executors.newFixedThreadPool(NUMTHREADS);
        for(String[] text : txt_label){
            DocumentTF documentTF = new DocumentTF(text[0], text[1]);
            executorService.execute(documentTF);
        } // end of for each loop
        executorService.shutdown();
        while (!executorService.isTerminated()){} // wait for it to terminate
    }

    // Add all the unique words to the word list
    private void PopulateWordList(){
        synchronized (List_Doc_info){
            for(Document_Info docInfo : List_Doc_info){
                Set<String> stringArrayList = docInfo.getTf().keySet();
                for (String str : stringArrayList) {
                    if(!wordList.contains(str)) wordList.add(str);
                } // end of inner for each loop
            }
        }
    }

    // Calculating and storing IDF for each word
    private void CalculateIdf(){
        int numofDocs = List_Doc_info.size();
        double wordCnt;
        for (String word : wordList) {
            wordCnt = 0;
            for(int k = 0; k < numofDocs; k++){
                if(List_Doc_info.get(k).getTf().containsKey(word)){
                    wordCnt++;
                } // end of if
            } // end of for loop
            double idfCalc = Math.log(numofDocs / wordCnt);
            idfHashmap.put(word, idfCalc);
        } // end of for each loop
    }

    private void TfIdfCalculator(){
        //tfidfMat = new double[List_Doc_info.size()][wordList.size()];
        tfidfMat = new BigDecimal[List_Doc_info.size()][wordList.size()]; // experiment

        int intdoc = 0;

        for(Document_Info docinfo : List_Doc_info){
            int intword = 0;
            for(String word : wordList){
                if(docinfo.getTf().containsKey(word)){
                    double tf = docinfo.getTf().get(word);
                    double idf = idfHashmap.get(word);
                    tfidfMat[intdoc][intword] = BigDecimal.valueOf(tf * idf);
                }else tfidfMat[intdoc][intword] = BigDecimal.valueOf(0);
                intword++;
            } // end of inner loop
            intdoc++;
        } // end of outer loop
    }

    private void write_TfIdfMat_to_CSV(String fileLoc){

        System.out.println("idfmap size: "+ idfHashmap.size());
        System.out.println("wordlist size: "+ wordList.size());

        File fl = new File(fileLoc+".csv");

        try(FileWriter fileWriter = new FileWriter(fl, true)){

            // writing labels of each word
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(" ");
            stringBuilder.append(",");
            for (String word : wordList) {
                stringBuilder.append(word);
                stringBuilder.append(",");
            }
            String toWriteStr = stringBuilder.toString().replaceAll(",$", "\n");
            fileWriter.write(toWriteStr);

            // writing the label and tfidf value
            for(int d = 0; d < List_Doc_info.size(); d++){
                StringBuilder strbldr = new StringBuilder();
                strbldr.append(List_Doc_info.get(d).getLabel());
                strbldr.append(",");
                for(int w = 0; w < wordList.size(); w++){
                    strbldr.append(tfidfMat[d][w]);
                    strbldr.append(",");
                } // end of inner loop
                String str = strbldr.toString().replaceAll(",$", "\n");
                fileWriter.write(str);
            } // end of outer loop

            fileWriter.flush();

        }catch (FileNotFoundException ex){
            System.err.println("FILE NOT FOUND IN write_TfIdfMat_to_CSV()");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
