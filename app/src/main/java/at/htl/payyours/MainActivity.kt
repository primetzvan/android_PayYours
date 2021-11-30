package at.htl.payyours

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

  companion object {
    private val PAYMENT_KEY = "PAY_YOURS_PAYMENT"
  }

  var payment: Payment = Payment(0, 0, 0.0)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    btn_place_plus.setOnClickListener { add(edit_places_main, 1) }
    btn_minus_place.setOnClickListener { add(edit_places_main, -1) }
    btn_player_plus.setOnClickListener { add(edit_player_main, 1) }
    btn_minus_player.setOnClickListener { add(edit_player_main, -1) }

    if(savedInstanceState == null){
      payment = SettingsActivity.getStoredPayment(this)
    }else{
      savedInstanceState.get(PAYMENT_KEY) as Payment
    }

    updatePaymentTextView()

  }

  override fun onSaveInstanceState(outState: Bundle) {
    outState.putSerializable(PAYMENT_KEY,payment)
    super.onSaveInstanceState(outState)
  }

  override fun onRestoreInstanceState(savedInstanceState: Bundle) {
    super.onRestoreInstanceState(savedInstanceState)
    payment = savedInstanceState.getSerializable(PAYMENT_KEY) as Payment
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.menu_main, menu)
    return super.onCreateOptionsMenu(menu)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    var result = when (item?.itemId) {
      R.id.menu_item_quit -> {
        finish()
        true
      }
      R.id.menu_item_settings -> {
        Log.d("MainActivity", "Settings menu called")
        //startActivity(Intent(this,SettingsActivity::class.java))
        val intent = Intent(this, SettingsActivity::class.java)
        getResult.launch(intent)
        true
      }
      else -> super.onOptionsItemSelected(item)
    }
    return result
  }

  // Receiver
  private val getResult =
    registerForActivityResult(
      ActivityResultContracts.StartActivityForResult()
    ) {
      if (it.resultCode == Activity.RESULT_OK) {
        val value = it.data?.getStringExtra("input")
      }
      payment = SettingsActivity.getStoredPayment(this)
      updatePaymentTextView()
    }

  private fun updatePaymentTextView() {

    edit_places_main.setText("${payment.courts}")
    edit_player_main.setText("${payment.players}")
    resultView.setText(String.format("%.2f", payment.amount))
    btn_minus_player.isEnabled = payment.players > 1
    btn_minus_place.isEnabled = payment.courts > 1

  }

  private fun add(textView: TextView, value: Int) {
    val newValue = textView.text.toString().toInt() + value
    if(newValue >= 0) {
      textView.text = "$newValue"
      updatePayment()
      updatePaymentTextView()
    }
  }

  private fun updatePayment() {
    payment = Payment(
      edit_places_main.text.toString().toInt(),
      edit_player_main.text.toString().toInt(),
      payment.pricePerUnit
    )
  }


}
