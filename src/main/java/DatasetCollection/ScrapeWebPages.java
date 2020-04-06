package DatasetCollection;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class ScrapeWebPages implements Runnable{

    private static final ArrayList<String> unwantedClasses = new ArrayList<String>(Arrays.asList(

            "nav", "navigation", "comment", "banner", "relatedposts", "tags",
            "infobox", "button", "menu", "recent", "social", "twitter", "share", "pagination",
            "references", "links", "footer", "subscribe", "sd-title", "archives", "related", "img",
            "math", "table", "script"

            /*"nav", "navigation", *//*"comment",*//* "banner", "relatedposts", *//*"tags",*//*
            "infobox", *//*"button",*//* "menu", *//*"recent",*//* "social", "twitter", *//*"share",*//* "pagination",
            "references", *//*"links",*//* *//*"footer",*//* "subscribe", "sd-title", "archives", "related", "img",
            "math", "table"*//*, "script"*/
    ));

    private static final ArrayList<String> unwantedTags = new ArrayList<String>(Arrays.asList(
            "img", "math", "table", "nav", "navigation", "script", "aside", "input",
            "footer", "button", "table", "a", "em"
    ));

    private String LinkToProcess;
    private String realOrFake;

    public ScrapeWebPages(String LinkToProcess, String realOrFake){
        this.LinkToProcess = LinkToProcess;
        this.realOrFake = realOrFake;
    }

    @Override
    public void run() {
        try{
            if(!LinkToProcess.isEmpty()){
                String text = WebScraper();
                if(!text.isEmpty()){
                    String[] arrStr = {LinkToProcess, text, realOrFake};
                    ReadAndWriteCSVFile.processedText.add(arrStr);
                }
            }
        }catch (Exception ex){
            System.err.println("ERROR in RUN methode AND LINK IS: " + LinkToProcess);
            ex.printStackTrace();
        }
    }

    public String WebScraper() throws IOException {

        System.out.println("Extracting Data For: "+ LinkToProcess + "\n");

        try{
            Document doc = Jsoup.connect(LinkToProcess)
                    .userAgent("Mozilla")
                    .timeout(3*1000) // 3 seconds
                    .get();

            for(String unwantedtags : unwantedTags){
                doc.select(unwantedtags).remove();
            }

            for(String unwantedclass : unwantedClasses){
                doc.select("[class~=(?i).*" + unwantedclass + "]").remove();
                doc.select("[id~=(?i).*" + unwantedclass + "]").remove();
            }

            Elements elems = null;

            try{
                elems = doc.getElementsByTag("article").first().select("h1, h2, p, pre, ol, ul, content");
            }catch (Exception ex){
                //ex.printStackTrace();
            }

            elems = (elems == null) || (elems.isEmpty()) ?
                    doc.getElementsByTag("body").first().select("h1, h2, p, pre, ol, ul, content") : elems;

            String strContent = "";
            if(elems != null){
                for(Element elem : elems){
                    String txt = elem.text().trim();
                    if(!txt.isEmpty()){
                        strContent += txt + " ";
                    }
                }
            }
            return strContent;
        }catch (Exception ex){
            System.err.println("ERROR in WebScrapper() LINK: " + LinkToProcess);
            ex.printStackTrace();
        }
        return  "";
    }
}

/*
REFERENCE:
    https://blogs.ashrithgn.com/jsoup-example-for-web-scraping/
*/
