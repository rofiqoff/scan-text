package com.rofiqoff.scantext

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.rofiqoff.scantext.databinding.ActivityResultBinding
import java.io.File

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun initView() {
        addBackButton()

        val path = intent?.getStringExtra(PARAM_FILE_PATH) ?: ""

        val uri = Uri.fromFile(File(path))
        Glide.with(this).load(uri).into(binding.ivResult)

        readTextFromImage(uri)
    }

    private fun addBackButton() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.label_result)
    }

    private fun readTextFromImage(image: Uri) {
        val inputImage = InputImage.fromFilePath(this, image)

        TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
            .process(inputImage)
            .addOnSuccessListener { text ->
                processTextRecognitionResult(text)
            }
            .addOnFailureListener { error ->
                error.printStackTrace()
            }
    }

    private fun processTextRecognitionResult(result: Text) {
        var finalText = ""
        for (block in result.textBlocks) {
            for (line in block.lines) {
                finalText += line.text + " \n"
            }
            finalText += "\n"
        }

        binding.tvResult.text = finalText.ifEmpty {
            getString(R.string.message_no_text_found)
        }
    }

    companion object {
        const val PARAM_FILE_PATH = "param-file-name"
    }
}
