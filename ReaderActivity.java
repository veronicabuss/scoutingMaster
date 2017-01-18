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
import android.widget.Toast;
import com.firebase.client.Firebase;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.Scanner;

import static org.strykeforce.qrscannergenerator.R.id.view;

public class ReaderActivity extends AppCompatActivity {
    private Button scan_btn;
    private CheckBox[] checkboxes = new CheckBox[6]; //refers to off/on checkboxes images
    private String scanResult;
    private static final String FIREBASE_URL = "https://testproj1-dc6de.firebaseio.com/"; //set to URL of firebase to send to
    private Firebase firebaseRef;
    private static final int NUM_ELEMENTS_SENDING = 3; //adjust to how much data will be sent in QR code
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) { //method that creates everything when app is opened
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader); //sets to layout of app

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

        //sends message when send button clicked
        findViewById(R.id.sendButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });



        //clears current stored data and resets checkboxes when reset button clicked
        //NEED TO CREATE DIALOG BOX ARE YOU SURE ALSO SAME WITH NEXT MATCH BUTTON
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("RESET MATCH?");
        builder.setMessage("Are you sure you want to clear the data and restart this match?");
        findViewById(R.id.resetButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        resetMatch();
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();

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
                Toast.makeText(this, scanResult, Toast.LENGTH_LONG).show(); //displays data from QR code on screen
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    //sends data as a single object to firebase
    public void sendMessage() {
        //gets QR results from private string
        String message = scanResult;

        //makes new object and calls findElements method as to push a single chatElement object to firebase
        if (!message.equals("")) { //sends only if message isn't empty
            ChatMessage sendingObj = findElements(message);
            firebaseRef.push().setValue(sendingObj);
            scanResult = "";
        }
    }

    //extracts data from QR string and returns it in a single element
    public ChatMessage findElements(String tempScanResult) {
        Scanner scan = new Scanner(tempScanResult); //makes scanner out of string for ease of extraction
        String tempLine;
        int indexEl;
        int elements[] = new int[NUM_ELEMENTS_SENDING];

        //for each element, stores in object
        for (int j = 0; j < NUM_ELEMENTS_SENDING; j++) {
            tempLine = scan.nextLine(); //gets line with element
            indexEl = tempLine.indexOf(':'); //extracts number after colon and stores in array
            elements[j] = Integer.parseInt(tempLine.substring(indexEl + 2));

            if (j == 0) //sets check button on/off of which scouter it received from
            {
                checkboxes[elements[0] - 1].setChecked(true);
            }
        }
        ChatMessage sendingChat = new ChatMessage(elements);
        return sendingChat;
    }

    //clears the current match stores and resets the checkboxes
    public void resetMatch()
    {
        for(int j=0; j<checkboxes.length; j++)
        {
            checkboxes[j].setChecked(false);
        }
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
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
