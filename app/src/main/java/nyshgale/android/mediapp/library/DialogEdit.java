package nyshgale.android.mediapp.library;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AutoCompleteTextView;
import nyshgale.android.mediapp.R;
import nyshgale.android.mediapp.custom.Adapter;
import nyshgale.android.mediapp.custom.Event;
import nyshgale.android.mediapp.database.Cause;
import nyshgale.android.mediapp.database.Medication;
import nyshgale.android.mediapp.database.Symptom;

/**
 * Dialog for Edit
 */
public class DialogEdit
		extends DialogFragment {

	private AutoCompleteTextView symptomName;

	private AutoCompleteTextView causeDetails;

	private AutoCompleteTextView medicationName;

	private Object object;

	private Event.OnSaveButtonClickedListener callback;

	public static DialogEdit create (Object object) {
		DialogEdit dialog = new DialogEdit();
		dialog.object = object;
		return dialog;
	}

	@Override
	public void onAttach (Activity activity) {
		super.onAttach(activity);
		try {
			callback = (Event.OnSaveButtonClickedListener) activity;
		} catch (Exception ex) {
			Log.e(this.toString(), ex.getMessage());
		}
	}

	@Override
	public Dialog onCreateDialog (Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(getTitle());
		builder.setView(getLayout());
		builder.setPositiveButton(R.string.function_save, new DialogInterface.OnClickListener() {
			@Override
			public void onClick (DialogInterface dialog, int which) {
				if (callback != null) {
					switch (object.toString()) {
						case Symptom.TYPE:
							Symptom symptom = (Symptom) object;
							symptom.setName(symptomName.getText().toString());
							callback.onSaveButtonClicked(symptom);
							break;
						case Cause.TYPE:
							Cause cause = (Cause) object;
							cause.setDetails(causeDetails.getText().toString());
							callback.onSaveButtonClicked(cause);
							break;
						case Medication.TYPE:
							Medication medication = (Medication) object;
							medication.setName(medicationName.getText().toString());
							callback.onSaveButtonClicked(medication);
							break;
						default:
							break;
					}
				}
			}
		});
		builder.setNegativeButton(R.string.function_cancel, null);
		return builder.create();
	}

	@Override
	public void onStart () {
		super.onStart();
		switch (object.toString()) {
			case Symptom.TYPE:
				Symptom symptom = (Symptom) object;
				symptomName.setText(symptom.getName());
				break;
			case Cause.TYPE:
				Cause cause = (Cause) object;
				causeDetails.setText(cause.getDetails());
				break;
			case Medication.TYPE:
				Medication medication = (Medication) object;
				medicationName.setText(medication.getName());
				break;
		}
	}

	private String getTitle () {
		switch (object.toString()) {
			case Symptom.TYPE:
				return "Symptom";
			case Cause.TYPE:
				return "Cause";
			case Medication.TYPE:
				return "Medication";
			default:
				return "";
		}
	}

	private View getLayout () {
		LayoutInflater inflater = getActivity().getLayoutInflater();
		int paddingVertical = getResources().getDimensionPixelSize(R.dimen.dimen_16);
		int paddingHorizontal = getResources().getDimensionPixelSize(R.dimen.dimen_4);
		View layout = null;
		switch (object.toString()) {
			case Symptom.TYPE:
				layout = inflater.inflate(R.layout.edit_library_symptom, null, false);
				layout.setPadding(paddingHorizontal, paddingVertical, paddingHorizontal, paddingVertical);
				symptomName = (AutoCompleteTextView) layout.findViewById(R.id.autotext_name__edit_library_symptom);
				symptomName.setAdapter(new Adapter(getActivity(), Symptom.TYPE));
				layout.findViewById(R.id.imagebutton_remove__edit_library_symptom).setVisibility(View.GONE);
				break;
			case Cause.TYPE:
				layout = inflater.inflate(R.layout.edit_library_cause, null, false);
				layout.setPadding(paddingHorizontal, paddingVertical, paddingHorizontal, paddingVertical);
				causeDetails = (AutoCompleteTextView) layout.findViewById(R.id.autotext_details__edit_library_cause);
				causeDetails.setAdapter(new Adapter(getActivity(), Medication.TYPE));
				layout.findViewById(R.id.imagebutton_remove__edit_library_cause).setVisibility(View.GONE);
				break;
			case Medication.TYPE:
				layout = inflater.inflate(R.layout.edit_library_medication, null, false);
				layout.setPadding(paddingHorizontal, paddingVertical, paddingHorizontal, paddingVertical);
				medicationName = (AutoCompleteTextView) layout.findViewById(R.id.autotext_name__edit_library_medication);
				medicationName.setAdapter(new Adapter(getActivity(), Medication.TYPE));
				layout.findViewById(R.id.imagebutton_remove__edit_library_medication).setVisibility(View.GONE);
				break;
		}
		return layout;
	}

}
