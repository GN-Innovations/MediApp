package nyshgale.android.mediapp;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import nyshgale.android.mediapp.custom.Adapter;
import nyshgale.android.mediapp.database.Symptom;
import android.widget.Toast;

import java.util.List;

/**
 * Diagnose Fragment
 */
public class FragmentDiagnose
		extends Fragment
		implements OnEditorActionListener {

	private AutoCompleteTextView queryView;

	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View layout = inflater.inflate(R.layout.fragment_diagnose, container);
		queryView = (AutoCompleteTextView) layout.findViewById(R.id.autotext_query__fragment_diagnose);
		queryView.setOnEditorActionListener(this);
		return layout;
	}

	@Override
	public void onStart () {
		super.onStart();
		queryView.setAdapter(new Adapter(getActivity(), Symptom.TYPE));
	}

	@Override
	public boolean onEditorAction (TextView v, int actionId, KeyEvent event) {
		if (actionId == EditorInfo.IME_ACTION_GO) {
			String name = queryView.getText().toString();
			List<Symptom> symptoms = Symptom.getRecords(Symptom._NAME + " = ?", new String[] {name}, null, Symptom._NAME);
			Symptom symptom = (!symptoms.isEmpty()) ? symptoms.get(0) : null;
			if (symptom != null) {
				final Intent diagnosisIntent = new Intent(getActivity(), nyshgale.android.mediapp.ActivityDiagnose.class);
				diagnosisIntent.putExtra(Symptom._ID, symptom.getId());
				startActivity(diagnosisIntent);
			} else {
				Toast.makeText(getActivity(), getString(R.string.prompt_symptom_input), Toast.LENGTH_LONG).show();
			}
			return true;
		} else {
			return false;
		}
	}

}
