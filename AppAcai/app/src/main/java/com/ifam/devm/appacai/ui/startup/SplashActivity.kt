package com.ifam.devm.appacai.ui.startup

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.ifam.devm.appacai.R
import com.ifam.devm.appacai.repository.room.AppDatabase
import com.ifam.devm.appacai.repository.sqlite.PREF_DATA_NAME
import com.ifam.devm.appacai.ui.home.HomeActivity
import org.jetbrains.anko.doAsync

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


        // Shared Preferences armazenam configurações de sessão do usuário. Aqui é usado para checar
        // se usuário já fez login. Caso tenha feito, vai para tela de Home, não tendo feito vai
        // para tela de login
        val sharedPreferences = getSharedPreferences(PREF_DATA_NAME, MODE_PRIVATE)
        val login = sharedPreferences.getString("login", "")

        // inicializa banco de dados, quando será adicionado os "serviços padrão"
        // escolhi "getAllServicos" de forma arbitrária, apenas para finalizar a inicialização,
        // quando ele insere também valores padrão, em AppDatabase.getDatabase() --> onCreate(db)
        doAsync {
            AppDatabase.getDatabase(applicationContext).usuarioDao().getAllUsuario()
        }

        // uma maneira de atrasar a execução em X milisegundos, ideal para a tela de splash
        // o código dentro de "postDelayed" será executar após o tempo "delayMillis", aqui sendo 2000 ms
        Handler(Looper.getMainLooper()).postDelayed({
            when (login) {
                "" -> startActivity(Intent(this, StartupActivity::class.java))
                else -> startActivity(Intent(this, HomeActivity::class.java))
            }

            // note que após abrir a Activity é preciso encerrar essa, tirando ela da pilha
            finish()
        }, 2000)
    }
}