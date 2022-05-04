package com.example.myapplication

import android.content.*
import android.os.Bundle
import android.text.method.*
import android.widget.*
import androidx.appcompat.app.*
import androidx.appcompat.widget.*
import com.android.volley.*
import com.android.volley.toolbox.*
import com.google.android.gms.auth.api.signin.*
import org.json.JSONObject
import androidx.core.app.ActivityCompat.*
import android.content.Intent
import android.net.ConnectivityManager
import androidx.core.app.*
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.GraphRequest
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task

class LoginActivity : AppCompatActivity(){

    lateinit var callbackManager: CallbackManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //val btnLogin:AppCompatButton=findViewById(R.id.btnLogin)
        val btnLogin=findViewById<AppCompatButton>(R.id.btnLogin)

        val emailinput:EditText=findViewById(R.id.EmailInput)
        val passwordinput:EditText=findViewById(R.id.PasswordInput)
        val showHide:ImageView=findViewById(R.id.showhidepasswordbtn)
        val signupbtn:TextView=findViewById(R.id.txtSignup)

        var v=false

        //Show & Hide Password :
        showHide.setOnClickListener(){
            if(v != true){
                v=true
                showHide.setBackgroundResource(R.drawable.hide)
                passwordinput.transformationMethod=HideReturnsTransformationMethod.getInstance()
            } else {
                v=false
                showHide.setBackgroundResource(R.drawable.view)
                passwordinput.transformationMethod=PasswordTransformationMethod.getInstance()
            }
        }

        //Register Button :
        signupbtn.setOnClickListener{
            val intent=Intent(this@LoginActivity,RegisterActivity::class.java)
            startActivity(intent)
        }

        //Login Button :
        btnLogin.setOnClickListener{
            if(checkInternet()){
                val email:String =emailinput.text.toString()
                val pass:String=passwordinput.text.toString()
                val url="https://doancoso1.000webhostapp.com/Operations/Authontification/Login.php"
                val params=HashMap<String,String>()
                params["email"]=email
                params["password"]=pass
                val jO=JSONObject(params as Map<*, *>)
                val rq:RequestQueue=Volley.newRequestQueue(this@LoginActivity)
                val jor=JsonObjectRequest(Request.Method.POST,url,jO,Response.Listener { res->
                    try {
                        if(res.getString("success").equals("1")){
                            val intent=Intent(this@LoginActivity,MainActivity::class.java)
                            intent.putExtra("UserName",res.getString("user"))
                            startActivity(intent)
                            emailinput.text.clear()
                            passwordinput.text.clear()
                        } else { alert("Message d'Erreur !",res.getString("message")) }

                    }catch (e:Exception){
                        alert("Message d'Erreur !",""+e.message)
                    }
                },Response.ErrorListener { err->
                    alert("Message d'Erreur !",""+err.message)
                })
                rq.add(jor)
            }else
                alert("Erreur :","No Network Connected !")
        }

    }

    private fun alert(title:String,message:String){
        val builder= AlertDialog.Builder(this@LoginActivity)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("Ok",{ dialogInterface: DialogInterface, i: Int -> }).create()
        builder.show()
    }



    private fun checkInternet():Boolean{
        val connManager:ConnectivityManager=this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val wifiConn=connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
        val mobilData=connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
        if(!wifiConn!!.isConnectedOrConnecting && !mobilData!!.isConnectedOrConnecting)
            return false
        else
            return true
    }

}