package nyshgale.android.mediapp.library;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;
import nyshgale.android.mediapp.R;
import nyshgale.android.mediapp.custom.Adapter;
import nyshgale.android.mediapp.database.Illness;

/**
 * Activity for Illness
 */
public class ActivityIllness
		extends Activity {

	private Illness illness;

	private AutoCompleteTextView nameView;

	private EditText descriptionView;

	private FragmentIllnessSymptoms symptomsLayout;

	private FragmentIllnessCauses causesLayout;

	private FragmentIllnessMedications medicationsLayout;

	private EditText healthtipsView;

	private EditText notesView;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_library_illness);
		nameView = (AutoCompleteTextView) findViewById(R.id.autotext_name__edit_library_illness);
		descriptionView = (EditText) findViewById(R.id.edittext_description__edit_library_illness);
		symptomsLayout = (FragmentIllnessSymptoms) getFragmentManager().findFragmentById(R.id.fragment_symptoms__edit_library_illness);
		medicationsLayout = (FragmentIllnessMedications) getFragmentManager().findFragmentById(R.id.fragment_medications__edit_library_illness);
		causesLayout = (FragmentIllnessCauses) getFragmentManager().findFragmentById(R.id.fragment_causes__edit_library_illness);
		healthtipsView = (EditText) findViewById(R.id.edittext_remedies__edit_library_illness);
		notesView = (EditText) findViewById(R.id.edittext_notes__edit_library_illness);
	}

	@Override
	protected void onStart () {
		super.onStart();
		nameView.setAdapter(new Adapter(this, Illness.TYPE));
		long id = getIntent().getLongExtra(Illness._ID, 0);
		if (id > 0) {
			illness = new Illness(id);
		}
		if (illness != null) {
			setTitle(illness.getName() + " Illness");
			nameView.setText(illness.getName());
			descriptionView.setText(illness.getDescription());
			symptomsLayout.setSymptoms(illness.getSymptoms());
			causesLayout.setCauses(illness.getCauses());
			medicationsLayout.setMedications(illness.getMedications());
			healthtipsView.setText(illness.getRemedies());
			notesView.setText(illness.getNotes());
		}
	}

	@Override
	public boolean onCreateOptionsMenu (Menu menu) {
		getMenuInflater().inflate(R.menu.item_edit, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected (MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_item_save:
				save();
				return true;
			case R.id.action_item_cancel:
				final Intent libraryIntent = new Intent(this, ActivityMain.class);
				startActivity(libraryIntent);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	private void save () {
		if (illness == null) {
			illness = new Illness();
		}
		illness.setName(nameView.getText().toString());
		illness.setDescription(descriptionView.getText().toString());
		illness.setSymptoms(symptomsLayout.getSymptoms());
		illness.setCauses(causesLayout.getCauses());
		illness.setMedications(medicationsLayout.getMedications());
		illness.setRemedies(healthtipsView.getText().toString());
		illness.setNotes(notesView.getText().toString());
		illness.save();
		if (illness.isSaved()) {
			Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
			finish();
		} else {
			Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
		}
	}

}
