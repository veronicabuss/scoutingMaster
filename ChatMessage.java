package org.strykeforce.qrscannergenerator;

public class ChatMessage {
    private String scoutID,teamNumber,matchNumber,autoHigh,autoLow,autoGear,teleHigh,teleLow,teleGear;
    private String baseLineCross,canPickGearOffGround,playsDefense,highShotDefended,touchPad,climbRopeTime,scoutName,notes;
    private int scoutIDint,teamNumberint,matchNumberint,autoHighint,autoLowint,autoGearint,teleHighint,teleLowint,teleGearint;
    private int baseLineCrossint,canPickGearOffGroundint,playsDefenseint,highShotDefendedint,touchPadint,climbRopeTimeint,scoutNameint,notesint;
    //int numInt = 15, numStg = 2;
    public ChatMessage() {
    }
    /*
    SCOUT ID int
    TEAM NUM int
    MATCH NUM int

    Auto High int
    Auto Low int
    Auto Gears int

    Tele High int
    Tele Low int
    Tele Gears int
    Climb Rope Time int

    Crosses base line BOOL
    Picks gear off ground BOOL
    On defence BOOL
    Defended shooting high  BOOL
    Touchpad BOOL
    Scout Name StrING
    Notes STRING
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

    public ChatMessage(int[] data, String[] names)
    {
        this.scoutIDint = data[0];
        this.teamNumberint = data[1];
        this.matchNumberint = data[2];
        this.autoHighint = data[3];
        this.autoLowint = data[4];
        this.autoGearint = data[5];

        this.teleHighint = data[6];
        this.teleLowint = data[7];
        this.teleGearint = data[8];

        this.baseLineCrossint = data[9];
        this.canPickGearOffGroundint = data[10];
        this.playsDefenseint = data[11];
        this.highShotDefendedint = data[12];
        this.touchPadint = data[13];
        this.climbRopeTimeint = data[14];

        this.scoutName = names[0];
        this.notes = names[1];
    }
        /*
    SCOUT ID int
    TEAM NUM int
    MATCH NUM int
    Auto High int
    Auto Low int
    Auto Gears int
    Tele High int
    Tele Low int
    Tele Gears int
    Climb Rope Time int
    Crosses base line BOOL
    Picks gear off ground BOOL
    On defence BOOL
    Defended shooting high  BOOL
    Touchpad BOOL
    Scout Name StrING
    Notes STRING
    {"phonetype":"N95","cat":"WP"}
     */
    public String jsonString()
    {
        String stg = "{\"Scout ID\":" + this.scoutIDint + ",\"Team Num\":" + this.teamNumberint + ",\"Match Num\":" + this.matchNumberint+"}";
        System.out.println(stg);
        return stg;
    }

    public int getTeamNumberInt() {
        return teamNumberint;
    }

    public int getScoutID() {
        return scoutIDint;
    }

    public int getBaseLineCross() {
        return baseLineCrossint;
    }

    public int getAutoGear() {
        return autoGearint;
    }

    public int getPlaysDefense() {
        return playsDefenseint;
    }

    public int getCanPickGearOffGround() {
        return canPickGearOffGroundint;
    }

    public int getHighShotDefended() {
        return highShotDefendedint;
    }

    public int getTouchPad() {
        return touchPadint;
    }

    public int getMatchNumberInt() {return matchNumberint;}

    public int getAutoHigh() {return autoHighint;}

    public int getAutoLow() {return autoLowint;}

    public int getTeleHigh() {return teleHighint;}

    public int getTeleLow() {return teleLowint;}

    public int getTeleGear() {return teleGearint;}

    public int getClimbRopeTime() {return climbRopeTimeint;}

    public String getScoutName() {return scoutName;}

    public String getNotes() {return notes;}
}
