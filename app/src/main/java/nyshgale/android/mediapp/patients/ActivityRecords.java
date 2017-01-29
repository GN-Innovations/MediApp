package nyshgale.android.mediapp.patients;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import nyshgale.android.mediapp.R;
import nyshgale.android.mediapp.database.Patient;

import java.util.List;

public class ActivityRecords
		extends Activity
		implements AdapterView.OnItemSelectedListener {

	private ListRecords listRecords;

	private Spinner patientView;

	private ArrayAdapter<String> adapter;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_patients_records);
		listRecords = (ListRecords) getFragmentManager().findFragmentById(R.id.fragment_list__activity_patients_records);
		patientView = (Spinner) findViewById(R.id.spinner_patient__activity_patients_records);
		patientView.setOnItemSelectedListener(this);
	}

	@Override
	protected void onStart () {
		super.onStart();
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
		getPatients();
		patientView.setAdapter(adapter);
	}

	@Override
	public void onItemSelected (AdapterView<?> parent, View view, int position, long id) {
		String code = parent.getItemAtPosition(position).toString().substring(8, 11);
		Patient patient = new Patient(code);
		listRecords.setPatient(patient);
	}

	@Override
	public void onNothingSelected (AdapterView<?> parent) {
		//do nothing
	}

	private void getPatients () {
		adapter.clear();
		List<Patient> patients = Patient.getRecords(null, null, null, null);
		adapter.add("All Patients");
		for (Patient patient : patients) {
			adapter.add("Patient " + patient.getCode() + ": " + patient.getFirstName() + " " + patient.getLastName());
		}
	}

}
