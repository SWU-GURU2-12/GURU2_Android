package com.example.what_s_in_my_luggage

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast

class AddCarrier : AppCompatActivity() {

    // TODO: 데이터베이스에서 데이터 가져오기 (destination, template)
    val destinationList = arrayOf(
        "일본",
        "중국",
        "미국",
        "영국",
        "프랑스",
        "독일",
        "이탈리아",
        "스페인",
        "러시아",
        "캐나다",
        "브라질",
        "멕시코",
        "인도네시아",
        "인도",
        "필리핀",
        "베트남",
        "터키",
        "호주",
        "이란",
        "이스라엘",
        "사우디아라비아",
        "남아프리카공화국",
        "아르헨티나",
        "폴란드",
        "스웨덴",
        "우크라이나",
        "스위스",
        "노르웨이",
        "핀란드",
        "오스트리아",
        "벨기에",
        "그리스",
        "덴마크",
        "네덜란드",
        "쿠웨이트",
        "아랍에미리트",
        "카타르",
        "헝가리",
        "아일랜드",
        "말레이시아",
        "싱가포르",
        "칠레",
        "캄보디아",
        "크로아티아",
        "콜롬비아",
        "코스타리카",
        "쿠바",
        "체코",
        "에콰도르",
        "에스토니아",
        "그루지야",
        "아이슬란드",
        "라트비아",
        "리투아니아",
        "룩셈부르크",
        "몰타",
        "모로코",
        "뉴질랜드",
        "파나마",
        "페루",
        "포르투갈",
        "루마니아",
        "세르비아",
        "슬로바키아",
        "슬로베니아",
        "탄자니아",
        "타이",
        "우루과이",
        "바티칸"
    )
    val templateList = arrayOf("기본", "기분 좋은 바다 여행", "혼자 떠나는 유럽 한 달")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_carrier)

    // toolbar
        var toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()?.setTitle("캐리어 추가")

        fun onOptionsItemSelected(item: MenuItem): Boolean {
            when (item.itemId) {
                android.R.id.home -> { // 뒤로가기 버튼 클릭
                    finish()
                    return true
                }
            }
            return super.onOptionsItemSelected(item)
        }


    // title
        var title = findViewById<TextView>(R.id.carrierTitle)

    // destination spinner
        var destination: String = ""
        val spinner = findViewById<Spinner>(R.id.destinationSpin)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, destinationList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner.adapter = adapter
        spinner.prompt = "여행지"

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: android.view.View, position: Int, id: Long) {
                destination = destinationList[position]
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

    // TODO: date picker
    // 년월일~ 년월일 선택하는 date picker 구현


    // template spinner
        var template: String = ""
        val spinner2 = findViewById<Spinner>(R.id.templateSpin)
        val adapter2 = ArrayAdapter(this, android.R.layout.simple_spinner_item, templateList)
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner2.adapter = adapter2
        spinner2.prompt = "템플릿"

        spinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: android.view.View, position: Int, id: Long) {
                template = templateList[position]
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

    // create Carrier
        var create = findViewById<Button>(R.id.createCarrier)

        create.setOnClickListener {
            // TODO: title 데이터가 없다면 경고메시지 발생 -> 일정, 템플릿
            if (title.text.toString() == "") {
                val builder = android.app.AlertDialog.Builder(this)
                builder.setMessage("제목을 입력해주세요.")
                    .setPositiveButton("확인",
                        DialogInterface.OnClickListener { dialog, which ->
                            Toast.makeText(applicationContext, "확인", Toast.LENGTH_SHORT).show()
                        })
                val alertDialog = builder.create()
                alertDialog.show()
                return@setOnClickListener
            }
            
            // intent
            var intent = Intent(this, PackLuggage::class.java)
            intent.putExtra("title", title.text.toString())
            intent.putExtra("destination", destination)
            // intent.putExtra("date", date)
            intent.putExtra("template", template)
            startActivity(intent)
        }
    }
}