package nyshgale.android.mediapp.custom;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import nyshgale.android.mediapp.R;
import nyshgale.android.mediapp.database.Cause;
import nyshgale.android.mediapp.database.Illness;
import nyshgale.android.mediapp.database.Medication;
import nyshgale.android.mediapp.database.Patient;
import nyshgale.android.mediapp.database.Symptom;

/**
 * Options Dialog for delete
 */
public class DialogDelete
		extends DialogFragment {

	private Object object;

	private Event.OnDeleteButtonClickedListener callback;

	public static DialogDelete create (Object object) {
		DialogDelete dialog = new DialogDelete();
		dialog.object = object;
		return dialog;
	}

	@Override
	public void onAttach (Activity activity) {
		super.onAttach(activity);
		try {
			callback = (Event.OnDeleteButtonClickedListener) activity;
		} catch (Exception ex) {
			Log.e(this.toString(), ex.getMessage());
		}
	}

	@Override
	public Dialog onCreateDialog (Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(getTitle());
		builder.setMessage("It can't be undone");
		builder.setPositiveButton(R.string.function_delete, new DialogInterface.OnClickListener() {

			@Override
			public void onClick (DialogInterface dialog, int id) {
				if (callback != null) {
					callback.onDeleteButtonClicked(object);
				}
			}
		});
		builder.setNegativeButton(R.string.function_cancel, null);
		return builder.create();
	}

	private String getTitle () {
		switch (object.toString()) {
			case Symptom.TYPE:
				return "Erase " + ((Symptom) object).getName() + "?";
			case Cause.TYPE:
				return "Erase " + ((Cause) object).getDetails() + "?";
			case Medication.TYPE:
				return "Erase " + ((Medication) object).getName() + "?";
			case Illness.TYPE:
				return "Erase " + ((Illness) object).getName() + "?";
			case Patient.TYPE:
				return "Erase Patient:" + ((Patient) object).getCode() + "?";
			default:
				return "";
		}
	}

}
