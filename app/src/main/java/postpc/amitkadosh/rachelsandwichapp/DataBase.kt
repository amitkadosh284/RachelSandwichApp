package postpc.amitkadosh.rachelsandwichapp

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseException
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson

/**
 * this class in charge all the data base and the connection to firebase
 */
class DataBase : Application() {

    lateinit var sp: SharedPreferences
    var id: String? = null
    private lateinit var fireStore : FirebaseFirestore
    var name: String? = null

    private val _orderLivaData: MutableLiveData<Order> = MutableLiveData()
    val orderLiveData: LiveData<Order>
        get() = _orderLivaData

    override fun onCreate() {
        super.onCreate()
        sp = this.getSharedPreferences("order_data", Context.MODE_PRIVATE)
        FirebaseApp.initializeApp(this)
        fireStore = FirebaseFirestore.getInstance()

        initializeFromSp()
        if (id!!.isNotEmpty()){
            initFirebaseObserver()
        }
    }

    /**
     * this function takes the id of order from the sp and create listener to changes from firebase
     * on this order if order exists
     */
    private fun initializeFromSp() {
        id = sp.getString("id", "").toString()
        name = sp.getString("name", "").toString()
        if (id!!.isNotEmpty()){
            fireStore.collection("orders").document(id.toString()).get().addOnSuccessListener {result ->
                _orderLivaData.value = result?.toObject(Order::class.java)
            }
        }
    }

    /**
     * this function upload or update order to the firebase and set the values in the live data and
     * the share preference
     */
    public fun uploadOrUpdate(order: Order){
        if (id.isNullOrEmpty()){
            id = order.id
            initFirebaseObserver()
        }
        if (name.isNullOrEmpty()){
            name = order.name
        }
        fireStore.collection("orders").document(order.id).set(order).addOnSuccessListener {
            Toast.makeText(this, "your order was update", Toast.LENGTH_SHORT).show()
        }
        _orderLivaData.value = order
        val editor = sp.edit()
        editor.putString("id", id)
        editor.putString("name", order.name)
        editor.apply()
    }

    /**
     * create fire base observer on the order
     */
    private fun initFirebaseObserver() {
        id?.let {
            if(it.isNotEmpty()){
                fireStore.collection("orders").document(it).addSnapshotListener { result: DocumentSnapshot?, e: FirebaseException? ->
                    if (result != null) {
                        _orderLivaData.value = result.toObject(Order::class.java)
                    }
                }
            }
        }
    }


    /**
     * this function delete order from the database ans set the values in live data an sp
     */
    public fun deleteOrder(){
        if(id != null){
            Toast.makeText(this, "Your order have been deleted", Toast.LENGTH_SHORT).show()
            val editor = sp.edit()
            editor.remove("id")
            editor.clear()
            editor.apply()
            id = null
            _orderLivaData.value = null
        }
    }
}

