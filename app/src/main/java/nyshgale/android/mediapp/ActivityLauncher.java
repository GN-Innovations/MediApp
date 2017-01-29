package nyshgale.android.mediapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import nyshgale.android.mediapp.database.ENGINE;
import nyshgale.android.mediapp.database.LIBRARY;
import nyshgale.android.mediapp.database.PATIENTS;
import nyshgale.android.mediapp.database.Patient;
import nyshgale.android.mediapp.database.Preferences;
import nyshgale.android.mediapp.database.SYSTEM;

import java.io.File;
import java.io.FileOutputStream;

public class ActivityLauncher
		extends Activity {

	private ProgressBar progress;

	private TextView init;

	private TextView tap;

	private boolean success;

	{
		success = false;
	}

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_launcher);
		progress = (ProgressBar) findViewById(R.id.progress_init__activity_launcher);
		init = (TextView) findViewById(R.id.textview_init__activity_launcher);
		tap = (TextView) findViewById(R.id.textview_tap__activity_launcher);
	}

	@Override
	protected void onStart () {
		super.onStart();
		SharedPreferences sp = getSharedPreferences("MediApp.Settings", 0);
		if (sp.getBoolean("FirstRun", true)) {
			DialogDisclaimer.create().show(getFragmentManager(), null);
		}
		new Initialization(getApplicationContext()).execute(LIBRARY.DB, PATIENTS.DB, SYSTEM.DB);
	}

	public void start (View view) {
		if (success) {
			Intent mainIntent = new Intent(this, nyshgale.android.mediapp.ActivityMain.class);
			startActivity(mainIntent);
			finish();
		}
	}

	private void initialized (boolean result) {
		if (result) {
			init.setText(getString(R.string.msg_init_success));
			tap.setVisibility(View.VISIBLE);
			success = true;
		} else {
			init.setText(getString(R.string.msg_init_failed));
		}
	}

	private class Initialization
			extends AsyncTask<String, Integer, Boolean> {

		private final String TAG = "Launcher.Initialization";

		Patient patient = new Patient();

		Preferences preferences = new Preferences();

		private Context context;

		public Initialization (Context context) {
			this.context = context;
		}

		@Override
		protected Boolean doInBackground (String... params) {
			boolean result = false;
			int count = params.length;
			for (int index = 0; index < count; index++) {
				SQLiteDatabase database = null;
				try {
					database = ENGINE.start(ENGINE.READ, params[index]);
					result = database.isOpen();
				} catch (Exception ex) {
					Log.e(TAG, "doInBackground: " + ex.getMessage());
				} finally {
					ENGINE.stop(database);
				}
				initializeData(params[index]);
				if (isCancelled() || !result) {
					break;
				}
				publishProgress((int) ((index + 1) / (float) count) * 100);
			}
			return result;
		}

		@Override
		protected void onPreExecute () {
			super.onPreExecute();
			ENGINE.init(context);
		}

		@Override
		protected void onPostExecute (Boolean result) {
			super.onPostExecute(result);
			initialized(result);
		}

		@Override
		protected void onProgressUpdate (Integer... values) {
			super.onProgressUpdate(values);
			progress.setProgress(values[0]);
		}

		private void initializeData (String database) {
			switch (database) {
				case LIBRARY.DB:
					try {
						File library = new File(ENGINE.appFolder().getAbsolutePath() + "/library.mediapp-library");
						if (ENGINE.appFolder().list().length == 0 && context.getAssets().list("library").length > 0) {
							ENGINE.clone(context.getAssets().open("library/8"), new FileOutputStream(library));
						}
					} catch (Exception ex) {
						Log.e(TAG, "initializeData: " + ex.getMessage());
					}
					break;
				case PATIENTS.DB:
					if (Patient.getRecords(null, null, null, null).isEmpty()) {
						patient.setCode("000");
						patient.setLastName("Anonymous");
						patient.setFirstName("");
						patient.setBirthDate(0);
						patient.setGender("");
						patient.save();
						if (patient.isSaved()) {
							Log.i(TAG, "Patient: " + patient.getCode() + " has been saved");
						}
					}
					break;
				case SYSTEM.DB:
					if (Preferences.get().getId() == 0) {
						preferences.setPatientCode(patient.getCode());
						preferences.save();
						if (preferences.isSaved()) {
							Log.i(TAG, "Preferences saved: Patient:" + patient.getCode() + " is the current");
						}
					}
					break;
			}
		}

	}

}
