package com.example.a2021eoskotlinmemo

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat

// 1. 상속
class RecyclerAdapter : RecyclerView.Adapter<RecyclerAdapter.Holder>() {
// 6. command + i를 눌러 함수 세개 모두 구현


    var helper : SqliteHelper? = null




    //command + i를 눌러서 세개를 모두 선택해줘야 apdapter에 해당하는 빨간줄 없어짐
    var listData = mutableListOf<Memo>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        // 특정 xml 파일을 클래스로 변경
        val view:View = LayoutInflater.from(parent.context).inflate(R.layout.item_recycler,parent,false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        // 데이터와 아이템뷰를 연결
        val memo : Memo = listData[position]
        holder.setMemo(memo)
    }

    override fun getItemCount(): Int {
        //아이템 수를 리턴
        return listData.size
    }


//2. holder 정의
    // 레이아웃을 코드로 변환하면 view로 바뀌기 때문에 view를 받는 것
inner class Holder(itemView : View) : RecyclerView.ViewHolder(itemView) {

    var mMemo : Memo? = null



    // 3. cell에 있는 view findviewyid로 연결
    //cell에 있는 요소들을 연결

    var title : TextView = itemView.findViewById(R.id.tv_title)
    var content : TextView = itemView.findViewById(R.id.tv_content)
    var dateTime : TextView = itemView.findViewById(R.id.tv_dateTime)



    //data class로 선언한 타입을 셀에 넣어주는 함수 선언
    fun setMemo(memo: Memo) {
        title.text = memo.title
        content.text = memo.content
        //long형태를 보기 편하게 해준다
        val sdf = SimpleDateFormat("yyyy/MM/dd hh:mm")

        dateTime.text = "${sdf.format(memo.dateTime)}"

        this.mMemo = memo

        //cell을 누르면 넘어가는 함수 선언,update
        itemView.setOnClickListener{
          //  Toast.makeText(itemView.context, "클릭된 item : ${title.text.toString()}", Toast.LENGTH_SHORT).show()

            //현재 어디서 일어나는지 알아내는 개념. 매우 복잡한 개념
            val context = itemView.context as Activity

            val intent = Intent(context,DetailActivity::class.java)

            intent.putExtra("memo",memo)

            context.startActivityForResult(intent,100)


        }

    }

    init{
    val btn_delete : Button = itemView.findViewById(R.id.btn_delete)

        btn_delete.setOnClickListener{
            helper?.deleteMemo(mMemo !!) //db에서 삭제
            listData.remove(mMemo) //listData에서 삭제
            notifyDataSetChanged() //메모리 갱신

        }
    }

}



}

//4.cell에 넣을 데이터들을 담을 클래스 선언
//데이터를 담을 클래스
//data class Memo(var no:Int?,var title: String,var content:String,var dateTime : Long)