package postpc.amitkadosh.rachelsandwichapp

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseException
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson


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
        Log.d("id of order", id.toString());
        if (id.isNullOrEmpty()){
            initFirebaseObserver()
        }
    }

    private fun initializeFromSp() {
        id = sp.getString("id", null).toString()
        name = sp.getString("name", "").toString()
        id?.let{fireStore.collection("orders").document(id.toString()).get().addOnSuccessListener {result ->
            _orderLivaData.value = result?.toObject(Order::class.java)
        }}
    }

    public fun uploadOrUpdate(order: Order){
        if (id.isNullOrEmpty()){
            id = order.id
            initFirebaseObserver()
        }
        if (name.isNullOrEmpty()){
            name = order.name
        }
        fireStore.collection("orders").document(order.id).set(order).addOnSuccessListener {
            val editor = sp.edit()
            editor.putString("id", id)
            editor.putString("name", order.name)
            editor.apply()
            _orderLivaData.setValue(order)
        }
    }

    private fun initFirebaseObserver() {
        id?.let {
            Log.d("initialize", id.toString())
            if(it.isNotEmpty()){
                fireStore.collection("orders").document(it).addSnapshotListener { result: DocumentSnapshot?, e: FirebaseException? ->
                    if (result != null) {
                        _orderLivaData.value = result.toObject(Order::class.java)
                    }
                }
            }
        }
    }


    public fun deleteOrder(){
        id?.let {
            fireStore.collection("orders").document(it).delete().addOnSuccessListener {
                //toast
                val editor = sp.edit()
                editor.remove(id)
                editor.apply()
                id = null
                _orderLivaData.setValue(null)
            }
        }
    }
}

