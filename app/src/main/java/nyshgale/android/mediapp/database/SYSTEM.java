package nyshgale.android.mediapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Preferences database
 */
public class SYSTEM
		extends SQLiteOpenHelper {

	public final static String DB = "nyshgale.android.mediapp.database.system";

	private final static String name = "system.db";

	private final static int version = 2;

	public SYSTEM (Context context) {
		super(context, name, null, version);
	}

	@Override
	public void onCreate (SQLiteDatabase db) {
		db.execSQL(Create.PreferencesTable);
		db.execSQL(Create.SecurityTable);
	}

	@Override
	public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(Drop.PreferencesTable);
		db.execSQL(Drop.SecurityTable);
		onCreate(db);
	}

	private final static class Create {

		private final static String PreferencesTable = "CREATE TABLE " + Preferences._TABLENAME + " ( " +
				                                               Preferences._ID + " INTEGER PRIMARY KEY, " +
				                                               Preferences._PATIENT_CODE + " TEXT NOT NULL) ";

		private final static String SecurityTable = "CREATE TABLE " + Security._TABLENAME + " ( " +
				                                            Security._ID + " INTEGER PRIMARY KEY, " +
				                                            Security._PASSWORD + " TEXT NOT NULL) ";

	}

	private final static class Drop {

		private final static String PreferencesTable = "DROP TABLE IF EXISTS " + Preferences._TABLENAME;

		private final static String SecurityTable = "DROP TABLE IF EXISTS " + Security._TABLENAME;

	}

}
