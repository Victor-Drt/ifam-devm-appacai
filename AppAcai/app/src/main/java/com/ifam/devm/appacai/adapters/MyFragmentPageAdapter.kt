package com.ifam.devm.appacai.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.PagerAdapter
import com.ifam.devm.appacai.ui.cadastro_user.CadastrarUsuarioFragment
import com.ifam.devm.appacai.ui.login.LoginFragment

class MyFragmentPageAdapter : FragmentPagerAdapter {

    private val mTableTitles : Array<String>

    constructor (fm: FragmentManager, mTableTitles : Array<String>) : super(fm) {
        this.mTableTitles = mTableTitles
    }

    override fun getCount(): Int {
        return this.mTableTitles.size
    }

    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> return LoginFragment()
            1 -> return CadastrarUsuarioFragment()
            else -> return LoginFragment()
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return this.mTableTitles[position]
    }
}