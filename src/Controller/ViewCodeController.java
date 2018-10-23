package Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

import java.net.URL;
import java.util.ResourceBundle;

public class ViewCodeController extends Navigation implements Initializable {

    @FXML
    Button btnBack;
    @FXML
    Button btnContinue;
    @FXML
    TextArea txtAreaContents;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        txtAreaContents.setText("BLA BLA BLA BLA BLALSDPADOIHAUSDGUIAGSDUAHYISDVIADS");
    }

    @FXML
    private void btnBackClick(ActionEvent actionEvent){
        closeCurScene(actionEvent);
        loadStage("/Fxml/LoginPage.fxml", actionEvent);
    }

    @FXML
    private void btnContinueClick(ActionEvent actionEvent){
        closeCurScene(actionEvent);
        loadStage("/Fxml/SimulationPage.fxml", actionEvent);
    }


}
