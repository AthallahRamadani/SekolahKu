package athallah.sekolahku.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context): SQLiteOpenHelper(
    context,
    "sekolahku.db",
    null,
    2
) {
    override fun onCreate(db: SQLiteDatabase?) {
        val sql = "CREATE TABLE $TABLE_NAME(" +
                "$ID_FIELD INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$FIRST_NAME_FIELD TEXT," +
                "$LAST_NAME_FIELD TEXT," +
                "$GENDER_FIELD TEXT," +
                "$EDUCATION_FIELD TEXT," +
                "$HOBBIES_FIELD TEXT," +
                "$PHONE_NUMBER_FIELD TEXT," +
                "$ADDRESS_FIELD TEXT," +
                "$EMAIL_FIELD TEXT," +
                "$TANGGAL_LAHIR_FIELD TEXT)"

        db?.execSQL(sql)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
       if (db != null && newVersion > oldVersion){
            val sqlAddColumnEmail = "ALTER TABLE $TABLE_NAME ADD $EMAIL_FIELD TEXT"
            val sqlAddColumnBirthDate = "ALTER TABLE $TABLE_NAME ADD $TANGGAL_LAHIR_FIELD TEXT"
           db.execSQL(sqlAddColumnEmail)
           db.execSQL(sqlAddColumnBirthDate)
       }
    }

    companion object {
        val TABLE_NAME = "student"
        val ID_FIELD = "id"
        val FIRST_NAME_FIELD = "first_name"
        val LAST_NAME_FIELD = "last_name"
        val GENDER_FIELD = "gender"
        val EDUCATION_FIELD = "education"
        val ADDRESS_FIELD = "address"
        val HOBBIES_FIELD = "hobbies"
        val PHONE_NUMBER_FIELD = "phone_number"
        val EMAIL_FIELD = "email"
        val TANGGAL_LAHIR_FIELD = "tanggal_lahir"
    }
}
