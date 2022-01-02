package com.example.satorsquare

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.satorsquare.databinding.ActivityMainBinding
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {
    private var squareText = """
    SATOR
    AREPO
    TENET
    OPERA
    ROTAS
""".trimIndent().split("\\n".toRegex())

    private lateinit var binding: ActivityMainBinding
    private val scope = CoroutineScope(Job() + Dispatchers.Main)
    private var job = initTimeoutAsync()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSquareText()
        binding.buttonStart.setOnClickListener { handleAutoUpdate() }

    }

    private fun initTimeoutAsync(): Deferred<Unit> {
        return scope.async(start = CoroutineStart.LAZY) {
            while (isActive) {
                updateSquare()
                delay(1000)
            }
        }
    }

    private fun updateSquare() {
        squareText = squareText.reversed()
        setSquareText()
    }

    private fun setSquareText() {
        binding.textView.text = squareText.joinToString("\n")
    }

    private fun handleAutoUpdate() {
        if (job.isActive) {
            job.cancel()
            job = initTimeoutAsync()
            binding.buttonStart.text = resources.getText(R.string.button_start)
        } else {
            job.start()
            binding.buttonStart.text = resources.getText(R.string.button_stop)
        }
    }
}