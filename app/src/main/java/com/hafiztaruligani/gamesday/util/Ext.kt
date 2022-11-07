package com.hafiztaruligani.gamesday.util

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners
import com.bumptech.glide.request.RequestOptions
import java.text.SimpleDateFormat
import java.util.*

fun ImageView.glide(
    resource: String,
    topLeft: Int = 0,
    topRight: Int = 0,
    bottomRight: Int = 0,
    bottomLeft: Int = 0,
    circleCrop: Boolean = false,
    centerCrop: Boolean = false,
    override: Boolean = true,
    placeholder: Int? =null
){
    var glide = Glide.with(context)
        .load(resource)
        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)

    if(override) glide = glide.override(width, height)
    if(centerCrop) glide = glide.centerCrop()

    placeholder?.let { glide.placeholder(placeholder) }

    if(
        topLeft > 0 ||
        topRight > 0 ||
        bottomRight > 0 ||
        bottomLeft > 0
    ) {
        val rounded = RequestOptions().transform(
            CenterCrop(), GranularRoundedCorners(
                topLeft.toFloat(),
                topRight.toFloat(),
                bottomRight.toFloat(),
                bottomLeft.toFloat()
            )
        )
        glide.apply(rounded).into(this)
    }else if(circleCrop) glide.circleCrop().into(this)
    else glide.into(this)

}

fun String.convertIntoList(): List<String>{
    return if (this.contains("["))
        this.replace(" ","").replace("[","").replace("]","").split(',')
    else listOf()
}

fun String.toStringDate(): String {
    return try {
        val reader = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val writer = SimpleDateFormat("dd MMMM yyyy", Locale.US)
        val date = reader.parse(this)
        writer.format(date ?: this)
    }catch (e:Exception){
        this
    }
}

fun List<Any>.removeBracket(): String{
    if (isEmpty()) return ""
    return toString().drop(1).dropLast(1)
}