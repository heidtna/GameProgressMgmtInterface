package ProgManager;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class GameMgmtController implements Initializable {
    @FXML private Button btnAdd, btnPrint, btnEdit;
    @FXML private AnchorPane ancGames, ancPrint;
    @FXML private VBox vbxGames;
    @FXML private TextArea txtArPrint;
    @FXML private TableView tblVwPrint;

    @FXML
    public void addGames() throws IOException {

        //Change to EditGame window when 'Add Game' button pressed
        Stage stage = (Stage) btnEdit.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("GameEditInterface.fxml"));

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void printGames() throws IOException {
        LinkedList<Game> gameList = new LinkedList<>();
        gameList.addAll(EditGameController.getGamesList());

        String areaText = "";//String to load into TextArea when 'Print Games' is pressed
        String strForm = "%-3s %-25s %-20s %-15s %-20s %-20s%n";//Set column format

        //Create column names and header separator
        String printHeaders = String.format(strForm, "ID", "Title", "System",
                                                     "Progress", "Ownership", "Edition");
        printHeaders += String.format("%104s", "").replace(' ', '=');

        //Display Text Area information only when at least one game has been entered. If no games
        //have been entered, display alert asking user if they would like to add a game.
        if (vbxGames.getChildren().isEmpty()) {
            Alert noGames = new Alert(Alert.AlertType.INFORMATION);
            ButtonType btnTypeConfirm = new ButtonType("Yes");
            ButtonType btnTypeDeny = new ButtonType("No");
            noGames.getButtonTypes().setAll(btnTypeConfirm, btnTypeDeny);

            noGames.setHeaderText("No games entered");
            noGames.setContentText("Would you like to add a game?");
            Optional<ButtonType> result = noGames.showAndWait();

            //Displays Game Edit window if user selects "Yes"
            if (result.get().getText() == "Yes") {
                Stage stage = (Stage) btnEdit.getScene().getWindow();
                Parent root = FXMLLoader.load(getClass().getResource("GameEditInterface.fxml"));

                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }
        }

        else {
            txtArPrint.setText(printHeaders);
            areaText += txtArPrint.getText() + '\n';//Load areaText with headers
        }

        Collections.sort(gameList);
        for (int i = 0; i < gameList.size(); ++i) {

            //Create tmp variables to shorten formatting statements
            String tmpID = String.valueOf(gameList.get(i).getGameID());
            String tmpTitle = gameList.get(i).getTitle();
            String tmpSys = gameList.get(i).getSystem();
            String tmpProg = gameList.get(i).getProgStatus();
            String tmpOwn = gameList.get(i).getOwnStatus();
            String tmpEd = gameList.get(i).getEditionType();

            //Set formatted Game information into TextArea and load
            //into areaText one line at a time
            txtArPrint.setText(String.format(strForm, tmpID, tmpTitle, tmpSys,
                                                      tmpProg, tmpOwn, tmpEd));
            areaText += txtArPrint.getText();


        }
        System.out.println(areaText);
        txtArPrint.setText(areaText); //Final loading of areaText string into TextArea

        //tblVwPrint.getItems().add(gameList.get(0).getTitle());

    }
/*
To be implemented at a later date. Will allow user to edit information stored
in Game objects that have already been created.

    public void editGames(Event e) throws IOException {
        System.out.println("Test");

        Stage stage = (Stage) vbxGames.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("GameEditInterface.fxml"));

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        LinkedList<Game> tmp = new LinkedList<>();
        ArrayList<Text> games = new ArrayList<>();

        tmp.addAll(EditGameController.getGamesList());//Make local copy of Games list

        //Get Game title attributes load them into 'games' ArrayList
        for (int i = 0; i < tmp.size(); ++i) {
            Text tmpTxt = new Text(tmp.get(i).getTitle());
            games.add(tmpTxt);
        }

        vbxGames.getChildren().addAll(games);//Load Game titles into interface
    }
}
