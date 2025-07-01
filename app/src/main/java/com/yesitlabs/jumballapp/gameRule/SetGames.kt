package com.yesitlabs.jumballapp.gameRule

import android.util.Log
import com.yesitlabs.jumballapp.R
import com.yesitlabs.jumballapp.model.guessName.GuessName
import kotlin.random.Random

class SetGames {


    fun setScreen(screen: String) : ScreenRow
    {
        val screenSet  = ScreenRow(5,4,1)

        when (screen) {

            "5-2-3" -> {

                   screenSet.r1 = 5
                   screenSet.r2 = 2
                   screenSet.r3 = 3

            }

            "5-4-1" -> {
                screenSet.r1 = 5
                screenSet.r2 = 4
                screenSet.r3 = 1
            }

            "5-3-2"-> {
                screenSet.r1 = 5
                screenSet.r2 = 3
                screenSet.r3 = 2
            }

            "3-5-2" -> {

                screenSet.r1 = 3
                screenSet.r2 = 5
                screenSet.r3 = 2
            }


            "4-5-1" -> {

                screenSet.r1 = 4
                screenSet.r2 = 5
                screenSet.r3 = 1
            }


            "4-4-2" -> {

                screenSet.r1 = 4
                screenSet.r2 = 4
                screenSet.r3 = 2
            }

            "4-3-3" -> {
                screenSet.r1 = 4
                screenSet.r2 = 3
                screenSet.r3 = 3
            }

            "3-4-3" -> {

                screenSet.r1 = 3
                screenSet.r2 = 4
                screenSet.r3 = 3
            }

            "4-2-4" -> {

                screenSet.r1 = 4
                screenSet.r2 = 2
                screenSet.r3 = 4
            }

        }

        return screenSet
    }

    fun getRandomPlayer(row1 : Int , row2 : Int , row3 : Int ) : Int{

        when(Random.nextInt(1, 4)){
            1 -> {
                if (row1 == 1)
                {
                    return 1
                }else{
                    when(Random.nextInt(1, row1)){
                        1-> {
                            return 1
                        }
                        2->{
                            return 2
                        }
                        3->{
                            return 3
                        }
                        4->{
                            return 4
                        }
                        5->{
                            return 5
                        }

                        else ->{
                            return 1
                        }
                    }

                }

            }

            2 ->{
                if (row2 == 1)
                {
                    return 6
                }else{
                    when(Random.nextInt(1, row2)){
                        1-> {
                            return 6
                        }
                        2->{
                            return 7
                        }
                        3->{
                            return 8
                        }
                        4->{
                            return 9
                        }
                        5->{
                            return 10
                        }

                        else ->{
                            return 6
                        }
                    }
                }

            }

            3 ->{

                if (row3 == 1)
                {
                    return  11
                }else{
                    when(Random.nextInt(1, row3)){
                        1-> {
                            return 11
                        }
                        2->{
                            return 12
                        }
                        3->{
                            return 13
                        }
                        4->{
                            return 14
                        }
                        5->{
                            return 15
                        }

                        else ->{
                            return 11
                        }
                    }
                }


            }

            else ->{
                if (row1 == 1){
                    return 1
                }else{
                    when(Random.nextInt(1, row1)){
                        1-> {
                            return 1
                        }
                        2->{
                            return 2
                        }
                        3->{
                            return 3
                        }
                        4->{
                            return 4
                        }
                        5->{
                            return 5
                        }

                        else ->{
                            return 1
                        }
                    }
                }

            }
        }
    }

    fun getRandomNumber(maxNum : Int) : Int {
        return  Random.nextInt(0, maxNum)
    }

    fun getRandomGustedPlayerName(hintList: ArrayList<GuessName>, answerList: ArrayList<GuessName>):  ArrayList<Int> {

        val result  = ArrayList<Int>()

        when(Random.nextInt(1, 3)){

            1 -> {
                for (data in answerList) {
                    for (i in 0 until hintList.size) {
                        if (data.hint == hintList[i].hint && !hintList[i].used) {
                            result.add(i)
                            hintList[i].used = true
                        }
                    }
                }
                return result
            }

            else ->{

              do {
                    val num = getRandomNumber(hintList.size)
                    if (result.isEmpty()){
                        result.add(num)
                    }else{
                        if (!result.contains(num)){
                            result.add(num)
                        }
                    }
                }while (result.size < hintList.size)

                Log.e("hint_data" , result.toString() )
                return result
            }
        }

    }

    fun shuffleName(name: String): ArrayList<GuessName> {
        val shuffledChars = name.toCharArray()
        shuffledChars.shuffle() // Shuffle the characters
        return getCharactersList(String(shuffledChars))
    }


    fun generatePermutations(letters: List<Char>): List<String> {
        if (letters.isEmpty()) return listOf("")

        val permutations = mutableListOf<String>()
        for ((index, letter) in letters.withIndex()) {
            val remainingLetters = letters.toMutableList()
            remainingLetters.removeAt(index)
            val subPermutations = generatePermutations(remainingLetters)
            for (subPermutation in subPermutations) {
                permutations.add(letter + subPermutation)
            }
        }
        return permutations
    }

