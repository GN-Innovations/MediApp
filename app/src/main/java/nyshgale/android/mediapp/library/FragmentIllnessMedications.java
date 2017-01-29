package nyshgale.android.mediapp.library;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import nyshgale.android.mediapp.R;
import nyshgale.android.mediapp.custom.Adapter;
import nyshgale.android.mediapp.database.Medication;

import java.util.ArrayList;
import java.util.List;

/**
 * Layout for Illness' Medications
 */
public class FragmentIllnessMedications
		extends Fragment {

	private List<MedicationView> medicationViews;

	private LayoutInflater inflater;

	private ViewGroup container;

	private LinearLayout linearLayout;

	{
		medicationViews = new ArrayList<>();
	}

	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View layout = inflater.inflate(R.layout.fragment_library_illness_medications, container);
		this.inflater = inflater;
		this.container = container;
		this.linearLayout = (LinearLayout) layout.findViewById(R.id.linearlayout_medications__fragment_library_illness_medications);
		ImageButton addButton = (ImageButton) layout.findViewById(R.id.imagebutton_add__fragment_library_illness_medications);
		addButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick (View v) {
				for (MedicationView medicationView : medicationViews) {
					if (medicationView.getMedication() == null) {
						return;
					}
				}
				addMedication(new Medication());
			}
		});
		return layout;
	}

	public void addMedication (Medication medication) {
		MedicationView medicationView = new MedicationView(medication);
		medicationViews.add(medicationView);
		linearLayout.addView(medicationView.getView());
	}

	public List<Medication> getMedications () {
		List<Medication> medications = new ArrayList<>();
		for (MedicationView medicationView : medicationViews) {
			if (medicationView.getMedication() != null) {
				Medication medication = medicationView.getMedication();
				if (medication.getId() <= 0) {
					medication.save();
				}
				boolean duplicate = (medications.contains(medication));
				if (!duplicate) {
					medications.add(medication);
				} else {
					removeMedication(medicationView);
				}
			}
		}
		return medications;
	}

	public void setMedications (List<Medication> medications) {
		if (!medications.isEmpty()) {
			for (MedicationView medicationView : medicationViews) {
				removeMedication(medicationView);
			}
			for (Medication medication : medications) {
				addMedication(medication);
			}
		}
	}

	public void removeMedication (MedicationView medicationView) {
		linearLayout.removeView(medicationView.getView());
		medicationViews.remove(medicationView);
	}

	private class MedicationView
			implements View.OnClickListener {

		private View view;

		private AutoCompleteTextView nameView;

		public MedicationView (Medication medication) {
			view = inflater.inflate(R.layout.edit_library_medication, container);
			nameView = (AutoCompleteTextView) view.findViewById(R.id.autotext_name__edit_library_medication);
			nameView.setAdapter(new Adapter(getActivity(), Medication.TYPE));
			ImageButton removeView = (ImageButton) view.findViewById(R.id.imagebutton_remove__edit_library_medication);
			removeView.setOnClickListener(this);
			setMedication(medication);
		}

		public View getView () {
			return view;
		}

		public Medication getMedication () {
			if (nameView.getText().length() > 0) {
				String name = nameView.getText().toString();
				return new Medication(name);
			} else {
				return null;
			}
		}

		private void setMedication (Medication medication) {
			if (medication != null) {
				nameView.setText(medication.getName());
			}
		}

		@Override
		public void onClick (View v) {
			if (getMedication() == null || getMedication().getId() == 0) {
				removeMedication(this);
			}
		}

	}

}
