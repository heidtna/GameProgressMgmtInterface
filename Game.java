package ProgManager;

import javafx.scene.control.CheckBox;
import java.util.ArrayList;

public class Game implements Comparable {
    private static int nextGameID = 1;

    private int gameID;
    private String title;
    private String system;
    private String progStatus;
    private String progDetail;
    private String mastDetail;
    private String ownStatus;
    private String copyType;
    private String editionType;
    private String editionDetails;
    //private ArrayList<String> strTasks;
    private ArrayList<CheckBox> chkTasks;

    Game() {
        gameID = nextGameID;
        ++nextGameID;
    }

    Game(String Title, String System, String Progress, String Ownership,
            String CopyType, String Edition) {
        gameID = nextGameID;
        ++nextGameID;

        title = Title;
        system = System;
        progStatus = Progress;
        ownStatus = Ownership;
        copyType = CopyType;
        editionType = Edition;
    }

    public int getGameID() { return gameID; }

    public void setTitle(String newTitle) { title = newTitle; }
    public String getTitle() { return title; }

    public void setSystem(String newSystem) { system = newSystem; }
    public String getSystem() { return system; }

    public void setProgStatus(String progStatus) { this.progStatus = progStatus; }
    public String getProgStatus() { return progStatus; }

    public void setProgDetail(String progDetail) { this.progDetail = progDetail; }
    public String getProgDetail() { return progDetail; }

    public void setMastDetail(String mastDetail) {this.mastDetail = mastDetail; }
    public String getMastDetail() { return mastDetail; }

    public void setOwnStatus(String ownStatus) { this.ownStatus = ownStatus; }
    public String getOwnStatus() { return ownStatus; }

    public void setCopyType(String copyType) { this.copyType = copyType; }
    public String getCopyType() { return copyType; }

    public void setEditionType(String editionType) { this.editionType = editionType; }
    public String getEditionType() { return editionType; }

    public void setEditionDetails(String editionDetails) { this.editionDetails = editionDetails; }
    public String getEditionDetails() { return editionDetails; }

    //public void setTasks(ArrayList<String> tasks) { strTasks = tasks; }
    //public ArrayList<String> getTasks() { return strTasks; }

    public void setCheckTasks(ArrayList<CheckBox> chkTasks) { this.chkTasks = chkTasks; }
    public ArrayList<CheckBox> getTaskList() { return chkTasks; }

    private static int strLength(String str) {
        int charCt = 0;
        String subStr = str;

        while (subStr != "") {

            //Keep removing first char in string...
            try {
                subStr = subStr.substring(1);
                ++charCt;
            }

            //...until only one char is left
            catch (Exception e) {
                return charCt;
            }
        }

        return charCt;
    }

    @Override
    public int compareTo(Object tmp) {
        if (tmp instanceof Game) {
            Game tmpGame = (Game)tmp;

            //Use length of second object to iterate...
            try {
                for (int i = 0; i < strLength(tmpGame.getTitle()); ++i) {

                    char thisCh = this.getTitle().charAt(i);
                    char tmpCh = tmpGame.getTitle().charAt(i);

                    if (thisCh < tmpCh) return -1;
                    else if (thisCh > tmpCh) return 1;
                }
            }

            //... unless the length of the second object is shorter than the first
            catch (Exception e) {
                return -1;
            }
        }//end if
        return 0;
    }//end compareTo

    public String toString() {
        String tmp = "";

        tmp += "Title: " + this.getTitle() + '\n' +
                "System: " + this.getSystem() + '\n' +
                "Progress: " + this.getProgStatus() + '\n' +
                "Ownership: " + this.getOwnStatus() + '\n' +
                "Copy Type: " + this.getCopyType() + '\n' +
                "Edition Type" + this.getEditionType();

        return tmp;
    }//end toString

}//endgame
