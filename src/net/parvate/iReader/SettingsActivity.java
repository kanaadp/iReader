package net.parvate.iReader;

import net.parvate.R;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class SettingsActivity extends Activity {
	final static int defaultcolumnSpace = 50;
	final static int defaultparagraphSpace = 50;
	final static int defaultblackThreshold = 3881787;
	protected static final String bt = "BlackThreshold";
	protected static final String ps = "ParagraphSpace";
	protected static final String cs = "ColumnSpace";
	protected static final String lang = "Language";
	protected static final String sp = "SpinnerPos";
	private static Spinner spinner;
	private static ArrayAdapter<CharSequence> adapter;
	private static AppPreferences prefs;
	private static EditText blackThresholdText;
	private static EditText paragraphSpaceText;
	private static EditText columnSpaceText;

	@Override
	public void onCreate(Bundle SavedInstanceState) {

		super.onCreate(SavedInstanceState);
		setContentView(R.layout.settings_layout);

		SettingsActivity.blackThresholdText = (EditText) findViewById(R.id.EditText06);
		SettingsActivity.paragraphSpaceText = (EditText) findViewById(R.id.EditText08);
		SettingsActivity.columnSpaceText = (EditText) findViewById(R.id.EditText07);
		SettingsActivity.spinner = (Spinner) findViewById(R.id.spinner1);

		SettingsActivity.adapter = ArrayAdapter.createFromResource(this,
				R.array.Languages, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		prefs = new AppPreferences(this);

		boolean saved = prefs.getBoolean("saved");
		System.out.println(saved);
		if (saved) {
			System.out.println(prefs.getInt(bt));
			spinner.setSelection(prefs.getInt(sp));
			SettingsActivity.blackThresholdText.setText(String.valueOf(prefs
					.getInt(bt)));
			SettingsActivity.paragraphSpaceText.setText(String.valueOf(prefs
					.getInt(ps)));
			SettingsActivity.columnSpaceText.setText(String.valueOf(prefs
					.getInt(cs)));
		}
		System.out.println("Settings Loaded.");
	}

	@Override
	public void onPause() {
		super.onPause();
		int BlackThresholdValue = Integer.parseInt(blackThresholdText.getText()
				.toString());
		int ParagraphSpaceValue = Integer.parseInt(paragraphSpaceText.getText()
				.toString());
		int ColumnSpaceValue = Integer.parseInt(columnSpaceText.getText()
				.toString());
		String language = spinner.getSelectedItem().toString();
		int SpinnerPos = spinner.getSelectedItemPosition();
		prefs.saveInt(bt, BlackThresholdValue);
		prefs.saveInt(ps, ParagraphSpaceValue);
		prefs.saveInt(cs, ColumnSpaceValue);
		prefs.saveString(lang, language);
		prefs.saveInt(sp, SpinnerPos);
		prefs.saveBoolean("saved", true);
	}

	public void setDefaults(View view) {
		Log.i(getPackageName(), "Setting Defaults");
		// Gets Text fields.
		EditText blackThresholdText = (EditText) findViewById(R.id.EditText06);
		EditText paragraphSpaceText = (EditText) findViewById(R.id.EditText08);
		EditText columnSpaceText = (EditText) findViewById(R.id.EditText07);

		// Setting defaults
		System.out.println("Setting Defaults");
		// reset Black Threshold
		System.out.println("Original BlackThreshold Value :"
				+ blackThresholdText.getText());
		System.out
				.println("Setting value to default: " + defaultblackThreshold);
		blackThresholdText.setText(Integer.toString(defaultblackThreshold));

		// Reset Paragraph Spacing
		System.out.println(paragraphSpaceText.getText());
		System.out
				.println("Setting value to default: " + defaultparagraphSpace);
		paragraphSpaceText.setText(Integer.toString(defaultparagraphSpace));

		// Reset Column Spacing
		System.out.println(columnSpaceText.getText());
		System.out.println("Setting value to default: " + defaultcolumnSpace);
		columnSpaceText.setText(Integer.toString(defaultcolumnSpace));
	}

}
