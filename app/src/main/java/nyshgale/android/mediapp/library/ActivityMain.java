package nyshgale.android.mediapp.library;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import nyshgale.android.mediapp.R;
import nyshgale.android.mediapp.custom.DialogDelete;
import nyshgale.android.mediapp.custom.DialogOptions;
import nyshgale.android.mediapp.custom.Event;
import nyshgale.android.mediapp.database.Cause;
import nyshgale.android.mediapp.database.Illness;
import nyshgale.android.mediapp.database.LIBRARY;
import nyshgale.android.mediapp.database.Medication;
import nyshgale.android.mediapp.database.Symptom;

public class ActivityMain
		extends Activity
		implements Event.OnItemLongClickedListener,
		           Event.OnOptionSelectedListener,
		           Event.OnSaveButtonClickedListener,
		           Event.OnDeleteButtonClickedListener {

	private ListIllnesses listIllnesses;

	private ListSymptoms listSymptoms;

	private ListCauses listCauses;

	private ListMedications listMedications;

	private Object object;

	{
		object = new Object();
	}

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_library);
	}

	@Override
	protected void onStart () {
		super.onStart();
		setList(R.id.action_library_illnesses);
	}

	@Override
	public boolean onCreateOptionsMenu (Menu menu) {
		getMenuInflater().inflate(R.menu.library, menu);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected (MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_library_new_item:
				newItem();
				return true;
			case R.id.action_library_illnesses:
			case R.id.action_library_medications:
			case R.id.action_library_symptoms:
			case R.id.action_library_causes:
				item.setChecked(true);
				setList(item.getItemId());
				return true;
			case R.id.action_library_export:
				if (LIBRARY.exportDB("library")) {
					Toast.makeText(this, "Exported Successfully", Toast.LENGTH_SHORT).show();
				}
				return true;
			case R.id.action_library_import:
				if (LIBRARY.importDB("library")) {
					switch (object.toString()) {
						case Illness.TYPE:
							listIllnesses.loader.onContentChanged();
							break;
						case Symptom.TYPE:
							listSymptoms.loader.onContentChanged();
							break;
						case Cause.TYPE:
							listCauses.loader.onContentChanged();
							break;
						case Medication.TYPE:
							listMedications.loader.onContentChanged();
							break;
						default:
							break;
					}
					Toast.makeText(this, "Imported Successfully", Toast.LENGTH_SHORT).show();
				}
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	private void newItem () {
		switch (object.toString()) {
			case Illness.TYPE:
				editItem(new Illness());
				break;
			case Medication.TYPE:
				editItem(new Medication());
				break;
			case Symptom.TYPE:
				editItem(new Symptom());
				break;
			case Cause.TYPE:
				editItem(new Cause());
				break;
			default:
				break;
		}
	}

	private void editItem (Object object) {
		switch (object.toString()) {
			case Illness.TYPE:
				final Intent illnessIntent = new Intent(this, ActivityIllness.class);
				Illness illness = (Illness) object;
				illnessIntent.putExtra(Illness._ID, illness.getId());
				startActivity(illnessIntent);
				break;
			case Medication.TYPE:
			case Symptom.TYPE:
			case Cause.TYPE:
				DialogEdit dialog = DialogEdit.create(object);
				dialog.show(getFragmentManager(), null);
				break;
			default:
				break;
		}
	}

	private void setList (int type) {
		switch (type) {
			case R.id.action_library_illnesses:
				listIllnesses = (ListIllnesses) Fragment.instantiate(this, "nyshgale.android.mediapp.library.ListIllnesses");
				changeFragment(listIllnesses);
				object = new Illness();
				setTitle("Library - Illnesses");
				break;
			case R.id.action_library_medications:
				listMedications = (ListMedications) Fragment.instantiate(this, "nyshgale.android.mediapp.library.ListMedications");
				changeFragment(listMedications);
				object = new Medication();
				setTitle("Library - Medications");
				break;
			case R.id.action_library_symptoms:
				listSymptoms = (ListSymptoms) Fragment.instantiate(this, "nyshgale.android.mediapp.library.ListSymptoms");
				changeFragment(listSymptoms);
				object = new Symptom();
				setTitle("Library - Symptoms");
				break;
			case R.id.action_library_causes:
				listCauses = (ListCauses) Fragment.instantiate(this, "nyshgale.android.mediapp.library.ListCauses");
				changeFragment(listCauses);
				object = new Cause();
				setTitle("Library - Causes");
				break;
		}
	}

	private void changeFragment (Fragment fragment) {
		getFragmentManager().beginTransaction().replace(R.id.frame_list__activity_library, fragment).commit();
	}

	@Override
	public void onItemLongClicked (Object object) {
		this.object = object;
		DialogOptions.create(object).show(getFragmentManager(), null);
	}

	@Override
	public void onOptionSelected (String option) {
		switch (option) {
			case "Edit":
				editItem(object);
				break;
			case "Delete":
				deleteItem(object);
				break;
			default:
				break;
		}
	}

	private void deleteItem (Object object) {
		DialogDelete.create(object).show(getFragmentManager(), null);
	}

	@Override
	public void onSaveButtonClicked (Object object) {
		switch (object.toString()) {
			case Illness.TYPE:
				break;
			case Medication.TYPE:
				Medication medication = (Medication) object;
				medication.save();
				if (medication.isSaved()) {
					listMedications.loader.onContentChanged();
					Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
				}
				break;
			case Symptom.TYPE:
				Symptom symptom = (Symptom) object;
				symptom.save();
				if (symptom.isSaved()) {
					listSymptoms.loader.onContentChanged();
					Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
				}
				break;
			case Cause.TYPE:
				Cause cause = (Cause) object;
				cause.save();
				if (cause.isSaved()) {
					listCauses.loader.onContentChanged();
					Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
				}
				break;
			default:
				break;
		}
	}

	@Override
	public void onDeleteButtonClicked (Object object) {
		switch (object.toString()) {
			case Illness.TYPE:
				Illness illness = (Illness) object;
				illness.delete();
				if (illness.isDeleted()) {
					listIllnesses.loader.onContentChanged();
					Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
				}
				break;
			case Medication.TYPE:
				Medication medication = (Medication) object;
				medication.delete();
				if (medication.isDeleted()) {
					listMedications.loader.onContentChanged();
					Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
				}
				break;
			case Symptom.TYPE:
				Symptom symptom = (Symptom) object;
				symptom.delete();
				if (symptom.isDeleted()) {
					listSymptoms.loader.onContentChanged();
					Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
				}
				break;
			case Cause.TYPE:
				Cause cause = (Cause) object;
				cause.delete();
				if (cause.isDeleted()) {
					listCauses.loader.onContentChanged();
					Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
				}
				break;
			default:
				break;
		}
	}

}
