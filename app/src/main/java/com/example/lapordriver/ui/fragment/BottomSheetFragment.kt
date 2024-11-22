package com.example.lapordriver.ui.fragment

import android.app.Activity
import android.app.Activity.RESULT_OK
import com.example.lapordriver.ui.CameraActivity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.example.lapordriver.databinding.FragmentBottomSheetBinding
import com.example.lapordriver.ui.MainViewModel
import com.example.lapordriver.utils.ViewModelFactory
import androidx.fragment.app.viewModels
import com.example.lapordriver.api.RetrofitClient
import com.example.lapordriver.api.responses.DefaultResponse
import com.example.lapordriver.ui.MainActivity
import com.example.lapordriver.utils.Helper.reduceFileImage
import com.example.lapordriver.utils.Helper.uriToFile
import com.example.lapordriver.utils.Result
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Response
import java.io.File

class BottomSheetFragment : BottomSheetDialogFragment() {

    companion object {
        const val REQUEST_CAMERA = 101

    }

    private var imageUri: Uri? = null
    private var getFile: File? = null

    private val mainViewModel : MainViewModel by viewModels {
        ViewModelFactory()
    }

    private var selectedId : String = ""

    private var _binding: FragmentBottomSheetBinding? = null
    private val binding get() = _binding!!

    private val openCameraXLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val intent = result.data
            if (result.resultCode == RESULT_OK && intent != null && intent.hasExtra("imageUri")) {
                imageUri = intent.getParcelableExtra("imageUri")
                Log.d("TAG", "onActivityResult: $imageUri")

                imageUri?.let {
                    binding.ivPhoto.setImageURI(it)
                } ?: Toast.makeText(context, "No image to display", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Handle Photo Click
        binding.ivPhoto.setOnClickListener {
            // Launch intent for photo
            val intent = Intent(context, CameraActivity::class.java)
            openCameraXLauncher.launch(intent)
        }

        // Handle Submit Button Click
        binding.btnSubmit.setOnClickListener {
            // Handle the form submission
            handleSubmit()
        }

        retrieveData()
    }



    private fun retrieveData() {
        mainViewModel.getVehicle().observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    Log.d("data1", "data ${result.toString()}")
                }
                is Result.Error -> {
                    Log.d("data2", "data ${result.error}")
                }
                is Result.Success -> {
                    val data = result.data
                    Log.d("data3", "data success $data")

                    // Extract vehicle types from the data
                    val vehicleTypes = data!!.map { it?.type } // Extract 'type' field as list
                    val vehicleMap =
                        data.associateBy({ it!!.type }, { it!!.vehicleId }) // Map type to id

                    // Populate Spinner
                    val adapter = ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_spinner_item,
                        vehicleTypes
                    )

                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.spinnerVehicle.adapter = adapter

                    // Handle Spinner Selection
                    binding.spinnerVehicle.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                parent: AdapterView<*>,
                                view: View?,
                                position: Int,
                                id: Long
                            ) {
                                val selectedType = parent.getItemAtPosition(position).toString()
                                selectedId = vehicleMap[selectedType]!! // Get vehicleId by type
                            }

                            override fun onNothingSelected(parent: AdapterView<*>?) {
                                // Handle no selection
                            }
                        }
                }
            }
        }
    }

    private fun handleSubmit() {
        // Show the progress bar
        binding.progressBar.visibility = View.VISIBLE
        binding.btnSubmit.visibility = View.GONE

        // Retrieve note and vehicleId as RequestBody
        val note = binding.etComplaintNote.text.toString().trim().toRequestBody("text/plain".toMediaType())
        val vehicleId = selectedId.toRequestBody("text/plain".toMediaType())
        val id = "8UjCTNZlRoLvxcd1gZ0kuWqMpcumCL"
        val userId = id.toRequestBody("text/plain".toMediaType())

        // Convert image file to MultipartBody.Part
        getFile = uriToFile(imageUri!!, requireContext()) // Convert URI to file
        val file = reduceFileImage(getFile as File) // Reduce image size if needed
        val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "photo", // Key for the file in your API
            file.name, // File name
            requestImageFile
        )

        RetrofitClient
            .instanceV1
            .addLaporanV1(vehicleId, note, userId, imageMultipart)
            .enqueue(object : retrofit2.Callback<DefaultResponse> {
                override fun onResponse(
                    call: Call<DefaultResponse>,
                    response: Response<DefaultResponse>
                ) {
                    // Hide the progress bar
                    binding.progressBar.visibility = View.GONE

                    if (response.code() == 200) {
                        Log.d("dataOk", "Response code: ${response.code()}")

                        // Close the BottomSheetFragment
                        (parentFragment as? BottomSheetFragment)?.dismiss()

                        // Restart the MainActivity
                        val intent = Intent(requireContext(), MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        requireActivity().finish()
                    } else {
                        Log.d("dataOk", "Response code: ${response.code()}")

                        // Close the BottomSheetFragment
                        (parentFragment as? BottomSheetFragment)?.dismiss()

                        // Restart the MainActivity
                        val intent = Intent(requireContext(), MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        requireActivity().finish()
                    }
                }

                override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {

                    // Close the BottomSheetFragment
                    (parentFragment as? BottomSheetFragment)?.dismiss()

                    // Restart the MainActivity
                    val intent = Intent(requireContext(), MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    requireActivity().finish()
                }
            })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
