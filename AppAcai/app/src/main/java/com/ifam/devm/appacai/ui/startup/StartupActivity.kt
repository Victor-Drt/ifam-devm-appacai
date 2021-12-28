package com.ifam.devm.appacai.ui.startup

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ifam.devm.appacai.R
import com.ifam.devm.appacai.adapters.MyFragmentPageAdapter
import com.ifam.devm.appacai.model.Usuario
import kotlinx.android.synthetic.main.activity_startup.*
import kotlinx.android.synthetic.main.content_startup.*

class StartupActivity : AppCompatActivity() {
    private lateinit var usuario: Usuario

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_startup)

        viewPager_startup.adapter = MyFragmentPageAdapter(supportFragmentManager, resources.getStringArray(R.array.titles_tab) )
        tabLayoutStartup.setupWithViewPager(viewPager_startup)
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}