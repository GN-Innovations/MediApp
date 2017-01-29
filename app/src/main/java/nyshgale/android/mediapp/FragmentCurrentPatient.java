package nyshgale.android.mediapp;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import nyshgale.android.mediapp.custom.DialogChangePatient;
import nyshgale.android.mediapp.database.Patient;
import nyshgale.android.mediapp.database.Preferences;

/**
 * Diagnose Fragment
 */
public class FragmentCurrentPatient
		extends Fragment {

	private TextView codeView;

	private TextView nameView;

	private TextView ageView;

	private TextView genderView;

	private Patient patient;

	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View layout = inflater.inflate(R.layout.fragment_current_patient, container);
		codeView = (TextView) layout.findViewById(R.id.textview_code__fragment_current_patient);
		nameView = (TextView) layout.findViewById(R.id.textview_name__fragment_current_patient);
		ageView = (TextView) layout.findViewById(R.id.textview_age__fragment_current_patient);
		genderView = (TextView) layout.findViewById(R.id.textview_gender__fragment_current_patient);
		ImageButton changeButton = (ImageButton) layout.findViewById(R.id.imagebutton_change__fragment_current_patient);
		changeButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick (View v) {
				DialogChangePatient.create().show(getActivity().getFragmentManager(), null);
			}
		});
		return layout;
	}

	@Override
	public void onStart () {
		super.onStart();
		loadPatient();
	}

	public void loadPatient () {
		patient = new Patient(Preferences.get().getPatientCode());
		codeView.setText("Code:" + patient.getCode());
		nameView.setText(patient.getFirstName() + " " + patient.getLastName());
		ageView.setText("Age: " + patient.getAge());
		genderView.setText("Gender: " + patient.getGender());
	}

}
