package com.ifam.devm.appacai.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.ifam.devm.appacai.R
import com.ifam.devm.appacai.ui.cadastro_user.EditarDadosUserActivity
import kotlinx.android.synthetic.main.activity_home.*
import android.view.ViewGroup




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
                    title = "Dados Loja"
                    true
                }
                R.id.page_vendedores -> {
                    replaceFragment(vendedoresFragment)
                    title = "Vendedores"
                    true
                }
                R.id.page_home -> {
                    replaceFragment(homeFragment)
                    title = "Home"
                    true
                }
                R.id.page_produtos -> {
                    replaceFragment(produtosFragment)
                    title = "Produtos"
                    true
                }
                R.id.page_vendas -> {
                    replaceFragment(vendasFragment)
                    title = "Vendas"
                    true
                }
                else -> false
            }
        }

    }

//    override fun onStop() {
//        super.onStop()
//        finish()
//    }


    private fun replaceFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.commit()
    }
}


