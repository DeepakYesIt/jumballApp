package com.yesitlabs.jumballapp.errormassage

object ErrorMessage {


     const val netWorkError:String="Check your internet connection."
     const val nullResponseError:String="INVALID LOGIN CREDENTIALS !"
     const val passwordPattern:String="^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{6,}$"
     const val emailPattern:String="[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
     const val nameError:String="Full name can't be empty."
     const val countryError:String="Please select country."
     const val skillLevelError:String="Please select skill level."
     const val postionError:String="Please select position."
     const val styleOfPlayError:String="Please select style of play."
     const val worldCupError:String="Please Select World Cup."
     const val emailError:String="Email can't be empty."
     const val emailValidationError:String="Please enter your valid email."
     const val passwordError:String="Password can't be empty."
     const val cnfPasswordError:String="Confirm Password can't be empty."
     const val passwordValidationError:String="Password should be in 8 characters and should contain least one numeric value, alphabets and one special character."
     const val termsError:String="Please accept the terms and condition."
     const val apiError:String="Failed to retrieve data due to a server error."
     const val serverError:String="Failed to retrieve data due to a server error."
     const val sessionError:String="Your session has expired. Please log in again to continue."
}