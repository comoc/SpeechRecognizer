/*******************************************************************************
 * Copyright 2013 Akihiro Komori
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.blogspot.comolog;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;

//@see <a href="http://stackoverflow.com/questions/4975443/is-there-a-way-to-use-the-speechrecognizer-api-directly-for-speech-input">Is there a way to use the SpeechRecognizer API directly for speech input?</html>
//@see <a href="http://techbooster.org/android/application/14927/">SpeechRecognizerを使った音声認識を行う</a>
//@see <a href="http://blog.livedoor.jp/awamaster/archives/5853980.html">音声認識を使ってみよう！</a>
public class SpeechRecognition implements RecognitionListener {
	@SuppressWarnings("unused")
	private static final String TAG = SpeechRecognition.class.getSimpleName();

	private SpeechRecognizer mSpeechRecognizer;

	public interface ResultCallback {
		void onResults(List<String> results);

		void onError(String reason);
	}

	private ResultCallback mResultCallback;

	public SpeechRecognition(Context context) {

		mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(context);
		mSpeechRecognizer.setRecognitionListener(this);
	}

	public void start(ResultCallback callback) {
		mResultCallback = callback;

		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
				RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
				SpeechRecognition.class.getPackage().getName());
		mSpeechRecognizer.startListening(intent);
	}

	@Override
	public void onBeginningOfSpeech() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onBufferReceived(byte[] arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onEndOfSpeech() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onError(int error) {

		String reason = "";
		if (mResultCallback != null) {
			switch (error) {
			case SpeechRecognizer.ERROR_AUDIO:
				reason = "SpeechRecognizer.ERROR_AUDIO";
				break;
			case SpeechRecognizer.ERROR_CLIENT:
				reason = "SpeechRecognizer.ERROR_CLIENT";
				break;
			case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
				reason = "SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS";
				break;
			case SpeechRecognizer.ERROR_NETWORK:
				reason = "SpeechRecognizer.ERROR_NETWORK";
				break;
			case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
				reason = "SpeechRecognizer.ERROR_NETWORK_TIMEOUT";
				break;
			case SpeechRecognizer.ERROR_NO_MATCH:
				reason = "SpeechRecognizer.ERROR_NO_MATCH";
				break;
			case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
				reason = "SpeechRecognizer.ERROR_RECOGNIZER_BUSY";
				break;
			case SpeechRecognizer.ERROR_SERVER:
				reason = "SpeechRecognizer.ERROR_SERVER";
				break;
			case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
				reason = "SpeechRecognizer.ERROR_SPEECH_TIMEOUT";
				break;
			}

			mResultCallback.onError(reason);
		}

	}

	@Override
	public void onEvent(int eventType, Bundle params) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPartialResults(Bundle partialResults) {
		// If you feel like it, you can use the partial result.
		// receiveResults(partialResults);
	}

	@Override
	public void onReadyForSpeech(Bundle params) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onResults(Bundle results) {
		receiveResults(results);
	}

	@Override
	public void onRmsChanged(float rmsdB) {
		// TODO Auto-generated method stub

	}

	private void receiveResults(Bundle results) {
		if ((results != null)
				&& results.containsKey(SpeechRecognizer.RESULTS_RECOGNITION)) {
			List<String> res = results
					.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
			if (mResultCallback != null)
				mResultCallback.onResults(res);
		}
	}
}
