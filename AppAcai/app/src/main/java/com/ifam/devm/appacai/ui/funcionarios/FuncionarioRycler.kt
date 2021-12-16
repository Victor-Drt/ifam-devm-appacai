package com.ifam.devm.appacai.ui.funcionarios

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.ifam.devm.appacai.R

class FuncionarioRycler: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.recycler_funcionario)

        val recyclerViewFuncionario =  findViewById<RecyclerView>(R.id.recyclerFun)
        recyclerViewFuncionario.adapter = ListaFuncionarios()
    }

}