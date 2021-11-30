package at.htl.payyours

import android.content.Context
import android.media.audiofx.Equalizer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_settings.*
import java.text.DecimalFormat
import java.util.*

class SettingsActivity : AppCompatActivity() {

  companion object{

    val LOG_TAG = SettingsActivity::class.java.simpleName

    val PREFERENCE_FILENAME = "PayYoursPreferences"
    val PRICE_PER_UNIT_KEY = "PRICE_PER_UNIT"
    val PLAYERS_KEY = "DEFAULT_PLAYERS"
    val COURTS_KEY = "DEFAULT_COURTS"

    val DEFAULT_COURTS = 5
    val DEFAULT_PLAYERS = 2
    val DEFAULT_PRICE_PER_UNIT = 1.0F

    fun getStoredPayment(context: Context) : Payment{
      val preferences = context.applicationContext
        .getSharedPreferences(PREFERENCE_FILENAME, Context.MODE_PRIVATE)

      val payment = Payment(
        preferences.getInt(COURTS_KEY, DEFAULT_COURTS),
        preferences.getInt(PLAYERS_KEY, DEFAULT_PLAYERS),
        preferences.getFloat(PRICE_PER_UNIT_KEY, DEFAULT_PRICE_PER_UNIT).toDouble()
      )

      Log.d(LOG_TAG, "getStoredPayment(): $payment")
      return payment

    }

  }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        btn_ok.setOnClickListener { onSave() }
        val payment = getStoredPayment(this)
        edit_price.setText("${payment.pricePerUnit}")
        edit_player.setText("${payment.players}")
        edit_place.setText("${payment.courts}")
    }

  private fun onSave() {

    val decimalFormat = DecimalFormat.getInstance(Locale.getDefault())
    val pricePerUnit = decimalFormat.parse(this.edit_price.text.toString()).toFloat()
    val players = decimalFormat.parse(this.edit_player.text.toString()).toInt()
    val places = decimalFormat.parse(this.edit_place.text.toString()).toInt()
    val preferences = this.applicationContext
      .getSharedPreferences(PREFERENCE_FILENAME, Context.MODE_PRIVATE)
      .edit()
    preferences.putFloat(PRICE_PER_UNIT_KEY, pricePerUnit)
    preferences.putInt(PLAYERS_KEY, players)
    preferences.putInt(COURTS_KEY, places)
    preferences.commit()

    finish()
  }

}
