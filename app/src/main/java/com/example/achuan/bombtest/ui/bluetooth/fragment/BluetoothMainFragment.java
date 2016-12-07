package com.example.achuan.bombtest.ui.bluetooth.fragment;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.achuan.bombtest.R;
import com.example.achuan.bombtest.base.SimpleFragment;
import com.example.achuan.bombtest.ui.bluetooth.BluetoothChatService;
import com.example.achuan.bombtest.ui.bluetooth.activity.DeviceListActivity;
import com.example.achuan.bombtest.util.LogUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static cn.bmob.v3.Bmob.getApplicationContext;

/**
 * Created by achuan on 16-12-5.
 */

public class BluetoothMainFragment extends SimpleFragment {


    @BindView(R.id.bt_connect)
    Button mBtConnect;
    @BindView(R.id.tv_Timing)
    TextView mTvTiming;
    @BindView(R.id.tv_Delay)
    TextView mTvDelay;
    @BindView(R.id.et_inputMessage)
    EditText mEtInputMessage;
    @BindView(R.id.bt_sendMessage)
    Button mBtSendMessage;
    @BindView(R.id.tv_connectStatus)
    TextView mTvConnectStatus;//蓝牙连接进度文本显示框

    private Context mContext;
    // 调试标志位
    private static final String TAG = "BluetoothChat";
    private static final boolean D = true;
    // Intent request codes
    private static final int REQUEST_ENABLE_BT = 1;
    private static final int REQUEST_CONNECT_DEVICE = 2;

