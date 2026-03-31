package com.example.firebaseproject

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.FirebaseFirestore

class UpdateCourse : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            FirebaseUI(
                LocalContext.current,
                intent.getStringExtra("courseName"),
                intent.getStringExtra("courseDuration"),
                intent.getStringExtra("courseDescription"),
                intent.getStringExtra("courseID")
            )
        }
    }
}

@Composable
fun FirebaseUI(
    context: Context,
    name: String?,
    duration: String?,
    description: String?,
    courseID: String?
) {

    val courseName = remember { mutableStateOf(name ?: "") }
    val courseDuration = remember { mutableStateOf(duration ?: "") }
    val courseDescription = remember { mutableStateOf(description ?: "") }

    Column {

        TextField(value = courseName.value, onValueChange = { courseName.value = it })
        TextField(value = courseDuration.value, onValueChange = { courseDuration.value = it })
        TextField(value = courseDescription.value, onValueChange = { courseDescription.value = it })

        Button(
            onClick = {
                val db = FirebaseFirestore.getInstance()

                val updated = Course(courseID, courseName.value, courseDuration.value, courseDescription.value)

                db.collection("Courses").document(courseID!!)
                    .set(updated)
                    .addOnSuccessListener {
                        Toast.makeText(context, "Updated", Toast.LENGTH_SHORT).show()
                        context.startActivity(Intent(context, CourseDetailsActivity::class.java))
                    }
            }
        ) {
            Text("Update")
        }
    }
}