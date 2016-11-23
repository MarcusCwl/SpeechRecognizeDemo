package com.baobomb.speechrecognizedemo;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.RecognitionListener;
import edu.cmu.pocketsphinx.SpeechRecognizer;

import static android.widget.Toast.makeText;

/**
 * Created by LEAPSY on 2016/11/22.
 */

public class SpeechService extends Service implements RecognitionListener {
    SpeechRecognizerAsync speechRecognizerAsync;

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        SpeechRecognizerApplication.speechService = this;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        speechRecognizerAsync = new SpeechRecognizerAsync(this);
        speechRecognizerAsync.execute();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("BAO", "destroy");
        if (SpeechRecognizerApplication.speechRecognizer != null) {
            SpeechRecognizerApplication.speechRecognizer.cancel();
            SpeechRecognizerApplication.speechRecognizer.shutdown();
        }
    }

    @Override
    public void onBeginningOfSpeech() {
        Log.d("BAO", "Start speech");
    }

    @Override
    public void onEndOfSpeech() {
        if (!SpeechRecognizerApplication.speechRecognizer.getSearchName().equals(SpeechKeys.WAKEUP)) {
            Log.d("BAO", "end speech");
            switchSearch(SpeechKeys.WAKEUP);
        }
    }

    @Override
    public void onResult(Hypothesis hypothesis) {
        Log.d("BAO", "onResult");
        if (hypothesis != null) {
            Log.d("BAO", "result : "+hypothesis.getHypstr());
        }
    }

    @Override
    public void onPartialResult(Hypothesis hypothesis) {
        Log.d("BAO", "onPartialResult");
        if (hypothesis == null) {
            Log.d("BAO", "no result");
            sendMessage("");
            return;
        }

        String text = hypothesis.getHypstr();
        switch (text) {
            case SpeechKeys.COMMANDER:
                sendMessage(text);
                switchSearch(SpeechKeys.COMMAND_SEARCH);
                break;
            case SpeechKeys.MENU:
                sendMessage(text);
                switchSearch(SpeechKeys.WAKEUP);
                break;
            case SpeechKeys.CAMERA:
                sendMessage(text);
                switchSearch(SpeechKeys.WAKEUP);
                break;
            case SpeechKeys.SETTING:
                sendMessage(text);
                switchSearch(SpeechKeys.WAKEUP);
                break;
            case SpeechKeys.BLUETOOTH:
                sendMessage(text);
                switchSearch(SpeechKeys.WAKEUP);
                break;
            case SpeechKeys.WAKEUP:
                sendMessage(text);
                switchSearch(SpeechKeys.WAKEUP);
                break;
            case SpeechKeys.ERROR:
                sendMessage(text);
                switchSearch(SpeechKeys.WAKEUP);
                break;
            default:
                sendMessage("");
                break;
        }
    }

    @Override
    public void onError(Exception e) {
        Log.d("BAO", "onError" + e.toString());
    }

    @Override
    public void onTimeout() {
        Log.d("BAO", "onTimeout");
        switchSearch(SpeechKeys.WAKEUP);
    }

    public void switchSearch(String searchName) {
        Log.d("BAO", "switch search");
        SpeechRecognizerApplication.speechRecognizer.stop();
        // If we are not spotting, start listening with timeout (10000 ms or 10 seconds).
        if (searchName.equals(SpeechKeys.WAKEUP)) {
            SpeechRecognizerApplication.speechRecognizer.startListening(searchName);
        } else {
            SpeechRecognizerApplication.speechRecognizer.startListening(searchName, 10000);
        }
    }

    public void sendMessage(String msg) {
        Log.d("BAO", msg);
        Message message = Message.obtain(SpeechRecognizerApplication.speechHandler);
        message.obj = msg;
        message.sendToTarget();
    }

}
