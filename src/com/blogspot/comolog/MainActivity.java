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

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {

	private ServiceConnection mServiceConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			mSpeechRecognitionService = ((SpeechRecognitionService.LocalBinder) service)
					.getService();
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			mSpeechRecognitionService = null;
		}

	};
	private SpeechRecognition.ResultCallback mResultCallback = new SpeechRecognition.ResultCallback() {

		@Override
		public void onResults(List<String> results) {
			synchronized (MainActivity.this) {
				for (String s : results) {
					mResults += s + "\n";
				}
			}
			mHandler.post(mRunnable);
		}

		@Override
		public void onError(String reason) {
			synchronized (MainActivity.this) {
				mResults = new String(reason);
			}
			mHandler.post(mRunnable);
		}

	};
	private EditText mEditText1;
	private Handler mHandler;
	private String mResults;
	private Runnable mRunnable = new Runnable() {

		@Override
		public void run() {
			synchronized (MainActivity.this) {
				mEditText1.setText(mResults);
			}
		}

	};
	private SpeechRecognitionService mSpeechRecognitionService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		bindService(new Intent(MainActivity.this,
				SpeechRecognitionService.class), mServiceConnection,
				Context.BIND_AUTO_CREATE);

		Button btn = (Button) findViewById(R.id.button1);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mResults = "";
				mEditText1.setText(getString(R.string.recognizing));
				mSpeechRecognitionService.startRecognition(mResultCallback);
			}

		});

		mEditText1 = (EditText) findViewById(R.id.editText1);

		mHandler = new Handler();

		mResults = new String();
	}

	@Override
	protected void onDestroy() {

		unbindService(mServiceConnection);

		super.onDestroy();
	}

}
