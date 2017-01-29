package nyshgale.android.mediapp.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Symptom
		extends Interface {

	// ****************************************************************************************** //
	// ****************************** CONSTANTS ************************************************* //
	// ****************************************************************************************** //

	public final static String _NAME = "symptom_name";

	public final static String TYPE = "nyshgale.android.mediapp.database.library.symptom";

	protected final static String _TABLENAME = "symptoms";

	private final static String TAG = "Library.Symptom";

	// ****************************************************************************************** //
	// ****************************** PRIVATE PROPERTIES **************************************** //
	// ****************************************************************************************** //

	private boolean saved;

	private boolean deleted;

	private long id;

	private String name;

	// ****************************************************************************************** //
	// ****************************** PROPERTIES INITIALIZATION ********************************* //
	// ****************************************************************************************** //

	{
		saved = false;
		deleted = false;
		id = 0;
		name = "";
	}

	// ****************************************************************************************** //
	// ****************************** CONSTRUCTORS ********************************************** //
	// ****************************************************************************************** //

	public Symptom () {
		super();
	}

	public Symptom (long id) {
		if (id > 0) {
			this.id = id;
			getById();
		}
	}

	public Symptom (String name) {
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
			cursor = database.query(_TABLENAME, new String[] {_ID, _NAME}, null, null, null, null, _NAME);
		} catch (Exception ex) {
			Log.e(TAG, "getCursor: " + ex.getMessage());
		}
		return cursor;
	}

	public static List<Symptom> getRecords (String selection, String[] selectionArgs, String groupBy, String orderBy) {
		SQLiteDatabase database = null;
		Cursor cursor = null;
		List<Symptom> symptoms = new ArrayList<>();
		try {
			database = ENGINE.start(ENGINE.READ, LIBRARY.DB);
			cursor = database.query(_TABLENAME, new String[] {_ID, _NAME}, selection, selectionArgs, groupBy, null, orderBy);
			if (cursor != null && cursor.moveToFirst()) {
				do {
					Symptom symptom = new Symptom();
					symptom.id = cursor.getLong(cursor.getColumnIndex(_ID));
					symptom.name = cursor.getString(cursor.getColumnIndex(_NAME));
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

	// ****************************************************************************************** //
	// ****************************** PUBLIC METHODS ******************************************** //
	// ****************************************************************************************** //

	@Override
	public boolean equals (Object object) {
		boolean result = false;
		if (object != null && object instanceof Symptom) {
			Symptom symptom = (Symptom) object;
			result = (symptom.id == this.id) & (symptom.name.equals(this.name));
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
		}
	}

	@Override
	public void delete () {
		SQLiteDatabase database = null;
		try {
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
	}

	@Override
	protected ContentValues putValues () {
		ContentValues values = new ContentValues();
		if (this.id > 0) {
			values.put(_ID, this.id);
		}
		values.put(_NAME, this.name);
		return values;
	}

	@Override
	protected String[] getAllColumns () {
		return new String[] {_ID, _NAME};
	}

}
