package com.jagoar.jaguar2;

import android.media.MediaRecorder;
import android.util.Log;

import java.io.IOException;

public class Audio {
    private MediaRecorder recorder = null;
    private static String fileName = null;
    private static final String LOG_TAG = "AudioRecordTest";


    private void startRecording() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(fileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        recorder.start();
    }
    private void stopRecording() {
        recorder.stop();
        recorder.release();
        recorder = null;
    }
}
