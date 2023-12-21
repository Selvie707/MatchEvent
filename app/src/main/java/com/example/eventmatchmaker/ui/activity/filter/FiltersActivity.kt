package com.example.eventmatchmaker.ui.activity.filter

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.compose.ui.text.toLowerCase
import androidx.core.util.Pair
import androidx.core.util.component1
import androidx.core.util.component2
import com.example.eventmatchmaker.R
import com.example.eventmatchmaker.databinding.ActivityFiltersBinding
import com.example.eventmatchmaker.ui.activity.search.SearchActivity
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

class FiltersActivity : AppCompatActivity() {
    // TODO fix filter

    private lateinit var binding: ActivityFiltersBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFiltersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.spEventCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent?.getItemAtPosition(position).toString()
                Log.d("Selected Item", selectedItem)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                
            }
        }

        binding.slMinPrice.addOnChangeListener { _, value, _ ->
            val formattedMinValue = String.format("%.3f", value / 1000)
            binding.tvMinPrice.text = formattedMinValue
        }

        binding.slMaxPrice.addOnChangeListener { _, value, _ ->
            val formattedMaxValue = String.format("%.3f", value / 1000)
            binding.tvMaxPrice.text = formattedMaxValue
        }

        binding.etStartDate.setOnClickListener {
            val picker = MaterialDatePicker.Builder.dateRangePicker()
                .setTheme(R.style.ThemeMaterialCalendar)
                .setTitleText("Select Date Range")
                .setSelection(Pair(null, null))
                .build()

            picker.show(this.supportFragmentManager, "TAG")

            picker.addOnPositiveButtonClickListener {
                binding.etStartDate.hint = convertTimeToDate(it.first) + "/" + convertTimeToDate(it.second)
            }

            picker.addOnNegativeButtonClickListener {
                picker.dismiss()
            }
        }

        binding.btnNext.setOnClickListener {
            val selectedItem = binding.spEventCategory.selectedItem.toString()
                .lowercase(Locale.getDefault())
            val ageLimitData = binding.etAgeLimit.text.toString()

            val (startDate, endDate) = splitDateString(binding.etStartDate.text.toString())

            val startDateData = startDate + "T00:00:00.000Z"
            val startDateCapData = endDate + "T00:00:00.000Z"
            val minPriceData = binding.tvMinPrice.text.toString().replace(".", "")
            val maxPriceData = binding.tvMaxPrice.text.toString().replace(".", "")

            val intent = Intent(this@FiltersActivity, SearchActivity::class.java)
            intent.putExtra("SELECTED_ITEM", selectedItem)
            intent.putExtra("AGE_LIMIT_DATA", ageLimitData)
            intent.putExtra("START_TIME_DATA", startDateData)
            intent.putExtra("START_TIME_CAP_DATA", startDateCapData)
            intent.putExtra("MIN_PRICE_DATA", minPriceData)
            intent.putExtra("MAX_PRICE_DATA", maxPriceData)
            startActivity(intent)
        }
    }

    private fun splitDateString(input: String): Pair<String, String> {
        val dates = input.split("/")
        val firstDate = dates[0]
        val secondDate = dates[1]
        return Pair(firstDate, secondDate)
    }

    private fun convertTimeToDate(time: Long): String {
        val utc = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        utc.timeInMillis = time
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return format.format(utc.time)
    }
}