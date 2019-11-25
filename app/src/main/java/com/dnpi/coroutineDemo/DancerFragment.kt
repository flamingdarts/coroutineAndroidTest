package com.dnpi.coroutineDemo


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_dancer.*
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext


class DancerFragment : Fragment(), CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private var job = Job()

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
        setDancersName()

    }
    private fun setAnimation() {
        dancer_animation.playAnimation()
        println("play animation in ${Thread.currentThread().name}")
    }

    private suspend fun getDancersName(): String {
        delay(4000)
        println("return Name in thread: ${Thread.currentThread().name}")
        return "Jason"
    }

    private fun setDancersName() {
        launch {
            text_under_dancer.text = withContext(Dispatchers.IO) {
                getDancersName()
            }
        }
        println("This part in setDancersName in thread ${Thread.currentThread().name} is not blocked")
    }


}
