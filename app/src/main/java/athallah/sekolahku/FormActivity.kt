package athallah.sekolahku

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Intent
import android.icu.text.CaseMap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import athallah.sekolahku.DetailActivity.Companion.KEY_ID
import athallah.sekolahku.database.StudentDataSource
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.time.Month
import java.util.*

class FormActivity : AppCompatActivity(), OnDateSetListener {
    private lateinit var nmdepan: TextInputLayout
    private lateinit var nmbelakang: TextInputLayout
    private lateinit var noHp: TextInputLayout
    private lateinit var gender: RadioGroup
    private lateinit var jenjang: Spinner
    private lateinit var membaca: CheckBox
    private lateinit var menulis: CheckBox
    private lateinit var menggambar: CheckBox
    private lateinit var alamat: TextInputLayout
    private lateinit var emailtil: TextInputLayout
    private lateinit var tanggaltil: TextInputLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        nmdepan = findViewById(R.id.nm_dpn)
        nmbelakang = findViewById(R.id.nm_blkng)
        noHp = findViewById(R.id.nohp)
        gender = findViewById(R.id.rd_grp)
        jenjang = findViewById(R.id.spin)
        membaca = findViewById(R.id.membaca)
        menulis = findViewById(R.id.menulis)
        menggambar = findViewById(R.id.menggambar)
        alamat = findViewById(R.id.alamat)
        emailtil = findViewById(R.id.email)
        tanggaltil = findViewById(R.id.tanggal)

        val saveBtn = findViewById<Button>(R.id.btn_simpan)
//          ketika savebtn tombol simpan dipencet
        saveBtn.setOnClickListener {
//            saveNewDataToDatabase()
            save()

        }

        tanggaltil.setEndIconOnClickListener {
            showDatePickerDialogue()
        }

        showReceivedDataStudent()

    }


    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG)
            .show()
    }

    //        cara menginput inputan
    private fun showInputuser() {
//        percabangan
        if (!isAllInputValid()) {
            showToast("your input not valid")
            return
        }

//         inputan textinputlayout
        val namaDepan = nmdepan.editText?.text ?: ""
        val namaBelakang = nmbelakang.editText?.text ?: ""
        val nomorTelp = noHp.editText?.text ?: ""
        val alamat = alamat.editText?.text ?: ""
        val email = emailtil.editText?.text ?: ""
        val tanggal = tanggaltil.editText?.text ?: ""

//          inputan radiobutton
        val selectedgender = when (gender.checkedRadioButtonId) {
            R.id.pria -> "Pria"
            R.id.wanita -> "Wanita"
            else -> ""
        }

//          inputan spinner
        val selectedEducation = jenjang.selectedItem.toString()

//          inputan checkbox
        val hobiPilihan = arrayListOf<String>()
        if (membaca.isChecked) hobiPilihan.add("Membaca")
        if (menulis.isChecked) hobiPilihan.add("Menulis")
        if (menggambar.isChecked) hobiPilihan.add("Menggambar")

//          menampilkan di notif Toast
        showToast(
            "Hello $namaDepan $namaBelakang\n" +
                    "No. Hp : $nomorTelp\n" +
                    "Gender : $selectedgender\n" +
                    "Education : $selectedEducation\n" +
                    "Hobbies : $hobiPilihan\n" +
                    "Address : $alamat\n" +
                    "Email : $email\n" +
                    "Tanggal Lahir : $tanggal"
        )
    }

    //      untuk menampilkan menu diatas pojok kanan
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_form_menu, menu)
        return true
    }

    //          ngasih aksi ke menunya(pojok kanan atas)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.save_menu -> {
