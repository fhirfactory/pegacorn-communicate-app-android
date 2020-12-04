package im.vector.calls.dialer

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.ImageViewCompat
import im.vector.R
import kotlinx.android.synthetic.main.dialer_key.view.*


class DialerKey (context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {
    init {
        View.inflate(context, R.layout.dialer_key, this)

        context.theme.obtainStyledAttributes(
                attrs,
                R.styleable.DialerKey,
                0, 0
        ).apply {
            try {
                val drawable = getDrawable(R.styleable.DialerKey_key_image)
                if(drawable!=null){
                    keyImage.setImageDrawable(drawable)
                }else {
                    keyNumberTextView.text = getString(R.styleable.DialerKey_key_number)
                    keyCharTextView.text = getString(R.styleable.DialerKey_key_text)
                }

                getDrawable(R.styleable.DialerKey_key_background)?.let {
                    textLayout.background = it
                }

                ImageViewCompat.setImageTintList(keyImage, ColorStateList.valueOf(getColor(R.styleable.DialerKey_key_tint, Color.BLACK)))
            } finally {
                recycle()
            }
        }
    }
}