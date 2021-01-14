package task05.version02;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//添加或编辑
public class AddAndEditPane extends BorderPane {
    // 保存通过编辑进入的用户信息
    private String myName;
    private static List<String> myPhones;
    private String myEmail;
    private String myMark;

    private static TextField nameText;
    private static List<TextField> phoneText;
    private static TextField emailText;
    private static TextField markText;

    private int type;

    public AddAndEditPane(String title,BorderPane borderPane, TelephoneBook telephoneBook,int type,int index){
        if(type == 2){
            // 填入信息
            myName = telephoneBook.getName();
            myEmail = telephoneBook.getEmail();
            myMark = telephoneBook.getMark();
            myPhones = new ArrayList<>();

            for (int i = 0; i < telephoneBook.getTelephone().length; i++){
                myPhones.add(telephoneBook.getTelephone()[i]);
            }
        }

        this.type = type;
        this.setTop(getBorderTop(title,borderPane,telephoneBook,index));
        this.setCenter(getBorderBody());
    }

    public HBox getBorderTop(String _title,BorderPane borderPane,TelephoneBook telephoneBook,int index){
        HBox hBox = new HBox();
        Label back = new Label("返回");
        Label sure = new Label("确定");
        Label title = new Label(_title);

        back.setFont(new Font(20));
        sure.setFont(new Font(20));
        title.setFont(new Font(24));
        back.setTextFill(Color.WHITE);
        sure.setTextFill(Color.WHITE);
        title.setTextFill(Color.WHITE);

        hBox.getChildren().addAll(back,title,sure);
        hBox.setPadding(new Insets(20,0,20,0));
        hBox.setSpacing(40);
        hBox.setStyle("-fx-background-color: #3D5A80");
        hBox.setAlignment(Pos.CENTER);

        // 根据title来确定返回的页面
        back.setOnMouseClicked(event -> {
            action(_title,borderPane,telephoneBook,"back",index);
        });

        // 提交编辑(添加)后的信息,同样根据title属性来确定返回的页面
        sure.setOnMouseClicked(event -> {
            action(_title,borderPane,telephoneBook,"sure",index);
        });

        // 返回
        back.setOnMouseClicked(event -> {
            borderPane.getChildren().clear();
            borderPane.setTop(TelephoneBookListPane.getBorderTop(borderPane));
            ScrollPane scrollPane = TelephoneBookListPane.getBorderBody(borderPane);
            borderPane.setCenter(scrollPane);
            borderPane.setBottom(TelephoneBookListPane.getBorderBottom(borderPane));
            borderPane.setLeft(TelephoneBookListPane.getSideBar(scrollPane));
        });
        return hBox;
    }

  //页面跳转详情
    public static void action(String _title,BorderPane borderPane,TelephoneBook telephoneBook,String type,int index){
        if(_title.equals("编辑联系人")){
            TelephoneBook telephoneBook1 = getAddInfo(1);
            // 如果信息是空,则需要重新输入，将不会跳转页面
            if(telephoneBook1 != null){
                borderPane.getChildren().clear();
                // 点击确定重置信息
                if(type.equals("sure")){
                    TelephoneBookListPane.getList().set(index,getAddInfo(2));
                    borderPane.setCenter(DetailInformation.getBorderBody(getAddInfo(2)));
                }else{
                    borderPane.setCenter(DetailInformation.getBorderBody(telephoneBook));
                }
                borderPane.setTop(DetailInformation.getBorderTop(telephoneBook.getName(),borderPane));
                borderPane.setBottom(DetailInformation.getBorderBottom(borderPane,telephoneBook,index));
            }
        }else{
            TelephoneBook telephoneBook1 = getAddInfo(1);
            if(telephoneBook1 != null){
                // 添加数据 跳转页面前增加数据
                if(type.equals("sure")){
                    TelephoneBookListPane.getList().add(telephoneBook1);
                }
                borderPane.getChildren().clear();
                borderPane.setTop(TelephoneBookListPane.getBorderTop(borderPane));
                ScrollPane scrollPane = TelephoneBookListPane.getBorderBody(borderPane);
                borderPane.setCenter(scrollPane);
                borderPane.setLeft(TelephoneBookListPane.getSideBar(scrollPane));
                borderPane.setBottom(TelephoneBookListPane.getBorderBottom(borderPane));
            }
        }

    }

