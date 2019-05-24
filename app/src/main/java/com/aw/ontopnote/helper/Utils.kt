package com.aw.ontopnote.helper

import android.content.Context
import androidx.core.content.ContextCompat
import com.aw.ontopnote.R

object Utils {

    fun rgbToColorRes(context: Context, rgbColor: Int) : Int {
        return when (rgbColor) {
            ContextCompat.getColor(context, R.color.redMaterial) -> ContextCompat.getColor(context, R.color.redMaterial)
            ContextCompat.getColor(context, R.color.pinkMaterial) -> ContextCompat.getColor(context, R.color.pinkMaterial)
            ContextCompat.getColor(context, R.color.purpleMaterial) -> ContextCompat.getColor(context, R.color.purpleMaterial)
            ContextCompat.getColor(context, R.color.deepPurpleMaterial) -> ContextCompat.getColor(context, R.color.deepPurpleMaterial)
            ContextCompat.getColor(context, R.color.indigoMaterial) -> ContextCompat.getColor(context, R.color.indigoMaterial)
            ContextCompat.getColor(context, R.color.lightBluetMaterial) -> ContextCompat.getColor(context, R.color.lightBluetMaterial)
            ContextCompat.getColor(context, R.color.cyanMaterial) -> ContextCompat.getColor(context, R.color.cyanMaterial)
            ContextCompat.getColor(context, R.color.tealMaterial) -> ContextCompat.getColor(context, R.color.tealMaterial)
            ContextCompat.getColor(context, R.color.greenMaterial) -> ContextCompat.getColor(context, R.color.greenMaterial)
            ContextCompat.getColor(context, R.color.lightGreenMaterial) -> ContextCompat.getColor(context, R.color.lightGreenMaterial)
            ContextCompat.getColor(context, R.color.limeMaterial) -> ContextCompat.getColor(context, R.color.limeMaterial)
            ContextCompat.getColor(context, R.color.yellowMaterial) -> ContextCompat.getColor(context, R.color.yellowMaterial)
            ContextCompat.getColor(context, R.color.amberMaterial) -> ContextCompat.getColor(context, R.color.amberMaterial)
            ContextCompat.getColor(context, R.color.orangeMaterial) -> ContextCompat.getColor(context, R.color.orangeMaterial)
            ContextCompat.getColor(context, R.color.deepOrangeMaterial) -> ContextCompat.getColor(context, R.color.deepOrangeMaterial)
            ContextCompat.getColor(context, R.color.brownMaterial) -> ContextCompat.getColor(context, R.color.brownMaterial)
            ContextCompat.getColor(context, R.color.greyMaterial) -> ContextCompat.getColor(context, R.color.greyMaterial)
            ContextCompat.getColor(context, R.color.blueGreyMaterial) -> ContextCompat.getColor(context, R.color.blueGreyMaterial)
            ContextCompat.getColor(context, R.color.black) -> ContextCompat.getColor(context, R.color.black)
            else -> {
                ContextCompat.getColor(context, R.color.blueMaterial)
            }
        }
    }
}