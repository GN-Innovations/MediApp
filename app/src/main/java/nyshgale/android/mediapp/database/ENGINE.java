package nyshgale.android.mediapp.database;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import java.io.Closeable;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Database ENGINE
 */
public class ENGINE {

	public final static int READ = 0x1001;

	protected final static int WRITE = 0x11010;

	private final static String TAG = "MediApp.ENGINE";

	private static Context context;

	public static void init (Context context) {
		ENGINE.context = context;
	}

	public static SQLiteDatabase start (int mode, String schema) {
		if (!isContextValid()) {
			restart();
		}
		SQLiteDatabase database = null;
		try {
			switch (mode) {
				case READ:
					switch (schema) {
						case LIBRARY.DB:
							database = new LIBRARY(context).getReadableDatabase();
							break;
						case PATIENTS.DB:
							database = new PATIENTS(context).getReadableDatabase();
							break;
						case SYSTEM.DB:
							database = new SYSTEM(context).getReadableDatabase();
							break;
					}
					break;
				case WRITE:
					switch (schema) {
						case LIBRARY.DB:
							database = new LIBRARY(context).getWritableDatabase();
							break;
						case PATIENTS.DB:
							database = new PATIENTS(context).getWritableDatabase();
							break;
						case SYSTEM.DB:
							database = new SYSTEM(context).getWritableDatabase();
							break;
					}
					break;
				default:
					break;
			}
		} catch (Exception ex) {
			Log.e(TAG, "start: " + ex.getMessage());
		}
		return database;
	}

	public static void stop (Closeable... objects) {
		for (Closeable object : objects) {
			if (object != null) {
				try {
					object.close();
				} catch (Exception ex) {
					Log.e(TAG, "stop: " + ex.getMessage());
				}
			}
		}
	}

	public static void clone (InputStream input, OutputStream output) {
		try {
			byte[] buffer = new byte[1024];
			int length;
			while ((length = input.read(buffer)) > 0) {
				output.write(buffer, 0, length);
			}
		} catch (Exception ex) {
			Log.e(TAG, "clone: " + ex.getMessage());
		} finally {
			ENGINE.stop(input, output);
		}
	}

	public static File appFolder () {
		File appFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/MediApp");
		if (appFolder.mkdirs()) {
			Log.i(TAG, "AppFolder created: " + appFolder.getAbsolutePath());
		} else {
			Log.i(TAG, "AppFolder: " + appFolder.getAbsolutePath());
		}
		return appFolder;
	}

	protected static Context getContext () {
		return context;
	}

	private static boolean isContextValid () {
		if (context != null) {
			return true;
		} else {
			return false;
		}
	}

	private static void restart () {
		context.startActivity(new Intent(context, nyshgale.android.mediapp.ActivityLauncher.class));
	}

}
