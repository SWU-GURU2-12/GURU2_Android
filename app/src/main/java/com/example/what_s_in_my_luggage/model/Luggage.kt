package com.example.what_s_in_my_luggage.model

import com.example.what_s_in_my_luggage.Items

class Luggage {
    var luggageID: String = ""
    var userName: String = ""
    var carriername: String = ""
    var destination: String = ""
    var schedule: String = ""
    var itemListInLuggage: MutableList<String>? = null
    var currentTime: String = ""
    var imageURL: String? = null  // 이미지 URL에 대한 기본값을 null로 설정
    var title: String = ""
    var content: String = ""

    // 파라미터가 없는 기본 생성자
    constructor()

    // 모든 필드를 초기화하는 파라미터가 있는 생성자
    constructor(carrierId: String, userName: String, carriername: String, destination: String, schedule: String, title: String, content: String, imageURL: String?) {
        this.luggageID = carrierId
        this.userName = userName
        this.carriername = carriername
        this.destination = destination
        this.schedule = schedule
        this.title = title
        this.content = content
        this.imageURL = imageURL
    }
}
