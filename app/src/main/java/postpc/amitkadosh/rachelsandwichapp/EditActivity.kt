package postpc.amitkadosh.rachelsandwichapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.lifecycle.Observer
import com.google.android.material.floatingactionbutton.FloatingActionButton

class EditActivity : AppCompatActivity() {
    private lateinit var dataBase: DataBase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        dataBase = this.applicationContext as DataBase
        val order: Order? = dataBase.orderLiveData.value
        //finds views
        val hello: TextView = findViewById(R.id.nameField)
        val plus: TextView = findViewById(R.id.plus)
        val minus: TextView = findViewById(R.id.minus)
        val progressView: TextView = findViewById(R.id.progress)
        val tahini: SwitchCompat = findViewById(R.id.tahini)
        val hummus: SwitchCompat = findViewById(R.id.hummus)
        val commentContent : EditText = findViewById(R.id.comments)
        val deleteButton : FloatingActionButton = findViewById(R.id.delete)
        val saveButton : FloatingActionButton = findViewById(R.id.save)

        //sets views
        setViews(order, hello, tahini, hummus, commentContent, progressView, minus, plus)

        deleteButton.setOnClickListener{
            dataBase.deleteOrder()
        }

        saveButton.setOnClickListener{
            if (order != null) {
                order.pickles = progressView.text.toString().toInt()
                order.tahini = tahini.isChecked
                order.hummus = hummus.isChecked
                order.comment = commentContent.text.toString()
                dataBase.uploadOrUpdate(order)
                Toast.makeText(this, "Your changes have been saved", Toast.LENGTH_SHORT).show()
            }
        }

        plus.setOnClickListener {
            if (order != null){
                try {
                    var num_pickels : Int = order.pickles
                    num_pickels++
                    plus.isEnabled = num_pickels < 10
                    if (num_pickels >= 10){
                        plus.visibility = View.GONE
                    }
                    else{
                        minus.visibility = View.VISIBLE
                    }
                    order.pickles = num_pickels
                    progressView.text = order.pickles.toString()
                } catch (e : Exception){
                    Toast.makeText(this, "Invalid pickels number", Toast.LENGTH_SHORT).show()
                }
            }
        }

        minus.setOnClickListener {
            if (order != null){
                try {
                    var num_pickels : Int = order.pickles.toInt()
                    num_pickels--
                    plus.isEnabled = num_pickels > 0
                    if (num_pickels <= 0){
                        minus.visibility = View.GONE
                    }
                    else{
                        minus.visibility = View.VISIBLE
                    }
                    order.pickles = num_pickels
                    progressView.text = order.pickles.toString()
                } catch (e : Exception){
                    Toast.makeText(this, "Invalid pickels number", Toast.LENGTH_SHORT).show()
                }
            }
        }


            //set observer to the liveData
            dataBase.orderLiveData.observe(this, Observer { it->
                if (it == null){
                    val intent = Intent(this, NewOrderActivity::class.java)
                    startActivity(intent)
                }
                else if (it.status == Status.INPROGRESS){
                    val intent = Intent(this, InProgressActivity::class.java)
                    startActivity(intent)
                }
            })
        }

    /**
     * set the views after change the order
     */
    private fun setViews(
        order: Order?,
        hello: TextView,
        tahini: SwitchCompat,
        hummus: SwitchCompat,
        commentContent: EditText,
        progressView: TextView,
        minus: TextView,
        plus: TextView
    ) {
        val helloStr = "Hii " + (order?.name ?: "")
        hello.text = helloStr
        tahini.isChecked = order?.tahini ?: false
        hummus.isChecked = order?.hummus ?: false
        val comment = order?.comment
        commentContent.setText(comment)
        progressView.text = order?.pickles.toString()

        try {
            val num_pickels: Int = progressView.text.toString().toInt()
            if (num_pickels <= 0) {
                minus.visibility = View.GONE
            }
            if (num_pickels >= 10) {
                plus.visibility = View.GONE
            }
        } catch (e: Exception) {
            Log.d("failed convert", "failed to convert string to int")
        }
    }


}