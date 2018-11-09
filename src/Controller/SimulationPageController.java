package Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SimulationPageController extends Navigation implements Initializable {

    private String ANSWER_FILELOC = "C:\\Users\\moonl\\OneDrive\\Documents\\BabaWork\\BabaWork\\src\\BruteForceCode\\answer.txt";
    private String HTML_FILELOC = "C:\\Users\\moonl\\OneDrive\\Documents\\BabaWork\\BabaWork\\src\\BruteForceCode\\htmlBruteForce.txt";
    private String PYTHON_FILELOC = "C:\\Users\\moonl\\OneDrive\\Documents\\BabaWork\\BabaWork\\src\\BruteForceCode\\pythonBruteForce.txt";
    private String BLANK_ANSWER_SPACE = "_______________________________________________";
    private int SCORE_DIALOG_WIDTH = 800;
    private int SCORE_DIALOG_HEIGHT = 400;

    @FXML
    private ToggleButton btnBack;
    @FXML
    private ToggleButton btnRetry;
    @FXML
    private Button btnRunCode;
    @FXML
    private Button btnExit;
    @FXML
    private VBox vBoxHtml;
    @FXML
    private VBox vBoxPython;
    @FXML
    private VBox vBoxAnswerContainer;

    private List<String> listAnswers;//read from answer.txt
    private Map<Integer ,Label> mapLblAnswer;// contain lineNumber and the text of the line, ex. 20. print("Tester")

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listAnswers = getAnswerFromFile();
        mapLblAnswer = new HashMap<>();
        readTextFromFileAndAppendToPane(vBoxHtml, HTML_FILELOC);
        readTextFromFileAndAppendToPane(vBoxPython, PYTHON_FILELOC);

        for (Label answer:  //populate the right side with answer label with drag-n-drop event and styling
             mapLblAnswer.values()) {
            answer.setWrapText(true);
            answer.setPadding(new Insets(5, 5, 5, 5));
            answer.minWidthProperty().bind(vBoxAnswerContainer.widthProperty());
            answer.setStyle("-fx-border-width: 3px; -fx-border-color: #FF5959; -fx-text-fill: #FF5959; -fx-font-size: 18px;");
            answer.setOnDragDetected(event -> {//allocate drag event for the answer label
                Dragboard dragboard = answer.startDragAndDrop(TransferMode.ANY);
                ClipboardContent clipboardContent = new ClipboardContent();
                clipboardContent.putString(answer.getText());
                dragboard.setContent(clipboardContent);
                event.consume();
            });
            vBoxAnswerContainer.getChildren().add(answer);
        }

    }

    private boolean isAllBlankAnswer(){
        Pattern pattern = Pattern.compile("([\"'])(\\\\?.)*?\\1");//get label text from node
        Matcher m = pattern.matcher(vBoxPython.getChildren().toString());
        while(m.find()) {
            String line = m.group(0).replace("'", "");
            line = line.replace("\t", "");
            if(line.equals("_______________________________________________"))
                return false;
        }
        return true;
    }


    private int getNumberOfCorrectAnswer(){//return the number of correctly drag-n-drop user input
        int correct = 0;
        Pattern pattern = Pattern.compile("([\"'])(\\\\?.)*?\\1");//get label text from node
        Matcher m = pattern.matcher(vBoxPython.getChildren().toString());
        int lineCount = 0;
        while(m.find()){
            String line = m.group(0).replace("'", "");
            line = line.replace("\t", "");
            if(mapLblAnswer.keySet().contains(lineCount)){//check if the line(label) number is the answer line
                System.out.println("Matches: " + line);
                System.out.println("Matches: " + mapLblAnswer.get(lineCount).getText());
               if(mapLblAnswer.get(lineCount).getText().equals(line)){//check if user answer is the same as the given answer
                    System.out.println("Matches: " + line);
                    correct++;
               }
            }
            lineCount++;
        }
        return correct;
    }

    //read from a text file and populate the Vbox pane with label for each line in the text file
    private void readTextFromFileAndAppendToPane (VBox pane, String fileName){
        try{
            Scanner sc = new Scanner(new File(fileName));
            int tabCount;
            int lineCount = 0;
            while(sc.hasNextLine()){
                String line = sc.nextLine();
                String lineWithoutTab = line.replace("\t", "");

                if(listAnswers.contains(lineWithoutTab)){//find if answer is in the pythonBruteForce.txt, if it's replace it with BLANK_ANSWER_SPACE
                    StringBuilder sbBlankAnswer = new StringBuilder();
                    tabCount = 0;
                    mapLblAnswer.put(lineCount, new Label(lineWithoutTab)); // to populate the right container with answer label
                    Pattern tabPattern = Pattern.compile("[\t]");
                    Matcher m = tabPattern.matcher(line);

                    while(m.find()) tabCount++;
                    for(int i = 0; i<tabCount; i++) sbBlankAnswer.append("\t");

                    sbBlankAnswer.append(BLANK_ANSWER_SPACE);
                    Label lblAnswerBlank = new Label(sbBlankAnswer.toString());
                    lblAnswerBlank.setStyle("-fx-text-fill: #4C9DA6");
                    lblAnswerBlank.setUserData(lineCount + "," +lineWithoutTab);

                    lblAnswerBlank.setOnDragOver(event -> {     //drag-over Event for the blank label
                        if(event.getGestureSource() != mapLblAnswer && event.getDragboard().hasString()){
                            event.acceptTransferModes(TransferMode.ANY);
                        }
                        event.consume();
                    });
                    lblAnswerBlank.setOnDragDropped(event -> {      //on drag drop, set the underline to the dragged item text
                        Dragboard db = event.getDragboard();
                        if (db.hasString()){
                            lblAnswerBlank.setText(sbBlankAnswer.toString().replace(BLANK_ANSWER_SPACE, db.getString()));
                        }
                    });
                    pane.getChildren().add(lblAnswerBlank);
                }else {
                    pane.getChildren().add(new Label(line));
                }
                lineCount++;
            }
        }catch (FileNotFoundException ex){
            System.out.println(fileName + "Not found");
            ex.printStackTrace();
        }

    }

    private List<String> getAnswerFromFile(){//populate the lis with lines from answer.txt
        List<String> listStr = new ArrayList<>();
        try{
            Scanner sc = new Scanner(new File(ANSWER_FILELOC));
            while (sc.hasNextLine()){
                listStr.add(sc.nextLine());
            }
        }catch (FileNotFoundException ex){
            System.out.println("answer.txt not found");
            ex.printStackTrace();
        }
        return listStr;
    }

    @FXML
    private void btnBackPressed(ActionEvent actionEvent){
        closeCurScene(actionEvent);
        loadStage("/Fxml/LoginPage.fxml", actionEvent);
    }

    @FXML
    private void btnRetryPressed(ActionEvent actionEvent){
        closeCurScene(actionEvent);
        loadStage("/Fxml/SimulationPage.fxml", actionEvent);
    }
    @FXML
    private void btnRunCodePressed(ActionEvent actionEvent){
        if (isAllBlankAnswer())
            showScoreDialog(actionEvent);
        else
            showAlertDialog();
    }

    private void showScoreDialog(ActionEvent actionEvent){//pop-up a score dialog to show score based on user drag-n-drop input
        final Stage dialog = new Stage();
        dialog.setResizable(false);
        dialog.initModality(Modality.APPLICATION_MODAL);
        BorderPane dialogBorderPane = new BorderPane();

        Text txtPreScore = new Text("YOU HAVE SCORED");
        Text txtScore = new Text(String.valueOf(getNumberOfCorrectAnswer()));
        Text txtPostScore = new Text(" OUT OF " + mapLblAnswer.size() + " QUESTIONS RIGHT");
        Text txtLinkInfo = new Text("Click on the link below to know more about brute force attack");
        txtLinkInfo.setStyle("-fx-text-fill: #FF5959");

        txtPreScore.setStyle("-fx-font-family: Abel; -fx-text-fill: #393D46; -fx-font-size: 24px");
        txtPostScore.setStyle("-fx-font-family: Abel; -fx-text-fill: #393D46; -fx-font-size: 24px;");
        txtScore.setStyle("-fx-font-family: Abel; -fx-text-fill: #4C9DA6; -fx-font-size: 24px;");
        txtLinkInfo.setStyle("-fx-font-family: Abel; -fx-text-fill: #4C9DA6; -fx-font-size: 18px;");

        HBox scoreHBox = new HBox(txtScore, txtPostScore);
        scoreHBox.setAlignment(Pos.CENTER);
        Hyperlink bruteForceAttackLink = new Hyperlink();
        bruteForceAttackLink.setText("Click Here");

        Button btnMainMenu = new Button("MAIN MENU");
        btnMainMenu.setOnMouseClicked(event -> {
            dialog.close();
            closeCurScene(actionEvent);
            loadStage("/Fxml/MenuPage.fxml", actionEvent);
        });

        VBox centerVbox = new VBox(txtPreScore, scoreHBox, txtLinkInfo, bruteForceAttackLink);
        centerVbox.setAlignment(Pos.CENTER);
        centerVbox.setPadding(new Insets(50, 20, 50, 20));
        centerVbox.setSpacing(5);
        dialogBorderPane.setTop(centerVbox);
        dialogBorderPane.setStyle("-fx-background-color: #FFAD5B");

        HBox btnPaneContainer = new HBox();
        btnPaneContainer.setAlignment(Pos.BOTTOM_RIGHT);
        btnPaneContainer.setPadding(new Insets(5, 5, 5, 5));
        btnPaneContainer.minWidth(dialogBorderPane.getWidth());
        btnPaneContainer.getChildren().add(btnMainMenu);
        dialogBorderPane.setBottom(btnPaneContainer);

        Scene dialogScene = new Scene(dialogBorderPane, SCORE_DIALOG_WIDTH, SCORE_DIALOG_HEIGHT);
        dialog.setScene(dialogScene);
        dialog.show();
    }

    private void showAlertDialog(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText("All blank must be filled in");
        alert.showAndWait();
    }

    @FXML
    private void btnExitPressed(ActionEvent actionEvent) {
        closeCurScene(actionEvent);
        loadStage("/Fxml/MenuPage.fxml", actionEvent);
    }

    @FXML
    private void vBoxHtmlDragDrop(DragEvent dragEvent) {
    }

    @FXML
    private void vBoxHtmlDragOver(DragEvent dragEvent) {
    }

}
