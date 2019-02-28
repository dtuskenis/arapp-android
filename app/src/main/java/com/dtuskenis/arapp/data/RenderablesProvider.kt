package com.dtuskenis.arapp.data

import android.content.Context
import android.content.res.AssetManager
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.rendering.Renderable
import com.google.ar.sceneform.rendering.ViewRenderable
import com.dtuskenis.arapp.R
import com.dtuskenis.arapp.functional.Accept
import com.dtuskenis.arapp.functional.Cancel
import java.util.concurrent.CompletableFuture

class RenderablesProvider(private val context: Context,
                          private val assetsManager: AssetManager) {

    fun loadRenderableNamed(name: String,
                            onCompleted: (Renderable) -> Unit): Cancel =
            get(ModelRenderable.builder()
                        .setSource(context) { assetsManager.open("models/${name}/renderable.sfb") }
                        .build(),
                onCompleted)

    fun getRenderableControls(onCompleted: Accept<ViewRenderable>): Cancel =
            get(ViewRenderable.builder()
                        .setView(context, R.layout.view_renderable_controls)
                        .build(),
                onCompleted)

    private fun <T> get(future: CompletableFuture<T>, onCompleted: (T) -> Unit): Cancel {
        val loading = future.thenAccept { onCompleted(it) }

        return { loading.cancel(true) }
    }
}