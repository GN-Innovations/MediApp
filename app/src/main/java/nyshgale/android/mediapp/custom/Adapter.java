package nyshgale.android.mediapp.custom;

import android.content.Context;
import android.widget.ArrayAdapter;
import nyshgale.android.mediapp.database.Cause;
import nyshgale.android.mediapp.database.Illness;
import nyshgale.android.mediapp.database.Medication;
import nyshgale.android.mediapp.database.Patient;
import nyshgale.android.mediapp.database.Symptom;

import java.util.List;

/**
 * Custom Adapter
 */
public class Adapter
		extends ArrayAdapter<String> {

	public Adapter (Context context, String type) {
		super(context, android.R.layout.simple_list_item_1, getStrings(type));
	}

	private static String[] getStrings (String type) {
		switch (type) {
			case Symptom.TYPE:
				return getFromSymptoms();
			case Cause.TYPE:
				return getFromCauses();
			case Medication.TYPE:
				return getFromMedications();
			case Illness.TYPE:
				return getFromIllnesses();
			case Patient.TYPE:
				return getFromPatients();
			default:
				return new String[] {};
		}
	}

	private static String[] getFromSymptoms () {
		List<Symptom> symptoms = Symptom.getRecords(null, null, Symptom._NAME, Symptom._NAME);
		String[] names = new String[symptoms.size()];
		int index = 0;
		for (Symptom symptom : symptoms) {
			names[index] = symptom.getName();
			index++;
		}
		return names;
	}

	private static String[] getFromCauses () {
		List<Cause> causes = Cause.getRecords(null, null, Cause._DETAILS, Cause._DETAILS);
		String[] names = new String[causes.size()];
		int index = 0;
		for (Cause cause : causes) {
			names[index] = cause.getDetails();
			index++;
		}
		return names;
	}

	private static String[] getFromMedications () {
		List<Medication> medications = Medication.getRecords(null, null, Medication._NAME, Medication._NAME);
		String[] names = new String[medications.size()];
		int index = 0;
		for (Medication medication : medications) {
			names[index] = medication.getName();
			index++;
		}
		return names;
	}

	private static String[] getFromIllnesses () {
		List<Illness> illnesses = Illness.getRecords(null, null, Illness._NAME, Illness._NAME);
		String[] names = new String[illnesses.size()];
		int index = 0;
		for (Illness illness : illnesses) {
			names[index] = illness.getName();
			index++;
		}
		return names;
	}

	private static String[] getFromPatients () {
		List<Patient> patients = Patient.getRecords(null, null, Patient._LASTNAME, Patient._LASTNAME);
		String[] names = new String[patients.size()];
		int index = 0;
		for (Patient patient : patients) {
			names[index] = patient.getLastName();
			index++;
		}
		return names;
	}

}
