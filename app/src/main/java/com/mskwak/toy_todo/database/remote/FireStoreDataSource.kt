package com.mskwak.toy_todo.database.remote

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.*
import com.mskwak.toy_todo.model.Task
import com.mskwak.toy_todo.model.Task.Companion.FIELD_COMPLETED
import com.mskwak.toy_todo.model.Task.Companion.FIELD_MEMO
import com.mskwak.toy_todo.model.Task.Companion.FIELD_TITLE
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class FireStoreDataSource(
    private val db: FirebaseFirestore,
    private val uid: String
) : RemoteDataSource {

    private val todoRef: CollectionReference
        get() = db.collection(USER_COLLECTION).document(uid)
            .collection(TODO_COLLECTION)

    //변경 사항에 대한 liveData
    private val _activeTasks = MutableLiveData<List<Task>>()
    private val _completedTasks = MutableLiveData<List<Task>>()

    //observeTaskById에 대한 LiveData
    private val _taskLiveData = MutableLiveData<Task>()

    //한개 task에 대한 변경 리스너
    //다른 task에 대하여 수신대기를 할 경우 기존 listener는 remove
    private var taskByIdListener: ListenerRegistration? = null

    init {
        initActiveTaskListener()
        initCompletedTaskListener()
    }

    private fun initActiveTaskListener() {
        todoRef.whereEqualTo(FIELD_COMPLETED, false)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.w(TAG, "active tasks listen fail", error)
                    return@addSnapshotListener
                }
                CoroutineScope(Dispatchers.IO).launch {
                    val list = snapshot?.documents?.map { documentToTask(it) } ?: return@launch
                    _activeTasks.postValue(list)
                }
            }
    }

    private fun initCompletedTaskListener() {
        todoRef.whereEqualTo(FIELD_COMPLETED, true)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.w(TAG, "completed tasks listen fail", error)
                    return@addSnapshotListener
                }
                CoroutineScope(Dispatchers.IO).launch {
                    val list = snapshot?.documents?.map { documentToTask(it) } ?: return@launch
                    _completedTasks.postValue(list)
                }
            }
    }

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

    override fun observeActiveTasks(): LiveData<List<Task>> = _activeTasks

    override fun observeCompletedTasks(): LiveData<List<Task>> = _completedTasks

    override fun observeTaskById(taskId: Long): LiveData<Task> {
        //이전 등록되어있던 리스너 삭제후 새 document에 대하여 설정
        taskByIdListener?.remove()
        taskByIdListener =
            todoRef.document(taskId.toString()).addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.w(TAG, "task listen fail", error)
                    return@addSnapshotListener
                }
                snapshot?.let {
                    _taskLiveData.value = documentToTask(it)
                }
            }

        return _taskLiveData
    }

    private fun documentToTask(document: DocumentSnapshot): Task {
        return document.data.let {
            val title = it?.get(FIELD_TITLE)?.toString() ?: ""
            val memo = it?.get(FIELD_MEMO)?.toString() ?: ""
            val completed = it?.get(FIELD_COMPLETED)?.toString()?.toBoolean() ?: false
            Task(document.id.toLong(), title, memo, completed)
        }
    }

    companion object {
        private val TAG = FireStoreDataSource::class.simpleName
        private const val USER_COLLECTION = "users"
        private const val TODO_COLLECTION = "todo"
    }
}