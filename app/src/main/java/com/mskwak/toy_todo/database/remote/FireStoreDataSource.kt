package com.mskwak.toy_todo.database.remote

import android.util.Log
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.mskwak.toy_todo.AppApplication
import com.mskwak.toy_todo.model.Task
import com.mskwak.toy_todo.model.Task.Companion.FIELD_COMPLETED
import com.mskwak.toy_todo.model.Task.Companion.FIELD_MEMO
import com.mskwak.toy_todo.model.Task.Companion.FIELD_TITLE
import kotlinx.coroutines.tasks.await

class FireStoreDataSource(
    private val db: FirebaseFirestore
) : RemoteDataSource {

    private val email: String
        get() {
            return AppApplication.INSTANCE.currentUserEmail ?: kotlin.run {
                throw NullPointerException("email is null")
            }
        }

    private val todoRef: CollectionReference
        get() = db.collection(USER_COLLECTION).document(email)
            .collection(TODO_COLLECTION)

    override suspend fun insertTask(id: Long, task: Task) {
        val map = hashMapOf(
            FIELD_TITLE to task.title,
            FIELD_MEMO to task.memo,
            FIELD_COMPLETED to task.isCompleted
        )

        todoRef.document(id.toString())
            .set(map)
            .addOnSuccessListener {
                Log.d(TAG, "insert task to firestore: success")
            }.addOnFailureListener {
                Log.w(TAG, "insert task to firesotore: fail", it)
            }
    }

    override suspend fun updateTask(task: Task) {
        insertTask(task.id, task)
    }

    override suspend fun updateCompleted(taskId: Long, completed: Boolean) {
        todoRef.document(taskId.toString())
            .update(FIELD_COMPLETED, completed)
            .addOnSuccessListener {
                Log.d(TAG, "update completed from firestore: success")
            }.addOnFailureListener {
                Log.w(TAG, "update completed from firesotore: fail", it)
            }
    }

    override suspend fun deleteTask(task: Task) {
        todoRef.document(task.id.toString())
            .delete()
            .addOnSuccessListener {
                Log.d(TAG, "delete task from firestore: success")
            }.addOnFailureListener {
                Log.w(TAG, "delete task from firestore: fail", it)
            }
    }

    override suspend fun getTaskById(taskId: Long): Task? {
        var task: Task? = null
        todoRef.document(taskId.toString()).get().addOnSuccessListener {
            val title = it.getString(FIELD_TITLE) ?: ""
            val memo = it.getString(FIELD_MEMO) ?: ""
            val isCompleted = it.getBoolean(FIELD_COMPLETED) ?: false
            task = Task(taskId, title, memo, isCompleted)
        }.addOnFailureListener {
            Log.w(TAG, "get task from firestore: fail", it)
        }.await()

        return task
    }

    override suspend fun deleteCompletedTask() {
        todoRef.whereEqualTo(FIELD_COMPLETED, true)
            .get()
            .addOnSuccessListener { querySnapshot ->
                db.runBatch {
                    querySnapshot.documents.forEach { doc ->
                        doc.reference.delete()
                    }
                }
            }.addOnFailureListener {
                Log.w(TAG, "delete completed task from firestore: fail", it)
            }
    }

    companion object {
        private val TAG = FireStoreDataSource::class.simpleName
        private const val USER_COLLECTION = "users"
        private const val TODO_COLLECTION = "todo"
    }
}