package com.example.what_s_in_my_luggage

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.example.what_s_in_my_luggage.model.ListViewItem
import com.example.what_s_in_my_luggage.model.Luggage
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
    private val refLuggage = database.getReference("luggage")
    private val refTravelPlace = database.getReference("travelPlace")
    private val refSavedTemplate = database.getReference("savedTemplate")
    private val refChecklist = database.getReference("checklist")
    private val storage = FirebaseStorage.getInstance()

    // user와 관련된 데이터 리스트
    private var userName = "NaomiWatts" // 현재 로그인한 사용자의 이름
    private var travelPlaceList = arrayListOf<ListViewItem>() // 여행지 리스트
    private var savedTemplateList = arrayListOf<String>() // 내가 저장한 템플릿 (북마크)
    private var luggageList = arrayListOf<String>() // 나의 짐 목록 (마이룸) - LuggageID 저장
    private var postList = arrayListOf<String>() // 발행한 글 목록 - postID 저장 TODO: 임시

    // checklist와 관련된 데이터 리스트
    var allItems = arrayListOf<Items>()
    var recommendation = arrayListOf<Items>()
    var electronics = arrayListOf<Items>()
    var inFlightEssentials = arrayListOf<Items>()
    var clothes = arrayListOf<Items>()
    var otherClothes = arrayListOf<Items>()
    var care = arrayListOf<Items>()
    var food = arrayListOf<Items>()

    // 임시 데이터
    var tempLuggage: Luggage? = null
    var savedTemplateListView = arrayListOf<ListViewItem>()

    // TODO: 로그인 후 init 할 것.
    fun init(userName: String = "NaomiWatts") {
        this.userName = userName
    }

    fun clear() {
        userName = ""
        travelPlaceList.clear()
        savedTemplateList.clear()
        luggageList.clear()
        postList.clear()
    }

