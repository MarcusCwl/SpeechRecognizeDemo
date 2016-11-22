package com.baobomb.speechrecognizedemo;

import android.app.Application;
import android.os.Handler;

import edu.cmu.pocketsphinx.SpeechRecognizer;

/**
 * Created by LEAPSY on 2016/11/22.
 */

public class SpeechRecognizerApplication extends Application {
    public static SpeechRecognizer speechRecognizer;
    public static SpeechService speechService;
    public static Handler speechHandler;
    public static SpeechRecognizerAsync speechRecognizerAsync;

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
