package com.aravind.todolist

import android.content.Context
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.runtime.snapshots.SnapshotStateList
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

const val FILE_NAME = "todo_list.txt"


fun writeDate(items: SnapshotStateList<String>, context: Context) {
    val fos = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE)
    val oas = ObjectOutputStream(fos)
    val itemsArray = ArrayList<String>()
    itemsArray.addAll(items)
    oas.writeObject(itemsArray)
    oas.close()
}

fun readData(context: Context): SnapshotStateList<String> {
    var itemList : ArrayList<String>
    val fis = context.openFileInput(FILE_NAME)
    val ois = ObjectInputStream(fis)
    val items = SnapshotStateList<String>()
    try {
        itemList = ois.readObject() as ArrayList<String>
        items.addAll(itemList)
    } catch (e: Exception) {
        itemList = ArrayList()
    }
    return items;
}