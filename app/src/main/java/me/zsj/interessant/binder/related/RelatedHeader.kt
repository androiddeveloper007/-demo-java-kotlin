package me.zsj.interessant.binder.related

import me.zsj.interessant.model.Header

class RelatedHeader {
    var header: Header
    var related: Boolean = false

    constructor(header: Header, related: Boolean) {
        this.header = header
        this.related = related
    }
}