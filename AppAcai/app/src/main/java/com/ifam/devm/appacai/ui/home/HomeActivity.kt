package com.ifam.devm.appacai.ui.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.ifam.devm.appacai.R
import com.ifam.devm.appacai.model.Usuario
import com.ifam.devm.appacai.repository.room.AppDatabase
import com.ifam.devm.appacai.repository.sqlite.PREF_DATA_NAME
import com.ifam.devm.appacai.ui.senha.RecuperarSenhaViewModel
import com.ifam.devm.appacai.ui.startup.StartupActivity
import kotlinx.android.synthetic.main.activity_home.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.lang.Exception

class HomeActivity : AppCompatActivity() {

    private val adminFragment = AdminFragment()
    private val vendedoresFragment = VendedoresFragment()
    private val homeFragment = HomeFragment()
    private val produtosFragment = ProdutosFragment()
    private val vendasFragment = VendasFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        replaceFragment(homeFragment)

        bottom_navigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.page_admin -> {
                    replaceFragment(adminFragment)
                    true
                }
                R.id.page_vendedores -> {
                    replaceFragment(vendedoresFragment)
                    true
                }
                R.id.page_home -> {
                    replaceFragment(homeFragment)
                    true
                }
                R.id.page_produtos -> {
                    replaceFragment(produtosFragment)
                    true
                }
                R.id.page_vendas -> {
                    replaceFragment(vendasFragment)
                    true
                }
                else -> false
            }
        }

    }

    override fun onStop() {
        super.onStop()
        finish()
    }

    private fun replaceFragment(fragment: Fragment) {
        if (fragment != null) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, fragment)
            transaction.commit()
        }
    }
}


