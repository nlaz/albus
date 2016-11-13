package db;

import android.provider.BaseColumns;

/**
 * Created by nlazaris on 11/7/16.
 */

public final class MomentEntityContract {
    private MomentEntityContract() {}

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";

    public static final String SQL_CREATE_TABLE =
        "CREATE TABLE " + MomentEntity.TABLE_NAME + " (" +
        MomentEntity._ID + " INTEGER PRIMARY KEY, " +
        MomentEntity.COLUMN_TITLE + TEXT_TYPE + COMMA_SEP +
        MomentEntity.COLUMN_DESCRIPTION + TEXT_TYPE + " )";

    public static final String SQL_DELETE_ENTRIES =
        "DROP TABLE IF EXISTS " + MomentEntity.TABLE_NAME;

    public static class MomentEntity implements BaseColumns {
        public static final String TABLE_NAME = "moments";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DESCRIPTION = "description";
    }
}
