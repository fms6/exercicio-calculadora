package br.ufpe.cin.if710.calculadora

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : Activity(), View.OnClickListener {
    private var displayedText = ""

    fun Context.toast(message: CharSequence) =
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Recupera valores em caso da activity ter sido destruída
        if (savedInstanceState != null) {
            text_info.text = savedInstanceState.getString("textInfo")
            text_calc.setText(savedInstanceState.getString("textCalc"))
        }

        // Define um mesmo listener para todos os botões que exibem caracteres na tela
        btn_0.setOnClickListener(this)
        btn_1.setOnClickListener(this)
        btn_2.setOnClickListener(this)
        btn_3.setOnClickListener(this)
        btn_4.setOnClickListener(this)
        btn_5.setOnClickListener(this)
        btn_6.setOnClickListener(this)
        btn_7.setOnClickListener(this)
        btn_8.setOnClickListener(this)
        btn_9.setOnClickListener(this)
        btn_Add.setOnClickListener(this)
        btn_Subtract.setOnClickListener(this)
        btn_Multiply.setOnClickListener(this)
        btn_Divide.setOnClickListener(this)
        btn_Dot.setOnClickListener(this)
        btn_Power.setOnClickListener(this)
        btn_LParen.setOnClickListener(this)
        btn_RParen.setOnClickListener(this)

        // Botão "clear" limpa todo o texto exibido na tela
        btn_Clear.setOnClickListener {
            displayedText = ""
            text_info.text = ""
            text_calc.setText("")
        }

        btn_Equal.setOnClickListener{
            val text = text_info.text.toString()

            try {
                val result = eval(text).toString()  // Calcula a expressão  armazenada em text_info e exibe em text_calc
                text_calc.setText(result)
                displayedText = ""                  // Limpa o buffer de texto para que o próximo caractere digitado seja o primeiro da expressão
            } catch (e: RuntimeException) {
                this.toast(e.message.toString())    // Caso haja algum erro no cálculo, exibe um toast com a mensagem de erro
            }
        }
    }

    // Armazena valores antes da activity ser destruída
    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putString("textCalc", text_calc.text.toString())
        outState?.putString("textInfo", text_info.text.toString())
    }


    // Adiciona um novo caractere à string exibida na tela
    override fun onClick(btnView: View?) {
        val btn = btnView as Button
        displayedText += btn.tag.toString()
        text_info.text = displayedText
        text_calc.setText("")
    }

}
