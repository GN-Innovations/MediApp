package nyshgale.android.mediapp.custom;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;
import nyshgale.android.mediapp.database.Cause;
import nyshgale.android.mediapp.database.Illness;
import nyshgale.android.mediapp.database.Medication;
import nyshgale.android.mediapp.database.Patient;
import nyshgale.android.mediapp.database.Symptom;

public class Loader
		extends AsyncTaskLoader<Cursor> {

	/**
	 * Cursor variable
	 */
	private Cursor data;

	/**
	 * Table to fetch the data
	 */
	private String type;

	/**
	 * Loader's Constructor
	 *
	 * @param context that is needed
	 */
	public Loader (Context context, String type) {
		super(context);
		this.type = type;
	}

	/**
	 * Called when the loader has been canceled. Data should be release
	 *
	 * @param data to be release
	 */
	@Override
	public void onCanceled (Cursor data) {
		super.onCanceled(data);
		if (data != null) {
			data.close();
		}
	}

	/**
	 * Loader's execution method
	 *
	 * @return cursor data
	 */
	@Override
	public Cursor loadInBackground () {
		switch (type) {
			case Symptom.TYPE:
				return Symptom.getCursor();
			case Cause.TYPE:
				return Cause.getCursor();
			case Medication.TYPE:
				return Medication.getCursor();
			case Illness.TYPE:
				return Illness.getCursor();
			case Patient.TYPE:
				return Patient.getCursor();
			default:
				return null;
		}
	}

	/**
	 * Loader's output method
	 *
	 * @param data to be delivered
	 */
	@Override
	public void deliverResult (Cursor data) {
		if (isReset()) {
			if (data != null) {
				data.close();
			}
		}
		this.data = data;
		if (isStarted()) {
			super.deliverResult(data);
		}
	}


	/**
	 * Loader's startup method
	 */
	@Override
	public void onStartLoading () {
		if (data != null) {
			deliverResult(data);
		}
		if (takeContentChanged() || data == null) {
			forceLoad();
		}
	}

	/**
	 * Called when the loader has been stop. Ensures that the loader will be
	 * cancelled
	 */
	@Override
	public void onStopLoading () {
		cancelLoad();
	}

	/**
	 * Resetting the loader
	 */
	@Override
	public void onReset () {
		super.onReset();
		onStopLoading();
		if (data != null) {
			data.close();
		}
	}

}
