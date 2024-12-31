package com.example.project1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.my_calendr_chat.CardData

class CardAdapter(private val cardDataList: List<CardData>) : RecyclerView.Adapter<CardAdapter.CardViewHolder>() {

    class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profileImage: ImageView = itemView.findViewById(R.id.profile_image)
        val userName: TextView = itemView.findViewById(R.id.user_name)
        val ratingImage: ImageView = itemView.findViewById(R.id.rating_image)
        val firstComment: TextView = itemView.findViewById(R.id.first_comment)
        val secondComment: TextView = itemView.findViewById(R.id.second_comment)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cardview, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val cardData = cardDataList[position]

        holder.profileImage.setImageResource(cardData.profileImage)
        holder.userName.text = cardData.userName
        holder.ratingImage.setImageResource(cardData.ratingImage)
        holder.firstComment.text = cardData.firstComment
        holder.secondComment.text = cardData.secondComment
    }

    override fun getItemCount(): Int {
        return cardDataList.size
    }
}
