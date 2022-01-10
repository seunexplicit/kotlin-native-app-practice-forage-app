package com.example.forageapp

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        supportFragmentManager.fragments.forEach { fragment ->
            Log.d("Main Activity", "${findViewById<View?>(R.id.loader)?.toString()} --- ${fragment.toString()}")
            if(fragment is AddFragment){
                Log.d("Main Activity 2", "${findViewById<View?>(R.id.loader)?.toString()} -- ${fragment.progressLoader}")
                fragment.progressLoader = findViewById(R.id.loader)
            }
        }
        return super.onCreateView(name, context, attrs)

    }

}