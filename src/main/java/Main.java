import DatasetCollection.ReadAndWriteCSVFile;
import DatasetCollection.WebCrawler;

import java.io.IOException;
import java.util.Collections;

public class Main {

    public static void main(String[] args) throws Exception {
        // TODO Auto-generated method stub
        System.out.println("Hello World");

        // crawling for fake news
        WebCrawler webCrawlerFakeNews = new WebCrawler();
        webCrawlerFakeNews.MultiThreadedCrawler("D:/fakenewsnet/BuzzFeed_fake_news_content.csv", 4,  "FAKE");

        //crawling real news
        WebCrawler webCrawlerRealNews = new WebCrawler();
        webCrawlerRealNews.MultiThreadedCrawler("D:/fakenewsnet/BuzzFeed_real_news_content.csv", 4, "REAL");

        ReadAndWriteCSVFile.ShuffleProcessedData();
        ReadAndWriteCSVFile.writeToCSV("D:/fakenewsText01.csv");

        System.out.println("COMPLETED CRAWLING");
    }
}
