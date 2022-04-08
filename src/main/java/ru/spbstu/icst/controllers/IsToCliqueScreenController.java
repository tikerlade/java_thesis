package ru.spbstu.icst.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class IsToCliqueScreenController implements Initializable {
    @FXML
    private Label isConnectedLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.isConnectedLabel.setText("Connected");
    }
}
