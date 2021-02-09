package klijent;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
        Parent root = loader.load();
        Controller controller = loader.getController();
        primaryStage.setTitle("Akvaristicka Prodavnica");
        primaryStage.setScene(new Scene(root, 1000, 500));
        primaryStage.show();
        primaryStage.setOnCloseRequest(windowEvent -> {
            try {
                controller.exit();
            }catch (Exception e){
                e.printStackTrace();
            }
            Platform.exit();
            System.exit(0);
        } );
    }


    public static void main(String[] args) {
        launch();
    }


}
