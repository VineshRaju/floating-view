package xyz.vinesh.floatingviewdemo

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import xyz.vinesh.floatingview.FloatingView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val floatingView = FloatingView(R.layout.flag, 150, 100)
                .attachOn(activity = this, yPos = 100, gravity = Gravity.END xor Gravity.TOP)

        floatingView.onClick = {
            Toast.makeText(this, "Pressed Flag", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, Main2Activity::class.java))

        }
        tvHello.setOnClickListener {
            Toast.makeText(this, "Pressed text", Toast.LENGTH_SHORT).show()
        }
    }

}
