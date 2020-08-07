package my.rockpilgrim.pushovermessenger.adapter

import android.widget.TextView
import androidx.databinding.BindingAdapter
import my.rockpilgrim.pushovermessenger.R
import java.util.*

@BindingAdapter("app:data")
fun setCardData(textView: TextView, date: String?) {
    if (date == null) {
        textView.text = textView.context.getString(R.string.no_data)
    }else
        textView.text = date.toString()
}