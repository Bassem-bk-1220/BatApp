package com.hb.batapp.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.hb.batapp.R
import com.hb.batapp.models.UserModel
import com.hb.batapp.utils.*
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity(), View.OnClickListener {

    private var currentStep = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        tv_previous.setOnClickListener(this)
        tv_next.setOnClickListener(this)

        setStepView()
    }

    private fun setStepView() {
        val listLabels = arrayOf("Profile", "Details", "End")
        stepview_register.setLabels(listLabels)
            .setBarColorIndicator(ContextCompat.getColor(this, R.color.colorYellow))
            .setProgressColorIndicator(ContextCompat.getColor(this, R.color.colorGrey))
            .setLabelColorIndicator(ContextCompat.getColor(this, R.color.colorGrey))
            .setCompletedPosition(1)
            .drawView()

    }


    override fun onClick(view: View?) {
        when (view) {
            tv_previous -> {
                Toast.makeText(this, "previous", Toast.LENGTH_SHORT).show()
                }

            tv_next -> {
                when (currentStep) {
                    0 -> {
                        if (validateNameAndEmail()) {
                            stepview_register.completedPosition = 2
                            currentStep++
                            et_full_name.invisible()
                            et_address_mail.invisible()

                            et_phone_number.show()
                            et_password.show()
                        }
                    }
                    1 -> {
                        if (isPhoneNumberAndPasswordValid()) {
                            currentStep++
                            stepview_register.completedPosition = 3
                            et_phone_number.hide()
                            et_password.hide()

                            tv_code_message.show()
                            et_code.show()
                        }
                    }
                    2 -> {
                        if (!isCodeValid()) {
                            saveUserToFirebase()
                        }
                    }
                }
            }
        }
    }

    private fun saveUserToFirebase() {
        val email = et_address_mail.editableText.toString()
        val password = et_password.editableText.toString()
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { result ->
                if (result.isSuccessful) saveUserToDatabase()
            }.addOnFailureListener {
                showError()
            }

    }

    private fun saveUserToDatabase() {
        val uid = FirebaseAuth.getInstance().uid
        val reference = FirebaseDatabase.getInstance().getReference("$USERS$uid")

        val phoneNumber = et_phone_number.editableText.toString()
        val fullName = et_full_name.editableText.toString()
        val email = et_address_mail.editableText.toString()
        val password = et_password.editableText.toString()

        val user = UserModel(
            fullName,
            email,
            password,
            phoneNumber
        )
        reference.setValue(user)
            .addOnSuccessListener {
                startActivity(Intent(this, HomeActivity::class.java))
            }.addOnFailureListener {
                showError()
            }


    }

    private fun showError() {
        Toast.makeText(this, getString(R.string.data_not_saved), Toast.LENGTH_SHORT).show()
    }

    private fun validateNameAndEmail(): Boolean {
        val name = et_full_name.editableText.toString()
        val email = et_address_mail.editableText.toString()
        var isUserInputValid = true
        when {
            name.isEmpty() -> {
                isUserInputValid = false
                et_full_name.error = getString(R.string.full_name_empty)
            }
            name.length < 6 -> {
                isUserInputValid = false
                et_full_name.error = getString(R.string.full_name_invalid)
            }
            email.isEmpty() -> {
                isUserInputValid = false
                et_address_mail.error = getString(R.string.email_not_empty)
            }
            !email.isValidEmail() -> {
                isUserInputValid = false
                et_address_mail.error = getString(R.string.email_not_valid)
            }
        }

        return isUserInputValid
    }

    private fun isPhoneNumberAndPasswordValid(): Boolean {
        val phoneNumber = et_phone_number.editableText.toString()
        val password = et_password.editableText.toString()
        var isUserInputValid = true
        when {
            password.isEmpty() -> {
                isUserInputValid = false
                et_password.error = getString(R.string.password_not_empty)
            }
            password.length < 6 -> {
                isUserInputValid = false
                et_password.error = getString(R.string.pasword_not_alid)
            }
            phoneNumber.isEmpty() -> {
                isUserInputValid = false
                et_phone_number.error = getString(R.string.phone_number_empty)
            }
            !phoneNumber.isValidMobileNumber() -> {
                isUserInputValid = false
                et_phone_number.error = getString(R.string.phone_number_invalid)
            }
        }
        return isUserInputValid
    }


    private fun isCodeValid(): Boolean {
        val code = et_code.editableText.toString()
        return code.isEmpty() || code.length != 6
    }

}