package nyshgale.android.mediapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import nyshgale.android.mediapp.custom.DialogInfo;
import nyshgale.android.mediapp.custom.Event;
import nyshgale.android.mediapp.database.Cause;
import nyshgale.android.mediapp.database.Diagnosis;
import nyshgale.android.mediapp.database.Illness;
import nyshgale.android.mediapp.database.Medication;
import nyshgale.android.mediapp.database.Patient;
import nyshgale.android.mediapp.database.Preferences;
import nyshgale.android.mediapp.database.Symptom;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Diagnosis Activity
 */
public class ActivityDiagnose
		extends Activity
		implements Event.OnDiagnoseAnsweredListener,
		           Event.OnOkButtonClickedListener {

	private final static int MEDIAPP = 0x1;

	private final static int PATIENT = 0x2;

	private List<Illness> illnesses;

	private List<Symptom> symptoms;

	private List<Symptom> selectedSymptoms;

	private List<Symptom> deselectedSymptoms;

	private List<Cause> causes;

	private List<Cause> selectedCauses;

	private List<Cause> deselectedCauses;

	private LinearLayout linearLayout;

	private TextView datetimeView;

	private boolean hasResult;

	private Diagnosis diagnosis;

	{
		illnesses = new ArrayList<>();
		symptoms = new ArrayList<>();
		selectedSymptoms = new ArrayList<>();
		deselectedSymptoms = new ArrayList<>();
		causes = new ArrayList<>();
		selectedCauses = new ArrayList<>();
		deselectedCauses = new ArrayList<>();
		hasResult = false;
		diagnosis = new Diagnosis();
	}

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_diagnose);
		linearLayout = (LinearLayout) findViewById(R.id.linearlayout_messages__activity_diagnose);
		datetimeView = (TextView) findViewById(R.id.textview_datetime__activity_diagnose);
	}

	@Override
	protected void onStart () {
		super.onStart();
		if (!hasResult) {
			diagnosis.setDatetime(Calendar.getInstance().getTimeInMillis());
			diagnosis.setPatient(new Patient(Preferences.get().getPatientCode()).getId());
			SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMMM dd, yyyy - hh:mm:ss aa", Locale.getDefault());
			datetimeView.setText(dateFormat.format(new Date(diagnosis.getDatetime())));
			long id = getIntent().getLongExtra(Symptom._ID, 0);
			Symptom symptom = new Symptom(id);
			if (!selectedSymptoms.contains(symptom)) {
				selectedSymptoms.add(symptom);
			}
			String message = "Along with " + symptom.getName() + ", are you experiencing other symptoms?";
			DialogDiagnosis.create(new Object(), message).show(getFragmentManager(), null);
			showMessage(message, MEDIAPP);
		}
	}

	private void filterIllnessesBySymptoms () {
		illnesses = Illness.getRecordsBySymptoms(selectedSymptoms);
		List<Illness> items = new ArrayList<>(illnesses);
		for (Illness illness : items) {
			for (Symptom selected : selectedSymptoms) {
				if (!illness.getSymptoms().contains(selected)) {
					illnesses.remove(illness);
				}
			}
		}
		if (illnesses.size() > 1) {
			updateSymptoms();
		} else if (illnesses.size() == 1) {
			if (selectedSymptoms.size() > 3) {
				showResult(illnesses.get(0));
			} else {
				updateSymptoms();
			}
		} else {
			noResult();
		}
	}

	private void filterIllnessesBySymptom () {
		illnesses = Illness.getRecordsBySymptoms(selectedSymptoms);
		List<Illness> items = new ArrayList<>(illnesses);
		for (Illness illness : items) {
			if (!illness.getSymptoms().contains(selectedSymptoms.get(0))) {
				illnesses.remove(illness);
			}
			if (illness.getSymptoms().size() > 1) {
				illnesses.remove(illness);
			}
		}
		if (illnesses.size() == 1) {
			showResult(illnesses.get(0));
		} else {
			noResult();
		}
	}

	private void filterIllnessesByCauses () {
		List<Illness> items = new ArrayList<>(illnesses);
		for (Illness illness : items) {
			for (Cause selected : selectedCauses) {
				if (!illness.getCauses().contains(selected)) {
					illnesses.remove(illness);
				}
			}
		}
		if (illnesses.size() > 1) {
			updateCauses();
		} else if (illnesses.size() == 1) {
			showResult(illnesses.get(0));
		} else {
			noResult();
		}
	}

	private void updateSymptoms () {
		symptoms.clear();
		for (Illness illness : illnesses) {
			for (Symptom symptom : illness.getSymptoms()) {
				if (!selectedSymptoms.contains(symptom) && !deselectedSymptoms.contains(symptom) && !symptoms.contains(symptom)) {
					symptoms.add(symptom);
				}
			}
		}
		if (symptoms.size() > 0) {
			Collections.shuffle(symptoms);
			ask(symptoms.get(0));
		} else {
			rankIllnesses();
		}
	}

	private void updateCauses () {
		causes.clear();
		for (Illness illness : illnesses) {
			for (Cause cause : illness.getCauses()) {
				if (!selectedCauses.contains(cause) && !deselectedCauses.contains(cause) && !causes.contains(cause)) {
					causes.add(cause);
				}
			}
		}
		if (causes.size() > 0) {
			Collections.shuffle(causes);
			ask(causes.get(0));
		} else {
			multipleResult();
		}
	}

	private void rankIllnesses () {
		int highest = 0;
		int counter;
		for (Illness illness : illnesses) {
			counter = 0;
			for (Symptom symptom : illness.getSymptoms()) {
				if (selectedSymptoms.contains(symptom)) {
					counter++;
				}
			}
			if (counter >= highest) {
				highest = counter;
			}
		}
		List<Illness> items = new ArrayList<>(illnesses);
		for (Illness illness : items) {
			counter = 0;
			for (Symptom symptom : illness.getSymptoms()) {
				if (selectedSymptoms.contains(symptom)) {
					counter++;
				}
			}
			if (counter < highest) {
				illnesses.remove(illness);
			}
		}
		if (illnesses.size() == 1) {
			if (selectedSymptoms.size() < 4) {
				updateCauses();
			} else {
				showResult(illnesses.get(0));
			}
		} else if (illnesses.isEmpty()) {
			noResult();
		} else {
			updateCauses();
		}
	}

	private void ask (Symptom symptom) {
		String message = "Do you have " + symptom.getName() + "?";
		DialogDiagnosis.create(symptom, message).show(getFragmentManager(), null);
		showMessage(message, MEDIAPP);
	}

	private void ask (Cause cause) {
		String message = "Have you experienced " + cause.getDetails() + " lately?";
		DialogDiagnosis.create(cause, message).show(getFragmentManager(), null);
		showMessage(message, MEDIAPP);
	}

	private void noResult () {
		showMessage("Inadequate symptoms for proper diagnosis.", MEDIAPP);
		hasResult = true;
	}

	private void multipleResult () {
		String illnessesName = "";
		for (Illness illness : illnesses) {
			illnessesName += "\n" + illness.getName();
		}
		showMessage("Possible diagnosis for your symptoms:" + illnessesName, MEDIAPP);
		hasResult = true;
	}

	private void showResult (Illness illness) {
		diagnosis.setIllness(illness.getId());
		diagnosis.setSymptoms(selectedSymptoms);
		diagnosis.setCauses(selectedCauses);
		diagnosis.save();
		showMessage("You maybe experiencing " + illness.getName() + ".", MEDIAPP);
		String medications = "Medications: ";
		for (Medication medication : illness.getMedications()) {
			medications += "\n " + medication.getName();
		}
		showMessage(medications, MEDIAPP);
		if (!illness.getRemedies().isEmpty()) {
			showMessage("Health Tips: \n" + illness.getRemedies(), MEDIAPP);
		}
		if (!illness.getNotes().isEmpty()) {
			showMessage("Notes: \n" + illness.getNotes(), MEDIAPP);
		}
		hasResult = true;
		if (diagnosis.isSaved()) {
			Toast.makeText(this, "Diagnosis saved.", Toast.LENGTH_SHORT).show();
		}
		DialogInfo.create(illness).show(getFragmentManager(), null);
	}

	@Override
	public void onAnswered (Object object, boolean answer) {
		String message = (answer) ? "Yes" : "No";
		showMessage(message, PATIENT);
		switch (object.toString()) {
			case Cause.TYPE:
				if (answer) {
					selectedCauses.add((Cause) object);
					filterIllnessesByCauses();
				} else {
					deselectedCauses.add((Cause) object);
					updateCauses();
				}
				break;
			case Symptom.TYPE:
				if (answer) {
					selectedSymptoms.add((Symptom) object);
					filterIllnessesBySymptoms();
				} else {
					deselectedSymptoms.add((Symptom) object);
					updateSymptoms();
				}
				break;
			default:
				if (!answer) {
					filterIllnessesBySymptom();
				} else {
					filterIllnessesBySymptoms();
				}
				break;
		}
	}

	@Override
	public void onOkButtonClicked () {
		final Intent diagnosisActivity = new Intent(this, nyshgale.android.mediapp.patients.ActivityPatientDiagnosis.class);
		diagnosisActivity.putExtra(Diagnosis._ID, diagnosis.getId());
		startActivity(diagnosisActivity);
	}

	private void showMessage (String message, int type) {
		View view = new View(this);
		TextView textView = new TextView(this);
		switch (type) {
			case MEDIAPP:
				view = getLayoutInflater().inflate(R.layout.fragment_diagnosis_mediapp, linearLayout, false);
				textView = (TextView) view.findViewById(R.id.textview_msg__view_diagnosis_mediapp);
				break;
			case PATIENT:
				view = getLayoutInflater().inflate(R.layout.fragment_diagnosis_patient, linearLayout, false);
				textView = (TextView) view.findViewById(R.id.textview_msg__view_diagnosis_patient);
				break;
		}
		textView.setText(message);
		linearLayout.addView(view);
		view.setFocusableInTouchMode(true);
		view.requestFocus();
	}

}
