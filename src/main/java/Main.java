import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.print.DocFlavor;
import java.io.File;
import java.net.URL;

public class Main extends Application {

    public static void main(String[] args) throws Exception {
        // TODO Auto-generated method stub
        System.out.println("Hello World");
        launch(args);

        // crawling for fake news
/*        WebCrawler webCrawlerFakeNews = new WebCrawler();
        webCrawlerFakeNews.MultiThreadedCrawler("D:/fakenewsnet/BuzzFeed_fake_news_content.csv", 4,  "FAKE");

        //crawling real news
        WebCrawler webCrawlerRealNews = new WebCrawler();
        webCrawlerRealNews.MultiThreadedCrawler("D:/fakenewsnet/BuzzFeed_real_news_content.csv", 4, "REAL");

        ReadAndWriteCSVFile.ShuffleProcessedData();
        ReadAndWriteCSVFile.writeToCSV("D:/fakenewsText01.csv");

        System.out.println("COMPLETED CRAWLING");*/


/*        TF_IDF_Vectorizer tf_idf_vectorizer = new TF_IDF_Vectorizer("D:/fakenewsText01.csv",
                3, 2, "D:/TfIdf");

        System.out.println("COMPLETED CALCULATING TF-IDF");*/

        /*NaiveBayesClassification bayesClassification = new NaiveBayesClassification("D:/TfIdf.csv");
        String str = "already hints nbc news lester holt moderator first presidential debate donald trump hillary clinton fact checking candidates course allow trump say anything pleases without held accountable except hillary clinton make matters worse cable news networks including critical trump offer onscreen fact checks comments made candidates wrong onscreen factchecking help expose inaccurate misleading assertions without interrupting flow debate nearly major cable broadcast networks including univision telemundo said use sort screen fact check graphics thus burden keeping candidates honest likely come holt person stage trump lies average every threeandahalf minutes one think fact checking process paramount honest open debate issues sadly media realizing ratings line rather kowtow donald trump band deplorable surrogates clinton campaign forceful effort hold trump accountable clinton campaign communications director jennifer palmieri telling reporters moderator let lies like come mouth debate go unchallenged give donald trump unfair advantage believe role moderator call lies real time unfortunately trump impending lies already brink checked falsehoods somehow hillary clinton shine debate hopes win double standard qualified woman every run office go beyond unqualified nominee history look slightly better abhorrent hopefully clinton wipe floor trump first debate one back may hard convincing american people one thing election shown us journalism dying already dead";

        System.out.println(bayesClassification.predictRealOrFake(str));*/

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        URL url = new File("src/main/java/gui.fxml").toURI().toURL();
        Parent root = FXMLLoader.load(url);
        Scene scene = new Scene(root);
        primaryStage.setTitle("Fake News Classifier");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
