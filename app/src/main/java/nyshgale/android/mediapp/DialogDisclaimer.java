package nyshgale.android.mediapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DialogDisclaimer extends DialogFragment {
	
	private final String[] messages = {
		"This mobile app not suitable for major illnesses.",
		"It is not a replacement nor a substitute to professional health care providers.",
		"Built to give information regarding first aids."
	};
	
	public static DialogDisclaimer create () {
		DialogDisclaimer dialog = new DialogDisclaimer();
		return dialog;
	}
	
	public Dialog onCreateDialog (Bundle savedInstanceState) {
		super.onCreateDialog(savedInstanceState);
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setView(getLayout());
		builder.setNeutralButton(getString(R.string.function_ok), new DialogInterface.OnClickListener() {
			@Override
			public void onClick (DialogInterface dialog, int which) {
				SharedPreferences sp = getActivity().getSharedPreferences("MediApp.Settings", 0);
				SharedPreferences.Editor spEditor = sp.edit();
			    spEditor.putBoolean("FirstRun", false);
			    spEditor.commit();
			}
		});
		return builder.create();
	}
	
	@SuppressLint("InflateParams")
	private View getLayout () {
		LayoutInflater inflater = getActivity().getLayoutInflater();
		int padding = getResources().getDimensionPixelSize(R.dimen.dimen_8);
		View layout = inflater.inflate(R.layout.fragment_disclaimer, null, false);
		LinearLayout linearLayout = (LinearLayout) layout.findViewById(R.id.linearlayout_messages__fragment_disclaimer);
		TextView firstMessage = (TextView) layout.findViewById(R.id.textview_message__fragment_disclaimer);
		int index = 0;
		firstMessage.setTextAppearance(getActivity(), R.style.AppTheme_TextAppearance_Description);
		firstMessage.setText(messages[index]);
		for (index = 1; index < messages.length; index++) {
			TextView message = new TextView(getActivity());
			message.setLayoutParams(firstMessage.getLayoutParams());
			message.setPadding(padding, padding, padding, padding);
			message.setTextAppearance(getActivity(), R.style.AppTheme_TextAppearance_Description);
			message.setText(messages[index]);
			linearLayout.addView(message);
		}
		return layout;
	}
	
}
