package com.example.itscartest

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    private lateinit var previewView: androidx.camera.view.PreviewView
    private lateinit var buildingImage: ImageView
    private lateinit var buildingDescription: TextView
    private lateinit var resultLayout: View
    private lateinit var cameraExecutor: ExecutorService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        previewView = findViewById(R.id.previewView)
        buildingImage = findViewById(R.id.buildingImage)
        buildingDescription = findViewById(R.id.buildingDescription)
        resultLayout = findViewById(R.id.resultLayout)

        // 카메라 권한 확인
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }

        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    @OptIn(androidx.camera.core.ExperimentalGetImage::class)
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                // 권한이 거부된 경우 처리
                finish()
            }
        }
    }

    @OptIn(androidx.camera.core.ExperimentalGetImage::class)
    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener(Runnable {
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            val imageAnalysis = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()

            imageAnalysis.setAnalyzer(cameraExecutor, TextAnalyzer { buildingNumber ->
                runOnUiThread {
                    if (buildingNumber != null) {
                        displayBuildingInfo(buildingNumber)
                    }
                }
            })

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageAnalysis)
            } catch(exc: Exception) {
                Log.e(TAG, "카메라 바인딩 실패: ${exc.message}")
            }

        }, ContextCompat.getMainExecutor(this))
    }

    private fun displayBuildingInfo(buildingNumber: String) {
        // 예: "1", "2" 등으로 가정
        when (buildingNumber) {
            "1" -> {
                buildingImage.setImageResource(R.drawable.building_1)
                buildingDescription.text = getString(R.string.building_1_description)
                resultLayout.visibility = View.VISIBLE
            }
            "2" -> {
                buildingImage.setImageResource(R.drawable.building_2)
                buildingDescription.text = getString(R.string.building_2_description)
                resultLayout.visibility = View.VISIBLE
            }
            // 추가 건물 번호 처리...
            else -> {
                resultLayout.visibility = View.GONE
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    companion object {
        private const val TAG = "MainActivity"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }
}

@OptIn(androidx.camera.core.ExperimentalGetImage::class)
class TextAnalyzer(private val onTextRecognized: (String?) -> Unit) : ImageAnalysis.Analyzer {

    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            recognizer.process(image)
                .addOnSuccessListener { visionText ->
                    var detectedNumber: String? = null
                    for (block in visionText.textBlocks) {
                        for (line in block.lines) {
                            for (element in line.elements) {
                                val text = element.text
                                if (text.matches(Regex("\\d+"))) { // 숫자만 감지
                                    detectedNumber = text
                                    break
                                }
                            }
                            if (detectedNumber != null) break
                        }
                        if (detectedNumber != null) break
                    }
                    onTextRecognized(detectedNumber)
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "텍스트 인식 실패: ${e.message}")
                    onTextRecognized(null)
                }
                .addOnCompleteListener {
                    imageProxy.close()
                }
        } else {
            imageProxy.close()
        }
    }

    companion object {
        private const val TAG = "TextAnalyzer"
    }
}

