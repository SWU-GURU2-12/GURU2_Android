package com.example.what_s_in_my_luggage

import android.content.Context
import android.util.Log
import com.example.what_s_in_my_luggage.model.ListViewItem
import com.example.what_s_in_my_luggage.model.Luggage
import com.example.what_s_in_my_luggage.model.SavedTemplate
import com.google.firebase.Firebase
import com.google.firebase.database.database

// TODO: 코루틴
class UserDataManager constructor() {
    companion object {     // Singleton
        private var instance: UserDataManager? = null
        private lateinit var context: Context // Context: 안드로이드 애플리케이션에서 애플리케이션 환경 정보를 제공하고 다양한 시스템 서비스에 액세스할 수 있도록 함
        fun getInstance(context: Context): UserDataManager {
            if (instance == null) {
                instance = UserDataManager()
            }
            this.context = context
            return instance!!
        }
    }

    // Database
    private val database = Firebase.database
    private val refUsers = database.getReference("users")
    private val refLuggage = database.getReference("Luggage")
    private val refTravelPlace = database.getReference("travelPlace")
    private val refSavedTemplate = database.getReference("savedTemplate")

    // user와 관련된 데이터 리스트
    private var userName = "" // 현재 로그인한 사용자의 이름
    private var travelPlaceList = arrayListOf<ListViewItem>() // 여행지 리스트
    private var savedTemplateList = arrayListOf<String>() // 내가 저장한 템플릿 (북마크)
    private var luggageList = arrayListOf<String>() // 나의 짐 목록 (마이룸) - LuggageID 저장
    private var postList = arrayListOf<String>() // 발행한 글 목록 - postID 저장 TODO: 임시

    // TODO: 로그인 후 init 할 것.
    fun init(userName: String = "NaomiWatts") {
        this.userName = userName
        setTravelPlaceList()
        setSavedTemplateList()
    }

    fun clear() {
        userName = ""
        travelPlaceList.clear()
        savedTemplateList.clear()
        luggageList.clear()
        postList.clear()
    }
// User
    fun getUserName(): String {
        return userName
    }

// Travel Place List
    fun setTravelPlaceList() {
        refTravelPlace.get().addOnSuccessListener {
            for (data in it.children) {
                val place = data.getValue(ListViewItem::class.java)
                travelPlaceList.add(place!!)
            }
        }
    }

    fun getTravelPlaceList(): ArrayList<ListViewItem> {
        if (travelPlaceList.isEmpty()) {
            setTravelPlaceList()
        }
        return travelPlaceList
    }

// Saved Template List
    fun setSavedTemplateList() {
        // savedTemplate 노드의 key 값은 userName
        refSavedTemplate.child(userName).get().addOnSuccessListener {
            val savedTemplate = it.getValue(SavedTemplate::class.java)
            if (savedTemplate != null) {
                for (luggageID in savedTemplate.SavedLuggageIdList) {
                    savedTemplateList.add(luggageID)
                    Log.e("SavedTemplate", luggageID)
                }
            }
        }
    }

    // add carrier fragment에서 ui에 그리기 위한 데이터
    fun getSavedTemplateListView(): ArrayList<ListViewItem> {
        if (savedTemplateList.isEmpty()) {
            setSavedTemplateList()
        }
        val temp = arrayListOf<ListViewItem>()
        for (luggageID in savedTemplateList) {
            refLuggage.child(luggageID).get().addOnSuccessListener {
                val luggage = it.getValue(Luggage::class.java)
                val listViewItem = ListViewItem(luggage!!.title, luggage.userName)
                temp.add(listViewItem)
            }
        }
        return temp
    }

    fun addSavedTemplate(luggageID: String) {
        savedTemplateList.add(luggageID)
    }

}