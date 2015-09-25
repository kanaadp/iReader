package net.parvate.iReader;

import java.io.File;

import net.parvate.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class IReaderActivity extends Activity {
	private static final int DIALOG_INVALID_IMAGE = 0;
	public static File directoryPathPictures;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			// SD card must be mounted and accessible
			Log.i(getPackageName(), "Media is Writable");

			Log.i(getPackageName(), "Activity Initialized");
		} else {
			Log.e(getPackageName(), "Media is not writeable");
		}

		directoryPathPictures = Environment
				.getExternalStoragePublicDirectory("Pictures/");

		System.out.println("directory: "
				+ directoryPathPictures.getAbsolutePath());
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

	public void onClick(View view) {
		Log.i(getPackageName(), "Image Button Clicked");
		File image = getImageFromEditText((EditText) findViewById(R.id.editText1));
		if (image != null) {
			Intent readIntent = new Intent(this, ReadActivity.class);
			readIntent.putExtra("image", image);
			startActivity(readIntent);
		}
	}

	public void onPreviewClick(View view) {
		ImageView preview = (ImageView) findViewById(R.id.imageView1);
		File image = getImageFromEditText((EditText) findViewById(R.id.editText1));
		if (image != null) {
			Bitmap bmp = BitmapFactory.decodeFile(image.getAbsolutePath());
			preview.setImageBitmap(bmp);
		}
	
	}

	public Dialog onCreateDialog(int type) {
		Dialog dialog = null;
		AlertDialog.Builder aBuilder = new AlertDialog.Builder(this);
		switch (type) {
		case DIALOG_INVALID_IMAGE:
			aBuilder.setTitle("Invalid Image.");
			aBuilder.setMessage("The image you have entered does not exist. Please check the image and try again.");
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

	public File getImageFromEditText(EditText editText) {
		String imageNameString = editText.getText().toString();
		File image = new File(directoryPathPictures, imageNameString);
		if (!image.exists()) {
			showDialog(DIALOG_INVALID_IMAGE);
			return null;
		} else {
			return image;
		}

	}

}