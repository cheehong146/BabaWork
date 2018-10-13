package Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private int totPoints;

    @FXML
    private void btnLoginClick(ActionEvent actionEvent) {
        System.out.println(txtUsername.getText() + ", " + txtPassword.getText());
        loadStage("/Fxml/SimulationPage.fxml", actionEvent);
        closeCurScene(actionEvent);
    }

    @FXML
    private void btnPointClick(ActionEvent actionEvent) {
        totPoints = calcPointByPassword(txtPassword.getText());
        totPoints += calcPointByUrl(url);
        System.out.println("Total Points: " + totPoints);
    }

    private int calcPointByUrl(String url){
        int points = 0;
        if(url.contains("https"))
            points = 10;

        return  points;
    }

    private int calcPointByPassword(String pass){
        int points = 0;
        boolean lowercaseFlag = false;
        boolean uppercaseFlag = false;
        boolean numberFlag = false;
        String regexSymbol = "[:?!@#$%^&*()]";

        if (pass.length() >= 8)//I Assume is 8 charac and above?
            points += 10;

        char [] chPass = pass.toCharArray();
        //check if lowercase, uppercase, or contain digit
        for (char ch :
                chPass) {
            if (Character.isLowerCase(ch))
                lowercaseFlag = true;
            else if(Character.isUpperCase(ch))
                uppercaseFlag = true;
            else if(Character.isDigit(ch))
                numberFlag = true;

            if(lowercaseFlag && uppercaseFlag && numberFlag)
                break;
        }
        points += lowercaseFlag? 10: 0;
        points += uppercaseFlag? 10: 0;
        points += numberFlag? 10: 0;

        //check if password contain symbol
        Pattern pattern = Pattern.compile(regexSymbol);
        Matcher matcher = pattern.matcher(pass);
        if (matcher.find())
            points += 10;

        return points;
    }
}
