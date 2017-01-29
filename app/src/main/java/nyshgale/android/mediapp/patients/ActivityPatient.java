package nyshgale.android.mediapp.patients;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import nyshgale.android.mediapp.R;
import nyshgale.android.mediapp.custom.Adapter;
import nyshgale.android.mediapp.custom.DatePicker;
import nyshgale.android.mediapp.custom.Event;
import nyshgale.android.mediapp.database.Patient;

import java.util.Calendar;
import java.util.List;

/**
 * Activity for Illness
 */
public class ActivityPatient
		extends Activity
		implements Event.OnDateSetListener {

	private Patient patient;

	private TextView codeView;

	private AutoCompleteTextView lastnameView;

	private AutoCompleteTextView firstnameView;

	private EditText ageView;

	private Spinner genderView;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_patient);
		codeView = (TextView) findViewById(R.id.textview_code__edit_patient);
		lastnameView = (AutoCompleteTextView) findViewById(R.id.autotext_lastname__edit_patient);
		firstnameView = (AutoCompleteTextView) findViewById(R.id.autotext_firstname__edit_patient);
		ageView = (EditText) findViewById(R.id.edittext_age__edit_patient);
		genderView = (Spinner) findViewById(R.id.spinner_gender__edit_library_symptom);
	}

	@Override
	protected void onStart () {
		super.onStart();
		ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.patients_gender));
		lastnameView.setAdapter(new Adapter(this, Patient.TYPE));
		long id = getIntent().getLongExtra(Patient._ID, 0);
		if (id > 0) {
			patient = new Patient(id);
		} else {
			patient = new Patient();
			patient.setCode(generateCode());
		}
		if (patient != null) {
			setTitle("Patient: " + patient.getCode());
			codeView.setText("Code: " + patient.getCode());
			lastnameView.setText(patient.getLastName());
			firstnameView.setText(patient.getFirstName());
			ageView.setText(patient.getAge());
			genderView.setSelection(adapter.getPosition(patient.getGender()));
		}
	}

	@Override
	public boolean onCreateOptionsMenu (Menu menu) {
		getMenuInflater().inflate(R.menu.item_edit, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected (MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_item_save:
				save();
				return true;
			case R.id.action_item_cancel:
				final Intent libraryIntent = new Intent(this, ActivityMain.class);
				startActivity(libraryIntent);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	private void save () {
		if (patient == null) {
			patient = new Patient();
		}
		patient.setLastName(lastnameView.getText().toString());
		patient.setFirstName(firstnameView.getText().toString());
		patient.setGender(genderView.getSelectedItem().toString());
		patient.save();
		if (patient.isSaved()) {
			Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
			finish();
		} else {
			Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
		}
	}

	public void showDatePicker (View view) {
		long millis = Calendar.getInstance().getTimeInMillis();
		if (patient.getId() > 0 || patient.getBirthDate() != 0) {
			millis = patient.getBirthDate();
		}
		DatePicker.create(millis).show(getFragmentManager(), null);
	}

	@Override
	public void onDateSet (long millis) {
		patient.setBirthDate(millis);
		ageView.setText(patient.getAge());
	}

	private String generateCode () {
		String code;
		int number = 0;
		List<Patient> patients = Patient.getRecords(null, null, Patient._CODE, Patient._CODE);
		if (!patients.isEmpty()) {
			number = Integer.parseInt(patients.get(patients.size() - 1).getCode());
		}
		number++;
		code = Integer.toString(number);
		while (code.length() < 3) {
			code = "0" + code;
		}
		return code;
	}

}
