package Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
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
    private int SCORE_DIALOG_HEIGHT = 500;

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

    private List<String> listAnswers;
    private Map<Integer ,Label> mapLblAnswer;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listAnswers = getAnswerFromFile();
        mapLblAnswer = new HashMap<>();
        readTextFromFileAndAppendToPane(vBoxHtml, HTML_FILELOC);
        readTextFromFileAndAppendToPane(vBoxPython, PYTHON_FILELOC);

//        final ToggleButton toggle = new ToggleButton();
//        toggle.getStylesheets().add(this.getClass().getResource(
//                "imagetogglebutton.css"
//        ).toExternalForm());
//        toggle.setMinSize(148, 148); toggle.setMaxSize(148, 148);
//        stage.setScene(new Scene(
//                StackPaneBuilder.create()
//                        .children(toggle)
//                        .style("-fx-padding:10; -fx-background-color: cornsilk;")
//                        .build()
//        ));
//        stage.show();
        for (Label answer:  //populate the right container with answer label with event and style
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

    private int getNumberOfCorrectAnswer(){
        int correct = 0;
        Pattern pattern = Pattern.compile("([\"'])(\\\\?.)*?\\1");//get label text from node
        Matcher m = pattern.matcher(vBoxPython.getChildren().toString());
        int lineCount = 0;
        while(m.find()){
            String line = m.group(0).replace("'", "");
            line = line.replace("\t", "");
            if(mapLblAnswer.keySet().contains(lineCount)){
                System.out.println("Matches: " + line);
                System.out.println("Matches: " + mapLblAnswer.get(lineCount).getText());
                if(mapLblAnswer.get(lineCount).getText().equals(line)){
                    System.out.println("Matches: " + line);
                    correct++;
                }
            }
            lineCount++;
        }
        return correct;
    }

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
                    lblAnswerBlank.setStyle("-fx-text-fill: BLUE");
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

    private List<String> getAnswerFromFile(){
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
        final Stage dialog = new Stage();
        dialog.setResizable(false);
        dialog.initModality(Modality.APPLICATION_MODAL);
        VBox dialogVbox = new VBox(20);
        dialogVbox.setAlignment(Pos.CENTER);

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

        Button btnMainMenu = new Button("MAIN MENU");
//        btnMainMenu.setAlignment(Pos.BASELINE_RIGHT);
        btnMainMenu.setOnMouseClicked(event -> {
            dialog.close();
            closeCurScene(actionEvent);
            loadStage("/Fxml/MenuPage.fxml", actionEvent);
        });

        dialogVbox.getChildren().add(txtPreScore);
        dialogVbox.getChildren().add(scoreHBox);
        dialogVbox.getChildren().add(txtLinkInfo);
        dialogVbox.setStyle("-fx-background-color: #FFAD5B");

        Hyperlink bruteForceAttackLink = new Hyperlink();
        bruteForceAttackLink.setText("Click Here");
        dialogVbox.getChildren().add(bruteForceAttackLink);

        HBox btnPaneContainer = new HBox();
        btnPaneContainer.setAlignment(Pos.BOTTOM_RIGHT);
        btnPaneContainer.setPadding(new Insets(5, 5, 5, 5));
        btnPaneContainer.minWidth(dialogVbox.getWidth());
        btnPaneContainer.getChildren().add(btnMainMenu);
        dialogVbox.getChildren().add(btnPaneContainer);


        Scene dialogScene = new Scene(dialogVbox, SCORE_DIALOG_WIDTH, SCORE_DIALOG_HEIGHT);
        dialog.setScene(dialogScene);
        dialog.show();
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
