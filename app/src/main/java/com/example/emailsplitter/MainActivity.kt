package com.example.emailsplitter

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.emailsplitter.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(savedInstanceState == null) {
            val fTrans = supportFragmentManager.beginTransaction()
                fTrans.replace(R.id.fl_frag_container, InputFragment(), "input_frag")
                fTrans.commit()
        }

    }
}