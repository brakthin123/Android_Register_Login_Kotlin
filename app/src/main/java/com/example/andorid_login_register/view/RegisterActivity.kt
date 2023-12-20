package com.example.andorid_login_register.view

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.KeyEvent
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.example.andorid_login_register.R
import com.example.andorid_login_register.data.RegisterBody
import com.example.andorid_login_register.data.ValidateEmailBody
import com.example.andorid_login_register.databinding.ActivityRegisterBinding
import com.example.andorid_login_register.repository.AuthRepository
import com.example.andorid_login_register.utils.APIService
import com.example.andorid_login_register.view_model.RegisterActivityViewModel
import com.example.andorid_login_register.view_model.RegisterActivityViewModelFactory

class RegisterActivity : AppCompatActivity(), View.OnClickListener, View.OnFocusChangeListener,
    View.OnKeyListener , TextWatcher {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var viewModel: RegisterActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // binding for text
        binding.editTextUsername.onFocusChangeListener = this
        binding.editTextEmail.onFocusChangeListener = this
        binding.editTextPassword.onFocusChangeListener = this
        binding.editTextConfirmPassword.onFocusChangeListener = this

        // Binding to Action
        binding.editTextConfirmPassword.setOnKeyListener(this)
        binding.editTextConfirmPassword.addTextChangedListener(this)
        binding.buttonRegister.setOnClickListener(this)
        viewModel = ViewModelProvider(this, RegisterActivityViewModelFactory(AuthRepository(APIService.getService
            ()), application)).get(RegisterActivityViewModel::class.java)

        setupObservers()
    }
    private fun setupObservers(){
        viewModel.getIsLoading().observe(this){
            binding.progressBar.isVisible = it
        }
        viewModel.getIsUniqueEmail().observe(this){
            if (validateEmail(shouldUpdateView = false)){
                if(it){
                    binding.emailTil.apply {
                        if(isErrorEnabled) isErrorEnabled = false
                        setStartIconDrawable(R.drawable.baseline_check_circle_24)
                        setStartIconTintList(ColorStateList.valueOf(Color.GREEN))
                    }
                }else{
                    binding.emailTil.apply {
                        if (startIconDrawable != null) startIconDrawable = null
                        isErrorEnabled = true
                        error = "Email is already taken"
                    }
                }
            }
        }
        viewModel.getErrorMessage().observe(this){
            //fullName ,email, password
            val formErrorKeys = arrayOf("name", "email", "password")
            val message = StringBuilder()
            it.map { entry ->
                if (formErrorKeys.contains(entry.key)){
                    when(entry.key){
                        "name" -> {
                            binding.fullNameTil.apply {
                                isErrorEnabled = true
                                error = entry.value
                            }
                        }
                        "email" -> {
                            binding.emailTil.apply {
                                isErrorEnabled = true
                                error = entry.value
                            }
                        }
                        "password" -> {
                            binding.passwordTil.apply {
                                isErrorEnabled = true
                                error = entry.value
                            }
                        }
                    }
                }else{
                    message.append(entry.value).append("\n")
                }
                if(message.isNotEmpty()){
                    AlertDialog.Builder(this)
                        .setIcon(R.drawable.baseline_back_hand_24)
                        .setTitle("INFORMATION")
                        .setMessage(message)
                        .setPositiveButton("OK"){dialog, _ -> dialog!!.dismiss()}
                        .show()
                }
            }
        }
        viewModel.getUser().observe(this){
            if(it != null){
                startActivity(Intent(this, WelcomeActivity::class.java))
            }
        }
    }
    private fun validateFullName(): Boolean {
        var errorMassage: String? = null
        val value: String = binding.editTextUsername.text.toString()
        if (value.isEmpty()) {
            errorMassage = "Full name is required"
        }
        if (errorMassage != null) {
            binding.fullNameTil.apply {
                isErrorEnabled = true
                error = errorMassage
            }
        }
        return errorMassage == null
    }
    private fun validateEmail(shouldUpdateView: Boolean = true): Boolean {
        var errorMassage: String? = null
        val value = binding.editTextEmail.text.toString()
        if (value.isEmpty()) {
            errorMassage = "Email is required"
        } else if (!Patterns.EMAIL_ADDRESS.matcher(value).matches()) {
            errorMassage = "Email address is invalid"
        }
        if (errorMassage != null && shouldUpdateView) {
            binding.emailTil.apply {
                isErrorEnabled = true
                error = errorMassage
            }
        }
        return errorMassage == null
    }
    private fun validatePassword(shouldUpdateView: Boolean = true): Boolean {
        var errorMassage: String? = null
        val value = binding.editTextPassword.text.toString()
        if (value.isEmpty()) {
            errorMassage = "Password is required"
        } else if (value.length < 6) {
            errorMassage = "Password must be 6 characters long"
        }
        if (errorMassage != null && shouldUpdateView) {
            binding.passwordTil.apply {
                isErrorEnabled = true
                error = errorMassage
            }
        }
        return errorMassage == null
    }
    private fun validateConfirmPassword(shouldUpdateView: Boolean = true): Boolean {
        var errorMassage: String? = null
        val value = binding.editTextConfirmPassword.text.toString()
        if (value.isEmpty()) {
            errorMassage = "Confirm password is required"
        } else if (value.length < 6) {
            errorMassage = "Confirm password must be 6 characters long"
        }
        if (errorMassage != null && shouldUpdateView) {
            binding.confirmPasswordTil.apply {
                isErrorEnabled = true
                error = errorMassage
            }
        }
        return errorMassage == null
    }
    private fun validatePasswordAndConfirmPassword(shouldUpdateView: Boolean = true): Boolean {
        var errorMassage: String? = null
        val password = binding.editTextPassword.text.toString()
        val confirmPassword = binding.editTextConfirmPassword.text.toString()
        if (password != confirmPassword) {
            errorMassage = "Confirm password doesn't match with password"
        }
        if (errorMassage != null && shouldUpdateView) {
            binding.confirmPasswordTil.apply {
                isErrorEnabled = true
                errorMassage = errorMassage
            }
        }
        return errorMassage == null
    }

    override fun onClick(view: View?) {
        if (view != null && view.id == R.id.buttonRegister)
            onSubmit()
    }

    // This function Focus on input
    override fun onFocusChange(view: View?, hasFocus: Boolean) {
        if (view != null) {
            when (view.id) {
                R.id.editTextUsername -> {
                    if (hasFocus) {
                        if (binding.fullNameTil.isErrorEnabled) {
                                binding.fullNameTil.isErrorEnabled = false
                        }
                    }else {
                        validateFullName()
                    }
                }
                R.id.editTextEmail -> {
                    if (hasFocus) {
                        if (binding.emailTil.isErrorEnabled) {
                            binding.emailTil.isErrorEnabled = false
                        }
                        }else {
                        if (validateEmail()) {
                            // do validation for its uniqueness
                            viewModel.validateEmailAddress(ValidateEmailBody(binding.editTextEmail.text!!.toString()))
                        }
                    }
                }
                R.id.editTextPassword -> {
                    if (hasFocus) {
                        if (binding.passwordTil.isErrorEnabled) {
                            binding.passwordTil.isErrorEnabled = false
                        }
                    }else {
                        if (validatePassword() && binding.editTextConfirmPassword.text!!.isNotEmpty() &&
                            validateConfirmPassword() && validatePasswordAndConfirmPassword()) {
                            if (binding.confirmPasswordTil.isErrorEnabled) {
                                binding.confirmPasswordTil.isErrorEnabled = false
                            }
                            binding.confirmPasswordTil.apply {
                                setStartIconDrawable(R.drawable.baseline_check_circle_24)
                                setStartIconTintList(ColorStateList.valueOf(Color.GREEN))
                            }
                        }
                    }
                }
                R.id.editTextConfirmPassword -> {
                    if (hasFocus) {
                        if (binding.confirmPasswordTil.isErrorEnabled) {
                            binding.confirmPasswordTil.isErrorEnabled = false
                        }
                    }else {
                        // confirm with icon check
                        if (validateConfirmPassword() && validatePassword() && validatePasswordAndConfirmPassword()) {
                            if (binding.passwordTil.isErrorEnabled) {
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

    override fun onKey(view: View?, keyCode: Int, keyEvent: KeyEvent?): Boolean {
            if (KeyEvent.KEYCODE_ENTER == keyCode && keyEvent!!.action == KeyEvent.ACTION_UP){
                // do registration
                onSubmit()
            }
        return false
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if (validatePassword(shouldUpdateView = false) && validateConfirmPassword(shouldUpdateView = false) && validatePasswordAndConfirmPassword(shouldUpdateView = false)){
            binding.confirmPasswordTil.apply {
                if (isErrorEnabled) isErrorEnabled = false
                setStartIconDrawable(R.drawable.baseline_check_circle_24)
                setStartIconTintList(ColorStateList.valueOf(Color.GREEN))
            }
        }else{
            if (binding.confirmPasswordTil.startIconDrawable != null)
                binding.confirmPasswordTil.startIconDrawable = null
        }
    }

    override fun afterTextChanged(s: Editable?) {}
    private fun onSubmit(){
        if (validate()){
            viewModel.registerUser(RegisterBody(binding.editTextUsername.text!!.toString(), binding.editTextEmail.text!!.toString(), binding.editTextPassword.text!!.toString()))
        }
    }
    private fun validate(): Boolean{
        var isValid = true
        if (!validateFullName()) isValid = false
        if (!validateEmail()) isValid = false
        if (!validatePassword()) isValid = false
        if (!validateConfirmPassword()) isValid = false
        if (isValid && !validatePasswordAndConfirmPassword()) isValid = false
        return isValid
    }
}