package com.hb.batapp.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.hb.batapp.R
import com.hb.batapp.utils.isValidEmail
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        tv_forget_password.setOnClickListener(this)
        tv_connexion.setOnClickListener(this)
        tv_inscription.setOnClickListener(this)
        linear_layout_facebook.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view) {
            tv_forget_password -> {
                Toast.makeText(this, getString(R.string.forget_password), Toast.LENGTH_SHORT).show()
            }
            tv_connexion -> {
                if (verifyUserInput()) {
                    val email: String = et_address_mail.editableText.toString()
                    val password = et_password.editableText.toString()
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                        .addOnSuccessListener {
                            startActivity(Intent(this, HomeActivity::class.java))
                        }
                        .addOnFailureListener {
                            Snackbar.make(
                                constraint_parent_login,
                                getString(R.string.check_your_credentials),
                                Snackbar.LENGTH_LONG
                            ).show()
                        }
                }
            }
            tv_inscription -> {
                startActivity(Intent(this, RegisterActivity::class.java))
            }
            linear_layout_facebook -> {
                Toast.makeText(this, getString(R.string.facebook), Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun verifyUserInput(): Boolean {
        val email: String = et_address_mail.editableText.toString()
        val password = et_password.editableText.toString()
        var isUserValid = true

        when {
            password.isEmpty() -> {
                isUserValid = false
                et_password.error = getString(R.string.password_not_empty)
            }
            password.length < 6 -> {
                isUserValid = false
                et_password.error = getString(R.string.pasword_not_alid)
            }
            email.isEmpty() -> {
                isUserValid = false
                et_address_mail.error = getString(R.string.email_not_empty)
            }
            !email.isValidEmail() -> {
                isUserValid = false
                et_address_mail.error = getString(R.string.email_not_valid)
            }
        }
        return isUserValid
    }
}