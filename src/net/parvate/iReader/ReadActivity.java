package net.parvate.iReader;

import java.io.File;
import java.util.ArrayList;

import com.googlecode.tesseract.android.TessBaseAPI;

import net.parvate.R;
import net.parvate.iReader.imageproc.ImageProcessing;
import net.parvate.iReader.imageproc.Line;
import net.parvate.iReader.imageproc.SmartScanner;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ReadActivity extends Activity {
	private static final int NO_OUTPUT = 0;
	private static final int LANGUAGE_NOT_SUPPORTED = 1;
	private static final String TESSBASE_PATH = "/mnt/sdcard/";
	static int numParagraphs = 0;
	private static File image;
	private static TessBaseAPI tessbase = new TessBaseAPI();
	private static ArrayList<Line> paragraphs;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.read_layout);
		ReadActivity.image = (File) this.getIntent().getExtras().get("image");
		System.out.println(ReadActivity.image.exists());
		AppPreferences prefs = new AppPreferences(this);

		int spinnerpos = prefs.getInt(SettingsActivity.sp);
		if (spinnerpos != 42) {
			System.out.println();
			if (spinnerpos == 0) {
				// english
				System.out.println(prefs.getString(SettingsActivity.lang));
				tessbase.init(TESSBASE_PATH, "eng");

			} else if (spinnerpos == 1) {
				// french
				System.out.println(prefs.getString(SettingsActivity.lang));
				showDialog(LANGUAGE_NOT_SUPPORTED);
				tessbase.init(TESSBASE_PATH, "eng");
			}
			OCRactivity.setLanguage(spinnerpos);
		} else {
			tessbase.init(TESSBASE_PATH, "eng");
		}
	}

	public void ocrFull(View view) {
		System.out.println("OCRing Full image");
		TextView tv = (TextView) findViewById(R.id.textView2);
		Log.i(getPackageName(), "OCR process begun");
		Bitmap bmp = BitmapFactory.decodeFile(ReadActivity.image
				.getAbsolutePath());

		String output = net.parvate.iReader.imageproc.ImagetoText.ocrText(bmp,
				tessbase);
		System.out.println(output);

	}

	public void separateParagraphs(View view) {
		AppPreferences prefs = new AppPreferences(this);

		int spinnerpos = prefs.getInt(SettingsActivity.sp);
		if (spinnerpos != 42) {
			SmartScanner.setBlackThreshold(prefs.getInt(SettingsActivity.bt));
			SmartScanner.setColumnSpace(prefs.getInt(SettingsActivity.cs));
			SmartScanner.setParagraphSpace(prefs.getInt(SettingsActivity.ps));
		} else {
			SmartScanner
					.setBlackThreshold(SettingsActivity.defaultblackThreshold);
			SmartScanner.setColumnSpace(SettingsActivity.defaultcolumnSpace);
			SmartScanner
					.setParagraphSpace(SettingsActivity.defaultparagraphSpace);
		}

		Bitmap bmp = BitmapFactory.decodeFile(image.getAbsolutePath());
		paragraphs = ImageProcessing.find(bmp);

		LinearLayout top = (LinearLayout) findViewById(R.id.linearLayout2);
		top.setOrientation(LinearLayout.VERTICAL);

		for (int i = 0; i < paragraphs.size(); i++) {

			ArrayList<Integer> coords = paragraphs.get(i).getCoords();
			int width = coords.get(1) - coords.get(0);
			int height = coords.get(3) - coords.get(2);

			System.out.println("Height of paragraph = " + height);
			System.out.println("Width of paragraph = " + width);
			Bitmap read = Bitmap.createBitmap(bmp, coords.get(0),
					coords.get(2), width, height);

			LinearLayout linlayout = new LinearLayout(this);
			top.addView(linlayout);
			linlayout.setOrientation(LinearLayout.HORIZONTAL);
			ImageView imv = new ImageView(this);
			imv.setId(i * 2);
			imv.setImageBitmap(read);

			Button butt = new Button(this);
			butt.setId(i * 2 + 1);
			butt.setText("OCR");
			butt.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View view) {
					System.out.println("Butt Clicked! :D");
					ImageView imv = (ImageView) findViewById(view.getId() - 1);
					Bitmap bmp = ((BitmapDrawable) imv.getDrawable())
							.getBitmap();
					Context context = view.getContext();
					Intent i = new Intent(context, OCRactivity.class);
					String output = net.parvate.iReader.imageproc.ImagetoText
							.ocrText(bmp, ReadActivity.tessbase);
					System.out.println("Output ===============" + output);
					i.putExtra("image", bmp);
					i.putExtra("Output", output);

					context.startActivity(i);
				}
			});
			linlayout.addView(imv);
			linlayout.addView(butt);
			
		}
		
	}

	public void concatParagraphs(View view) {
		if (numParagraphs == 0) {

		} else {
			showDialog(NO_OUTPUT);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.reader_menu, menu);
		return true;
	}

	public void toSettings(MenuItem item) {
		Log.i(getPackageName(), "Settings");
		Intent i = new Intent(this, SettingsActivity.class);
		startActivity(i);
	}

	public void toAbout(MenuItem item) {
		Log.i(getPackageName(), "About");
	}

	public Dialog onCreateDialog(int type) {
		Dialog dialog = null;
		AlertDialog.Builder aBuilder = new AlertDialog.Builder(this);
		switch (type) {
		case NO_OUTPUT:
			aBuilder.setTitle("No output.");
			aBuilder.setMessage("No text has been OCRed.");
			aBuilder.setNegativeButton("Ok",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.cancel();
						}
					});
			AlertDialog invalidalert = aBuilder.create();
			dialog = invalidalert;
			break;
		case LANGUAGE_NOT_SUPPORTED:
			aBuilder.setTitle("Language not supported");
			aBuilder.setMessage("Support for this lagnuage is coming soon!");
			aBuilder.setNegativeButton("Ok",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.cancel();
						}
					});
			AlertDialog bmpalert = aBuilder.create();
			dialog = bmpalert;
			break;
		/*
		 * case LOADING: aBuilder. }); AlertDialog ttsalert = aBuilder.create();
		 * dialog = ttsalert; break;
		 */
		}

		return dialog;
	}
}
