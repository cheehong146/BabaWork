package Controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginPageController extends Navigation implements Initializable {

    private int SCORE_DIALOG_WIDTH = 800;
    private int SCORE_DIALOG_HEIGHT = 500;
    private String URL_LOCK_ICON_LOC = "images/lock.png";
    private String ICON_IDENTIFIER = ":lock:";

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
    private ToggleButton btnBack;
    @FXML
    private ToggleButton btnRetry;
    @FXML
    private ComboBox ddlUrl;

    private int totPoints;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
            String notSecureUrl = "http://www.authcube.com";
            String semiSecureUrl = "https://www.authcube.com";
            String secureUrl = ICON_IDENTIFIER + "https://www.authcube.com";

            ddlUrl.setItems(FXCollections.observableArrayList(notSecureUrl, semiSecureUrl, secureUrl));
            ddlUrl.setCellFactory(param -> new UrlListCell());
            ddlUrl.setButtonCell(new UrlListCell());
    }

    @FXML
    private void btnLoginClick(ActionEvent actionEvent) {
        System.out.println(txtUsername.getText() + ", " + txtPassword.getText());
        totPoints = calcPointByPassword(txtPassword.getText());
        totPoints += calcPointByUrl();
        System.out.println("Total Points: " + totPoints);
    }

    @FXML
    private void btnScoreClick(ActionEvent actionEvent) {
        final Stage dialog = new Stage();
        dialog.setResizable(false);
        dialog.initModality(Modality.APPLICATION_MODAL);
        VBox dialogVbox = new VBox(20);
        dialogVbox.setAlignment(Pos.CENTER);

        Text txtPreScore = new Text("YOU HAVE SCORED");
        Text txtScore = new Text(String.valueOf(totPoints));
        Text txtPostScore = new Text(" POINTS");
        Text txtLinkInfo = new Text("Click on the link below to know how to create a strong password");
        txtLinkInfo.setStyle("-fx-text-fill: #FF5959");

        txtPreScore.setStyle("-fx-font-family: Abel; -fx-text-fill: #393D46; -fx-font-size: 24px");
        txtPostScore.setStyle("-fx-font-family: Abel; -fx-text-fill: #393D46; -fx-font-size: 24px;");
        txtScore.setStyle("-fx-font-family: Abel; -fx-text-fill: #4C9DA6; -fx-font-size: 24px;");
        txtLinkInfo.setStyle("-fx-font-family: Abel; -fx-text-fill: #4C9DA6; -fx-font-size: 18px;");

        HBox scoreHBox = new HBox(txtScore, txtPostScore);
        scoreHBox.setAlignment(Pos.CENTER);

        dialogVbox.getChildren().add(txtPreScore);
        dialogVbox.getChildren().add(scoreHBox);
        dialogVbox.getChildren().add(txtLinkInfo);
        dialogVbox.setStyle("-fx-background-color: #FFAD5B");


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
    private void btnRetryPressed (ActionEvent actionEvent){
        closeCurScene(actionEvent);
        loadStage("/Fxml/LoginPage.fxml", actionEvent);
    }

    @FXML
    private void btnViewCodeClick(ActionEvent actionEvent){
        closeCurScene(actionEvent);
        loadStage("/Fxml/ViewCodePage.fxml", actionEvent);
    }

    @FXML
    private void btnBackPressed(ActionEvent actionEvent){
        closeCurScene(actionEvent);
        loadStage("/Fxml/MenuPage.fxml", actionEvent);
    }

    private int calcPointByUrl(){
        int points = 0;
        try {
            String selectedItem = ddlUrl.getSelectionModel().getSelectedItem().toString();
            if(selectedItem.substring(0, 5).equals("https")){
                points = 5;
            }else if (selectedItem.substring(0, ICON_IDENTIFIER.length()).equals(ICON_IDENTIFIER)){
                points = 10;
            }
        }catch (Exception e){
            System.out.println("SSL Cert not valid");
        }
        System.out.println("Points by URL: " + points);//TODO REMOVE IF DONT WANT
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

    class UrlListCell extends ListCell<String> {
        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (item == null || empty)
                setGraphic(null);
            else {
                if(item.substring(0, ICON_IDENTIFIER.length()).equals(ICON_IDENTIFIER)) {
                    Image image = new Image(URL_LOCK_ICON_LOC);
                    ImageView imageView = new ImageView(image);
                    imageView.setFitHeight(16);
                    imageView.setFitWidth(16);
                    Label lbl = new Label(item.substring(ICON_IDENTIFIER.length()));
                    lbl.setStyle("-fx-text-fill: BLACK;");
                    HBox hBox = new HBox(imageView, lbl);
                    hBox.setAlignment(Pos.CENTER_LEFT);
                    setGraphic(hBox);
                }else{
                    Label lbl = new Label(item);
                    lbl.setStyle("-fx-text-fill: BLACK;");
                    setGraphic(lbl);
                }
            }

            setText("");
        }

    }
}
