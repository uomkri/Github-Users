package ru.uomkri.tchktest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKAccessToken
import com.vk.api.sdk.auth.VKAuthCallback
import ru.uomkri.tchktest.repo.Repository

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val callback = object : VKAuthCallback {
            override fun onLogin(token: VKAccessToken) {
                Repository.setSuccess()
            }

            override fun onLoginFailed(errorCode: Int) {
                Toast.makeText(baseContext, "Login failed with error code $errorCode", Toast.LENGTH_SHORT).show()
            }

        }

        VK.onActivityResult(requestCode, resultCode, data, callback)
    }
}