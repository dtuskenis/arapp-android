package com.dtuskenis.arapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.ar.sceneform.rendering.Renderable
import com.google.ar.sceneform.rendering.ViewRenderable
import com.dtuskenis.arapp.data.CatalogueItemsProvider
import com.dtuskenis.arapp.data.RenderablesProvider
import com.dtuskenis.arapp.lifecycle.LifecycleControl
import com.dtuskenis.arapp.views.ar.ArView
import com.dtuskenis.arapp.views.catalogue.CatalogueView
import com.dtuskenis.arapp.views.*
import kotlinx.coroutines.Dispatchers

class AppActivity : AppCompatActivity() {

    private val untilDestroyed = LifecycleControl(Dispatchers.Main)

    private val renderablesProvider: RenderablesProvider by lazy { RenderablesProvider(this, assets) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // TODO: implement Bootstrap MVP
        val loadingView = findViewById<View>(R.id.view_loading)

        untilDestroyed.launch {
            loadingView.visibility = View.VISIBLE

            val renderableControls = renderablesProvider.loadRenderableControls()
            val renderableLoadingIndicator = renderablesProvider.loadRenderableLoadingIndicator()

            showAR(renderableControls,
                   renderableLoadingIndicator)

            loadingView.visibility = View.GONE
        }
    }

    private fun showAR(renderableControls: ViewRenderable,
                       renderableLoadingIndicator: Renderable) {
        AppPresenter(untilDestroyed,

                     view = AppView(untilDestroyed,

                                    arView = ArView(findViewById(R.id.view_ar),
                                                    renderableControls,
                                                    renderableLoadingIndicator),

                                    catalogueView = CatalogueView(findViewById(R.id.view_catalogue))),

                     model = AppModel(catalogueItemsProvider = CatalogueItemsProvider(assets),

                                      renderablesProvider = renderablesProvider))
    }

    override fun onDestroy() {
        untilDestroyed.terminate()

        super.onDestroy()
    }
}
