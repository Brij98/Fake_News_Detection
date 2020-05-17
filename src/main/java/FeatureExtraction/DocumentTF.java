package FeatureExtraction;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DocumentTF implements Runnable{

    // this class calculates term frequency for each article

    private HashMap<String, Integer> WordCountMap; // stores each number of times a word appears
    private HashMap<String, Double> TermFrequencyMap; // stores the calculated term frequency
    private String inputString;

    public DocumentTF(String inputString){
        WordCountMap = new HashMap<>();
        TermFrequencyMap = new HashMap<>();
        this.inputString = inputString;
    }

    @Override
    public void run() {
        if(!inputString.isEmpty()){
            CalculateTF();
            TF_IDF_Vectorizer.DocumentTfList.add(TermFrequencyMap);
        }
    }

    private void CalculateTF(){
        String[] arrWords = inputString.split(" ");
        for(String word : arrWords){
            if (WordCountMap.containsKey(word)) {
                WordCountMap.put(word, WordCountMap.get(word) + 1);
            } else {
                WordCountMap.put(word, 1);
            }
        }

        Iterator tfIterator = WordCountMap.entrySet().iterator();
        while (tfIterator.hasNext()){
            Map.Entry<String, Integer> wordcountpair = (Map.Entry<String, Integer>) tfIterator.next();
            String word = wordcountpair.getKey();
            Double tfval = Double.valueOf(wordcountpair.getValue());
            if(!TermFrequencyMap.containsKey(word)){
                TermFrequencyMap.put(word, tfval/arrWords.length);
            }
        }
    }

}
