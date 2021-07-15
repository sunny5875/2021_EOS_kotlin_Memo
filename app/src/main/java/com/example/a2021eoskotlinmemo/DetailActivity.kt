package com.example.a2021eoskotlinmemo

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val ibtn_delete : ImageButton = findViewById(R.id.ibtn_delete)
        val ibtn_save : ImageButton = findViewById(R.id.ibtn_save)
        val edit_title : EditText = findViewById(R.id.editTitle)
        val edit_content: EditText = findViewById(R.id.editContent)

        //넘겨온 애를 받는 코드
        val memo = intent.getSerializableExtra("memo") as Memo

        edit_title.setText(memo.title)
        edit_content.setText(memo.content)

        ibtn_save.setOnClickListener{
        //제목과 내용이 있으면 main으로 돌아가기

            if(edit_title.text.toString().isNotEmpty() && edit_content.text.toString().isNotEmpty()){
                memo.title = edit_title.text.toString()
                memo.content = edit_content.text.toString()


                val returnIntent = Intent()

                returnIntent.putExtra("returnMemo",memo)
                //startActivityforResult라는 함수를 썼으므로 결과 설정
                setResult(Activity.RESULT_OK,returnIntent)

                //main activity로 돌아간다
                finish()

            }
            else if(edit_title.text.toString().isNotEmpty()){
                Toast.makeText(this, "내용이 비어있습니다", Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(this, "제목이 비어있습니다", Toast.LENGTH_SHORT).show()
            }
        }

        ibtn_delete.setOnClickListener{
           setResult(Activity.RESULT_CANCELED,Intent().putExtra("returnMemo",memo))

            //main activity로 돌아간다
            finish()

        }
    }
}