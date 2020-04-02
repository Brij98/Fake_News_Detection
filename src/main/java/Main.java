import DatasetCollection.WebCrawler;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws Exception {
        // TODO Auto-generated method stub
        System.out.println("Hello World");

      /*  WebCrawler webCrawler = new WebCrawler();
        webCrawler.ReadFromCSVFile("D:/fakenewsnet/BuzzFeed_fake_news_content.csv", 4);
        try{
            System.out.println(webCrawler.WebScraper());
        }catch (IOException ex){
            ex.printStackTrace();
        }*/

      WebCrawler webCrawler = new WebCrawler();
      webCrawler.MultiThreadedCrawler("D:/fakenewsLinks.txt", 1, "D:/fakenewsText.csv", "FAKE");
      //Thread.sleep(30000);

      //webCrawler.PrintLinksAndText();
    }
}
