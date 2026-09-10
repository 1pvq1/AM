package com.example.androidmaiden.data.repository

import com.example.androidmaiden.data.local.TodoDao
import com.example.androidmaiden.data.local.TodoEntity
import com.example.androidmaiden.domain.model.TodoItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TodoRepository(private val todoDao: TodoDao) {
    fun getAllTodos(): Flow<List<TodoItem>> {
        return todoDao.getAllTodos().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    suspend fun getTodoById(id: Long): TodoItem? {
        return todoDao.getTodoById(id)?.toDomain()
    }

    suspend fun insertTodo(item: TodoItem): Long {
        return todoDao.insertTodo(TodoEntity.fromDomain(item))
    }

    suspend fun updateTodo(item: TodoItem) {
        todoDao.updateTodo(TodoEntity.fromDomain(item))
    }

    suspend fun deleteTodo(item: TodoItem) {
        todoDao.deleteTodo(TodoEntity.fromDomain(item))
    }

    suspend fun deleteTodoById(id: Long) {
        todoDao.deleteTodoById(id)
    }
}