// User
    fun generateUser() {
        addSavedTemplate("기본 템플릿")
    }

    fun setUserName(userName: String) {
        this.userName = userName
    }

    fun getUserName(): String {
        return userName
    }

    // Travel Place List
    fun setTravelPlaceList() {
        travelPlaceList.clear()

        refTravelPlace.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (data in snapshot.children) {
                    val place = data.getValue(ListViewItem::class.java)
                    travelPlaceList.add(place!!)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase_error", "!!! Travel Place List: ${error.message}")
            }
        })
    }

    fun getTravelPlaceList(): ArrayList<ListViewItem> {
        return travelPlaceList
    }

    // Saved Template List
    fun setSavedTemplateList() {
        savedTemplateList.clear()

        refSavedTemplate.child(userName).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (data in snapshot.children) {
                    savedTemplateList.add(data.getValue(String::class.java)!!)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase_error", "!!! Saved Template List: ${error.message}")
            }
        })
    }

    fun getSavedTemplateList(): ArrayList<String> {
        return savedTemplateList
    }

    fun setSavedTemplateListView() { // add carrier fragment에서 ui에 그리기 위한 데이터
        if (savedTemplateList.isEmpty()) {
            setSavedTemplateList()
        }

        if (savedTemplateListView.isNotEmpty()) return
        Log.e("Firebase_error", "!!! setSavedTemplateListView is empty")

        for (luggageID in savedTemplateList) {
            refLuggage.child(luggageID).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val luggage = snapshot.getValue(Luggage::class.java)
                    val listViewItem = ListViewItem(luggage!!.title, luggage.userName)
                    savedTemplateListView.add(listViewItem)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("Firebase_error", "!!! Luggage : ${error.message}")
                }
            })
        }
    }

    @JvmName("getTempLuggage1")
    fun getSavedTemplateListView(): ArrayList<ListViewItem> {
        if (savedTemplateListView.isEmpty())
        {
            setSavedTemplateListView()
            return savedTemplateListView
        }
        Log.e("Firebase_error", "!!! getSavedTemplateListView is empty")
        return savedTemplateListView
    }

    fun addSavedTemplate(luggageID: String) {
        savedTemplateList.add(luggageID)
        saveSavedTemplateList()
    }

    fun removeSavedTemplate(luggageID: String) {
        savedTemplateList.remove(luggageID)
        saveSavedTemplateList()
    }

    fun saveSavedTemplateList() {
        refSavedTemplate.child(userName).setValue(savedTemplateList)
    }

    // Luggage
    fun setLuggageList() { // todo
        luggageList.clear()
        refLuggage.get().addOnSuccessListener {
            for (data in it.children) {
                // data의 key값을 luggagelist에 추가
                luggageList.add(data.key!!)
            }
        }
    }
    @JvmName("setTempLuggage1")
    fun setTempLuggage(luggage: Luggage) {
        tempLuggage = luggage
        tempLuggage!!.luggageID = generateLuggageID()
    }

    @JvmName("getTempLuggage1")
    fun getTempLuggage(): Luggage? {
        return tempLuggage
    }

    fun saveLuggage() {
        if (tempLuggage == null) return
        luggageList.add(tempLuggage!!.luggageID)
        refLuggage.child(tempLuggage!!.luggageID).setValue(tempLuggage)
    }

    fun generateLuggageID(): String {
        if(luggageList.isEmpty()) {
            setLuggageList()
        }
        return "luggage${luggageList.size + 1}"
    }

    // Pack Luggage & Checklist
    fun setCurrentTime() {
        val sdf = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault())
        val time = Date()
        val currentTime = sdf.format(time)

        tempLuggage?.currentTime = currentTime
    }

    fun removeCurrentTime() {
        tempLuggage?.currentTime = ""
    }

    fun setItemListInLuggage(item: Items) {
        if (tempLuggage?.itemListInLuggage == null) {
            tempLuggage?.itemListInLuggage = ArrayList()
        }

        // 기존 리스트에 새로운 아이템 추가
        tempLuggage?.itemListInLuggage?.add(item.name)
    }

    fun removeItemListInLuggage() {
        tempLuggage?.itemListInLuggage?.clear()
    }

    fun uploadImageToFirebaseStorage(bitmap: Bitmap, fileName: String) {
        val storageRef = storage.reference
        val imagesRef = storageRef.child("captures/$fileName.jpg")

        // ByteArrayOutputStream을 사용하여 Bitmap 이미지를 byte 배열로 변환
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos)
        val data = baos.toByteArray()

        // Firebase Storage에 이미지 업로드
        val uploadTask = imagesRef.putBytes(data)

        uploadTask.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // 업로드 성공
                Log.d("Firebase_upload", "Image upload successful")
            } else {
                // 업로드 실패
                Log.e("Firebase_upload", "Image upload failed: ${task.exception}")
            }
        }
    }

    fun removeScreenshotFromFirebase(fileName: String) {
        val storageRef = storage.reference
        val imagesRef = storageRef.child("captures/${fileName}\\_capture.jpg")

        imagesRef.delete().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("Firebase_delete", "Screenshot delete successful")
            } else {
                Log.e("Firebase_delete", "Screenshot delete failed: ${task.exception}")
            }
        }.addOnFailureListener { exception ->
            Log.e("Firebase_delete_error", "Screenshot delete failed: $exception")
        }

        removeCurrentTime()
    }

    fun setItemLists() {
        // 백그라운드 스레드에서 데이터베이스에서 아이템 정보를 가져오기
        CoroutineScope(Dispatchers.IO).launch {
            val num = 0

            // Firebase Storage 관리 객체 소환
            val firebaseStorage = FirebaseStorage.getInstance()

            // 저장소의 최상위 참조 객체 얻어오기
            val rootRef = firebaseStorage.reference

            // StorageReference 리스트 생성
            val imageRefs: List<StorageReference> = (1..15).map { index ->
                rootRef.child("items/item_image_removebg_${num + index}.png")
            }

            // 아이템 목록 생성
            allItems = ArrayList(
                listOf(
                    Items(imageRefs[0], "어댑터"),
                    Items(imageRefs[1], "카메라"),
                    Items(imageRefs[2], "보조배터리"),
                    Items(imageRefs[3], "여권"),
                    Items(imageRefs[4], "개인 가방"),
                    Items(imageRefs[5], "유럽 돈"),
                    Items(imageRefs[6], "겨울 상의"),
                    Items(imageRefs[7], "겨울 하의"),
                    Items(imageRefs[8], "머플러"),
                    Items(imageRefs[9], "모자"),
                    Items(imageRefs[10], "부츠"),
                    Items(imageRefs[11], "화장품"),
                    Items(imageRefs[12], "칫솔&치약"),
                    Items(imageRefs[13], "스킨케어"),
                    Items(imageRefs[14], "컵라면"),
                )
            )

            recommendation = ArrayList(
                listOf(
                    Items(imageRefs[3], "여권"),
                    Items(imageRefs[5], "유럽 돈"),
                    Items(imageRefs[0], "어댑터"),
                    Items(imageRefs[2], "보조배터리"),
                    Items(imageRefs[12], "칫솔&치약"),
                )
            )

            electronics = ArrayList(
                listOf(
                    Items(imageRefs[0], "어댑터"),
                    Items(imageRefs[1], "카메라"),
                    Items(imageRefs[2], "보조배터리")
                )
            )

            inFlightEssentials = ArrayList(
                listOf(
                    Items(imageRefs[3], "여권"),
                    Items(imageRefs[2], "보조배터리"),
                    Items(imageRefs[4], "개인 가방"),
                    Items(imageRefs[5], "현금")
                )
            )

            clothes = ArrayList(
                listOf(
                    Items(imageRefs[6], "겨울 상의"),
                    Items(imageRefs[7], "겨울 하의")
                )
            )

            otherClothes = ArrayList(
                listOf(
                    Items(imageRefs[8], "머플러"),
                    Items(imageRefs[9], "모자"),
                    Items(imageRefs[10], "부츠")
                )
            )

            care = ArrayList(
                listOf(
                    Items(imageRefs[11], "화장품"),
                    Items(imageRefs[12], "칫솔&치약"),
                    Items(imageRefs[13], "스킨케어")
                )
            )

            food = ArrayList(
                listOf(
                    Items(imageRefs[14], "컵라면")
                )
            )
        }
    }
}