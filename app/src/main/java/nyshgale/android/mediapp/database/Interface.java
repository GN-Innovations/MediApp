package nyshgale.android.mediapp.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;

abstract class Interface
		implements BaseColumns {

	@Override
	public abstract boolean equals (Object object);

	@Override
	public abstract String toString ();

	public abstract void save ();

	public abstract void delete ();

	protected abstract boolean insert ();

	protected abstract boolean update ();

	protected abstract void getById ();

	protected abstract void getByUnique ();

	protected abstract void getFromCursor (Cursor cursor);

	protected abstract ContentValues putValues ();

	protected abstract String[] getAllColumns ();

}
