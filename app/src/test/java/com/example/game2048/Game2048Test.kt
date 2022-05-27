package com.example.game2048

import org.junit.Test

import org.junit.Assert.*
import kotlin.math.pow

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class Game2048Test {
    @Test
    fun isGameOver() {
        val field: Array<Array<Int>> = Array(4){Array(4) {0} }
        var k = 0
        for (i in 0..3){
            for (j in 0..3) {
                k++
                field[i][j] = 2.0.pow(k).toInt()
            }
        }
        val list1 = arrToList(field)
        assertEquals(true, isGameOver(list1))
        field[0][0] = 0
        val list2 = arrToList(field)
        assertEquals(false, isGameOver(list2))
    }

    @Test
    fun isMoveAvailable(){
        val field: Array<Array<Int>> = Array(4){Array(4) {0} }
        var k = 0
        for (i in 0..3){
            for (j in 0..3) {
                k++
                field[i][j] = 2.0.pow(k).toInt()
            }
        }
        field[0][0] = 0
        val list1 = arrToList(field)
        assertEquals(true, isMoveAvailable(list1, "Left"))
        assertEquals(false, isMoveAvailable(list1, "Right"))
    }

    @Test
    fun moveCells() {
        val field: Array<Array<Int>> = Array(4){Array(4) {0} }
        var k = 0
        for (i in 0..3){
            for (j in 0..3) {
                k++
                field[i][j] = 2.0.pow(k).toInt()
            }
        }
        field[0][3] = 0
        val expectedRes: Array<Array<Int>> = Array(4){Array(4) {0} }
        for (i in 1..3) {
            field[0][i-1] = expectedRes[0][i]
        }
        for (i in 1..3) {
            for (j in 0..3) {
                expectedRes[i][j] = field[i][j]
            }
        }
        assertArrayEquals(expectedRes, moveCells(field, "Left"))
    }
}