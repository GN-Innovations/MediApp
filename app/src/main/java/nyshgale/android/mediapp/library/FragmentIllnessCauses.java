package nyshgale.android.mediapp.library;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import nyshgale.android.mediapp.R;
import nyshgale.android.mediapp.custom.Adapter;
import nyshgale.android.mediapp.database.Cause;

import java.util.ArrayList;
import java.util.List;

/**
 * Layout for Illness' Causes
 */
public class FragmentIllnessCauses
		extends Fragment {

	private List<CauseView> causeViews;

	private LayoutInflater inflater;

	private ViewGroup container;

	private LinearLayout linearLayout;

	{
		causeViews = new ArrayList<>();
	}

	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View layout = inflater.inflate(R.layout.fragment_library_illness_causes, container);
		this.inflater = inflater;
		this.container = container;
		this.linearLayout = (LinearLayout) layout.findViewById(R.id.linearlayout_causes__fragment_library_illness_causes);
		ImageButton addButton = (ImageButton) layout.findViewById(R.id.imagebutton_add__fragment_library_illness_causes);
		addButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick (View v) {
				for (CauseView causeView : causeViews) {
					if (causeView.getCause() == null) {
						return;
					}
				}
				addCause(new Cause());
			}
		});
		return layout;
	}

	public void addCause (Cause cause) {
		CauseView causeView = new CauseView(cause);
		causeViews.add(causeView);
		linearLayout.addView(causeView.getView());
	}

	public List<Cause> getCauses () {
		List<Cause> causes = new ArrayList<>();
		for (CauseView causeView : causeViews) {
			if (causeView.getCause() != null) {
				Cause cause = causeView.getCause();
				if (cause.getId() <= 0) {
					cause.save();
				}
				boolean duplicate = (causes.contains(cause));
				if (!duplicate) {
					causes.add(cause);
				} else {
					removeCause(causeView);
				}
			}
		}
		return causes;
	}

	public void setCauses (List<Cause> causes) {
		if (!causes.isEmpty()) {
			for (CauseView causeView : causeViews) {
				removeCause(causeView);
			}
			for (Cause cause : causes) {
				addCause(cause);
			}
		}
	}

	public void removeCause (CauseView causeView) {
		linearLayout.removeView(causeView.getView());
		causeViews.remove(causeView);
	}

	private class CauseView
			implements View.OnClickListener {

		private View view;

		private AutoCompleteTextView detailsView;

		public CauseView (Cause cause) {
			view = inflater.inflate(R.layout.edit_library_cause, container);
			detailsView = (AutoCompleteTextView) view.findViewById(R.id.autotext_details__edit_library_cause);
			detailsView.setAdapter(new Adapter(getActivity(), Cause.TYPE));
			ImageButton removeView = (ImageButton) view.findViewById(R.id.imagebutton_remove__edit_library_cause);
			removeView.setOnClickListener(this);
			setCause(cause);
		}

		public View getView () {
			return view;
		}

		public Cause getCause () {
			if (detailsView.getText().length() > 0) {
				String details = detailsView.getText().toString();
				return new Cause(details);
			} else {
				return null;
			}
		}

		private void setCause (Cause cause) {
			if (cause != null) {
				detailsView.setText(cause.getDetails());
			}
		}

		@Override
		public void onClick (View v) {
			if (getCause() == null || getCause().getId() == 0) {
				removeCause(this);
			}
		}

	}

}
