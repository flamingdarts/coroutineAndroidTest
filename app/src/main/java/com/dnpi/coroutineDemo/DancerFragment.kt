package com.dnpi.coroutineDemo


import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.Voice
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_dancer.*
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.HashSet
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
        giveAnswerButton.isEnabled = false
        tellJokeButton.setOnClickListener {
            tellJoke()
        }
        giveAnswerButton.setOnClickListener {
            giveAnswer()
        }
    }

    private fun giveAnswer() {
        if (answerIndex >= 0) {
            val answer = getAnswerBlocking()
            text_under_dancer.text = answer
            textToSpeech.speak(answer, TextToSpeech.QUEUE_FLUSH, null, "")
        }
        tellJokeButton.isEnabled = true
        giveAnswerButton.isEnabled = false
    }

    private fun tellJoke() {
        val joke = getJokeBlocking()
        text_under_dancer.text = joke
        textToSpeech.speak(joke, TextToSpeech.QUEUE_FLUSH, null, "")
        tellJokeButton.isEnabled = false
        giveAnswerButton.isEnabled = true
    }

    private fun getAnswerBlocking(): String {
        Thread.sleep(3000)
        return Jokes().getAnswer(answerIndex)

    }

    private fun getJokeBlocking(): String {
        Thread.sleep(3000)
        answerIndex = Random.nextInt(0, 6)
        return Jokes().getJoke(answerIndex)
    }

    private fun setupTTS() {
        textToSpeech = TextToSpeech(requireContext(), TextToSpeech.OnInitListener {
            if (it == TextToSpeech.SUCCESS) {
                val result = textToSpeech.setLanguage(Locale.UK)
                val a = HashSet<String>()
                a.add("male")
                textToSpeech.voice = Voice("en-us-x-sfg#male_2-local", Locale("en","US"),400,200,true,a)

                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("TTS", "The Language specified is not supported!")
                } else {
                    tellJokeButton.isEnabled = true
                }
            } else {
                Log.e("TTS", "Initilization Failed!")
                tellJokeButton.isEnabled = false
            }
        })
    }

    private fun setAnimation() {
        dancer_animation.playAnimation()
        println("play animation in ${Thread.currentThread().name}")
    }

    override fun onDestroy() {
        textToSpeech.stop()
        textToSpeech.shutdown()
        super.onDestroy()
    }
}
