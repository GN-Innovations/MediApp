package nyshgale.android.mediapp.custom;

import nyshgale.android.mediapp.database.Patient;

/**
 * Interfaces that would be called in the caller and will automatically trigger in the listener.
 */
public class Event {

	/**
	 * Listener for when an option has been selected
	 */
	public interface OnOptionSelectedListener {

		/**
		 * An option has been selected
		 *
		 * @param option that has been selected
		 */
		void onOptionSelected (String option);
	}

	/**
	 * Listener for when an item has been pressed and hold by the user
	 */
	public interface OnItemLongClickedListener {

		/**
		 * An object has been pressed and hold
		 *
		 * @param object that has been pressed and hold
		 */
		void onItemLongClicked (Object object);
	}

	/**
	 * Listener for when the save button has been clicked
	 */
	public interface OnSaveButtonClickedListener {

		/**
		 * The save button has been clicked
		 *
		 * @param object to be saved
		 */
		void onSaveButtonClicked (Object object);
	}

	/**
	 * Listener for when the delete button has been clicked
	 */
	public interface OnDeleteButtonClickedListener {

		/**
		 * The delete button has been clicked
		 *
		 * @param object to be deleted
		 */
		void onDeleteButtonClicked (Object object);
	}

	/**
	 * Listener for when user answered a diagnose query
	 */
	public interface OnDiagnoseAnsweredListener {

		/**
		 * Diagnose dialog answered
		 *
		 * @param object that's being queried
		 */
		void onAnswered (Object object, boolean answer);
	}

	/**
	 * Listener for when user change patient
	 */
	public interface OnPatientChangedListener {

		/**
		 * User has changed patient
		 *
		 * @param patient that will be set
		 */
		void onPatientChanged (Patient patient);
	}

	/**
	 * Listener for when user sets the birthdate
	 */
	public interface OnDateSetListener {

		/**
		 * Birthdate has been set
		 *
		 * @param date to be set
		 */
		void onDateSet (long millis);
	}

	/**
	 * Listener for when the ok button has been clicked
	 */
	public interface OnOkButtonClickedListener {

		/**
		 * The delete button has been clicked
		 */
		void onOkButtonClicked ();
	}

}
