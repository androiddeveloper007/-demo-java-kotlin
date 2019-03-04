package me.zsj.interessant.binder.related

import me.zsj.interessant.model.ItemList

class Card() {
    lateinit var item: ItemList
    constructor(item: ItemList) : this() {
        this.item = item
    }
}