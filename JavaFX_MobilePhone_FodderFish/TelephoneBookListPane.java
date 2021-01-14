package task05.version02;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TelephoneBookListPane extends BorderPane {
    // 搜索框
    private static TextField search;
    // 搜索按钮
    private static Button searchButton;
    // 增加按钮
    private static Button addButton;
    // 主界面的主体滚动窗口
    public static ScrollPane scrollPane;
    // 右侧导航条
    private static char[] sideBar = new char[26];
    // 存放记录的数组
    private static TelephoneBookList[] books = new TelephoneBookList[26];
    // 存放记录的list表
    private static List<TelephoneBook> list = new ArrayList<>();


    //构造方法；
    public TelephoneBookListPane() {
        getInitInfo();
        scrollPane = getBorderBody(this);
        this.setTop(getBorderTop(this));
        this.setCenter(scrollPane);
        this.setBottom(getBorderBottom(this));
        this.setLeft(getSideBar(scrollPane));
    }

    //头部的搜索框;
    public static HBox getBorderTop(BorderPane borderPane) {
        HBox hBox = new HBox();
        search = new TextField();
        search.setPrefHeight(40);
        search.setPrefWidth(250);
        search.setPromptText("\uD83D\uDD0D搜索联系人");

        searchButton = new Button("搜索");
        searchButton.setPrefHeight(40);
        searchButton.setPrefWidth(70);
        searchButton.setStyle("-fx-background-color:#98C1D9;");

        addButton = new Button("增加");
        addButton.setPrefHeight(40);
        addButton.setPrefWidth(70);
        addButton.setStyle("-fx-background-color:#E0FBFC;");

       // hBox.setPadding(new Insets(0, 0, 0, 0));
        //hBox.setSpacing(5);
        hBox.setAlignment(Pos.CENTER_LEFT);       //消除布局中的空隙;
        hBox.getChildren().addAll(addButton,search, searchButton);

        // 点击查找按钮绑定事件
        searchButton.setOnAction(event -> {
            borderPane.setCenter(getSearchBorderBody(borderPane));
            ScrollPane sc = TelephoneBookListPane.getBorderBody(borderPane);
            if (!search.getText().equals("")) {
                borderPane.setRight(null);
            } else {
                borderPane.setCenter(sc);
                borderPane.setLeft(getSideBar(sc));
            }
        });
        addButton.setOnMouseClicked(event -> {
            borderPane.getChildren().clear();
            borderPane.setCenter(new AddAndEditPane("添加联系人", borderPane, null, 1, 0));
        });
        return hBox;
    }

    //主体部分;
    public static ScrollPane getBorderBody(BorderPane borderPane) {
        initArray();
        ScrollPane scrollPane = new ScrollPane();
        VBox vBox = new VBox();
        vBox.setStyle("-fx-background-color:#A8DADC");
        vBox.setPadding(new Insets(0, 35, 0, 0));

        int index = 0;
        // 将TelephoneBookList数组中的数据渲染到界面
        for (int i = 0; i < books.length; i++) {
            // 单条链
            List<TelephoneBook> p = books[i].getList();
            if (p.size() > 0) {
                // 添加一个小标题
                vBox.getChildren().add(getBlockName(i));
            }
            for (int j = 0; j < p.size(); j++) {
                NameListPane nameListPane = new NameListPane(p.get(j), borderPane, index);
                index++;
                vBox.getChildren().add(nameListPane);
            }
        }

        scrollPane.setPannable(true);
        //scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setContent(vBox);
        scrollPane.setStyle("-fx-background-insets: 0;");//背景内边框距离外边框距离

        return scrollPane;
    }

    //导航;
    public static VBox getSideBar(ScrollPane scrollPane) {
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.setPrefWidth(35);
        vBox.setSpacing(2);
        vBox.setStyle("-fx-background-color:#98C1D9");

        int index = 0;
        for (int i = 0; i < sideBar.length; i++) {
            sideBar[i] = (char) (65 + i);          //65 = ‘A’
            HBox hBox = new HBox();
            Label label = new Label(String.valueOf(sideBar[i]));
            hBox.getChildren().add(label);
            hBox.setAlignment(Pos.CENTER);
            vBox.getChildren().add(hBox);
            int length = list.size();
            for (int j = 0; j < books.length; j++) {
                if (books[j].getList().size() != 0) {
                    length++;
                }
            }

            // 使其每页只有十条数据；
            int finalIndex = index;
            int finalLength = length - 10;
            hBox.setOnMouseClicked(event -> {
                // 计算要设置的vValue
                double percent = finalIndex * 1.0 / finalLength;
                scrollPane.setVvalue(percent);
            });
            // 计算出算上子元素后的所有元素的下标和，包括那个小标题
            index = index + (books[i].getList().size() == 0 ? 0 : books[i].getList().size() + 1);
        }

        return vBox;
    }


    public static GridPane getBorderBottom(BorderPane borderPane) {

        //Swing写视频聊天窗口
        Webcam webcam = Webcam.getDefault();
        webcam.close();
        webcam.setViewSize(WebcamResolution.VGA.getSize());
        WebcamPanel panel = new WebcamPanel(webcam);
        panel.setFPSDisplayed(false);
        panel.setDisplayDebugInfo(false);
        panel.setImageSizeDisplayed(false);
        panel.setMirrored(true);
        JFrame window = new JFrame("基于Swing的视频聊天");
        window.setUndecorated(true);
        int width = Toolkit.getDefaultToolkit().getScreenSize().width;
        int height = Toolkit.getDefaultToolkit().getScreenSize().height;
        int windowsWedth = 600;
        int windowsHeight = 600;
        window.setBounds((width - windowsWedth) / 2,
                (height - windowsHeight) / 2, windowsWedth, windowsHeight);
        window.setAlwaysOnTop(!window.isAlwaysOnTop());
        window.add(panel);
        window.setResizable(true);
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        window.setSize(8000, 1080);
        window.pack();
        final JButton button = new JButton("挂断");
        window.add(panel, BorderLayout.CENTER);
        window.add(button, BorderLayout.SOUTH);
        window.setResizable(true);
        window.pack();
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                webcam.close();
                window.dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
            }
        });
        GridPane bottomPane = new GridPane();
        Stage primaryStage = new Stage();
        Button dial = new Button("短信");
        dial.setStyle("-fx-background-color:#3D5A80");
        dial.setMinSize(100, 50);
        bottomPane.add(dial, 0, 0);
        dial.setOnAction(e -> {
            Stage sendStage = new Stage();
            BorderPane borderpane02 = new BorderPane();
            borderpane02.setPadding(new Insets(97, 17, 80, 17));
            borderpane02.setStyle("-fx-background-image:url(file:/C:/Users/86173/Desktop/图片/bigmobile.png)");

            GridPane topPane02 = new GridPane();
            borderpane02.setTop(topPane02);
            Label searchLabel02 = new Label("收件人:");
            searchLabel02.setFont(Font.font("Segoe Script", 25));
            searchLabel02.setMinSize(82, 38);
            topPane02.add(searchLabel02, 0, 0);
            TextField searchNumber02 = new TextField();
            searchNumber02.setPromptText("\uD83D\uDD0D请输入联系人号码");
            searchNumber02.setMinSize(303, 38);
            topPane02.add(searchNumber02, 1, 0);

            GridPane bottomPane02 = new GridPane();
            borderpane02.setBottom(bottomPane02);
            TextField information = new TextField();
            information.setPromptText("请在此输入发送内容，默认发送一次");
            information.setMinSize(300, 137);
            bottomPane02.add(information, 0, 0);

            Button send = new Button("发送");
            send.setStyle("-fx-background-color:#3D5A80");
            send.setMinSize(386, 50);
            bottomPane02.add(send, 0, 1);
            send.setOnAction(a -> {
                String sentence = information.getText();
                String QQ = searchNumber02.getText();
                try {
                    SendInformation.Send(sentence, QQ);
                } catch (AWTException awtException) {
                    awtException.printStackTrace();
                }
                information.clear();
                searchNumber02.clear();

                Button ok = new Button("OK");
                Stage tip = new Stage();
                BorderPane tipPane = new BorderPane();
                Text tip1 = new Text(20, 20, "        发送成功!");
                tip1.setFont(Font.font("Courier", FontWeight.BOLD, FontPosture.ITALIC, 15));
                tip1.setFill(Color.BLACK);
                tipPane.setTop(tip1);
                tipPane.setCenter(ok);
                Scene tipScene1 = new Scene(tipPane, 20, 60);
                tip.setScene(tipScene1);
                tip.show();
                ok.setOnAction(n -> {
                    tip.close();
                });
            });

            Button back = new Button("返回");
            back.setStyle("-fx-background-color:#98C1D9");
            back.setMinSize(386, 50);
            bottomPane02.add(back, 0, 2);
            back.setOnAction(a -> {
                sendStage.close();
                //primaryStage.show();
            });

            Scene scene2 = new Scene(borderpane02, 420, 875);
            sendStage.setScene(scene2);
            sendStage.setResizable(false);//不可调整窗体大小
            sendStage.initStyle(StageStyle.UNDECORATED);

            sendStage.show();
        });

        Button addressBook = new Button("通讯录");
        addressBook.setStyle("-fx-background-color:#98C1D9");
        addressBook.setMinSize(105, 50);
        bottomPane.add(addressBook, 1, 0);

        Button note = new Button("视频聊天");
        note.setStyle("-fx-background-color:#E0FBFC");
        note.setMinSize(104, 50);
        bottomPane.add(note, 2, 0);
        note.setOnAction(e -> {
            webcam.open();
            window.setVisible(true);
        });

        Button exist = new Button("退出");
        exist.setStyle("-fx-background-color:#EE6C4D");
        exist.setMinSize(77, 50);
        bottomPane.add(exist, 3, 0);
        exist.setOnAction(e -> {
            System.exit(0);
        });
        return bottomPane;
    }


    //初始化分块存储表
    public static void initArray() {
        for (int i = 0; i < books.length; i++) {
            books[i] = new TelephoneBookList();
        }

        // 排序
        Collections.sort(list);

        for (int i = 0; i < list.size(); i++) {
            books[list.get(i).getBlock()].getList().add(list.get(i));
        }
    }

    //小标题标识字母
    public static HBox getBlockName(int index) {
        HBox hBox = new HBox();
        char c = (char) ('A' + index);
        Label name = new Label(String.valueOf(c));
        name.setFont(new Font(24));
        name.setTextFill(Color.web("White"));
        hBox.getChildren().add(name);
        hBox.setPadding(new Insets(12, 35, 12, 20));
        hBox.setStyle("-fx-background-color: #457B9D;-fx-border-radius: 25;-fx-background-radius: 25; ");
        hBox.setMaxWidth(700);
       /* -fx-background-radius: 25; -fx-border-radius: 25*/
        hBox.setPrefHeight(35);
        return hBox;
    }


    // 获取list列表
    public static List<TelephoneBook> getList() {
        return list;
    }


    //获取books
    public static TelephoneBookList[] getBooks() {
        return books;
    }


    // 初始化的通讯录
    public void getInitInfo() {
        list.add(new TelephoneBook("黄湖超", new String[]{"13983239810", "17300258845", "15896574256"}, "室友", "1598755562@163.com"));
        list.add(new TelephoneBook("郑杰", new String[]{"18725934726"}, "我自己", "1957366623@163.com"));
        list.add(new TelephoneBook("何玉婷", new String[]{"18716530300"}, "无", "49571022@qq.com"));
        list.add(new TelephoneBook("肖俊", new String[]{"15862574125"}, "无", "4586211145@qq.com"));
        list.add(new TelephoneBook("陈红休", new String[]{"15742689512"}, "无", "1578963258@163.com"));
        list.add(new TelephoneBook("郑源舟", new String[]{"15786258596", "41523652587"}, "无", "1578956214@163.com"));
        list.add(new TelephoneBook("莫盛玟", new String[]{"15788956632"}, "无", "460170495@163.com"));
        list.add(new TelephoneBook("赵若宇", new String[]{"15789952548"}, "无", "18375707143@163.com"));
        list.add(new TelephoneBook("李老师", new String[]{"15741528987"}, "无", "13364035985@163.com"));
        list.add(new TelephoneBook("张老师", new String[]{"12547822451"}, "无", "13364032713@qq.com"));
        list.add(new TelephoneBook("赵宏", new String[]{"123458596574"}, "无", "3158056343@qq.com"));
        list.add(new TelephoneBook("李泽宽", new String[]{"15785428548"}, "无", "1597895346@qq.com"));
        list.add(new TelephoneBook("阿凡达", new String[]{"25874514754"}, "无", "1573045975@qq.com"));
        list.add(new TelephoneBook("包子铺", new String[]{"12358589658"}, "无", "15730164241@126.com"));
        list.add(new TelephoneBook("苏圣", new String[]{"4565416548"}, "无", "15615165162@163.com"));
        list.add(new TelephoneBook("傅俊杰", new String[]{"17522665566"}, "无", "5452589522@qq.com"));
        list.add(new TelephoneBook("郑山东", new String[]{"14522566654"}, "无", "2546569441@qq.com"));
    }

    //点击搜索按钮后主界面要渲染的内容
    public static ScrollPane getSearchBorderBody(BorderPane borderPane) {
        // 搜索框内的字符串
        String input = search.getText();
        if (input.equals("")) {
            return scrollPane;
        }
        // 排序
        Collections.sort(list);

        VBox vBox = new VBox();
        vBox.setPadding(new Insets(5, 10, 5, 20));
        vBox.setStyle("-fx-background-color:#A8DADC");

        for (int i = 0; i < list.size(); i++) {
            // 如果list中的名字包含搜索框的内容
            if (list.get(i).getName().contains(input) || list.get(i).getEmail().contains(input)) {
                NameListPane nameListPane = new NameListPane(list.get(i), borderPane, i);
                vBox.getChildren().add(nameListPane);
            } else {
                for (int j = 0; j < list.get(i).getTelephone().length; j++) {
                    if (list.get(i).getTelephone()[j].contains(input)) {
                        NameListPane nameListPane = new NameListPane(list.get(i), borderPane, i);
                        vBox.getChildren().add(nameListPane);
                        break;
                    }
                }
            }
        }

        scrollPane.setPannable(true);
        //scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setContent(vBox);
        scrollPane.setStyle("-fx-background-insets: 0");

        return scrollPane;
    }
    public static void closeNow(Stage stage){
        stage.close();
    }
}
