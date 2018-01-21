package me.rajanikant.blubot.ui;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayout;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

import at.markushi.ui.CircleButton;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.rajanikant.blubot.R;

public class ControlActivity extends BaseActivity {

    private static final String TAG = "BluetoothControl";
    private static final String SERVER_UUID = "00001101-0000-1000-8000-00805F9B34FB";
    private static final long DELAY = 3000;

    private static final int VIEW_STATE_CONNECTING = 0;
    private static final int VIEW_STATE_CONNECTED = 1;

    @BindView(R.id.control_board_button_a)
    CircleButton controlBoardButtonA;
    @BindView(R.id.control_board_button_up)
    CircleButton controlBoardButtonUp;
    @BindView(R.id.control_board_button_b)
    CircleButton controlBoardButtonB;
    @BindView(R.id.control_board_button_left)
    CircleButton controlBoardButtonLeft;
    @BindView(R.id.control_board_button_close)
    CircleButton controlBoardButtonClose;
    @BindView(R.id.control_board_button_right)
    CircleButton controlBoardButtonRight;
    @BindView(R.id.control_board_button_c)
    CircleButton controlBoardButtonC;
    @BindView(R.id.control_board_button_down)
    CircleButton controlBoardButtonDown;
    @BindView(R.id.control_board_button_d)
    CircleButton controlBoardButtonD;
    @BindView(R.id.control_device_name)
    TextView textDeviceName;
    @BindView(R.id.control_progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.control_board_layout)
    GridLayout controlBoardLayout;

    private BluetoothDevice mSelectedDevice;
    private ConnectThread mConnectThread;
    private BluetoothSocket mSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);
        ButterKnife.bind(this);

        Intent sourceIntent = getIntent();
        if (sourceIntent != null) {
            mSelectedDevice = sourceIntent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
        }

        if (mSelectedDevice == null) {
            Toast.makeText(this, R.string.device_not_connected, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        textDeviceName.setText(mSelectedDevice.getName());

        setViewState(VIEW_STATE_CONNECTING);

        mConnectThread = new ConnectThread(mSelectedDevice);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Start connection to selected device
        mConnectThread.start();
    }

    @Override
    void onBluetoothTurnedOn() {
        // Do nothing here
    }

    @Override
    void onBluetoothTurnedOff() {
        // Finish this activity, and start ConnectionActivity (Already in stack)
        finish();
    }

    @OnClick({R.id.control_board_button_a, R.id.control_board_button_b, R.id.control_board_button_c,
            R.id.control_board_button_d, R.id.control_board_button_up, R.id.control_board_button_down,
            R.id.control_board_button_left, R.id.control_board_button_right, R.id.control_board_button_close})
    public void onControlButtonClick(View view) {
        int viewId = view.getId();
        String signal = "";

        switch (viewId) {
            case R.id.control_board_button_a:
                // Write signal 0x0a
                signal = "0x0A";
                break;
            case R.id.control_board_button_b:
                // Write signal 0x0b
                signal = "0x0B";
                break;
            case R.id.control_board_button_c:
                // Write signal 0x0c
                signal = "0x0C";
                break;
            case R.id.control_board_button_d:
                // Write signal 0x0d
                signal = "0x0D";
                break;
            case R.id.control_board_button_up:
                // Write signal 0x01
                signal = "0x01";
                break;
            case R.id.control_board_button_down:
                // Write signal 0x02
                signal = "0x02";
                break;
            case R.id.control_board_button_left:
                // Write signal 0x03
                signal = "0x03";
                break;
            case R.id.control_board_button_right:
                // Write signal 0x04
                signal = "0x04";
                break;
            case R.id.control_board_button_close:
                // Write signal 0x05
                signal = "0x05";
                break;
            default:
        }

        if (mSocket != null) {
            try {
                OutputStream outputStream = mSocket.getOutputStream();
                outputStream.write(signal.getBytes());
                outputStream.flush();

            } catch (IOException e) {
                Log.e(TAG, "onControlButtonClick: ", e);
                e.printStackTrace();
            }
        }

        if (viewId == R.id.control_board_button_close) {
            finish();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mConnectThread.cancel();
    }

    public void setSocket(BluetoothSocket socket) {
        mSocket = socket;
    }

    private class ConnectThread extends Thread {

        private final BluetoothSocket mmSocket;

        ConnectThread(BluetoothDevice device) {
            // Use a temporary object that is later assigned to mmSocket because mmSocket is final.
            BluetoothSocket tmp = null;

            try {
                // Get a BluetoothSocket to connect with the given BluetoothDevice.
                tmp = device.createInsecureRfcommSocketToServiceRecord(UUID.fromString(SERVER_UUID));
            } catch (IOException e) {
                Log.e(TAG, "Socket's create() method failed", e);
            }
            mmSocket = tmp;
        }

        public void run() {

            try { sleep(DELAY); } catch (InterruptedException ignored) {}

            try {
                // Connect to the remote device through the socket.
                mmSocket.connect();
                Log.d(TAG, "run: Socket connected " + (mmSocket.isConnected() ? "true" : "false"));
            } catch (IOException connectException) {

                // Unable to connect; close the socket and return.
                Log.e(TAG, "run: ConnectException ", connectException);
                try {
                    mmSocket.close();
                } catch (IOException closeException) {
                    Log.e(TAG, "Could not close the client socket", closeException);
                }
                return;
            }

            // The connection attempt succeeded. Perform work associated with
            // the connection in a separate thread.
            manageConnectedSocket(mmSocket);
        }

        private void manageConnectedSocket(final BluetoothSocket socket) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setViewState(VIEW_STATE_CONNECTED);
                    setSocket(socket);
                }
            });
        }

        // Closes the client socket and causes the thread to finish.
        void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Could not close the client socket", e);
            }
        }
    }


    private void setViewState(int viewState) {
        switch (viewState) {
            case VIEW_STATE_CONNECTING:
                progressBar.setVisibility(View.VISIBLE);
                controlBoardLayout.setAlpha(0.3f);
                break;
            case VIEW_STATE_CONNECTED:
                progressBar.setVisibility(View.GONE);
                controlBoardLayout.setAlpha(1.0f);
                break;
            default:
                throw new IllegalStateException("View State must be one of VIEW_STATE_CONNECTING or VIEW_STATE_CONNECTED");
        }

        boolean controlEnabled = viewState == VIEW_STATE_CONNECTED;
        controlBoardButtonA.setEnabled(controlEnabled);
        controlBoardButtonB.setEnabled(controlEnabled);
        controlBoardButtonC.setEnabled(controlEnabled);
        controlBoardButtonD.setEnabled(controlEnabled);
        controlBoardButtonUp.setEnabled(controlEnabled);
        controlBoardButtonDown.setEnabled(controlEnabled);
        controlBoardButtonLeft.setEnabled(controlEnabled);
        controlBoardButtonRight.setEnabled(controlEnabled);
        controlBoardButtonClose.setEnabled(controlEnabled);
    }
}
