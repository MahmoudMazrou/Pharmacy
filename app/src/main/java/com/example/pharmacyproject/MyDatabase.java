package com.example.pharmacyproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.pharmacyproject.Models.Spenner;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;

public class MyDatabase extends SQLiteAssetHelper {

    public static final String DB_NAME = "pharma.db";//اسم قاعدة البيانات لازم يكون صح ومع الامتداد
    public static final int DB_VERSION = 1;//رقم الفيرجن عشان اذ بدي احدث
    public static final String SPENNER_TB_NAME = "spenner";//اسم الجدول لازم يكون صح
    //****spenner Table column**
    public static final String SPENNER_CLN_id= "id";//اسماءء الحقول لازم تكون نفس الاسم بضبط زي مهي بقاعدة البيانات
    public static final String SPENNER_CLN_name= "name";
    public static final String SPENNER_CLN_description= "description";
    public static final String SPENNER_CLN_image= "image";




    public MyDatabase(Context context) {//بعدل الكونتكس هيك  لانشاء الداتا بيز
        super(context, DB_NAME, null, DB_VERSION);//بحط المتغيرات الاساسية هين  DB_NAME    DB_VERSION

    }

    public boolean insertSpenner(Spenner spenner) {//بترجع boolean عشان نفحص انو تمت العملية بنجاح ولا لا
        SQLiteDatabase db = getWritableDatabase(); //هاد مؤشر على قاعدة البيانات

        ContentValues cv =new ContentValues();
        cv.put(SPENNER_CLN_name,spenner.getName());
        cv.put(SPENNER_CLN_description,spenner.getDescription());
        cv.put(SPENNER_CLN_image,spenner.getImage());


         long res =  db.insert(SPENNER_TB_NAME,null,cv);
      return res>0; //معناها اذ القيمة اكبر من الصفر نجحت الاضافة يعني برجعtrue العكس false
    }


    public ArrayList<Spenner> getAllSpenner() {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor =  db.query(SPENNER_TB_NAME,null,null,//  Cursorهاد مؤشر في خصائص كثبرة
                null,null,null,null);//خصائص غير مستخدمةnull
        ArrayList<Spenner> spenners = new ArrayList<>();
        if(cursor.moveToFirst()){//اذهب للبداية
            do{
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String description = cursor.getString(2);
                String imge = cursor.getString(3);


                spenners.add(new Spenner(id,name,description,imge));
            }
            while (cursor.moveToNext());//moveToNext كل ما في نيكست بترجع صح اما اذ فش نكست بتطلع
        }

        return spenners;
    }
//    public Model getModel(int id) {
//        SQLiteDatabase db = getWritableDatabase();
//        Cursor cursor =  db.query(MODELS_TB_NAME,null,"id=?",//  Cursorهاد مؤشر في خصائص كثبرة
//                new String[] {String.valueOf(id)},null,null,null);//هين
//        Model c;
//        if(cursor.moveToFirst()){//اذهب للبدايةdo{
//            int i = cursor.getInt(cursor.getColumnIndex(MODELS_CLN_id));
//            String name = cursor.getString(cursor.getColumnIndex(MODELS_CLN_name));
//            String discription = cursor.getString(cursor.getColumnIndex(MODELS_CLN_discription));
//
//            c=  new Model(i,name,discription);
//            return c;
//        }
//        return null;
//
//    }




}

