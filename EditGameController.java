package ProgManager;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.security.Key;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Optional;
import java.util.ResourceBundle;

public class EditGameController implements Initializable {

    //Create two ArrayLists to store tasks in parallel
    private static LinkedList<Game> gamesList = new LinkedList<>();
    private ArrayList<CheckBox> taskList = new ArrayList<>();
    //private ArrayList<String> strTaskList = new ArrayList<>();

    //Create all RadioButtons and corresponding toggle groups
    @FXML private RadioButton rdoNoStart, rdoProgress, rdoFinished, rdoComplete, rdoMaster;
    @FXML private RadioButton rdoOwned, rdoPrevious, rdoRented, rdoBorrow;
    @FXML private RadioButton rdoPhysCopy, rdoDigCopy, rdoStdEdi, rdoSpecEdi;
    @FXML private ToggleGroup grpProg, grpOwn, grpCopy, grpEdition;

    //Create other nodes
    @FXML private Button  btnAddTask, btnDelTask;
    @FXML private Button btnSubmit, btnCancel;
    @FXML private TextField txtFldTitle, txtFldProg, txtFldMaster, txtFldSpecEdi;
    @FXML private ImageView imgVwSystemIcon;
    @FXML private ChoiceBox choiceSystem;
    @FXML private HBox hbxProg, hbxMaster, hbxSpecEdi;
    @FXML private FlowPane flPnEdit;
    @FXML private ScrollPane scrlPaneTaskList;

    public static LinkedList<Game> getGamesList() { return gamesList; }

    //Set event to collapse TextFields when corresponding RadioButton is
    //not selected, and expand when it is
    public void collapseProgTextField (Event e) {
        if (e.getSource() == rdoProgress) {
            hbxProg.setPrefHeight(38); hbxProg.setVisible(true);
            hbxMaster.setPrefHeight(0); hbxMaster.setVisible(false);
        }
        else if (e.getSource() == rdoMaster){
            hbxProg.setPrefHeight(0); hbxProg.setVisible(false);
            hbxMaster.setPrefHeight(38); hbxMaster.setVisible(true);
        }
        else {
            hbxProg.setPrefHeight(0); hbxProg.setVisible(false);
            hbxMaster.setPrefHeight(0); hbxMaster.setVisible(false);
        }
    }//end collapseProgTextField

    //Only allow user to select an edition when a physical
    //or digital version of the game is selected.
    public void unlockEditionBtns (Event e) {
        if ( (e.getSource() == rdoPhysCopy) || (e.getSource() == rdoDigCopy) ) {
            rdoStdEdi.setDisable(false); rdoSpecEdi.setDisable(false);
        }
    }

    //Expand Edition Details TextField when RadioButton is selected
    public void collapseEditionTxtField (Event e) {
        if (e.getSource() == rdoSpecEdi) {
            hbxSpecEdi.setVisible(true); hbxSpecEdi.setPrefHeight(38);
        }
        else if (e.getSource() == rdoStdEdi) {
            hbxSpecEdi.setVisible(false); hbxSpecEdi.setPrefHeight(0);
        }
    }

    public void testTaskButtons (Event e) {
        //Color code for HBox node in Titled Pane: #ACA794

        //Utilized StackOverflow to learn how to add content to Scroll Pane on command.
        //URL: https://stackoverflow.com/questions/38546860/javafx-add-children-to-scrollpane
        VBox vbxList = new VBox();
        vbxList.setPadding(new Insets(10, 0, 0, 10));
        vbxList.setSpacing(10);

        //Add custom tasks
        if (e.getSource() == btnAddTask) {
            TextInputDialog newTask = new TextInputDialog();
            newTask.setTitle("New Task"); newTask.setHeaderText("Add a Task or Objective");
            newTask.setContentText("Enter the name of a task to complete:");

            //More information about Optional objects and avoiding 'null pointer' errors
            //URL: https://www.oracle.com/technical-resources/articles/java/java8-optional.html
            Optional<String> result = newTask.showAndWait();


            /*
            Referenced Code Makery to get TextInput dialog to only create a checkbox
            when text (not empty or spaces) is present and 'OK' is pressed. 'isPresent()'
            was added to avoid null pointer errors when 'Cancel' is clicked.
            URL: https://code.makery.ch/blog/javafx-dialogs-official/
             */
            if (result.isPresent() && !newTask.getResult().isBlank()) {
                CheckBox chkTask = new CheckBox();
                chkTask.setText(newTask.getResult());
                taskList.add(chkTask);
                //strTaskList.add(chkTask.getText());
            }
            vbxList.getChildren().addAll(taskList);
            scrlPaneTaskList.setContent(vbxList);
        }//end if AddTask

        //Delete all selected/completed tasks
        else if (e.getSource() == btnDelTask) {

            //index backwards to avoid skipping consecutive indexes while deleting.
            for (int i = (taskList.size() - 1); i >= 0; --i) {
                if (taskList.get(i).isSelected()) {
                    taskList.remove(i);
                    //strTaskList.remove(i);
                }
            }
            vbxList.getChildren().addAll(taskList);
            scrlPaneTaskList.setContent(vbxList);
        }//end else if Del
    }//end testTaskButtons

