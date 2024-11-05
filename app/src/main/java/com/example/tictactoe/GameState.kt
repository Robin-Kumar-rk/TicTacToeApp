package com.example.tictactoe

fun findDefensiveMove(grid: List<String>, player: String): Int {

    for (i in grid.indices) {
        if (grid[i].isEmpty()) {

            val gridCopy = grid.toMutableList()
            gridCopy[i] = player

            if (checkGameState(gridCopy) == player) {
                return i
            }
        }
    }
    // No defensive move needed
    return -1
}

fun findWinningMove(grid: List<String>, computer: String): Int {

    for (i in grid.indices) {
        if (grid[i].isEmpty()) {
            val gridCopy = grid.toMutableList()
            gridCopy[i] = computer

            if (checkGameState(gridCopy) == computer) {
                return i
            }
        }
    }
    // No winning move found
    return -1
}

val winPatterns = arrayOf(
    arrayOf(0, 1, 2), arrayOf(3, 4, 5), arrayOf(6, 7, 8), arrayOf(0, 3, 6),
    arrayOf(1, 4, 7), arrayOf(2, 8, 5), arrayOf(0, 4, 8), arrayOf(2, 4, 6),
)

// Function to check the current game state
fun checkGameState(grid: List<String>): String? {

    for (pattern in winPatterns) {
        if (grid[pattern[0]] != "" &&
            grid[pattern[0]] == grid[pattern[1]] &&
            grid[pattern[0]] == grid[pattern[2]]) {
            return grid[pattern[0]]  // Return the winner ("X" or "O")
        }
    }

    return if (grid.none { it.isEmpty() }) "draw" else null
}

fun opening(lastMove: Int): Int {
    return when (lastMove) {
        4 -> arrayOf(0, 2, 6, 8).random()
        3 -> arrayOf(0, 6).random()
        1 -> arrayOf(0, 2).random()
        5 -> arrayOf(2, 8).random()
        7 -> arrayOf(6, 8).random()
        else -> 4
    }
}
