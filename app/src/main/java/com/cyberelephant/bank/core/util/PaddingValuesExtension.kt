package com.cyberelephant.bank.core.util

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp


fun PaddingValues.calculateVerticalPadding(): Dp = calculateTopPadding() + calculateBottomPadding()

@Composable
fun PaddingValues.calculateHorizontalPadding(): Dp =
    calculateRightPadding(LocalLayoutDirection.current) + calculateLeftPadding(LocalLayoutDirection.current)