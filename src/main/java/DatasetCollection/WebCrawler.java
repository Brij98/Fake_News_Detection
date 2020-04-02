package DatasetCollection;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;

public class WebCrawler {

    private ArrayList<String> listLinks = new ArrayList<>();

   // public static List<String[]> LinksAndText = new ArrayList<String[]>();
    public static List<String[]> LinksAndText = Collections.synchronizedList(new ArrayList<String[]>());

    /*
    * Read URLs from CSV file from a certain Column
    * */
    public void ReadFromCSVFile(String FileName, int ColNum) throws Exception{
        //File flcsv = new File("../Datasets/"+ FileName);
        File flcsv = new File(FileName); // Test purpose

        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(flcsv))) {
            String strLn;
            String header = bufferedReader.readLine();
            while((strLn = bufferedReader.readLine()) != null){
                String[] tokens = strLn.split(",");
                listLinks.add(tokens[ColNum -1]);
            }
        }  catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*public String WebScraper(*//*String Link*//*) throws IOException{

        System.out.println("Extracting Data For: "+ listLinks.get(3) + "\n\n");

        Document doc = Jsoup.connect(listLinks.get(3))
                            .timeout(10*1000) // 10 seconds
                            .get();

        for(String unwantedtags : ScrapeWebPages.unwantedTags){
            doc.select(unwantedtags).remove();
        }

        for(String unwantedclass : ScrapeWebPages.unwantedClasses){
            doc.select("[class~=(?i).*" + unwantedclass + "]").remove();
            doc.select("[id~=(?i).*" + unwantedclass + "]").remove();
        }

        Elements elems = null;

        try{
            elems = doc.getElementsByTag("article").first().select("h1, h2, p, pre, ol, ul, content");
        }catch (Exception ex){}

        elems = (elems == null) || (elems.isEmpty()) ?
                doc.getElementsByTag("body").first().select("h1, h2, p, pre, ol, ul, content") : elems;

        String strContent = "";
        if(elems != null){
            for(Element elem : elems){
                String txt = elem.text().trim();
                if(!txt.isEmpty()){
                    strContent += txt + "\n";
                }
            }
        }
        return strContent;
    }*/

    public void MultiThreadedCrawler(String readFromFilePath, int colNumToRead/*, String writeToFilePath*/) throws Exception{
        ReadFromCSVFile(readFromFilePath, colNumToRead);
        /*for(String link : listLinks){
            Thread thread = new Thread(new ScrapeWebPages(link));
            thread.start();
        }*/

        // Experiment
        WebCrawler webCrawler = new WebCrawler();
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for(String link : listLinks){
            ScrapeWebPages scrapeWebPages = new ScrapeWebPages(link);
            executorService.execute(scrapeWebPages);
            //Thread thread = new Thread(new ScrapeWebPages(link));
            //thread.start();
        }
        executorService.shutdown();


   /*     ScrapeWebPages scrapeWebPages = new ScrapeWebPages("https://politics.theonion.com/trump-quietly-checks-with-aides-to-make-sure-he-d-be-in-1842399427");
        Thread thread = new Thread(scrapeWebPages);
        thread.start();
        /thread.join();*/

        /*while(LinksAndText.size() != listLinks.size()){

        }*/

        PrintLinksAndText();

    }

    public void PrintLinksAndText(){
        System.out.println("Size of : "+ LinksAndText.size()); // debug
        synchronized (LinksAndText){
            /*for(String[] arrstr : LinksAndText){
                System.out.println(arrstr[0] + " : "+ arrstr[1]);
            }*/
            Iterator iterator = LinksAndText.iterator();
            while (iterator.hasNext()){
                String[] arrstr = (String[]) iterator.next();
                System.out.println(arrstr[0] + " : "+ arrstr[1]);
            }
        }
    }

    private void writeToCSV(String path, String txtToWrite){


    }
}
