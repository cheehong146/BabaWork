package Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginPageController extends Navigation {

    @FXML
    private Button btnLogin;
    @FXML
    private Hyperlink hlRegister;
    @FXML
    private Button btnPoints;
    @FXML
    private Button btnCode;
    @FXML
    private TextField txtUsername;
    @FXML
    private PasswordField txtPassword;

    private String url = "";
    private int points = 0;

    @FXML
    private void btnLoginClick(ActionEvent actionEvent) {
        System.out.println(txtUsername.getText() + ", " + txtPassword.getText());
        loadStage("/Fxml/SimulationPage.fxml", actionEvent);
        closeCurScene(actionEvent);
    }

    @FXML
    private void btnPointClick(ActionEvent actionEvent) {
        if (url.contains("https")) {
            points += 10;
        }
        System.out.println("Points: " + points);
    }
}
