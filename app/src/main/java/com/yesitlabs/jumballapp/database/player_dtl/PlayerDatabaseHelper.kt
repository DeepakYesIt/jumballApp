package com.yesitlabs.jumballapp.database.player_dtl

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class PlayerDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "myteamplayer"
        private const val TABLE_NAME = "players"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_CAPTAIN = "captain"
        private const val COLUMN_COUNTRY = "country"
        private const val COLUMN_TYPE = "type"
        private const val COLUMN_DESIGNATION = "designation"
        private const val COLUMN_JERSEYNUMBER = "jersynumber"
        private const val COLUMN_ANSWER = "answer"
        private const val COLUMN_USE = "use"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_PLAYERS_TABLE = ("CREATE TABLE $TABLE_NAME (" +
                "$COLUMN_ID INTEGER PRIMARY KEY, " +
                "$COLUMN_NAME TEXT, " +
                "$COLUMN_CAPTAIN TEXT, " +
                "$COLUMN_COUNTRY TEXT, " +
                "$COLUMN_TYPE TEXT, " +
                "$COLUMN_DESIGNATION TEXT, " +
                "$COLUMN_JERSEYNUMBER TEXT, " +
                "$COLUMN_ANSWER TEXT, " +
                "$COLUMN_USE TEXT)")
        db.execSQL(CREATE_PLAYERS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addPlayer(name: String, captain: String, country: String, type: String, designation: String, jersynumber: String, answer: String, use: String) {
        val values = ContentValues().apply {
            put(COLUMN_NAME, name)
            put(COLUMN_CAPTAIN, captain)
            put(COLUMN_COUNTRY, country)
            put(COLUMN_TYPE, type)
            put(COLUMN_DESIGNATION, designation)
            put(COLUMN_JERSEYNUMBER, jersynumber)
            put(COLUMN_ANSWER, answer)
            put(COLUMN_USE, use)
        }

        val db = this.writableDatabase
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    @SuppressLint("Range")
    fun getAllPlayers(): ArrayList<PlayerModel> {
        val playersList = ArrayList<PlayerModel>()
        val selectQuery = "SELECT * FROM $TABLE_NAME"

        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()) {
            do {
                val player = PlayerModel(
                    cursor.getString(cursor.getColumnIndex(COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_NAME)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_CAPTAIN)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_COUNTRY)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_TYPE)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_DESIGNATION)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_JERSEYNUMBER)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_ANSWER)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_USE))// convert integer to boolean
                )
                playersList.add(player)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return playersList
    }

    fun deleteAllPlayers() {
        val db = this.writableDatabase
        db.delete(TABLE_NAME, null, null)
        db.close()
    }

    fun updatePlayerUse(playerId: Long, use: String): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_USE, use)
        }
        val whereClause = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(playerId.toString())
        val rowsUpdated = db.update(TABLE_NAME, values, whereClause, whereArgs)
        db.close()
        return rowsUpdated
    }


    fun updatePlayerAnswer(playerId: Long, newAnswer: String): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_ANSWER, newAnswer)
        }
        val whereClause = "${COLUMN_ID} = ?"
        val whereArgs = arrayOf(playerId.toString())

        val rowsUpdated = db.update(TABLE_NAME, values, whereClause, whereArgs)
        db.close()

        return rowsUpdated
    }
}