    //编辑主体
    public VBox getBorderBody(){
        VBox outBox = new VBox();
        VBox topBox = new VBox();
        VBox bottomBox = new VBox();

        Label name = new Label("姓名:");
        nameText = new TextField();
        Label phone = new Label("手机:");
        // 初始化分配内存
        phoneText = new ArrayList<>();
        // 创建第一个元素
        phoneText.add(new TextField());
        Label addMore = new Label("添加手机");
        topBox.getChildren().addAll(getHRecord(name,nameText),getHRecord(phone,phoneText.get(0)));
        topBox.setStyle("-fx-background-color:#E0FBFC");


        Label email = new Label("邮件:");
        emailText = new TextField();
        Label mark = new Label("备注:");
        markText = new TextField();
        bottomBox.getChildren().addAll(getAdd(addMore),getHRecord(email,emailText),getHRecord(mark,markText));
        bottomBox.setStyle("-fx-background-color:#E0FBFC");

        if(type == 2){  // 编辑
            nameText.setText(myName);
            emailText.setText(myEmail);
            markText.setText(myMark);
            phoneText.get(0).setText(myPhones.get(0));
            // 遍历myPhones，填入所有电话
            if(myPhones.size() > 1){
                for(int i = 1; i < myPhones.size(); i++){
                    TextField textField = new TextField(myPhones.get(i));
                    phoneText.add(textField);
                    topBox.getChildren().add(getHRecord(new Label("手机:"),textField));
                }
            }
        }

        outBox.getChildren().addAll(topBox,bottomBox);

        // 点击添加更多
        addMore.setOnMouseClicked(event -> {
            TextField textField = new TextField();
            topBox.getChildren().add(getHRecord(new Label("手机:"),textField));
            phoneText.add(textField);
        });

        return outBox;
    }

    //获取HBox
    public HBox getHRecord(Label label,TextField textField){
        HBox hBox = new HBox();
        hBox = getStyleOfHBox(hBox);
        label.setFont(new Font(20));
        hBox.getChildren().addAll(label,textField);
        return hBox;
    }

    //添加手机添加一个HBox
    public HBox getAdd(Label label){
        HBox hBox = new HBox();
        hBox = getStyleOfHBox(hBox);
        label.setFont(new Font(20));
        hBox.getChildren().add(label);

        return hBox;
    }

    //设置HBox的样式
    public HBox getStyleOfHBox(HBox hBox){
        hBox.setAlignment(Pos.CENTER);
        hBox.setPrefHeight(50);
        hBox.setPadding(new Insets(10,10,10,10));
        hBox.setSpacing(15);
        hBox.setStyle("-fx-border-width: 0 0 1 0;-fx-border-color: #e0e0e0");
        return hBox;
    }

    // 获取填写的信息
    public static TelephoneBook getAddInfo(int type){
        // 正则表达式
        Pattern pName = Pattern.compile("^[\\u4e00-\\u9fa5]{2,6}$");
        Pattern pEmail = Pattern.compile("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$");
        Pattern pPhone = Pattern.compile("^([1]\\d{10}|([\\(（]?0[0-9]{2,3}[）\\)]?[-]?)?([2-9][0-9]{6,7})+(\\-[0-9]{1,4})?)$");
        boolean phoneOk = true;

        String[] phones = new String[phoneText.size()];
        if(type == 2){      //编辑进入
            for(int i = 0; i < phoneText.size(); i++){
                phones[i] = phoneText.get(i).getText();
            }
        }else{
            for(int i = 0; i < phones.length; i++){
                phones[i] = phoneText.get(i).getText();
            }
        }

        // 验证信息正确性
        Matcher mName = pName.matcher(nameText.getText());
        Matcher mEmail = pEmail.matcher(emailText.getText());
        for (int i = 0; i < phones.length; i++) {
            Matcher mPhone = pPhone.matcher(phoneText.get(i).getText());
            if (!mPhone.matches()) {
                phoneOk = false;
            }
        }
        if (phoneOk && mName.matches() && mEmail.matches()) {
            TelephoneBook telephoneBook = new TelephoneBook(nameText.getText(), phones, markText.getText(), emailText.getText());
            return telephoneBook;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("错误!");
            alert.setContentText("请检查是否输入正确的信息！");
            alert.showAndWait();
        }
        return null;
    }
}
