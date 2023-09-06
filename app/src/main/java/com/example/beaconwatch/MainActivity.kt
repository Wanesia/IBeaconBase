package com.example.beaconwatch

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private lateinit var bluetoothAdapter: BluetoothAdapter
    private val PERMISSION_REQUEST_CODE = 1
    private lateinit var scanButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        scanButton = findViewById(R.id.scanButton)

        //Checking whether device supports bluetooth or its on/off
        if (bluetoothAdapter.isEnabled) {
            Log.d("Bluetooth","Bluetooth is ON")
            Toast.makeText(this,"Bluetooth is ON",Toast.LENGTH_SHORT).show()
        }else  if(!bluetoothAdapter.isEnabled){
            Log.d("Bluetooth","Bluetooth is OFF")
            Toast.makeText(this,"Bluetooth is OFF",Toast.LENGTH_SHORT).show()
        }
        else{
            Log.d("Bluetooth","Device doesn't support Bluetooth")
            Toast.makeText(this,"Device doesn't support Bluetooth",Toast.LENGTH_SHORT).show()
        }


        val permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Permission has been granted, you can proceed with Bluetooth scanning
            Log.d("Permission", "Permission has been granted, you can proceed with Bluetooth scanning")
            setupBluetoothScanning()
        } else {
            // Permission has not been granted, so request it
            Log.d("Permission", "Permission has not been granted, so request it")
            ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE)
        }

    }

    private fun setupBluetoothScanning() {
        Log.d("setupBluetoothScanning", "setupBluetoothScanning method starts")

        scanButton.setOnClickListener {
            try {
                val bluetoothLeScanner = bluetoothAdapter.bluetoothLeScanner

                val scanCallback = object : ScanCallback() {
                    override fun onScanResult(callbackType: Int, result: ScanResult) {
                        val device = result.device
                        val deviceName = device.name
                        val deviceAddress = device.address
                        val rssi = result.rssi
                        if (rssi >= -40) {
                            Log.d("Tag", "AAAAAAAAAAAAAAAAAAAAAAAAA $device \n $deviceName \n $rssi")
                        }
                    }
                }

                val scanSettings = ScanSettings.Builder()
                    .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                    .build()

                bluetoothLeScanner.startScan(null, scanSettings, scanCallback)
            } catch (e: SecurityException) {
                Log.d("Error","Ups $e")
            }
        }
    }
}
