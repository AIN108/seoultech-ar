package com.example.itscartest

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    private lateinit var previewView: androidx.camera.view.PreviewView
    private lateinit var targetView: View
    private lateinit var buildingInfoCard: CardView
    private lateinit var buildingTitle: TextView
    private lateinit var buildingImage: ImageView
    private lateinit var buildingDescription: TextView
    private lateinit var closeButton: Button
    private lateinit var cameraExecutor: ExecutorService

    // 빌딩 목록
    private val buildings = listOf(
        Building(
            number = "1",
            name = "대학본부",
            imageResId = R.drawable.building_1,
            description = "대학본부 1번"
        ),
        Building(
            number = "2",
            name = "다산관",
            imageResId = R.drawable.building_2,
            description = "다산관 2번"
        ),
        Building(
            number = "3",
            name = "창학관",
            imageResId = R.drawable.building_3,
            description = "창학관 3번"
        ),
        Building(
            number = "7",
            name = "테크노파크",
            imageResId = R.drawable.building_7,
            description = "테크노파크 7번"
        ),
        Building(
            number = "34",
            name = "도서관",
            imageResId = R.drawable.building_34,
            description = "도서관 34번"
        ),
        Building(
            number = "39",
            name = "다빈치관",
            imageResId = R.drawable.building_39,
            description = "다빈치관 39번"
        ),
        Building(
            number = "51",
            name = "100주년기념관",
            imageResId = R.drawable.building_51,
            description = "100주년기념관 51번"
        ),
        Building(
            number = "60",
            name = "미래관",
            imageResId = R.drawable.building_60,
            description = "미래관 60번"
        ),
        Building(
            number = "62",
            name = "테크노큐브",
            imageResId = R.drawable.building_62,
            description = "테크노큐브 62번"
        )
        // 추가 빌딩을 여기에 추가
    )

    private var isProcessing = false // 빌딩 인식 중인지 여부

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        previewView = findViewById(R.id.previewView)
        targetView = findViewById(R.id.targetView)
        buildingInfoCard = findViewById(R.id.buildingInfoCard)
        buildingTitle = findViewById(R.id.buildingTitle)
        buildingImage = findViewById(R.id.buildingImage)
        buildingDescription = findViewById(R.id.buildingDescription)
        closeButton = findViewById(R.id.closeButton)

        // 닫기 버튼 클릭 리스너
        closeButton.setOnClickListener {
            buildingInfoCard.visibility = View.GONE
            isProcessing = false
        }

        // 카메라 권한 확인
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }

        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
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

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener(Runnable {
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // 프리뷰 설정
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }

            // 후면 카메라 선택
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            // 이미지 분석 설정
            val imageAnalysis = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()

            imageAnalysis.setAnalyzer(cameraExecutor, TextAnalyzer { recognizedNumbers ->
                runOnUiThread {
                    if (!isProcessing && recognizedNumbers.isNotEmpty()) {
                        // 인식된 숫자들을 길이 순으로 정렬(긴 숫자 우선)
                        val sortedNumbers = recognizedNumbers.sortedByDescending { it.length }
                        // 인식된 숫자 중 빌딩 목록에 있는 번호를 찾음
                        val detectedNumber = sortedNumbers.find { number ->
                            buildings.any { it.number == number }
                        }
                        if (detectedNumber != null) {
                            val building = buildings.find { it.number == detectedNumber }
                            if (building != null) {
                                displayBuildingInfo(building)
                                isProcessing = true
                            }
                        }
                    }
                }
            })

            try {
                // 기존에 바인딩된 카메라가 있으면 해제
                cameraProvider.unbindAll()
                // 카메라에 프리뷰와 이미지 분석을 바인딩
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageAnalysis
                )
            } catch (exc: Exception) {
                Log.e(TAG, "카메라 바인딩 실패: ${exc.message}")
            }

        }, ContextCompat.getMainExecutor(this))
    }

    private fun displayBuildingInfo(building: Building) {
        buildingTitle.text = building.name
        buildingImage.setImageResource(building.imageResId)
        buildingDescription.text = building.description
        buildingInfoCard.visibility = View.VISIBLE
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









