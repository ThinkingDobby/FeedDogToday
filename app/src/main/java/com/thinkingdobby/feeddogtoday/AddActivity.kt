package com.thinkingdobby.feeddogtoday

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import androidx.appcompat.app.AlertDialog
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import kotlinx.android.synthetic.main.activity_add.*

class AddActivity : AppCompatActivity() {

    private var defaultValue = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        supportActionBar?.title = "동물 추가"

        // 동물 종 선택
        val animalTypes = arrayOf("강아지", "고양이", "기타")
        var animalType = "고양이"

        add_btn_petTypeSelect.setOnClickListener {
            val builder = AlertDialog.Builder(this@AddActivity)
            builder.setTitle("동물 종류를 고르세요.")
            builder.setSingleChoiceItems(animalTypes, -1) { _, which: Int ->
                animalType = animalTypes[which]
            }

            builder.setPositiveButton("확인") { _, which ->
                add_et_petTypePrint.text = animalType
            }

            builder.setNegativeButton("취소") {_, which -> }

            builder.create().show()
        }
        // 동물 종 선택

        add_btn_submit.setOnClickListener {
            if (TextUtils.isEmpty(add_et_petNameInput.text)) {
                AlertDialog.Builder(this@AddActivity)
                    .setTitle("동물 이름을 입력하세요.")
                    .setPositiveButton("확인") { dialog, which -> }
                    .create()
                    .show()

                return@setOnClickListener
            }

            val ref = FirebaseDatabase.getInstance().getReference("Pets").push()

            val postPet = Pet()
            postPet.petId = ref.key!!
            postPet.writeTime = ServerValue.TIMESTAMP
            postPet.petName = add_et_petNameInput.text.toString()
            postPet.petType = add_et_petTypePrint.text.toString()

            ref.setValue(postPet)
            finish()
        }
    }
}
