package com.dtuskenis.arapp.extensions

import android.widget.SeekBar

val SEEK_BAR_PROGRESS_VALUES: ClosedRange<Int> by lazy { 0..100 }

fun SeekBar.setOnProgressChangedByUserListener(listener: (progress: Int) -> Unit) =
        setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    listener(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {

            }
        })