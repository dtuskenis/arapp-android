package com.dtuskenis.arapp.views.ar

import android.view.MotionEvent
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.math.Quaternion
import com.google.ar.sceneform.math.Vector3

internal class NodeRotationController(node: Node) {

    init {
        var lastRotationY = node.worldRotation.y
        var motionStartingX = 0.0f
        var motionDeltaX = 0.0f

        node.setOnTouchListener { hitTestResult, motionEvent ->
            val motionX = motionEvent.rawX

            if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                motionStartingX = motionX
            }

            if (motionEvent.action == MotionEvent.ACTION_MOVE) {
                motionDeltaX = motionX - motionStartingX
                node.worldRotation = Quaternion.axisAngle(Vector3(0f, 1f, 0f), lastRotationY + motionDeltaX)
            }

            if (motionEvent.action == MotionEvent.ACTION_UP) {
                lastRotationY += motionDeltaX
            }

            true
        }
    }
}