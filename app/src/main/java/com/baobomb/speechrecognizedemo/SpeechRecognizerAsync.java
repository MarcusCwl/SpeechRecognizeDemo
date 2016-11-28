package com.baobomb.speechrecognizedemo;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;

import edu.cmu.pocketsphinx.Assets;

import static edu.cmu.pocketsphinx.SpeechRecognizerSetup.defaultSetup;

/**
 * Created by LEAPSY on 2016/11/22.
 */

public class SpeechRecognizerAsync extends AsyncTask<Void, Void, Exception> {
    Context context;

    public SpeechRecognizerAsync(Context context) {
        this.context = context;
        SpeechRecognizerApplication.speechRecognizerAsync = this;
    }

    public Exception init() {
        Log.d("BAO", "speech async init");
        try {
            Assets assets = new Assets(context);
            File assetDir = assets.syncAssets();
            setupRecognizer(assetDir);
        } catch (IOException e) {
            return e;
        }
        return null;
    }

    @Override
    protected Exception doInBackground(Void... params) {
        return init();
    }

    @Override
    protected void onPostExecute(Exception result) {
        Log.d("BAO", "speech async onPostExecute");
        if (result != null) {
            Message message = Message.obtain(SpeechRecognizerApplication.speechHandler);
            message.obj = SpeechKeys.ERROR;
            message.sendToTarget();
        } else {
            SpeechRecognizerApplication.speechService.switchSearch(SpeechKeys.WAKEUP);
        }
    }


    private void setupRecognizer(File assetsDir) throws IOException {
        SpeechRecognizerApplication.speechRecognizer = defaultSetup()
                .setAcousticModel(new File(assetsDir, "en-us-ptm"))
                .setDictionary(new File(assetsDir, "cmudict-en-us.dict"))
                .setRawLogDir(assetsDir)
                .setKeywordThreshold(1e-45f)
                .setBoolean("-allphone_ci", true)
                .getRecognizer();
        SpeechRecognizerApplication.speechRecognizer.addListener(SpeechRecognizerApplication.speechService);
        SpeechRecognizerApplication.speechRecognizer.addKeyphraseSearch(SpeechKeys.WAKEUP, SpeechKeys.COMMANDER);
        File commanderGrammar = new File(assetsDir, "commander.gram");
        SpeechRecognizerApplication.speechRecognizer.addGrammarSearch(SpeechKeys.COMMAND_SEARCH, commanderGrammar);
        File digitsGrammar = new File(assetsDir, "digits.gram");
        SpeechRecognizerApplication.speechRecognizer.addGrammarSearch(SpeechKeys.DIGITS_SEARCH, digitsGrammar);
        File languageModel = new File(assetsDir, "weather.dmp");
        SpeechRecognizerApplication.speechRecognizer.addNgramSearch(SpeechKeys.FORECAST_SEARCH, languageModel);
        File phoneticModel = new File(assetsDir, "en-phone.dmp");
        SpeechRecognizerApplication.speechRecognizer.addAllphoneSearch(SpeechKeys.PHONE_SEARCH, phoneticModel);
    }



//    private void setupRecognizer(File assetsDir) throws IOException {
//        // The recognizer can be configured to perform multiple searches
//        // of different kind and switch between them
//
//        SpeechRecognizerApplication.speechRecognizer = defaultSetup()
//                .setAcousticModel(new File(assetsDir, "en-us-ptm"))
//                .setDictionary(new File(assetsDir, "cmudict-en-us.dict"))
////                .setAcousticModel(new File(assetsDir, "zh_ptm"))
////                .setDictionary(new File(assetsDir, "zh_tw.dic"))
//                // To disable logging of raw audio comment out this call (takes a lot of space on the device)
//                .setRawLogDir(assetsDir)
//
//                // Threshold to tune for keyphrase to balance between false alarms and misses
//                .setKeywordThreshold(1e-45f)
//                // Use context-independent phonetic search, context-dependent is too slow for mobile
//                .setBoolean("-allphone_ci", true)
////                .setBoolean("-remove_noise", true)
//
//                .getRecognizer();
//        SpeechRecognizerApplication.speechRecognizer.addListener(SpeechRecognizerApplication.speechService);
//
//        /** In your application you might not need to add all those searches.
//         * They are added here for demonstration. You can leave just one.
//         */
//
//        // Create keyword-activation search.
////        SpeechRecognizerApplication.speechRecognizer.addKeyphraseSearch(SpeechKeys.SETTING, SpeechKeys.MENU);
//        SpeechRecognizerApplication.speechRecognizer.addKeyphraseSearch(SpeechKeys.WAKEUP, SpeechKeys.COMMANDER);
//
////        File languageModel = new File(assetsDir, "zh_tw.lm");//这里为语音模型
////        SpeechRecognizerApplication.speechRecognizer.addNgramSearch("test", languageModel);
//
////        File menuGrammar = new File(assetsDir, "menu.gram");
////        SpeechRecognizerApplication.speechRecognizer.addGrammarSearch(SpeechKeys.MENU_SEARCH, menuGrammar);
//
//        File commanderGrammar = new File(assetsDir, "commander.gram");
//        SpeechRecognizerApplication.speechRecognizer.addGrammarSearch(SpeechKeys.COMMAND_SEARCH, commanderGrammar);
//        File digitsGrammar = new File(assetsDir, "digits.gram");
//        SpeechRecognizerApplication.speechRecognizer.addGrammarSearch(SpeechKeys.DIGITS_SEARCH, digitsGrammar);
//        File languageModel = new File(assetsDir, "weather.dmp");
//        SpeechRecognizerApplication.speechRecognizer.addNgramSearch(SpeechKeys.FORECAST_SEARCH, languageModel);
//        File phoneticModel = new File(assetsDir, "en-phone.dmp");
//        SpeechRecognizerApplication.speechRecognizer.addAllphoneSearch(SpeechKeys.PHONE_SEARCH, phoneticModel);
//    }

}
