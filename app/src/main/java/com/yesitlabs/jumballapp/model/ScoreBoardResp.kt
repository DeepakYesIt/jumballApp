// YApi QuickType插件生成，具体参考文档:https://plugins.jetbrains.com/plugin/18847-yapi-quicktype/documentation

package com.yesitlabs.jumballapp.model

data class ScoreBoardResp (
    val code: Int,
    val data: Data,
    val success: Boolean,
    val message: String
)

data class Data (
    val getUserWonWorldCup: GetUserWonWorldCup?,
    val totalPoint: Int?,
    val totalGoal: totalGoal,
    val getTotalMatch: Int?,
    val total_goal_console: TotalGoalConsole?,
    val winPercent: Int?
)

data class GetUserWonWorldCup (
    val updatedAt: String,
    val user_id: Int,
    val total_won: Int?,
    val created_at: String,
    val id: Int
)

data class totalGoal (
    val totalGoalSum: String?
)

data class TotalGoalConsole (
    val total_goal_console: String?
)
