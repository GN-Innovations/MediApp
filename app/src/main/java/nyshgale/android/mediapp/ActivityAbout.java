package nyshgale.android.mediapp;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import nyshgale.android.mediapp.database.LIBRARY;

public class ActivityAbout
		extends Activity {

	private TextView appView;

	private TextView libraryView;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		appView = (TextView) findViewById(R.id.textview_app_version__activity_about);
		libraryView = (TextView) findViewById(R.id.textview_library_version__activity_about);
	}

	@Override
	protected void onStart () {
		super.onStart();
		appView.setText(getString(R.string.app_version) + ": 1.7.254 Beta");
		libraryView.setText(getString(R.string.library_version) + ": " + LIBRARY.getVersionCode() + " Alpha");
	}

}
