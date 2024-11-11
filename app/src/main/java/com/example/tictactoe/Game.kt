package com.example.tictactoe

import android.widget.Button
import android.widget.Toast

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable

fun Game(modifier: Modifier) {
    val board = remember { mutableStateListOf("", "", "", "", "", "", "", "", "") }
    val player by remember { mutableStateOf("X") }
    val computer by remember { mutableStateOf("O") }
    var totalFilledCell by remember { mutableIntStateOf(0) }
    val moveNumberOnCell by remember { mutableStateOf(arrayOf(0, 0, 0, 0, 0, 0, 0, 0, 0)) }
    val moveOrder by remember { mutableStateOf(mutableListOf<Int>())}
    var expanded by remember { mutableStateOf(false) }
    var winner by remember { mutableStateOf("") }
    var difficulty by remember { mutableStateOf("Easy") }
    val context = LocalContext.current
    val modeButtonColor = when (difficulty) {
        "Easy" -> Color.Green
        "Hard" -> Color.Yellow
        "Impossible" -> Color.Red
        else -> Color.White
    }


    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .padding(16.dp)
                .border(
                    4.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(Color.Red, Color.Green, Color.Blue), // Gradient colors
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) // Matching rounded corners for border
                .padding(16.dp),

        ) {
            Text(
                text = "Tic Tac Toe",
                color = Color.White,
                fontSize = 40.sp,
                fontWeight = FontWeight.W400,
                modifier = Modifier.align(Alignment.Center),

            )
        }

        Spacer(modifier = Modifier.padding(8.dp))
        Row {
            Box {
                Button(
                    onClick = {
                        if (totalFilledCell != 0) {
                            Toast.makeText(context,"You can't change in middle game", Toast.LENGTH_SHORT).show()
                        } else {
                            expanded = true
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black,// Set the background color
                        contentColor = modeButtonColor
                    ),
                    border = BorderStroke(2.dp, modeButtonColor)
                ) {
                    Text(text = difficulty, fontSize = 24.sp)
                    Icon(Icons.Default.ArrowDropDown, contentDescription = "")
                }

                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false}) {
                    DropdownMenuItem(text = { Text(text = "Easy") }, onClick = {
                        difficulty = "Easy"
                        expanded = false
                    })
                    DropdownMenuItem(text = { Text(text = "Hard") }, onClick = {
                        difficulty = "Hard"
                        expanded = false
                    })
                    DropdownMenuItem(text = { Text(text = "Impossible") }, onClick = {
                        difficulty = "Impossible"
                        expanded = false
                    })
                }
            }
        }

        Spacer(modifier = Modifier.padding(16.dp))

        TicTacToeGrid(onClick = { index ->
            if (winner == "") {
                if (board[index].isEmpty()) {
                    board[index] = player
                    totalFilledCell++
                    moveNumberOnCell[index] = totalFilledCell
                    moveOrder.add(index)
                    var gameState = checkGameState(board)
                    if (gameState == player) {
                        winner = "You Win"
                    } else if (gameState == "draw") {
                        winner = "It's a draw"
                    } else {
                        val computerResponse = computerResponse(
                            board,
                            difficulty,
                            player,
                            totalFilledCell,
                            computer,
                            index
                        )
                        if (computerResponse == -1) {
                            winner = "It's a draw"
                        } else if (computerResponse < -1) {
                            winner = "You Win"
                        } else if (computerResponse > 8) {
                            winner = "Computer Wins"
                        } else {
                            board[computerResponse] = computer
                            totalFilledCell++
                            moveNumberOnCell[computerResponse] = totalFilledCell
                            moveOrder.add(computerResponse)
                            gameState = checkGameState(board)
                            if (gameState == computer) {
                                winner = "Computer Wins"
                            } else if (gameState == "draw") {
                                winner = "It's a draw"
                            }
                        }
                    }
                }
            }

        }, board, moveNumberOnCell)


        Spacer(modifier = Modifier.padding(16.dp))

        if (winner != "") {
            if (winner == "You Win") {
                Box(
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .background(
                            color = Color.Black, // Bright gold background for excitement
                            shape = RoundedCornerShape(20.dp) // Rounded corners
                        )
                        .border(
                            4.dp,
                            Color.Green,
                            shape = RoundedCornerShape(20.dp)
                        )
                        .padding(8.dp)
                ) {
                    Text(
                        text = winner,
                        fontSize = 32.sp, // Larger font size for emphasis
                        fontWeight = FontWeight.Bold, // Bold text
                        color = Color(0xFF8AF360), // Red text color for excitement ,
                        modifier = Modifier.align(Alignment.Center) // Center the text
                    )
                }

            } else {
                Text(
                    text = winner,
                    fontSize = 32.sp,
                    color = Color.Red,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
        Spacer(modifier = Modifier.padding(16.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Button (
                onClick = {
                    if (!moveOrder.isEmpty()) {
                        val lastMove = moveOrder.removeAt(moveOrder.lastIndex)
                        board[lastMove] = ""
                        moveNumberOnCell[lastMove] = 0
                        totalFilledCell -= 1
                        if (moveOrder.size % 2 != 0) {
                            val secondLastMove = moveOrder.removeAt(moveOrder.lastIndex)
                            board[secondLastMove] = ""
                            moveNumberOnCell[secondLastMove] = 0
                            totalFilledCell -= 1
                        }
                    }
                    winner = ""
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black, // Set the background color
                    contentColor = Color(0xFF18FAFA)
                ),
                border = BorderStroke(4.dp, Color(0xFF36F8FF))
            ) {
                Text(text = "Back", fontSize = 24.sp)
            }
            Button(
                onClick = {
                    for (i in board.indices) {
                        board[i] = ""
                        moveNumberOnCell[i] = 0
                    }
                    winner = ""
                    totalFilledCell = 0
                    moveOrder.clear()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black, // Set the background color
                    contentColor = Color(0xFF18FAFA)
                ),
                border = BorderStroke(4.dp, Color(0xFF36F8FF))
            ) {
                Text(text = "Reset", fontSize = 24.sp)
            }
        }
    }

}


@Composable
fun TicTacToeGrid(onClick: (Int) -> Unit, board: List<String>, moveNumberOnCell: Array<Int>) {
    Column {
        for (row in 0..2) {
            Row {
                for (col in 0..2) {
                    TicTacToeCell(
                        index = row * 3 + col,
                        onClick = onClick,
                        board[row * 3 + col],
                        moveNumberOnCell
                    )
                }
            }
        }
    }
}

@Composable
fun TicTacToeCell(index: Int, onClick: (Int) -> Unit, player: String, moveNumberOnCell: Array<Int>) {
    Box(
        modifier = Modifier
            .size(80.dp)
            .padding(4.dp)
            .border(4.dp, Color.Green, RoundedCornerShape(16.dp)) // Rounded corners with border
            .clickable { onClick(index) } // Clickable behavior
            .background(Color.Transparent) // Transparent background
    ) {
        // Display move number at top-left corner if available
        Text(
            text = if (moveNumberOnCell[index] == 0) "" else moveNumberOnCell[index].toString(),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(4.dp)
        )


        // Display player symbol (X or O) in the center
        Text(
            text = player,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold, // Bold text for emphasis
            color = if (player == "X") Color.Green else Color.Red,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}









