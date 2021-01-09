package com.hb.batapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.hb.batapp.R
import com.hb.batapp.models.UserModel
import com.hb.batapp.utils.USERS
import kotlinx.android.synthetic.main.fragment_profile.*


class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetchCurrentUser()
    }


    private fun fetchCurrentUser() {
        val idUser = FirebaseAuth.getInstance().uid
        val reference = FirebaseDatabase.getInstance().getReference("$USERS$idUser")
        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(activity, getString(R.string.error_getting_user), Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val currentUser = snapshot.getValue(UserModel::class.java)
                currentUser?.let {
                    tv_profile_full_name.text = it.fullName
                }
            }
        })
    }


}