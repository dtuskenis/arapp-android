package com.dtuskenis.arapp.views.ar

import android.widget.SeekBar
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.NodeParent
import com.google.ar.sceneform.collision.Box
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.Color
import com.google.ar.sceneform.rendering.ViewRenderable
import com.dtuskenis.arapp.R
import com.dtuskenis.arapp.data.RenderableScale
import com.dtuskenis.arapp.extensions.SEEK_BAR_PROGRESS_VALUES
import com.dtuskenis.arapp.extensions.map
import com.dtuskenis.arapp.extensions.scaleFloat
import com.dtuskenis.arapp.extensions.setOnProgressChangedByUserListener
import com.rtugeek.android.colorseekbar.ColorSeekBar
import kotlin.math.roundToInt

internal class NodeControls(parent: NodeParent,
                            renderableControls: ViewRenderable) {

    private val scaleSeekBar = renderableControls.view.findViewById<SeekBar>(R.id.scale_seek_bar)
    private val colorSeekBar = renderableControls.view.findViewById<ColorSeekBar>(R.id.color_seek_bar)

    private val nodeParams = mutableMapOf<Node, NodeParams>()

    private val controlsNode = Node().apply {
        setParent(parent)
        isEnabled = false
        renderable = renderableControls
    }

    fun showOn(node: Node) {
        configureFor(node)
        moveTo(node)
        controlsNode.isEnabled = true
    }

    fun hide() {
        controlsNode.isEnabled = false
    }

    private fun configureFor(node: Node) {
        val currentParams = currentParamsOf(node)

        scaleSeekBar.progress = renderableScaleToSeekBarProgress(currentParams.scale)
        scaleSeekBar.setOnProgressChangedByUserListener {
            val newScale = seekBarProgressToRenderableScale(it)

            nodeParams[node] = currentParamsOf(node).copy(scale = newScale)

            node.worldScale = newScale.floatValue.let { Vector3(it, it, it) }

            moveTo(node)
        }

        colorSeekBar.color = currentParams.tintColor
        colorSeekBar.setOnColorChangeListener { _, _, newColor ->
            nodeParams[node] = currentParamsOf(node).copy(tintColor = newColor)

            node.renderable?.material?.setFloat3(MATERIAL_TINT, Color(newColor))
        }
    }

    private fun moveTo(node: Node) {
        val collisionBoxSize = (node.renderable?.collisionShape?.let { it as? Box })?.size ?: Vector3.zero()
        controlsNode.worldPosition =
            node.worldPosition.apply { y += (collisionBoxSize.y * node.worldScale.y) }
    }

    private fun currentParamsOf(node: Node): NodeParams {
        return nodeParams.getOrPut(node) {
            NodeParams(
                scale = RenderableScale(1.0f),
                tintColor = android.graphics.Color.TRANSPARENT
            )
        }
    }

    private fun renderableScaleToSeekBarProgress(scale: RenderableScale): Int {
        return scaleFloat(scale.floatValue,
                          srcRange = RenderableScale.ALLOWED_FLOAT_VALUES,
                          dstRange = SEEK_BAR_PROGRESS_VALUES.map { it.toFloat() })
                .roundToInt()
    }

    private fun seekBarProgressToRenderableScale(progress: Int): RenderableScale {
        return scaleFloat(progress.toFloat(),
                          srcRange = SEEK_BAR_PROGRESS_VALUES.map { it.toFloat() },
                          dstRange = RenderableScale.ALLOWED_FLOAT_VALUES)
                .let { RenderableScale(it) }
    }

    companion object {
        private const val MATERIAL_TINT = "baseColorTint"
    }
}