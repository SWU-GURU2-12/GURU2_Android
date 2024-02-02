package com.example.what_s_in_my_luggage

import android.content.Context
import com.example.what_s_in_my_luggage.model.ListViewItem
import com.example.what_s_in_my_luggage.model.Luggage
import com.example.what_s_in_my_luggage.model.SavedTemplate
import com.google.firebase.Firebase
import com.google.firebase.database.database
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

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
    private var savedTemplateList = arrayListOf<ListViewItem>() // 내가 저장한 템플릿 (북마크)
    private var luggageList = arrayListOf<String>() // 나의 짐 목록 (마이룸) - LuggageID 저장
    private var postList = arrayListOf<String>() // 발행한 글 목록 - postID 저장 TODO: 임시

    // TODO: 로그인 후 init 할 것.
    fun init(userName: String = "NaomiWatts"): Boolean {
        this.userName = userName
        setTravelPlaceList()
        return true
    }

    fun clear() {
        userName = ""
        travelPlaceList.clear()
        savedTemplateList.clear()
        luggageList.clear()
        postList.clear()
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
            // savedLuggageIdList에 저장된 luggageID를 가져와서 listView에 추가
            if (savedTemplate != null) {
                for (luggageID in savedTemplate.SavedLuggageIdList) {
                    // luggage database에서 title과 userName 가져오기
                    refLuggage.child(luggageID).get().addOnSuccessListener {
                        val luggage = it.getValue(Luggage::class.java)
                        savedTemplateList.add(ListViewItem(luggage!!.title, luggage.userName))
                    }
                }
            }
        }
    }

    fun getSavedTemplateList(): ArrayList<ListViewItem> {
        if (savedTemplateList.isEmpty()) {
            setSavedTemplateList()
        }
        return savedTemplateList
    }

}