//                showInputuser()
//                saveNewDataToDatabase()
//                untuk pindah ke listactivity
//                val id = item.itemId
//                if (id == R.id.save_menu) {
//                    val intent = Intent(
//                        this, ListActivity::class.java)
//                    startActivity (intent)
//                }
                save()
                true
            }
            android.R.id.home -> {
                finish()
                true
            }
            else -> false
        }
    }

    //    validasi nama depan
    private fun validateFirstName(): Boolean {
        val firstName = nmdepan.editText?.text ?: ""

        return if (firstName.isEmpty()) {
            nmdepan.setErrorInput("Nama depan jangan kosong")
            false
        } else if (isContainSpecialCharacter(firstName.toString())) {
            nmdepan.setErrorInput("nama depan jangan pakai special character")
            false
        } else {
            nmdepan.clearError()
            true
        }
    }

    //    validasi nama belakang
    private fun validateLastName(): Boolean {
        val lastName = nmbelakang.editText?.text ?: ""

        return if (lastName.isEmpty()) {
            nmbelakang.setErrorInput("Nama belakang jangan kosong")
            false
        } else if (isContainSpecialCharacter(lastName.toString())) {
            nmbelakang.setErrorInput("nama belakang jangan pakai special character")
            false
        } else {
            nmbelakang.clearError()
            true
        }
    }

    //    validasi email
    private fun validateEmail(): Boolean {
        val email = emailtil.editText?.text ?: ""

        return if (email.isEmpty()) {
            emailtil.setErrorInput("Email jangan kosong")
            false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailtil.setErrorInput("email tidak benar")
            true
        } else {
            emailtil.clearError()
            true
        }
    }


    //    validasi tanggal
    private fun validateDate(): Boolean {
        val tanggal = tanggaltil.editText?.text ?: ""

        return if (tanggal.isEmpty()) {
            tanggaltil.setErrorInput("Tanggal jangan kosong")
            false
        } else {
            tanggaltil.clearError()
            true
        }
    }

    //    validasi no hp
    private fun validatePhoneNumber(): Boolean {
        val phoneNum = noHp.editText?.text ?: ""

        val phoneNumLength = phoneNum.length

        return if (phoneNum.isEmpty()) {
            noHp.setErrorInput("No Handphone jangan kosong")
            false
        } else if (isContainAlphabet(phoneNum.toString())) {
            noHp.setErrorInput("No handphone harus angka")
            false
        } else if (phoneNumLength > 12) {
            noHp.setErrorInput("nomor ente kepanjangan")
            false
        } else {
            noHp.clearError()
            true
        }
    }

    //    validasi no hp (harus angka)
    private fun isContainAlphabet(name: String): Boolean {
        val forbiddenAlphabet = "qwertyuiopasdfghjklzxcvbnm" +
                "QWERTYUIOPASDFGHJKLZXCVBNM" +
                "`[]|',./;~!@#$%^&*()_+{}:<>?"

        for (character in name) {
            if (forbiddenAlphabet.contains(character)) {
                return true
            }
        }
        return false
    }

    //    validasi alamat
    private fun validateAddress(): Boolean {
        val address = alamat.editText?.text ?: ""

        return if (address.isEmpty()) {
            alamat.setErrorInput("Alamat jangan kosong")
            false
        } else {
            alamat.clearError()
            true
        }
    }

    //    fungsi message
    private fun TextInputLayout.setErrorInput(message: String) {
        error = message
        isErrorEnabled = true
    }

    private fun TextInputLayout.clearError() {
        error = null
        isErrorEnabled = false
    }

    //    untuk validasi semuanya
    private fun isAllInputValid(): Boolean {
        return validateFirstName() and
                validateLastName() and
                validateAddress() and
                validatePhoneNumber() and
                validateDate() and
                validateEmail()
    }

    //    validasi special character
    private fun isContainSpecialCharacter(name: String): Boolean {
        val forbiddenCharacters = "1234567890`[]|',./;~!@#$%^&*()_+{}:<>?"

        for (character in name) {
            if (forbiddenCharacters.contains(character)) {
                return true
            }
        }
        return false
    }

    //    ngumpulin data user ke siswa
    private fun collectInputToStudent(): Siswa {
        val namaDepan = nmdepan.editText?.text ?: ""
        val namaBelakang = nmbelakang.editText?.text ?: ""
        val nomorTelp = noHp.editText?.text ?: ""
        val alamat = alamat.editText?.text ?: ""
        val date = tanggaltil.editText?.text ?: ""
        val email = emailtil.editText?.text ?: ""
        val selectedgender = when (gender.checkedRadioButtonId) {
            R.id.pria -> "Pria"
            R.id.wanita -> "Wanita"
            else -> ""
        }
        val selectedEducation = jenjang.selectedItem.toString()
        val hobiPilihan = arrayListOf<String>()
        if (membaca.isChecked) hobiPilihan.add("Membaca")
        if (menulis.isChecked) hobiPilihan.add("Menulis")
        if (menggambar.isChecked) hobiPilihan.add("Menggambar")

        Log.d("FORM", hobiPilihan.joinToString(", "))
        return Siswa().apply {
            this.namaDepan = namaDepan.toString()
            this.namaBelakang = namaBelakang.toString()
            this.noHp = nomorTelp.toString()
            this.alamat = alamat.toString()
            this.gender = selectedgender
            this.jenjang = selectedEducation
            this.hobi = hobiPilihan
            this.date = date.toString()
            this.email = email.toString()
        }
    }

    //    kirim ke database
    private fun saveNewDataToDatabase() {
        if (!isAllInputValid()) {
            showToast("Gak valid bro")
            return
        }
        val student = collectInputToStudent()
        try {
            val datasource = StudentDataSource(this)
            datasource.insert(student)
            showToast("berhasil euy")
            finish()

        } catch (e: Exception) {
            e.printStackTrace()
            showToast("gagal euy")
        }
    }

    private fun showDatePickerDialogue() {
        val selectedCalendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            this,
            this,
            selectedCalendar.get(Calendar.YEAR),
            selectedCalendar.get(Calendar.MONTH),
            selectedCalendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val userSelectedCalendar = Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month)
            set(Calendar.DAY_OF_MONTH, dayOfMonth)
        }
        val dateFormat = SimpleDateFormat.getDateInstance()
        val dateString = dateFormat.format(userSelectedCalendar.time)
        tanggaltil.editText?.setText(dateString)

    }

    private fun showReceivedDataStudent() {
        val id = intent.getLongExtra(KEY_ID, -1)
        try {
            val dataSource = StudentDataSource(this)
            val foundStudent = dataSource.findById(id)
            nmdepan.editText?.setText(foundStudent.namaDepan)
            nmbelakang.editText?.setText(foundStudent.namaBelakang)
            noHp.editText?.setText(foundStudent.noHp)
            alamat.editText?.setText(foundStudent.alamat)
            emailtil.editText?.setText(foundStudent.email)
            tanggaltil.editText?.setText(foundStudent.date)

            when (foundStudent.gender) {
                "Pria" -> gender.check(R.id.pria)
                "Wanita" -> gender.check(R.id.wanita)
            }

            val savedHobbies = foundStudent.hobi
            if (savedHobbies.contains("Membaca")) membaca.isChecked = true
            if (savedHobbies.contains("Menulis")) menulis.isChecked = true
            if (savedHobbies.contains("Menggambar")) menggambar.isChecked = true

            val educationAdapter = jenjang.adapter
            for (i in 0 until educationAdapter.count) {
                if (educationAdapter.getItem(i) == foundStudent.jenjang) {
                    jenjang.setSelection(i)
                    break
                }
            }

            title = "Edit Data Siswa"
        } catch (e: Exception) {
            e.printStackTrace()
            title = "Add Data Siswa"
        }

    }

    private fun updateNewDataToDatabase() {
        if (!isAllInputValid()) {
            showToast("Gak valid bro")
            return
        }
        val student = collectInputToStudent()
        student.id = intent.getLongExtra(KEY_ID, -1)
        try {
            val datasource = StudentDataSource(this)
            datasource.update(student)
            showToast("berhasil update euy")
            finish()

        } catch (e: Exception) {
            e.printStackTrace()
            showToast("gagal upate euy")
        }
    }

    private fun save(){
        if (intent.getLongExtra(KEY_ID,-1) > -1){
            updateNewDataToDatabase()
        } else {
            saveNewDataToDatabase()
        }
    }

    companion object {
        const val KEY_ID = "id"
    }


}