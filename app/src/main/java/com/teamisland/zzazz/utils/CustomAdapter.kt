package com.teamisland.zzazz.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.teamisland.zzazz.R
import com.teamisland.zzazz.ui.ProjectActivity

/**
 * Override [RecyclerView.Adapter] for effect tab
 */
class CustomAdapter(
    private val list: ArrayList<Int>,
    private val context: Context,
    private val frame: Int
) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    /**
     * [RecyclerView.Adapter.onCreateViewHolder]
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_view, parent, false)

        return ViewHolder(view)
    }

    /**
     * [RecyclerView.Adapter.getItemCount]
     */
    override fun getItemCount(): Int = list.size

    /**
     * [RecyclerView.Adapter.onBindViewHolder]
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.imageView.setImageResource(list[position])
        holder.imageView.setOnClickListener {
            val bitmap = (context.resources.getDrawable(R.drawable.load) as BitmapDrawable).bitmap
            val bitmapArrayList: MutableList<Bitmap> = mutableListOf()
            val point = Effect.Point(30, 30)
            val pointArrayList: MutableList<Effect.Point> = mutableListOf()
            val widthArrayList: MutableList<Int> = mutableListOf()
            val heightArrayList: MutableList<Int> = mutableListOf()

            // for test
            for (i in 0 until 30) {
                bitmapArrayList.add(bitmap)
                pointArrayList.add(point)
                widthArrayList.add(30)
                heightArrayList.add(30)
            }
            ProjectActivity.effectList.add(
                Effect(
                    frame,
                    frame + 29,
                    0,
                    0xFFFFFF,
                    bitmapArrayList,
                    pointArrayList,
                    widthArrayList,
                    heightArrayList
                )
            )
            Log.d("add", "${ProjectActivity.effectList.size}")
        }
    }

    /**
     * Class for each effects
     */
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        /**
         * Effect Image View
         */
        val imageView = itemView.findViewById<ImageView>(R.id.item)!!
    }
}
