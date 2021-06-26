package postpc.amitkadosh.rachelsandwichapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.lifecycle.Observer
import com.google.android.material.floatingactionbutton.FloatingActionButton

class EditActivity : AppCompatActivity() {
    private lateinit var amitDataBase: DataBase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        dataBase = this.applicationContext as DataBase
        val order: Order? = dataBase.orderLiveData.value
        //finds views
        val hello: TextView = findViewById(R.id.nameField)
        val tahini: SwitchCompat = findViewById(R.id.tahini)
        val hummus: SwitchCompat = findViewById(R.id.hummus)
        val pickelsBar: SeekBar = findViewById(R.id.pickelsBar)
        val commentContent : EditText = findViewById(R.id.comments)
        val deleteButton : FloatingActionButton = findViewById(R.id.delete)
        val saveButton : FloatingActionButton = findViewById(R.id.save)

        //sets views
        val helloStr = "Hii " + (order?.name ?: "")
        hello.text = helloStr
        tahini.isChecked = order?.tahini ?: false
        hummus.isChecked = order?.hummus ?: false
        val comment = order?.comment
        commentContent.setText(comment)
        pickelsBar.progress = order?.pickles ?: 0

        deleteButton.setOnClickListener{
            dataBase.deleteOrder()

            val intent = Intent(this, NewOrderActivity::class.java)
            startActivity(intent)
        }

        saveButton.setOnClickListener{
            if (order != null) {
                order.pickles = pickelsBar.progress
                order.tahini = tahini.isChecked
                order.hummus = hummus.isChecked
                order.comment = commentContent.text.toString()
            }
        }


        //set observer to the liveData
        dataBase.orderLiveData.observe(this, Observer { it->
            if (it.status == Status.INPROGRESS){
                val intent = Intent(this, InProgressActivity::class.java)
                startActivity(intent)
            }
        })
    }
}