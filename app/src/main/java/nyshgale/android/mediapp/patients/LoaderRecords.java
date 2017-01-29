package nyshgale.android.mediapp.patients;

import android.content.AsyncTaskLoader;
import android.content.Context;
import nyshgale.android.mediapp.database.Diagnosis;
import nyshgale.android.mediapp.database.Patient;

import java.util.List;

public class LoaderRecords
		extends AsyncTaskLoader<List<Diagnosis>> {

	private List<Diagnosis> data;

	private Patient patient;

	public LoaderRecords (Context context, Patient patient) {
		super(context);
		this.patient = patient;
	}

	@Override
	public void onCanceled (List<Diagnosis> data) {
		super.onCanceled(data);
		if (data != null) {
			data.clear();
		}
	}

	@Override
	public List<Diagnosis> loadInBackground () {
		if (patient.getId() > 0) {
			data = Diagnosis.getRecordsOfPatient(patient);
		} else {
			data = Diagnosis.getRecords(null, null, null, Diagnosis._DATETIME + " DESC");
		}
		return data;
	}

	@Override
	public void deliverResult (List<Diagnosis> data) {
		if (isReset()) {
			if (data != null) {
				data.clear();
			}
		}
		this.data = data;
		if (isStarted()) {
			super.deliverResult(data);
		}
	}

	@Override
	public void onStartLoading () {
		if (data != null) {
			deliverResult(data);
		}
		if (takeContentChanged() || data == null) {
			forceLoad();
		}
	}

	@Override
	public void onStopLoading () {
		cancelLoad();
	}

	@Override
	public void onReset () {
		super.onReset();
		onStopLoading();
		if (data != null) {
			data.clear();
		}
	}

}