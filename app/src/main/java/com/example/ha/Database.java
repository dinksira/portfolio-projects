package com.example.ha;
import com.example.ha.User;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class Database extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "HA";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "users";
    private static final String COLUMN_FIRST_NAME = "first_name";
    private static final String COLUMN_LAST_NAME = "last_name";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_AGE = "age";
    private static final String COLUMN_SEX = "sex";
    private static final String COLUMN_PASSWORD = "password";
    private DatabaseReference firebaseReference;

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        // Initialize Firebase
        firebaseReference = FirebaseDatabase.getInstance().getReference("users");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + "(" +
                COLUMN_FIRST_NAME + " TEXT, " +
                COLUMN_LAST_NAME + " TEXT, " +
                COLUMN_EMAIL + " TEXT, " +
                COLUMN_AGE + " TEXT, " +
                COLUMN_SEX + " TEXT, " +
                COLUMN_PASSWORD + " TEXT)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void register(String first_name, String last_name, String email, String age, String sex, String password) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FIRST_NAME, first_name);
        values.put(COLUMN_LAST_NAME, last_name);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_AGE, age);
        values.put(COLUMN_SEX, sex);
        values.put(COLUMN_PASSWORD, password);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public boolean login(String username, String password) {
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {COLUMN_FIRST_NAME};
        String selection = COLUMN_FIRST_NAME + " = ? AND " + COLUMN_PASSWORD + " = ?";
        String[] selectionArgs = {username, password};
        Cursor cursor = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        boolean result = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return result;
    }

    public void syncWithFirebase() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        while (cursor.moveToNext()) {
            String firstName = cursor.getString(cursor.getColumnIndex(COLUMN_FIRST_NAME));
            String lastName = cursor.getString(cursor.getColumnIndex(COLUMN_LAST_NAME));
            String email = cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL));
            String age = cursor.getString(cursor.getColumnIndex(COLUMN_AGE));
            String sex = cursor.getString(cursor.getColumnIndex(COLUMN_SEX));
            String password = cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD));

            // Upload user data to Firebase
            String userId = firebaseReference.push().getKey();
            User user = new User(userId, firstName, lastName, email, age, sex, password);
            firebaseReference.child(userId).setValue(user);
        }
        cursor.close();
        db.close();
    }
}
