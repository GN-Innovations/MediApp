package nyshgale.android.mediapp.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Diagnosis
		extends Interface {

	// ****************************************************************************************** //
	// ****************************** CONSTANTS ************************************************* //
	// ****************************************************************************************** //

	public final static String _DATETIME = "diagnosis_datetime";

	public final static String _PATIENT = "dignosis_patient";

	public final static String _ILLNESS = "dignosis_illness";

	public final static String TYPE = "nyshgale.android.mediapp.database.patients.diagnosis";

	protected final static String _TABLENAME = "diagnosis";

	private final static String TAG = "Patients.Diagnosis";

	// ****************************************************************************************** //
	// ****************************** PRIVATE PROPERTIES **************************************** //
	// ****************************************************************************************** //

	private boolean saved;

	private boolean deleted;

	private long id;

	private long datetime;

	private long patient;

	private long illness;

	private List<Symptom> symptoms;

	private List<Cause> causes;

	// ****************************************************************************************** //
	// ****************************** PROPERTIES INITIALIZATION ********************************* //
	// ****************************************************************************************** //

	{
		saved = false;
		deleted = false;
		id = 0;
		datetime = 0;
		patient = 0;
		illness = 0;
		symptoms = new ArrayList<>();
		causes = new ArrayList<>();
	}

	// ****************************************************************************************** //
	// ****************************** CONSTRUCTORS ********************************************** //
	// ****************************************************************************************** //

	public Diagnosis () {
		super();
	}

	public Diagnosis (long id) {
		if (id > 0) {
			this.id = id;
			getById();
		}
	}

	public Diagnosis (String millis) {
		if (millis != null) {
			this.datetime = Long.parseLong(millis);
			getByUnique();
		}
	}

	// ****************************************************************************************** //
	// ****************************** STATIC METHODS ******************************************** //
	// ****************************************************************************************** //

	public static List<Diagnosis> getRecords (String selection, String[] selectionArgs, String groupBy, String orderBy) {
		SQLiteDatabase database = null;
		Cursor cursor = null;
		List<Diagnosis> diagnoses = new ArrayList<>();
		try {
			database = ENGINE.start(ENGINE.READ, PATIENTS.DB);
			cursor = database.query(_TABLENAME, new String[] {_ID, _DATETIME, _PATIENT, _ILLNESS}, selection, selectionArgs, groupBy, null, orderBy);
			if (cursor != null && cursor.moveToFirst()) {
				do {
					Diagnosis diagnosis = new Diagnosis();
					diagnosis.id = cursor.getLong(cursor.getColumnIndex(_ID));
					diagnosis.datetime = cursor.getLong(cursor.getColumnIndex(_DATETIME));
					diagnosis.patient = cursor.getLong(cursor.getColumnIndex(_PATIENT));
					diagnosis.illness = cursor.getLong(cursor.getColumnIndex(_ILLNESS));
					diagnoses.add(diagnosis);
				} while (cursor.moveToNext());
			}
		} catch (Exception ex) {
			Log.e(TAG, "getRecords: " + ex.getMessage());
		} finally {
			ENGINE.stop(database, cursor);
		}
		return diagnoses;
	}

	public static List<Diagnosis> getRecordsOfPatient (Patient patient) {
		SQLiteDatabase database = null;
		Cursor cursor = null;
		List<Diagnosis> diagnoses = new ArrayList<>();
		try {
			if (patient != null && patient.getId() > 0) {
				database = ENGINE.start(ENGINE.READ, PATIENTS.DB);
				cursor = database.query(_TABLENAME, new String[] {_ID, _DATETIME, _PATIENT, _ILLNESS}, _PATIENT + " = ?", new String[] {Long.toString(patient.getId())}, null, null, _DATETIME + " DESC");
				if (cursor != null && cursor.moveToFirst()) {
					do {
						Diagnosis diagnosis = new Diagnosis();
						diagnosis.id = cursor.getLong(cursor.getColumnIndex(_ID));
						diagnosis.datetime = cursor.getLong(cursor.getColumnIndex(_DATETIME));
						diagnosis.patient = cursor.getLong(cursor.getColumnIndex(_PATIENT));
						diagnosis.illness = cursor.getLong(cursor.getColumnIndex(_ILLNESS));
						diagnoses.add(diagnosis);
					} while (cursor.moveToNext());
				}
			}
		} catch (Exception ex) {
			Log.e(TAG, "getRecordsOfPatient: " + ex.getMessage());
		} finally {
			ENGINE.stop(database, cursor);
		}
		return diagnoses;
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

	public long getDatetime () {
		return datetime;
	}

	public void setDatetime (long datetime) {
		this.datetime = datetime;
	}

	public long getPatient () {
		return patient;
	}

	public void setPatient (long patient) {
		this.patient = patient;
	}

	public long getIllness () {
		return illness;
	}

	public void setIllness (long illness) {
		this.illness = illness;
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

	// ****************************************************************************************** //
	// ****************************** PUBLIC METHODS ******************************************** //
	// ****************************************************************************************** //

	@Override
	public boolean equals (Object object) {
		boolean result = false;
		if (object != null && object instanceof Diagnosis) {
			Diagnosis diagnosis = (Diagnosis) object;
			result = (diagnosis.id == this.id) &
					         (diagnosis.datetime == this.datetime) &
					         (diagnosis.patient == this.patient) &
					         (diagnosis.illness == this.illness);
		}
		return result;
	}

	@Override
	public String toString () {
		return TYPE;
	}

	@Override
	public void save () {
		boolean valid = this.patient > 0 & this.illness > 0;
		if (valid) {
			if (this.id > 0) {
				this.saved = update();
			} else {
				this.saved = insert();
			}
			Symptoms.save(this.id, symptoms);
			Causes.save(this.id, causes);
		}
	}

	@Override
	public void delete () {
		SQLiteDatabase database = null;
		try {
			database = ENGINE.start(ENGINE.WRITE, PATIENTS.DB);
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
			database = ENGINE.start(ENGINE.WRITE, PATIENTS.DB);
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
			database = ENGINE.start(ENGINE.WRITE, PATIENTS.DB);
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
			database = ENGINE.start(ENGINE.READ, PATIENTS.DB);
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
			database = ENGINE.start(ENGINE.READ, PATIENTS.DB);
			cursor = database.query(_TABLENAME, getAllColumns(), _DATETIME + " = ?", new String[] {Long.toString(this.datetime)}, null, null, null);
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
		this.datetime = cursor.getLong(cursor.getColumnIndex(_DATETIME));
		this.patient = cursor.getLong(cursor.getColumnIndex(_PATIENT));
		this.illness = cursor.getLong(cursor.getColumnIndex(_ILLNESS));
	}

	@Override
	protected ContentValues putValues () {
		ContentValues values = new ContentValues();
		if (this.id > 0) {
			values.put(_ID, this.id);
		}
		values.put(_DATETIME, this.datetime);
		values.put(_PATIENT, this.patient);
		values.put(_ILLNESS, this.illness);
		return values;
	}

	@Override
	protected String[] getAllColumns () {
		return new String[] {_ID, _DATETIME, _PATIENT, _ILLNESS};
	}

	// ****************************************************************************************** //
	// ****************************** Symptoms ************************************************** //
	// ****************************************************************************************** //

	protected static class Symptoms
			implements BaseColumns {

		protected final static String _DIAGNOSIS = "diagnosis_id";

		protected final static String _SYMPTOM = "symptom_id";

		protected final static String _TABLENAME = "diagnosis_symptoms";

		private final static String TAG = "Diagnosis.Symptoms";

		protected static List<Symptom> getRecords (long diagnosis_id) {
			SQLiteDatabase database = null;
			Cursor cursor = null;
			List<Symptom> symptoms = new ArrayList<>();
			try {
				database = ENGINE.start(ENGINE.READ, PATIENTS.DB);
				cursor = database.query(_TABLENAME, new String[] {_DIAGNOSIS, _SYMPTOM}, _DIAGNOSIS + " = ?", new String[] {Long.toString(diagnosis_id)}, null, null, null);
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

		protected static void save (long diagnosis_id, List<Symptom> symptoms) {
			SQLiteDatabase database = null;
			ContentValues values = new ContentValues();
			try {
				delete(diagnosis_id);
				database = ENGINE.start(ENGINE.WRITE, PATIENTS.DB);
				if (symptoms != null && !symptoms.isEmpty()) {
					for (Symptom symptom : symptoms) {
						values.clear();
						values.put(_DIAGNOSIS, diagnosis_id);
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

		protected static void delete (long diagnosis_id) {
			SQLiteDatabase database = null;
			try {
				database = ENGINE.start(ENGINE.WRITE, PATIENTS.DB);
				database.delete(_TABLENAME, _DIAGNOSIS + " = ?", new String[] {Long.toString(diagnosis_id)});
			} catch (Exception ex) {
				Log.e(TAG, "delete: " + ex.getMessage());
			} finally {
				ENGINE.stop(database);
			}
		}

	}

	// ****************************************************************************************** //
	// ****************************** Causes **************************************************** //
	// ****************************************************************************************** //

	protected static class Causes
			implements BaseColumns {

		protected final static String _DIAGNOSIS = "diagnosis_id";

		protected final static String _CAUSE = "cause_id";

		protected final static String _TABLENAME = "diagnosis_causes";

		private final static String TAG = "Diagnosis.Causes";

		protected static List<Cause> getRecords (long diagnosis_id) {
			SQLiteDatabase database = null;
			Cursor cursor = null;
			List<Cause> causes = new ArrayList<>();
			try {
				database = ENGINE.start(ENGINE.READ, PATIENTS.DB);
				cursor = database.query(_TABLENAME, new String[] {_DIAGNOSIS, _CAUSE}, _DIAGNOSIS + " = ?", new String[] {Long.toString(diagnosis_id)}, null, null, null);
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

		protected static void save (long diagnosis_id, List<Cause> causes) {
			SQLiteDatabase database = null;
			ContentValues values = new ContentValues();
			try {
				delete(diagnosis_id);
				database = ENGINE.start(ENGINE.WRITE, PATIENTS.DB);
				if (causes != null && !causes.isEmpty()) {
					for (Cause cause : causes) {
						values.clear();
						values.put(_DIAGNOSIS, diagnosis_id);
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

		protected static void delete (long diagnosis_id) {
			SQLiteDatabase database = null;
			try {
				database = ENGINE.start(ENGINE.WRITE, PATIENTS.DB);
				database.delete(_TABLENAME, _DIAGNOSIS + " = ?", new String[] {Long.toString(diagnosis_id)});
			} catch (Exception ex) {
				Log.e(TAG, "delete: " + ex.getMessage());
			} finally {
				ENGINE.stop(database);
			}
		}

	}

}
