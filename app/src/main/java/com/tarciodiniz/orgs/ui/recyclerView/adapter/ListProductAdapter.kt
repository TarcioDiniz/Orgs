package com.tarciodiniz.orgs.ui.recyclerView.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tarciodiniz.orgs.R
import com.tarciodiniz.orgs.model.Produto

class ListProductAdapter (
    private val context: Context,
    private val product: List<Produto>
        ): RecyclerView.Adapter<ListProductAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        fun bind(produto: Produto) {

            val name = itemView.findViewById<TextView>(R.id.name)
            val description = itemView.findViewById<TextView>(R.id.description)
            val value = itemView.findViewById<TextView>(R.id.value)

            name.text = produto.name
            description.text = produto.description
            value.text = produto.value.toPlainString()

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.product, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val produto = product[position]
        holder.bind(produto)
    }

    override fun getItemCount(): Int = product.size

}
