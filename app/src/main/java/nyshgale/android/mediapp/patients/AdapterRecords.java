package nyshgale.android.mediapp.patients;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import nyshgale.android.mediapp.R;
import nyshgale.android.mediapp.database.Diagnosis;
import nyshgale.android.mediapp.database.Illness;
import nyshgale.android.mediapp.database.Patient;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AdapterRecords
		extends ArrayAdapter<Diagnosis>
		implements View.OnClickListener {

	private LayoutInflater inflater;

	public AdapterRecords (Context context) {
		super(context, R.layout.listitem_records);
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void setData (List<Diagnosis> data) {
		clear();
		if (data != null) {
			addAll(data);
		}
	}

	@Override
	public View getView (int position, View convertView, ViewGroup parent) {
		View view;
		if (convertView == null) {
			view = inflater.inflate(R.layout.listitem_records, parent, false);
		} else {
			view = convertView;
		}
		Diagnosis item = getItem(position);
		Patient patient = new Patient(item.getPatient());
		Illness illness = new Illness(item.getIllness());
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy MMM dd - hh:mm:ss aa", Locale.getDefault());
		((TextView) view.findViewById(R.id.textview_patient__listitem_records)).setText(patient.getFirstName() + " " + patient.getLastName());
		((TextView) view.findViewById(R.id.textview_illness__listitem_records)).setText(illness.getName());
		((TextView) view.findViewById(R.id.textview_datetime__listitem_records)).setText(dateFormat.format(new Date(item.getDatetime())));
		((TextView) view.findViewById(R.id.hidden_id__listitem_records)).setText(Long.toString(item.getId()));
		view.setOnClickListener(this);
		return view;
	}

	@Override
	public void onClick (View view) {
		long id = Long.parseLong(((TextView) view.findViewById(R.id.hidden_id__listitem_records)).getText().toString());
		final Intent activityDiagnosis = new Intent(getContext(), nyshgale.android.mediapp.patients.ActivityPatientDiagnosis.class);
		activityDiagnosis.putExtra(Diagnosis._ID, id);
		getContext().startActivity(activityDiagnosis);
	}

}
