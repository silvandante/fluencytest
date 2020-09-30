package com.walker.fluencytest.ui.auth

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.walker.fluencytest.FluenyTestApplication
import com.walker.fluencytest.R
import com.walker.fluencytest.data.constants.AppConstants
import com.walker.fluencytest.data.models.auth.AuthData
import com.walker.fluencytest.ui.home.FeedDeckActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    val READ_EXTERNAL_STORAGE = 101
    val WRITE_EXTERNAL_STORAGE = 105


    private lateinit var authViewModel: AuthViewModel
    private var grantedPermissions = false


    private fun checkForPermission(permission: String, name: String, requestCode: Int){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            when{
                ContextCompat.checkSelfPermission(applicationContext, permission) == PackageManager.PERMISSION_GRANTED -> {
                    grantedPermissions = true
                    handleLogin()
                }
                ContextCompat.checkSelfPermission(applicationContext, permission) == PackageManager.PERMISSION_DENIED -> {
                    grantedPermissions = false

                    progress_bar_login_loading.visibility = View.GONE
                    buttonLogin.visibility = View.VISIBLE

                    val builder = AlertDialog.Builder(this, R.style.AlertDialog)
                    builder.setTitle("Permissão requeridas")
                    builder.setMessage("Você precisa aceitar da permissão "+name+" para usar esse APP")
                        .setPositiveButton("OK") { dialog, id ->
                            ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
                            Toast.makeText(
                                this,
                                "Mude o status da permissão para permitir",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        .setNegativeButton("NÃO QUERO") { dialog, id ->
                            Toast.makeText(
                                this,
                                "Você não poderá usar o app sem as permissões.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    val dialog = builder.create()

                    dialog.show()

                }
                shouldShowRequestPermissionRationale(permission) ->requestPermissionRationaleExplanation(permission, name, requestCode )

                else -> ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
            }
        }
    }

    fun askPermissions(){
        grantedPermissions = false

        checkForPermission(Manifest.permission.READ_EXTERNAL_STORAGE, "Ler arquivos", READ_EXTERNAL_STORAGE)
        checkForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, "Gravar arquivos", WRITE_EXTERNAL_STORAGE)




    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        fun innerCheck(name: String) {
            if(grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED){

            }else{
                grantedPermissions = true
                handleLogin()
            }
        }

        when (requestCode) {
            READ_EXTERNAL_STORAGE -> innerCheck("READ_EXTERNAL_STORAGE")
            WRITE_EXTERNAL_STORAGE -> innerCheck("WRITE_EXTERNAL_STORAGE")
        }
    }


    fun requestPermissionRationaleExplanation (permission: String, name: String, requestCode: Int) {
        val builder = AlertDialog.Builder(this,R.style.AlertDialog )
        builder.setTitle("Permissão requerida")
        builder.setMessage("Esse app precisa da permissão "+name+" para continuar.")
            .setPositiveButton("OK") { dialog, id ->
                ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
            }
        val dialog = builder.create()

        dialog.show()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        authViewModel = ViewModelProvider(this).get(AuthViewModel::class.java)

        val myApp = this.applicationContext as FluenyTestApplication


        // Inicializa eventos
        setListeners();
        observe(myApp)


    }


    override fun onClick(v: View) {
        if (v.id == R.id.buttonLogin) {
            progress_bar_login_loading.visibility = View.VISIBLE
            buttonLogin.visibility = View.GONE
            askPermissions()
        }
    }

    /**
     * Inicializa os eventos de click
     */
    private fun setListeners() {
        buttonLogin.setOnClickListener(this)
    }


    /**
     * Pega o usuário associado a esse token
     */
    private fun getUser(token: String) {
        authViewModel.getUser(token)
    }

    /**
     * Observa ViewModel
     */
    private fun observe(myApp: FluenyTestApplication) {
        authViewModel.token.observe(this, Observer {

            if (it != null) {
                getUser(it)
            } else {
                progress_bar_login_loading.visibility = View.GONE
                buttonLogin.visibility = View.VISIBLE
                if(myApp.getIsConnected())
                    Toast.makeText(this, "Login falhou. Verifique seu email e/ou senha.", Toast.LENGTH_SHORT).show()
                else
                    Toast.makeText(this, "Verifique sua conexão com a internet", Toast.LENGTH_SHORT).show()
            }
        })
        authViewModel.userId.observe(this, Observer {
            if(it!=null){
                var intent = Intent(this, FeedDeckActivity::class.java)
                intent.putExtra(AppConstants.SHARED_PREFERENCES.USER_ID, it.toString())
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
                this.finish()
            } else {
                progress_bar_login_loading.visibility = View.GONE
                buttonLogin.visibility = View.VISIBLE
                if(myApp.getIsConnected())
                    Toast.makeText(this, "Login falhou. Verifique seu email e/ou senha.", Toast.LENGTH_SHORT).show()
                else
                    Toast.makeText(this, "Verifique sua conexão com a internet", Toast.LENGTH_SHORT).show()
            }
        })
    }

    /**
     * Autentica usuário
     */
    private fun handleLogin() {
        val email = edit_email_text.text.toString()
        val password = edit_password_text.text.toString()

        val authData = AuthData(email=email,password = password)

        authViewModel.doLogin(authData)
    }

}