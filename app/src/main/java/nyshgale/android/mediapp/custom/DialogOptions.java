package nyshgale.android.mediapp.custom;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import nyshgale.android.mediapp.database.Cause;
import nyshgale.android.mediapp.database.Illness;
import nyshgale.android.mediapp.database.Medication;
import nyshgale.android.mediapp.database.Patient;
import nyshgale.android.mediapp.database.Preferences;
import nyshgale.android.mediapp.database.Symptom;

/**
 * Options Dialog for edit and delete
 */
public class DialogOptions
		extends DialogFragment
		implements DialogInterface.OnClickListener {

	private String[] options = {};

	private Object object;

	private Event.OnOptionSelectedListener callback;

	public static DialogOptions create (Object object) {
		DialogOptions dialog = new DialogOptions();
		dialog.object = object;
		return dialog;
	}

	@Override
	public void onAttach (Activity activity) {
		super.onAttach(activity);
		try {
			callback = (Event.OnOptionSelectedListener) activity;
		} catch (Exception ex) {
			Log.e(this.toString(), ex.getMessage());
		}
	}

	@Override
	public Dialog onCreateDialog (Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(getTitle());
		if (object.toString().equals(Patient.TYPE)) {
			Patient patient = (Patient) object;
			if (patient.getCode().equals("000")) {
				options = new String[] {"Set as Current"};
				builder.setItems(options, this);
				if (Preferences.get().getPatientCode().equals(patient.getCode())) {
					options = new String[] {"Current"};
					builder.setItems(options, this);
				}
			} else {
				options = new String[] {"Set as Current", "Edit", "Delete"};
				builder.setItems(options, this);
				if (Preferences.get().getPatientCode().equals(patient.getCode())) {
					options = new String[] {"Current", "Edit"};
					builder.setItems(options, this);
				}
			}
		} else {
			options = new String[] {"Edit", "Delete"};
			builder.setItems(options, this);
		}
		return builder.create();
	}

	@Override
	public void onClick (DialogInterface dialog, int which) {
		if (callback != null) {
			callback.onOptionSelected(options[which]);
		}
	}

	private String getTitle () {
		switch (object.toString()) {
			case Illness.TYPE:
				return ((Illness) object).getName();
			case Symptom.TYPE:
				return ((Symptom) object).getName();
			case Cause.TYPE:
				return ((Cause) object).getDetails();
			case Medication.TYPE:
				return ((Medication) object).getName();
			case Patient.TYPE:
				return "Patient: " + ((Patient) object).getCode();
			default:
				return "";
		}
	}

}
