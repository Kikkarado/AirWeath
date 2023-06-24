package ua.airweath.utils.animation.navanimation

import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.ui.unit.IntOffset

fun enterTransitionHorizontally(initialOffsetX: Int, animationSpec: FiniteAnimationSpec<IntOffset>) =
    slideInHorizontally(initialOffsetX = { initialOffsetX }, animationSpec = animationSpec) + fadeIn()
fun enterTransitionHorizontally(initialOffsetX: Int, durationMillis: Int) =
    slideInHorizontally(initialOffsetX = { initialOffsetX }, animationSpec = tween(durationMillis)) + fadeIn()

fun exitTransitionHorizontally(initialOffsetX: Int, animationSpec: FiniteAnimationSpec<IntOffset>) =
    slideOutHorizontally(targetOffsetX = { initialOffsetX }, animationSpec = animationSpec) + fadeOut()
fun exitTransitionHorizontally(initialOffsetX: Int, durationMillis: Int) =
    slideOutHorizontally(targetOffsetX = { initialOffsetX }, animationSpec = tween(durationMillis)) + fadeOut()

fun popEnterTransitionHorizontally(initialOffsetX: Int, animationSpec: FiniteAnimationSpec<IntOffset>) =
    slideInHorizontally(initialOffsetX = { initialOffsetX }, animationSpec = animationSpec) + fadeIn()
fun popEnterTransitionHorizontally(initialOffsetX: Int, durationMillis: Int) =
    slideInHorizontally(initialOffsetX = { initialOffsetX }, animationSpec = tween(durationMillis)) + fadeIn()

fun popExitTransitionHorizontally(initialOffsetX: Int, animationSpec: FiniteAnimationSpec<IntOffset>) =
    slideOutHorizontally(targetOffsetX = { initialOffsetX }, animationSpec = animationSpec) + fadeOut()
fun popExitTransitionHorizontally(initialOffsetX: Int, durationMillis: Int) =
    slideOutHorizontally(targetOffsetX = { initialOffsetX }, animationSpec = tween(durationMillis)) + fadeOut()