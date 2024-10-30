package com.example.itscartest

import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

@OptIn(androidx.camera.core.ExperimentalGetImage::class)
class TextAnalyzer(private val onTextRecognized: (List<String>) -> Unit) : ImageAnalysis.Analyzer {

    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    private val regex = Regex("\\d+") // 숫자만 감지

    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val imageRotationDegrees = imageProxy.imageInfo.rotationDegrees
            val image = InputImage.fromMediaImage(mediaImage, imageRotationDegrees)
            recognizer.process(image)
                .addOnSuccessListener { visionText ->
                    val recognizedNumbers = mutableSetOf<String>()
                    for (block in visionText.textBlocks) {
                        for (line in block.lines) {
                            for (element in line.elements) {
                                val text = element.text
                                if (regex.matches(text)) {
                                    recognizedNumbers.add(text)
                                }
                            }
                        }
                    }
                    onTextRecognized(recognizedNumbers.toList())
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "텍스트 인식 실패: ${e.message}")
                    onTextRecognized(emptyList())
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







