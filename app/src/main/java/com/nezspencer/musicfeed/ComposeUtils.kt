package com.nezspencer.musicfeed

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Composable
fun TitleText(
    modifier: Modifier = Modifier,
    text: String,
    color: Color = Color.White,
    style: TextStyle = MaterialTheme.typography.h4,
    lineHeight: TextUnit = 41.sp
) {
    Text(
        text = text,
        style = style,
        color = color,
        lineHeight = lineHeight,
        modifier = modifier
    )
}

@Composable
fun BodyText(
    modifier: Modifier = Modifier,
    text: String,
    color: Color = Color.White,
    style: TextStyle = MaterialTheme.typography.body1,
    lineHeight: TextUnit = 21.sp
) {
    Text(
        text = text,
        style = style,
        color = color,
        lineHeight = lineHeight,
        modifier = modifier
    )
}

@Composable
fun CaptionText(
    modifier: Modifier = Modifier,
    text: String,
    color: Color = Color.White,
    style: TextStyle = MaterialTheme.typography.caption,
    lineHeight: TextUnit = 16.sp
) {
    Text(
        text = text,
        style = style,
        color = color,
        lineHeight = lineHeight,
        modifier = modifier
    )
}