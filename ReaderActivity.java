package org.strykeforce.qrscannergenerator;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import com.firebase.client.Firebase;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.Scanner;

public class ReaderActivity extends AppCompatActivity {
    private Button scan_btn;
    private CheckBox[] checkboxes = new CheckBox[6]; //refers to off/on checkboxes images
    private String scanResult;
    private static final String FIREBASE_URL = "https://testproj1-dc6de.firebaseio.com/"; //set to URL of firebase to send to
    private Firebase firebaseRef;
    private static final int NUM_ELEMENTS_SENDING = 17;
    private ChatMessage[] scoutingData = new ChatMessage[6];
    private int curScoutID;
    private int matchNumber=1;
    private GoogleApiClient client;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) { //method that creates everything when app is opened
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader); //sets to layout of app

        //need to count scored balls, not shot balls
        //number gears  climbing yes/no fast/slow auto place gear? score balls - count. shooting location in key or out key or both
        //low goal number of dumps
        //floor pickup / hopper outside field pickup check box
        //defender on them yes/no
        //playing defense yes/no
        //qualitative - broken down / auto movement / notes / defense

        //initializes firebase to be able to send data to that URL
        Firebase.setAndroidContext(this);
        firebaseRef = new Firebase(FIREBASE_URL);

        //initializes all off/on check boxes
        checkboxes[0] = (CheckBox) findViewById(R.id.checkRed1);
        checkboxes[1] = (CheckBox) findViewById(R.id.checkRed2);
        checkboxes[2] = (CheckBox) findViewById(R.id.checkRed3);
        checkboxes[3] = (CheckBox) findViewById(R.id.checkBlue1);
        checkboxes[4] = (CheckBox) findViewById(R.id.checkBlue2);
        checkboxes[5] = (CheckBox) findViewById(R.id.checkBlue3);
        //sets visibility of check boxes
        for (int j = 0; j < checkboxes.length; j++) {
            checkboxes[j].setChecked(false);
        }

        /*
        STORE BUTTON WILL STORE LOCALLY
        findViewById(R.id.storeButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storeData();
            }
        });*/

        final TextView matchDisplay = (TextView)findViewById(R.id.matchNumView);
        findViewById(R.id.nextMatchButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextMatch();
                matchDisplay.setText("Match: " + Integer.toString(matchNumber));
            }
        });


        //sends message when send button clicked
        final AlertDialog.Builder builderSend = new AlertDialog.Builder(this);
        builderSend.setTitle("STORE AND SEND MATCH?");
        builderSend.setMessage("Are you sure you want to send the data? Do you have ALL the scouting data?");
        findViewById(R.id.sendButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builderSend.setPositiveButton("YES", new DialogInterface.OnClickListener() { //sets what the yes option will do
                    public void onClick(DialogInterface dialog, int which) {
                        sendMessage(); //calls method to restart match
                        dialog.dismiss(); //closes dialog box
                    }
                });
                builderSend.setNegativeButton("NO", new DialogInterface.OnClickListener() { //sets what the no option will do
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss(); //closes dialog box
                    }
                });
                AlertDialog alert = builderSend.create();
                alert.show();
                TextView msgTxt = (TextView) alert.findViewById(android.R.id.message);
                msgTxt.setTextSize((float)35.0);
            }
        });

        
        //first, opens a dialog box to check if they are sure with clearing the match, then clears current stored data and resets checkboxes
        final AlertDialog.Builder builderReset = new AlertDialog.Builder(this);
        builderReset.setTitle("RESET MATCH?");
        builderReset.setMessage("Are you sure you want to clear the data and restart this match?");
        findViewById(R.id.resetButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builderReset.setPositiveButton("YES", new DialogInterface.OnClickListener() { //sets what the yes option will do
                    public void onClick(DialogInterface dialog, int which) {
                        resetMatch(); //calls method to restart match
                        dialog.dismiss(); //closes dialog box
                    }
                });
                builderReset.setNegativeButton("NO", new DialogInterface.OnClickListener() { //sets what the no option will do
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss(); //closes dialog box
                    }
                });
                final AlertDialog alert = builderReset.create();
                System.out.println(DialogInterface.BUTTON_NEGATIVE);
                alert.show();
                TextView msgTxt = (TextView) alert.findViewById(android.R.id.message);
                msgTxt.setTextSize((float)35.0);

            }
        });

        //initializes scan button and sets scan button to open camera and scan when pressed
        scan_btn = (Button) findViewById(R.id.scan_btn);
        final Activity activity = this;
        scan_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrator = new IntentIntegrator(activity);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setPrompt("Scan");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    //method that scans QR code from camera and stores in a string
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "You cancelled the scanning", Toast.LENGTH_LONG).show();
            } else {
                scanResult = result.getContents(); //gets data from QR code and stores in private string
                //Toast.makeText(this, scanResult, Toast.LENGTH_LONG).show(); //displays data from QR code on screen
                storeData();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    //change reset button, check for all 6 scouts before sending message
    public void storeData(){
        //gets QR results from private string
        String message = scanResult;

        if(!message.equals(""))
        {
            ChatMessage sendingObj = findElements(message);
            scoutingData[curScoutID-1] = sendingObj;
            scanResult = "";
        }
    }

    public void nextMatch(){
        matchNumber++;
    }

    //sends data as a single object to firebase
    public void sendMessage() {
        //makes new object and calls findElements method as to push a single chatElement object to firebase
        firebaseRef.push().setValue(scoutingData);
        scoutingData = new ChatMessage[6];
        for(int j=0; j<checkboxes.length; j++)
        {
            checkboxes[j].setChecked(false);
        }
    }

    //extracts data from QR string and returns it in a single element
    public ChatMessage findElements(String tempScanResult) {
        Scanner scan = new Scanner(tempScanResult); //makes scanner out of string for ease of extraction
        String tempLine, tempString;
        int indexEl;
        String elements[] = new String[NUM_ELEMENTS_SENDING];

        //for each element, stores in object
        for (int j = 0; j < NUM_ELEMENTS_SENDING; j++) {
            tempLine = scan.nextLine(); //gets line with element
            indexEl = tempLine.indexOf(':'); //extracts number after colon and stores in array
            elements[j] = tempLine.substring(indexEl + 2);
            if (j==0) //sets check button on/off of which scouter it received from
            {
                curScoutID = Integer.parseInt(elements[0]);
                checkboxes[curScoutID - 1].setChecked(true);
            }
        }
        ChatMessage sendingChat = new ChatMessage(elements);
        return sendingChat;
    }

    //clears the current match stores and resets the checkboxes
    public void resetMatch()
    {
        scoutingData = new ChatMessage[6];
        for(int j=0; j<checkboxes.length; j++)
        {
            checkboxes[j].setChecked(false);
        }
    }

    private static int countLines(String str){
        String[] lines = str.split("\r\n|\r|\n");
        return  lines.length;
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.*/

    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Reader Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
