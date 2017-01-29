package nyshgale.android.mediapp.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Illness
		extends Interface {

	// ****************************************************************************************** //
	// ****************************** CONSTANTS ************************************************* //
	// ****************************************************************************************** //

	public final static String _NAME = "illness_name";

	public final static String _DESCRIPTION = "illness_description";

	public final static String _REMEDIES = "illness_remedies";

	public final static String _NOTES = "illness_notes";

	public final static String TYPE = "nyshgale.android.mediapp.database.library.illness";

	protected final static String _TABLENAME = "illnesses";

	private final static String TAG = "Library.Illness";

	// ****************************************************************************************** //
	// ****************************** PRIVATE PROPERTIES **************************************** //
	// ****************************************************************************************** //

	private boolean saved;

	private boolean deleted;

	private long id;

	private String name;

	private String description;

	private List<Symptom> symptoms;

	private List<Cause> causes;

	private List<Medication> medications;

	private String remedies;

	private String notes;

	// ****************************************************************************************** //
	// ****************************** PROPERTIES INITIALIZATION ********************************* //
	// ****************************************************************************************** //

	{
		saved = false;
		deleted = false;
		id = 0;
		name = "";
		description = "";
		symptoms = new ArrayList<>();
		causes = new ArrayList<>();
		medications = new ArrayList<>();
		remedies = "";
		notes = "";
	}

	// ****************************************************************************************** //
	// ****************************** CONSTRUCTORS ********************************************** //
	// ****************************************************************************************** //

	public Illness () {
		super();
	}

	public Illness (long id) {
		if (id > 0) {
			this.id = id;
			getById();
		}
	}

	public Illness (String name) {
		if (name != null) {
			this.name = name;
			getByUnique();
		}
	}

	// ****************************************************************************************** //
	// ****************************** STATIC METHODS ******************************************** //
	// ****************************************************************************************** //

	public static Cursor getCursor () {
		Cursor cursor = null;
		try {
			SQLiteDatabase database = ENGINE.start(ENGINE.READ, LIBRARY.DB);
			cursor = database.query(_TABLENAME, new String[] {_ID, _NAME, _DESCRIPTION, _REMEDIES, _NOTES}, null, null, null, null, _NAME);
		} catch (Exception ex) {
			Log.e(TAG, "getCursor: " + ex.getMessage());
		}
		return cursor;
	}

	public static List<Illness> getRecords (String selection, String[] selectionArgs, String groupBy, String orderBy) {
		SQLiteDatabase database = null;
		Cursor cursor = null;
		List<Illness> illnesses = new ArrayList<>();
		try {
			database = ENGINE.start(ENGINE.READ, LIBRARY.DB);
			cursor = database.query(_TABLENAME, new String[] {_ID, _NAME, _DESCRIPTION, _REMEDIES, _NOTES}, selection, selectionArgs, groupBy, null, orderBy);
			if (cursor != null && cursor.moveToFirst()) {
				do {
					Illness illness = new Illness();
					illness.id = cursor.getLong(cursor.getColumnIndex(_ID));
					illness.name = cursor.getString(cursor.getColumnIndex(_NAME));
					illnesses.add(illness);
				} while (cursor.moveToNext());
			}
		} catch (Exception ex) {
			Log.e(TAG, "getRecords: " + ex.getMessage());
		} finally {
			ENGINE.stop(database, cursor);
		}
		return illnesses;
	}

	public static List<Illness> getRecordsBySymptoms (List<Symptom> symptoms) {
		SQLiteDatabase database = null;
		Cursor cursor = null;
		List<Illness> illnesses = new ArrayList<>();
		try {
			if (symptoms != null && !symptoms.isEmpty()) {
				database = ENGINE.start(ENGINE.READ, LIBRARY.DB);
				String symptomIDs = "0";
				for (Symptom symptom : symptoms) {
					symptomIDs += ", " + symptom.getId();
				}
				cursor = database.query(Symptoms._TABLENAME, new String[] {Symptoms._ILLNESS}, Symptoms._SYMPTOM + " IN (" + symptomIDs + ")", null, Symptoms._ILLNESS, null, null);
				if (cursor != null && cursor.moveToFirst()) {
					do {
						Illness illness = new Illness(cursor.getLong(cursor.getColumnIndex(Symptoms._ILLNESS)));
						illnesses.add(illness);
					} while (cursor.moveToNext());
				}
			}
		} catch (Exception ex) {
			Log.e(TAG, "getRecordsBySymptoms: " + ex.getMessage());
		} finally {
			ENGINE.stop(database, cursor);
		}
		return illnesses;
	}

	// ****************************************************************************************** //
	// ****************************** GETTER AND SETTER ***************************************** //
	// ****************************************************************************************** //

	public boolean isSaved () {
		return saved;
	}

	public boolean isDeleted () {
		return deleted;
	}

	public long getId () {
		return id;
	}

	public void setId (long id) {
		this.id = id;
	}

	public String getName () {
		return name;
	}

	public void setName (String name) {
		this.name = name;
	}

	public String getDescription () {
		return description;
	}

	public void setDescription (String description) {
		this.description = description;
	}

	public List<Symptom> getSymptoms () {
		if (symptoms.isEmpty()) {
			symptoms = Symptoms.getRecords(this.id);
		}
		return symptoms;
	}

	public void setSymptoms (List<Symptom> symptoms) {
		this.symptoms = symptoms;
	}

	public List<Cause> getCauses () {
		if (causes.isEmpty()) {
			causes = Causes.getRecords(this.id);
		}
		return causes;
	}

	public void setCauses (List<Cause> causes) {
		this.causes = causes;
	}

	public List<Medication> getMedications () {
		if (medications.isEmpty()) {
			medications = Medications.getRecords(this.id);
		}
		return medications;
	}

	public void setMedications (List<Medication> medications) {
		this.medications = medications;
	}

	public String getRemedies () {
		return remedies;
	}

	public void setRemedies (String remedies) {
		this.remedies = remedies;
	}

	public String getNotes () {
		return notes;
	}

	public void setNotes (String notes) {
		this.notes = notes;
	}

	// ****************************************************************************************** //
	// ****************************** PUBLIC METHODS ******************************************** //
	// ****************************************************************************************** //

	@Override
	public boolean equals (Object object) {
		boolean result = false;
		if (object != null && object instanceof Illness) {
			Illness illness = (Illness) object;
			result = (illness.id == this.id) &
					         (illness.name.equals(this.name)) &
					         (illness.description.equals(this.description)) &
					         (illness.remedies.equals(this.remedies)) &
					         (illness.notes.equals(this.notes)) &
					         (illness.symptoms.equals(this.symptoms)) &
					         (illness.medications.equals(this.medications));
		}
		return result;
	}

	@Override
	public String toString () {
		return TYPE;
	}

	@Override
	public void save () {
		boolean valid = !this.name.isEmpty();
		if (valid) {
			if (this.id > 0) {
				this.saved = update();
			} else {
				this.saved = insert();
			}
			Symptoms.save(this.id, this.symptoms);
			Medications.save(this.id, this.medications);
			Causes.save(this.id, this.causes);
		}
	}

	@Override
	public void delete () {
		SQLiteDatabase database = null;
		try {
			Symptoms.delete(this.id);
			Causes.delete(this.id);
			Medications.delete(this.id);
			database = ENGINE.start(ENGINE.WRITE, LIBRARY.DB);
			deleted = (database.delete(_TABLENAME, _ID + " = ?", new String[] {Long.toString(this.id)}) > 0);
		} catch (Exception ex) {
			Log.e(TAG, "delete: " + ex.getMessage());
		} finally {
			ENGINE.stop(database);
		}
	}

	// ****************************************************************************************** //
	// ****************************** PROTECTED METHODS ***************************************** //
	// ****************************************************************************************** //

	@Override
	protected boolean insert () {
		SQLiteDatabase database = null;
		boolean result = false;
		try {
			database = ENGINE.start(ENGINE.WRITE, LIBRARY.DB);
			this.id = database.insert(_TABLENAME, null, putValues());
			result = (id > 0);
		} catch (Exception ex) {
			Log.e(TAG, "insert: " + ex.getMessage());
		} finally {
			ENGINE.stop(database);
		}
		return result;
	}

	@Override
	protected boolean update () {
		SQLiteDatabase database = null;
		boolean result = false;
		try {
			database = ENGINE.start(ENGINE.WRITE, LIBRARY.DB);
			result = (database.update(_TABLENAME, putValues(), _ID + " = ?", new String[] {Long.toString(this.id)}) > 0);
		} catch (Exception ex) {
			Log.e(TAG, "update: " + ex.getMessage());
		} finally {
			ENGINE.stop(database);
		}
		return result;
	}

	@Override
	protected void getById () {
		SQLiteDatabase database = null;
		Cursor cursor = null;
		try {
			database = ENGINE.start(ENGINE.READ, LIBRARY.DB);
			cursor = database.query(_TABLENAME, getAllColumns(), _ID + " = ?", new String[] {Long.toString(this.id)}, null, null, null);
			if (cursor != null && cursor.moveToFirst()) {
				getFromCursor(cursor);
			}
		} catch (Exception ex) {
			Log.e(TAG, "getById: " + ex.getMessage());
		} finally {
			ENGINE.stop(database, cursor);
		}
	}

	@Override
	protected void getByUnique () {
		SQLiteDatabase database = null;
		Cursor cursor = null;
		try {
			database = ENGINE.start(ENGINE.READ, LIBRARY.DB);
			cursor = database.query(_TABLENAME, getAllColumns(), _NAME + " = ?", new String[] {this.name}, null, null, null);
			if (cursor != null && cursor.moveToFirst()) {
				getFromCursor(cursor);
			}
		} catch (Exception ex) {
			Log.e(TAG, "getByUnique: " + ex.getMessage());
		} finally {
			ENGINE.stop(database, cursor);
		}
	}


	@Override
	protected void getFromCursor (Cursor cursor) {
		this.id = cursor.getLong(cursor.getColumnIndex(_ID));
		this.name = cursor.getString(cursor.getColumnIndex(_NAME));
		this.description = cursor.getString(cursor.getColumnIndex(_DESCRIPTION));
		this.remedies = cursor.getString(cursor.getColumnIndex(_REMEDIES));
		this.notes = cursor.getString(cursor.getColumnIndex(_NOTES));
	}

	@Override
	protected ContentValues putValues () {
		ContentValues values = new ContentValues();
		if (this.id > 0) {
			values.put(_ID, this.id);
		}
		values.put(_NAME, this.name);
		values.put(_DESCRIPTION, this.description);
		values.put(_REMEDIES, this.remedies);
		values.put(_NOTES, this.notes);
		return values;
	}

	@Override
	protected String[] getAllColumns () {
		return new String[] {_ID, _NAME, _DESCRIPTION, _REMEDIES, _NOTES};
	}

	// ****************************************************************************************** //
	// ****************************** SYMPTOMS ************************************************** //
	// ****************************************************************************************** //

	protected static class Symptoms
			implements BaseColumns {

		protected final static String _TABLENAME = "illness_symptoms";

		protected final static String _ILLNESS = "illness_id";

		protected final static String _SYMPTOM = "symptom_id";

		private final static String TAG = "Illness.Symptoms";

		protected static List<Symptom> getRecords (long illness_id) {
			SQLiteDatabase database = null;
			Cursor cursor = null;
			List<Symptom> symptoms = new ArrayList<>();
			try {
				database = ENGINE.start(ENGINE.READ, LIBRARY.DB);
				cursor = database.query(_TABLENAME, new String[] {_ILLNESS, _SYMPTOM}, _ILLNESS + " = ?", new String[] {Long.toString(illness_id)}, null, null, null);
				if (cursor != null && cursor.moveToFirst()) {
					do {
						Symptom symptom = new Symptom(cursor.getLong(cursor.getColumnIndex(_SYMPTOM)));
						symptoms.add(symptom);
					} while (cursor.moveToNext());
				}
			} catch (Exception ex) {
				Log.e(TAG, "getRecords: " + ex.getMessage());
			} finally {
				ENGINE.stop(database, cursor);
			}
			return symptoms;
		}

		protected static void save (long illness_id, List<Symptom> symptoms) {
			SQLiteDatabase database = null;
			ContentValues values = new ContentValues();
			try {
				delete(illness_id);
				database = ENGINE.start(ENGINE.WRITE, LIBRARY.DB);
				if (symptoms != null && !symptoms.isEmpty()) {
					for (Symptom symptom : symptoms) {
						values.clear();
						values.put(_ILLNESS, illness_id);
						values.put(_SYMPTOM, symptom.getId());
						database.insert(_TABLENAME, null, values);
					}
				}
			} catch (Exception ex) {
				Log.e(TAG, "save: " + ex.getMessage());
			} finally {
				ENGINE.stop(database);
			}
		}

		protected static void delete (long illness_id) {
			SQLiteDatabase database = null;
			try {
				database = ENGINE.start(ENGINE.WRITE, LIBRARY.DB);
				database.delete(_TABLENAME, _ILLNESS + " = ?", new String[] {Long.toString(illness_id)});
			} catch (Exception ex) {
				Log.e(TAG, "delete: " + ex.getMessage());
			} finally {
				ENGINE.stop(database);
			}
		}

	}

	// ****************************************************************************************** //
	// ****************************** CAUSES **************************************************** //
	// ****************************************************************************************** //

	protected static class Causes
			implements BaseColumns {

		protected final static String _TABLENAME = "illness_causes";

		protected final static String _ILLNESS = "illness_id";

		protected final static String _CAUSE = "cause_id";

		private final static String TAG = "Illness.Causes";

		protected static List<Cause> getRecords (long illness_id) {
			SQLiteDatabase database = null;
			Cursor cursor = null;
			List<Cause> causes = new ArrayList<>();
			try {
				database = ENGINE.start(ENGINE.READ, LIBRARY.DB);
				cursor = database.query(_TABLENAME, new String[] {_ILLNESS, _CAUSE}, _ILLNESS + " = ?", new String[] {Long.toString(illness_id)}, null, null, null);
				if (cursor != null && cursor.moveToFirst()) {
					do {
						Cause cause = new Cause(cursor.getLong(cursor.getColumnIndex(_CAUSE)));
						causes.add(cause);
					} while (cursor.moveToNext());
				}
			} catch (Exception ex) {
				Log.e(TAG, "getRecords: " + ex.getMessage());
			} finally {
				ENGINE.stop(database, cursor);
			}
			return causes;
		}

		protected static void save (long illness_id, List<Cause> causes) {
			SQLiteDatabase database = null;
			ContentValues values = new ContentValues();
			try {
				delete(illness_id);
				database = ENGINE.start(ENGINE.WRITE, LIBRARY.DB);
				if (causes != null && !causes.isEmpty()) {
					for (Cause cause : causes) {
						values.clear();
						values.put(_ILLNESS, illness_id);
						values.put(_CAUSE, cause.getId());
						database.insert(_TABLENAME, null, values);
					}
				}
			} catch (Exception ex) {
				Log.e(TAG, "save: " + ex.getMessage());
			} finally {
				ENGINE.stop(database);
			}
		}

		protected static void delete (long illness_id) {
			SQLiteDatabase database = null;
			try {
				database = ENGINE.start(ENGINE.WRITE, LIBRARY.DB);
				database.delete(_TABLENAME, _ILLNESS + " = ?", new String[] {Long.toString(illness_id)});
			} catch (Exception ex) {
				Log.e(TAG, "delete: " + ex.getMessage());
			} finally {
				ENGINE.stop(database);
			}
		}

	}

	// ****************************************************************************************** //
	// ****************************** MEDICATIONS *********************************************** //
	// ****************************************************************************************** //

	protected static class Medications
			implements BaseColumns {

		protected final static String _ILLNESS = "illness_id";

		protected final static String _MEDICATION = "medication_id";

		protected final static String _TABLENAME = "illness_medications";

		private final static String TAG = "Illness.Medications";

		protected static List<Medication> getRecords (long illness_id) {
			SQLiteDatabase database = null;
			Cursor cursor = null;
			List<Medication> medications = new ArrayList<>();
			try {
				database = ENGINE.start(ENGINE.READ, LIBRARY.DB);
				cursor = database.query(_TABLENAME, new String[] {_ILLNESS, _MEDICATION}, _ILLNESS + " = ?", new String[] {Long.toString(illness_id)}, null, null, null);
				if (cursor != null && cursor.moveToFirst()) {
					do {
						Medication medication = new Medication(cursor.getLong(cursor.getColumnIndex(_MEDICATION)));
						medications.add(medication);
					} while (cursor.moveToNext());
				}
			} catch (Exception ex) {
				Log.e(TAG, "getRecords: " + ex.getMessage());
			} finally {
				ENGINE.stop(database, cursor);
			}
			return medications;
		}

		protected static void save (long illness_id, List<Medication> medications) {
			SQLiteDatabase database = null;
			ContentValues values = new ContentValues();
			try {
				delete(illness_id);
				database = ENGINE.start(ENGINE.WRITE, LIBRARY.DB);
				if (medications != null && !medications.isEmpty()) {
					for (Medication medication : medications) {
						values.clear();
						values.put(_ILLNESS, illness_id);
						values.put(_MEDICATION, medication.getId());
						database.insert(_TABLENAME, null, values);
					}
				}
			} catch (Exception ex) {
				Log.e(TAG, "save: " + ex.getMessage());
			} finally {
				ENGINE.stop(database);
			}
		}

		protected static void delete (long illness_id) {
			SQLiteDatabase database = null;
			try {
				database = ENGINE.start(ENGINE.WRITE, LIBRARY.DB);
				database.delete(_TABLENAME, _ILLNESS + " = ?", new String[] {Long.toString(illness_id)});
			} catch (Exception ex) {
				Log.e(TAG, "delete: " + ex.getMessage());
			} finally {
				ENGINE.stop(database);
			}
		}

	}

}
