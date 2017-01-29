package nyshgale.android.mediapp.custom;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import nyshgale.android.mediapp.R;
import nyshgale.android.mediapp.database.Cause;
import nyshgale.android.mediapp.database.Illness;
import nyshgale.android.mediapp.database.Medication;
import nyshgale.android.mediapp.database.Patient;
import nyshgale.android.mediapp.database.Symptom;

/**
 * Dialog for Edit
 */
public class DialogInfo
		extends DialogFragment {

	private Object object;

	private Event.OnOkButtonClickedListener callback;

	public static DialogInfo create (Object object) {
		DialogInfo dialog = new DialogInfo();
		dialog.object = object;
		return dialog;
	}

	@Override
	public void onAttach (Activity activity) {
		super.onAttach(activity);
		try {
			if (object.toString().equals(Illness.TYPE)) {
				callback = (Event.OnOkButtonClickedListener) activity;
			}
		} catch (Exception ex) {
			Log.e("", ex.getMessage());
		}
	}

	@Override
	public Dialog onCreateDialog (Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setView(getLayout());
		builder.setNeutralButton(R.string.function_ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick (DialogInterface dialog, int which) {
				if (callback != null) {
					callback.onOkButtonClicked();
				}
			}
		});
		return builder.create();
	}

	@SuppressLint("InflateParams")
	private View getLayout () {
		LayoutInflater inflater = getActivity().getLayoutInflater();
		int padding = getResources().getDimensionPixelSize(R.dimen.dimen_4);
		View layout = null;
		LinearLayout linearLayout = null;
		TextView textView = null;
		switch (object.toString()) {
			case Symptom.TYPE:
				Symptom symptom = (Symptom) object;
				layout = inflater.inflate(R.layout.info_library_symptom, null, false);
				((TextView) layout.findViewById(R.id.textview_name__info_library_symptom)).setText(symptom.getName());
				return layout;
			case Cause.TYPE:
				Cause cause = (Cause) object;
				layout = inflater.inflate(R.layout.info_library_cause, null, false);
				((TextView) layout.findViewById(R.id.textview_details__info_library_cause)).setText(cause.getDetails());
				return layout;
			case Medication.TYPE:
				Medication medication = (Medication) object;
				layout = inflater.inflate(R.layout.info_library_medication, null, false);
				((TextView) layout.findViewById(R.id.textview_name__info_library_medication)).setText(medication.getName());
				return layout;
			case Illness.TYPE:
				Illness illness = (Illness) object;
				layout = inflater.inflate(R.layout.info_library_illness, null, false);
				((TextView) layout.findViewById(R.id.textview_name__info_library_illness)).setText(illness.getName());
				((TextView) layout.findViewById(R.id.textview_description__info_library_illness)).setText(illness.getDescription());
				linearLayout = (LinearLayout) layout.findViewById(R.id.linearlayout_symptoms__info_library_illness);
				for (Symptom illnessSymptom : illness.getSymptoms()) {
					View view = inflater.inflate(R.layout.info_library_symptom, linearLayout, false);
					view.findViewById(R.id.textview_title__info_library_symptom).setVisibility(View.GONE);
					;
					textView = (TextView) view.findViewById(R.id.textview_name__info_library_symptom);
					textView.setText(illnessSymptom.getName());
					textView.setPadding(padding, padding, padding, padding);
					linearLayout.addView(view);
				}
				linearLayout = (LinearLayout) layout.findViewById(R.id.linearlayout_causes__info_library_illness);
				for (Cause illnessCause : illness.getCauses()) {
					View view = inflater.inflate(R.layout.info_library_cause, linearLayout, false);
					view.findViewById(R.id.textview_title__info_library_cause).setVisibility(View.GONE);
					textView = (TextView) view.findViewById(R.id.textview_details__info_library_cause);
					textView.setText(illnessCause.getDetails());
					textView.setPadding(padding, padding, padding, padding);
					linearLayout.addView(view);
				}
				linearLayout = (LinearLayout) layout.findViewById(R.id.linearlayout_medications__info_library_illness);
				for (Medication illnessMedication : illness.getMedications()) {
					View view = inflater.inflate(R.layout.info_library_medication, linearLayout, false);
					view.findViewById(R.id.textview_title__info_library_medication).setVisibility(View.GONE);
					textView = (TextView) view.findViewById(R.id.textview_name__info_library_medication);
					textView.setText(illnessMedication.getName());
					textView.setPadding(padding, padding, padding, padding);
					linearLayout.addView(view);
				}
				((TextView) layout.findViewById(R.id.textview_remedies__info_library_illness)).setText(illness.getRemedies());
				((TextView) layout.findViewById(R.id.textview_notes__info_library_illness)).setText(illness.getNotes());
				return layout;
			case Patient.TYPE:
				Patient patient = (Patient) object;
				layout = inflater.inflate(R.layout.info_patient, null, false);
				((TextView) layout.findViewById(R.id.textview_code__info_patient)).setText("Code: " + patient.getCode());
				((TextView) layout.findViewById(R.id.textview_name__info_patient)).setText(patient.getFirstName() + " " + patient.getLastName());
				((TextView) layout.findViewById(R.id.textview_age__info_patient)).setText("Age: " + patient.getAge());
				((TextView) layout.findViewById(R.id.textview_gender__info_patient)).setText("Gender: " + patient.getGender());
				return layout;
			default:
				return layout;
		}
	}

}
