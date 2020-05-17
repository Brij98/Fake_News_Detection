import DatasetCollection.ReadAndWriteCSVFile;
import DatasetCollection.WebCrawler;
import FeatureExtraction.TF_IDF_Vectorizer;

import java.io.IOException;
import java.util.Collections;

public class Main {

    public static void main(String[] args) throws Exception {
        // TODO Auto-generated method stub
        System.out.println("Hello World");

        // crawling for fake news
        /*WebCrawler webCrawlerFakeNews = new WebCrawler();
        webCrawlerFakeNews.MultiThreadedCrawler("D:/fakenewsnet/BuzzFeed_fake_news_content.csv", 4,  "FAKE");*/

        //crawling real news
        /*WebCrawler webCrawlerRealNews = new WebCrawler();
        webCrawlerRealNews.MultiThreadedCrawler("D:/fakenewsnet/BuzzFeed_real_news_content.csv", 4, "REAL");*/

        /*ReadAndWriteCSVFile.ShuffleProcessedData();
        ReadAndWriteCSVFile.writeToCSV("D:/fakenewsText01.csv");*/

        //System.out.println("COMPLETED CRAWLING");


        TF_IDF_Vectorizer tf_idf_vectorizer = new TF_IDF_Vectorizer("D:/fakenewsText01.csv",
                2, "D:/TfIdf");

        System.out.println("COMPLETED CALCULATING TF-IDF");

    }
}
