package nyshgale.android.mediapp.patients;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import nyshgale.android.mediapp.R;
import nyshgale.android.mediapp.custom.DialogChangePatient;
import nyshgale.android.mediapp.custom.Event;
import nyshgale.android.mediapp.custom.PDF;
import nyshgale.android.mediapp.database.Cause;
import nyshgale.android.mediapp.database.Diagnosis;
import nyshgale.android.mediapp.database.Illness;
import nyshgale.android.mediapp.database.Medication;
import nyshgale.android.mediapp.database.Patient;
import nyshgale.android.mediapp.database.Symptom;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ActivityPatientDiagnosis
		extends Activity
		implements Event.OnPatientChangedListener {

	private TextView datetimeView;

	private TextView patientcodeView;

	private TextView patientnameView;

	private TextView symptomsView;

	private TextView causesView;

	private TextView illnessView;

	private TextView medicationsView;

	private LinearLayout symptomsLayout;

	private LinearLayout causesLayout;

	private LinearLayout medicationsLayout;

	private TextView remediesView;

	private TextView notesView;

	private Diagnosis diagnosis;

	private Boolean shown = false;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.info_diagnosis);
		datetimeView = (TextView) findViewById(R.id.textview_datetime__info_diagnosis);
		patientcodeView = (TextView) findViewById(R.id.textview_patient_code__info_diagnosis);
		patientnameView = (TextView) findViewById(R.id.textview_patient_name__info_diagnosis);
		symptomsView = (TextView) findViewById(R.id.textview_symptoms__info_diagnosis);
		causesView = (TextView) findViewById(R.id.textview_causes__info_diagnosis);
		illnessView = (TextView) findViewById(R.id.textview_illness__info_diagnosis);
		medicationsView = (TextView) findViewById(R.id.textview_medications__info_diagnosis);
		symptomsLayout = (LinearLayout) findViewById(R.id.linearlayout_symptoms__info_diagnosis);
		causesLayout = (LinearLayout) findViewById(R.id.linearlayout_causes__info_diagnosis);
		medicationsLayout = (LinearLayout) findViewById(R.id.linearlayout_medications__info_diagnosis);
		remediesView = (TextView) findViewById(R.id.textview_remedies__info_diagnosis);
		notesView = (TextView) findViewById(R.id.textview_notes__info_diagnosis);
	}

	@Override
	protected void onStart () {
		super.onStart();
		long id = getIntent().getLongExtra(Diagnosis._ID, 0);
		diagnosis = new Diagnosis(id);
		if (!shown) {
			loadDiagnosis();
			shown = true;
		}
	}

	@Override
	public boolean onCreateOptionsMenu (Menu menu) {
		getMenuInflater().inflate(R.menu.diagnosis, menu);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			getMenuInflater().inflate(R.menu.export_to_pdf, menu);
		}
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected (MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_diagnosis_change_patient:
				DialogChangePatient.create().show(this.getFragmentManager(), null);
				return true;
			case R.id.action_export_to_pdf:
				PDF pdf = PDF.create(diagnosis);
				if (pdf.export(findViewById(R.id.layout_main__info_diagnosis))) {
					Toast.makeText(this, pdf.getFilename(), Toast.LENGTH_LONG).show();
				}
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	private void loadDiagnosis () {
		SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy - hh:mm:ss aa", Locale.getDefault());
		datetimeView.setText(dateFormat.format(new Date(diagnosis.getDatetime())));
		loadPatient(new Patient(diagnosis.getPatient()));
		symptomsView.setText("Symptoms given: ");
		loadSymptoms();
		if (!diagnosis.getCauses().isEmpty()) {
			causesView.setText("Causes given: ");
			loadCauses();
		} else {
			causesView.setText("No causes given.");
		}
		loadIllness(new Illness(diagnosis.getIllness()));
		medicationsView.setText("Proposed medications: ");
		loadMedications(new Illness(diagnosis.getIllness()));
	}

	private void loadPatient (Patient patient) {
		patientcodeView.setText("Patient Code:" + patient.getCode());
		patientnameView.setText(patient.getFirstName() + " " + patient.getLastName());
	}

	private void loadSymptoms () {
		symptomsLayout.removeAllViews();
		for (Symptom symptom : diagnosis.getSymptoms()) {
			View view = getLayoutInflater().inflate(R.layout.info_library_symptom, symptomsLayout, false);
			view.findViewById(R.id.textview_title__info_library_symptom).setVisibility(View.GONE);
			TextView textView = (TextView) view.findViewById(R.id.textview_name__info_library_symptom);
			textView.setText(symptom.getName());
			symptomsLayout.addView(view);
		}
	}

	private void loadCauses () {
		causesLayout.removeAllViews();
		for (Cause cause : diagnosis.getCauses()) {
			View view = getLayoutInflater().inflate(R.layout.info_library_cause, causesLayout, false);
			view.findViewById(R.id.textview_title__info_library_cause).setVisibility(View.GONE);
			TextView textView = (TextView) view.findViewById(R.id.textview_details__info_library_cause);
			textView.setText(cause.getDetails());
			causesLayout.addView(view);
		}
	}

	private void loadIllness (Illness illness) {
		illnessView.setText("Possible illness: " + illness.getName());
		remediesView.setText(illness.getRemedies());
		notesView.setText(illness.getNotes());
	}

	private void loadMedications (Illness illness) {
		medicationsLayout.removeAllViews();
		for (Medication medication : illness.getMedications()) {
			View view = getLayoutInflater().inflate(R.layout.info_library_medication, medicationsLayout, false);
			view.findViewById(R.id.textview_title__info_library_medication).setVisibility(View.GONE);
			TextView textView = (TextView) view.findViewById(R.id.textview_name__info_library_medication);
			textView.setText(medication.getName());
			medicationsLayout.addView(view);
		}
	}

	@Override
	public void onPatientChanged (Patient patient) {
		diagnosis.setPatient(patient.getId());
		diagnosis.save();
		if (diagnosis.isSaved()) {
			loadPatient(patient);
			Toast.makeText(this, "Diagnosis saved", Toast.LENGTH_SHORT).show();
		}
	}

}
