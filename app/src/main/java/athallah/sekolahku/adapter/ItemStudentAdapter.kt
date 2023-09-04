package athallah.sekolahku.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import athallah.sekolahku.R
import athallah.sekolahku.Siswa

class ItemStudentAdapter(context: Context): ArrayAdapter<Siswa>(context, R.layout.item_siswa) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        if (view== null) {
            view = LayoutInflater.from(context)
                .inflate(R.layout.item_siswa, parent, false)
        }

        val student = getItem(position)
        if (student != null && view !=null) {
            val nameTv = view.findViewById<TextView>(R.id.name_tv)
            val genderTv = view.findViewById<TextView>(R.id.gender_tv)
            val phoneNumberTv = view.findViewById<TextView>(R.id.phone_number_tv)
            val educationTv = view.findViewById<TextView>(R.id.education_tv)

            nameTv.text= "${student.namaDepan} ${student.namaBelakang}"
            genderTv.text = student.gender
            phoneNumberTv.text = student.noHp
            educationTv.text = student.jenjang
        }
        return view ?: throw java.lang.NullPointerException("View still null")
    }

}