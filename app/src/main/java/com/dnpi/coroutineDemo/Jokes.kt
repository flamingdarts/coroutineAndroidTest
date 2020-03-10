package com.dnpi.coroutineDemo

class Jokes {
    private val listOfJokes = listOf("What do you call it when an apple user looks you in the eye?",
    "Why should you never fart in an apple store?",
        "What did the iPhone salesman say to the customer?",
        "What do you call someone who has seen an iPhone being stolen?",
        "What should you call an iPhone charger?",
        "What happens if you put an iPhone in a blender?",
        "What do you call an iPhone that isn't kidding around?"
    )
    private val listOfAnswers = listOf("iContact!",
        "They don't have Windows!",
        "It's my way or the Huawei!",
        "iWitness!",
        "Apple Juice!",
        "You get Apple Juice!",
        "Dead Siri-ous!"
    )
    fun getJoke(index: Int) = listOfJokes[index]
    fun getAnswer(index: Int) = listOfAnswers[index]
}