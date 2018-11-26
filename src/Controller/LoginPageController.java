package Controller;

import javafx.application.Platform;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginPageController extends Navigation implements Initializable {

    private int SCORE_DIALOG_WIDTH = 800;
    private int SCORE_DIALOG_HEIGHT = 450;
    private String URL_LOCK_ICON_LOC = "images/lock.png";
    private String ICON_IDENTIFIER = ":lock:";
    private String MAX_SCORE = "60";

    @FXML
    private Button btnLogin;
    @FXML
    private Button btnScore;
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

//    private int totPoints;
    private boolean isBtnLoginClicked;
    private Score userScore;//should have created a dictionary instead of object so i can iterate the variable easier, oh well

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //initialize URL combobox data and populate it
        String notSecureUrl = "http://www.authcube.com";
        String semiSecureUrl = "https://www.authcube.com";
        String secureUrl = ICON_IDENTIFIER + "https://www.authcube.com";

        userScore = new Score();
        isBtnLoginClicked = false;

        ddlUrl.setItems(FXCollections.observableArrayList(notSecureUrl, semiSecureUrl, secureUrl));
        ddlUrl.setCellFactory(param -> new UrlListCell());
        ddlUrl.setButtonCell(new UrlListCell());
        ddlUrl.getSelectionModel().selectFirst(); //Select 1st value as default

        Timer timer = new java.util.Timer();

        timer.schedule(new TimerTask() {
            public void run() {
                Platform.runLater(new Runnable() {
                    public void run() {
                        urlPopup();
                    }
                });
            }
        }, 1000);


    }

    @FXML
    private void btnLoginClick(ActionEvent actionEvent) { //calculate password and url points and print out the total
        boolean bothTextFieldFilledIn = false;

        if (!txtUsername.getText().isEmpty() && !txtPassword.getText().isEmpty())
            bothTextFieldFilledIn = true;

        if (bothTextFieldFilledIn){
            registerGoodPopup();
            calcTotScore(userScore);
            System.out.println("Total Points: " + userScore.getTotScore());
            isBtnLoginClicked = true;
        }else{
            showLoginAlertDialog();
        }

    }

    private void registerGoodPopup(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("REGISTRATION SUCCESSFUL");
        alert.setContentText("Please click the SCORE button to view your score");
        alert.showAndWait();
    }

    @FXML
    private void ddlUrlOnAction(ActionEvent actionEvent){
        loginInfoPopup();
    }

    @FXML
    private void btnScoreClick(ActionEvent actionEvent) {//pop-up a dialog box, containing tot_score
        if (isBtnLoginClicked) {
            showScoreDialog(actionEvent);
        }else{
            showScoreAlertDialog();//show when user didnt press login button yet before click the score button
        }
    }

    private void showScoreDialog(ActionEvent actionEvent){
        String fxAbelTextLargerStyle = "-fx-font-family: Abel; -fx-font-size: 24px;";
        String fxAbelTextSmallerStyle = "-fx-font-family: Abel; -fx-font-size: 18px;";
        String fxSystemTextBoldStyle = "-fx-font-size: 18px; -fx-font-weight: bold;";
//        String cellBorderStyle = "-fx-border-width: 18px; -fx-border-color: #000000;";
        final Stage dialog = new Stage();
        dialog.setResizable(false);
        dialog.initModality(Modality.APPLICATION_MODAL);
        VBox dialogVbox = new VBox(20);
        dialogVbox.setAlignment(Pos.CENTER);

        Text txtPreScore = new Text("YOU HAVE SCORED");
        Text txtScore = new Text(String.valueOf(userScore.getTotScore()));
        Text txtPostScore = new Text(" POINTS OUT OF " + MAX_SCORE + " POINTS");
        Text txtLinkInfoPre = new Text("PLEASE REFER TO ");
        Text txtLinkInfoMid = new Text("HOW TO CREATE A STRONG PASSWORD ");
        Text txtLinkInfoPost = new Text("ARTICLE ON OUR WEBSITE.");

        txtPreScore.setStyle(fxAbelTextLargerStyle);
        txtPostScore.setStyle(fxAbelTextLargerStyle);
        txtScore.setStyle(fxAbelTextLargerStyle);
        txtLinkInfoPre.setStyle(fxAbelTextSmallerStyle);
        txtLinkInfoMid.setStyle(fxSystemTextBoldStyle);
        txtLinkInfoPost.setStyle(fxAbelTextSmallerStyle);

        //top section with how much score over the max score
        HBox scoreHBox = new HBox(txtScore, txtPostScore);
        scoreHBox.setAlignment(Pos.CENTER);

        //middle section, the grid with breakdown of score
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(20);
        gridPane.setVgap(15);
        gridPane.getStylesheets().add("@../StyleSheets/ScoreGridPane.css");
            //header
        Label requirementHeader = new Label("REQUIREMENTS");
        Label pointHeader = new Label("POINTS");
        requirementHeader.setStyle(fxSystemTextBoldStyle + " -fx-underline: true;");
        pointHeader.setStyle(fxSystemTextBoldStyle + " -fx-underline: true;");
            //left cell
        Text lengthCell = new Text("MINIMUM OF 8 CHARACTERS");
        Text uppercaseCell = new Text("UPPERCASE LETTER");
        Text lowercaseCell = new Text("LOWERCASE LETTER");
        Text numberCell = new Text("NUMBERS");
        Text specialCharCell = new Text("SPECIAL CHARACTER ");
        Text secureUrlCell = new Text("SECURE URL");
            //right cell
        Text lengthPointCell = new Text(userScore.getLength() + " POINTS");
        Text uppercasePointCell = new Text(userScore.getUppercase() + " POINTS");
        Text lowercasePointCell = new Text(userScore.getLowercase() + " POINTS");
        Text numberPointCell = new Text(userScore.getDigits() + " POINTS");
        Text specialCharPointCell = new Text(userScore.getSymbol() + " POINTS");
        Text secureUrlPointCell = new Text(userScore.getUrl() + " POINTS");
            //cell styling
        lengthCell.setStyle(fxAbelTextSmallerStyle);
        uppercaseCell.setStyle(fxAbelTextSmallerStyle);
        lowercaseCell.setStyle(fxAbelTextSmallerStyle);
        numberCell.setStyle(fxAbelTextSmallerStyle);
        specialCharCell.setStyle(fxAbelTextSmallerStyle);
        secureUrlCell.setStyle(fxAbelTextSmallerStyle);
        lengthPointCell.setStyle(fxAbelTextSmallerStyle);
        uppercasePointCell.setStyle(fxAbelTextSmallerStyle);
        lowercasePointCell.setStyle(fxAbelTextSmallerStyle);
        numberPointCell.setStyle(fxAbelTextSmallerStyle);
        specialCharPointCell.setStyle(fxAbelTextSmallerStyle);
        secureUrlPointCell.setStyle(fxAbelTextSmallerStyle);
        //grid cell add children
        gridPane.add(requirementHeader, 0,0 );
        gridPane.add(pointHeader, 1,0 );
        gridPane.add(lengthCell, 0,1 );
        gridPane.add(lengthPointCell, 1,1 );
        gridPane.add(uppercaseCell, 0,2 );
        gridPane.add(uppercasePointCell, 1,2 );
        gridPane.add(lowercaseCell, 0,3 );
        gridPane.add(lowercasePointCell, 1,3 );
        gridPane.add(specialCharCell, 0,4 );
        gridPane.add(specialCharPointCell, 1,4 );
        gridPane.add(numberCell, 0, 5);
        gridPane.add(numberPointCell, 1, 5);
        gridPane.add(secureUrlCell, 0,6 );
        gridPane.add(secureUrlPointCell, 1,6 );

        //bottom section, help section with please refer to how to create a strong password article on our website
        HBox hBoxLinkInfo = new HBox(txtLinkInfoPre, txtLinkInfoMid, txtLinkInfoPost);
        hBoxLinkInfo.setAlignment(Pos.CENTER);

        //menu button at left most bottom
        Button btnMainMenu = new Button("Main Menu");
        VBox vBoxBottom = new VBox(btnMainMenu);
        vBoxBottom.setAlignment(Pos.BOTTOM_RIGHT);

        btnMainMenu.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                closeCurScene(actionEvent);
                loadStage("/Fxml/MainMenuPage.fxml", actionEvent);
            }
        });


        dialogVbox.getChildren().add(txtPreScore);
        dialogVbox.getChildren().add(scoreHBox);
        dialogVbox.getChildren().add(gridPane);
        dialogVbox.getChildren().add(hBoxLinkInfo);
        dialogVbox.getChildren().add(vBoxBottom);
        dialogVbox.setStyle("-fx-background-color: #FFAD5B");



        Scene dialogScene = new Scene(dialogVbox, SCORE_DIALOG_WIDTH, SCORE_DIALOG_HEIGHT);
        dialog.setScene(dialogScene);
        dialog.show();
    }

    private void showScoreAlertDialog(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("ERROR");
        alert.setContentText("PLEASE CLICK THE LOGIN BUTTON BEFORE VIEWING THE SCORE");
        alert.showAndWait();
    }

    private void showLoginAlertDialog(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("ERROR");
        alert.setContentText("PLEASE FILL IN THE USERNAME AND PASSWORD FIELD");
        alert.showAndWait();
    }

    @FXML
    private void btnRetryPressed (ActionEvent actionEvent){//refresh the page
        closeCurScene(actionEvent);
        loadStage("/Fxml/LoginPage.fxml", actionEvent);
    }

    @FXML
    private void btnBackPressed(ActionEvent actionEvent){
        closeCurScene(actionEvent);
        loadStage("/Fxml/MainMenuPage.fxml", actionEvent);
    }

    private void calcTotScore(Score userScore){
        calcPointByUrl(userScore);
        calcPointByPassword(userScore);
    }

    private void calcPointByUrl(Score score){
        String selectedItem = ddlUrl.getSelectionModel().getSelectedItem().toString();//get the user selected url combo-box
        if(selectedItem.substring(0, 5).equals("https")){//if https without lock icon, +5 point
            score.setUrl(5);
        }else if (selectedItem.substring(0, ICON_IDENTIFIER.length()).equals(ICON_IDENTIFIER)){//else if https with lock icon, +10 point
            score.setUrl(10);
        }
        System.out.println("Points by URL: " + score.getUrl());
    }

    private void calcPointByPassword(Score score){
        String pass = txtPassword.getText();
        /*criteria of point for password strength rule, if matches rules +x Points, else +0 points
        +10 points, for length if 8 and above
        +10 points, if got lowercase
        +10 points, if got uppercase
        +10 points, if got digits
        +10 points, if got symbol
        */
        boolean lowercaseFlag = false;
        boolean uppercaseFlag = false;
        boolean numberFlag = false;
        String regexSymbol = "[:?!@#$%^&*()]";

        if (pass.length() >= 8)//pass length check
            userScore.setLength(10);

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
        userScore.setLowercase(lowercaseFlag? 10: 0);
        userScore.setUppercase(uppercaseFlag? 10: 0);
        userScore.setDigits(numberFlag? 10: 0);

        //check if password contain symbol
        Pattern pattern = Pattern.compile(regexSymbol);
        Matcher matcher = pattern.matcher(pass);
        if (matcher.find())
            userScore.setSymbol(10);
    }

    private void urlPopup(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("URL");
        alert.setContentText("PLEASE SELECT THE MOST SECURE URL ");
        alert.showAndWait();
    }

    private void loginInfoPopup(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("REGISTRATION");
        alert.setContentText("To prevent unauthorized access to your account, you are required to create a username and password." +
                "\n\n" +
                "Your password should include:" +
                "\n1. More than 8 characters" +
                "\n2. Alphabets and numbers" +
                "\n3. Special character (optional)");
        alert.showAndWait();
    }

    class UrlListCell extends ListCell<String> {//custom class for the combo-bos URL since got icon, we need to create our own ListCell
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
                    Label lblHttps = new Label(item.substring(ICON_IDENTIFIER.length(), ICON_IDENTIFIER.length() + "https://".length()));
                    lblHttps.setStyle("-fx-text-fill: GREEN;");
                    Label lblUrl = new Label(item.substring(ICON_IDENTIFIER.length() + "https://".length()));
                    lblUrl.setStyle("-fx-text-fill: BLACK");
                    HBox hBox = new HBox(imageView, lblHttps, lblUrl);
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

    class Score{
        /*criteria of point for password strength rule, if matches rules +x Points, else +0 points
        +10 points, for length if 8 and above
        +10 points, if got lowercase
        +10 points, if got uppercase
        +10 points, if got digits
        +10 points, if got symbol
        */
        private int length;
        private int lowercase;
        private int uppercase;
        private int digits;
        private int symbol;
        private int url;

        public Score() {
        }

        public int getLength() {
            return length;
        }

        public void setLength(int length) {
            this.length = length;
        }

        public int getLowercase() {
            return lowercase;
        }

        public void setLowercase(int lowercase) {
            this.lowercase = lowercase;
        }

        public int getUppercase() {
            return uppercase;
        }

        public void setUppercase(int uppercase) {
            this.uppercase = uppercase;
        }

        public int getDigits() {
            return digits;
        }

        public void setDigits(int digits) {
            this.digits = digits;
        }

        public int getSymbol() {
            return symbol;
        }

        public void setSymbol(int symbol) {
            this.symbol = symbol;
        }

        public int getUrl() {
            return url;
        }

        public void setUrl(int url) {
            this.url = url;
        }

        public int getTotScore(){
            return  length+lowercase+uppercase+digits+symbol+url;
        }
    }
}
