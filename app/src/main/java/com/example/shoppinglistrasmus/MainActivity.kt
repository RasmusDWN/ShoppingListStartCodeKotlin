package com.example.shoppinglistrasmus

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shoppinglistrasmus.adapters.ProductAdapter
import com.example.shoppinglistrasmus.data.Product
import com.example.shoppinglistrasmus.data.Repository
import com.example.shoppinglistrasmus.data.Repository.addProduct
import com.google.firebase.FirebaseApp
import kotlinx.android.synthetic.main.activity_main.*
import org.pondar.dialogFragmentdemokotlinnew.DialogFragment


class MainActivity : AppCompatActivity() {

    //you need to have an Adapter for the products
    private val items = arrayOf("0", "1", "3", "4", "5")
    lateinit var adapter: ProductAdapter
    private val RESULT_CODE_PREFERENCES = 1

    fun convertListToString(): String {
        var result = ""
        for (product in Repository.products) {
            result = result + product.toString()
        }
        return result
    }

    private fun addNewProduct() {
        val newProduct = Product(
            name = editTextTitle.text.toString(),
            quantity = editTextQuantity.text.toString().toInt()
        )
        addProduct(newProduct)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main )
        FirebaseApp.initializeApp(applicationContext)
        button_add.setOnClickListener { addNewProduct()}

        Repository.getData().observe(this, Observer {
            Log.d("Products","Found ${it.size} products")
            updateUI()
        })

        sortNameButton.setOnClickListener {
            Repository.products.sortBy { it.name }
            adapter.notifyDataSetChanged()
        }

        sortQuantityButton.setOnClickListener {
            Repository.products.sortBy { it.quantity }
            adapter.notifyDataSetChanged()
        }

        val name = PreferenceHandler.getName(this)
        val notifications = PreferenceHandler.useNotifications(this)
        updateUISettings(name, notifications)
    }

    fun updateUI() {
        val layoutManager = LinearLayoutManager(this)

        /*you need to have a defined a recylerView in your
        xml file - in this case the id of the recyclerview should
        be "recyclerView" - as the code line below uses that */

       recyclerView.layoutManager = layoutManager

       adapter = ProductAdapter(Repository.products)

      /*connecting the recyclerview to the adapter  */
      recyclerView.adapter = adapter
    }

    fun updateUISettings(name: String, notifications: Boolean) {
        myName.text = name
        if (notifications)
            useNotifications.text = getString(R.string.on)
        else
            useNotifications.text = getString(R.string.off)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RESULT_CODE_PREFERENCES) {
            val name = PreferenceHandler.getName(this)
            val notifications = PreferenceHandler.useNotifications(this)
            val message = "Welcome to your shopping list, $name!"
            val toast = Toast.makeText(this, message, Toast.LENGTH_LONG)
            toast.show()
            updateUISettings(name, notifications)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    // User clicks yes to deleting list
    fun posClicked() {
        val toast = Toast.makeText(
            this,
            "Your list has been deleted", Toast.LENGTH_LONG
        )
        toast.show()
        Repository.deleteList()
    }

    // User clicks no to deleting list
    fun negClicked() {
        val toast = Toast.makeText(
            this,
            "List stays", Toast.LENGTH_LONG
        )
        toast.show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.product_share -> {
                val text = convertListToString()
                val sharingIntent = Intent(Intent.ACTION_SEND)
                sharingIntent.type = "text/plain"
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Shared Data")
                sharingIntent.putExtra(Intent.EXTRA_TEXT, text)
                startActivity(Intent.createChooser(sharingIntent, "Share Using"))
                return true
            }

            R.id.product_delete -> {
                Toast.makeText(this, "Delete product clicked", Toast.LENGTH_LONG)
                    .show()
                val dialog = DialogFragment(::posClicked, ::negClicked)
                dialog.show(supportFragmentManager, "fragment")

                return true
            }

            R.id.product_help -> {
                Toast.makeText(this, "Help item clicked", Toast.LENGTH_LONG)
                    .show()
                return true
            }

            R.id.product_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivityForResult(intent, RESULT_CODE_PREFERENCES)
                return true
            }
        }

        return false
    }
}
