package com.rithikjain.nstvideo

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope {
    private lateinit var mJob: Job
    override val coroutineContext: CoroutineContext
        get() = mJob + Dispatchers.Main

    private var isModelRunning = false
    private var contentImage: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        progressBar.visibility = View.INVISIBLE
        mJob = Job()

        val styleList = mutableListOf<String>()
        val styleNameList = mutableListOf<String>()
        styleList.add("none.png")
        styleNameList.add("None")
        for (i in 1..26) {
            styleList.add("style$i.jpg")
            styleNameList.add("Style $i")
        }

        val borderDrawable = ContextCompat.getDrawable(this, R.drawable.image_border)
        val stylesRecyclerViewAdapter = StylesRecyclerViewAdapter(borderDrawable!!)
        stylesRecyclerViewAdapter.setStylesList(styleList, styleNameList)
        stylesRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = stylesRecyclerViewAdapter
        }
        LinearSnapHelper().attachToRecyclerView(stylesRecyclerView)

        stylesRecyclerView.addOnItemClickListener(object: OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {
                if (!isModelRunning) {
                    if (contentImage != null) {
                        isModelRunning = true
                        stylesRecyclerViewAdapter.notifyItemChanged(stylesRecyclerViewAdapter.selectedPos)
                        stylesRecyclerViewAdapter.selectedPos = position
                        stylesRecyclerViewAdapter.notifyItemChanged(stylesRecyclerViewAdapter.selectedPos)
                        if (position == 0) {
                            outputImage.setImageBitmap(contentImage)
                            isModelRunning = false
                        } else {

                        }
                    } else {
                        Toast.makeText(
                            this@MainActivity,
                            "Select an image first",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Log.d("esh", "Model Running")
                }
            }
        })
    }
}