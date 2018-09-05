package br.ufpe.cin.if710.calculadora


//Como usar a função:
// eval("2+2") == 4.0
// eval("2+3*4") = 14.0
// eval("(2+3)*4") = 20.0
//Fonte: https://stackoverflow.com/a/26227947
fun eval(str: String): Double {
    return object : Any() {
        var pos = -1
        var ch: Char = ' '
        fun nextChar() {
            val size = str.length
            ch = if ((++pos < size)) str[pos] else (-1).toChar()
        }

        fun eat(charToEat: Char): Boolean {
            while (ch == ' ') nextChar()
            if (ch == charToEat) {
                nextChar()
                return true
            }
            return false
        }

        fun parse(): Double {
            nextChar()
            val x = parseExpression()
            if (pos < str.length) throw RuntimeException("Caractere inesperado: $ch")
            return x
        }

        // Grammar:
        // expression = term | expression `+` term | expression `-` term
        // term = factor | term `*` factor | term `/` factor
        // factor = `+` factor | `-` factor | `(` expression `)`
        // | number | functionName factor | factor `^` factor
        fun parseExpression(): Double {
            var x = parseTerm()
            while (true) {
                when {
                    eat('+') -> x += parseTerm() // adição
                    eat('-') -> x -= parseTerm() // subtração
                    else -> return x
                }
            }
        }

        fun parseTerm(): Double {
            var x = parseFactor()
            while (true) {
                when {
                    eat('*') -> x *= parseFactor() // multiplicação
                    eat('/') -> x /= parseFactor() // divisão
                    else -> return x
                }
            }
        }

        fun parseFactor(): Double {
            if (eat('+')) return parseFactor() // + unário
            if (eat('-')) return -parseFactor() // - unário
            var x: Double
            val startPos = this.pos
            if (eat('(')) { // parênteses
                x = parseExpression()
                eat(')')
            } else if ((ch in '0'..'9') || ch == '.') { // números
                while ((ch in '0'..'9') || ch == '.') nextChar()
                x = java.lang.Double.parseDouble(str.substring(startPos, this.pos))
            } else if (ch in 'a'..'z') { // funções
                while (ch in 'a'..'z') nextChar()
                val func = str.substring(startPos, this.pos)
                x = parseFactor()
                x = when (func) {
                    "sqrt" -> Math.sqrt(x)
                    "sin" -> Math.sin(Math.toRadians(x))
                    "cos" -> Math.cos(Math.toRadians(x))
                    "tan" -> Math.tan(Math.toRadians(x))
                    else -> throw RuntimeException("Função desconhecida: $func")
                }
            } else {
                throw RuntimeException("Caractere inesperado: $ch")
            }
            if (eat('^')) x = Math.pow(x, parseFactor()) // potência
            return x
        }
    }.parse()
}