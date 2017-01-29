package nyshgale.android.mediapp.custom;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import nyshgale.android.mediapp.database.Patient;

import java.util.List;

public class DialogChangePatient
		extends DialogFragment {

	private Event.OnPatientChangedListener callback;

	public static DialogChangePatient create () {
		DialogChangePatient dialog = new DialogChangePatient();
		return dialog;
	}

	@Override
	public void onAttach (Activity activity) {
		super.onAttach(activity);
		try {
			callback = (Event.OnPatientChangedListener) activity;
		} catch (Exception ex) {
			Log.e("", ex.getMessage());
		}
	}

	@Override
	public Dialog onCreateDialog (Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		final List<Patient> patients = Patient.getRecords(null, null, null, Patient._CODE);
		final String[] list = new String[patients.size() + 1];
		int index = 0;
		for (Patient patient : patients) {
			list[index] = patient.getCode() + ": " + patient.getFirstName() + " " + patient.getLastName();
			index++;
		}
		list[index] = "Create patient";
		builder.setTitle("Choose patient");
		builder.setItems(list, new DialogInterface.OnClickListener() {

			@Override
			public void onClick (DialogInterface dialog, int which) {
				String code = list[which].substring(0, 3);
				if (callback != null) {
					if (which == patients.size()) {
						startActivity(new Intent(getActivity(), nyshgale.android.mediapp.patients.ActivityPatient.class));
					} else {
						callback.onPatientChanged(new Patient(code));
					}
				}
			}

		});
		return builder.create();
	}

}
