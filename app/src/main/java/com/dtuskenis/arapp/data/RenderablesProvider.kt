package com.dtuskenis.arapp.data

import android.content.Context
import android.content.res.AssetManager
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.rendering.Renderable
import com.google.ar.sceneform.rendering.ViewRenderable
import com.dtuskenis.arapp.R
import kotlinx.coroutines.future.await

class RenderablesProvider(private val context: Context,
                          private val assetsManager: AssetManager) {

    suspend fun loadRenderableNamed(name: String): Renderable =
        ModelRenderable.builder()
            .setSource(context) { assetsManager.open("models/$name/renderable.sfb") }
            .build()
            .await()

    suspend fun loadRenderableControls(): ViewRenderable =
        ViewRenderable.builder()
            .setView(context, R.layout.view_renderable_controls)
            .build()
            .await()
}