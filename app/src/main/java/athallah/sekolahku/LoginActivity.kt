package athallah.sekolahku

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import com.google.android.material.textfield.TextInputLayout

class LoginActivity : AppCompatActivity() {
    private lateinit var usernameTil: TextInputLayout
    private lateinit var passwordTil: TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        usernameTil = findViewById(R.id.username)
        passwordTil = findViewById(R.id.password)

        val loginBtn = findViewById<Button>(R.id.login)
        loginBtn.setOnClickListener { login() }

        usernameTil.editText?.doOnTextChanged { text, start, before, count ->
            validateUsername()
        }
        passwordTil.editText?.doOnTextChanged { text, start, before, count ->
            validatePassword()
        }

    }

    private fun validateUsername(): Boolean {
        val username = usernameTil.editText?.text ?: ""

        return if (username.isEmpty()) {
            usernameTil.setErrorInput("Username jangan kosong")
            false
        } else {
            usernameTil.clearError()
            true
        }
    }

    private fun validatePassword(): Boolean {
        val password = passwordTil.editText?.text ?: ""

        return if (password.isEmpty()) {
            passwordTil.setErrorInput("Password jangan kosong")
            false
        } else {
            passwordTil.clearError()
            true
        }
    }

    private fun isAllInputValid(): Boolean {
        return validateUsername() and
                validatePassword()
    }

    private fun TextInputLayout.setErrorInput(message: String) {
        error = message
        isErrorEnabled = true
    }

    private fun TextInputLayout.clearError() {
        error = null
        isErrorEnabled = false
    }

    private fun isContainSpecialCharacter(name: String): Boolean {
        val forbiddenCharacters = "1234567890`[]|',./;~!@#$%^&*()_+{}:<>?"

        for (character in name) {
            if (forbiddenCharacters.contains(character)) {
                return true
            }
        }
        return false
    }

    private fun login() {
        if (!isAllInputValid()) {
            showToast("Gak valid bro")
            return
        } else {
            val intent = Intent(this, ListActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG)
            .show()
    }
}