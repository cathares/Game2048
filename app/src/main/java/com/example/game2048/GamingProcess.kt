package com.example.game2048

import kotlin.random.Random
import kotlin.random.nextInt

fun gameStart(): List<List<Int>> {
    val field = Array(4) { Array(4) { 0 } }
    val start: Pair<Int, Int> = Random.nextInt(0, 3) to Random.nextInt(0, 3)
    field[start.first][start.second] = 2
    return arrToList(field)
}

fun moveCells(field: Array<Array<Int>>, direction: String): Array<Array<Int>> {
    when (direction) {
        "Right" -> {
            while (true) {
                for (i in 0..3) {
                    for (j in 0..2) {
                        if (field[i][j + 1] == 0) {
                            field[i][j + 1] = field[i][j]
                            field[i][j] = 0
                        }
                    }
                }
                var c = 0
                for (i in 0..3) {
                    for (j in 0..2) {
                        if (field[i][j] != 0 && field[i][j + 1] == 0) break
                        c++
                    }
                }
                if (c == 12)
                    break
            }
        }

        "Left" -> {
            while (true) {
                for (i in 0..3) {
                    for (j in 3 downTo 1) {
                        if (field[i][j - 1] == 0) {
                            field[i][j - 1] = field[i][j]
                            field[i][j] = 0
                        }
                    }
                }
                var c = 0
                for (i in 0..3) {
                    for (j in 3 downTo 1) {
                        if (field[i][j] != 0 && field[i][j - 1] == 0) break
                        c++
                    }
                }
                if (c == 12)
                    break
            }
        }
        "Up" -> {
            while (true) {
                for (i in 3 downTo 1) {
                    for (j in 0..3) {
                        if (field[i - 1][j] == 0) {
                            field[i - 1][j] = field[i][j]
                            field[i][j] = 0
                        }
                    }
                }
                var c = 0
                for (i in 3 downTo 1) {
                    for (j in 0..3) {
                        if (field[i][j] != 0 && field[i - 1][j] == 0) break
                        c++
                    }
                }
                if (c == 12)
                    break
            }
        }
        "Down" -> {
            while (true) {
                for (i in 0..2) {
                    for (j in 0..3) {
                        if (field[i + 1][j] == 0) {
                            field[i + 1][j] = field[i][j]
                            field[i][j] = 0
                        }
                    }
                }
                var c = 0
                for (i in 0..2) {
                    for (j in 0..3) {
                        if (field[i][j] != 0 && field[i + 1][j] == 0) break
                        c++
                    }
                }
                if (c == 12)
                    break
            }
        }
    }
    return field
}


fun changeCells(field: Array<Array<Int>>, direction: String): Pair<Array<Array<Int>>, Int> {
    var changedCell = Pair(-1, -1)
    var score = 0
    when (direction) {
        "Right" -> {
            for (i in 0..3) {
                for (j in 3 downTo 1) {
                    if (field[i][j] == field[i][j - 1] && Pair(i, j) != changedCell) {
                        field[i][j] = field[i][j] * 2
                        field[i][j - 1] = 0
                        score += field[i][j]
                        changedCell = Pair(i, j)
                    }
                }
            }
        }
        "Left" -> {
            for (i in 0..3) {
                for (j in 0..2) {
                    if (field[i][j] == field[i][j + 1] && Pair(i, j) != changedCell) {
                        field[i][j] = field[i][j] * 2
                        field[i][j + 1] = 0
                        score += field[i][j]
                        changedCell = Pair(i, j)
                    }
                }
            }
        }
        "Up" -> {
            for (i in 0 .. 2) {
                for (j in 0..3) {
                    if (field[i][j] == field[i + 1][j] && Pair(i, j) != changedCell) {
                        field[i][j] = field[i][j] * 2
                        field[i + 1][j] = 0
                        score += field[i][j]
                        changedCell = Pair(i, j)
                    }
                }
            }
        }
        "Down" -> {
            for (i in 3 downTo 1) {
                for (j in 0..3) {
                    if (field[i][j] == field[i - 1][j] && Pair(i, j) != changedCell) {
                        field[i][j] = field[i][j] * 2
                        field[i - 1][j] = 0
                        score += field[i][j]
                        changedCell = Pair(i, j)
                    }
                }
            }
        }
    }
    return Pair(field, score)
}


fun makeTurn(currPos: List<List<Int>>, direction: String): Pair<List<List<Int>>, Int> {
    val emptyCells: MutableList<Pair<Int,Int>> = mutableListOf()
    var field: Array<Array<Int>> = Array(4) {Array(4) {0} }
    for (i in 0..3) {
        for (j in 0..3) {
            field[i][j] = currPos[i][j]
        }
    }
    field = moveCells(field, direction)
    val fieldAndScore = changeCells(field, direction)
    field = moveCells(fieldAndScore.first, direction)

    for (i in 0..3) {
        for (j in 0..3) {
            if (field[i][j] == 0) {
                emptyCells.add(Pair(i,j))
            }
        }
    }
    if (emptyCells.isNotEmpty()) {
        val posToAdd = Random.nextInt(0 until emptyCells.size)
        field[emptyCells[posToAdd].first][emptyCells[posToAdd].second] = 2
    }

    val res  = arrToList(field)
    return Pair(res, fieldAndScore.second)
}

fun isGameOver(currPos: List<List<Int>>): Boolean {
    val dirArr: Array<String> = arrayOf("Left", "Right", "Up", "Down")
    var flag = false
    for (dir in dirArr) {
        val changedField = makeTurn(currPos, dir).first
        for (i in 0..3) {
            for (j in 0..3) {
                if (changedField[i][j] != currPos[i][j]) {
                    flag = true
                    break
                }
            }
        }
        if (flag) return false
    }
    return true
}

fun isMoveAvailable(currPos: List<List<Int>>, dir: String): Boolean {
    var field: Array<Array<Int>> = Array(4) {Array(4) {0} }
    for (i in 0..3) {
        for (j in 0..3) {
            field[i][j] = currPos[i][j]
        }
    }
    field = moveCells(field, dir)
    val fieldAndScore = changeCells(field, dir)
    field = moveCells(fieldAndScore.first, dir)
    for (i in 0..3) {
        for (j in 0..3) {
            if (field[i][j] != currPos[i][j]) {
                return true
            }
        }
    }
    return false
}

fun arrToList(field:Array<Array<Int>>): List<List<Int>> =
        listOf(
        listOf(field[0][0], field[0][1], field[0][2], field[0][3]),
        listOf(field[1][0], field[1][1], field[1][2], field[1][3]),
        listOf(field[2][0], field[2][1], field[2][2], field[2][3]),
        listOf(field[3][0], field[3][1], field[3][2], field[3][3])
            )
