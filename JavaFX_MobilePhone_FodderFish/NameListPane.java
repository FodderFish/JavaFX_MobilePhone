package task05.version02;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;

public class NameListPane extends HBox {

    //渲染主界面的记录
    public NameListPane(TelephoneBook telephoneBook, BorderPane borderPane,int index){
        this.setPrefWidth(295);
        Circle circle = new Circle();
        circle.setRadius(17);
        Label label = new Label(telephoneBook.getName());
        label.setFont(new Font(24));
        this.setPadding(new Insets(10,10,10,10));
        this.setSpacing(10);
        circle.setFill(Color.rgb(241,250,238));

        this.setOnMouseClicked(event -> {
            borderPane.getChildren().clear();
            borderPane.setTop(new DetailInformation(telephoneBook,borderPane,index));
        });

        this.getChildren().addAll(circle,label);
        this.setStyle("-fx-border-width: 0 0 1 0;-fx-border-color: white");
    }
}
