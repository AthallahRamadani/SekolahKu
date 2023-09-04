package athallah.sekolahku

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        progressBar = findViewById(R.id.progress_horizontal)
        supportActionBar?.hide()
        updateProgress()
    }

    private fun updateProgress() {
        val handler = Handler()
        val loadingRunnable = Runnable {
            var currentProgress = progressBar.progress
            currentProgress += 10
            progressBar.progress = currentProgress
            if (currentProgress > 100) {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                updateProgress()
            }
        }
        handler.postDelayed(loadingRunnable, 300)
    }
}