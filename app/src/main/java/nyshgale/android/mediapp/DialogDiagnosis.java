package nyshgale.android.mediapp;

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
import android.widget.TextView;
import nyshgale.android.mediapp.custom.Event;

/**
 * Diagnose Dialog
 */
public class DialogDiagnosis
		extends DialogFragment {

	public Object object;

	public String message;

	private Event.OnDiagnoseAnsweredListener callback;

	public static DialogDiagnosis create (Object object, String message) {
		DialogDiagnosis dialog = new DialogDiagnosis();
		dialog.object = object;
		dialog.message = message;
		return dialog;
	}

	@Override
	public void onAttach (Activity activity) {
		super.onAttach(activity);
		try {
			callback = (Event.OnDiagnoseAnsweredListener) activity;
		} catch (Exception ex) {
			Log.e(this.toString(), ex.getMessage());
		}
	}

	@Override
	public Dialog onCreateDialog (Bundle savedInstanceState) {
		super.onCreateDialog(savedInstanceState);
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setView(getLayout());
		builder.setPositiveButton(R.string.function_yes, new DialogInterface.OnClickListener() {
			@Override
			public void onClick (DialogInterface dialog, int which) {
				if (callback != null) {
					callback.onAnswered(object, true);
				}
			}
		});
		builder.setNegativeButton(R.string.function_no, new DialogInterface.OnClickListener() {
			@Override
			public void onClick (DialogInterface dialog, int which) {
				if (callback != null) {
					callback.onAnswered(object, false);
				}
			}
		});
		return builder.create();
	}

	@SuppressLint("InflateParams")
	private View getLayout () {
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View layout = inflater.inflate(R.layout.fragment_diagnose_ask, null, false);
		((TextView) layout.findViewById(R.id.textview_message__fragment_diagnose_ask)).setText(message);
		return layout;
	}

}
