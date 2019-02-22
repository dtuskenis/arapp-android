package com.dtuskenis.arapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.dtuskenis.arapp.views.ar.ArModel
import com.dtuskenis.arapp.views.ar.ArPresenter
import com.dtuskenis.arapp.views.ar.ArView
import com.dtuskenis.arapp.views.catalogue.CatalogueModel
import com.dtuskenis.arapp.views.catalogue.CataloguePresenter
import com.dtuskenis.arapp.views.catalogue.CatalogueView
import com.dtuskenis.arapp.views.Coordinator
import com.dtuskenis.arapp.subscriptions.SubscriptionsGroupControl

class MainActivity : AppCompatActivity() {

    private val untilDestroyed = SubscriptionsGroupControl()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val coordinator = Coordinator(this,
                                      assets)

        CataloguePresenter(untilDestroyed,
                           view = CatalogueView(findViewById(R.id.view_catalogue)),
                           model = CatalogueModel(assets,
                                                  coordinator))

        ArPresenter(untilDestroyed,
                    view = ArView(findViewById(R.id.view_ar)),
                    model = ArModel(coordinator))
    }

    override fun onDestroy() {
        untilDestroyed.unsubscribeAll()

        super.onDestroy()
    }
}
