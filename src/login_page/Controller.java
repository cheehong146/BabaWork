package login_page;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class Controller {

    public Button btnLogin;
    public Hyperlink hlRegister;
    public Button btnPoints;
    public Button btnCode;

    public TextField txtUsername;
    public PasswordField txtPassword;


    public void btnLoginClick(ActionEvent actionEvent) {
        System.out.println(txtUsername.getText() + ", " + txtPassword.getText());
    }
}
