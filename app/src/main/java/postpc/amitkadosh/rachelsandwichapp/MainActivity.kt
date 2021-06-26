package postpc.amitkadosh.rachelsandwichapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer

class MainActivity : AppCompatActivity() {
    private lateinit var dataBase : DataBase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBase = this.applicationContext as DataBase
        setContentView(R.layout.activity_main)
        if (dataBase.id.isNullOrEmpty()){
            val intent = Intent(this, NewOrderActivity::class.java)
            startActivity(intent)
        }
        else{
            dataBase.orderLiveData.observe(this, Observer { order: Order ->
                startActivityByStatus(order)
            })
            dataBase.orderLiveData.value?.let { startActivityByStatus(it) }
        }

    }

    private fun startActivityByStatus(order: Order) {
        when (order.status) {
            Status.WAITING -> {
                val intent = Intent(this, EditActivity::class.java)
                startActivity(intent)
            }
            Status.INPROGRESS -> {
                val intent = Intent(this, InProgressActivity::class.java)
                startActivity(intent)
            }
            Status.DONE -> {
                val intent = Intent(this, DoneActivity::class.java)
                startActivity(intent)
            }
            else -> Log.d("public item liveData", "no valid status")
        }
    }
}