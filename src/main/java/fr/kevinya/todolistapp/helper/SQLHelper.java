package fr.kevinya.todolistapp.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLHelper extends SQLiteOpenHelper {
	public static final String TABLE_TASKS = "tasks";
	public static final String TABLE_TASKS_COLUMN_ID = "id";
	public static final String TABLE_TASKS_COLUMN_BACKEND_ID = "backend_id";
	public static final String TABLE_TASKS_COLUMN_NAME = "name";
	public static final String TABLE_TASKS_COLUMN_STATUS = "status";
	public static final String TABLE_TASKS_COLUMN_VERSION = "version";
	private static final String TABLE_TASKS_CREATE = "create table "
			+ TABLE_TASKS + " ("
			+ TABLE_TASKS_COLUMN_ID	+ " integer primary key autoincrement, "
			+ TABLE_TASKS_COLUMN_BACKEND_ID	+ " integer, "
			+ TABLE_TASKS_COLUMN_NAME + " text, "
			+ TABLE_TASKS_COLUMN_STATUS + " integer not null default 0, "
			+ TABLE_TASKS_COLUMN_VERSION + " integer not null default 0"
			+ ");";
	private static final String TABLE_TASKS_DELETE = "drop table if exists " + TABLE_TASKS;
	
	private static final String DATABASE_NAME = "todolist.db";
	private static final int DATABASE_VERSION = 5;
	
	public SQLHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(TABLE_TASKS_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(TABLE_TASKS_DELETE);
		onCreate(db);
	}

}
