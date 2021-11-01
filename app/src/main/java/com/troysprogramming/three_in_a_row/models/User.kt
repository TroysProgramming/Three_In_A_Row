package com.troysprogramming.three_in_a_row.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.troysprogramming.three_in_a_row.models.database.SQLiteService

@Entity(tableName = "user")
class User(id: Int, username: String, password: String) {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public var id = id

    @ColumnInfo(name = "username")
    private var username = username

    @ColumnInfo(name = "password")
    private var password = password

    companion object {
        private var user : User = useDefaultUser()

        private fun useDefaultUser() : User {
            return User(0, "Guest", "")
        }

        fun createUser(username: String, password: String) {

            val newUser = User(
                SQLiteService.getInstance().getHighestUserID() + 1,
                username,
                password
            )

            SQLiteService.getInstance().createUser(newUser)

            loginAsUser(newUser)
        }

        fun loginAsUser(user: User) {
            this.user = user
        }

        fun logout() { this.user = useDefaultUser() }

        fun getUser() : User { return user }
    }

    fun getID() : Int { return id }
    fun getUsername() : String { return username }
    fun getPassword() : String { return password }
}