    //Check fields for nulls and create a 'Game' object with entered or default data
    public void submitEvent (Event e) throws IOException {
        if (e.getSource() == btnSubmit) {
            System.out.println("Submit");
            int result = submitGameInfo();
            if (result == 0) { changeScene(); }
        }//end if Submit

        //Switch scene back to Management interface if Cancel is pressed
        else if (e.getSource() == btnCancel) {
            changeScene();
        }
    }

    private void editGame() {
        Game tmpGame = new Game(); //Create new Game object

        //Set Game attributes with values from View
        tmpGame.setTitle(txtFldTitle.getText());
        tmpGame.setSystem(choiceSystem.getValue().toString());
        tmpGame.setProgStatus(grpProg.getSelectedToggle().getUserData().toString());

        //Set Progress Details only if 'Progress' was set to 'In-Progress'...
        if (grpProg.getSelectedToggle().getUserData().toString() == "In-Progress") {
            tmpGame.setProgDetail(txtFldProg.getText());
        }

        //...or set Master Details if 'Master' was set
        else if (grpProg.getSelectedToggle().getUserData().toString() == "Mastered") {
            tmpGame.setMastDetail(txtFldMaster.getText());
        }
        tmpGame.setOwnStatus(grpOwn.getSelectedToggle().getUserData().toString());
        tmpGame.setCopyType(grpCopy.getSelectedToggle().getUserData().toString());
        tmpGame.setEditionType(grpEdition.getSelectedToggle().getUserData().toString());

        //Set Edition Details if 'Special Edition was set
        if (grpEdition.getSelectedToggle().getUserData().toString() == "Special Edition") {
            tmpGame.setEditionDetails(txtFldSpecEdi.getText());
        }
        tmpGame.setCheckTasks(taskList);//Use taskList ArrayList to set tasks Game attr.

        gamesList.add(tmpGame); //Add Game object to LinkedList

        //Check LinkedList of Games to see if Game already exits.
        //If it does, write over the existing data. If it does
        //not, add the new Game to the end of the list.
        /*
        for (int i = 0; i < gamesList.size(); ++i) {
            if (tmpGame.getTitle() == gamesList.get(i).getTitle()) {
                Alert tmpAlert = new Alert(Alert.AlertType.INFORMATION);
                tmpAlert.setHeaderText("Would you like to save changes to \n\n" +
                                       tmpGame);
            }
        }
        */

        //Testing setters for game object
        System.out.println(tmpGame.getTitle());
        System.out.println(tmpGame.getSystem());
        System.out.println(tmpGame.getProgStatus());
        System.out.print("Progress Detail: "); System.out.println(tmpGame.getProgDetail());
        System.out.print("Master Detail: "); System.out.println(tmpGame.getMastDetail());
        System.out.println(tmpGame.getOwnStatus());
        System.out.println(tmpGame.getCopyType());
        System.out.println(tmpGame.getEditionType());
        System.out.print("Edition Details: "); System.out.println(tmpGame.getEditionDetails());

        System.out.println(tmpGame.getTaskList());


    }

