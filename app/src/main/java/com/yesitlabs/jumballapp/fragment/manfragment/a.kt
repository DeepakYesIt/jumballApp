package com.yesitlabs.jumballapp.fragment.manfragment

//    private fun shootBall() {
//        if (!isTimerFinish) {
//            getTestUserShoot()
//            val playerPower: String = if (userType.equals("USER",true)) {
//                allCpuPlayer[selectedPlayerNum!!.text.toString().toInt() - 1].type.uppercase()
//            } else {
//
//                allUserPlayer[selectedPlayerNum!!.text.toString().toInt() - 1].type.uppercase()
//            }
//            if (userType.equals("USER",true)) {
//                cpuDbHelper.updatePlayerUse(allCpuPlayer[selectedPlayerNum!!.text.toString().toInt() - 1].id.toLong(), "true")
//                cpuDbHelper.updatePlayerAnswer(allCpuPlayer[selectedPlayerNum!!.text.toString().toInt() - 1].id.toLong(), "true")
//                Log.d("@@@Error ", "myPass move screen$myPass")
//                Log.d("@@@Error ", "cpuPass move screen$cpuPass")
//                val bundle = Bundle()
//                bundle.putString("userType", "USER")
//                bundle.putInt("selected_player_num", selectedPlayerNum!!.text.toString().toInt())
//                val size = 0/*if (playerPower == "PURPLE") {
//                    1
//                } else {
//                    0
//                }*/
//                for (data in allUserPlayer) {
//                    if (data.id.toInt() == selectedPlayerNum!!.text.toString().toInt()) {
//                        if (data.designation.uppercase() == "DF") {
//                            bundle.putInt("size", size + 2 + myPass)
//                        } else {
//                            if (data.designation.uppercase() == "MF") {
//                                bundle.putInt("size", size + 3 + myPass)
//                            } else {
//                                bundle.putInt("size", size + 4 + myPass)
//                            }
//                        }
//                    }
//                }
//                findNavController().navigate(R.id.shoot_Screen, bundle)
//
//                Log.e("Work", "Shoot Button")
//            } else {
//                myPlayerDbHelper.updatePlayerUse(allUserPlayer[selectedPlayerNum!!.text.toString().toInt() - 1].id.toLong(), "true")
//                myPlayerDbHelper.updatePlayerAnswer(allUserPlayer[selectedPlayerNum!!.text.toString().toInt() - 1].id.toLong(), "true")
//                var size = 0/*if (playerPower == "PURPLE") {
//                    1
//                } else {
//                    0
//                }*/
//
//                Log.d("@@@Error ", "myPass move screen$myPass")
//                Log.d("@@@Error ", "cpuPass move screen$cpuPass")
//
//                val bundle = Bundle()
//                bundle.putString("userType", "USER")
//                for (data in allCpuPlayer) {
//                    if (data.id.toInt() == selectedPlayerNum!!.text.toString().toInt()) {
//                        size = if (data.designation.uppercase() == "DF") {
//                            size + 2 + cpuPass
//                        } else {
//                            if (data.designation.uppercase() == "MF") {
//                                size + 3 + cpuPass
//                            } else {
//                                size + 4 + cpuPass
//                            }
//                        }
//                    }
//                }
//                val num = setGames.getRandomNumber(size)
//                Log.e("Gold Kick", num.toString())
//                Log.d("******", "play screen  :=" + num)
//                bundle.putInt("select_box", num)
//                bundle.putInt("size", size)
//                bundle.putInt("selected_player_num", selectedPlayerNum!!.text.toString().toInt())
//                Log.e("Shoot to Kick", bundle.toString())
//                findNavController().navigate(R.id.goal_keeper_Screen, bundle)
//            }
//        }
//    }