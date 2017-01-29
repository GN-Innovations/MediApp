package nyshgale.android.mediapp.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Patient
		extends Interface {

	// ****************************************************************************************** //
	// ****************************** CONSTANTS ************************************************* //
	// ****************************************************************************************** //

	public final static String _CODE = "patient_code";

	public final static String _LASTNAME = "patient_lastname";

	public final static String _FIRSTNAME = "patient_firstname";

	public final static String _BIRTHDATE = "patient_birthdate";

	public final static String _GENDER = "patient_gender";

	public final static String TYPE = "nyshgale.android.mediapp.database.patients.patient";

	protected final static String _TABLENAME = "patients";

	private final static String TAG = "Patients.Patient";

	// ****************************************************************************************** //
	// ****************************** PRIVATE PROPERTIES **************************************** //
	// ****************************************************************************************** //

	private boolean saved;

	private boolean deleted;

	private long id;

	private String code;

	private String lastname;

	private String firstname;

	private long birthdate;

	private String gender;

	// ****************************************************************************************** //
	// ****************************** PROPERTIES INITIALIZATION ********************************* //
	// ****************************************************************************************** //

	{
		saved = false;
		deleted = false;
		id = 0;
		code = "";
		lastname = "";
		firstname = "";
		birthdate = 0;
		gender = "";
	}

	// ****************************************************************************************** //
	// ****************************** CONSTRUCTORS ********************************************** //
	// ****************************************************************************************** //

	public Patient () {
		super();
	}

	public Patient (long id) {
		if (id > 0) {
			this.id = id;
			getById();
		}
	}

	public Patient (String code) {
		if (code != null) {
			this.code = code;
			getByUnique();
		}
	}

	// ****************************************************************************************** //
	// ****************************** STATIC METHODS ******************************************** //
	// ****************************************************************************************** //

	public static Cursor getCursor () {
		Cursor cursor = null;
		try {
			SQLiteDatabase database = ENGINE.start(ENGINE.READ, PATIENTS.DB);
			cursor = database.query(_TABLENAME, new String[] {_ID, _CODE, _LASTNAME, _FIRSTNAME, _BIRTHDATE, _GENDER}, null, null, null, null, _LASTNAME);
		} catch (Exception ex) {
			Log.e(TAG, "getCursor: " + ex.getMessage());
		}
		return cursor;
	}

	public static List<Patient> getRecords (String selection, String[] selectionArgs, String groupBy, String orderBy) {
		SQLiteDatabase database = null;
		Cursor cursor = null;
		List<Patient> patients = new ArrayList<>();
		try {
			database = ENGINE.start(ENGINE.READ, PATIENTS.DB);
			cursor = database.query(_TABLENAME, new String[] {_ID, _CODE, _LASTNAME, _FIRSTNAME, _BIRTHDATE, _GENDER}, selection, selectionArgs, groupBy, null, orderBy);
			if (cursor != null && cursor.moveToFirst()) {
				do {
					Patient patient = new Patient();
					patient.id = cursor.getLong(cursor.getColumnIndex(_ID));
					patient.code = cursor.getString(cursor.getColumnIndex(_CODE));
					patient.lastname = cursor.getString(cursor.getColumnIndex(_LASTNAME));
					patient.firstname = cursor.getString(cursor.getColumnIndex(_FIRSTNAME));
					patient.birthdate = cursor.getLong(cursor.getColumnIndex(_BIRTHDATE));
					patient.gender = cursor.getString(cursor.getColumnIndex(_GENDER));
					patients.add(patient);
				} while (cursor.moveToNext());
			}
		} catch (Exception ex) {
			Log.e(TAG, "getRecords: " + ex.getMessage());
		} finally {
			ENGINE.stop(database, cursor);
		}
		return patients;
	}

	// ****************************************************************************************** //
	// ****************************** GETTER AND SETTER ******************************************** //
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

	public String getCode () {
		return code;
	}

	public void setCode (String code) {
		this.code = code;
	}

	public String getLastName () {
		return lastname;
	}

	public void setLastName (String lastname) {
		this.lastname = lastname;
	}

	public String getFirstName () {
		return firstname;
	}

	public void setFirstName (String firstname) {
		this.firstname = firstname;
	}

	public long getBirthDate () {
		return birthdate;
	}

	public void setBirthDate (long birthdate) {
		this.birthdate = birthdate;
	}

	public String getGender () {
		return gender;
	}

	public void setGender (String gender) {
		this.gender = gender;
	}

	public String getAge () {
		return computeAge();
	}

	// ****************************************************************************************** //
	// ****************************** PUBLIC METHODS ******************************************** //
	// ****************************************************************************************** //

	@Override
	public boolean equals (Object object) {
		boolean result = false;
		if (object != null && object instanceof Patient) {
			Patient patient = (Patient) object;
			result = (patient.id == this.id) &
					         (patient.code.equals(this.code)) &
					         (patient.lastname.equals(this.lastname)) &
					         (patient.firstname.equals(this.firstname)) &
					         (patient.birthdate == this.birthdate) &
					         (patient.gender.equals(this.gender));
		}
		return result;
	}

	@Override
	public String toString () {
		return TYPE;
	}

	@Override
	public void save () {
		boolean valid = !this.code.isEmpty() & !this.lastname.isEmpty();
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
			cursor = database.query(_TABLENAME, getAllColumns(), _CODE + " = ?", new String[] {this.code}, null, null, null);
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
		this.code = cursor.getString(cursor.getColumnIndex(_CODE));
		this.lastname = cursor.getString(cursor.getColumnIndex(_LASTNAME));
		this.firstname = cursor.getString(cursor.getColumnIndex(_FIRSTNAME));
		this.birthdate = cursor.getLong(cursor.getColumnIndex(_BIRTHDATE));
		this.gender = cursor.getString(cursor.getColumnIndex(_GENDER));
	}

	@Override
	protected ContentValues putValues () {
		ContentValues values = new ContentValues();
		if (this.id > 0) {
			values.put(_ID, this.id);
		}
		values.put(_CODE, this.code);
		values.put(_LASTNAME, this.lastname);
		values.put(_FIRSTNAME, this.firstname);
		values.put(_BIRTHDATE, this.birthdate);
		values.put(_GENDER, this.gender);
		return values;
	}

	@Override
	protected String[] getAllColumns () {
		return new String[] {_ID, _CODE, _LASTNAME, _FIRSTNAME, _BIRTHDATE, _GENDER};
	}

	// ****************************************************************************************** //
	// ****************************** PRIVATE METHODS ******************************************* //
	// ****************************************************************************************** //

	private String computeAge () {
		if (code.equals("000")) {
			return "0";
		}
		int age = 0;
		Calendar today = Calendar.getInstance();
		Calendar birth = Calendar.getInstance();
		if (id > 0 || birthdate != 0) {
			birth.setTimeInMillis(birthdate);
		} else {
			birth.setTimeInMillis(today.getTimeInMillis());
		}
		age = today.get(Calendar.YEAR) - birth.get(Calendar.YEAR);
		if ((today.get(Calendar.MONTH) - birth.get(Calendar.MONTH)) < 0) {
			age--;
		} else if ((today.get(Calendar.MONTH) - birth.get(Calendar.MONTH)) == 0) {
			if ((today.get(Calendar.DAY_OF_MONTH) - birth.get(Calendar.DAY_OF_MONTH)) < 0) {
				age--;
			}
		}
		if (age < 0) {
			age = 0;
		}
		return Integer.toString(age);
	}

}
