package com.thinkingdobby.feeddogtoday

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_list.*
import kotlinx.android.synthetic.main.card_pet_3.view.*
class ListActivity : AppCompatActivity() {

    val petList = mutableListOf<Pet>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        supportActionBar?.title = "애완동물 목록"


        // Floating Action Buttons
        list_fabtn_add.setOnClickListener {
            val intent = Intent(this@ListActivity, AddActivity::class.java)
            startActivity(intent)
        }

        list_fabtn_set.setOnClickListener {
            val intent = Intent(this@ListActivity, SettingActivity::class.java)
            startActivity(intent)
        }
        // Floating Action Buttons

        val layoutManager = LinearLayoutManager(this@ListActivity)

        list_rv_pets.layoutManager = layoutManager
        list_rv_pets.adapter = MyAdapter()

        FirebaseDatabase.getInstance().getReference("Pets")
            .orderByChild("writeTime").addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    snapshot?.let { snapshot ->
                        val pet = snapshot.getValue(Pet::class.java)
                        pet?.let {
                            if (previousChildName == null) {
                                petList.add(it)
                                list_rv_pets.adapter?.notifyItemInserted(petList.size - 1)
                            } else {
                                val prevIndex = petList.map { it.petId }.indexOf(previousChildName)
                                petList.add(prevIndex + 1, pet)
                                list_rv_pets.adapter?.notifyItemInserted(prevIndex + 1)
                            }
                        }
                    }
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    snapshot?.let { snapshot ->
                        val pet = snapshot.getValue(Pet::class.java)
                        pet?.let {
                            val prevIndex = petList.map { it.petId }.indexOf(previousChildName)
                            petList[prevIndex + 1] = pet
                            list_rv_pets.adapter?.notifyItemChanged(prevIndex + 1)
                        }
                    }
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                    snapshot?.let {
                        val pet = snapshot.getValue(Pet::class.java)
                        pet?.let { pet ->
                            val existIndex = petList.map { it.petId }.indexOf(pet.petId)
                            petList.removeAt(existIndex)
                            list_rv_pets.adapter?.notifyItemRemoved(existIndex)
                            if (previousChildName == null) {
                                petList.add(pet)
                                list_rv_pets.adapter?.notifyItemChanged(petList.size - 1)
                            } else {
                                val prevIndex = petList.map { it.petId }.indexOf(previousChildName)
                                petList.add(prevIndex + 1, pet)
                                list_rv_pets.adapter?.notifyItemChanged(prevIndex + 1)
                            }
                        }
                    }
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    snapshot?.let {
                        val pet = snapshot.getValue(Pet::class.java)
                        pet?.let { pet ->
                            val existIndex = petList.map { it.petId }.indexOf(pet.petId)
                            petList.removeAt(existIndex)
                            list_rv_pets.adapter?.notifyItemRemoved(existIndex)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    error?.toException()?.printStackTrace()
                }
            })
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val list_cv_tv_petName: TextView = itemView.list_cv_tv_petName
        val list_cv_tv_petType: TextView = itemView.list_cv_tv_petType
        val list_cv_cb_breakfast: CheckBox = itemView.list_cv_cb_breakfast
        val list_cv_cb_dinner: CheckBox = itemView.list_cv_cb_dinner
        val list_cv_btn_submit: Button = itemView.list_cv_btn_submit
        val list_cv_btn_remove: Button = itemView.list_cv_btn_remove
    }

    inner class MyAdapter : RecyclerView.Adapter<MyViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            return MyViewHolder(
                LayoutInflater.from(this@ListActivity).inflate(
                    R.layout.card_pet_3,
                    parent,
                    false
                )
            )
        }

        override fun getItemCount(): Int {
            return petList.size
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            val pet = petList[position]
            holder.list_cv_tv_petName.text = pet.petName
            holder.list_cv_tv_petType.text = pet.petType
            holder.list_cv_cb_breakfast.isChecked = pet.breakfastChecked
            holder.list_cv_cb_dinner.isChecked = pet.dinnerChecked

            val ref = FirebaseDatabase.getInstance().getReference("Pets").child(pet.petId)
            val checkUpdates = mutableMapOf<String, Any>()

            holder.list_cv_btn_submit.setOnClickListener {
                checkUpdates.put("breakfastChecked", holder.list_cv_cb_breakfast.isChecked)
                checkUpdates.put("dinnerChecked", holder.list_cv_cb_dinner.isChecked)
                ref.updateChildren(checkUpdates)
            }

            holder.list_cv_btn_remove.setOnClickListener {
                ref.removeValue()
            }
        }
    }
}
