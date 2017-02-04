package org.strykeforce.qrscannergenerator;

public class ChatMessage {
    private String scoutID,teamNumber,matchNumber,autoHigh,autoLow,autoGear,teleHigh,teleLow,teleGear;
    private String baseLineCross,canPickGearOffGround,playsDefense,highShotDefended,touchPad,climbRopeTime,scoutName,notes;

    public ChatMessage() {

    }

    /*
    SCOUT ID
    TEAM NUM
    MATCH NUM

    Auto High
    Auto Low
    Auto Gears

    Tele High
    Tele Low
    Tele Gears

    Crosses base line
    Picks gear off ground
    On defence
    Defended shooting high
    Touchpad
    Climb Rope Time
    Scout Name
    Notes
     */

    public ChatMessage(String[] data) {

        this.scoutID = data[0];
        this.teamNumber = data[1];
        this.matchNumber = data[2];
        this.autoHigh = data[3];
        this.autoLow = data[4];
        this.autoGear = data[5];

        this.teleHigh = data[6];
        this.teleLow = data[7];
        this.teleGear = data[8];

        this.baseLineCross = data[9];
        this.canPickGearOffGround = data[10];
        this.playsDefense = data[11];
        this.highShotDefended = data[12];
        this.touchPad = data[13];
        this.climbRopeTime = data[14];

        this.scoutName = data[15];
        this.notes = data[16];
    }


    public String getTeamNumber() {
        return teamNumber;
    }

    public String getScoutID() {
        return scoutID;
    }

    public String getBaseLineCross() {
        return baseLineCross;
    }

    public String getAutoGear() {
        return autoGear;
    }

    public String getPlaysDefense() {
        return playsDefense;
    }

    public String getCanPickGearOffGround() {
        return canPickGearOffGround;
    }

    public String getHighShotDefended() {
        return highShotDefended;
    }

    public String getTouchPad() {
        return touchPad;
    }

    public String getMatchNumber() {return matchNumber;}

    public String getAutoHigh() {return autoHigh;}

    public String getAutoLow() {return autoLow;}

    public String getTeleHigh() {return teleHigh;}

    public String getTeleLow() {return teleLow;}

    public String getTeleGear() {return teleGear;}

    public String getClimbRopeTime() {return climbRopeTime;}

    public String getScoutName() {return scoutName;}

    public String getNotes() {return notes;}
}