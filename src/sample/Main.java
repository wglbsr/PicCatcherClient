package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.List;


public class Main extends Application {
    Button exitBtn;
    TextField textFieldURL;
    TextField textFieldPath;
    void setOnTop(){

    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("数据");
        primaryStage.setAlwaysOnTop(true);
        Scene scene = new Scene(root, 800, 500);
        exitBtn = (Button) scene.lookup("#btnDownload");
        textFieldURL = (TextField) scene.lookup("#labelUrl");
        textFieldPath = (TextField) scene.lookup("#labelPath");
        exitBtn.setOnMousePressed(event -> {

            String url = textFieldURL.getText();

            PicCatcher dataCatcher = new PicCatcher();
            dataCatcher.start();

            String path = textFieldPath.getText();
            Document document = null;
            try {
                document = Jsoup.connect(url).get();
                if (document != null) {
                    List<Element> element = document.getElementsByClass("zoom");
                    element.removeIf((e) -> !e.hasAttr("aid"));
                    /* https://img.520mojing.com/data/attachment/forum/202009/15/111004vl06qqqjlvlcmm2n.jpg */
                    for (Element img : element) {
                        String fileUrl = img.attr("src");
                        String fileName = Tool.findStrByRegEx(fileUrl, "(?<=\\/)[a-zA-Z0-9]+.jpg$");
                        String directory = Tool.findStrByRegEx(fileUrl, "(?<=\\/)[0-9]{6}(?=\\/)");
                        String subDir = Tool.findStrByRegEx(fileUrl, "(?<=\\/)[0-9]{2}(?=\\/)");
                        if (Tool.validStr(fileName)) {
                            new Thread(() -> {
                                try {
                                    NetTool.downLoadFromUrl(fileUrl, fileName, path + File.separator + directory + File.separator + subDir + File.separator, 10000);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }).start();
                        }
                    }
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
        SystemClipboardMonitor temp = new SystemClipboardMonitor();
        new JFrame().setVisible(true);
    }
}
