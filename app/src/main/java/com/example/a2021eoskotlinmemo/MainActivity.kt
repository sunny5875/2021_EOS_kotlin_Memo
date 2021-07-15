package com.example.a2021eoskotlinmemo

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    // 만든 sqliteHelper 클래스 변수 선언
   val helper = SqliteHelper(this,"memo",1)

    //8. 데이터를 만든 리사이클러뷰 이름으로 연결
    val adapter = RecyclerAdapter()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



//7. 더미데이터 만든 함수 하나 선언
        fun makeDummyData() : MutableList<Memo> {
            val data : MutableList<Memo> = mutableListOf()

            for(no in 1..10) {
                val title = "이것은 ${no}번쨰 메모"
                val content = "이것은 ${no}번쨰 내용"
                val date = System.currentTimeMillis()
                val memo = Memo(no, title, content, date)

                data.add(memo)
            }

            return data

        }

        adapter.helper = helper


        //mainactivity view 연결
        val recyclerMemo  =  findViewById<RecyclerView>(R.id.recylerMemo)
        val btn_add : Button = findViewById(R.id.btn_add)

        //adapter와 layoutmanager 연결
        //adapter.listData = makeDummyData()
        adapter.listData.addAll(helper.selectMemo())

        recyclerMemo.adapter = adapter
        recyclerMemo.layoutManager = LinearLayoutManager(this)


        btn_add.setOnClickListener{
           // Toast.makeText(this, "add button", Toast.LENGTH_SHORT).show()

            val intent = Intent(this,DetailActivity::class.java)

            val memo = Memo("","", System.currentTimeMillis())

            intent.putExtra("memo",memo)

            //새 액티비티를 열어주면서 새 액티비티가 주는 결과값을 받음
            startActivityForResult(intent,99)


        }


    }
//startActivityforresult에서 돌아온 값에 따라 다르게 실행할 수 있는 함수
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val memo = data?.getSerializableExtra("returnMemo") as Memo

        print(memo.title)

        if(resultCode == Activity.RESULT_OK){
            when(requestCode){
                //새로운 메모를 추가하는 코드
                99 -> {
                    memo.dateTime = System.currentTimeMillis()

                    helper.insertMemo(memo)

                    //listdata를 지우고 모두 다시 받아오기
                    adapter.listData.clear()

                    adapter.listData.addAll(helper.selectMemo())
                    adapter.notifyDataSetChanged()
                }
                100->{
                    memo.dateTime = System.currentTimeMillis()

                    helper.updateMemo(memo)

                    //listdata를 지우고 모두 다시 받아오기
                    adapter.listData.clear()

                    adapter.listData.addAll(helper.selectMemo())
                    adapter.notifyDataSetChanged()
                }
            }
        }
        else if(resultCode == Activity.RESULT_CANCELED){

            helper.deleteMemo(memo)

            adapter.listData.clear()
            adapter.listData.addAll(helper.selectMemo())
            adapter.notifyDataSetChanged()
        }
    }
}

