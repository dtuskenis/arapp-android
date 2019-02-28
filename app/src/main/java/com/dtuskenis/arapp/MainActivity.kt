package com.dtuskenis.arapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.ar.sceneform.rendering.ViewRenderable
import com.dtuskenis.arapp.data.CatalogueItemsProvider
import com.dtuskenis.arapp.data.RenderablesProvider
import com.dtuskenis.arapp.views.ar.ArView
import com.dtuskenis.arapp.views.catalogue.CatalogueView
import com.dtuskenis.arapp.subscriptions.SubscriptionsGroupControl
import com.dtuskenis.arapp.views.*

class MainActivity : AppCompatActivity() {

    private val untilDestroyed = SubscriptionsGroupControl()

    private val renderablesProvider: RenderablesProvider by lazy { RenderablesProvider(this, assets) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // TODO: implement Bootstrap MVP
        val loadingView = findViewById<View>(R.id.view_loading)

        loadingView.visibility = View.VISIBLE

        renderablesProvider.getRenderableControls { renderableControls ->
            loadingView.visibility = View.GONE

            showAR(renderableControls)
        }
    }

    private fun showAR(renderableControls: ViewRenderable) {
        AppPresenter(untilDestroyed,

                     view = AppView(untilDestroyed,

                                    arView = ArView(findViewById(R.id.view_ar),
                                                    renderableControls),

                                    catalogueView = CatalogueView(findViewById(R.id.view_catalogue))),

                     model = AppModel(catalogueItemsProvider = CatalogueItemsProvider(assets),

                                      renderablesProvider = renderablesProvider))
    }

    override fun onDestroy() {
        untilDestroyed.unsubscribeAll()

        super.onDestroy()
    }
}
