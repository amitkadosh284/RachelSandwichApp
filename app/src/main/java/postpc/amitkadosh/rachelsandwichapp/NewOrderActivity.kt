package postpc.amitkadosh.rachelsandwichapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton


class NewOrderActivity : AppCompatActivity() {

    private lateinit var dataBase : DataBase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBase = this.applicationContext as DataBase
        setContentView(R.layout.new_order)

        //finds views
        val nameField: TextView = findViewById(R.id.name)
        val tahini: SwitchCompat = findViewById(R.id.tahini)
        val hummus: SwitchCompat = findViewById(R.id.hummus)
        val pickelsBar: SeekBar = findViewById(R.id.pickelsBar)
        val commentContent : EditText = findViewById(R.id.comments)
        val saveButton : FloatingActionButton = findViewById(R.id.sendOrder)


        if (dataBase.name != ""){
            nameField.text = dataBase.name
        }

        saveButton.setOnClickListener{
            updateOrder(pickelsBar, tahini, hummus, commentContent, nameField)
            val intent = Intent(this, EditActivity::class.java)
            startActivity(intent)
        }
    }

    private fun updateOrder(
        pickelsBar: SeekBar,
        tahini: SwitchCompat,
        hummus: SwitchCompat,
        commentContent: EditText,
        nameField: TextView
    ) {
        val order = Order()
        order.pickles = pickelsBar.progress
        order.tahini = tahini.isChecked
        order.hummus = hummus.isChecked
        order.comment = commentContent.text.toString()
        order.name = nameField.text.toString()
        dataBase.uploadOrUpdate(order)
    }
}