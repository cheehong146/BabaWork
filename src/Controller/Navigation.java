package Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public abstract class Navigation {
    public void loadStage(String fxml, ActionEvent actionEvent){
        try{
            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.setScene((new Scene(root)));
            stage.show();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void closeCurScene(ActionEvent actionEvent){
        Node node = (Node)(actionEvent.getSource());
        node.getScene().getWindow().hide();
    }
}
