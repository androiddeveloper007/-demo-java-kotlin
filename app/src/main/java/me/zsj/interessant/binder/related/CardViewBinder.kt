package me.zsj.interessant.binder.related

import android.app.Activity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearSnapHelper
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import me.drakeet.multitype.ItemViewBinder
import me.zsj.interessant.R
import me.zsj.interessant.model.ItemList

class CardViewBinder(): ItemViewBinder<Card, CardViewBinder.CardHolder>() {
    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): CardViewBinder.CardHolder {
        return CardHolder(inflater.inflate(R.layout.item_horizontal_list, parent, false))
    }

    override fun onBindViewHolder(holder: CardViewBinder.CardHolder, card: Card) {
        holder.setCards(card.item.data.itemList)
    }

    class CardHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        private var cardList: RecyclerView
        private var cardAdapter: CardAdapter? = null
        private val snapHelper = LinearSnapHelper()
        init {
            cardList = itemView.findViewById<View>(R.id.card_list) as RecyclerView
            val layoutManager = LinearLayoutManager(itemView.context)
            layoutManager.orientation = LinearLayoutManager.HORIZONTAL
            cardList.layoutManager = layoutManager
            cardAdapter = CardAdapter(cardList.context as Activity)
            cardList.adapter = cardAdapter
            snapHelper.attachToRecyclerView(cardList)
        }

        fun setCards(items: List<ItemList>) {
            cardAdapter!!.setItems(items)
            cardAdapter!!.notifyDataSetChanged()
        }
    }

}