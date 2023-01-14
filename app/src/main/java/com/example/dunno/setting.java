package com.example.dunno;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.dunno.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class setting extends AppCompatActivity{
    BluetoothAdapter BTAdapter;
    Button bt , scan;
    TextView textView;
    public ArrayList<BluetoothDevice> mBTDevices = new ArrayList<>();
    public DeviceListAdapter mDeviceListAdapter;
    RecyclerView lvNewDevices;

    Animation scaleUp , scaleDown;

    void getId(){
        bt = findViewById(R.id.bton);
        scan = findViewById(R.id.scanbtn);
        lvNewDevices =  findViewById(R.id.devicelist);
        textView = findViewById(R.id.textView);
        scaleUp = AnimationUtils.loadAnimation(this ,R.anim.scale_up);
        scaleDown = AnimationUtils.loadAnimation(this ,R.anim.scale_down);
    }


    //@SuppressLint({"SetTextI18n", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getId();
        BTAdapter = BluetoothAdapter.getDefaultAdapter();

        //mBTDevices = new ArrayList<>();
        if (!BTAdapter.isEnabled()) {
            Intent enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBT);

            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(mBroadcastReceiver1, BTIntent);

        }


        textView.setAlpha(0.00f);
        if (BTAdapter.isEnabled()) {
            bt.setText("TURN OFF BLUETOOTH");
            textView.setAlpha(1.00f);
            textView.setText("Bluetooth ON");
        }else{
            bt.setText("TURN ON BLUETOOTH");
            textView.setAlpha(1.00f);
            textView.setText("Bluetooth OFF");
        }

        bt.setOnClickListener(v -> onOffBt());

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                discover();
            }
        });

    }


    public void onOffBt() {
        assert BTAdapter != null;
        if (!BTAdapter.isEnabled()) {
            Intent enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBT);
            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(mBroadcastReceiver1, BTIntent);

        }
        if (BTAdapter.isEnabled()) {
            bt.setText("TURN On BLUETOOTH");
            textView.setText("Bluetooth Off");
            BTAdapter.disable();
        }
    }

    @SuppressLint("SetTextI18n")
    public void discover(){
        // Get List of Paired Bluetooth Device
        Set<BluetoothDevice> pairedDevices = BTAdapter.getBondedDevices();
        List<Object> deviceList = new ArrayList<>();
        if (pairedDevices.size() > 0) {
            // There are paired devices. Get the name and address of each paired device.
            for (BluetoothDevice device : pairedDevices) {
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address
                DeviceInfoModel deviceInfoModel = new DeviceInfoModel(deviceName,deviceHardwareAddress);
                deviceList.add(deviceInfoModel);
            }
            // Display paired device using recyclerView
            RecyclerView recyclerView = findViewById(R.id.devicelist);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            DeviceListAdapter deviceListAdapter = new DeviceListAdapter(this,deviceList);
            recyclerView.setAdapter(deviceListAdapter);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
        }

        /*
        if(BTAdapter.isDiscovering()){
            BTAdapter.cancelDiscovery();
            checkBTPermissions();
            BTAdapter.startDiscovery();
            textView.setAlpha(1.00f);
            textView.setText("SCANNING");
        }
        if (!BTAdapter.isDiscovering()){
            checkBTPermissions();

            BTAdapter.startDiscovery();
            textView.setAlpha(1.00f);
            textView.setText("SCANNING");
        }*/
    }

    private void checkBTPermissions() {
        int permissionCheck = this.checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION");
        permissionCheck += this.checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");
        if (permissionCheck != 0) {

            this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001); //Any number
        }
    }

    /** //////////////////////////////////////////////////////
                ON/OFF Broadcast    */
    private final BroadcastReceiver mBroadcastReceiver1 = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (action.equals(BTAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BTAdapter.ERROR);

                switch(state){
                    case BluetoothAdapter.STATE_OFF:
                        textView.setAlpha(1.00f);
                        textView.setText("bluetooth off");
                        bt.setAlpha(1.00f);
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        textView.setAlpha(1.00f);
                        textView.setText("bluetooth turning off");
                        bt.setText("Turn on bluetooth");
                        break;
                    case BluetoothAdapter.STATE_ON:
                        textView.setAlpha(1.00f);
                        textView.setText("bluetooth on");
                        bt.setText("TURN Off BLUETOOTH");
                        //textView.setText("Bluetooth On");
                        //bt.setAlpha(0.01f);
                        bt.setText("Turn off bluetooth");
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        textView.setAlpha(1.00f);
                        textView.setText("bluetooth turning on");
                        break;
                }
            }
        }
    };


    /* //////////////////////////////////////////////////////
                discovering Broadcast/
    private final BroadcastReceiver mBroadcastReceiver2 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (action.equals(BluetoothDevice.ACTION_FOUND)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                mBTDevices.add(device);
                mDeviceListAdapter = new DeviceListAdapter(context,R.layout.device_adapter_view,mBTDevices);
                lvNewDevices.setAdapter((ListAdapter) mDeviceListAdapter);
            }
        }
    };
    /* //////////////////////////////////////////////////////
                    Pairing broadcast/
    private final BroadcastReceiver mBroadcastReceiver3 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if(action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)){
                BluetoothDevice mDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                if (mDevice.getBondState() == BluetoothDevice.BOND_BONDED){
                    textView.setAlpha(1.00f);
                    textView.setText("bonded");
                }
                if (mDevice.getBondState() == BluetoothDevice.BOND_BONDING) {
                    textView.setAlpha(1.00f);
                    textView.setText("bonding");
                }
                if (mDevice.getBondState() == BluetoothDevice.BOND_NONE) {
                    textView.setAlpha(1.00f);
                    textView.setText("no bond");
                }
            }
        }
    };
    /**  ////////////////////////////////////////////////////// */

}