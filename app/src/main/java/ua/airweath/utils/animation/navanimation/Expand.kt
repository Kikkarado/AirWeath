package ua.airweath.utils.animation.navanimation

import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkOut
import androidx.compose.ui.unit.IntSize

fun enterTransitionExpand(animationSpec: FiniteAnimationSpec<IntSize>) =
    expandIn(animationSpec = animationSpec) + fadeIn()
fun enterTransitionExpand(durationMillis: Int) =
    expandIn(animationSpec = tween(durationMillis)) + fadeIn()

fun exitTransitionExpand(animationSpec: FiniteAnimationSpec<IntSize>) =
    shrinkOut(animationSpec = animationSpec) + fadeOut()
fun exitTransitionExpand(durationMillis: Int) =
    shrinkOut(animationSpec = tween(durationMillis)) + fadeOut()

fun popEnterTransitionExpand(animationSpec: FiniteAnimationSpec<IntSize>) =
    expandIn(animationSpec = animationSpec) + fadeIn()
fun popEnterTransitionExpand(durationMillis: Int) =
    expandIn(animationSpec = tween(durationMillis)) + fadeIn()

fun popExitTransitionExpand(animationSpec: FiniteAnimationSpec<IntSize>) =
    shrinkOut(animationSpec = animationSpec) + fadeOut()
fun popExitTransitionExpand(durationMillis: Int) =
    shrinkOut(animationSpec = tween(durationMillis)) + fadeOut()