package athallah.sekolahku.database

import android.content.ContentValues
import android.content.Context
import android.content.res.Resources
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import athallah.sekolahku.Siswa
import kotlinx.coroutines.selects.select

class StudentDataSource(context: Context) {
    private val helper = DatabaseHelper(context)
    private var db: SQLiteDatabase? = null

    private fun open() {
        db = helper.writableDatabase
    }

    private fun close() {
        db = null
        helper.close()
    }

    fun insert(siswa: Siswa) {
        val contentValues = ContentValues().apply {
            put(DatabaseHelper.FIRST_NAME_FIELD, siswa.namaDepan)
            put(DatabaseHelper.LAST_NAME_FIELD, siswa.namaBelakang)
            put(DatabaseHelper.GENDER_FIELD, siswa.gender)
            put(DatabaseHelper.EDUCATION_FIELD, siswa.jenjang)
            put(DatabaseHelper.ADDRESS_FIELD, siswa.alamat)
            put(DatabaseHelper.PHONE_NUMBER_FIELD, siswa.noHp)
            put(DatabaseHelper.HOBBIES_FIELD, siswa.hobi.joinToString(", "))
            put(DatabaseHelper.EMAIL_FIELD, siswa.email)
            put(DatabaseHelper.TANGGAL_LAHIR_FIELD, siswa.date)
        }
        Log.d("DATA FORM", siswa.hobi.joinToString(", "))
        open()
        val database = db ?: throw IllegalAccessException("database not initialized")
        database.insertOrThrow(DatabaseHelper.TABLE_NAME, null, contentValues)
        close()
    }

    //    untuk menampilkan data dari kursor
    private fun fetchRow(cursor: Cursor): Siswa {
        cursor.run {
            val id = getInt(getColumnIndexOrThrow(DatabaseHelper.ID_FIELD)).toLong()
            val firstName = getString(getColumnIndexOrThrow(DatabaseHelper.FIRST_NAME_FIELD))
            val lastName = getString(getColumnIndexOrThrow(DatabaseHelper.LAST_NAME_FIELD))
            val gender = getString(getColumnIndexOrThrow(DatabaseHelper.GENDER_FIELD))
            val education = getString(getColumnIndexOrThrow(DatabaseHelper.EDUCATION_FIELD))
            val phonenumber = getString(getColumnIndexOrThrow(DatabaseHelper.PHONE_NUMBER_FIELD))
            val address = getString(getColumnIndexOrThrow(DatabaseHelper.ADDRESS_FIELD))
            val hobbies = getString(getColumnIndexOrThrow(DatabaseHelper.HOBBIES_FIELD))
            val email = getString(getColumnIndexOrThrow(DatabaseHelper.EMAIL_FIELD))
            val birthDate = getString(getColumnIndexOrThrow(DatabaseHelper.TANGGAL_LAHIR_FIELD))

            Log.d("DATA", hobbies)
            return Siswa().apply {
                this.id = id
                this.namaDepan = firstName
                this.namaBelakang = lastName
                this.gender = gender
                this.jenjang = education
                this.noHp = phonenumber
                this.alamat = address
                this.hobi = hobbies.split(", ")
                this.email = email
                this.date = birthDate
            }
        }
    }

    fun getAll(): List<Siswa> {
        open()
        val database = db ?: throw IllegalAccessException("Database not opened")
        val cursor = database.rawQuery(
            "SELECT * FROM ${DatabaseHelper.TABLE_NAME}",
            null
        )
        val students = arrayListOf<Siswa>()
        while (cursor.moveToNext()) {
            val student = fetchRow(cursor)
            students.add(student)
        }
        cursor.close()
        close()

        if (students.isEmpty()) throw Resources.NotFoundException("No data students")
        return students
    }

    fun findById(id: Long): Siswa {
        open()
        val database = db ?: throw IllegalAccessException("Databse not opened")
        val cursor = database.rawQuery(
            "SELECT * FROM ${DatabaseHelper.TABLE_NAME} WHERE ${DatabaseHelper.ID_FIELD}=?",
            arrayOf(id.toString())
        )
        if (cursor.count <= 0) throw Resources.NotFoundException("Data siswa id : $id gak ada")
        cursor.moveToFirst()
        val foundSiswa = fetchRow(cursor)
        cursor.close()
        close()

        return foundSiswa
    }

    //    untuk search
    fun findByKeywordName(keyword: String): List<Siswa> {
        open()
        val database = db ?: throw IllegalAccessException("Databse not opened")
        val nameToSearch = "$keyword%"
        val cursor = database.rawQuery(
            "SELECT * FROM ${DatabaseHelper.TABLE_NAME} " +
                    "WHERE ${DatabaseHelper.FIRST_NAME_FIELD} LIKE ? OR " +
                    "${DatabaseHelper.LAST_NAME_FIELD} LIKE ?",
            arrayOf(nameToSearch, nameToSearch)
        )
        val foundSiswaList = arrayListOf<Siswa>()
        while (cursor.moveToNext()) {
//        fetch row untuk mengambil data siswa didalam kursor
            val siswa = fetchRow(cursor)
            foundSiswaList.add(siswa)
        }
        if (foundSiswaList.size <= 0) throw Resources.NotFoundException("No data siswa with keyword $keyword")
        cursor.close()
        close()
        return foundSiswaList
    }

    fun delete(siswa: Siswa) {
        open()
        val database = db ?: throw IllegalAccessException("Databse not opened")
        val totalDeletedData = database.delete(
            DatabaseHelper.TABLE_NAME,
        "${DatabaseHelper.ID_FIELD}=?",
        arrayOf(siswa.id.toString())
        )
        if (totalDeletedData <= 0) throw Resources.NotFoundException("Data siswa id $siswa.id tidak ada")
        close()
    }

    fun update(siswa: Siswa) {
        open()
        val database = db ?: throw IllegalAccessException("Databse not opened")
        val contentValues = ContentValues().apply {
            put(DatabaseHelper.FIRST_NAME_FIELD, siswa.namaDepan)
            put(DatabaseHelper.LAST_NAME_FIELD, siswa.namaBelakang)
            put(DatabaseHelper.GENDER_FIELD, siswa.gender)
            put(DatabaseHelper.EDUCATION_FIELD, siswa.jenjang)
            put(DatabaseHelper.ADDRESS_FIELD, siswa.alamat)
            put(DatabaseHelper.PHONE_NUMBER_FIELD, siswa.noHp)
            put(DatabaseHelper.HOBBIES_FIELD, siswa.hobi.joinToString(", "))
            put(DatabaseHelper.EMAIL_FIELD, siswa.email)
            put(DatabaseHelper.TANGGAL_LAHIR_FIELD, siswa.date)
        }
        val totalUpdateData = database.update(
            DatabaseHelper.TABLE_NAME,
            contentValues,
            "${DatabaseHelper.ID_FIELD}=?",
            arrayOf(siswa.id.toString())
        )
        if (totalUpdateData <= 0) throw Resources.NotFoundException("No Student id : ${siswa.id}")
        close()
    }
}