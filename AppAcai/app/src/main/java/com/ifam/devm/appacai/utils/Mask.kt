package com.ifam.devm.appacai.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

class Mask {
    companion object {
        private fun replaceChars(fullString : String) : String{
            return fullString
                .replace(".", "")
                .replace("-", "")
                .replace("(", "")
                .replace(")", "")
                .replace("/", "")
                .replace(" ", "")
                .replace("*", "")
                .replace("+", "")
        }

        fun mask(mask : String, editableString : EditText) : TextWatcher {

            var internalMask = mask

            return object : TextWatcher {
                var isUpdating : Boolean = false
                var oldString : String = ""

                override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) { }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    val str = replaceChars(s.toString())
                    var stringWithMask = ""

                    if (internalMask == "+## (##) ####-#####" && str.length == 13) {
                        internalMask = "+## (##) #####-####"
                    }

                    if (internalMask == "+## (##) #####-####" && str.length < 13) {
                        internalMask = "+## (##) ####-#####"
                    }

                    if (count == 0) {//is deleting
                        isUpdating = true
                    }

                    if (isUpdating){
                        oldString = str
                        isUpdating = false
                        return
                    }

                    var i = 0
                    //+## (##) ####-#####"
                    for (m : Char in internalMask.toCharArray()){
                        if (m != '#' && str.length > oldString.length){
                            stringWithMask += m
                            continue
                        }
                        try {
                            stringWithMask += str[i]
                        }catch (e : Exception){
                            break
                        }
                        i++
                    }

                    isUpdating = true

                    editableString.setText(stringWithMask)
                    editableString.setSelection(stringWithMask.length)

                }

                override fun afterTextChanged(editable: Editable) {}
            }
        }
    }
}