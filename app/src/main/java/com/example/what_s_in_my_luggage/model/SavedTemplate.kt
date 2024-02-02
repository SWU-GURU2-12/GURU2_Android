package com.example.what_s_in_my_luggage.model

class SavedTemplate {
    var userName: String = ""
    var SavedLuggageIdList: ArrayList<String> = arrayListOf()

    constructor()

    constructor(userName: String, SavedLuggageIdList: ArrayList<String> = arrayListOf()) {
        this.userName = userName
        this.SavedLuggageIdList = SavedLuggageIdList
    }

    fun addTemplate(luggageID: String) {
        this.SavedLuggageIdList.add(luggageID)
    }
}