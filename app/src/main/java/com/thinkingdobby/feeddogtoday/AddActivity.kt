package com.thinkingdobby.feeddogtoday

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import androidx.appcompat.app.AlertDialog
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_add.*

class AddActivity : AppCompatActivity() {

    private var defaultValue = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        supportActionBar?.title = "동물 추가"

        add_cv_swh_settingDefaultValue.setOnClickListener {
            defaultValue = true
        }

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
            postPet.petName = add_et_petNameInput.text.toString()
            postPet.petType = add_et_petTypePrint.text.toString()
            postPet.feedingPeriodType = add_cv_et_settingPeriod.text.toString()
            postPet.defaultValue = defaultValue

            ref.setValue(postPet)
            finish()
        }
    }
}
