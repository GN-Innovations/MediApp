package nyshgale.android.mediapp.patients;

import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.widget.ListView;
import nyshgale.android.mediapp.R;
import nyshgale.android.mediapp.database.Diagnosis;
import nyshgale.android.mediapp.database.Patient;

import java.util.List;

/**
 * ListFragment for Patients
 */
public class ListRecords
		extends ListFragment
		implements LoaderManager.LoaderCallbacks<List<Diagnosis>> {

	// ****************************************************************************************** //
	// ****************************** PRIVATE VARIABLES ***************************************** //
	// ****************************************************************************************** //

	/**
	 * adapter that will display list of symptoms
	 */
	private AdapterRecords adapter;

	private Patient patient = new Patient();

	// ****************************************************************************************** //
	// ****************************** OVERRIDE METHODS ****************************************** //
	// ****************************************************************************************** //

	/**
	 * Setting up all required initializations
	 *
	 * @param savedInstanceState useless
	 */
	@Override
	public void onActivityCreated (Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setEmptyText(getString(R.string.prompt_no_records));
		adapter = new AdapterRecords(getActivity());
		setListAdapter(adapter);
		setListShown(false);
		getListView().setChoiceMode(ListView.CHOICE_MODE_NONE);
		getLoaderManager().initLoader(0, null, this);
	}

	public void setPatient (Patient patient) {
		this.patient = patient;
		getLoaderManager().restartLoader(0, null, this);
	}

	// ****************************************************************************************** //
	// ****************************** IMPLEMENTED METHODS *************************************** //
	// ****************************************************************************************** //

	/**
	 * Creates and loads the cursor
	 *
	 * @param id useless
	 * @param args useless
	 *
	 * @return cursor with the data or none at all
	 */
	@Override
	public Loader<List<Diagnosis>> onCreateLoader (int id, Bundle args) {
		return new nyshgale.android.mediapp.patients.LoaderRecords(getActivity(), patient);
	}

	/**
	 * When the loader is done loading the data, the adapter will get the data
	 * from cursor. This fragment will then show the list if not empty
	 *
	 * @param loader useless
	 * @param data cursor
	 */
	@Override
	public void onLoadFinished (Loader<List<Diagnosis>> loader, List<Diagnosis> data) {
		adapter.setData(data);
		if (isResumed()) {
			setListShown(true);
		} else {
			setListShownNoAnimation(true);
		}
	}

	/**
	 * This method will be called when there is interruption in loading the data
	 *
	 * @param loader useless
	 */
	@Override
	public void onLoaderReset (Loader<List<Diagnosis>> loader) {
		adapter.setData(null);
	}

}
