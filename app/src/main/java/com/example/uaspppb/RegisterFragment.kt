package com.example.uaspppb

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.uas_ppapb.LoginFragment
import com.example.uaspppb.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPreferences = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        with(binding) {
            regist.setOnClickListener {
                val username = txtUser.text.toString()
                val password = txtPassword.text.toString()
                val email = txtEmail.text.toString()
                val phone = txtPhone.text.toString()

                if (username.isNotEmpty() && password.isNotEmpty() && email.isNotEmpty() && phone.isNotEmpty()) {
                    // Menyimpan data pengguna di SharedPreferences
                    saveUserData(username, email, password, phone)

                    // Mengarahkan ke halaman login setelah registrasi berhasil
                    Toast.makeText(requireContext(), "Registrasi berhasil", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Input tidak boleh kosong", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Menyimpan data pengguna di SharedPreferences
    private fun saveUserData(username: String, email: String, password: String, phone: String) {
        val editor = sharedPreferences.edit()
        editor.putString("USERNAME", username)
        editor.putString("EMAIL", email)
        editor.putString("PASSWORD", password)
        editor.putString("PHONE", phone)
        editor.apply()
    }

    override fun onResume() {
        super.onResume()
        // Mengosongkan input setelah halaman registrasi dilihat
        with(binding) {
            txtUser.text?.clear()
            txtEmail.text?.clear()
            txtPhone.text?.clear()
            txtPassword.text?.clear()
        }
    }
}
