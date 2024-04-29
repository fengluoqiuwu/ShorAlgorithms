package main;

import javafx.application.Application;
import javafx.stage.Stage;

import static GUI.GUIUtil.StageUtil.changeViews;
import static GUI.GUIUtil.StageUtil.resetLocation;
import static java.lang.System.exit;

public class Main extends Application {
    private static Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        primaryStage.setOnHiding(e-> exit(0));
        primaryStage.setResizable(false);
        changeViews(primaryStage,"/GUI/mainPage/mainPage.fxml");
        primaryStage.show();
        primaryStage.setTitle("My Project Program");
        resetLocation(primaryStage);
    }

    public static Stage getPrimaryStage() {return primaryStage;}
}
