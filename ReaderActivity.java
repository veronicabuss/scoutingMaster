package org.strykeforce.qrscannergenerator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.Scanner;

public class ReaderActivity extends AppCompatActivity {
    private Button scan_btn;
    private ImageButton[] offbuttons = new ImageButton[3]; //refers to off/on checkboxes images
    private ImageButton[] onbuttons = new ImageButton[3];
    private String scanResult;
    private static final String FIREBASE_URL = "https://testproj1-dc6de.firebaseio.com/"; //set to URL of firebase to send to
    private Firebase firebaseRef;
    private static final int NUM_ELEMENTS_SENDING = 3; //adjust to how much data will be sent in QR code

    @Override
    protected void onCreate(Bundle savedInstanceState) { //method that creates everything when app is opened
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader); //sets to layout of app

        //initializes firebase to be able to send data to that URL
        Firebase.setAndroidContext(this);
        firebaseRef = new Firebase(FIREBASE_URL);

        //initializes all off/on check boxes
        offbuttons[0] = (ImageButton)findViewById(R.id.red1Off);
        offbuttons[1] = (ImageButton)findViewById(R.id.red2Off);
        offbuttons[2] = (ImageButton)findViewById(R.id.red3Off);
        onbuttons[0] = (ImageButton)findViewById(R.id.red1On);
        onbuttons[1] = (ImageButton)findViewById(R.id.red2On);
        onbuttons[2] = (ImageButton)findViewById(R.id.red3On);
        //sets visibility of check boxes
        for(int j=0; j<offbuttons.length; j++)
        {
            offbuttons[j].setVisibility(View.VISIBLE);
            onbuttons[j].setVisibility(View.INVISIBLE);
        }

        //sends message when send button clicked
        findViewById(R.id.sendButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
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
    }

    //method that scans QR code from camera and stores in a string
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null){
            if(result.getContents()==null){
                Toast.makeText(this, "You cancelled the scanning", Toast.LENGTH_LONG).show();
            }
            else {
                scanResult = result.getContents(); //gets data from QR code and stores in private string
                Toast.makeText(this, scanResult,Toast.LENGTH_LONG).show(); //displays data from QR code on screen
            }
        }
        else
        {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    //sends data as a single object to firebase
    public void sendMessage(){
        //gets QR results from private string
        String message = scanResult;

        //makes new object and calls findElements method as to push a single chatElement object to firebase
        if(!message.equals("")){ //sends only if message isn't empty
            ChatMessage sendingObj = findElements(message);
            firebaseRef.push().setValue(sendingObj);
            scanResult = "";
        }
    }

    //extracts data from QR string and returns it in a single element
    public ChatMessage findElements(String tempScanResult)
    {
        Scanner scan = new Scanner(tempScanResult); //makes scanner out of string for ease of extraction
        String tempLine;
        int indexEl;
        int elements[] = new int[NUM_ELEMENTS_SENDING];

        //for each element, stores in object
        for(int j=0; j<NUM_ELEMENTS_SENDING; j++)
        {
            tempLine = scan.nextLine(); //gets line with element
            indexEl = tempLine.indexOf(':'); //extracts number after colon and stores in array
            elements[j] = Integer.parseInt(tempLine.substring(indexEl+2));

            if(j==0) //sets check button on/off of which scouter it received from
            {
                offbuttons[elements[0]-1].setVisibility(View.INVISIBLE);
                onbuttons[elements[0]-1].setVisibility(View.VISIBLE);
            }
        }
        ChatMessage sendingChat = new ChatMessage(elements);
        return sendingChat;
    }
}
