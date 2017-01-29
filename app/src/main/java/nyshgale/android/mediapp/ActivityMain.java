package nyshgale.android.mediapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import nyshgale.android.mediapp.custom.Event;
import nyshgale.android.mediapp.database.Patient;
import nyshgale.android.mediapp.database.Preferences;

/**
 * Main Activity
 */
public class ActivityMain
		extends Activity
		implements Event.OnPatientChangedListener {

	private FragmentCurrentPatient currentPatientLayout;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		currentPatientLayout = (FragmentCurrentPatient) getFragmentManager().findFragmentById(R.id.fragment_current_patient__activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu (Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected (MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_patients:
				final Intent patientsActivity = new Intent(this, nyshgale.android.mediapp.patients.ActivityMain.class);
				startActivity(patientsActivity);
				return true;
			case R.id.action_patients_records:
				final Intent recordsActivity = new Intent(this, nyshgale.android.mediapp.patients.ActivityRecords.class);
				startActivity(recordsActivity);
				return true;
			case R.id.action_library:
				final Intent libraryActivity = new Intent(this, nyshgale.android.mediapp.library.ActivityMain.class);
				startActivity(libraryActivity);
				return true;
			case R.id.action_about:
				final Intent aboutActivity = new Intent(this, nyshgale.android.mediapp.ActivityAbout.class);
				startActivity(aboutActivity);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onPatientChanged (Patient patient) {
		Preferences preferences = Preferences.get();
		preferences.setPatientCode(patient.getCode());
		preferences.save();
		if (preferences.isSaved()) {
			currentPatientLayout.loadPatient();
			Toast.makeText(this, "Preferences saved", Toast.LENGTH_SHORT).show();
		}
	}

}
