package com.example.deaft

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DictAdapter(private val itemList: List<String>, private val context: Context) : RecyclerView.Adapter<DictAdapter.ItemViewHolder>() {

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textItem: TextView = itemView.findViewById(R.id.textItem)
        val btnIcon: ImageButton = itemView.findViewById(R.id.btnIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.dict_words, parent, false)
        return ItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val currentItem = itemList[position]
        holder.textItem.text = currentItem

        holder.btnIcon.setOnClickListener {
            VibrationUtil.showEnableHapticFeedbackMessage(context)
            CoroutineScope(Dispatchers.Main).launch {
                processAndVibrate(context, currentItem)
            }
        }
    }

    override fun getItemCount() = itemList.size


    private suspend fun processAndVibrate(context: Context, text: String) {
            if (!Python.isStarted()) {
                Python.start(AndroidPlatform(context))
            }

            val databaseReference =
                FirebaseDatabase.getInstance().getReference("vibracoes-comunidade")

            databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val genericTypeIndicator = object : GenericTypeIndicator<Map<String, Any>>() {}
                    val value = snapshot.getValue(genericTypeIndicator)

                    val stringValue = value?.get(text) as? String

                    if (stringValue != null) {
                        GlobalScope.launch {
                            val processedText = VibrationUtil.translateToVibDict(stringValue)
                            Log.d("DictAdapter", processedText.toString())
                            processedText.forEach { element ->
                                VibrationUtil.vibrate(context, element)
                                delay(500)
                            }
                        }

                    } else {
                        Log.e("DictAdapter", "Valor não encontrado para a chave: $text")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("DictAdapter", "Erro ao obter dados do Firebase: ${error.message}")
            }
            })
        }
    }