    private String mConnectedDeviceName = null;
    private BluetoothAdapter mBluetoothAdapter = null;
    private BluetoothChatService mChatService = null;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_bluetooth_main;
    }

    @Override
    protected void initEventAndData() {
        //获取一个上下文对象
        mContext = getContext();
        //获得一个本地的蓝牙适配器
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        //如果蓝牙适配器为空，则表示手机硬件不支持蓝牙功能
        if (mBluetoothAdapter == null) {
            Toast.makeText(getContext(), "Bluetooth is not available", Toast.LENGTH_LONG).show();
            getActivity().finish();//则退出APP
            return;
        }

        // 如果蓝牙未开启，则弹出开启蓝牙的对话框
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        } else {// 否则，开启蓝牙会话服务
            if (mChatService == null) {
                //初始化蓝牙会话服务，执行蓝牙连接
                mChatService = new BluetoothChatService(mContext, mHandler);
            }
        }

        mTvTiming.setText("00"+"℃");
        mTvDelay.setText("00"+"%");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mChatService != null) {
            mChatService.stop();//重置蓝牙服务,停止一切线程
        }
    }

    //子活动结束后的回调处理方法
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_ENABLE_BT:
                // 如果用户打开蓝牙
                if (resultCode == Activity.RESULT_OK) {
                    // 则初始化蓝牙会话服务，执行蓝牙连接
                    mChatService = new BluetoothChatService(mContext, mHandler);
                } else {
                    //用户未打开蓝牙，或者蓝牙打开发生错误
                    Log.d(TAG, "BT not enabled");
                    Toast.makeText(mContext, R.string.bt_not_enabled_leaving,
                            Toast.LENGTH_SHORT).show();
                    getActivity().finish(); //退出APP
                }
                break;
            case REQUEST_CONNECT_DEVICE:
                // 当用户点击了一个设备去连接，则表示RESULT_OK，否则表示RESULT_CANCELED
                if (resultCode == Activity.RESULT_OK) {
                    //获得设备的MAC地址
                    String address = data.getExtras()
                            .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                    //得到蓝牙设备的对象
                    BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
                    //尝试去连接蓝牙设备
                    mChatService.connect(device);
                }
                break;
            default:
                break;
        }
    }

    // The Handler that gets information back from the BluetoothChatService
    public final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BluetoothChatService.MESSAGE_STATE_CHANGE: //蓝牙状态发生改变，则更新状态显示
                    if (D) Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                    switch (msg.arg1) {
                        case BluetoothChatService.STATE_CONNECTED:
                            mTvConnectStatus.setText(R.string.title_connected_to);
                            mTvConnectStatus.append(mConnectedDeviceName);
                            mBtConnect.setText("断开连接");
                            break;
                        case BluetoothChatService.STATE_CONNECTING:
                            mTvConnectStatus.setText(R.string.title_connecting);
                            mBtConnect.setText("正在连接");
                            break;
                        case BluetoothChatService.STATE_LISTEN:
                        case BluetoothChatService.STATE_NONE:
                            mTvConnectStatus.setText(R.string.title_not_connected);
                            mBtConnect.setText("连接设备");
                            break;
                    }
                    break;
                case BluetoothChatService.MESSAGE_WRITE: //蓝牙正在发送数据，更新显示到UI中
                    byte[] writeBuf = (byte[]) msg.obj;
                    String writeMessage = new String(writeBuf);
                    dealSendData(writeMessage);
                    break;
                case BluetoothChatService.MESSAGE_READ: //蓝牙正在接收数据，更新显示到UI中
                    /*byte[] readBuf = (byte[]) msg.obj;
                    String readMessage = new String(readBuf, 0, msg.arg1);*/
                    Bundle data = msg.getData();
                    String readMessage = data.getString("BTdata");
                    LogUtil.d("lyc-changeworld",readMessage);
                    dealReceiveData(readMessage,msg.arg1);//msg.arg1=0
                    break;
                case BluetoothChatService.MESSAGE_DEVICE_NAME: //蓝牙连接成功，弹出连接到相应设备成功的Toast
                    mConnectedDeviceName = msg.getData().getString(BluetoothChatService.DEVICE_NAME);
                    Toast.makeText(getApplicationContext(), "Connected to "
                            + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    break;
                case BluetoothChatService.MESSAGE_TOAST: //蓝牙连接错误的Toast
                    Toast.makeText(getApplicationContext(), msg.getData().getString(BluetoothChatService.TOAST),
                            Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @OnClick({R.id.bt_connect, R.id.bt_sendMessage})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_connect:
                if(mChatService != null&&mChatService.getState()==BluetoothChatService.STATE_CONNECTED)
                {
                    mChatService.stop();//重置蓝牙服务,停止一切线程
                    mBtConnect.setText("连接设备");
                }else {
                    // Launch the DeviceListActivity to see devices and do scan
                    Intent serverIntent = new Intent(getActivity(), DeviceListActivity.class);
                    startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
                }
                break;
            case R.id.bt_sendMessage:
                String message=mEtInputMessage.getText().toString();
                if(!message.equals("")){
                    sendMessage(message+"F");
                    mEtInputMessage.setText("");
                }else {
                    Toast.makeText(mContext, "输入不能为空...", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    /**
     * 发送一个数据
     * @param message  一串待发送的字符串
     */
    private void sendMessage(String message) {
        // 在发送数据之前，确保蓝牙会话服务是连接状态，否则弹出Toast
        if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
            Toast.makeText(mContext, R.string.not_connected, Toast.LENGTH_SHORT).show();
            return;
        }
        // 确保该字符串非空，有数据的数据要发送
        if (message.length() > 0) {
            // 得到字符串的实际字节，单字节顺序发送
            byte[] send = message.getBytes();
            mChatService.write(send);
        }
    }
    /**
     * 处理蓝牙发送出去的数据
     * @param str  手机发送成功的字符串
     */
    private void dealSendData(String str){
        Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
    }
    /**
     * 处理蓝牙接收的数据
     * @param str  手机接收到的字符串
     */
    private void dealReceiveData(String str,int isFrame){
        if(isFrame == 0) {
            int i;
            if(str.charAt(0) == '1'){
                for(i=1;str.charAt(i)==' ';i++);
                mTvTiming.setText(str.substring(i)+"℃");
            }
            else if(str.charAt(0) == '2'){
                for(i=1;str.charAt(i)==' ';i++);
                mTvDelay.setText(str.substring(i)+"%");
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }
}
