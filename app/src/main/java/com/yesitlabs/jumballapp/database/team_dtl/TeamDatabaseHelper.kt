package com.yesitlabs.jumballapp.database.team_dtl

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.yesitlabs.jumballapp.database.player_dtl.CPUPlayerDatabaseHelper

class TeamDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "TeamDatabase.db"
        private const val TABLE_NAME = "teams"
        private const val COLUMN_ID = "id"
        private const val COLUMN_CAPTAIN_NAME = "captain_name"
        private const val COLUMN_COUNTRY = "country"
        private const val COLUMN_ENABLE = "enable"
        private const val COLUMN_PLD = "PLD"
        private const val COLUMN_W = "W"
        private const val COLUMN_D = "D"
        private const val COLUMN_L = "L"
        private const val COLUMN_F = "F"
        private const val COLUMN_A = "A"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_TEAMS_TABLE =
            ("CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY, $COLUMN_CAPTAIN_NAME TEXT, $COLUMN_COUNTRY TEXT, $COLUMN_ENABLE INTEGER, $COLUMN_PLD INTEGER, $COLUMN_W INTEGER, $COLUMN_D INTEGER, $COLUMN_L INTEGER, $COLUMN_F INTEGER, $COLUMN_A INTEGER)")
        db.execSQL(CREATE_TEAMS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }


    @SuppressLint("SuspiciousIndentation")
    fun addTeam(team: TeamModel) {
        val values = ContentValues()
        values.put(COLUMN_CAPTAIN_NAME, team.captainName)
        values.put(COLUMN_COUNTRY, team.country)
        values.put(COLUMN_ENABLE, team.enable)
        values.put(COLUMN_PLD, team.PLD)
        values.put(COLUMN_W, team.W)
        values.put(COLUMN_D, team.D)
        values.put(COLUMN_L, team.L)
        values.put(COLUMN_F, team.F)
        values.put(COLUMN_A, team.A)

        val db = this.writableDatabase
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    @SuppressLint("Range")
    fun getAllTeams(): ArrayList<TeamModel> {
        val teamsList = ArrayList<TeamModel>()
        val selectQuery = "SELECT * FROM $TABLE_NAME"

        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()) {
            do {
                val team = TeamModel(
                    cursor.getInt(cursor.getColumnIndex(COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_CAPTAIN_NAME)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_COUNTRY)),
                    cursor.getInt(cursor.getColumnIndex(COLUMN_ENABLE)),
                    cursor.getInt(cursor.getColumnIndex(COLUMN_PLD)),
                    cursor.getInt(cursor.getColumnIndex(COLUMN_W)),
                    cursor.getInt(cursor.getColumnIndex(COLUMN_D)),
                    cursor.getInt(cursor.getColumnIndex(COLUMN_L)),
                    cursor.getInt(cursor.getColumnIndex(COLUMN_F)),
                    cursor.getInt(cursor.getColumnIndex(COLUMN_A)),
                )
                teamsList.add(team)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return teamsList
    }


    @SuppressLint("Range")
    fun updatePLD(
        id: Int,
        newPLD: Int,
    ) {
        val db = this.writableDatabase
        val cursor = db.rawQuery("SELECT $COLUMN_PLD FROM $TABLE_NAME WHERE $COLUMN_ID = ?", arrayOf(id.toString()))

        var currentPLD = 0 // Default value if the cursor doesn't return any result

        if (cursor.moveToFirst()) {
            currentPLD = cursor.getInt(cursor.getColumnIndex(COLUMN_PLD))
        }

        cursor.close()

        val totalPLD = currentPLD + newPLD

        val values = ContentValues().apply {
            put(COLUMN_PLD, totalPLD)
        }

        db.update(TABLE_NAME, values, "$COLUMN_ID = ?", arrayOf(id.toString()))
        db.close()
    }


    @SuppressLint("Range")
    fun updateW(
        id: Int,
        newW: Int,
    ) {
        val db = this.writableDatabase
        val cursor = db.rawQuery("SELECT $COLUMN_W FROM $TABLE_NAME WHERE $COLUMN_ID = ?", arrayOf(id.toString()))

        var currentW = 0 // Default value if the cursor doesn't return any result

        if (cursor.moveToFirst()) {
            currentW = cursor.getInt(cursor.getColumnIndex(COLUMN_W))
        }

        cursor.close()

        val totalW = currentW + newW

        val values = ContentValues().apply {
            put(COLUMN_W, totalW)
        }

        db.update(TABLE_NAME, values, "$COLUMN_ID = ?", arrayOf(id.toString()))
        db.close()
    }

    @SuppressLint("Range")
    fun updateD(
        id: Int,
        newD: Int,
    ) {
        val db = this.writableDatabase
        val cursor = db.rawQuery("SELECT $COLUMN_D FROM $TABLE_NAME WHERE $COLUMN_ID = ?", arrayOf(id.toString()))

        var currentD = 0 // Default value if the cursor doesn't return any result

        if (cursor.moveToFirst()) {
            currentD = cursor.getInt(cursor.getColumnIndex(COLUMN_D))
        }

        cursor.close()

        val totalD = currentD + newD

        val values = ContentValues().apply {
            put(COLUMN_D, totalD)
        }

        db.update(TABLE_NAME, values, "$COLUMN_ID = ?", arrayOf(id.toString()))
        db.close()

    }

    @SuppressLint("Range")
    fun updateL(
        id: Int,
        newL: Int,
    ) {
        val db = this.writableDatabase
        val cursor = db.rawQuery("SELECT $COLUMN_L FROM $TABLE_NAME WHERE $COLUMN_ID = ?", arrayOf(id.toString()))

        var currentL = 0 // Default value if the cursor doesn't return any result

        if (cursor.moveToFirst()) {
            currentL = cursor.getInt(cursor.getColumnIndex(COLUMN_L))
        }

        cursor.close()

        val totalL = currentL + newL

        val values = ContentValues().apply {
            put(COLUMN_L, totalL)
        }

        db.update(TABLE_NAME, values, "$COLUMN_ID = ?", arrayOf(id.toString()))
        db.close()

    }

    @SuppressLint("Range")
    fun updateF(
        id: Int,
        newF: Int,
    ) {

        val db = this.writableDatabase
        val cursor = db.rawQuery("SELECT $COLUMN_F FROM $TABLE_NAME WHERE $COLUMN_ID = ?", arrayOf(id.toString()))

        var currentF = 0 // Default value if the cursor doesn't return any result

        if (cursor.moveToFirst()) {
            currentF = cursor.getInt(cursor.getColumnIndex(COLUMN_F))
        }

        cursor.close()

        val totalF = currentF + newF

        val values = ContentValues().apply {
            put(COLUMN_F, totalF)
        }

        db.update(TABLE_NAME, values, "$COLUMN_ID = ?", arrayOf(id.toString()))
        db.close()
    }

    @SuppressLint("Range")
    fun updateA(
        id: Int,
        newA: Int,
    ) {
        val db = this.writableDatabase
        val cursor = db.rawQuery("SELECT $COLUMN_A FROM $TABLE_NAME WHERE $COLUMN_ID = ?", arrayOf(id.toString()))

        var currentA = 0 // Default value if the cursor doesn't return any result

        if (cursor.moveToFirst()) {
            currentA = cursor.getInt(cursor.getColumnIndex(COLUMN_A))
        }

        cursor.close()

        val totalA = currentA + newA

        val values = ContentValues().apply {
            put(COLUMN_A, totalA)
        }

        db.update(TABLE_NAME, values, "$COLUMN_ID = ?", arrayOf(id.toString()))
        db.close()
    }




    fun update_team_Enabling(
        id: Int,
        enable: Int
    ) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_ENABLE, enable)
        }
        db.update(TABLE_NAME, values, "$COLUMN_ID = ?", arrayOf(id.toString()))
        db.close()
    }

    fun deleteAllTeams() {
        val db = this.writableDatabase
        db.delete(TABLE_NAME, null, null)
        db.close()
    }


    /*


    intilize
    val TEAMdbHelper = TeamDatabaseHelper(context)

    add
    val Team = TeamModel("John Doe", "10", "Forward", "Red")
    TEAMdbHelper.addTeam(Team)

    getallPlayer
    val allTeam = TEAMdbHelper.getAllTeams()

    updateTeam
    TEAMdbHelper.updatePTS(_,_)
    */

}

