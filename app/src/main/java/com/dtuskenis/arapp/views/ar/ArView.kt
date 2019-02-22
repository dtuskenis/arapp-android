package com.dtuskenis.arapp.views.ar

import android.support.design.widget.FloatingActionButton
import android.view.View
import com.google.ar.sceneform.ArSceneView
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.math.Vector3
import com.dtuskenis.arapp.R
import com.dtuskenis.arapp.extensions.onLaidOut
import com.dtuskenis.arapp.functional.Run
import com.dtuskenis.arapp.views.Renderable
import com.dtuskenis.arapp.subscriptions.Publisher
import com.dtuskenis.arapp.subscriptions.Subscribable
import com.dtuskenis.arapp.subscriptions.publish

class ArView(rootView: View) {

    private val onCommittedOrCancelled = Publisher<Run>()

    private val node = Node()

    private val sceneView = rootView.findViewById<ArSceneView>(R.id.sceneform_ar_scene_view)
    private val commitButton = rootView.findViewById<FloatingActionButton>(R.id.commit_button)
    private val cancelButton = rootView.findViewById<FloatingActionButton>(R.id.cancel_button)

    init {
        setControlsVisible(false)

        sceneView.run {
            onLaidOut {

                val width = width.toFloat()
                val height = height.toFloat()

                scene.addOnUpdateListener {
                    arFrame?.hitTest(width / 2, height / 2)
                           ?.firstOrNull()
                           ?.hitPose
                           ?.let { Vector3(it.tx(), it.ty(), it.tz()) }
                           ?.let { node.worldPosition = it }
                }
            }
        }

        node.setParent(sceneView.scene)

        commitButton.setOnClickListener {
            node.renderable?.let {

                val newNode = Node()

                newNode.setParent(sceneView.scene)
                newNode.renderable = it
                newNode.worldPosition = node.worldPosition

                backToTracking()
            }
        }

        cancelButton.setOnClickListener { backToTracking() }
    }

    fun showLoading() {
        toFitting()
    }

    fun showNewRenderable(renderable: Renderable) {
        toFitting(renderable)
    }

    fun onCommittedOrCancelled(): Subscribable<Run> = onCommittedOrCancelled

    private fun toFitting(model: Renderable? = null) {
        @Suppress("USELESS_ELVIS_RIGHT_IS_NULL")
        node.renderable = model?.arCoreRenderable ?: /* TODO: loading indicator renderable */null

        setControlsVisible(true)
    }

    private fun backToTracking() {
        node.renderable = null

        setControlsVisible(false)

        onCommittedOrCancelled.publish()
    }

    private fun setControlsVisible(visible: Boolean) {
        arrayOf(commitButton, cancelButton).forEach { if (visible) it.show() else it.hide() }
    }
}