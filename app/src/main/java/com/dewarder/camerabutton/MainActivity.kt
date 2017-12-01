package com.dewarder.camerabutton

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<CameraButton>(R.id.camera_button).apply {
            setOnHoldEventListener(object : CameraButton.OnHoldEventListener {
                override fun onStart() {
                    Toast.makeText(this@MainActivity, "START", Toast.LENGTH_SHORT).show()
                }

                override fun onFinish() {
                    Toast.makeText(this@MainActivity, "FINISH", Toast.LENGTH_SHORT).show()
                }

                override fun onCancel() {
                    Toast.makeText(this@MainActivity, "CANCEL", Toast.LENGTH_SHORT).show()
                }
            })

            setOnTapEventListener {
                Toast.makeText(this@MainActivity, "TAP", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
