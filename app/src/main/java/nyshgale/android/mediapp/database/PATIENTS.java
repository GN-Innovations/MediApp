package nyshgale.android.mediapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * PatientsTable database
 */
public class PATIENTS
		extends SQLiteOpenHelper {

	public final static String DB = "nyshgale.android.mediapp.database.patients";

	private final static String name = "patients.db";

	private final static int version = 6;

	private final static String extension = ".mediapp-patients";

	public PATIENTS (Context context) {
		super(context, name, null, version);
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
			result = ENGINE.start(ENGINE.READ, PATIENTS.DB).isOpen();
		} catch (Exception ex) {
			Log.e("importDB", ex.getMessage());
			result = false;
		}
		return result;
	}

	@Override
	public void onCreate (SQLiteDatabase db) {
		db.execSQL(Create.PatientsTable);
		db.execSQL(Create.DiagnosisTable);
		db.execSQL(Create.DiagnosisSymptomsTable);
		db.execSQL(Create.DiagnosisCausesTable);
	}

	@Override
	public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(Drop.PatientsTable);
		db.execSQL(Drop.DiagnosisTable);
		db.execSQL(Drop.DiagnosisSymptomsTable);
		db.execSQL(Drop.DiagnosisCausesTable);
		onCreate(db);
	}

	private final static class Create {

		private final static String PatientsTable = "CREATE TABLE " + Patient._TABLENAME + " ( " +
				                                            Patient._ID + " INTEGER PRIMARY KEY, " +
				                                            Patient._CODE + " TEXT NOT NULL UNIQUE, " +
				                                            Patient._LASTNAME + " TEXT NOT NULL, " +
				                                            Patient._FIRSTNAME + " TEXT NOT NULL, " +
				                                            Patient._BIRTHDATE + " INTEGER NOT NULL, " +
				                                            Patient._GENDER + " TEXT NOT NULL )";

		private final static String DiagnosisTable = "CREATE TABLE " + Diagnosis._TABLENAME + " ( " +
				                                             Diagnosis._ID + " INTEGER PRIMARY KEY, " +
				                                             Diagnosis._DATETIME + " INTEGER NOT NULL, " +
				                                             Diagnosis._PATIENT + " INTEGER NOT NULL, " +
				                                             Diagnosis._ILLNESS + " INTEGER NOT NULL )";

		private final static String DiagnosisSymptomsTable = "CREATE TABLE " + Diagnosis.Symptoms._TABLENAME + " ( " +
				                                                     Diagnosis.Symptoms._ID + " INTEGER PRIMARY KEY, " +
				                                                     Diagnosis.Symptoms._DIAGNOSIS + " INTEGER NOT NULL, " +
				                                                     Diagnosis.Symptoms._SYMPTOM + " INTEGER NOT NULL )";

		private final static String DiagnosisCausesTable = "CREATE TABLE " + Diagnosis.Causes._TABLENAME + " ( " +
				                                                   Diagnosis.Causes._ID + " INTEGER PRIMARY KEY, " +
				                                                   Diagnosis.Causes._DIAGNOSIS + " INTEGER NOT NULL, " +
				                                                   Diagnosis.Causes._CAUSE + " INTEGER NOT NULL )";

	}

	private final static class Drop {

		private final static String PatientsTable = "DROP TABLE IF EXISTS " + Patient._TABLENAME;

		private final static String DiagnosisTable = "DROP TABLE IF EXISTS " + Diagnosis._TABLENAME;

		private final static String DiagnosisSymptomsTable = "DROP TABLE IF EXISTS " + Diagnosis.Symptoms._TABLENAME;

		private final static String DiagnosisCausesTable = "DROP TABLE IF EXISTS " + Diagnosis.Causes._TABLENAME;

	}

}
