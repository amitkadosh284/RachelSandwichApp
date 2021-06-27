package postpc.amitkadosh.rachelsandwichapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
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
        val progressView: TextView = findViewById(R.id.progress)
        val plus: TextView = findViewById(R.id.plus)
        val minus: TextView = findViewById(R.id.minus)
        val tahini: SwitchCompat = findViewById(R.id.tahini)
        val hummus: SwitchCompat = findViewById(R.id.hummus)
        val commentContent : EditText = findViewById(R.id.comments)
        val saveButton : FloatingActionButton = findViewById(R.id.sendOrder)

        //set view
        minus.visibility = View.GONE

        //set the name if he already order
        if (dataBase.name != ""){
            nameField.text = dataBase.name
        }

        //listener to the save button
        saveButton.setOnClickListener{
            updateOrder(progressView, tahini, hummus, commentContent, nameField)
            val intent = Intent(this, EditActivity::class.java)
            startActivity(intent)
        }

        plus.setOnClickListener {
            try {
                var num_pickels : Int = progressView.text.toString().toInt()
                num_pickels++
                plus.isEnabled = num_pickels < 10
                if (num_pickels >= 10){
                    plus.visibility = View.GONE
                }
                else{
                    minus.visibility = View.VISIBLE
                }
                progressView.text = num_pickels.toString()
            } catch (e : Exception){
                Toast.makeText(this, "Invalid pickels number", Toast.LENGTH_SHORT).show()
            }
        }

        minus.setOnClickListener {
            try {
                var num_pickels : Int = progressView.text.toString().toInt()
                num_pickels--
                minus.isEnabled = num_pickels > 0
                if (num_pickels <= 0){
                    minus.visibility = View.GONE
                }
                else{
                    minus.visibility = View.VISIBLE
                }
                progressView.text = num_pickels.toString()
            } catch (e : Exception){
                Toast.makeText(this, "Invalid pickels number", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * update all the fields of the current order
     */
    private fun updateOrder(
        pickelProgress: TextView,
        tahini: SwitchCompat,
        hummus: SwitchCompat,
        commentContent: EditText,
        nameField: TextView
    ) {
        val order = Order()
        order.pickles = pickelProgress.text.toString().toInt()
        order.tahini = tahini.isChecked
        order.hummus = hummus.isChecked
        order.comment = commentContent.text.toString()
        order.name = nameField.text.toString()
        dataBase.uploadOrUpdate(order)
    }
}