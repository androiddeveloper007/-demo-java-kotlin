package me.zsj.interessant.binder.related

import me.zsj.interessant.model.Data
import me.zsj.interessant.model.Header

class HeaderItem {
    var header: Header?
    var data: Data? = null
    var isShowArrowIcon: Boolean = false

    constructor(header: Header, isShowArrowIcon: Boolean) {
        this.header = header
        this.isShowArrowIcon = isShowArrowIcon
    }

    constructor(data: Data, header: Header, isShowArrowIcon: Boolean) {
        this.data = data
        this.header = header
        this.isShowArrowIcon = isShowArrowIcon
    }
}