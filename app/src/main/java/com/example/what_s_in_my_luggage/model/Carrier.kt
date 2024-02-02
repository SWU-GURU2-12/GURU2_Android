package com.example.what_s_in_my_luggage.model

class Carrier {
    var carrierId: Int = 0
    var userName: String = ""

    var title: String = ""
    var destination: String = ""
    var schedule: String = ""

    constructor()

    constructor(carrierId: Int, userName: String, title: String, destination: String, schedule: String) {
        this.carrierId = carrierId
        this.userName = userName
        this.title = title
        this.destination = destination
        this.schedule = schedule
    }

}