package com.example.dunno;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import io.paperdb.Paper;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dunno.Prevelent.Prevelent;
import com.example.dunno.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import static android.content.ContentValues.TAG;

public class health_monitoring extends AppCompatActivity implements Dialogue.ExampleDialogListener {

    Button heartbeat , temp , bloodpressure, glucose;

    Animation scaleUp , scaleDown;
    String patientEmail;
    public String arduinoMsg;
    public String deviceName = null;
    public String deviceAddress;
    public static Handler handler;
    public static BluetoothSocket mmSocket;
    public static ConnectedThread connectedThread;
    public static CreateConnectThread createConnectThread;

    private final static int CONNECTING_STATUS = 1; // used in bluetooth handler to identify message status
    private final static int MESSAGE_READ = 2; // used in bluetooth handler to identify message update

    String tempurature ="tm";
    String heartBeat ="hb";
    String glucosee ="gl";
    String bloodPressure ="bp";

    ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_health_monitoring);
        id();
        //btnOntouch ();

        // If a bluetooth device has been selected from SelectDeviceActivity
        deviceName = getIntent().getStringExtra("deviceName");
        deviceAddress = getIntent().getStringExtra("device");
        patientEmail = getIntent().getStringExtra("patient_Email");

        if (deviceName != null){
            deviceAddress = getIntent().getStringExtra("device");
            Toast.makeText(health_monitoring.this,"Connecting to " + deviceName + "...",Toast.LENGTH_SHORT).show();

            /**call a new thread to create a bluetooth connection to the selected device */
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            createConnectThread = new CreateConnectThread(bluetoothAdapter,deviceAddress);
            createConnectThread.start();
        }

        /**Second most important piece of Code. GUI Handler*/
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg){
                switch (msg.what){
                    case CONNECTING_STATUS:
                        switch(msg.arg1){
                            case 1:
                                Toast.makeText(health_monitoring.this,"Connected to " + deviceName,Toast.LENGTH_SHORT).show();
                                break;
                            case -1:
                                Toast.makeText(health_monitoring.this,"Device fails to connect" + deviceName,Toast.LENGTH_SHORT).show();
                                break;
                        }
                        break;
                    case MESSAGE_READ:
                        arduinoMsg = msg.obj.toString(); // Read message from Arduino

                        String textt = arduinoMsg.substring(0, 2);
                        String value = arduinoMsg.substring(3);

                        String email_pat = Paper.book().read(Prevelent.userEmailKey);
                        Log.e("textt" , textt);
                        Log.e("value" , value);
                        Log.e("patient email" , email_pat);

                        value = value.replaceAll("(\\r|\\n)", "");

                        String date = java.text.DateFormat.getDateTimeInstance().format(new Date());
                        //textView.setText(currentDateTimeString);


                        if (textt.equals(tempurature)){
                            sendMeasurementToDataBase(email_pat,value+" °C","Temperature" , date);
                        }else if (textt.equals(heartBeat)){
                            sendMeasurementToDataBase(email_pat,value + " Bp","Heart_Beat" , date);
                        }else if (textt.equals(glucosee)){
                            sendMeasurementToDataBase(email_pat,value+ " mg/l","Glucose" , date);
                        }else if (textt.equals(bloodPressure)){
                            sendMeasurementToDataBase(email_pat,value+ " pr","Pressure" , date);
                        }

                        Intent i = new Intent(health_monitoring.this , Popup.class);
                        i.putExtra("arduinoText", textt);
                        i.putExtra("arduinoValue", value);
                        i.putExtra("arduinoMsg", arduinoMsg);
                        i.putExtra("patient_Email", patientEmail);
                        loadingBar.dismiss();
                        startActivity(i);
                }
            }
        };

        heartbeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                heartbeat.startAnimation(scaleUp);
                heartbeat.startAnimation(scaleDown);
                String cmdText = "1";
                // Send command to Arduino board
                connectedThread.write(cmdText);
                loadingBar.setTitle("measuring");
                loadingBar.setMessage("please wait!");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();
            }
        });
        temp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                heartbeat.startAnimation(scaleUp);
                heartbeat.startAnimation(scaleDown);
                String cmdText = "2";
                // Send command to Arduino board
                connectedThread.write(cmdText);
                loadingBar.setTitle("measuring");
                loadingBar.setMessage("please wait!");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();
            }
        });
        bloodpressure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                heartbeat.startAnimation(scaleUp);
                heartbeat.startAnimation(scaleDown);
                String cmdText = "3";
                // Send command to Arduino board
                connectedThread.write(cmdText);
                loadingBar.setTitle("measuring");
                loadingBar.setMessage("please wait!");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();
            }
        });
        glucose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                heartbeat.startAnimation(scaleUp);
                heartbeat.startAnimation(scaleDown);
                String cmdText = "4";
                // Send command to Arduino board
                connectedThread.write(cmdText);
                loadingBar.setTitle("measuring");
                loadingBar.setMessage("please wait!");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();
            }
        });
    }

    public void id(){
        heartbeat = findViewById(R.id.heartbeat);
        temp = findViewById(R.id.temp);
        bloodpressure = findViewById(R.id.bloodpressure);
        glucose = findViewById(R.id.glucose);
        scaleUp = AnimationUtils.loadAnimation(this ,R.anim.scale_up);
        scaleDown = AnimationUtils.loadAnimation(this ,R.anim.scale_down);
        Paper.init(this);
        loadingBar = new ProgressDialog(this);
    }

    @Override
    public void applyTexts(String username, String password) {

    }

    /* ============================ Thread to Create Bluetooth Connection =================================== */
    public static class CreateConnectThread extends Thread {

        public CreateConnectThread(BluetoothAdapter bluetoothAdapter, String address) {
            /*
            Use a temporary object that is later assigned to mmSocket
            because mmSocket is final.
             */
            BluetoothDevice bluetoothDevice = bluetoothAdapter.getRemoteDevice(address);
            BluetoothSocket tmp = null;
            UUID uuid = bluetoothDevice.getUuids()[0].getUuid();

            try {
                /*
                Get a BluetoothSocket to connect with the given BluetoothDevice.
                Due to Android device varieties,the method below may not work fo different devices.
                You should try using other methods i.e. :
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
                 */
                tmp = bluetoothDevice.createInsecureRfcommSocketToServiceRecord(uuid);

            } catch (IOException e) {
                Log.e(TAG, "Socket's create() method failed", e);
            }
            mmSocket = tmp;
        }

        public void run() {
            // Cancel discovery because it otherwise slows down the connection.
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            bluetoothAdapter.cancelDiscovery();
            try {
                // Connect to the remote device through the socket. This call blocks
                // until it succeeds or throws an exception.
                mmSocket.connect();
                Log.e("Status", "Device connected");
                handler.obtainMessage(CONNECTING_STATUS, 1, -1).sendToTarget();
            } catch (IOException connectException) {
                // Unable to connect; close the socket and return.
                try {
                    mmSocket.close();
                    Log.e("Status", "Cannot connect to device");
                    handler.obtainMessage(CONNECTING_STATUS, -1, -1).sendToTarget();
                } catch (IOException closeException) {
                    Log.e(TAG, "Could not close the client socket", closeException);
                }
                return;
            }

            // The connection attempt succeeded. Perform work associated with
            // the connection in a separate thread.
            connectedThread = new ConnectedThread(mmSocket);
            connectedThread.run();
        }

        // Closes the client socket and causes the thread to finish.
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Could not close the client socket", e);
            }
        }
    }

    /* =============================== Thread for Data Transfer =========================================== */
    public static class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[1024];  // buffer store for the stream
            int bytes = 0; // bytes returned from read()
            // Keep listening to the InputStream until an exception occurs
            while (true) {
                try {
                    /*
                    Read from the InputStream from Arduino until termination character is reached.
                    Then send the whole String message to GUI Handler.
                     */
                    buffer[bytes] = (byte) mmInStream.read();
                    String readMessage;
                    if (buffer[bytes] == '\n'){
                        readMessage = new String(buffer,0,bytes);
                        Log.e("Arduino Message",readMessage);
                        handler.obtainMessage(MESSAGE_READ,readMessage).sendToTarget();
                        bytes = 0;
                    } else {
                        bytes++;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            }
        }
        /* Call this from the main activity to send data to the remote device */
        public void write(String input) {
            byte[] bytes = input.getBytes(); //converts entered String into bytes
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) {
                Log.e("Send Error","Unable to send message",e);
            }
        }

        /* Call this from the main activity to shutdown the connection */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }

    /* ============================ Terminate Connection at BackPress ====================== */
    @Override
    public void onBackPressed() {
        /// Terminate Bluetooth Connection and close app
        /*if (createConnectThread != null){
            createConnectThread.cancel();
        }*/

        Intent p = new Intent(health_monitoring.this , Activity1.class);
        p.putExtra("device" ,deviceAddress );
        startActivity(p);
        /*
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);*/
    }

    public void sendMeasurementToDataBase(String patientEmail_ , String measurementValue , String measurementText , String date_){
        final DatabaseReference RootRef ;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if ((snapshot.child("Patient").child(patientEmail_).child("Measurement").child(measurementText).exists())){
                    HashMap<String,Object> userdataMap = new HashMap<>();
                    userdataMap.put(date_ ,measurementValue);

                    RootRef.child("Patient").child(patientEmail_).child("Measurement").child(measurementText).updateChildren(userdataMap).addOnCompleteListener(task -> {
                        if (task.isSuccessful()){
                            Toast.makeText(health_monitoring.this , "Data added" , Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(health_monitoring.this , "network error! try again" , Toast.LENGTH_SHORT).show();
                        }
                    });
                }else {
                    Toast.makeText(health_monitoring.this , "this already exist" , Toast.LENGTH_SHORT).show();

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
}