package com.uma.bankakstask.ui.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.uma.bankakstask.R
import com.uma.bankakstask.data.api.ApiHelper
import com.uma.bankakstask.data.api.RetrofitBulder
import com.uma.bankakstask.data.model.Field
import com.uma.bankakstask.data.repository.DataManager
import com.uma.bankakstask.databinding.ActivityMainBinding
import com.uma.bankakstask.utils.Status.*
import java.util.regex.Matcher
import java.util.regex.Pattern
import java.util.regex.PatternSyntaxException


class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private lateinit var viewModel: MainActivityViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var optionsList: Array<String>
    private var fieldsList: ArrayList<Field> = ArrayList()
    private var number_of_fields: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setViewModel()
        setUpSpiner()

        binding.spOptions.onItemSelectedListener = this

        binding.btnProceed.setOnClickListener {

            if (fieldValidations()) {
                showSnack("SuccessFully Processed...")
            } else {
                showSnack("Please enter valid one...")
            }
        }
    }

    /**
     * Setting viewmodel..
     */
    private fun setViewModel() {
        viewModel =
            ViewModelProviders.of(
                this,
                MainActivityViewFactory(DataManager(this, ApiHelper(RetrofitBulder.apiService)))
            )
                .get(MainActivityViewModel::class.java)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.mainBinding = viewModel
    }

    private fun setUpObservers(id: String) {

        viewModel.callFirstDataApi(id).observe(this, Observer {
            when (it.status) {
                ERROR -> {
                    showSnack(it.message.toString())
                }
                LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                SUCCESS -> {
                    binding.progressBar.visibility = View.GONE
                    number_of_fields = it.data?.result?.number_of_fields!!
                    fieldsList = it.data?.result?.fields as ArrayList<Field>
                    addViews(number_of_fields)
                }
            }
        })
    }

    private fun addViews(number_of_fields: Int) {
        val textViewIDList = ArrayList<String>()
        for (i in 0..number_of_fields) {
            if (fieldsList[i].ui_type.type == "textfield") {
                addTextField(
                    i,
                    fieldsList[i].name,
                    fieldsList[i].hint_text
                )
                textViewIDList.add("fieldsList[i].name" + i + 1)
            } else if (fieldsList[i].ui_type.type == "dropdown") {
                val list: ArrayList<String> = ArrayList()
                for (element in fieldsList[i].ui_type.values) {
                    list.add(element.name)
                }
                addSpiner(id = i, name = fieldsList[i].name,list = list)
            }
        }
    }

    private fun addTextField(id: Int, name: String, hint_text: String) {
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        params.setMargins(0, 0, 0, 10)

        val textView = TextView(this)
        textView.apply {
            text = name
            textSize = resources.getDimension(R.dimen.xsmall)
            layoutParams = params
        }

        val editText = EditText(this)

        editText.apply {
            hint = hint_text
            layoutParams = params
            this.id = id
            inputType = if (fieldsList[id].type.data_type == "int") InputType.TYPE_CLASS_NUMBER
            else InputType.TYPE_CLASS_TEXT
        }
        binding.llRoot.addView(textView)
        binding.llRoot.addView(editText)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun addSpiner(id: Int,name: String, list: ArrayList<String>) {
        val spinner = Spinner(this)
        val linearLayout = LinearLayout(this)

        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(0, 0, 0, 10)

        val textView = TextView(this)
        textView.apply {
            text = name
            textSize = resources.getDimension(R.dimen.xsmall)
            params.setMargins(0,0,0,15)
            layoutParams = params
        }

        spinner.apply {
            layoutParams = params
            this.id = id
            adapter =
                ArrayAdapter(this@MainActivity, R.layout.spiner_drop_down, list)
        }

        linearLayout.apply {
            layoutParams = params
            setPadding(8, 8, 8, 8)
            background = resources.getDrawable(R.drawable.spinner_bg)
            addView(spinner)
        }

        binding.llRoot.addView(textView)
        binding.llRoot.addView(linearLayout)
    }

    private fun setUpSpiner() {
        optionsList = arrayOf("Option1", "Option2", "Option3")
        val countryAdapter = ArrayAdapter<CharSequence>(this, R.layout.spiner_text, optionsList)
        countryAdapter.setDropDownViewResource(R.layout.spiner_drop_down)
        binding.spOptions.adapter = countryAdapter
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when (parent?.id) {
            R.id.spOptions -> {
                binding.llRoot.removeAllViews()
//                showSnack(optionsList[position])
                val count = position + 1
                setUpObservers(count.toString())
            }
            else -> {
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    fun showSnack(text: String) {
        val snackbar = Snackbar
            .make(binding.llRoot, text, Snackbar.LENGTH_SHORT)
        snackbar.show()
    }

    private fun fieldValidations(): Boolean {
        var count = 0
        for (i in 0 until number_of_fields) {
            if (fieldsList[i].ui_type.type == "textfield") {

                if (findViewById<EditText>(i) != null) {
                    val etxtId = findViewById<EditText>(i)
                    val text = etxtId.text
                    if (text.isNotEmpty()) {
                        val regex = fieldsList[i].regex
                        if (regex != "") {
                            try {
                                val ps: Pattern = Pattern.compile(regex)
                                val ms: Matcher = ps.matcher(text)
                                val bs: Boolean = ms.matches()
                                if (!bs) {
                                    count++
                                }
                            } catch (e: PatternSyntaxException) {
                                e.printStackTrace()
                            }

                        }
                    } else {
                        etxtId.error = "Text must not be empty"
                        count++
                    }
                }
            } else if (fieldsList[i].ui_type.type == "dropdown") {

            }
        }

        return count <= 0
    }
}

