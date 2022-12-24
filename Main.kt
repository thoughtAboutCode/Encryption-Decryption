package encryptdecrypt

import java.io.File

private const val decryptOp = "dec"
private const val encryptOp = "enc"
private const val modeCommand = "-mode"
private const val keyCommand = "-key"
private const val dataCommand = "-data"
private const val inCommand = "-in"
private const val outCommand = "-out"
private const val algCommand = "-alg"
private const val shiftAlg = "shift"

fun encrypt(message: String, key: Int): String {
    return message.map {
        it + key
    }.joinToString(separator = "")
}

fun decrypt(message: String, key: Int): String {
    return message.map {
        it - key
    }.joinToString(separator = "")
}

fun main(args: Array<String>) {
    try {
        val targetOp = if (args.contains(modeCommand)) args[args.indexOf(modeCommand) + 1] else encryptOp
        val message: String = if (args.contains(dataCommand)) args[args.indexOf(dataCommand) + 1] else if (args.contains(inCommand)) File(args[args.indexOf(inCommand) + 1]).readText() else ""
        val key = if (args.contains(keyCommand)) args[args.indexOf(keyCommand) + 1].toInt() else 0
        val alg = if (args.contains(algCommand)) args[args.indexOf(algCommand) + 1] else shiftAlg
        val result = if (targetOp == encryptOp) {
            if (alg == shiftAlg) shiftEncrypt(message, key) else encrypt(message, key)
        } else {
            if (alg == shiftAlg) shiftDecrypt(message, key) else decrypt(message, key)
        }
        if (args.contains(outCommand)) {
            File(args[args.indexOf(outCommand) + 1]).writeText(result)
        } else {
            println(result)
        }
    } catch (ex: Exception) {
        println("Error: ${ex.message}")
    }
}

fun shiftEncrypt(message: String, key: Int): String {
    val englishAlphabetLowercase = ('a'..'z').toList()
    val englishAlphabetUppercase = ('A'..'Z').toList()
    return message.map {
        when (it) {
            in englishAlphabetLowercase -> {
                getShiftCharEncrypt(englishAlphabetLowercase, key, it)
            }

            in englishAlphabetUppercase -> {
                getShiftCharEncrypt(englishAlphabetUppercase, key, it)
            }

            else -> it.lowercaseChar()
        }
    }.joinToString(separator = "")
}

fun shiftDecrypt(message: String, key: Int): String {
    val englishAlphabetLowercase = ('a'..'z').toList()
    val englishAlphabetUppercase = ('A'..'Z').toList()
    return message.map {
        when (it) {
            in englishAlphabetLowercase -> {
                getShiftCharDecrypt(englishAlphabetLowercase, key, it)
            }

            in englishAlphabetUppercase -> {
                getShiftCharDecrypt(englishAlphabetUppercase, key, it)
            }

            else -> it.lowercaseChar()
        }
    }.joinToString(separator = "")
}

fun getShiftCharDecrypt(chars: List<Char>, key: Int, char: Char): Char {
    val index = chars.indexOf(char) + 1
    return chars[((index - key).takeIf { it >= 0 } ?: (chars.size + index - key)) - 1]
}

fun getShiftCharEncrypt(chars: List<Char>, key: Int, char: Char): Char {
    val index = chars.indexOf(char) + 1
    return chars[((index + key) % chars.size) - 1]
}
