package mx.tec.vetapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.google.firebase.auth.FirebaseAuth
import mx.tec.vetapp.ui.theme.VetAppTheme
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : ComponentActivity() {

    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VetAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    FirebaseExample(this, auth = auth)
                }
            }
        }

        auth = Firebase.auth
    }

    public override  fun onStart(){
        super.onStart()

        val currentUser = auth.currentUser
        if(currentUser == null){
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FirebaseExample(activity : Activity? = null, auth : FirebaseAuth? = null){

    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        OutlinedTextField(
            value = login,
            onValueChange = {login = it},
            label = {Text("Login")}
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = {Text("Password")},
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )
        Button(
            onClick = {
                if(activity != null) {
                    auth?.signInWithEmailAndPassword(login,password)
                        ?.addOnCompleteListener(activity){task ->
                            if (task.isSuccessful) {
                                Toast.makeText(activity, "LOG IN SUCCESSFUL", Toast.LENGTH_SHORT).show()
                                val intent = Intent(activity, MenuActivity::class.java)
                                activity.startActivity(intent)

                            } else{
                                Toast.makeText(activity, "LOG IN FAILED: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                }
            }
        ) {
            Text("Login")
        }

        Button(
            onClick = {
                if (activity != null) {
                    auth?.createUserWithEmailAndPassword(login, password)
                        ?.addOnCompleteListener(activity) { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(activity, "SIGN UP SUCCESSFUL", Toast.LENGTH_SHORT)
                                    .show()
                            } else {
                                Toast.makeText(
                                    activity,
                                    "SIGN UP FAILED: ${task.exception?.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                }
            }
        )
        {
            Text("Sign up")
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    VetAppTheme {
        FirebaseExample()
    }
}