package athallah.sekolahku

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.AdapterView
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat.startActivity
import athallah.sekolahku.DetailActivity.Companion.KEY_ID
import athallah.sekolahku.adapter.ItemStudentAdapter
import athallah.sekolahku.database.StudentDataSource
import java.lang.Exception
import java.text.Normalizer

class ListActivity : AppCompatActivity(), SearchView.OnQueryTextListener {
    private lateinit var adapter: ItemStudentAdapter
    private lateinit var studentSv: SearchView
    private lateinit var containerNotFound : LinearLayout
    private lateinit var studentLv : ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        studentLv = findViewById(R.id.student_lv)
        studentSv = findViewById(R.id.student_sv)
        containerNotFound = findViewById(R.id.not_found_container)

        adapter = ItemStudentAdapter(this)
        studentLv.adapter = adapter

        studentLv.setOnItemClickListener{parent, view, position, id ->
            val selectedSiswa = adapter.getItem(position)
            if (selectedSiswa != null) {
                val intent = Intent(this, DetailActivity::class.java)
                intent.putExtra(DetailActivity.KEY_ID,selectedSiswa.id)
                startActivity(intent)
            }
        }

        studentSv.setOnQueryTextListener(this)
        showContainerNotFound(false)
        registerForContextMenu(studentLv)
    }

    override fun onResume() {
        super.onResume()
        loadStudentData()
    }

    private fun loadStudentData() {
        try {
            val dataSource = StudentDataSource(this)
            val studentList = dataSource.getAll()

            adapter.clear()
            studentList.forEach { student ->
                adapter.add(student)
            }
            adapter.notifyDataSetChanged()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_list_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.plus_menu) {
            val intent = Intent(
                this, FormActivity::class.java)
            startActivity (intent)
            return true
        } else return false
    }

    fun searchStudentByName(keyword: String) {
        try {
            val dataSource = StudentDataSource(this)
            val foundStudent = dataSource.findByKeywordName(keyword)
            adapter.clear()
            adapter.addAll(foundStudent)
            adapter.notifyDataSetChanged()
            showContainerNotFound(false)
        } catch (e: Exception) {
            adapter.clear()
            adapter.notifyDataSetChanged()
            showContainerNotFound(true)
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        searchStudentByName(query ?:"")
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        searchStudentByName(newText?:"")
        return false
    }

    private fun showContainerNotFound(show: Boolean) {
        if (show) {
            containerNotFound.visibility = VISIBLE
            studentLv.visibility = GONE
        } else {
            containerNotFound.visibility = GONE
            studentLv.visibility = VISIBLE
        }
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        menuInflater.inflate(R.menu.activity_list_context_menu, menu)
        super.onCreateContextMenu(menu, v, menuInfo)
    }

    private fun deleteStudent(selectedStudent: Siswa) {
        try {
            val dataSource = StudentDataSource(this)
            dataSource.delete(selectedStudent)
            adapter.remove(selectedStudent)
            adapter.notifyDataSetChanged()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this,
                "Failed to delete ${selectedStudent.namaDepan} ${selectedStudent.namaBelakang}",
            Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val selectedPosition =when(val menuInfo = item.menuInfo){
            is AdapterView.AdapterContextMenuInfo -> menuInfo.position
            else -> -1
        }

        if (selectedPosition > -1) {
            val selectedStudent = adapter.getItem(selectedPosition)
            if(selectedStudent != null){
                when(item.itemId) {
                    R.id.delete_menu -> {
//                        deleteStudent(selectedStudent)
                        showConfirmationDelete(selectedStudent)
                        return true
                    }
                    R.id.edit_menu -> {
                        val intent = Intent(this, FormActivity::class.java)
                        intent.putExtra(FormActivity.KEY_ID,selectedStudent.id)
                        startActivity(intent)
                        return true
                    }
                }
            }
        }
        return false
    }

    private fun showConfirmationDelete(selectedStudent : Siswa) {
        AlertDialog.Builder(this)
            .setTitle("hapus siswa")
            .setMessage("yakin bos?")
            .setIcon(R.mipmap.ic_launcher)
            .setNegativeButton("batal"){dialog, which -> dialog.dismiss()}
            .setPositiveButton("lanjut mang") {dialog, which ->
                deleteStudent(selectedStudent)
                dialog.dismiss()
            }.show()
    }
}