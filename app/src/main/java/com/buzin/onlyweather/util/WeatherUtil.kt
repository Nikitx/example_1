package com.buzin.onlyweather.util

import android.R
import android.content.ContentProviderResult
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Color
import android.graphics.PorterDuff
import android.net.ConnectivityManager
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import java.util.*

class WeatherUtil {

    companion object {

        private val LOG_TAG = "Weather log ===>"
        private val LOG_NULL = "log is null :("

        fun toLog(message: String?) {
            try {
                message?.let { Log.d(LOG_TAG, it) }
            } catch (e: Exception) {
                Log.d(LOG_TAG, LOG_NULL)
            }
        }

        private fun toLogAny(any: Any) {
            toLog(any.toString())
        }

        fun toLog(l: Locale) {
            val sb = StringBuilder()
            sb.append("Locale = ").append(l.country)
            sb.append(SharedStrings.NEW_LINE_C)
            sb.append("\tgetDisplayCountry = ").append(l.displayCountry)
            sb.append(SharedStrings.NEW_LINE_C)
            sb.append("\tgetDisplayLanguage = ").append(l.displayLanguage)
            sb.append(SharedStrings.NEW_LINE_C)
            sb.append("\tgetDisplayName = ").append(l.displayName)
            sb.append(SharedStrings.NEW_LINE_C)
            sb.append("\tgetDisplayVariant = ").append(l.displayVariant)
            sb.append(SharedStrings.NEW_LINE_C)
            sb.append("\tgetLanguage = ").append(l.language)
            sb.append(SharedStrings.NEW_LINE_C)
            sb.append("\tgetVariant = ").append(l.variant)
            toLogAny(sb)
        }

        fun toLog(e: Throwable) {
            val sb = StringBuilder()
            sb.append(e.message)
            sb.append(SharedStrings.NEW_LINE_C)
            sb.append(e.javaClass.name)
            for (s in e.stackTrace) {
                sb.append(SharedStrings.NEW_LINE_C)
                sb.append(s.toString())
            }
            Log.e(LOG_TAG, sb.toString())
        }

        fun toLog(intent: Intent) {
            val sb = StringBuilder(intent.toString())
            if (!TextUtils.isEmpty(intent.action)) {
                sb.append(SharedStrings.NEW_LINE_C)
                sb.append("\t getAction = ")
                sb.append(intent.action)
            }
            val uri =
                if (intent.data == null) null else intent.data.toString()
            if (!TextUtils.isEmpty(uri)) {
                sb.append(SharedStrings.NEW_LINE_C)
                sb.append("\tgetData = ")
                sb.append(uri)
            }
            toLogAny(sb)
            toLog(intent.extras)
        }

        fun toLog(b: Bundle?) {
            if (b == null || b.isEmpty) {
                return
            }
            val sb = StringBuilder("Bundle")
            for (key in b.keySet()) {
                sb.append(SharedStrings.NEW_LINE_C)
                sb.append(key)
                sb.append(SharedStrings.TAB_C)
                sb.append(b[key])
            }
            toLogAny(sb)
        }

        fun toLog(cv: ContentValues?) {
            val sb = StringBuilder()
            sb.append("\tContentValues\n")
            if (cv != null) {
                for (key in cv.keySet()) {
                    sb.append(key)
                    sb.append(SharedStrings.COLON_C)
                    sb.append(cv[key])
                    sb.append(SharedStrings.NEW_LINE_C)
                }
            }
            sb.append(SharedStrings.NEW_LINE_C)
            toLogAny(sb)
        }

        fun toLog(cvs: Array<ContentValues?>) {
            for (cv in cvs) {
                toLog(cv)
            }
        }

        fun toLog(calendar: Calendar) {
            val sb = StringBuilder()
            sb.append(SharedStrings.SPACE_C)
            sb.append(calendar[Calendar.YEAR])
            sb.append(SharedStrings.SPACE_C)
            sb.append(calendar[Calendar.MONTH])
            sb.append(SharedStrings.SPACE_C)
            sb.append(calendar[Calendar.DAY_OF_MONTH])
            sb.append(SharedStrings.SPACE_C)
            sb.append(calendar[Calendar.HOUR_OF_DAY])
            sb.append(SharedStrings.SPACE_C)
            sb.append(calendar[Calendar.MINUTE])
            sb.append(SharedStrings.SPACE_C)
            sb.append(calendar[Calendar.SECOND])
            sb.append(SharedStrings.SPACE_C)
            sb.append(calendar[Calendar.MILLISECOND])
            toLogAny(sb)
        }

        fun toLog(c: Cursor) {
            val sb = StringBuilder()
            c.moveToFirst()
            while (!c.isAfterLast) {
                sb.append("< < < < < < <\n")
                for (i in 0 until c.columnCount) {
                    val columnName = c.getColumnName(i)
                    sb.append(columnName)
                    sb.append(SharedStrings.TAB_C)
                    sb.append(c.getString(c.getColumnIndex(columnName)))
                    sb.append(SharedStrings.NEW_LINE_C)
                }
                sb.append(" > > >\n \n")
                c.moveToNext()
            }
            toLogAny(sb)
        }

        fun toLog(results: Array<ContentProviderResult>) {
            for (result in results) {
                toLog(result)
            }
        }

        fun toLog(result: ContentProviderResult) {
            val sb = StringBuilder()
            sb.append("uri")
            sb.append(result.uri)
            sb.append(SharedStrings.NEW_LINE_C)
            sb.append("count")
            sb.append(result.count)
            toLogAny(sb)
        }

        fun showToast(context: Context, string: String) {
            //Toast.makeText(context, string, Toast.LENGTH_SHORT).show()
            val toast: Toast = Toast.makeText(context, string, Toast.LENGTH_SHORT)
            val view = toast.view
            view!!.background.setColorFilter(
                ContextCompat.getColor(context, R.color.holo_orange_dark),
                PorterDuff.Mode.SRC_ATOP
            )
            val text = view.findViewById<TextView>(R.id.message)
            text.setTextColor(Color.WHITE)
            toast.show()
        }

        fun showSnackbar(view: View, text: String) {
            Snackbar.make(view, text, Snackbar.LENGTH_LONG).show()
        }

        fun isNetworkAvailable(context: Context): Boolean {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            return connectivityManager.activeNetworkInfo != null
        }

    }
}