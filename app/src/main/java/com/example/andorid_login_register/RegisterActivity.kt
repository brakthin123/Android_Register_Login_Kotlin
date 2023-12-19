package com.example.andorid_login_register

import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.KeyEvent
import android.view.View
import com.example.andorid_login_register.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity(), View.OnClickListener, View.OnFocusChangeListener, View.OnKeyListener {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.editTextUsername.onFocusChangeListener = this
        binding.editTextEmail.onFocusChangeListener = this
        binding.editTextPassword.onFocusChangeListener = this
        binding.editTextConfirmPassword.onFocusChangeListener = this
    }

    private fun validateFullName(): Boolean {
        var errorMassage: String? = null
        val value: String = binding.editTextUsername.text.toString()
        if(value.isEmpty()){
            errorMassage = "Full name is required"
        }
        if(errorMassage != null){
            binding.fullNameTil.apply {
                isErrorEnabled = true
                error = errorMassage
            }
        }
        return errorMassage == null
    }

    private fun validateEmail(): Boolean {
        var errorMassage: String? = null
        val value = binding.editTextEmail.text.toString()
        if (value.isEmpty()){
            errorMassage = "Email is required"
        }else if(!Patterns.EMAIL_ADDRESS.matcher(value).matches()){
            errorMassage = "Email address is invalid"
        }
        if(errorMassage != null){
            binding.emailTil.apply {
                isErrorEnabled = true
                error = errorMassage
            }
        }
        return errorMassage == null
    }

    private fun validatePassword(): Boolean {
        var errorMassage: String? = null
        val value = binding.editTextPassword.text.toString()
        if (value.isEmpty()){
            errorMassage = "Password is required"
        }else if(value.length < 6 ){
            errorMassage = "Password must be 6 characters long"
        }
        if(errorMassage != null){
            binding.passwordTil.apply {
                isErrorEnabled = true
                error = errorMassage
            }
        }
        return errorMassage == null
    }

    private fun validateConfirmPassword(): Boolean{
        var errorMassage: String? = null
        val value = binding.editTextConfirmPassword.text.toString()
        if(value.isEmpty()){
            errorMassage = "Confirm password is required"
        }else if(value.length < 6 ){
            errorMassage = "Confirm password must be 6 characters long"
        }
        if(errorMassage != null){
            binding.confirmPasswordTil.apply {
                isErrorEnabled = true
                error = errorMassage
            }
        }
        return errorMassage == null
    }

    private fun validatePasswordAndConfirmPassword(): Boolean {
        var error: String? = null
        val password = binding.editTextPassword.text.toString()
        val confirmPassword = binding.editTextConfirmPassword.text.toString()
        if(password != confirmPassword){
            error = "Confirm password doesn't match with password"
        }
        return error == null
    }

    override fun onClick(view: View?) {
    }

    override fun onFocusChange(view: View?, hasFocus: Boolean) {
        if(view != null){
            when(view.id){
                R.id.editTextUsername -> {
                    if(hasFocus){
                        if(binding.fullNameTil.isErrorEnabled){
                            binding.fullNameTil.isErrorEnabled = false
                        }else{
                            validateFullName()
                        }
                    }
                }
                R.id.editTextEmail -> {
                    if(hasFocus){
                        if(binding.emailTil.isErrorEnabled){
                            binding.emailTil.isErrorEnabled = false
                        }else{
                            if (validateEmail()){
                                // do validation for its uniqueness
                            }
                        }
                    }
                }
                R.id.editTextPassword -> {
                    if(hasFocus){
                        if(binding.passwordTil.isErrorEnabled){
                            binding.passwordTil.isErrorEnabled = false
                        }else{
                            if (validatePassword() && binding.editTextConfirmPassword.text!!.isNotEmpty() && validateConfirmPassword() && validatePasswordAndConfirmPassword()){
                                if (binding.confirmPasswordTil.isErrorEnabled){
                                    binding.confirmPasswordTil.isErrorEnabled = false
                                }
                                binding.confirmPasswordTil.apply {
                                    setStartIconDrawable(R.drawable.baseline_check_circle_24)
                                    setStartIconTintList(ColorStateList.valueOf(Color.GREEN))
                                }
                            }
                        }
                    }
                }
                R.id.editTextConfirmPassword -> {
                    if(hasFocus){
                        if(binding.confirmPasswordTil.isErrorEnabled){
                            binding.confirmPasswordTil.isErrorEnabled = false
                        }else{
                            if (validateConfirmPassword() && validatePassword() && validatePasswordAndConfirmPassword()){
                                if (binding.passwordTil.isErrorEnabled){
                                    binding.passwordTil.isErrorEnabled = false
                                }
                                binding.confirmPasswordTil.apply {
                                    setStartIconDrawable(R.drawable.baseline_check_circle_24)
                                    setStartIconTintList(ColorStateList.valueOf(Color.GREEN))
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onKey(view: View?, event: Int, keyEvent: KeyEvent?): Boolean {
        return false
    }
}