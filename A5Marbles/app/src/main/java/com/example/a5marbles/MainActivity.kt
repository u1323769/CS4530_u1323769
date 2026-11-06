package com.example.a5marbles

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme

class MainActivity : ComponentActivity(), SensorEventListener {

    private val viewModel: MarbleViewModel by viewModels()

    private lateinit var sensorManager: SensorManager
    private var sensor: Sensor? = null
    private var usingAccelerometer = false

    // low-pass filter state, only used with the accelerometer fallback (emulator)
    private val filtered = FloatArray(3)
    private val alpha = 0.8f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        val gravity = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY)
        sensor = gravity ?: sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        usingAccelerometer = gravity == null && sensor != null
        viewModel.setSensorAvailable(sensor != null)

        setContent {
            MaterialTheme {
                MarbleScreen(viewModel)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        sensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_GAME)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
        viewModel.onPaused()
    }

    override fun onSensorChanged(event: SensorEvent) {
        val gx: Float
        val gy: Float
        if (usingAccelerometer) {
            // smooth the raw accelerometer so it behaves like the gravity sensor
            for (i in 0..2) {
                filtered[i] = alpha * filtered[i] + (1 - alpha) * event.values[i]
            }
            gx = filtered[0]
            gy = filtered[1]
        } else {
            gx = event.values[0]
            gy = event.values[1]
        }

        // Sensor axes: x right, y UP. Compose offsets: x right, y DOWN.
        // The sensor reports the reaction to gravity (opposite sign), so:
        //   tilt right edge down -> sensor x < 0 -> marble should go right  => -x
        //   tilt top edge down   -> sensor y < 0 -> marble should go up     => y
        viewModel.onTilt(ax = -gx, ay = gy, timestampNs = event.timestamp)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit
}