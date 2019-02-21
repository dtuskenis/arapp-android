package com.dtuskenis.arapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.dtuskenis.arapp.catalogue.CatalogueModel
import com.dtuskenis.arapp.catalogue.CataloguePresenter
import com.dtuskenis.arapp.catalogue.CatalogueView
import com.dtuskenis.arapp.subscriptions.SubscriptionsGroupControl

class MainActivity : AppCompatActivity() {

    private val untilDestroyed = SubscriptionsGroupControl()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        CataloguePresenter(
            untilDestroyed,
            view = CatalogueView(findViewById(R.id.view_catalogue)),
            model = CatalogueModel(
                this,
                assets
            )
        )
    }

    override fun onDestroy() {
        untilDestroyed.unsubscribeAll()

        super.onDestroy()
    }
}
