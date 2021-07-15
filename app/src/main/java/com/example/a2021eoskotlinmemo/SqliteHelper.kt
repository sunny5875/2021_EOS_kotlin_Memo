package com.example.a2021eoskotlinmemo

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.io.Serializable


class SqliteHelper(context : Context, name: String, version : Int) : SQLiteOpenHelper(context,name,null,version){

    override fun onCreate(db: SQLiteDatabase?) {
        // table 생성
        val create = "create table memo(" +
                "no integer primary key," +
                "title text," +
                "content text," +
                "datetime integer" +
                ")"
        db?.execSQL(create)
    }

        // 삽입
        fun insertMemo(memo : Memo){
            //map과 사용법은 비슷하지만 내부구조가 달라 sqlite에서 주로 사용
            val values = ContentValues()

             //memo 클래스의 데이터를 key value 형태로 저장
            values.put("title", memo.title)
            values.put("content", memo.content)
            values.put("datetime", memo.dateTime)


            //데이터베이스에 쓰는 작업을 하기 위해 작성한 값을 db에 넣어준다
            val wd : SQLiteDatabase = writableDatabase

            //memo의 값을 contentvalue형태로 넣어서 데이터베이스에 넣어준다
            wd.insert("memo",null,values)
            wd.close() //메모리 누수를 막기 위해
        }

        //조회
        fun selectMemo()  : MutableList<Memo>{

            val list : MutableList<Memo> = mutableListOf<Memo>()

            val select = "select * from memo"

            //읽어와야 하기 떄문에 readabledatabase
            val rd: SQLiteDatabase = readableDatabase

            //cursor : dataset을 처리할 때 현재 위치를 포함하는 데이터요소, 쿼리로 반환된 DAta set을 반복문으로 하나씩 처리 가능하게 해준다.
            val cursor : Cursor = rd.rawQuery(select, null)

            while(cursor.moveToNext()){
                val no = cursor.getInt(cursor.getColumnIndex("no"))
                val title = cursor.getString(cursor.getColumnIndex("title"))
                val content = cursor.getString(cursor.getColumnIndex("content"))
                val dateTime = cursor.getLong(cursor.getColumnIndex("datetime"))


                list.add(Memo(no,title,content,dateTime))
            }


            cursor.close()
            rd.close()

            return list

        }


        //수정
        fun updateMemo(memo : Memo){
            val values = ContentValues()

            values.put("title", memo.title)
            values.put("content", memo.content)
            values.put("datetime", memo.dateTime)

            val wd : SQLiteDatabase = writableDatabase

            //update(테이블명, 수정할 값, 수정할 조건, 조건이 NUll 일 때 기본값)
            wd.update("memo",values,"no = ${memo.no}",null)
            wd.close()
        }

        //삭제
        fun deleteMemo(memo : Memo){
            val delete = "delete from memo where no = ${memo.no}"
            val db : SQLiteDatabase = writableDatabase

            db.execSQL(delete)
            db.close()
        }


    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        //version을 바꿀 때에만 실행하는 함수로 사용할 일 거의 없음

    }


}

data class Memo(var no:Int?,var title: String,var content:String,var dateTime : Long) : Serializable{
//class를 통째로 넘기기 위해 serialiable을 상속한 것
    //no없이 생성하고 싶기 때문
    constructor(title : String, content : String, dateTime: Long) : this(null,title,content,dateTime)
}