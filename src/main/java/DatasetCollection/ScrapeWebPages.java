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
            "nav", "navigation", /*"comment",*/ "banner", "relatedposts", /*"tags",*/
            "infobox", /*"button",*/ "menu", /*"recent",*/ "social", "twitter", /*"share",*/ "pagination",
            "references", /*"links",*/ /*"footer",*/ "subscribe", "sd-title", "archives", "related", "img",
            "math", "table"/*, "script"*/
    ));

    private static final ArrayList<String> unwantedTags = new ArrayList<String>(Arrays.asList(
            "img", "math", "table", "nav", "navigation", "script", "aside", "input",
            "footer", "button", "table", "a", "em"
    ));

    private String LinkToProcess;

    public ScrapeWebPages(String LinkToProcess){
        this.LinkToProcess = LinkToProcess;
    }

    @Override
    public void run() {
        try{
            //System.out.println("Content: "+ WebScraper()); // debug


            String[] arrStr = {LinkToProcess, WebScraper()};

            //System.out.println(arrStr[1]); // debug

            WebCrawler.LinksAndText.add(arrStr);


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
                    .timeout(10*1000) // 10 seconds
                    .get();

            // debug
            /*if(doc.hasText()){
                System.out.println(doc.text());
            }*/

            for(String unwantedtags : unwantedTags){
                doc.select(unwantedtags).remove();
            }

            // debug
            /*if(doc.hasText()){
                System.out.println(doc.text());
            }*/

            for(String unwantedclass : unwantedClasses){
                doc.select("[class~=(?i).*" + unwantedclass + "]").remove();
                doc.select("[id~=(?i).*" + unwantedclass + "]").remove();
            }

            Elements elems = null;
            //debug
/*            if(doc.hasText()){
                System.out.println(doc.text());
            }*/
            try{
                elems = doc.getElementsByTag("body").first().select("h1, h2, p, pre, ol, ul, content");
            }catch (Exception ex){
                ex.printStackTrace();
            }

            /*if((elems == null) || (elems.isEmpty())){
                elems = doc.getElementsByTag("body").first().select("h1, h2, p, pre, ol, ul, content");
            }*/

        /*elems = (elems == null) || (elems.isEmpty()) ?
                doc.getElementsByTag("body").first().select("h1, h2, p, pre, ol, ul, content") : elems;*/

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
        return null;
    }
}

/*
REFERENCE:
    https://blogs.ashrithgn.com/jsoup-example-for-web-scraping/
*/
