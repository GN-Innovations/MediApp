package nyshgale.android.mediapp.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class Preferences
		extends Interface {

	// ****************************************************************************************** //
	// ****************************** CONSTANTS ************************************************* //
	// ****************************************************************************************** //

	public final static String _PATIENT_CODE = "preferences_patient_code";

	public final static String TYPE = "nyshgale.android.mediapp.database.system.preferences";

	protected final static String _TABLENAME = "preferences";

	private final static String TAG = "System.Preferences";

	// ****************************************************************************************** //
	// ****************************** PRIVATE PROPERTIES **************************************** //
	// ****************************************************************************************** //

	private boolean saved;

	private long id;

	private String patientCode;

	// ****************************************************************************************** //
	// ****************************** PROPERTIES INITIALIZATION ********************************* //
	// ****************************************************************************************** //

	{
		saved = false;
		id = 0;
		patientCode = "";
	}

	// ****************************************************************************************** //
	// ****************************** CONSTRUCTORS ********************************************** //
	// ****************************************************************************************** //

	public Preferences () {
		super();
	}

	public Preferences (long id) {
		if (id > 0) {
			this.id = id;
			getById();
		}
	}

	public Preferences (String patientCode) {
		if (patientCode != null) {
			this.patientCode = patientCode;
			getByUnique();
		}
	}

	// ****************************************************************************************** //
	// ****************************** STATIC METHODS ******************************************** //
	// ****************************************************************************************** //

	public static Preferences get () {
		SQLiteDatabase database = null;
		Cursor cursor = null;
		Preferences preferences = new Preferences();
		try {
			database = ENGINE.start(ENGINE.READ, SYSTEM.DB);
			cursor = database.query(_TABLENAME, new String[] {_ID, _PATIENT_CODE}, null, null, null, null, null);
			if (cursor != null && cursor.moveToFirst()) {
				preferences.id = cursor.getLong(cursor.getColumnIndex(_ID));
				preferences.patientCode = cursor.getString(cursor.getColumnIndex(_PATIENT_CODE));
			}
		} catch (Exception ex) {
			Log.e(TAG, "get: " + ex.getMessage());
		} finally {
			ENGINE.stop(database, cursor);
		}
		return preferences;
	}

	// ****************************************************************************************** //
	// ****************************** GETTER AND SETTER ***************************************** //
	// ****************************************************************************************** //

	public boolean isSaved () {
		return saved;
	}

	public long getId () {
		return id;
	}

	public void setId (long id) {
		this.id = id;
	}

	public String getPatientCode () {
		return patientCode;
	}

	public void setPatientCode (String patientCode) {
		this.patientCode = patientCode;
	}

	// ****************************************************************************************** //
	// ****************************** PUBLIC METHODS ******************************************** //
	// ****************************************************************************************** //

	@Override
	public boolean equals (Object object) {
		boolean result = false;
		if (object != null && object instanceof Preferences) {
			Preferences cause = (Preferences) object;
			result = (cause.id == this.id) & (cause.patientCode.equals(this.patientCode));
		}
		return result;
	}

	@Override
	public String toString () {
		return TYPE;
	}

	@Override
	public void save () {
		boolean valid = (!this.patientCode.isEmpty());
		if (valid) {
			if (this.id > 0) {
				this.saved = update();
			} else {
				this.saved = insert();
			}
		}
	}

	@Override
	public void delete () {
		SQLiteDatabase database = null;
		try {
			database = ENGINE.start(ENGINE.WRITE, SYSTEM.DB);
			database.delete(_TABLENAME, _ID + " = ?", new String[] {Long.toString(this.id)});
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
			database = ENGINE.start(ENGINE.WRITE, SYSTEM.DB);
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
			database = ENGINE.start(ENGINE.WRITE, SYSTEM.DB);
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
			database = ENGINE.start(ENGINE.READ, SYSTEM.DB);
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
			database = ENGINE.start(ENGINE.READ, SYSTEM.DB);
			cursor = database.query(_TABLENAME, getAllColumns(), _PATIENT_CODE + " = ?", new String[] {this.patientCode}, null, null, null);
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
		this.patientCode = cursor.getString(cursor.getColumnIndex(_PATIENT_CODE));
	}

	@Override
	protected ContentValues putValues () {
		ContentValues values = new ContentValues();
		if (this.id > 0) {
			values.put(_ID, this.id);
		}
		values.put(_PATIENT_CODE, this.patientCode);
		return values;
	}

	@Override
	protected String[] getAllColumns () {
		return new String[] {_ID, _PATIENT_CODE};
	}

}
