package com.example.lapordriver.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import com.example.lapordriver.R
import com.example.lapordriver.databinding.ActivityCameraBinding
import com.example.lapordriver.ui.fragment.BottomSheetFragment
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale

class CameraActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCameraBinding
    private var imageCapture: ImageCapture? = null
    private lateinit var outputDirectory: File
    private var photoFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize View Binding
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize output directory
        outputDirectory = getOutputDirectory()

        // Start the camera
        startCamera()

        // Capture button click listener
        binding.captureButton.setOnClickListener {
            takePhoto()
        }

        // Retake button click listener
        binding.retakeButton.setOnClickListener {
            binding.imagePreview.visibility = ImageView.GONE
            binding.previewView.visibility = PreviewView.VISIBLE
            binding.captureButton.visibility = ImageButton.VISIBLE
            binding.confirmButton.visibility = Button.GONE
            binding.retakeButton.visibility = Button.GONE
        }
    }

    private fun showBottomSheet(imageUri: Uri) {
        val bottomSheetFragment = BottomSheetFragment().apply {
            arguments = Bundle().apply {
                putParcelable("imageUri", imageUri)
            }
        }
        bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
    }



    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            // Create a Camera Preview
            val preview = androidx.camera.core.Preview.Builder().build().also {
                it.setSurfaceProvider(binding.previewView.surfaceProvider)
            }

            imageCapture = ImageCapture.Builder().build()

            // Select back camera
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to the camera
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture
                )
            } catch (e: Exception) {
                Log.e("CameraXActivity", "Use case binding failed", e)
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return

        // Create a file to save the photo
        photoFile = File(
            outputDirectory,
            SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(System.currentTimeMillis()) + ".jpg"
        )

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile!!).build()

        // Capture the photo
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val savedUri = Uri.fromFile(photoFile)
                    val intent = Intent()
                    intent.putExtra("imageUri", savedUri)
                    showImagePreview(savedUri)

                    binding.confirmButton.setOnClickListener {
                        photoFile?.let {
                            setResult(RESULT_OK, intent)
                            onBackPressedDispatcher.onBackPressed()
                        } ?: Toast.makeText(this@CameraActivity, "No image available", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.e("CameraXActivity", "Photo capture failed: ${exception.message}", exception)
                    Toast.makeText(this@CameraActivity, "Photo capture failed", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }


    private fun showImagePreview(savedUri: Uri) {
        // Hide the camera preview and show the image preview
        binding.previewView.visibility = PreviewView.GONE
        binding.imagePreview.visibility = ImageView.VISIBLE
        binding.imagePreview.setImageURI(savedUri)

        // Show confirm and retake buttons
        binding.captureButton.visibility = ImageButton.GONE
        binding.confirmButton.visibility = Button.VISIBLE
        binding.retakeButton.visibility = Button.VISIBLE
    }

    private fun getOutputDirectory(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists()) mediaDir else filesDir
    }
}
