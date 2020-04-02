package DatasetCollection;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WebCrawler {

    private static final int NUM_THREADS = 15;

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

    public void MultiThreadedCrawler(String readFromFilePath, int colNumToRead, String writeToFilePath, String fakeOrReal) throws Exception{
        ReadFromCSVFile(readFromFilePath, colNumToRead);

        ExecutorService executorService = Executors.newFixedThreadPool(NUM_THREADS);
        for(String link : listLinks){
            ScrapeWebPages scrapeWebPages = new ScrapeWebPages(link);
            executorService.execute(scrapeWebPages);
        }
        executorService.shutdown();
        while(!executorService.isTerminated()){}

        //PrintLinksAndText();
        writeToCSV(writeToFilePath, fakeOrReal);

    }

    public void PrintLinksAndText(){
        System.out.println("Size of : "+ LinksAndText.size()); // debug
        synchronized (LinksAndText){
            Iterator iterator = LinksAndText.iterator();
            while (iterator.hasNext()){
                String[] arrstr = (String[]) iterator.next();
                System.out.println(arrstr[0] + " : "+ arrstr[1]);
            }
        }
    }

    private void writeToCSV(String fileName, String fakeOrReal) throws IOException {
        File outputCSVfile = new File(fileName);

        FileWriter fileWriter = new FileWriter(outputCSVfile, true);
        if(outputCSVfile.length() == 0){
            fileWriter.append("Link");
            fileWriter.append(",");
            fileWriter.append("Text");
            fileWriter.append(",");
            fileWriter.append("Status");
            fileWriter.append("\n");
        }

        synchronized (LinksAndText){
            Iterator iterator = LinksAndText.iterator();
            while (iterator.hasNext()){
                String[] arrstr = (String[]) iterator.next();
                fileWriter.append(convertToCSVFormat(arrstr));
                fileWriter.append(",");
                fileWriter.append(fakeOrReal);
                fileWriter.append("\n");
            }
            fileWriter.flush();
            fileWriter.close();
        }
    }

    private String convertToCSVFormat(String[] arrStr){
        return Stream.of(arrStr).map(this::RemoveSpecialCharacter).collect(Collectors.joining(","));
    }

    private String RemoveSpecialCharacter(String str){
        String replaceChar = str.replaceAll("\\r", " ");
        if (str.contains(",") || str.contains("\"") || str.contains("'")) {
            str = str.replace("\"", "\"\"");
            replaceChar = "\"" + str + "\"";
        }
        return replaceChar;
    }
}
