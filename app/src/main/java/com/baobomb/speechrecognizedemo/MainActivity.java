package com.baobomb.speechrecognizedemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity {

    Handler speechHandler;
    TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    public void init() {
        result = (TextView) findViewById(R.id.result);
        speechHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                String message = (String)msg.obj;
                switch (message) {
                    case SpeechKeys.SETTING:
                        result.setText(message);
                        break;
                    case SpeechKeys.MENU:
                        result.setText(message);
                        break;
                    case SpeechKeys.CAMERA:
                        result.setText(message);
                        break;
                    case SpeechKeys.BLUETOOTH:
                        result.setText(message);
                        break;
                    case SpeechKeys.RETURN:
                        result.setText(message);
                        break;
                    case SpeechKeys.WAKEUP:
                        result.setText(message);
                        break;
                    case SpeechKeys.COMMANDER:
                        result.setText(message);
                        break;
                    case SpeechKeys.ERROR:
                        result.setText(message);
                        break;
                }

            }
        };
        SpeechRecognizerApplication.speechHandler = speechHandler;
        startService();
    }

    public void startService() {
        Intent intent = new Intent(this,SpeechService.class);
        startService(intent);
    }

}
