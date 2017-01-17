package org.strykeforce.qrscannergenerator;

public class ChatMessage {
    private int scoutID;
    private int teamNumber;
    private int matchNumber;

    public ChatMessage(){

    }

    public ChatMessage(int[] data){

        this.scoutID = data[0];
        this.teamNumber = data[1];
        this.matchNumber = data[2];
    }
    public int getTeamNumber()
    {
        return teamNumber;
    }

    public int getMatchNumber()
    {
        return matchNumber;
    }

    public int getScoutID() {
        return scoutID;
    }
}