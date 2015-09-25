package net.parvate.iReader;

import java.util.Locale;

import net.parvate.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class OCRactivity extends Activity implements OnInitListener {

	private static final int NO_OUTPUT = 0;
	Bitmap image;
	String output;
	private TextToSpeech mTts;
	private static int language;
	static final int MY_DATA_CHECK_CODE = 0;
	
	public static void setLanguage(int language){
		OCRactivity.language = language;
	}
	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ocr_layout);
		image = (Bitmap) this.getIntent().getExtras().get("image");
		output = this.getIntent().getExtras().getString("Output");
		System.out.println("Output ==========================>" + output);
		ImageView imv = (ImageView) findViewById(R.id.imageView1);
		imv.setImageBitmap(image);
		TextView tv = (TextView) findViewById(R.id.textView1);
		tv.setText(output);
		mTts = new TextToSpeech(this, this);
	}
	
	@Override
	public void onDestroy() {
		mTts.shutdown();
		super.onDestroy();
	}

	public void speakClick(View view) {
		System.out.println("Time to speak!");
		if (output == null) {
			showDialog(NO_OUTPUT);

		} else {
			if (language == 0) {
				mTts.setLanguage(Locale.US);
				mTts.speak(output, TextToSpeech.QUEUE_FLUSH, null);
			} else if (language == 1) {
				mTts.setLanguage(Locale.FRANCE);
				mTts.speak(output, TextToSpeech.QUEUE_FLUSH, null);
			}
		}

	}

	public Dialog onCreateDialog(int type) {
		Dialog dialog = null;
		AlertDialog.Builder aBuilder = new AlertDialog.Builder(this);
		switch (type) {
		case NO_OUTPUT:
			aBuilder.setTitle("No output.");
			aBuilder.setMessage("No words could be detected.");
			aBuilder.setNegativeButton("Ok",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.cancel();
						}
					});
			AlertDialog invalidalert = aBuilder.create();
			dialog = invalidalert;
			break;
		}

		return dialog;
	}
	
	

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		System.out.println("On Activity Result.");
		if (requestCode == MY_DATA_CHECK_CODE) {
			if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
				// success, create the TTS instance
				mTts = new TextToSpeech(this, this);
			} else {
				// missing data, install it
				Intent installIntent = new Intent();
				installIntent
						.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
				startActivity(installIntent);
			}
		}
	}



	@Override
	public void onInit(int arg0) {
		// TODO Auto-generated method stub
		System.out.println("Text to Speech engine Initialized.");
	}
}
