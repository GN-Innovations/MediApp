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
import nyshgale.android.mediapp.database.Symptom;

import java.util.ArrayList;
import java.util.List;

/**
 * Layout for Illness' Symptoms
 */
public class FragmentIllnessSymptoms
		extends Fragment {

	private List<SymptomView> symptomViews;

	private LayoutInflater inflater;

	private ViewGroup container;

	private LinearLayout linearLayout;

	{
		symptomViews = new ArrayList<>();
	}

	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View layout = inflater.inflate(R.layout.fragment_library_illness_symptoms, container);
		this.inflater = inflater;
		this.container = container;
		this.linearLayout = (LinearLayout) layout.findViewById(R.id.linearlayout_symptoms__fragment_library_illness_symptoms);
		ImageButton addButton = (ImageButton) layout.findViewById(R.id.imagebutton_add__fragment_library_illness_symptom);
		addButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick (View v) {
				for (SymptomView symptomView : symptomViews) {
					if (symptomView.getSymptom() == null) {
						return;
					}
				}
				Symptom symptom = new Symptom();
				addSymptom(symptom);
			}
		});
		return layout;
	}

	public void addSymptom (Symptom symptom) {
		SymptomView symptomView = new SymptomView(symptom);
		symptomViews.add(symptomView);
		linearLayout.addView(symptomView.getView());
	}

	public List<Symptom> getSymptoms () {
		List<Symptom> symptoms = new ArrayList<>();
		for (SymptomView symptomView : symptomViews) {
			if (symptomView.getSymptom() != null) {
				Symptom symptom = symptomView.getSymptom();
				if (symptom.getId() <= 0) {
					symptom.save();
				}
				boolean duplicate = symptoms.contains(symptom);
				if (!duplicate) {
					symptoms.add(symptom);
				} else {
					removeSymptom(symptomView);
				}
			}
		}
		return symptoms;
	}

	public void setSymptoms (List<Symptom> symptoms) {
		if (!symptoms.isEmpty()) {
			for (SymptomView symptomView : symptomViews) {
				removeSymptom(symptomView);
			}
			for (Symptom symptom : symptoms) {
				addSymptom(symptom);
			}
		}
	}

	public void removeSymptom (SymptomView symptomView) {
		linearLayout.removeView(symptomView.getView());
		symptomViews.remove(symptomView);
	}

	private class SymptomView
			implements View.OnClickListener {

		private View view;

		private AutoCompleteTextView nameView;

		public SymptomView (Symptom symptom) {
			view = inflater.inflate(R.layout.edit_library_symptom, container);
			nameView = (AutoCompleteTextView) view.findViewById(R.id.autotext_name__edit_library_symptom);
			nameView.setAdapter(new Adapter(getActivity(), Symptom.TYPE));
			ImageButton removeView = (ImageButton) view.findViewById(R.id.imagebutton_remove__edit_library_symptom);
			removeView.setOnClickListener(this);
			setSymptom(symptom);
		}

		public View getView () {
			return view;
		}

		public Symptom getSymptom () {
			if (nameView.getText().length() > 0) {
				String name = nameView.getText().toString();
				return new Symptom(name);
			} else {
				return null;
			}
		}

		private void setSymptom (Symptom symptom) {
			if (symptom != null) {
				nameView.setText(symptom.getName());
			}
		}


		@Override
		public void onClick (View v) {
			if (getSymptom() == null || getSymptom().getId() == 0) {
				removeSymptom(this);
			}
		}

	}

}
