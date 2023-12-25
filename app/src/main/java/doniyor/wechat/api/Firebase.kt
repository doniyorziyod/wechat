package doniyor.wechat.api

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import doniyor.wechat.model.Message
import doniyor.wechat.model.User
import java.text.SimpleDateFormat
import java.util.Date

class Firebase private constructor() {
    companion object {
        private val users = FirebaseDatabase.getInstance().reference.child("users")

        fun register(user: User, context: Context, callback: (Boolean) -> Unit) {
            val key = users.push().key.toString()
            user.key = key
            users.child(key).setValue(user)
            SharedHelper.getInstance(context).saveKey(key)
            callback(true)
        }

        fun usernameAvailable(username: String, callback: (Boolean) -> Unit) {
            users.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val u = snapshot.children
                    u.forEach {
                        val user = it.getValue(User::class.java)!!
                        if (user.username == username.trim().lowercase()) {
                            callback(false)
                        }
                    }
                    callback(true)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("TAG", "onCancelled: ${error.message}")
                }
            })
        }

        fun logIn(username: String, password: String, callback: (key: String?) -> Unit) {
            users.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val users = snapshot.children
                    users.forEach {
                        val user = it.getValue(User::class.java)!!
                        if (user.username == username) {
                            if (user.password!! == password) callback(it.key!!)
                            else callback(null)
                            return
                        }
                    }
                    callback(null)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("TAG", "$error")
                }
            })
        }

        fun writeMessage(text: String, context: Context, to: String) {
            val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
            val currentDate = sdf.format(Date())
            val currentUser = SharedHelper.getInstance(context).getKey()!!

            val key = Firebase.database.reference.push().key.toString()
            val message = Message(to, currentUser, text, currentDate, key)
            users.child(to).child("messages").child(key).setValue(message)
            users.child(currentUser).child("messages").child(key).setValue(message)
        }

        fun getMessages(
            context: Context,
            userKey: String,
            callback: (List<Message>) -> Unit
        ) {
            val key = SharedHelper.getInstance(context).getKey()!!
            users.child(key).child("messages")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val m = snapshot.children
                        val messages = mutableListOf<Message>()
                        m.forEach {
                            val message = it.getValue(Message::class.java)!!
                            if (message.from == userKey || message.to == userKey) messages.add(
                                message
                            )
                        }
                        messages.sortByDescending { it.date }
                        messages.reverse()
                        callback(messages)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })
        }


        fun getAllUsers(context: Context, searchKey: String, callback: (List<User>) -> Unit) {
            users.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val u = snapshot.children
                    val users = mutableListOf<User>()
                    u.forEach {
                        val user = it.getValue(User::class.java)!!
                        if (user.key != SharedHelper.getInstance(context).getKey()) {
                            if (searchKey.isEmpty()) users.add(user)
                            else if (((user.fullName + user.username).lowercase()).contains(
                                    searchKey.lowercase()
                                )
                            ) users.add(
                                user
                            )
                        }
                    }
                    callback(users)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("TAG", "onCancelled: $error")
                }
            })
        }

        fun updatePassword(context: Context, password: String, callback: (Boolean) -> Unit) {
            val key = SharedHelper.getInstance(context).getKey()
            users.child(key).child("password").setValue(password)
            callback(true)
        }

        fun getUser(key: String, callback: (User) -> Unit) {
            users.child(key).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val u = snapshot.getValue(User::class.java)
                    if (u != null) callback(u)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("TAG", error.toString())
                }
            })
        }

        fun getChats(
            searchKey: String,
            context: Context,
            callback: (contacts: List<User>) -> Unit
        ) {
            val currentUserKey = SharedHelper.getInstance(context).getKey()
            val mes = users.child(currentUserKey).child("messages")
            mes.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val keys = mutableListOf<String>()
                    val users = mutableListOf<User>()

                    val u = snapshot.children
                    val messages = mutableListOf<Message>()
                    u.forEach {
                        val message = it.getValue(Message::class.java)!!
                        messages.add(message)
                    }
                    if (messages.isEmpty()) callback(listOf())
                    messages.sortByDescending { it.date }
                    messages.forEach { message ->
                        val userKey =
                            if (message.from == currentUserKey) message.to!! else message.from!!
                        if (!keys.contains(userKey)) {
                            keys.add(userKey)
                        }
                    }
                    keys.forEach { userKey ->
                        getUser(userKey) {
                            if ((it.fullName + it.username).contains(searchKey)) users.add(it)
                            if (keys.size == users.size) callback(users)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("TAG", "$error")
                }
            })
        }
    }
}