package com.dtuskenis.arapp.views.ar

import android.support.design.widget.FloatingActionButton
import android.view.View
import com.google.ar.sceneform.ArSceneView
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.Renderable
import com.google.ar.sceneform.rendering.ViewRenderable
import com.dtuskenis.arapp.R
import com.dtuskenis.arapp.functional.Run
import com.dtuskenis.arapp.subscriptions.Publisher
import com.dtuskenis.arapp.subscriptions.Subscribable
import com.dtuskenis.arapp.subscriptions.publish

class ArView(rootView: View,
             renderableControls: ViewRenderable) {

    private val onFittingModeCancelled = Publisher<Run>()
    private val onAddButtonClickedAndHidden = Publisher<Run>()

    private val fittingNode = Node()

    private val sceneView = rootView.findViewById<ArSceneView>(R.id.sceneform_ar_scene_view)
    private val addButton = rootView.findViewById<FloatingActionButton>(R.id.add_button)
    private val commitButton = rootView.findViewById<FloatingActionButton>(R.id.commit_button)
    private val cancelButton = rootView.findViewById<FloatingActionButton>(R.id.cancel_button)

    private val nodeControls = NodeControls(sceneView.scene,
                                            renderableControls)

    init {
        sceneView.run {
            scene.addOnUpdateListener {
                val width = width.toFloat()
                val height = height.toFloat()

                arFrame?.hitTest(width / 2, height / 2)
                       ?.firstOrNull()
                       ?.hitPose
                       ?.let { Vector3(it.tx(), it.ty(), it.tz()) }
                       ?.let { fittingNode.worldPosition = it }
            }
        }

        fittingNode.isEnabled = false
        fittingNode.setParent(sceneView.scene)

        addButton.setOnClickListener {
            addButton.hide()

            onAddButtonClickedAndHidden.publish()
        }

        commitButton.setOnClickListener {
            // TODO: fix this condition
            fittingNode.renderable?.let {
                addNewNodeWith(it)
                switchToTrackingMode()
            }
        }

        sceneView.scene.setOnTouchListener { _, _ ->
            nodeControls.hide()

            true
        }

        cancelButton.setOnClickListener { switchToTrackingMode() }

        setControlsVisible(false)
    }

    fun switchToFittingModeWithLoadingIndicator() {
        switchToFittingMode()
    }

    fun switchToFittingModeWithRenderable(renderable: Renderable) {
        switchToFittingMode(renderable)
    }

    fun onFittingModeCancelled(): Subscribable<Run> = onFittingModeCancelled

    fun onAddButtonClickedAndHidden(): Subscribable<Run> = onAddButtonClickedAndHidden

    fun makeAddButtonVisible() {
        addButton.show()
    }

    private fun switchToFittingMode(renderable: Renderable? = null) {
        fittingNode.isEnabled = true
        @Suppress("USELESS_ELVIS_RIGHT_IS_NULL")
        fittingNode.renderable = renderable ?: /* TODO: loading indicator renderable */null

        setControlsVisible(true)
    }

    private fun switchToTrackingMode() {
        fittingNode.isEnabled = false
        fittingNode.renderable = null

        setControlsVisible(false)

        makeAddButtonVisible()
    }

    private fun setControlsVisible(visible: Boolean) {
        arrayOf(commitButton, cancelButton).forEach { if (visible) it.show() else it.hide() }
    }

    private fun addNewNodeWith(renderable: Renderable) {
        val newNode = Node()

        newNode.setParent(sceneView.scene)
        newNode.renderable = renderable
        newNode.worldPosition = fittingNode.worldPosition
        newNode.setOnTapListener { _, _ -> nodeControls.showOn(newNode) }
    }
}