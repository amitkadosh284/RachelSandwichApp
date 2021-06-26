package postpc.amitkadosh.rachelsandwichapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton


class DoneActivity : AppCompatActivity() {
    private lateinit var dataBase : DataBase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBase = this.applicationContext as DataBase
        setContentView(R.layout.activity_done)

        val gotItButton : FloatingActionButton = findViewById(R.id.gotIt)
        val hello: TextView = findViewById(R.id.nameField)

        val helloText: String = "Hii" + dataBase.name
        hello.text = helloText

        gotItButton.setOnClickListener{
            val order: Order? = dataBase.orderLiveData.value
            if (order != null){
                dataBase.deleteOrder()
                val intent = Intent(this, NewOrderActivity::class.java)
                startActivity(intent)
            }
        }
    }
}