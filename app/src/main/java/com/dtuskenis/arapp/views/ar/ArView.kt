package com.dtuskenis.arapp.views.ar

import android.support.design.widget.FloatingActionButton
import android.view.View
import android.widget.SeekBar
import com.google.ar.sceneform.ArSceneView
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.Color
import com.google.ar.sceneform.rendering.ViewRenderable
import com.dtuskenis.arapp.R
import com.dtuskenis.arapp.extensions.*
import com.dtuskenis.arapp.functional.Run
import com.dtuskenis.arapp.views.Renderable
import com.dtuskenis.arapp.subscriptions.Publisher
import com.dtuskenis.arapp.subscriptions.Subscribable
import com.dtuskenis.arapp.subscriptions.publish
import com.rtugeek.android.colorseekbar.ColorSeekBar
import kotlin.math.roundToInt

class ArView(rootView: View,
             renderableControls: ViewRenderable) {

    private val onCommittedOrCancelled = Publisher<Run>()

    private val fittingNode = Node()
    private val controlsNode = Node()

    private val sceneView = rootView.findViewById<ArSceneView>(R.id.sceneform_ar_scene_view)
    private val commitButton = rootView.findViewById<FloatingActionButton>(R.id.commit_button)
    private val cancelButton = rootView.findViewById<FloatingActionButton>(R.id.cancel_button)

    private val scaleSeekBar = renderableControls.view.findViewById<SeekBar>(R.id.scale_seek_bar)
    private val colorSeekBar = renderableControls.view.findViewById<ColorSeekBar>(R.id.color_seek_bar)

    // TODO: need to compose that better
    private val renderablesScales = mutableMapOf<Node, Renderable.ScaleMultiplier>()
    private val renderablesColors = mutableMapOf<Node, Int>()

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
                           ?.let { fittingNode.worldPosition = it }
                }
            }
        }

        fittingNode.isEnabled = false
        fittingNode.setParent(sceneView.scene)

        controlsNode.setParent(sceneView.scene)
        controlsNode.renderable = renderableControls
        controlsNode.isEnabled = false

        commitButton.setOnClickListener {
            // TODO: fix this condition
            fittingNode.renderable?.let {
                addNewNodeWith(it)
                backToTracking()
            }
        }

        sceneView.scene.setOnTouchListener { _, _ ->
            controlsNode.isEnabled = false

            true
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
        fittingNode.isEnabled = true
        @Suppress("USELESS_ELVIS_RIGHT_IS_NULL")
        fittingNode.renderable = model?.arCoreRenderable ?: /* TODO: loading indicator renderable */null

        setControlsVisible(true)
    }

    private fun backToTracking() {
        fittingNode.isEnabled = false
        fittingNode.renderable = null

        setControlsVisible(false)

        onCommittedOrCancelled.publish()
    }

    private fun setControlsVisible(visible: Boolean) {
        arrayOf(commitButton, cancelButton).forEach { if (visible) it.show() else it.hide() }
    }

    private fun addNewNodeWith(renderable: com.google.ar.sceneform.rendering.Renderable) {
        val newNode = Node()

        newNode.setParent(sceneView.scene)
        newNode.renderable = renderable
        newNode.worldPosition = fittingNode.worldPosition
        newNode.setOnTapListener { _, _ -> showControlsOn(newNode) }
    }

    private fun showControlsOn(node: Node) {
        configureScaleSeekBarFor(node)

        controlsNode.worldPosition = node.worldPosition.also {
            it.y += 0.25f
            it.z += 0.25f
        }
        controlsNode.isEnabled = true
    }

    private fun configureScaleSeekBarFor(node: Node) {
        val scale = renderablesScales.getOrPut(node) { Renderable.ScaleMultiplier(1.0f) }

        scaleSeekBar.progress = renderableScaleToSeekBarProgress(scale)
        scaleSeekBar.setOnProgressChangedByUserListener {
            val newScale = seekBarProgressToRenderableScale(it)

            renderablesScales[node] = newScale

            node.worldScale = newScale.floatValue.let { Vector3(it, it, it) }
        }

        val color = renderablesColors.getOrPut(node) { android.graphics.Color.WHITE }

        colorSeekBar.setOnColorChangeListener { _, _, newColor ->
            renderablesColors[node] = newColor

            node.renderable?.material?.setFloat3(MATERIAL_TINT, Color(newColor))
        }
        colorSeekBar.color = color
    }

    private fun renderableScaleToSeekBarProgress(scale: Renderable.ScaleMultiplier): Int {
        return scaleFloat(scale.floatValue,
                          srcRange = Renderable.ScaleMultiplier.ALLOWED_FLOAT_VALUES,
                          dstRange = SEEK_BAR_PROGRESS_VALUES.map { it.toFloat() })
                .roundToInt()
    }

    private fun seekBarProgressToRenderableScale(progress: Int): Renderable.ScaleMultiplier {
        return scaleFloat(progress.toFloat(),
                          srcRange = SEEK_BAR_PROGRESS_VALUES.map { it.toFloat() },
                          dstRange = Renderable.ScaleMultiplier.ALLOWED_FLOAT_VALUES)
                .let { Renderable.ScaleMultiplier(it) }
    }

     companion object {
         private const val MATERIAL_TINT = "baseColorTint"
     }
}