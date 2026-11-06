package com.example.a5marbles
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.math.exp
import kotlin.math.max
import kotlin.math.min

/** Everything the UI needs to draw the marble. Positions are in dp. */
data class MarbleState(
    val x: Float = 0f,
    val y: Float = 0f,
    val sensorAvailable: Boolean = true
)

/**
 * Holds the marble's physics. It knows nothing about Android sensors or Compose:
 * the Activity feeds it tilt values, the UI feeds it the size of the play area.
 */
class MarbleViewModel : ViewModel() {

    companion object {
        const val MARBLE_SIZE_DP = 40f
        private const val ACCEL_SCALE = 150f   // dp/s² per 1 m/s² of tilt
        private const val FRICTION = 1.5f      // higher = slows down faster
        private const val BOUNCE = 0.3f        // fraction of speed kept after hitting a wall
        private const val MAX_DT = 0.05f       // ignore huge time gaps (e.g. after resume)
    }

    private val _state = MutableStateFlow(MarbleState())
    val state: StateFlow<MarbleState> = _state.asStateFlow()

    private var x = 0f
    private var y = 0f
    private var vx = 0f
    private var vy = 0f
    private var maxX = 0f
    private var maxY = 0f
    private var lastTimestampNs = 0L
    private var centered = false

    fun setSensorAvailable(available: Boolean) {
        _state.update { it.copy(sensorAvailable = available) }
    }

    /** Called by the UI when the play area size is known (marble size already subtracted). */
    fun setBounds(maxXDp: Float, maxYDp: Float) {
        maxX = max(0f, maxXDp)
        maxY = max(0f, maxYDp)
        if (!centered) {
            x = maxX / 2f
            y = maxY / 2f
            centered = true
        }
        x = x.coerceIn(0f, maxX)
        y = y.coerceIn(0f, maxY)
        publish()
    }

    /** Call from onPause so the first event after resume doesn't produce a big dt. */
    fun onPaused() {
        lastTimestampNs = 0L
    }

    /**
     * @param ax acceleration along screen x (right is positive)
     * @param ay acceleration along screen y (down is positive, like Compose offsets)
     */
    fun onTilt(ax: Float, ay: Float, timestampNs: Long) {
        if (lastTimestampNs == 0L) {
            lastTimestampNs = timestampNs
            return
        }
        val dt = min((timestampNs - lastTimestampNs) / 1_000_000_000f, MAX_DT)
        lastTimestampNs = timestampNs

        // velocity += dt * scale * gravity
        vx += dt * ACCEL_SCALE * ax
        vy += dt * ACCEL_SCALE * ay

        // friction (frame-rate independent exponential decay)
        val damping = exp(-FRICTION * dt)
        vx *= damping
        vy *= damping

        // position += dt * velocity
        x += dt * vx
        y += dt * vy

        // keep inside the screen; lose most of the speed on impact
        if (x < 0f) { x = 0f; vx = -vx * BOUNCE }
        if (x > maxX) { x = maxX; vx = -vx * BOUNCE }
        if (y < 0f) { y = 0f; vy = -vy * BOUNCE }
        if (y > maxY) { y = maxY; vy = -vy * BOUNCE }

        publish()
    }

    private fun publish() {
        _state.update { it.copy(x = x, y = y) }
    }
}