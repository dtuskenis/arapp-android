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

    private val onSwitchedToTrackingMode = Publisher<Run>()

    private val sceneView = rootView.findViewById<ArSceneView>(R.id.sceneform_ar_scene_view)
    private val commitButton = rootView.findViewById<FloatingActionButton>(R.id.commit_button)
    private val cancelButton = rootView.findViewById<FloatingActionButton>(R.id.cancel_button)

    private val arScene = sceneView.scene

    private val fittingNode = Node().apply { setParent(arScene) }

    private val nodeControls = NodeControls(arScene,
                                            renderableControls)

    init {
        arScene.apply {
            addOnUpdateListener {
                fittingNode.takeIf { it.isEnabled }
                          ?.let { moveNodeToCenterOfCameraView(it) }
            }
            setOnTouchListener { _, _ ->  nodeControls.hide(); true}
        }

        cancelButton.setOnClickListener { switchToTrackingMode() }

        switchToTrackingMode()
    }

    fun switchToFittingModeWithLoadingIndicator() {
        switchToFittingMode()
    }

    fun switchToFittingModeWithRenderable(renderable: Renderable) {
        switchToFittingMode(renderable)
    }

    fun onSwitchedToTrackingMode(): Subscribable<Run> = onSwitchedToTrackingMode

    private fun switchToFittingMode(renderable: Renderable? = null) {
        fittingNode.isEnabled = true
        fittingNode.renderable = renderable // TODO: loading indicator renderable

        commitButton.setOnClickListener { renderable?.let { onCommittedNewNodeWith(it) } }
        forCommitAndCancelButton { it.show() }
    }

    private fun switchToTrackingMode() {
        fittingNode.isEnabled = false
        fittingNode.renderable = null

        commitButton.setOnClickListener(null)
        forCommitAndCancelButton { it.hide() }

        onSwitchedToTrackingMode.publish()
    }

    private fun forCommitAndCancelButton(action: (FloatingActionButton) -> Unit) {
        arrayOf(commitButton, cancelButton).forEach { action(it) }
    }

    private fun onCommittedNewNodeWith(renderable: Renderable) {
        addNewNodeWith(renderable)
        switchToTrackingMode()
    }

    private fun addNewNodeWith(renderable: Renderable) {
        val newNode = Node()

        newNode.setParent(arScene)
        newNode.renderable = renderable
        newNode.worldPosition = fittingNode.worldPosition
        newNode.setOnTapListener { _, _ -> nodeControls.showOn(newNode) }
    }

    private fun moveNodeToCenterOfCameraView(node: Node) {
        sceneView.apply {
            val width = width.toFloat()
            val height = height.toFloat()

            arFrame?.hitTest(width / 2, height / 2)
                   ?.firstOrNull()
                   ?.hitPose
                   ?.let { Vector3(it.tx(), it.ty(), it.tz()) }
                   ?.let { node.worldPosition = it }
        }
    }
}