package com.example.lapordriver.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.lapordriver.databinding.ActivityMainBinding
import com.example.lapordriver.ui.fragment.BottomSheetFragment
import com.example.lapordriver.utils.ViewModelFactory
import com.example.lapordriver.utils.Result


    class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val mainViewModel: MainViewModel by viewModels {
        ViewModelFactory()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.addFab.setOnClickListener {
            // Show the BottomSheetFragment
            val complaintBottomSheet = BottomSheetFragment()
            complaintBottomSheet.show(supportFragmentManager, complaintBottomSheet.tag)
        }

        mainViewModel.getAllVehicle().observe(this){ result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Result.Error -> {
                    // Hide the ProgressBar
                    binding.progressBar.visibility = View.GONE

                    // Show a Toast with the error message
                    val error = result.error
                    Toast.makeText(this, "Error: $error", Toast.LENGTH_SHORT).show()
                    Log.d("data2", "data $error")
                }
                is Result.Success -> {
                    // Hide the ProgressBar
                    binding.progressBar.visibility = View.GONE

                    val data = result.data
                    Log.d("data3", "data success $data")

                    val adapter = MainAdapter(data)
                    val recycleView = binding.rvComplaint
                    recycleView.adapter = adapter
                }
            }
        }
    }
}