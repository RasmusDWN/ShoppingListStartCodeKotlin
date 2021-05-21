package com.example.shoppinglistrasmus.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglistrasmus.data.Product
import com.example.shoppinglistrasmus.data.Repository.decreaseByOne
import com.example.shoppinglistrasmus.data.Repository.increaseByOne
import com.example.shoppinglistrasmus.R
import com.example.shoppinglistrasmus.data.Repository
import com.google.android.material.snackbar.Snackbar


class ProductAdapter(var products: MutableList<Product>) :
    RecyclerView.Adapter<ProductAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductAdapter.ViewHolder, position: Int) {
        holder.productTitle.text = products[position].name
        holder.productQuantity.text = products[position].quantity.toString()
    }

    override fun getItemCount(): Int {
        return products.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var productTitle: TextView
        var productQuantity: TextView
        var productIncrease: ImageButton
        var productDecrease: ImageButton
        var productDelete: ImageButton

        init {
            productTitle = itemView.findViewById(R.id.product_title)
            productQuantity = itemView.findViewById(R.id.product_quantity)
            productIncrease = itemView.findViewById(R.id.product_increase)
            productDecrease = itemView.findViewById(R.id.product_decrease)
            productDelete = itemView.findViewById(R.id.product_delete)

            productDelete.setOnClickListener { v: View ->
                val position = adapterPosition
                val parent = v.rootView.findViewById<View>(R.id.mainView)
                val savedProduct = Repository.products[position]
                Repository.deleteProduct(position)
                notifyItemRemoved(position)

                val snackbar = Snackbar
                    .make(parent, "Item deleted", Snackbar.LENGTH_LONG)
                    .setAction("UNDO") {
                        Repository.addProduct(savedProduct)
                        val snackbar = Snackbar.make(parent, "Item restored", Snackbar.LENGTH_SHORT)
                        
                        snackbar.show()
                    }
                snackbar.show()
            }

            productIncrease.setOnClickListener { v: View ->
                val position = adapterPosition
                increaseByOne(position)
                notifyDataSetChanged()
            }

            productDecrease.setOnClickListener { v: View ->
                val position = adapterPosition
                val savedProduct = Repository.products[position]
                if(savedProduct.quantity > 1) {
                    decreaseByOne(position)
                    notifyDataSetChanged()
                }
            }
        }


    }
}
