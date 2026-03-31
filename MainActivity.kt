package com.example.firebaseproject

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.TopAppBar
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : ComponentActivity() {
    @SuppressLint("UnrememberedMutableState")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Scaffold(
                topBar = {
                    TopAppBar(
                        backgroundColor = Color(0xFF4CAF50),
                        title = {
                            Text(
                                text = "GFG",
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                color = Color.White
                            )
                        }
                    )
                }
            ) { innerPadding ->
                FirebaseUI(LocalContext.current)
            }
        }
    }
}

@Composable
fun FirebaseUI(context: Context) {

    val courseID = remember { mutableStateOf("") }
    val courseName = remember { mutableStateOf("") }
    val courseDuration = remember { mutableStateOf("") }
    val courseDescription = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        TextField(
            value = courseName.value,
            onValueChange = { courseName.value = it },
            placeholder = { Text("Enter course name") },
            modifier = Modifier.padding(16.dp).fillMaxWidth()
        )

        TextField(
            value = courseDuration.value,
            onValueChange = { courseDuration.value = it },
            placeholder = { Text("Enter course duration") },
            modifier = Modifier.padding(16.dp).fillMaxWidth()
        )

        TextField(
            value = courseDescription.value,
            onValueChange = { courseDescription.value = it },
            placeholder = { Text("Enter course description") },
            modifier = Modifier.padding(16.dp).fillMaxWidth()
        )

        Button(
            onClick = {
                if (TextUtils.isEmpty(courseName.value)) {
                    Toast.makeText(context, "Enter name", Toast.LENGTH_SHORT).show()
                } else {
                    addDataToFirebase(
                        courseID.value,
                        courseName.value,
                        courseDuration.value,
                        courseDescription.value,
                        context
                    )
                }
            },
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            Text("Add Data")
        }

        Button(
            onClick = {
                context.startActivity(Intent(context, CourseDetailsActivity::class.java))
            },
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            Text("View Courses")
        }
    }
}

fun addDataToFirebase(
    courseID: String,
    courseName: String,
    courseDuration: String,
    courseDescription: String,
    context: Context
) {
    val db = FirebaseFirestore.getInstance()

    val course = Course(courseID, courseName, courseDuration, courseDescription)

    db.collection("Courses")
        .add(course)
        .addOnSuccessListener {
            Toast.makeText(context, "Added", Toast.LENGTH_SHORT).show()
        }
        .addOnFailureListener {
            Toast.makeText(context, "Fail", Toast.LENGTH_SHORT).show()
        }
}