     fun getCharactersList(name: String): ArrayList<GuessName> {
        val charactersList = ArrayList<GuessName>()
        for (char in name) {
            charactersList.add(
                GuessName(
                    char.toString(),
                    R.color.hint_color_unselect,
                    R.color.black,
                    false
                )
            )
        }
        return charactersList
    }


    fun getTShirtImage(country : String): Int {

        return when(country){
            "QAT" -> R.drawable.qat
            "ECU" -> R.drawable.ecu
            "SEN" -> R.drawable.sen
            "NED" -> R.drawable.ned
            "ENG" -> R.drawable.eng
            "IRN" -> R.drawable.irn
            "USA" -> R.drawable.usa
            "WAL" -> R.drawable.wal
            "ARG" -> R.drawable.arg
            "KSA" -> R.drawable.ksa
            "MEX" -> R.drawable.mex
            "POL" -> R.drawable.pol
            "FRA" -> R.drawable.fra
            "AUS" -> R.drawable.aus
            "DEN" -> R.drawable.den
            "TUN" -> R.drawable.tun
            "ESP" -> R.drawable.esp
            "CRC" -> R.drawable.crc
            "GER" -> R.drawable.ger
            "JPN" -> R.drawable.jpn
            "BEL" -> R.drawable.bel
            "CAN" -> R.drawable.can
            "MAR" -> R.drawable.mar
            "CRO" -> R.drawable.cro
            "BRA" -> R.drawable.bra
            "SRB" -> R.drawable.srb
            "SUI" -> R.drawable.sui // pending
            "CMR" -> R.drawable.cmr
            "POR" -> R.drawable.por
            "GHA" -> R.drawable.gha
            "URU" -> R.drawable.uru
            "KOR" -> R.drawable.kor
            else -> R.drawable.arg

           /* "QAT" -> R.drawable.player_golkeeper_img
            "ECU" -> R.drawable.player_golkeeper_img
            "SEN" -> R.drawable.player_golkeeper_img
            "NED" -> R.drawable.player_golkeeper_img
            "ENG" -> R.drawable.player_golkeeper_img
            "IRN" -> R.drawable.player_golkeeper_img
            "USA" -> R.drawable.player_golkeeper_img
            "WAL" -> R.drawable.player_golkeeper_img
            "ARG" -> R.drawable.player_golkeeper_img
            "KSA" -> R.drawable.player_golkeeper_img
            "MEX" -> R.drawable.player_golkeeper_img
            "POL" -> R.drawable.player_golkeeper_img
            "FRA" -> R.drawable.player_golkeeper_img
            "AUS" -> R.drawable.player_golkeeper_img
            "DEN" -> R.drawable.player_golkeeper_img
            "TUN" -> R.drawable.player_golkeeper_img
            "ESP" -> R.drawable.player_golkeeper_img
            "CRC" -> R.drawable.player_golkeeper_img
            "GER" -> R.drawable.player_golkeeper_img
            "JPN" -> R.drawable.player_golkeeper_img
            "BEL" -> R.drawable.player_golkeeper_img
            "CAN" -> R.drawable.player_golkeeper_img
            "MAR" -> R.drawable.player_golkeeper_img
            "CRO" -> R.drawable.player_golkeeper_img
            "BRA" -> R.drawable.player_golkeeper_img
            "SRB" -> R.drawable.player_golkeeper_img
            "SUI" -> R.drawable.player_golkeeper_img // pending
            "CMR" -> R.drawable.player_golkeeper_img
            "POR" -> R.drawable.player_golkeeper_img
            "GHA" -> R.drawable.player_golkeeper_img
            "URU" -> R.drawable.player_golkeeper_img
            "KOR" -> R.drawable.player_golkeeper_img
            else -> R.drawable.player_golkeeper_img*/

        }

    }
    fun getTShirtTextColor(country: String) : Int{
        return when(country){
            "QAT" -> R.color.white
            "ECU" -> R.color.black
            "SEN" -> R.color.white
            "NED" -> R.color.black
            "ENG" -> R.color.black
            "IRN" -> R.color.black
            "USA" -> R.color.black
            "WAL" -> R.color.white
            "ARG" -> R.color.black
            "KSA" -> R.color.black
            "MEX" -> R.color.white
            "POL" -> R.color.black
            "FRA" -> R.color.white
            "AUS" -> R.color.black
            "DEN" -> R.color.white
            "TUN" -> R.color.white
            "ESP" -> R.color.white
            "CRC" -> R.color.white
            "GER" -> R.color.white
            "JPN" -> R.color.white
            "BEL" -> R.color.white
            "CAN" -> R.color.white
            "MAR" -> R.color.white
            "CRO" -> R.color.black
            "BRA" -> R.color.black
            "SRB" -> R.color.white
            "SUI" -> R.color.white
            "CMR" -> R.color.white
            "POR" -> R.color.white
            "GHA" -> R.color.black
            "URU" -> R.color.black
            "KOR" -> R.color.white
             else -> R.color.black
        }
    }


}