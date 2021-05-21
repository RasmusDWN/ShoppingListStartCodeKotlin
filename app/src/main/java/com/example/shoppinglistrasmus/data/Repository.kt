package com.example.shoppinglistrasmus.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

object Repository {
    var products = mutableListOf<Product>()

    private lateinit var shoppingDB: FirebaseFirestore

    //listener to changes that we can then use in the Activity
    private var productListener = MutableLiveData<MutableList<Product>>()


    fun getData(): MutableLiveData<MutableList<Product>> {
        if (products.isEmpty())
            addRealTimeListener()
        productListener.value = products //we inform the listener we have new data
        return productListener
    }

    fun addProduct(product: Product) {
        shoppingDB = Firebase.firestore

        products.add(product)
        productListener.value = products
        shoppingDB.collection("products")
            .add(product)
            .addOnSuccessListener { documentReference ->
                Log.d("Error", "DocumentSnapshot written with ID: " + documentReference.id)
                product.id = documentReference.id
            }
            .addOnFailureListener { e ->
                Log.w("Error", "Error adding document", e)
            }
    }

    fun increaseByOne( position: Int) {
        val product = products[position]
        product.quantity++
        shoppingDB = Firebase.firestore
        shoppingDB.collection("products").document(product.id)
            .update("quantity", FieldValue.increment(1))
    }

    fun decreaseByOne( position: Int) {
        val product = products[position]
        product.quantity--
        shoppingDB = Firebase.firestore
        shoppingDB.collection("products").document(product.id)
            .update("quantity", FieldValue.increment(-1))
    }

    fun deleteProduct (index : Int) {
        shoppingDB = Firebase.firestore
        val product = products[index]
        shoppingDB.collection("products").document(product.id).delete().addOnSuccessListener {
            Log.d("Snapshot", "DocumentSnapshot with ID: ${product.id} is now deleted")
        }
            .addOnFailureListener { e ->
                Log.w("Error", "Something went wrong in deleting the document", e)
            }
    }

    fun deleteList (): MutableLiveData<MutableList<Product>> {
        shoppingDB = Firebase.firestore
        productListener.value = products
        for (product in products) {
            shoppingDB.collection("products").document(product.id).delete().addOnSuccessListener {
                Log.d("Snapshot", "DocumentSnapshot with ID: ${product.id} is now deleted")
            }
                .addOnFailureListener { e ->
                    Log.d("Error", "Something went worn in deleting the list")
                }
        }
        products.clear()
        productListener.value = products
        return productListener
    }

    private fun addRealTimeListener() {
        val shoppingDB = Firebase.firestore
        val docReference = shoppingDB.collection("products")
        docReference.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.d("Repository", "listen failed", e)
                return@addSnapshotListener
            }
            products.clear()
            for (document in snapshot?.documents!!) {
                Log.d("Repository_snapshotlist", "${document.id} => ${document.data}")
                val product = document.toObject<Product>()!!
                product.id = document.id
                products.add(product)
            }
            productListener.value = products
        }


    }

}