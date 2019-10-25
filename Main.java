/**
 * Program allows users to create Game and store Game objects. Information stored includes
 * the title of the game, the system the game is played on, as well as details about current
 * progress, ownership status, and game edition details. Additionally, program will allow
 * users to create and manage a checklist of tasks they would like complete while playing
 * the game. Task lists are unique to each game.
 *
 * The program's main window lists the title of each game already created, and includes
 * buttons to add more games and print basic details about each game in a TextArea. Games
 * in the TextArea are alphabetized by Game Title.
 *
 * Disclaimer: The following program was inspired by another service I have used for several
 * years called "The Backloggery" (http://www.backloggery.com). This program is not meant to be
 * an extension or re-creation of "The Backloggery", nor do I claim ownership of any
 * intellectual property found within this service. Any similarities in field names or layout
 * are coincidental, or because they were deemed to offer the most clarity or be the most
 * efficient. The state of this program is likely to change as I continue to make modifications
 * and improvements.
 *
 * Name: Nathan Heidt
 * Date: 10/14/2019
 */

package ProgManager;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("GameMgmtInterface.fxml"));
        primaryStage.setTitle("Game Progress Manager App");
        primaryStage.setScene(new Scene(root, 600, 500 ));
        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
