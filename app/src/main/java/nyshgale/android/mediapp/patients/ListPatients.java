package nyshgale.android.mediapp.patients;

import android.app.Activity;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import nyshgale.android.mediapp.R;
import nyshgale.android.mediapp.custom.DialogInfo;
import nyshgale.android.mediapp.custom.Event;
import nyshgale.android.mediapp.database.Patient;

/**
 * ListFragment for Patients
 */
public class ListPatients
		extends ListFragment
		implements LoaderManager.LoaderCallbacks<Cursor>,
		           AdapterView.OnItemLongClickListener,
		           AdapterView.OnItemClickListener {

	// ****************************************************************************************** //
	// ****************************** PRIVATE VARIABLES ***************************************** //
	// ****************************************************************************************** //

	public Loader<Cursor> loader;

	/**
	 * callback that will handle selected event
	 */
	private Event.OnItemLongClickedListener callback;

	/**
	 * adapter that will display list of symptoms
	 */
	private SimpleCursorAdapter adapter;

	// ****************************************************************************************** //
	// ****************************** OVERRIDE METHODS ****************************************** //
	// ****************************************************************************************** //

	/**
	 * Gets the activity that this fragment is attached. It will also check if
	 * the activity implements the callback.
	 *
	 * @param activity that this fragment is attached
	 */
	@Override
	public void onAttach (Activity activity) {
		super.onAttach(activity);
		try {
			callback = (Event.OnItemLongClickedListener) activity;
		} catch (Exception ex) {
			Log.e(this.toString(), ex.getMessage());
		}
	}

	/**
	 * Setting up all required initializations
	 *
	 * @param savedInstanceState useless
	 */
	@Override
	public void onActivityCreated (Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setEmptyText(getString(R.string.prompt_no_records));
		adapter = new SimpleCursorAdapter(getActivity(), R.layout.listitem_patients, null, new String[] {Patient._CODE, Patient._LASTNAME, Patient._FIRSTNAME}, new int[] {R.id.textview_code__listitem_patients, R.id.textview_lastname__listitem_patients, R.id.textview_firstname__listitem_patients}, 0);
		setListAdapter(adapter);
		setListShown(false);
		getListView().setOnItemLongClickListener(this);
		getListView().setOnItemClickListener(this);
		getListView().setChoiceMode(ListView.CHOICE_MODE_NONE);
		getLoaderManager().initLoader(0, null, this);
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
	public Loader<Cursor> onCreateLoader (int id, Bundle args) {
		return new nyshgale.android.mediapp.custom.Loader(getActivity(), Patient.TYPE);
	}

	/**
	 * When the loader is done loading the data, the adapter will get the data
	 * from cursor. This fragment will then show the list if not empty
	 *
	 * @param loader useless
	 * @param data cursor
	 */
	@Override
	public void onLoadFinished (Loader<Cursor> loader, Cursor data) {
		this.loader = loader;
		adapter.swapCursor(data);
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
	public void onLoaderReset (Loader<Cursor> loader) {
		adapter.swapCursor(null);
	}

	/**
	 * Called when the user clicked any item on the list
	 *
	 * @param parent useless
	 * @param view useless
	 * @param position of the item in the list
	 * @param id of the item from the database
	 */
	@Override
	public boolean onItemLongClick (AdapterView<?> parent, View view, int position, long id) {
		if (callback != null) {
			callback.onItemLongClicked(new Patient(id));
		}
		return true;
	}

	/**
	 * Called when the user clicked any item on the list
	 *
	 * @param parent useless
	 * @param view useless
	 * @param position of the item in the list
	 * @param id of the item from the database
	 */
	@Override
	public void onItemClick (AdapterView<?> parent, View view, int position, long id) {
		DialogInfo.create(new Patient(id)).show(getActivity().getFragmentManager(), null);
	}

}
