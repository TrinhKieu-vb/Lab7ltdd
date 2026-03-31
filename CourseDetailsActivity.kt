package com.example.firebaseproject

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.FirebaseFirestore
import androidx.compose.runtime.Composable
import androidx.compose.material.ExperimentalMaterialApi

class CourseDetailsActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val courseList = mutableStateListOf<Course?>()
            val db = FirebaseFirestore.getInstance()

            db.collection("Courses").get()
                .addOnSuccessListener {
                    for (d in it.documents) {
                        val c = d.toObject(Course::class.java)
                        c?.courseID = d.id
                        courseList.add(c)
                    }
                }

            FirebaseList(LocalContext.current, courseList)
        }
    }
}
@OptIn(ExperimentalMaterialApi::class)

@Composable
fun FirebaseList(context: Context, list: List<Course?>) {

    LazyColumn {
        itemsIndexed(list) { _, item ->
            Card(
                modifier = Modifier.padding(8.dp),
                elevation = 6.dp,
                onClick = {
                    val i = Intent(context, UpdateCourse::class.java)
                    i.putExtra("courseID", item?.courseID)
                    i.putExtra("courseName", item?.courseName)
                    i.putExtra("courseDuration", item?.courseDuration)
                    i.putExtra("courseDescription", item?.courseDescription)
                    context.startActivity(i)
                }
            ) {
                Column(Modifier.padding(8.dp)) {
                    Text(text = item?.courseName ?: "")
                    Text(text = item?.courseDuration ?: "")
                    Text(text = item?.courseDescription ?: "")
                }
            }
        }
    }
}