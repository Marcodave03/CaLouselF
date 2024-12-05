package main;

import javafx.application.Application;
import javafx.stage.Stage;
import view.RegisterPage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args); // Launch the JavaFX application
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("CaLouseIF");
        new RegisterPage(primaryStage);
        primaryStage.show();
    }
}
