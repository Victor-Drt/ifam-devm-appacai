package com.ifam.devm.appacai.utils

class CPFUtil {

    companion object {
        fun validarCadastroCPF(cpf : String) : Boolean{
            val cpfClean = cpf.replace(".", "").replace("-", "")

            //confere se possui 11 digitos
            if (cpfClean.length != 11)
                return false

            //confere se são números
            try {
                val number  = cpfClean.toLong()
            }catch (e : Exception){
                return false
            }

            var dvCurrent10 = cpfClean.substring(9,10).toInt()
            var dvCurrent11= cpfClean.substring(10,11).toInt()

            //a soma dos nove primeiros dígitos determina o décimo digito
            val cpfNineFirst = IntArray(9)
            var i = 9
            while (i > 0 ) {
                cpfNineFirst[i-1] = cpfClean.substring(i-1, i).toInt()
                i--
            }
            //multiplica-se os nove dígitos por seus pesos: 10,9..2
            var sumProductNine = IntArray(9)
            var weight = 10
            var position = 0
            while (weight >= 2){
                sumProductNine[position] = weight * cpfNineFirst[position]
                weight--
                position++
            }
            //Verifica se o nono digito está correto
            var dvForTenthDigit = sumProductNine.sum() % 11
            dvForTenthDigit = 11 - dvForTenthDigit //rule for tenth digit
            if(dvForTenthDigit > 9)
                dvForTenthDigit = 0
            if (dvForTenthDigit != dvCurrent10)
                return false

            //Verifica se o décimo digito está correto
            var cpfTenFirst = cpfNineFirst.copyOf(10)
            cpfTenFirst[9] = dvCurrent10
            //multiplica os nove dígitos por seus pesos: 10,9..2
            val sumProductTen = IntArray(10)
            var w = 11
            var p = 0
            while (w >= 2){
                sumProductTen[p] = w * cpfTenFirst[p]
                w--
                p++
            }
            //Verifica se o décimo primeiro digito está correto
            var dvForeleventhDigit = sumProductTen.sum() % 11
            dvForeleventhDigit = 11 - dvForeleventhDigit //rule for tenth digit
            if(dvForeleventhDigit > 9)
                dvForeleventhDigit = 0
            if (dvForeleventhDigit != dvCurrent11)
                return false

            return true
        }
    }
}