package com.example.grammar_parser.controller;

import com.example.grammar_parser.entry.*;
import com.example.grammar_parser.service.GrammarService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class HelloController {
    @FXML
    private VBox vbox;
    @FXML
    private TextArea grammar_text;
    @FXML
    private TextField text;
    @FXML
    private Tab t1;
    @FXML
    private Tab t2;
    @FXML
    private Tab t3;
    @FXML
    private Tab t4;
    @FXML
    private Tab t5;

    @FXML
    private Tab t6;
    @FXML
    private Tab t7;
    @FXML
    private Button openFileBtn;

    private List<String> grammarTextList;

    private GrammarService grammarService = new GrammarService();

    @FXML
    protected void onOpenFileBtnClick() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                new FileChooser.ExtensionFilter("All Files", "*.*"));
        Window window = vbox.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(window);
        if (selectedFile != null) {
            BufferedReader reader = new BufferedReader(new FileReader(selectedFile));
            grammarTextList = new ArrayList<>();
            reader.lines().forEach(e->{
                grammarTextList.add(e);
            });

        }

    }
    @FXML
    protected void startParseClick(){
        if (grammarTextList!=null){
            grammarService.startParser(grammarTextList,text.getText());
        }else if(grammar_text.getText()!=null&&grammar_text.getText().length()>0){
            grammarTextList = new ArrayList<>();
            String gt = grammar_text.getText();
            StringReader sr = new StringReader(gt);
            BufferedReader bufferedReader = new BufferedReader(sr);
            bufferedReader.lines().forEach(e->{
                grammarTextList.add(e);
            });
            grammarService.startParser(grammarTextList,text.getText());
        }
        startTab();
    }

    private void startTab(){
        Map<String, Object> resultMap = grammarService.getResultMap();
        resultMap.forEach((k,v)->{
            System.out.println("k:"+k+",v:"+v);
        });

        Label l1 = new Label();
        l1.getStyleClass().setAll("h4","lbl-info");
        l1.setText((String)resultMap.get("simResult"));
        t1.setContent(l1);

        Label l2 = new Label();
        l2.getStyleClass().setAll("h4","lbl-info");
        l2.setText((String)resultMap.get("rlfResult"));
        t2.setContent(l2);

        Label l3 = new Label();
        l3.getStyleClass().setAll("h4","lbl-info");
        l3.setText((String)resultMap.get("lrResult"));
        t3.setContent(l3);

        ListView<First> listView = new ListView<>();
        List<First> firstResult = (List<First>)resultMap.get("firstResult");
        ObservableList<First> firstList = FXCollections.observableArrayList(firstResult);
        listView.setItems(firstList);
        t4.setContent(listView);
        
        ListView<Follow> listView2 = new ListView<>();
        List<Follow> followResult = (List<Follow>)resultMap.get("followResult");
        ObservableList<Follow> followList = FXCollections.observableArrayList(followResult);
        listView2.setItems(followList);
        t5.setContent(listView2);

        Label l6 = new Label();
        l6.getStyleClass().setAll("h4","lbl-info");
        SelectMap selectMapResult = (SelectMap)resultMap.get("selectMapResult");
        l6.setText(selectMapResult.toString());
        t6.setContent(l6);

        List<Grammar> grammarList = (List<Grammar>)resultMap.get("analyzeResult");
        if (grammarList!=null&&grammarList.size()>0){
            ListView<Grammar> grammarListView = new ListView<>();
            ObservableList<Grammar> observableList = FXCollections.observableArrayList(grammarList);
            grammarListView.setItems(observableList);
            t7.setContent(grammarListView);
        }else {
            Label l7 = new Label();
            l7.getStyleClass().setAll("h4","lbl-danger");
            l7.setText("没有输入文本或者解析错误");
            t7.setContent(l7);
        }


    }

}