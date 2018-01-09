package me.rajanikant.blubot.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import me.rajanikant.blubot.BluBot;
import me.rajanikant.blubot.R;

import java.io.IOException;
import java.io.OutputStream;

import at.markushi.ui.CircleButton;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ControlActivity extends AppCompatActivity {

    OutputStream outputStream;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);
        ButterKnife.bind(this);

        outputStream = BluBot.getOutputStream();

        if (outputStream == null) {
            Toast.makeText(this, "Device not Paired properly : OutputStream null", Toast.LENGTH_LONG).show();
        }
    }

    @OnClick({R.id.control_board_button_a, R.id.control_board_button_b, R.id.control_board_button_c,
            R.id.control_board_button_d, R.id.control_board_button_up, R.id.control_board_button_down,
            R.id.control_board_button_left, R.id.control_board_button_right, R.id.control_board_button_close})
    public void onControlButtonClick(View view) {
        int viewId = view.getId();
        if (outputStream == null)
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
        } catch (IOException e) {
            Toast.makeText(this, "Could not send signal", Toast.LENGTH_SHORT).show();
        }
    }
}
