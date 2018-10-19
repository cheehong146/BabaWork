package Controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.net.ssl.HttpsURLConnection;
import java.net.URL;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginPageController extends Navigation implements Initializable {

    int SCORE_DIALOG_WIDTH = 500;
    int SCORE_DIALOG_HEIGHT = 300;

    @FXML
    private Button btnLogin;
    @FXML
    private Hyperlink hlRegister;
    @FXML
    private Button btnScore;
    @FXML
    private Button btnCode;
    @FXML
    private TextField txtUsername;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private Button btnBack;
    @FXML
    private Button btnRetry;

    private String url = "https://badssl.com/";
    private int totPoints;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    private void btnLoginClick(ActionEvent actionEvent) {
        System.out.println(txtUsername.getText() + ", " + txtPassword.getText());
        totPoints = calcPointByPassword(txtPassword.getText());
        totPoints += calcPointByUrl(url);
        System.out.println("Total Points: " + totPoints);
    }

    @FXML
    private void btnScoreClick(ActionEvent actionEvent) {
        final Stage dialog = new Stage();
        dialog.setResizable(false);
        dialog.initModality(Modality.APPLICATION_MODAL);
        VBox dialogVbox = new VBox(20);
        dialogVbox.setAlignment(Pos.CENTER);
        dialogVbox.getChildren().add(new Text("YOU HAVE SCORED " + totPoints + " POINTS"));
        dialogVbox.getChildren().add(new Text("Click on the link below to know how to create a strong password"));

        Hyperlink strongPasswordLink = new Hyperlink();
        strongPasswordLink.setText("Click Here");
        dialogVbox.getChildren().add(strongPasswordLink);

        Scene dialogScene = new Scene(dialogVbox, SCORE_DIALOG_WIDTH, SCORE_DIALOG_HEIGHT);
        dialog.setScene(dialogScene);
        dialog.show();

        strongPasswordLink.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Clicked on storng password link");
            }
        });
    }

    @FXML
    private void btnRetryClick (ActionEvent actionEvent){

    }

    @FXML
    private void btnViewCodeClick(ActionEvent actionEvent){
        closeCurScene(actionEvent);
        loadStage("/Fxml/ViewCodePage.fxml", actionEvent);
    }

    @FXML
    private void btnBackClick(ActionEvent actionEvent){
        closeCurScene(actionEvent);
        loadStage("/Fxml/SimulationChoicePage.fxml", actionEvent);
    }

    private int calcPointByUrl(String url){
        int points = 0;
        try {
            points += isCertValid(url)? 10: 0;
        }catch (Exception e){
            System.out.println("SSL Cert not valid");
        }
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

    //check if ssl cert is valid, will throw exception when it's self-signed, expired, or doesn't have one
    public boolean isCertValid(String aURL) throws Exception{
        URL destinationURL = new URL(aURL);
        HttpsURLConnection conn = (HttpsURLConnection) destinationURL.openConnection();
        conn.connect();
        Certificate[] certs = conn.getServerCertificates();
        for (Certificate cert : certs) {
            System.out.println("Certificate is: " + cert);
            if(cert instanceof X509Certificate) {
                //If you want the ssl cert specification uncomment this and use x
//                X509Certificate x = (X509Certificate ) cert;
//                System.out.println(x.getIssuerDN().toString());
                return true;
            }
        }
        return false;
    }
}
