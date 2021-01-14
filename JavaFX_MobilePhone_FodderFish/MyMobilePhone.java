package task05.version02;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MyMobilePhone extends Application {

    @Override
    public void start(Stage bigStage) throws Exception {
        TelephoneBookListPane bookListPane = new TelephoneBookListPane();
        bookListPane.setPadding(new Insets(97, 17, 80, 17));
        bookListPane.setStyle("-fx-background-image:url(file:/C:/Users/86173/Desktop/图片/bigmobile.png)");
        Scene scene = new Scene(bookListPane,420,875);
        bigStage.setScene(scene);
        bigStage.initStyle(StageStyle.UNDECORATED);
        bigStage.show();
        bigStage.setResizable(false);
    }
    public static void main(String[] args) {
        Application.launch(args);
    }
}
