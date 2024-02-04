package com.example.what_s_in_my_luggage

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.what_s_in_my_luggage.R.*

class PackLuggageFragment : Fragment() {
    var packingFrameActivity: PackingFrameActivity? = null

//    private lateinit var btnDepartureCal: Button
    private lateinit var itemListRecyclerView: RecyclerView
//    private lateinit var itemAdapter: ItemListAdapter
    private lateinit var allItemsBtn: Button
    private lateinit var recommendationBtn: Button
    private lateinit var electronicsBtn: Button
    private lateinit var inFlightEssentialsBtn: Button
    private lateinit var clothesBtn: Button
    private lateinit var otherClothesBtn: Button
    private lateinit var careBtn: Button
    private lateinit var foodBtn: Button
//    private val itemAdapter = ItemListAdapter(emptyList(), requireContext())
    private lateinit var itemAdapter: ItemListAdapter
//    private lateinit var ItemListAdapter itemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        setFragmentResultListener ?
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_pack_luggage, container, false)

        itemListRecyclerView = view.findViewById<RecyclerView>(R.id.itemListRecyclerView)
        allItemsBtn = view.findViewById<Button>(R.id.allItemsBtn)
        recommendationBtn = view.findViewById<Button>(R.id.recommendationBtn)
        electronicsBtn = view.findViewById<Button>(R.id.electronicsBtn)
        inFlightEssentialsBtn = view.findViewById<Button>(R.id.inFlightEssentialsBtn)
        clothesBtn = view.findViewById<Button>(R.id.clothesBtn)
        otherClothesBtn = view.findViewById<Button>(R.id.otherClothesBtn)
        careBtn = view.findViewById<Button>(R.id.careBtn)
        foodBtn = view.findViewById<Button>(R.id.foodBtn)
//        itemAdapter = ItemListAdapter(emptyList(), requireContext())
//        itemAdapter(activity)

        itemListRecyclerView.layoutManager = GridLayoutManager(requireContext(), 4)

        // 아이템 목록에 들어갈 아이템 객체 생성 및 어댑터 연결
        getItemListAdapter()

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if(context is PackingFrameActivity) {
            packingFrameActivity = context
        }
    }

        private fun getItemListAdapter() {
        // Glide에 전달할 Context 저장
        val contextForGlide = requireContext()
//        var dataManager = UserDataManager.getInstance(this)
//        dataManager.setItemLists()
        UserDataManager.getInstance(requireContext()).setItemLists()
        Log.d("itemlist_111","ok")

        // 짐꾸리기 페이지 로딩되면 전체 메뉴 바로 보여줌
//        itemAdapter = ItemListAdapter(dataManager.allItems, contextForGlide)
//        Log.d("itemlist_222","ok")
//        itemListRecyclerView.adapter = itemAdapter
//        Log.d("itemlist_333","ok")

        // 각 아이템 목록을 버튼에 연결
        allItemsBtn.setOnClickListener {
            itemListRecyclerView.adapter = ItemListAdapter(UserDataManager.getInstance(requireContext()).allItems, contextForGlide)
        }
        Log.d("itemlist_4","ok")

        recommendationBtn.setOnClickListener {
            itemListRecyclerView.adapter = ItemListAdapter(UserDataManager.getInstance(requireContext()).recommendation, contextForGlide)
        }

        electronicsBtn.setOnClickListener {
            itemListRecyclerView.adapter = ItemListAdapter(UserDataManager.getInstance(requireContext()).electronics, contextForGlide)
        }
        Log.d("itemlist_5","ok")

        inFlightEssentialsBtn.setOnClickListener {
            itemListRecyclerView.adapter = ItemListAdapter(UserDataManager.getInstance(requireContext()).inFlightEssentials, contextForGlide)
        }
        Log.d("itemlist_6","ok")

        clothesBtn.setOnClickListener {
            itemListRecyclerView.adapter = ItemListAdapter(UserDataManager.getInstance(requireContext()).clothes, contextForGlide)
        }
        Log.d("itemlist_7","ok")

        otherClothesBtn.setOnClickListener {
            itemListRecyclerView.adapter = ItemListAdapter(UserDataManager.getInstance(requireContext()).otherClothes, contextForGlide)
        }
        Log.d("itemlist_8","ok")

        careBtn.setOnClickListener {
            itemListRecyclerView.adapter = ItemListAdapter(UserDataManager.getInstance(requireContext()).care, contextForGlide)
        }
        Log.d("itemlist_9","ok")

        foodBtn.setOnClickListener {
            itemListRecyclerView.adapter = ItemListAdapter(UserDataManager.getInstance(requireContext()).food, contextForGlide)
        }
        Log.d("itemlist_10","ok")
    }

//    private fun getItemListAdapter() {
//        println("getItemListAdapter")
//        // Glide에 전달할 Context 저장
////        val contextForGlide = requireContext()
//        UserDataManager.getInstance(requireContext()).setItemLists()
//
//        // 짐꾸리기 페이지 로딩되면 전체 메뉴 바로 보여줌
//        ItemList.getItemAdapter().list = UserDataManager.getInstance(requireContext()).allItems
//        itemListRecyclerView.adapter = itemAdapter
//
//        // 각 아이템 목록을 버튼에 연결
//        allItemsBtn.setOnClickListener {
//            ItemList.getItemAdapter().list = UserDataManager.getInstance(requireContext()).allItems
//            itemAdapter.notifyDataSetChanged()
//        }
//
//        electronicsBtn.setOnClickListener {
//            ItemList.getItemAdapter().list = UserDataManager.getInstance(requireContext()).electronics
//            itemAdapter.notifyDataSetChanged()
////            itemListRecyclerView.adapter = ItemListAdapter(UserDataManager.getInstance(requireContext()).electronics, contextForGlide)
//        }
//
//        inFlightEssentialsBtn.setOnClickListener {
//            ItemList.getItemAdapter().list = UserDataManager.getInstance(requireContext()).inFlightEssentials
//            itemAdapter.notifyDataSetChanged()
////            itemListRecyclerView.adapter = ItemListAdapter(UserDataManager.getInstance(requireContext()).inFlightEssentials, contextForGlide)
//        }
//
//        clothesBtn.setOnClickListener {
//            ItemList.getItemAdapter().list = UserDataManager.getInstance(requireContext()).clothes
//            itemAdapter.notifyDataSetChanged()
////            itemListRecyclerView.adapter = ItemListAdapter(UserDataManager.getInstance(requireContext()).clothes, contextForGlide)
//        }
//
//        otherClothesBtn.setOnClickListener {
//            ItemList.getItemAdapter().list = UserDataManager.getInstance(requireContext()).otherClothes
//            itemAdapter.notifyDataSetChanged()
////            itemListRecyclerView.adapter = ItemListAdapter(UserDataManager.getInstance(requireContext()).otherClothes, contextForGlide)
//        }
//
//        careBtn.setOnClickListener {
//            ItemList.getItemAdapter().list = UserDataManager.getInstance(requireContext()).care
//            itemAdapter.notifyDataSetChanged()
////            itemListRecyclerView.adapter = ItemListAdapter(UserDataManager.getInstance(requireContext()).care, contextForGlide)
//        }
//
//        foodBtn.setOnClickListener {
//            ItemList.getItemAdapter().list = UserDataManager.getInstance(requireContext()).food
//            itemAdapter.notifyDataSetChanged()
////            itemListRecyclerView.adapter = ItemListAdapter(UserDataManager.getInstance(requireContext()).food, contextForGlide)
//        }
//    }
}