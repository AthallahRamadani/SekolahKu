package athallah.sekolahku

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import athallah.sekolahku.database.StudentDataSource
import java.lang.Exception
import kotlin.math.tan

class DetailActivity : AppCompatActivity() {

    private lateinit var namaTv: TextView
    private lateinit var noHpTv: TextView
    private lateinit var genderTv: TextView
    private lateinit var jenjangTv: TextView
    private lateinit var hobiTv: TextView
    private lateinit var alamatTv: TextView
    private lateinit var emailtilTv: TextView
    private lateinit var tanggaltilTv: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        namaTv = findViewById(R.id.name_tv)
        noHpTv = findViewById(R.id.nohp_tv)
        genderTv = findViewById(R.id.gender_tv)
        jenjangTv = findViewById(R.id.jenjang_tv)
        hobiTv = findViewById(R.id.hobi_tv)
        alamatTv = findViewById(R.id.alamat_tv)
        emailtilTv = findViewById(R.id.email_tv)
        tanggaltilTv = findViewById(R.id.tanggal_tv)

        getDetailSiswa()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> false
        }
    }

    private fun showDetailStudent(student: Siswa?) {
        namaTv.text = "${student?.namaDepan} ${student?.namaBelakang}"
        noHpTv.text = student?.noHp
        genderTv.text = student?.gender
        jenjangTv.text = student?.jenjang
        hobiTv.text = student?.hobi?.joinToString (", ")
        emailtilTv.text = student?.email
        tanggaltilTv.text = student?.date
        alamatTv.text = student?.alamat
    }

    private fun getDetailSiswa() {
        try{
            val dataSource = StudentDataSource(this)
            val siswa = dataSource.findById(
                intent.getLongExtra(KEY_ID, -1)
            )
            showDetailStudent(siswa)
        }catch (e: Exception) {
            showDetailStudent(null)
        }
    }

    companion object {
        const val  KEY_ID = "id"
    }
}