package com.tmjonker.pong.main;

import javafx.application.Application;
import javafx.stage.Stage;

public class Bridge extends Application {


    @Override
    public void start(Stage stage) throws Exception {

        new GameController();
    }

    public static void main(String[] args) {

        launch(args);
    }
}
