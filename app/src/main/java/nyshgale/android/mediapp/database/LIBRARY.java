package nyshgale.android.mediapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Library Database
 */
public class LIBRARY
		extends SQLiteOpenHelper {

	public final static String DB = "nyshgale.android.mediapp.database.library";

	private final static String name = "library.db";

	private final static int version = 8;

	private final static String extension = ".mediapp-library";

	public LIBRARY (Context context) {
		super(context, name, null, version);
	}

	public static String getVersionCode () {
		String main = Integer.toString(Illness.getRecords(null, null, null, null).size());
		String sub = Integer.toString(Symptom.getRecords(null, null, null, null).size() + Cause.getRecords(null, null, null, null).size() + Medication.getRecords(null, null, null, null).size());
		return version + "." + main + "." + sub;
	}

	public static boolean exportDB (String filename) {
		boolean result;
		try {
			File source = new File(ENGINE.getContext().getDatabasePath(name).getAbsolutePath());
			File destination = new File(ENGINE.appFolder().getAbsolutePath() + "/" + filename + extension);
			ENGINE.clone(new FileInputStream(source), new FileOutputStream(destination));
			result = true;
		} catch (Exception ex) {
			Log.e("exportDB", ex.getMessage());
			result = false;
		}
		return result;
	}

	public static boolean importDB (String filename) {
		boolean result;
		File source = new File(ENGINE.appFolder().getAbsolutePath() + "/" + filename + extension);
		File destination = new File(ENGINE.getContext().getDatabasePath(name).getAbsolutePath());
		try {
			ENGINE.clone(new FileInputStream(source), new FileOutputStream(destination));
			result = ENGINE.start(ENGINE.READ, LIBRARY.DB).isOpen();
		} catch (Exception ex) {
			Log.e("importDB", ex.getMessage());
			result = false;
		}
		return result;
	}

	@Override
	public void onCreate (SQLiteDatabase db) {
		db.execSQL(Create.SymptomsTable);
		db.execSQL(Create.CausesTable);
		db.execSQL(Create.MedicationsTable);
		db.execSQL(Create.IllnessesTable);
		db.execSQL(Create.IllnessSymptomsTable);
		db.execSQL(Create.IllnessCausesTable);
		db.execSQL(Create.IllnessMedicationsTable);
	}

	@Override
	public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(Drop.SymptomsTable);
		db.execSQL(Drop.CausesTable);
		db.execSQL(Drop.MedicationsTable);
		db.execSQL(Drop.IllnessesTable);
		db.execSQL(Drop.IllnessSymptomsTable);
		db.execSQL(Drop.IllnessCausesTable);
		db.execSQL(Drop.IllnessMedicationsTable);
		onCreate(db);
	}

	private final static class Create {

		private final static String SymptomsTable = "CREATE TABLE " + Symptom._TABLENAME + " ( " +
				                                            Symptom._ID + " INTEGER PRIMARY KEY, " +
				                                            Symptom._NAME + " TEXT NOT NULL UNIQUE )";

		private final static String CausesTable = "CREATE TABLE " + Cause._TABLENAME + " ( " +
				                                          Cause._ID + " INTEGER PRIMARY KEY, " +
				                                          Cause._DETAILS + " TEXT NOT NULL UNIQUE )";

		private final static String MedicationsTable = "CREATE TABLE " + Medication._TABLENAME + " ( " +
				                                               Medication._ID + " INTEGER PRIMARY KEY, " +
				                                               Medication._NAME + " TEXT NOT NULL UNIQUE )";

		private final static String IllnessesTable = "CREATE TABLE " + Illness._TABLENAME + " ( " +
				                                             Illness._ID + " INTEGER PRIMARY KEY, " +
				                                             Illness._NAME + " TEXT NOT NULL UNIQUE, " +
				                                             Illness._DESCRIPTION + " TEXT, " +
				                                             Illness._REMEDIES + " TEXT, " +
				                                             Illness._NOTES + " TEXT )";

		private final static String IllnessSymptomsTable = "CREATE TABLE " + Illness.Symptoms._TABLENAME + " ( " +
				                                                   Illness.Symptoms._ID + " INTEGER PRIMARY KEY, " +
				                                                   Illness.Symptoms._ILLNESS + " INTEGER NOT NULL, " +
				                                                   Illness.Symptoms._SYMPTOM + " INTEGER NOT NULL )";

		private final static String IllnessCausesTable = "CREATE TABLE " + Illness.Causes._TABLENAME + " ( " +
				                                                 Illness.Causes._ID + " INTEGER PRIMARY KEY, " +
				                                                 Illness.Causes._ILLNESS + " INTEGER NOT NULL, " +
				                                                 Illness.Causes._CAUSE + " INTEGER NOT NULL )";

		private final static String IllnessMedicationsTable = "CREATE TABLE " + Illness.Medications._TABLENAME + " ( " +
				                                                      Illness.Medications._ID + " INTEGER PRIMARY KEY, " +
				                                                      Illness.Medications._ILLNESS + " INTEGER NOT NULL, " +
				                                                      Illness.Medications._MEDICATION + " INTEGER NOT NULL )";
	}

	private final static class Drop {

		private final static String SymptomsTable = "DROP TABLE IF EXISTS " + Symptom._TABLENAME;

		private final static String CausesTable = "DROP TABLE IF EXISTS " + Cause._TABLENAME;

		private final static String MedicationsTable = "DROP TABLE IF EXISTS " + Medication._TABLENAME;

		private final static String IllnessesTable = "DROP TABLE IF EXISTS " + Illness._TABLENAME;

		private final static String IllnessSymptomsTable = "DROP TABLE IF EXISTS " + Illness.Symptoms._TABLENAME;

		private final static String IllnessCausesTable = "DROP TABLE IF EXISTS " + Illness.Causes._TABLENAME;

		private final static String IllnessMedicationsTable = "DROP TABLE IF EXISTS " + Illness.Medications._TABLENAME;
	}

}
