package com.tarciodiniz.orgs.ui.dialog

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import com.tarciodiniz.orgs.databinding.ImageFormBinding
import com.tarciodiniz.orgs.extensions.tryToLoad

class FormImageDialog(private val context: Context) {

    fun showDialog(
        urlPattern: String? = null,
        whenImageLoader: (image: String) -> Unit
    ) {
        ImageFormBinding
            .inflate(LayoutInflater.from(context)).apply {
                urlPattern?.let {
                    formImageview.tryToLoad(it)
                    formImageUrl.setText(it)
                }

                formButtonLoad.setOnClickListener {
                    val url = formImageUrl.text.toString()
                    formImageview.tryToLoad(url)
                }

                AlertDialog
                    .Builder(context)
                    .setView(root)
                    .setPositiveButton("Confirm") { _, _ ->
                        val url = formImageUrl.text.toString()
                        whenImageLoader(url)
                    }.setNegativeButton("Cancel") { _, _ -> }.show()
            }


    }

}