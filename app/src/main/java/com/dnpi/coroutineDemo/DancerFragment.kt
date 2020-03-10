package com.dnpi.coroutineDemo


import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_dancer.*
import kotlinx.coroutines.*
import org.w3c.dom.Text
import java.util.*
import kotlin.coroutines.CoroutineContext
import kotlin.random.Random


class DancerFragment : Fragment(), CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private var job = Job()
    private var answerIndex: Int = -1

    private lateinit var textToSpeech: TextToSpeech


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dancer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAnimation()
        setupTTS()
        giveAnswer.isEnabled = false
        tellJoke.setOnClickListener {
            answerIndex = Random.nextInt(0,2)
            val joke = Jokes().getJoke(answerIndex)
            text_under_dancer.text = joke
            textToSpeech.speak(joke,TextToSpeech.QUEUE_FLUSH,null,"")
            tellJoke.isEnabled = false
            giveAnswer.isEnabled = true
        }
        giveAnswer.setOnClickListener {
            if (answerIndex >= 0) {
                val answer = Jokes().getAnswer(answerIndex)
                text_under_dancer.text = answer
            }
            tellJoke.isEnabled = true
            giveAnswer.isEnabled = false
        }

        val childJob = launch {
            text_under_dancer.text = getDancersName()

            println("From launch: I am running in: ${Thread.currentThread().name}")
        }
        childJob.cancel()

    }

    private fun setupTTS() {
        textToSpeech = TextToSpeech(requireContext(), TextToSpeech.OnInitListener {
            if (it == TextToSpeech.SUCCESS) {
                val result = textToSpeech.setLanguage(Locale.UK)

                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("TTS", "The Language specified is not supported!")
                } else {
                    tellJoke.isEnabled = true
                }
            } else {
                Log.e("TTS", "Initilization Failed!")
                tellJoke.isEnabled = false
            }
        })
    }

    private fun setAnimation() {
        dancer_animation.playAnimation()
        println("play animation in ${Thread.currentThread().name}")
    }

    private suspend fun getDancersName(): String {
        return withContext(Dispatchers.IO) {
            delay(5000)
            println("thread I am running in: ${Thread.currentThread().name}")
            giveName()
        }

    }

    private fun giveName(): String {
        return "Jacker"
    }

    override fun onDestroy() {
        textToSpeech.stop()
        textToSpeech.shutdown()
        super.onDestroy()
    }
}
