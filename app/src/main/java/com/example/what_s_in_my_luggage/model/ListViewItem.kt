package com.example.what_s_in_my_luggage.model

class ListViewItem {
    var title: String = ""
    var subTitle: String = ""

    constructor()

    constructor(title: String, subTitle: String) {
        this.title = title
        this.subTitle = subTitle
    }
}
