package DatasetCollection;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WebCrawler {

    private static final int NUM_THREADS = 15;

    public void MultiThreadedCrawler(String readFromFilePath, int colNumToRead, String fakeOrReal) throws Exception{
        ReadAndWriteCSVFile.readText.clear();
        ReadAndWriteCSVFile.ReadFromCSVFile(readFromFilePath, colNumToRead);

        ExecutorService executorService = Executors.newFixedThreadPool(NUM_THREADS);
        for(String link : ReadAndWriteCSVFile.readText){
            ScrapeWebPages scrapeWebPages = new ScrapeWebPages(link, fakeOrReal);
            executorService.execute(scrapeWebPages);
        }
        executorService.shutdown();
        while(!executorService.isTerminated()){}

        ReadAndWriteCSVFile.readText.clear(); // clear the list
    }
}
