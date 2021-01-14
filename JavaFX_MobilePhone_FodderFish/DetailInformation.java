package task05.version02;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.Optional;

public class DetailInformation extends BorderPane {

    public DetailInformation(TelephoneBook telephoneBook, BorderPane borderPane, int index){
        this.setTop(getBorderTop(telephoneBook.getName(),borderPane));
        this.setCenter(getBorderBody(telephoneBook));
        this.setBottom(getBorderBottom(borderPane,telephoneBook,index));
    }

    public static VBox getBorderTop(String name, BorderPane borderPane){
        VBox vBox = new VBox();
        HBox back = new HBox();
        HBox hBox = new HBox();
        hBox.setPrefHeight(150);
        hBox.setAlignment(Pos.BOTTOM_LEFT);
        back.setAlignment(Pos.CENTER_LEFT);
        hBox.setPadding(new Insets(0,0,25,15));
        back.setPadding(new Insets(25,0,25,15));

        // 返回
        Label backTo = new Label("返回");
        Label label = new Label(name);

        // 设置字体和背景
        backTo.setFont(new Font(24));
        label.setFont(new Font(24));
        backTo.setTextFill(Color.WHITE);
        label.setTextFill(Color.WHITE);
        hBox.setStyle("-fx-background-color: #3D5A80");
        back.setStyle("-fx-background-color: #3D5A80");


        hBox.getChildren().add(label);
        back.getChildren().add(backTo);
        vBox.getChildren().addAll(back,hBox);

        // 返回
        backTo.setOnMouseClicked(event -> {
            borderPane.getChildren().clear();
            borderPane.setTop(TelephoneBookListPane.getBorderTop(borderPane));
            ScrollPane scrollPane = TelephoneBookListPane.getBorderBody(borderPane);
            borderPane.setCenter(scrollPane);
            borderPane.setBottom(TelephoneBookListPane.getBorderBottom(borderPane));
            borderPane.setLeft(TelephoneBookListPane.getSideBar(scrollPane));
        });

        return vBox;
    }

    //获取主体
    public static VBox getBorderBody(TelephoneBook telephoneBook){
        VBox vBox = new VBox();
        String[] phones = telephoneBook.getTelephone();
        if(phones.length == 1){
            vBox.getChildren().add(getRecord("电话",telephoneBook.getTelephone()[0]));
        }else{
            for(int i = 0; i < phones.length; i++){
                vBox.getChildren().add(getRecord("电话" + (i + 1),telephoneBook.getTelephone()[i]));
            }
        }
        vBox.getChildren().addAll(getRecord("邮件",telephoneBook.getEmail()),getRecord("备注",telephoneBook.getMark()));
        vBox.setPrefHeight(400);
        return vBox;
    }

    //获取单条记录
    public static HBox getRecord(String label, String mark){
        HBox hBox = new HBox();
        Label myLabel = new Label(label + ": ");

        myLabel.setFont(new Font(20));

        Text myMark = new Text(mark);
        myMark.setFont(new Font(20));

        hBox.getChildren().addAll(myLabel,myMark);
        hBox.setPadding(new Insets(10,10,10,10));
        hBox.setSpacing(10);
        hBox.setStyle("-fx-border-width: 0 0 1 0;-fx-border-color: #e0e0e0");
        return hBox;
    }

    //获取底部操作
    public static HBox getBorderBottom(BorderPane borderPane,TelephoneBook telephoneBook,int index){
        HBox hBox = new HBox();
        Label edit = new Label("编辑联系人");
        Label delete = new Label("删除联系人");

        edit.setFont(new Font(18));
        delete.setFont(new Font(18));

        hBox.setPadding(new Insets(20,40,20,40));
        hBox.setSpacing(100);

        hBox.getChildren().addAll(edit,delete);
        hBox.setStyle("-fx-background-color: #98C1D9");

        // 编辑
        edit.setOnMouseClicked(event -> {
            borderPane.getChildren().clear();
            TelephoneBook book = TelephoneBookListPane.getList().get(index);
            borderPane.setCenter(new AddAndEditPane("编辑联系人",borderPane,book,2,index));
        });

        // 删除
        delete.setOnMouseClicked(event -> {
            Alert _alert = new Alert(Alert.AlertType.CONFIRMATION,"",new ButtonType("否", ButtonBar.ButtonData.NO),
                    new ButtonType("是", ButtonBar.ButtonData.YES));
            //设置窗口的标题
            _alert.setTitle("提示");
            _alert.setHeaderText("您将删除该联系人，是否继续?");
            //showAndWait() 将在对话框消失以前不会执行之后的代码
            Optional<ButtonType> _buttonType = _alert.showAndWait();
            //根据点击结果返回
            if(_buttonType.get().getButtonData().equals(ButtonBar.ButtonData.YES)){
                borderPane.getChildren().clear();
                TelephoneBookListPane.getList().remove(index);
                borderPane.setTop(TelephoneBookListPane.getBorderTop(borderPane));
                ScrollPane scrollPane = TelephoneBookListPane.getBorderBody(borderPane);
                borderPane.setCenter(scrollPane);
                borderPane.setBottom(TelephoneBookListPane.getBorderBottom(borderPane));
                borderPane.setLeft(TelephoneBookListPane.getSideBar(scrollPane));
            }
        });

        return hBox;
    }
}
