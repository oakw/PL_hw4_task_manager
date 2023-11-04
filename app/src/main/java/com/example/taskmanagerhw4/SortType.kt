package com.example.taskmanagerhw4

// Types one can sort tasks in
enum class SortType {
    DUE_DATE_ASC {
        override fun toString() = "By Due Date (ASC)"
    },
    DUE_DATE_DESC {
        override fun toString() = "By Due Date (DESC)"
    },
    TITLE_ASC {
        override fun toString() = "By Title (ASC)"
    },
    TITLE_DESC {
        override fun toString() = "By Title (DESC)"
    },
    PRIORITY_ASC {
        override fun toString() = "By Priority (ASC)"
    },
    PRIORITY_DESC {
        override fun toString() = "By Priority (DESC)"
    },
    STATUS {
        override fun toString() = "By Status"
    };
}