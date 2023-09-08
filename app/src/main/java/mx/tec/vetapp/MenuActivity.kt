package mx.tec.vetapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import mx.tec.vetapp.ui.theme.VetAppTheme

class MenuActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            VetAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Menu(this)
                }
            }
        }
    }
}

@Composable
fun Menu(activity: Activity? = null, auth: FirebaseAuth? = null) {
    val db = Firebase.firestore

    var animalList by remember { mutableStateOf<List<String>>(emptyList()) }

    if (activity != null) {
        db.collection("animals")
            .get()
            .addOnSuccessListener { snapshot ->
                val dataList = snapshot.documents.mapNotNull { document ->
                    val name = document.getString("name")
                    val age = document.getString("age")
                    val weight = document.getString("weight")

                    "Name: $name    Age: $age   Weight: $weight"
                }
                animalList = dataList
            }
            .addOnFailureListener { exception ->
                Log.e("FIREBASE-TEST", "${exception.message}")
            }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        LazyColumn {
            items(animalList) { animal ->
                Text(text = animal)
            }
        }
        Button(
            onClick = {
                val intent = Intent(activity, DetailActivity::class.java)
                activity?.startActivity(intent)
            }
        ) {
            Text(text = "Register New Animal")
        }
    }
}
