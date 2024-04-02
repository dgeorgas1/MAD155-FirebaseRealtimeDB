package com.example.firebaserealtimedb

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    lateinit var editTextTeam: EditText
    lateinit var editTextWins: EditText
    lateinit var editTextLosses: EditText
    lateinit var editTextLeague: EditText

    lateinit var buttonAdd: Button

    lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editTextTeam = findViewById(R.id.editTextTeam)
        editTextWins = findViewById(R.id.editTextWins)
        editTextLosses = findViewById(R.id.editTextLosses)
        editTextLeague = findViewById(R.id.editTextLeague)

        buttonAdd = findViewById(R.id.buttonAdd)

        recyclerView = findViewById(R.id.RecyclerView)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager
        var adapter = com.example.firebaserealtimedb.adapter.Adapter(emptyList())
        recyclerView.adapter = adapter

        //Add divider
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                baseContext,
                layoutManager.orientation
            )
        )

        //Write to DB
        val database = Firebase.database
        val myRef = database.getReference("MLB")

        buttonAdd.setOnClickListener {
            val league = editTextLeague.text.toString()
            val team = editTextTeam.text.toString() +
                    " - W: " + editTextWins.text.toString() + ", L: " + editTextLosses.text.toString()
            val leagueRef = myRef.child(league)

            leagueRef.push().setValue(team)
        }

        //Read from DB
        myRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val teamsList = mutableListOf<String>()

                for (leagueSnapshot in snapshot.children) {
                    for (teamSnapshot in leagueSnapshot.children) {
                        val team = teamSnapshot.getValue(String::class.java)
                        team?.let {
                            teamsList.add(it)
                            Toast.makeText(applicationContext,"${editTextTeam.text} was added successfully!", Toast.LENGTH_LONG).show()
                        }
                    }
                }
                adapter.updateData(teamsList)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })
    }
}