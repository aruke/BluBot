package com.myapp.blubot;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.io.IOException;
import java.io.OutputStream;

import at.markushi.ui.CircleButton;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ControlActivity extends AppCompatActivity {

    OutputStream outputStream;
    @Bind(R.id.control_board_button_a)
    CircleButton controlBoardButtonA;
    @Bind(R.id.control_board_button_up)
    CircleButton controlBoardButtonUp;
    @Bind(R.id.control_board_button_b)
    CircleButton controlBoardButtonB;
    @Bind(R.id.control_board_button_left)
    CircleButton controlBoardButtonLeft;
    @Bind(R.id.control_board_button_close)
    CircleButton controlBoardButtonClose;
    @Bind(R.id.control_board_button_right)
    CircleButton controlBoardButtonRight;
    @Bind(R.id.control_board_button_c)
    CircleButton controlBoardButtonC;
    @Bind(R.id.control_board_button_down)
    CircleButton controlBoardButtonDown;
    @Bind(R.id.control_board_button_d)
    CircleButton controlBoardButtonD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);
        ButterKnife.bind(this);

        outputStream = BluBot.getOutputStream();

        if (outputStream == null) {
            // TODO Handle exception here

        }
    }

    @OnClick(R.id.fragment_bt_unavailable_github_link)
    public void onControlButtonClick(View view)
    {
        int viewId = view.getId();
        if(outputStream==null)
            return;
        try {
            switch (viewId) {
                case R.id.control_board_button_a:
                    outputStream.write(0x0a);
                    break;
                case R.id.control_board_button_b:
                    outputStream.write(0x0b);
                    break;
                case R.id.control_board_button_c:
                    outputStream.write(0x0c);
                    break;
                case R.id.control_board_button_d:
                    outputStream.write(0x0d);
                    break;
                case R.id.control_board_button_up:
                    outputStream.write(0x01);
                    break;
                case R.id.control_board_button_down:
                    outputStream.write(0x02);
                    break;
                case R.id.control_board_button_left:
                    outputStream.write(0x03);
                    break;
                case R.id.control_board_button_right:
                    outputStream.write(0x04);
                    break;
                case R.id.control_board_button_close:
                    outputStream.write(0x05);
                    break;
                default:
            }
        }catch (IOException e)
        {

        }
    }


    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_control, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/
}
