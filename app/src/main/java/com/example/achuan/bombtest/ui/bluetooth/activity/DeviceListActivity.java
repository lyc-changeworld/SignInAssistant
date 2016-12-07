package com.example.achuan.bombtest.ui.bluetooth.activity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.achuan.bombtest.R;
import com.example.achuan.bombtest.base.SimpleActivity;

import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by achuan on 16-12-5.
 */

public class DeviceListActivity extends SimpleActivity {

    @BindView(R.id.bt_scan)
    Button mBtScan;
    @BindView(R.id.lv_paired_devices)
    ListView mLvPairedDevices;
    @BindView(R.id.lv_new_devices)
    ListView mLvNewDevices;
    @BindView(R.id.tv_paired_devices)
    TextView mTvPairedDevices;
    @BindView(R.id.tv_new_devices)
    TextView mTvNewDevices;

    // Member fields
    private BluetoothAdapter mBtAdapter;
    private ArrayAdapter<String> mPairedDevicesArrayAdapter;
    private ArrayAdapter<String> mNewDevicesArrayAdapter;
    // Return Intent extra
    public static String EXTRA_DEVICE_ADDRESS = "device_address";

    @Override
    protected int getLayout() {
        // Setup the window
        //增加一个缓存条表示一个进程正在运行
        //this.requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        return R.layout.activity_bluetooth_list;
    }

    @Override
    protected void initEventAndData() {
        // Set result CANCELED incase the user backs out
        setResult(Activity.RESULT_CANCELED);

        // Get the local Bluetooth adapter
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();

        // Initialize array adapters. One for already paired devices and
        // one for newly discovered devices
        mPairedDevicesArrayAdapter = new ArrayAdapter<String>(this,
                R.layout.item_bluetooth_device);
        mNewDevicesArrayAdapter = new ArrayAdapter<String>(this,
                R.layout.item_bluetooth_device);

        //set up the ListView for paired devices and newly discovered devices
        mLvPairedDevices.setAdapter(mPairedDevicesArrayAdapter);
        mLvNewDevices.setAdapter(mNewDevicesArrayAdapter);
        mLvPairedDevices.setOnItemClickListener(mDeviceClickListener);
        mLvNewDevices.setOnItemClickListener(mDeviceClickListener);

        // Register for broadcasts when a device is discovered
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(mReceiver, filter);
        // Register for broadcasts when discovery has finished
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(mReceiver, filter);

        // Get a set of currently paired devices
        Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();
        // If there are paired devices, add each one to the ArrayAdapter
        if (pairedDevices.size() > 0) {
            mTvPairedDevices.setVisibility(View.VISIBLE);
            for (BluetoothDevice device : pairedDevices) {
                mPairedDevicesArrayAdapter.add(device.getName() + "\n"
                        + device.getAddress());
            }
        } else {
            String noDevices = getResources().getText(R.string.none_paired)
                    .toString();
            mPairedDevicesArrayAdapter.add(noDevices);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Make sure we're not doing discovery anymore
        if (mBtAdapter != null)
        {
            mBtAdapter.cancelDiscovery();
        }
        // Unregister broadcast listeners
        this.unregisterReceiver(mReceiver);
    }

    // The on-click listener for all devices in the ListViews
    private AdapterView.OnItemClickListener mDeviceClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            // Cancel discovery because it's costly and we're about to connect
            mBtAdapter.cancelDiscovery();

            // Get the device MAC address, which is the last 17 chars in the
            // View
            String info = ((TextView) view).getText().toString();
            String address = info.substring(info.length() - 17);

            // Create the result Intent and include the MAC address
            Intent intent = new Intent();
            intent.putExtra(EXTRA_DEVICE_ADDRESS, address);

            // Set result and finish this Activity
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    };

    private void doDiscovery() {
        // Indicate scanning in the title
        //setProgressBarIndeterminateVisibility(true);
        setTitle(R.string.scanning);

        // Turn on sub-title for new devices
        mTvNewDevices.setVisibility(View.VISIBLE);

        // If we're already discovering, stop it
        if (mBtAdapter.isDiscovering()) {
            mBtAdapter.cancelDiscovery();
        }

        // Request discover from BluetoothAdapter
        mBtAdapter.startDiscovery();
    }

    @OnClick(R.id.bt_scan)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_scan:
                doDiscovery();
                //点击扫描按钮后将其隐藏
                view.setVisibility(View.GONE);
                break;
            default:break;
        }
    }

    // The BroadcastReceiver that listens for discovered devices and
    // changes the title when discovery is finished
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent
                        .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // If it's already paired, skip it, because it's been listed
                // already
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    mNewDevicesArrayAdapter.add(device.getName() + "\n"
                            + device.getAddress());
                }
                // When discovery is finished, change the Activity title
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                //setProgressBarIndeterminateVisibility(false);
                setTitle(R.string.select_device);
                if (mNewDevicesArrayAdapter.getCount() == 0) {
                    String noDevices = getResources().getText(
                            R.string.none_found).toString();
                    mNewDevicesArrayAdapter.add(noDevices);
                }
            }
        }
    };

}
