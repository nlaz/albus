package db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import models.Moment;

/**
 * SQLiteHelper - Handles device database queries.
 *
 * Created by nlazaris on 11/7/16.
 */

public class SQLiteHelper extends SQLiteOpenHelper{

    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "PENSIEVE_DB.db";
    private static final MomentEntityContract.MomentEntity MomentEntity = new MomentEntityContract.MomentEntity();

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(MomentEntityContract.SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion == 1 && newVersion == 2) {
            db.execSQL(MomentEntityContract.ALTER_ADD_REVIEW_COUNT);
        }
    }

    public Integer insertMoment(Moment moment) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MomentEntity.COLUMN_TITLE, moment.getTitle());
        values.put(MomentEntity.COLUMN_DESCRIPTION, moment.getDescription());
        long rowid = db.insert(MomentEntity.TABLE_NAME, null, values);
        Cursor c = db.rawQuery("SELECT * from " + MomentEntity.TABLE_NAME + " WHERE rowid = "  + rowid, null);
        c.moveToFirst();
        return c.getInt(c.getColumnIndex(MomentEntity._ID));
    }

    public boolean updateMoment(Integer id, Moment moment) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MomentEntity.COLUMN_TITLE, moment.getTitle());
        values.put(MomentEntity.COLUMN_DESCRIPTION, moment.getDescription());
        db.update(MomentEntity.TABLE_NAME, values, MomentEntity._ID + " = ? ", new String[] { Integer.toString(id) });
        return true;
    }

    public Integer deleteMomentById(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(MomentEntity.TABLE_NAME, MomentEntity._ID + " = ? ", new String[] { Integer.toString(id) });
    }

    public ArrayList<Moment> getAllMoments() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Moment> list = new ArrayList<Moment>();

        Cursor c = db.rawQuery("SELECT * from " + MomentEntity.TABLE_NAME, null);
        c.moveToFirst();

        while(c.isAfterLast() == false) {
            Integer id    = c.getInt(c.getColumnIndex(MomentEntity._ID));
            Integer count = c.getInt(c.getColumnIndex(MomentEntity.COLUMN_REVIEW_COUNT));
            String title  = c.getString(c.getColumnIndex(MomentEntity.COLUMN_TITLE));
            String desc   = c.getString(c.getColumnIndex(MomentEntity.COLUMN_DESCRIPTION));

            list.add(new Moment(id, title, desc, count));
            c.moveToNext();
        }
        return list;
    }
}

