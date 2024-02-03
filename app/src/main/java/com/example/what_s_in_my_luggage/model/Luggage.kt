package com.example.what_s_in_my_luggage.model

import com.example.what_s_in_my_luggage.Items

class Luggage {
    var luggageID: String = ""
    var userName: String = ""

    var title: String = "" // 캐리어 이름
    var destination: String = ""
    var schedule: String = ""

    var itemListInLuggage: MutableList<String>? = null
    var currentTime: String = ""

    constructor()

    constructor(carrierId: String, userName: String, title: String, destination: String, schedule: String) {
        this.luggageID = carrierId
        this.userName = userName
        this.title = title
        this.destination = destination
        this.schedule = schedule
    }
}