    private void changeScene() throws IOException {
        Stage stage;
        Parent root;

        System.out.println("Cancel");
        stage = (Stage) btnCancel.getScene().getWindow();
        root = FXMLLoader.load(getClass().getResource("GameMgmtInterface.fxml"));

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    private int submitGameInfo() { //throws IOException {
        Alert a = new Alert(Alert.AlertType.NONE);//Set NONE Alert and change as needed

        //Create variables for each field to shorten boolean expressions
        Boolean isTitle = txtFldTitle.getText().isBlank();
        Object isSystem = choiceSystem.getValue();
        Toggle isProgress = grpProg.getSelectedToggle();
        Toggle isOwnership = grpOwn.getSelectedToggle();
        Toggle isCopy = grpCopy.getSelectedToggle();
        Toggle isEdition = grpEdition.getSelectedToggle();

        //Title and System fields required for 'Game' object creation
        if ( isTitle && (isSystem == null) ) {
            System.out.println("Title is blank");
            a.setAlertType(Alert.AlertType.INFORMATION);
            a.setHeaderText("Title and System not set");
            a.setContentText("If you would like to cancel game creation,\n" +
                    "please select the 'Cancel' button below.");
            a.showAndWait();
            return -1;
        }//end if

        else if (isSystem == null) {
            System.out.println("System is blank");
            a.setAlertType(Alert.AlertType.INFORMATION);
            a.setHeaderText("System Type not selected");
            a.setContentText("Please enter a system type, or click \n" +
                    "'Cancel' below to exit game creation.");
            a.showAndWait();
            return -1;
        }//end if

        else if (isTitle) {
            a.setAlertType(Alert.AlertType.INFORMATION);
            a.setHeaderText("Title is not set");
            a.setContentText("Please enter a title for the game, or\n" +
                    "click 'Cancel' below to exit game creation");
            a.showAndWait();
            return -1;
        }//end if

        //Show Alert letting user know that certain values will be set to defaults
        else if (isProgress == null || isOwnership == null || isCopy == null || isEdition == null) {
            String tmp = "Some fields are still blank. The following\n" +
                    "fields will be set to default values:\n \n";
            a.setAlertType(Alert.AlertType.WARNING);
            if (isProgress == null) { tmp += "Progress will be set to 'Not Started'\n"; }
            if (isOwnership == null) { tmp += "Ownership will be set to 'Owned'\n"; }
            if (isCopy == null) {
                tmp += "Copy will be set to 'Physical' and\n" +
                        "Edition will be set to 'Standard'\n";
            }
            else if (isEdition == null) {tmp += "Edition will be set to 'Standard'\n"; }
            tmp += "\nIs this okay?";

            //Referenced Code Makery for details on adding buttons to Dialog
            //URL: https://code.makery.ch/blog/javafx-dialogs-official/
            a.setHeaderText(tmp);
            ButtonType btnTypeConfirm = new ButtonType("Okay");
            ButtonType btnTypeCancel = new ButtonType("Cancel");
            a.getButtonTypes().setAll(btnTypeConfirm, btnTypeCancel);
            a.showAndWait();

            //Set null optional fields to default values on confirm and create Game object
            if (a.getResult() == btnTypeConfirm) {
                if (isProgress == null) { rdoNoStart.setSelected(true); }
                if (isOwnership == null) { rdoOwned.setSelected(true); }

                //If 'Copy' is not selected, enable 'Edition', set default values
                //for both, and create game object
                if (isCopy == null) {
                    rdoPhysCopy.setSelected(true);
                    rdoStdEdi.setDisable(false); rdoSpecEdi.setDisable(false);
                    rdoStdEdi.setSelected(true);

                    editGame();//Create Game object and set attributes

                    //After creating Game, return to Management window
                    //changeScene();

                    return 0;
                }//end if

                //If 'Copy' is set, but 'Edition' is not, set default for
                //'Edition' and create Game object
                else if (isEdition == null) {
                    rdoStdEdi.setSelected(true);

                    editGame();//Create Game object and set attributes

                    //After creating Game, return to Management window
                    //changeScene();

                    return 0;
                }

                editGame();//Create Game object and set attributes

                //After creating Game, return to Management window
                //changeScene();

                return 0;
            }

            return -1;
        }//end Check Fields

        //Executes if all values have already been set prior to pressing 'Submit'
        editGame();//Create Game object and set attributes

        //After creating Game, return to Management window
        //changeScene();

        return 0;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Image used in background was obtained from 'lifehack.org'. Name of artist could not
        //be found. Image name is "Classic Video Games"
        //URL to site: https://www.lifehack.org/articles/technology/100-awesome-minimalist-wallpapers.html
        //URL to image: https://cdn.lifehack.org/wp-content/uploads/2013/04/78.jpg

        choiceSystem.getItems().addAll("Playstation 4", "Xbox One", "Nintendo Switch");

        //Set UserData for RadioButtons so that text name only can be extracted
        rdoNoStart.setUserData("Not Started"); rdoProgress.setUserData("In-Progress");
        rdoFinished.setUserData("Finished"); rdoComplete.setUserData("Complete");
        rdoMaster.setUserData("Mastered");

        rdoOwned.setUserData("Owned"); rdoPrevious.setUserData("Previously Owned");
        rdoRented.setUserData("Rented"); rdoBorrow.setUserData("Borrowed");

        rdoPhysCopy.setUserData("Physical Copy"); rdoDigCopy.setUserData("Digital Copy");
        rdoStdEdi.setUserData("Standard Edition"); rdoSpecEdi.setUserData("Special Edition");

        //Utilized youtube and Geeks for Geeks to learn how to add an event for ChoiceBox
        //URL YouTube: https://www.youtube.com/watch?v=WZGyP57IH6M
        //URL GeeksforGeeks: https://www.geeksforgeeks.org/javafx-choicebox/
        choiceSystem.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                //System.out.println(t1);
                if (t1 == "Playstation 4") {

                    //Icon is the property of Sony Corporation of America.
                    //URL: https://www.playstation.com/en-us/explore/ps4/
                    Image ps4 = new Image("ps4.png"); imgVwSystemIcon.setImage(ps4);
                }

                else if (t1 == "Xbox One") {

                    //Icon is the property of Microsoft Corporation
                    //URL: https://www.xbox.com/en-US/xbox-one?xr=shellnav
                    Image xbox = new Image("xbox1.png"); imgVwSystemIcon.setImage(xbox);
                }

                else if (t1 == "Nintendo Switch") {

                    //Icon is the property of Nintendo Corporation
                    //URL: https://www.nintendo.com/switch/system/
                    Image nin = new Image("ninSwitch.png"); imgVwSystemIcon.setImage(nin);
                }
            }//end change listener
        });
    }//end initialize
}//end EditGameController
