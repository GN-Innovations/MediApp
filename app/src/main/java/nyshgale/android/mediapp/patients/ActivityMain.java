package nyshgale.android.mediapp.patients;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import nyshgale.android.mediapp.R;
import nyshgale.android.mediapp.custom.DialogDelete;
import nyshgale.android.mediapp.custom.DialogOptions;
import nyshgale.android.mediapp.custom.Event;
import nyshgale.android.mediapp.database.Patient;
import nyshgale.android.mediapp.database.Preferences;

public class ActivityMain
		extends Activity
		implements Event.OnItemLongClickedListener,
		           Event.OnOptionSelectedListener,
		           Event.OnDeleteButtonClickedListener {

	private ListPatients listPatients;

	private Patient patient;

	{
		patient = new Patient();
	}

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_patients);
		listPatients = (ListPatients) getFragmentManager().findFragmentById(R.id.fragment_list__activity_patients);
	}

	@Override
	protected void onResume () {
		super.onResume();
		if (listPatients.loader != null) {
			listPatients.loader.onContentChanged();
		}
	}

	@Override
	public boolean onCreateOptionsMenu (Menu menu) {
		getMenuInflater().inflate(R.menu.patients, menu);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected (MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_patients_new_item:
				editPatient(new Patient());
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	private void editPatient (Patient patient) {
		final Intent patientActivity = new Intent(this, ActivityPatient.class);
		patientActivity.putExtra(Patient._ID, patient.getId());
		startActivity(patientActivity);
	}

	@Override
	public void onItemLongClicked (Object object) {
		this.patient = (Patient) object;
		DialogOptions.create(object).show(getFragmentManager(), null);
	}

	@Override
	public void onOptionSelected (String option) {
		switch (option) {
			case "Set as Current":
				Preferences preferences = Preferences.get();
				preferences.setPatientCode(patient.getCode());
				preferences.save();
				if (preferences.isSaved()) {
					Toast.makeText(this, "Preferences saved", Toast.LENGTH_SHORT).show();
				}
				break;
			case "Edit":
				editPatient(patient);
				break;
			case "Delete":
				deleteItem(patient);
				break;
			default:
				break;
		}
	}

	private void deleteItem (Patient patient) {
		DialogDelete dialog = DialogDelete.create(patient);
		dialog.show(getFragmentManager(), null);
	}

	@Override
	public void onDeleteButtonClicked (Object object) {
		patient.delete();
		if (patient.isDeleted()) {
			listPatients.loader.onContentChanged();
			Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
		}
	}

}
