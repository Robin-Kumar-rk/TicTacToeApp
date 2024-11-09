package com.example.tictactoe


import kotlin.random.Random


fun computerResponse(
    board: List<String>, difficulty: String,
    player: String, totalFilledCell: Int,
    computer: String, lastMove: Int
): Int {
    return if (difficulty == "Easy") {
        easyResponse(board, totalFilledCell, player, computer)
    } else if (difficulty == "Hard") {
        normalResponse(board, player, totalFilledCell, computer, lastMove)
    } else {
        hardResponse(board, computer, player, totalFilledCell, lastMove)
    }
}

fun easyResponse(board: List<String>, totalFilledCell: Int, player: String, computer: String): Int {
    if (totalFilledCell == 9) return -1
    var move = findWinningMove(board, computer)
    if (move != -1) return move
    move = findDefensiveMove(board, player)
    if (move != -1) return move
    return randomResponse(board)
}

fun normalResponse(board: List<String>, player: String, totalFilledCell: Int, computer: String, lastMove: Int): Int {
    if (totalFilledCell == 9) return -1
    if (totalFilledCell == 1) return opening(lastMove)
    var move = findWinningMove(board, computer)
    if (move != -1) return move
    move = findDefensiveMove(board, player)
    if (move != -1) return move
    return randomResponse(board)
}

fun randomResponse(board: List<String>): Int {
    var idx = Random.nextInt(9)
    while (board[idx] != "") {
        idx = Random.nextInt(9)
    }
    return idx
}

data class Move(val score: Int, val index: Int)

fun hardResponse(board: List<String>, computer: String, player: String, totalFilledCell: Int, lastMove: Int ): Int {
    if (totalFilledCell == 1) {
        return opening(lastMove)
    }
    var x = findWinningMove(board, computer)
    if (x != -1) {
        return x
    }
    x = findDefensiveMove(board, player)
    if (x != -1) {
        return x
    }
    return minmax(board.toMutableList(), 0, computer, computer, player) as Int
}



fun minmax(grid: MutableList<String>, depth: Int, playTurn: String, computer: String, player: String): Any {
    val gameState = checkGameState(grid)

    // Return appropriate score based on game state
    if (gameState != null) {
        return when (gameState) {
            player -> depth - 100     // Player wins
            computer -> 100 - depth   // Computer wins
            "draw" -> -1              // Draw (use -1 to signal no valid move)
            else -> 0
        }
    }

    val moves = mutableListOf<Move>()

    for (i in grid.indices) {
        if (grid[i].isEmpty()) {
            grid[i] = playTurn               // Simulate the move
            val score = minmax(
                grid, depth + 1,
                if (playTurn == player) computer else player, computer, player
            ) as Int
            grid[i] = ""  // Undo the move
            moves.add(Move(score, i))
        }
    }

    return if (playTurn == computer) {
        // Maximize for the computer
        val maxMove = moves.maxByOrNull { it.score }!!
        if (depth == 0) maxMove.index else maxMove.score
    } else {
        // Minimize for the player
        val minMove = moves.minByOrNull { it.score }!!
        if (depth == 0) minMove.index else minMove.score
    }
}





