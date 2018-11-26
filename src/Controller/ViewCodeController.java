package Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;

import java.net.URL;
import java.util.ResourceBundle;

public class ViewCodeController extends Navigation implements Initializable {

    @FXML
    ToggleButton btnBack;
    @FXML
    Button btnContinue;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    @FXML
    private void btnBackClick(ActionEvent actionEvent){
        closeCurScene(actionEvent);
        loadStage("/Fxml/MainMenuPage.fxml", actionEvent);
    }

    @FXML
    private void btnContinueClick(ActionEvent actionEvent){
        closeCurScene(actionEvent);
        loadStage("/Fxml/SimulationPage.fxml", actionEvent);
    }


}
