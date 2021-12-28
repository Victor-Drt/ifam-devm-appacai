package com.ifam.devm.appacai.ui.venda

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ifam.devm.appacai.R
import com.ifam.devm.appacai.model.Pagamento
import com.ifam.devm.appacai.repository.room.AppDatabase
import com.ifam.devm.appacai.ui.home.VendedorMainActivity
import kotlinx.android.synthetic.main.activity_venda_realizada.*
import org.jetbrains.anko.doAsync
import java.text.SimpleDateFormat
import java.util.*

class VendaRealizadaActivity : AppCompatActivity() {
    private lateinit var nomeFuncionario: String
    private var valorHj: Float = 0.00f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_venda_realizada)

        val formataData = SimpleDateFormat("dd-MM-yyyy")
        val data = Date()
        val dataFormatadaHj = formataData.format(data)

        btnVendaRealizada.setOnClickListener {
            val db = AppDatabase.getDatabase(this)
            doAsync {
                val pagamento = Pagamento(gerarIdCadastro(), valorHj, dataFormatadaHj)
                db.pagamentoDao().insert(pagamento)
            }

            val intent = Intent(this, VendedorMainActivity::class.java)
            intent.putExtra("funcionario_nome", nomeFuncionario)
            startActivity(intent)
            finishAffinity()
        }
    }

    override fun onResume() {
        super.onResume()
        nomeFuncionario = intent.getStringExtra("funcionario_nome").toString()
        valorHj = intent.getFloatExtra("valorCompraHJ", 0.00f)
    }

    private fun gerarIdCadastro(): Long {
        val leftLimit = 1L
        val rightLimit = 1000000000L
        val generatedLong = leftLimit + (Math.random() * (rightLimit - leftLimit)).toLong()
        return generatedLong
    }
}