package Controller;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.net.URL;
import java.util.ResourceBundle;


public class WebsiteController extends Navigation implements Initializable {

    @FXML
    private Button btnBack;
    @FXML
    private Button btnContinue;
    @FXML
    private TextArea txtAreaContents;

    private String loremIpsum = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas viverra purus ut scelerisque elementum. Sed porttitor elit non justo dapibus suscipit. Vivamus tortor turpis, lobortis ut tempus a, aliquam sed turpis. Quisque vestibulum ultrices massa, lacinia congue leo pretium in. Integer malesuada pulvinar ex sit amet viverra. Maecenas non blandit leo. Fusce dignissim felis nisi, quis bibendum libero accumsan sagittis. Duis id sapien erat. Praesent sodales viverra augue, aliquet scelerisque mi tempus porta. Nunc urna lacus, interdum vel sollicitudin at, pretium ac est. Nulla quis tortor enim. Curabitur pellentesque accumsan nibh, sed auctor elit aliquet tristique. Suspendisse pellentesque porta euismod. Donec finibus orci erat, non feugiat turpis volutpat et.\n" +
            "Suspendisse commodo nulla sed augue commodo, nec ultrices est lacinia. Quisque congue lobortis venenatis. Ut cursus, eros at viverra tempus, lorem neque convallis ante, sodales viverra tellus mauris quis felis. Donec fringilla dolor et diam maximus, quis rutrum ligula lobortis. Duis ornare sapien molestie tincidunt porttitor. Mauris a malesuada nisl. Curabitur vitae velit non turpis commodo semper sit amet non ex. Curabitur malesuada ipsum a dui pretium, nec sollicitudin metus pellentesque. Etiam in turpis semper, tincidunt arcu sed, molestie dolor. Suspendisse eu metus massa. Integer facilisis quam metus, at maximus nisi mattis quis.\n" +
            "Cras non sodales lorem. Vivamus est lacus, dictum at pellentesque a, imperdiet quis enim. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Integer quis elit accumsan, interdum nisi vel, facilisis urna. Sed vel ultrices magna, sit amet maximus dolor. Fusce dapibus sed leo nec auctor. Phasellus congue eros luctus, tincidunt metus a, vestibulum lacus. Maecenas dapibus odio id commodo congue. Nulla in est sit amet dolor scelerisque lobortis quis sed orci. Curabitur luctus non nisi sed consectetur. Nullam vulputate, enim ac blandit dictum, nulla neque sodales augue, vitae efficitur orci justo in tortor. Vestibulum at laoreet tortor. Morbi tristique tellus at efficitur laoreet. In hac habitasse platea dictumst.\n" +
            "Nam porta justo vel ex commodo, vitae semper sapien sollicitudin. Curabitur risus est, viverra vel dui vitae, iaculis vulputate eros. Integer eu leo eget augue ornare hendrerit. Cras ac ornare ipsum. Proin quis justo ipsum. Phasellus tincidunt egestas tristique. Vivamus ut feugiat tortor. Aliquam non efficitur odio.\n" +
            "Donec sem diam, auctor vel dolor quis, pretium elementum lectus. Praesent sit amet lorem id massa vehicula sollicitudin. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi et dui lorem. Pellentesque ornare ut velit iaculis tristique. Proin egestas magna euismod, imperdiet tellus quis, accumsan eros. Nulla scelerisque congue vehicula. Phasellus sodales condimentum orci, in facilisis ligula rutrum eu. Integer euismod laoreet velit non bibendum. Nullam velit nisi, blandit non magna a, tristique vulputate velit. Nam eu nisi ac metus pellentesque elementum. Pellentesque rutrum blandit scelerisque.";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        txtAreaContents.setText(loremIpsum);
        txtAreaContents.setWrapText(true);
//        BackgroundImage backgroundImage = new BackgroundImage(new Image(getClass().getResource("images/back.png").toExternalForm()), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
//        btnBack.setBackground(new Background(backgroundImage));

    }

    @FXML
    private void btnBackClick(ActionEvent actionEvent){
        closeCurScene(actionEvent);
        loadStage("/Fxml/SimulationChoicePage.fxml", actionEvent);
    }

    @FXML
    private void btnContinueClick(ActionEvent actionEvent){
        closeCurScene(actionEvent);
        loadStage("/Fxml/LoginPage.fxml", actionEvent);
    }
}
