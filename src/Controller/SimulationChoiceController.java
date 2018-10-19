package Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class SimulationChoiceController extends Navigation{

    @FXML
    private Button btnEPayment;
    @FXML
    private Button btnWebsite;

    @FXML
    private void btnEPaymentClick(ActionEvent actionEvent){

    }

    @FXML
    private void btnWebsiteClick(ActionEvent actionEvent){
        closeCurScene(actionEvent);
        loadStage("/Fxml/WebsitePage.fxml", actionEvent);
    }

}
