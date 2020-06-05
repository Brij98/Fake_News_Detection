package GuiElements;

import ClassificationModel.NaiveBayesClassification;
import DatasetCollection.ReadAndWriteCSVFile;
import DatasetCollection.ScrapeWebPages;
import DatasetCollection.WebCrawler;
import FeatureExtraction.TF_IDF_Vectorizer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.*;

import java.util.Optional;

public class Controller {

    @FXML
    private Button btn_read_url;

    @FXML
    private Button btn_proctetext;

    @FXML
    private Button btn_classify;

    @FXML
    private TextField txt_url;

    @FXML
    private TextArea txt_extracted;

    @FXML
    private TextArea txt_proc;

    @FXML
    private TextArea txt_result;

    private String urlStr;
    private String inputStr;
    private String procStr;
    private String resutlStr;

    private NaiveBayesClassification classification = new NaiveBayesClassification("D:/TfIdf.csv");

    public void clear_all(ActionEvent event){
        txt_url.clear();
        txt_extracted.clear();
        txt_proc.clear();
        txt_result.clear();
        this.urlStr = "";
        this.inputStr = "";
        this.procStr = "";
        this.resutlStr = "";
    }

    public void extract_data_from_url(ActionEvent event){
        if(!txt_url.getText().trim().equals("")){

            try{
                ScrapeWebPages scrapeWebPages =  new ScrapeWebPages(txt_url.getText(), "NULL");
                inputStr = scrapeWebPages.WebScraper();
                if (!inputStr.isEmpty()){
                    txt_extracted.setText(inputStr);
                    txt_extracted.setWrapText(true);
                }else{
                    Optional<ButtonType> alert = new Alert(Alert.AlertType.ERROR, "Error occurred " +
                            "fetching the data. Check the link URL you entered").showAndWait();
                }

            }catch (Exception ex){
                System.out.print("before throwing");
                Optional<ButtonType> alert = new Alert(Alert.AlertType.ERROR, "Error occurred " +
                        "fetching the data").showAndWait();
            }
        }else {
            Optional<ButtonType> alert = new Alert(Alert.AlertType.ERROR, "Please enter a URL to Scrape")
                    .showAndWait();
        }
    }

    public void process_input_data(ActionEvent event){
        System.out.println("process_input_data clicked");
        if(!inputStr.isEmpty()){
            procStr = ReadAndWriteCSVFile.ProcessString(inputStr);
            txt_proc.setWrapText(true);
            txt_proc.setText(procStr);
        }else{
            Optional<ButtonType> alert = new Alert(Alert.AlertType.ERROR, "No content found to process")
                    .showAndWait();
        }
    }

    public void classify_processed_data(ActionEvent event){

        if(!procStr.isEmpty()){
            resutlStr = classification.predictRealOrFake(procStr);
            txt_result.setWrapText(true);
            txt_result.setText(resutlStr);
        }else{
            Optional<ButtonType> alert = new Alert(Alert.AlertType.ERROR, "Please process text before classification")
                    .showAndWait();
        }


    }

    public void display_help(ActionEvent event){
        String strHelp = "Fake News Classifier Help Page: "+ "\n\n"+
                "1) Enter the news URL to verify " + "\n\n" +
                "2) Select the Read Content Button to load the contents of the webpage " + "\n\n"+
                "3) Select the Process the Text Button to process the input text " + "\n\n" +
                "4) Select the Classify Button classify the processed text as fake or real news";

        Optional<ButtonType> alert = new Alert(Alert.AlertType.INFORMATION, strHelp)
                .showAndWait();
    }

    public void train_model(ActionEvent event) throws Exception {
        // crawling for fake news
        WebCrawler webCrawlerFakeNews = new WebCrawler();
        webCrawlerFakeNews.MultiThreadedCrawler("D:/fakenewsnet/BuzzFeed_fake_news_content.csv", 4,  "FAKE");

        //crawling real news
        WebCrawler webCrawlerRealNews = new WebCrawler();
        webCrawlerRealNews.MultiThreadedCrawler("D:/fakenewsnet/BuzzFeed_real_news_content.csv", 4, "REAL");

        ReadAndWriteCSVFile.ShuffleProcessedData();
        ReadAndWriteCSVFile.writeToCSV("D:/fakenewsText01.csv");

        System.out.println("COMPLETED CRAWLING");
        Optional<ButtonType> alert = new Alert(Alert.AlertType.INFORMATION, "Finished Web scraping URLs")
                .showAndWait();

                TF_IDF_Vectorizer tf_idf_vectorizer = new TF_IDF_Vectorizer("D:/fakenewsText01.csv",
                3, 2, "D:/TfIdf");

        System.out.println("COMPLETED CALCULATING TF-IDF");
        Optional<ButtonType> alert2 = new Alert(Alert.AlertType.INFORMATION, "Finished Calculating Tf-Idf")
                .showAndWait();
    }


}
