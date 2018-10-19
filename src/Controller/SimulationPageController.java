package Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class SimulationPageController extends Navigation implements Initializable {

    @FXML
    private Button btnBack;
    @FXML
    private Button btnRetry;
    @FXML
    private Button btnRunCode;
    @FXML
    private Button btnExit;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    private void btnBackPressed(ActionEvent actionEvent){
        closeCurScene(actionEvent);
        loadStage("/Fxml/LoginPage.fxml", actionEvent);
    }

    @FXML
    private void btnRetryPressed(ActionEvent actionEvent){

    }
    @FXML
    private void btnRunCodePressed(ActionEvent actionEvent){

    }

    @FXML
    private void textBoxContentsDragged(ActionEvent actionEvent){

    }

    @FXML
    private void btnExitPressed(ActionEvent actionEvent) {

    }
}
