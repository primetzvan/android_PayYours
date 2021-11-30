package at.htl.payyours

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts

class MainActivity : AppCompatActivity() {

  var payment : Payment = Payment(0,0,0.0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.menu_main,menu)
    return super.onCreateOptionsMenu(menu)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    var result = when(item?.itemId){
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
    TODO("Not yet implemented")
  }


}
