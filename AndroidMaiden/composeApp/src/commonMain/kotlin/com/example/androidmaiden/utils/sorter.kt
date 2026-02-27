package com.example.androidmaiden.utils

import com.example.androidmaiden.model.FileSysNode
import com.example.androidmaiden.model.NodeType


//class sorter {
//}

enum class SortMode(val label: String) {
    NAME("名称"), DATE("日期"), SIZE("大小")
}
enum class SortOrder { ASC, DESC }

//Natural Order Comparator (Numeric Awareness)
fun naturalCompare(a: String, b: String): Int {
    var i = 0; var j = 0
    val na = a.length; val nb = b.length
    while (i < na && j < nb) {
        val ca = a[i]; val cb = b[j]
        if (ca.isDigit() && cb.isDigit()) {
            var ia = i; var ja = j
            while (ia < na && a[ia].isDigit()) ia++
            while (ja < nb && b[ja].isDigit()) ja++
            val sa = a.substring(i, ia)
            val sb = b.substring(j, ja)
            val da = sa.toLongOrNull() ?: 0L
            val db = sb.toLongOrNull() ?: 0L
            if (da != db) return (da - db).sign
            // When the numbers are equal, the shorter number string comes first (for example, you can choose whether "02" < "2").
            val lenDiff = sa.length - sb.length
            if (lenDiff != 0) return lenDiff
            i = ia; j = ja
        } else {
            if (ca != cb) return ca.lowercaseChar().compareTo(cb.lowercaseChar())
            i++; j++
        }
    }
    return (na - nb)
}

private val Long.sign: Int get() = when {
    this < 0L -> -1
    this > 0L -> 1
    else -> 0
}

data class SortConfig(
    val mode: SortMode,
    val order: SortOrder,
    val foldersFirst: Boolean = true
)


fun comparatorFor(config: SortConfig): Comparator<FileSysNode> {

    val folderFirst = Comparator<FileSysNode> { a, b ->
        if (!config.foldersFirst) 0
        else when {
            a.isFolder && b.isFile -> -1
            a.isFile && b.isFolder -> 1
            else -> 0
        }
    }

    val core = when (config.mode) {
        SortMode.NAME -> Comparator<FileSysNode> { a, b ->
            naturalCompare(a.name, b.name)
        }
        SortMode.DATE -> Comparator<FileSysNode> { a, b ->
            val ta = a.lastModified ?: Long.MIN_VALUE
            val tb = b.lastModified ?: Long.MIN_VALUE
            ta.compareTo(tb)
        }
        SortMode.SIZE -> Comparator<FileSysNode> { a, b ->
            val sa = a.size ?: Long.MIN_VALUE
            val sb = b.size ?: Long.MIN_VALUE
            sa.compareTo(sb)
        }
    }

    val asc = folderFirst.then(core)
    return when (config.order) {
        SortOrder.ASC -> asc
        SortOrder.DESC -> asc.reversed()
    }
}

fun FileSysNode.sortedChildren(
    mode: SortMode,
    order: SortOrder = SortOrder.ASC,
    foldersFirst: Boolean = true
): List<FileSysNode> {
    val comparator = when (mode) {
        SortMode.NAME -> compareBy<FileSysNode> { it.name.lowercase() }
        SortMode.DATE -> compareBy<FileSysNode> { it.lastModified ?: Long.MIN_VALUE }
        SortMode.SIZE -> compareBy<FileSysNode> { it.size ?: Long.MIN_VALUE }
    }

    val folderFirst = if (foldersFirst) {
        compareBy<FileSysNode> { it.nodeType == NodeType.FILE }
    } else null

    val finalComparator = if (folderFirst != null) folderFirst.then(comparator) else comparator

    return when (order) {
        SortOrder.ASC -> children.sortedWith(finalComparator)
        SortOrder.DESC -> children.sortedWith(finalComparator.reversed())
    }
}