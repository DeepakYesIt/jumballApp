package com.yesitlabs.jumballapp

class AppConstant {
    companion object {
        const val NAME: String = "Name"
        const val EMAIL: String = "Email"
        const val PASSWORD: String = "Password"
        const val OTP: String = "OTP"


        const val MEDIA_URL = "https://jumball.tgastaging.com/assets/images/"
        const val STICKER_URL = "https://jumball.tgastaging.com/"

        fun getValue(length:Int) :IntArray{
            when(length){
                5 -> {
                    val randomNumber = (1..5).random()
                    when (randomNumber) {
                        1 -> {
                            val intArray = IntArray(5)
                            intArray[0] = 1
                            intArray[1] = 3
                            intArray[2] = 4
                            intArray[3] = 2
                            intArray[4] = 5
                            return intArray
                        }
                        2 -> {
                            val intArray = IntArray(5)
                            intArray[0] = 3
                            intArray[1] = 5
                            intArray[2] = 2
                            intArray[3] = 1
                            intArray[4] = 4
                            return intArray
                        }
                        3 -> {
                            val intArray = IntArray(5)
                            intArray[0] = 5
                            intArray[1] = 3
                            intArray[2] = 4
                            intArray[3] = 2
                            intArray[4] = 1
                            return intArray
                        }
                        4 -> {
                            val intArray = IntArray(5)
                            intArray[0] = 1
                            intArray[1] = 5
                            intArray[2] = 2
                            intArray[3] = 4
                            intArray[4] = 3
                            return intArray
                        }
                        else -> {
                            val intArray = IntArray(5)
                            intArray[0] = 4
                            intArray[1] = 3
                            intArray[2] = 2
                            intArray[3] = 1
                            intArray[4] = 5
                            return intArray
                        }
                    }
                }
                6->{
                    val randomNumber = (1..5).random()

                    when (randomNumber) {
                        1 -> {
                            val intArray = IntArray(6)
                            intArray[0] = 5
                            intArray[1] = 3
                            intArray[2] = 2
                            intArray[3] = 4
                            intArray[4] = 6
                            intArray[5] = 1
                            return intArray
                        }
                        2 -> {
                            val intArray = IntArray(6)
                            intArray[0] = 1
                            intArray[1] = 2
                            intArray[2] = 3
                            intArray[3] = 4
                            intArray[4] = 6
                            intArray[5] = 5
                            return intArray
                        }
                        3 -> {
                            val intArray = IntArray(6)
                            intArray[0] = 5
                            intArray[1] = 4
                            intArray[2] = 6
                            intArray[3] = 2
                            intArray[4] = 1
                            intArray[5] = 3
                            return intArray
                        }
                        4 -> {
                            val intArray = IntArray(6)
                            intArray[0] = 3
                            intArray[1] = 5
                            intArray[2] = 2
                            intArray[3] = 6
                            intArray[4] = 1
                            intArray[5] = 4
                            return intArray
                        }
                        else -> {
                            val intArray = IntArray(6)
                            intArray[0] = 3
                            intArray[1] = 6
                            intArray[2] = 5
                            intArray[3] = 2
                            intArray[4] = 1
                            intArray[5] = 4
                            return intArray
                        }
                    }
                }
                7->{
                    val randomNumber = (1..5).random()

                    when (randomNumber) {
                        1 -> {
                            val intArray = IntArray(7)
                            intArray[0] = 7
                            intArray[1] = 6
                            intArray[2] = 5
                            intArray[3] = 4
                            intArray[4] = 3
                            intArray[5] = 2
                            intArray[6]=1
                            return intArray
                        }
                        2 -> {
                            val intArray = IntArray(7)
                            intArray[0] = 1
                            intArray[1] = 2
                            intArray[2] = 3
                            intArray[3] = 5
                            intArray[4] = 4
                            intArray[5] = 7
                            intArray[6] = 6
                            return intArray
                        }
                        3 -> {
                            val intArray = IntArray(7)
                            intArray[0] = 4
                            intArray[1] = 5
                            intArray[2] = 7
                            intArray[3] = 3
                            intArray[4] = 1
                            intArray[5] = 2
                            intArray[6] = 6
                            return intArray
                        }
                        4 -> {
                            val intArray = IntArray(7)
                            intArray[0] = 1
                            intArray[1] = 5
                            intArray[2] = 6
                            intArray[3] = 7
                            intArray[4] = 4
                            intArray[5] = 3
                            intArray[6] = 2
                            return intArray
                        }
                        else -> {
                            val intArray = IntArray(7)
                            intArray[0] = 1
                            intArray[1] = 7
                            intArray[2] = 2
                            intArray[3] = 6
                            intArray[4] = 4
                            intArray[5] = 3
                            intArray[6] = 5
                            return intArray
                        }
                    }
                }
                4->{
                    val randomNumber = (1..5).random()

                    when (randomNumber) {
                        1 -> {
                            val intArray = IntArray(4)
                            intArray[0] = 2
                            intArray[1] = 3
                            intArray[2] = 1
                            intArray[3] = 4
                            return intArray
                        }
                        2 -> {
                            val intArray = IntArray(4)
                            intArray[0] = 4
                            intArray[1] = 3
                            intArray[2] = 2
                            intArray[3] = 1
                            return intArray
                        }
                        3 -> {
                            val intArray = IntArray(4)
                            intArray[0] = 1
                            intArray[1] = 2
                            intArray[2] = 4
                            intArray[3] = 3
                            return intArray
                        }
                        4 -> {
                            val intArray = IntArray(4)
                            intArray[0] = 3
                            intArray[1] = 1
                            intArray[2] = 2
                            intArray[3] = 4

                            return intArray
                        }
                        else -> {
                            val intArray = IntArray(4)
                            intArray[0] = 3
                            intArray[1] = 1
                            intArray[2] = 4
                            intArray[3] = 2

                            return intArray
                        }
                    }
                }
                3->{
                    val randomNumber = (1..5).random()

                    when (randomNumber) {
                        1 -> {
                            val intArray = IntArray(3)
                            intArray[0] = 3
                            intArray[1] = 2
                            intArray[2] = 1

                            return intArray
                        }
                        2 -> {
                            val intArray = IntArray(3)
                            intArray[0] = 2
                            intArray[1] = 3
                            intArray[2] = 1

                            return intArray
                        }
                        3 -> {
                            val intArray = IntArray(3)
                            intArray[0] = 2
                            intArray[1] = 1
                            intArray[2] = 3

                            return intArray
                        }
                        4 -> {
                            val intArray = IntArray(3)
                            intArray[0] = 1
                            intArray[1] = 3
                            intArray[2] = 2

                            return intArray
                        }
                        else -> {
                            val intArray = IntArray(3)
                            intArray[0] = 3
                            intArray[1] = 1
                            intArray[2] = 2

                            return intArray
                        }
                    }
                }

                else->{
                    val intArray = IntArray(5)
                    intArray[0] = 1
                    intArray[1] = 5
                    intArray[2] = 2
                    intArray[3] = 4
                    intArray[4] = 3
                    return intArray
                }

            }
        }
    }

}