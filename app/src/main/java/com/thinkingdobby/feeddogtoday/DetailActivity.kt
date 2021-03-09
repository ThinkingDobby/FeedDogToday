package com.thinkingdobby.feeddogtoday

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import androidx.appcompat.app.AlertDialog
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import kotlinx.android.synthetic.main.activity_add.*

class DetailActivity : AppCompatActivity() {

    private var defaultValue = false
    private var petId = "default"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        petId = intent.getStringExtra("petId")!!       // intent 에서 petId 받아옴

        supportActionBar?.title = "동물정보 변경"
        add_btn_submit.text = "변경하기"
        add_btn_cancel.text = "삭제하기"

        add_cv_swh_settingDefaultValue.setOnClickListener {
            defaultValue = true
        }

        val ref = FirebaseDatabase.getInstance().getReference("Pets").child(petId)

        add_btn_submit.setOnClickListener {
            if (TextUtils.isEmpty(add_et_petNameInput.text)) {
                AlertDialog.Builder(this@DetailActivity)
                    .setTitle("동물 이름을 입력하세요.")
                    .setPositiveButton("확인") { dialog, which -> }
                    .create()
                    .show()

                return@setOnClickListener
            }

            val detailUpdates = mutableMapOf<String, Any>()     // 데이터 업데이트

            detailUpdates.put("writeTime", ServerValue.TIMESTAMP)
            detailUpdates.put("petName", add_et_petNameInput.text.toString())
            detailUpdates.put("petType", add_et_petTypePrint.text.toString())
            detailUpdates.put("feedingPeriodType", add_cv_et_settingPeriod.text.toString())
            detailUpdates.put("defaultValue", defaultValue)
            ref.updateChildren(detailUpdates)

            finish()
        }

        add_btn_cancel.setOnClickListener {     // 데이터 삭제
            ref.removeValue()
            finish()
        }
    }
}