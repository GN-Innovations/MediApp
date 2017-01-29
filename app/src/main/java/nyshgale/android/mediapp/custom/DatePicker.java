package nyshgale.android.mediapp.custom;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;

import java.util.Calendar;

public class DatePicker
		extends DialogFragment
		implements DatePickerDialog.OnDateSetListener {

	private Event.OnDateSetListener callback;

	private Calendar calendar = Calendar.getInstance();

	public static DatePicker create (long millis) {
		DatePicker dialog = new DatePicker();
		dialog.calendar.setTimeInMillis(millis);
		return dialog;
	}

	@Override
	public void onAttach (Activity activity) {
		super.onAttach(activity);
		try {
			callback = (Event.OnDateSetListener) activity;
		} catch (Exception ex) {
			Log.e(this.toString(), ex.getMessage());
		}
	}

	@Override
	public Dialog onCreateDialog (Bundle savedInstanceState) {
		DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
		dialog.setTitle("Set Birth Date");
		return dialog;
	}

	@Override
	public void onDateSet (android.widget.DatePicker view, int year, int monthOfYear, int dayOfMonth) {
		if (callback != null) {
			calendar.set(year, monthOfYear, dayOfMonth);
			callback.onDateSet(calendar.getTimeInMillis());
		}
	}
}
