package nyshgale.android.mediapp.custom;

import android.annotation.TargetApi;
import android.graphics.pdf.PdfDocument;
import android.graphics.pdf.PdfDocument.Page;
import android.graphics.pdf.PdfDocument.PageInfo;
import android.os.Build;
import android.util.Log;
import android.view.View;
import nyshgale.android.mediapp.database.Diagnosis;
import nyshgale.android.mediapp.database.ENGINE;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@TargetApi(Build.VERSION_CODES.KITKAT)
public class PDF {

	private final String TAG = "Custom.PDF";

	private Object object;

	private File file;

	public static PDF create (Object object) {
		PDF pdf = new PDF();
		pdf.object = object;
		return pdf;
	}

	public boolean export (View view) {
		boolean result = false;
		PdfDocument document = new PdfDocument();
		PageInfo pageInfo = new PageInfo.Builder(view.getWidth(), view.getHeight(), 1).create();
		Page page = document.startPage(pageInfo);
		view.draw(page.getCanvas());
		document.finishPage(page);
		try {
			document.writeTo(getOutputStream());
			result = true;
			Log.i(TAG, "PDF file: " + file.getAbsolutePath());
		} catch (Exception ex) {
			Log.e("", ex.getMessage());
		}
		document.close();
		return result;
	}

	public String getFilename () {
		return file.getName();
	}

	private FileOutputStream getOutputStream () {
		Diagnosis diagnosis = (Diagnosis) object;
		SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyy-HHmmss", Locale.getDefault());
		String filename = dateFormat.format(new Date(diagnosis.getDatetime())) + ".pdf";
		FileOutputStream outputStream = null;
		try {
			file = new File(ENGINE.appFolder() + "/" + filename);
			outputStream = new FileOutputStream(file);
		} catch (Exception ex) {
			Log.e(TAG, "Creating file: " + ex.getMessage());
		}
		return outputStream;
	}

}
