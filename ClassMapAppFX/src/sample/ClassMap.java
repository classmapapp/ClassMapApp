package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class ClassMap extends Application {


    public Stage primaryStage;
    @Override
    public void start(Stage primaryStage) throws Exception{




        Parent root = FXMLLoader.load(getClass().getResource("ClassMapDataFile.fxml"));
        primaryStage.setTitle("Class Map");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }




    public static void main(String[] args) {
        DataConnection.login("Travis", "Clinkscales");

        launch(args);
    }
}
