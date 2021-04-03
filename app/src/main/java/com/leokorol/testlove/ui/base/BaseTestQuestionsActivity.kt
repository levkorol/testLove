package com.leokorol.testlove.ui.base

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.leokorol.testlove.R
import com.leokorol.testlove.TestApp
import com.leokorol.testlove.data_base.AuthManagerTest
import com.leokorol.testlove.model.AnswerVariant
import com.leokorol.testlove.ui.menu.togetherTests.MenuTestsActivity
import com.leokorol.testlove.ui.menu.togetherTests.recycler_view.AnswerVariantAdapter
import com.leokorol.testlove.ui.menu.togetherTests.testsTexts.QuestionWithVariants
import com.leokorol.testlove.utils.replaceActivity
import com.leokorol.testlove.utils.showToast


open class BaseTestQuestionsActivity(
    private val _questions: Array<QuestionWithVariants>,
    private val _layoutId: Int,
    private val _backgroundResource: Int,
    private val _answersBranch: String,
    private val numberTest: Int,
    private val lastQuestion: String,
) : AppCompatActivity() {
    private lateinit var _recyclerView: RecyclerView
    private lateinit var _textViewQuestionText: TextView
    private lateinit var _textViewNumberQuestion: TextView
    private var _allAnswerVariants: Array<Array<AnswerVariant>> =
        Array(_questions.size) { emptyArray() }

    //private lateinit var _allAnswerVariants: Array<Array<AnswerVariant>>
    private var _currentQuestionIndex = 0
    private lateinit var mAdView: AdView

    private val countOfCheckedAnswers: Int
        get() {
            var result = 0
            for (element in _allAnswerVariants[_currentQuestionIndex]) {
                if (element.isChecked) {
                    result++
                }
            }
            return result
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(_layoutId)
        _recyclerView = findViewById(R.id.recyclerView)
        _textViewQuestionText = findViewById(R.id.textViewQuestionText)
        _textViewNumberQuestion = findViewById(R.id.textViewNumberQuestion)
        val llm = LinearLayoutManager(this)
        _recyclerView.layoutManager = llm

        _currentQuestionIndex = TestApp.sharedPref?.getInt(lastQuestion, 0) ?: 0

        goToQuestion(0)

        initAds()
    }

    private fun initAds() {
        MobileAds.initialize(this) {}
        val listId = listOf("AC15A1685814DB24010455FD90B6BAC9")
        val configuration = RequestConfiguration
            .Builder()
            .setTestDeviceIds(listId)
            .build()
        MobileAds.setRequestConfiguration(configuration)

        mAdView = findViewById(R.id.adViewTest)
        val adRequest = AdRequest.Builder()
            .build()

        mAdView.loadAd(adRequest)
    }

    fun goToMenuActivity(view: View?) {
        replaceActivity(MenuTestsActivity())
    }

    fun goToNextQuestion(view: View?) {
        if (countOfCheckedAnswers == 0) {
            showToast("Выберите 1-2 варианта ответа")
        } else {
            val answerSet = mutableSetOf<String>()
            for (i in _allAnswerVariants[_currentQuestionIndex].indices) {
                if (_allAnswerVariants[_currentQuestionIndex][i].isChecked) {
                    answerSet.add(i.toString())
                }
            }
            AuthManagerTest.saveAnswer(numberTest, _currentQuestionIndex, answerSet)

            if (_currentQuestionIndex == _questions.size - 1) {
                AuthManagerTest.copyAnswersFromPrefsToDatabase(numberTest, _questions.size)
                val intent = Intent(this, MenuTestsActivity::class.java)
                startActivity(intent)
                showToast("Вы прошли тест. Если ваш партнер тоже прошел этот тест смотрите результаты или ждите.")

            } else {
                _currentQuestionIndex++
                TestApp.sharedPref?.edit()?.putInt(lastQuestion, _currentQuestionIndex)?.apply()
                goToQuestion(_currentQuestionIndex)
            }
        }
    }

    private fun goToQuestion(numberQuestion: Int) {
        val ava = AnswerVariantAdapter(_allAnswerVariants[_currentQuestionIndex])
        _recyclerView.adapter = ava
        ava.notifyDataSetChanged()
        _textViewQuestionText.text = _questions[numberQuestion].question
        _textViewNumberQuestion.text =
            (_currentQuestionIndex + 1).toString() + "/" + _questions.size
    }

    companion object {
        const val ANSWERS = "answers"
        const val ANSWERS_2 = "answers2"
        const val ANSWERS_3 = "answers3"
    }

    init {
        _allAnswerVariants = Array(_questions.size) { emptyArray() }
        for (iQuestion in _questions.indices) {
            val answerVariants = _questions[iQuestion].answerVariants
            _allAnswerVariants[iQuestion] =
                Array(answerVariants.size) { i -> AnswerVariant(answerVariants[i]) }
        }
    }
}