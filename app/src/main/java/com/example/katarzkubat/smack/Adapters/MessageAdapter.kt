package com.example.katarzkubat.smack.Adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.katarzkubat.smack.Model.Message
import com.example.katarzkubat.smack.R
import com.example.katarzkubat.smack.Services.UserDataService
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by katarz.kubat on 07.02.2018.
 */
class MessageAdapter(val context: Context, val messages: ArrayList<Message>) : RecyclerView.Adapter<MessageAdapter.ViewHolder>() {
    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.bindMessage(context, messages[position])
    }

    override fun getItemCount(): Int {
        return messages.count()
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.message_list_view, parent, false)
        return ViewHolder(view)
    }

    inner class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        val userImage = itemView?.findViewById<ImageView>(R.id.messageUserImage)
        val timeStamp = itemView?.findViewById<TextView>(R.id.messageDateLb)
        val userName = itemView?.findViewById<TextView>(R.id.messageUserNameLb)
        val textMessage = itemView?.findViewById<TextView>(R.id.messageMessageText)

        fun bindMessage(context: Context, message: Message) {
            val resourceId = context.resources.getIdentifier(message.userAvatar, "drawable", context.packageName)
            userImage?.setImageResource(resourceId)
            userImage?.setBackgroundColor(UserDataService.returnAvatarColor(message.avatarColor))
            userName?.text = message.userName
            timeStamp?.text = returnDataString(message.timeStamp)
            textMessage?.text = message.message
        }

        fun returnDataString(isoString: String) : String {

            val isoFormater = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            isoFormater.timeZone = TimeZone.getTimeZone("UTC")
            var convertedDate = Date()

            try {
                convertedDate = isoFormater.parse(isoString)

            } catch (e: ParseException) {
                Log.d("PARSE", "Date can't be parsed")
            }

            val outDateString = SimpleDateFormat("E, h:mm a", Locale.getDefault())
            return outDateString.format(convertedDate)
        }
    }
}