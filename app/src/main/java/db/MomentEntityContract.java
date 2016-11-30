package db;

import android.provider.BaseColumns;

/**
 * Created by nlazaris on 11/7/16.
 */

public final class MomentEntityContract {
    private MomentEntityContract() {}

    public static final String SQL_CREATE_TABLE =
        "CREATE TABLE " + MomentEntity.TABLE_NAME + " (" +
        MomentEntity._ID + " INTEGER PRIMARY KEY, " +
        MomentEntity.COLUMN_TITLE + " TEXT, " +
        MomentEntity.COLUMN_DESCRIPTION + " TEXT, " +
        MomentEntity.COLUMN_DESCRIPTION + " INTEGER DEFAULT 0 )";

    public static final String SQL_DELETE_ENTRIES =
        "DROP TABLE IF EXISTS " + MomentEntity.TABLE_NAME;

    public static final String ALTER_ADD_REVIEW_COUNT =
         "ALTER TABLE " + MomentEntity.TABLE_NAME +
         " ADD COLUMN " + MomentEntity.COLUMN_REVIEW_COUNT +
         " INTEGER DEFAULT 0";

    public static class MomentEntity implements BaseColumns {
        public static final String TABLE_NAME = "moments";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_REVIEW_COUNT = "review_count";
    }
}
