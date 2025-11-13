package com.example.androidmaiden.views.fileSys


//class sorter {
//}

enum class SortMode(val label: String) {
    NAME("名称"), DATE("日期"), SIZE("大小")
}
enum class SortOrder { ASC, DESC }

//自然序比较器（数字感知）
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
            // 数字相等时，较短数字串排前（"02" < "2" 可自行选择策略）
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

//统一排序器（文件夹优先、空值安全、升降序）
fun comparatorFor(config: SortConfig): Comparator<FileNode> {
    // 文件夹优先层
    val folderFirst = Comparator<FileNode> { a, b ->
        if (!config.foldersFirst) 0
        else when {
            a.isFolder && b.isFile -> -1
            a.isFile && b.isFolder -> 1
            else -> 0
        }
    }

    val core = when (config.mode) {
        SortMode.NAME -> Comparator<FileNode> { a, b ->
            naturalCompare(a.name, b.name)
        }
        SortMode.DATE -> Comparator<FileNode> { a, b ->
            val ta = a.lastModified ?: Long.MIN_VALUE
            val tb = b.lastModified ?: Long.MIN_VALUE
            ta.compareTo(tb)
        }
        SortMode.SIZE -> Comparator<FileNode> { a, b ->
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

fun FileNode.sortedChildren(
    mode: SortMode,
    order: SortOrder = SortOrder.ASC,
    foldersFirst: Boolean = true
): List<FileNode> {
    val comparator = when (mode) {
        SortMode.NAME -> compareBy<FileNode> { it.name.lowercase() }
        SortMode.DATE -> compareBy<FileNode> { it.lastModified ?: Long.MIN_VALUE }
        SortMode.SIZE -> compareBy<FileNode> { it.size ?: Long.MIN_VALUE }
    }

    val folderFirst = if (foldersFirst) {
        compareBy<FileNode> { it.nodeType == NodeType.FILE } // 文件夹优先
    } else null

    val finalComparator = if (folderFirst != null) folderFirst.then(comparator) else comparator

    return when (order) {
        SortOrder.ASC -> children.sortedWith(finalComparator)
        SortOrder.DESC -> children.sortedWith(finalComparator.reversed())
    }
}