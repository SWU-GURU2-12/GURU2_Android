package com.example.what_s_in_my_luggage.model

import com.example.what_s_in_my_luggage.Items

class Luggage {
    var luggageID: String = ""
    var userName: String = ""

    var carriername: String = "" // 캐리어 이름
    var destination: String = ""
    var schedule: String = ""

    var itemListInLuggage: MutableList<String>? = null
    var currentTime: String = ""
    var imageURL: String? = null

    var title: String = ""
    var content: String = ""



    constructor()

    constructor(carrierId: String, userName: String, carriername: String, destination: String, schedule: String, title: String, content: String) {
        this.luggageID = carrierId
        this.userName = userName
        this.carriername = carriername
        this.destination = destination
        this.schedule = schedule
        this.title = title
        this.content = content

        }
    }