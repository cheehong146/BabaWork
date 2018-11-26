package Controller;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;

import javax.swing.*;
import java.net.URL;
import java.util.ResourceBundle;


public class WebsiteController extends Navigation implements Initializable {

    @FXML
    private ToggleButton btnBack;
    @FXML
    private Button btnContinue;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    @FXML
    private void btnContinueClick(ActionEvent actionEvent){
        closeCurScene(actionEvent);
        loadStage("/Fxml/LoginPage.fxml", actionEvent);
    }

    @FXML
    private void btnBackClick(ActionEvent actionEvent){
        closeCurScene(actionEvent);
        loadStage("/Fxml/MainMenuPage.fxml", actionEvent);
    }
}
