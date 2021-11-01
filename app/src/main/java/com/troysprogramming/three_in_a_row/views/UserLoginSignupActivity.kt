package com.troysprogramming.three_in_a_row.views

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.troysprogramming.three_in_a_row.R
import com.troysprogramming.three_in_a_row.models.User
import com.troysprogramming.three_in_a_row.models.database.SQLiteService
import java.security.CryptoPrimitive
import java.security.KeyStore
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory

class UserLoginSignupActivity : AppCompatActivity() {

    private lateinit var edtUsername : EditText
    private lateinit var edtPassword : EditText
    private lateinit var chkNewAccount : CheckBox
    private lateinit var btnSubmit : Button

    private var signUpMode = false

    private var hashedPassword : String = ""

    override fun onCreate(sis: Bundle?) {
        super.onCreate(sis)
        setContentView(R.layout.layout_user_login_signup)

        edtUsername = findViewById(R.id.edt_username)
        edtPassword = findViewById(R.id.edt_password)
        chkNewAccount = findViewById(R.id.chk_new_account)
        btnSubmit = findViewById(R.id.btn_submit)

        chkNewAccount.setOnCheckedChangeListener { _, b -> checkboxChanged(b) }

        btnSubmit.setOnClickListener { submit() }
    }

    private fun checkboxChanged(isChecked: Boolean) {
        signUpMode = isChecked
        btnSubmit.text = if(isChecked) resources.getText(R.string.signup)
            else resources.getText(R.string.login)
    }

    private fun submit() {
        hashedPassword = hashPassword()
        if(signUpMode)
            signUp()
        else
            login()
    }

    private fun signUp() {
        if(SQLiteService.getInstance().getUsersUnderUsernameResult(edtUsername.text.toString())
                .isEmpty())
        {
            User.createUser(edtUsername.text.toString(), hashedPassword)
            finish()
        }
        else
        {
            Toast.makeText(this, "User already exists.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun login() {

        val users : List<User> = SQLiteService.getInstance()
            .getUsersUnderUsernameResult(edtUsername.text.toString())

        if(users.isNotEmpty())
        {
            // salt password, then compare with database password
            if(users[0].getPassword() == hashedPassword)
            {
                User.loginAsUser(users[0])
                finish()
            }
            else
            {
                Toast.makeText(this, "Incorrect Password.", Toast.LENGTH_SHORT).show()
            }
        }
        else
        {
            Toast.makeText(this, "User does not exist.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun hashPassword() : String {
        val hasher = MessageDigest.getInstance("SHA-256")
        hasher.update((edtPassword.text.toString()).encodeToByteArray())

        val bytes : ByteArray = hasher.digest()

        var builder : StringBuilder = StringBuilder()

        for(i in bytes.indices) { builder.append(bytes[i].toInt() and 0xFF) }

        return builder.toString()
    }
}