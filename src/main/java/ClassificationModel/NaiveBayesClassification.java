package ClassificationModel;

import DatasetCollection.ReadAndWriteCSVFile;

import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NaiveBayesClassification {

    private static List<String> VocabList = new ArrayList<>();
    private List<String[]> TfIdfVector = new ArrayList<>();

    private String filename;

    private double totalTfIdfReal;
    private double totalTfIdfFake;

    private int totalReal;
    private int totalFake;

    public NaiveBayesClassification(String filename){
        this.filename = filename;
        load_tfidf_vector();

        this.totalTfIdfReal = total_TfIdf_Count_Real();
        this.totalTfIdfFake = total_TfIdf_Count_Fake();
    }

    private void load_tfidf_vector(){
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(filename))){
            String row;
            if ((row = bufferedReader.readLine()) != null){
                String[] arrStr  = row.split(",");
                VocabList = Arrays.asList(arrStr);
                while((row = bufferedReader.readLine()) != null){
                    String[] strarr = row.split(",");
                    TfIdfVector.add(strarr);
                }
            }
        }catch (Exception ex){
            System.err.println("FILE NOT FOUND in load_tfidf_vector in NaiveBayesClassification");
            ex.printStackTrace();
        }
    }

    // Calculates the total tfidf weights of the words belonging to the real news class
    private double total_TfIdf_Count_Real(){
        double totTfIdf = 0.0;
        for (String[] arrStr: TfIdfVector) {
            double dbltot = 0.0;
            if(arrStr[0].toLowerCase().equals("real")){
                totalReal++;
                for(int i = 1; i < VocabList.size(); i++){
                    dbltot += Double.parseDouble(arrStr[i]);
                    /*if(Double.valueOf(arrStr[i]) > 0.0)
                    dbltot += Double.valueOf(arrStr[i]);*/
                } // end of inner loop
                totTfIdf += dbltot;
            } // end of if statement
        }
        return totTfIdf;
    }

    // Calculates the total tfidf weights of the words belonging to the fake news class
    private double total_TfIdf_Count_Fake(){
        double totTfIdf = 0.0;
        for (String[] arrStr: TfIdfVector) {
            double dbltot = 0.0;
            if(arrStr[0].toLowerCase().equals("fake")){
                totalFake++;
                for(int i = 1; i < VocabList.size(); i++){
                    dbltot += Double.parseDouble(arrStr[i]);
                    /*if(Double.valueOf(arrStr[i]) > 0.0)
                        dbltot += Double.valueOf(arrStr[i]);*/
                } // end of inner loop
                totTfIdf += dbltot;
            } // end of if statement
        }
        return totTfIdf;
    }


    // Calculates the total tfidf weights score of a word in real news class
    private double word_TfIdf_Count_Real(String word){
        int index = VocabList.indexOf(word);
        double dblcntr = 0.0;
        if(index > -1){
            for (String[] arrstr : TfIdfVector) {
                if(arrstr[0].toLowerCase().equals("real") && Double.parseDouble(arrstr[index]) > 0){
                    dblcntr += Double.parseDouble(arrstr[index]);
                }
            }
        }
        return dblcntr + 1;
    }

    // Calculates the total tfidf weights score of a word in fake news class
    private double word_TfIdf_Count_Fake(String word){
        int index = VocabList.indexOf(word);
        double dblcntr = 0.0;
        if(index > -1){
            for (String[] arrstr : TfIdfVector) {
                if(arrstr[0].toLowerCase().equals("fake") && Double.parseDouble(arrstr[index]) > 0){
                    dblcntr += Double.parseDouble(arrstr[index]);
                }
            }
        }
        return dblcntr + 1;
    }


    public String predictRealOrFake(String text){
        String procStr = ReadAndWriteCSVFile.ProcessString(text);
        String[] arrWords = procStr.split(" ");

        BigDecimal bigDecimal1 =  new BigDecimal("1.0");
        BigDecimal bigDecimal2 = new BigDecimal("1.0");


        for (String wrd : arrWords) {

            BigDecimal bigDecimalR = new BigDecimal(word_TfIdf_Count_Real(wrd) / (totalTfIdfReal + (VocabList.size() -1)));

            bigDecimal1 = bigDecimal1.multiply(bigDecimalR);
        }

        for (String wrd : arrWords) {

            BigDecimal bigDecimalF = new BigDecimal(word_TfIdf_Count_Fake(wrd) / (totalTfIdfFake + (VocabList.size() -1)));

            bigDecimal2 = bigDecimal2.multiply(bigDecimalF);

        }

        System.out.println(VocabList.size());
        System.out.println(TfIdfVector.get(50).length);


        System.out.println(totalTfIdfReal); // debug
        System.out.println(totalTfIdfFake);  // debug
        System.out.println(bigDecimal1);  // debug
        System.out.println(bigDecimal2); // debug

        double ratio1 = totalReal / TfIdfVector.size();
        double ratio2 = totalFake / TfIdfVector.size();

/*        bigDecimal1 = bigDecimal1.multiply(BigDecimal.valueOf(ratio1));
        bigDecimal2 = bigDecimal2.multiply(BigDecimal.valueOf(ratio2));*/
        String strresult = "";

        if(bigDecimal2.compareTo(bigDecimal1) == -1){
            strresult = "REAL NEWS";
        }else {
            strresult = "FAKE NEWS";
        }

        MathContext mathContext =  new MathContext(5);

        String strtoret = "Total Tf-Idf of Real News: " + totalTfIdfReal + " \n" +
                "Total Tf-Idf of Fake News: " + totalTfIdfFake + " \n" +
                "Probability of being Real: " + bigDecimal1.round(mathContext) + "\n"+
                "Probability of being Fake: " + bigDecimal2.round(mathContext) + "\n" +
                "Final Result: " + strresult;

        return strtoret;
    }
}
