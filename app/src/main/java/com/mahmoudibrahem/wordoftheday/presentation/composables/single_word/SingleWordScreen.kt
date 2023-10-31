package com.mahmoudibrahem.wordoftheday.presentation.composables.single_word

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.mahmoudibrahem.wordoftheday.R
import com.mahmoudibrahem.wordoftheday.core.util.ifNotEmpty
import com.mahmoudibrahem.wordoftheday.core.util.shadow
import com.mahmoudibrahem.wordoftheday.domain.model.Meaning
import com.mahmoudibrahem.wordoftheday.domain.model.Word
import com.mahmoudibrahem.wordoftheday.presentation.ui.theme.appFont

@Composable
fun SingleWordScreen(
    word: String = "",
    context: Context = LocalContext.current,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    viewModel: SingleWordViewModel = hiltViewModel(),
    onNavigateUp: () -> Unit = {}
) {
    val uiState = viewModel.uiState.collectAsState().value
    SingleWordScreenContent(
        uiState = uiState,
        onBackClicked = onNavigateUp,
        onChangeModeClicked = viewModel::onChangeModeClicked,
        onAudioBtnClicked = viewModel::onAudioButtonClicked,
        onShareClicked = viewModel::onShareClicked,
        onCopyClicked = { wordToCopy -> viewModel.onCopyClicked(context, wordToCopy) },
    )
    LaunchedEffect(key1 = uiState.screenMsg) {
        uiState.screenMsg.ifNotEmpty {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }
    DisposableEffect(key1 = lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.getWordDetails(word)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}

@Composable
private fun SingleWordScreenContent(
    uiState: SingleWordScreenUIState,
    onBackClicked: () -> Unit,
    onChangeModeClicked: () -> Unit,
    onAudioBtnClicked: (Word) -> Unit,
    onCopyClicked: (String) -> Unit,
    onShareClicked: (Word) -> Unit
) {
    val loadingComposition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.loader))
    val errorComposition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.error))
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(horizontal = 12.dp, vertical = 24.dp)
            .statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ScreenHeader(
            onBackClicked = onBackClicked,
            onChangeModeClicked = onChangeModeClicked,
            isShowImage = !uiState.isLoading && !uiState.isError,
            isDarkMode = uiState.isDarkMode
        )
        AnimatedVisibility(
            visible = uiState.isLoading,
            enter = scaleIn(animationSpec = tween(durationMillis = 500)) + fadeIn(
                animationSpec = tween(
                    durationMillis = 500
                )
            ),
            exit = scaleOut(animationSpec = tween(durationMillis = 500)) + fadeOut(
                animationSpec = tween(
                    durationMillis = 500
                )
            ),
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                LottieAnimation(
                    modifier = Modifier.size(200.dp),
                    composition = loadingComposition,
                    iterations = LottieConstants.IterateForever,
                )
            }
        }
        AnimatedVisibility(
            visible = uiState.isError && uiState.word==null,
            enter = scaleIn(animationSpec = tween(delayMillis = 500)) + fadeIn(
                animationSpec = tween(
                    delayMillis = 500
                )
            ),
            exit = scaleOut(animationSpec = tween(delayMillis = 500)) + fadeOut(
                animationSpec = tween(
                    delayMillis = 500
                )
            )
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LottieAnimation(
                        modifier = Modifier.fillMaxWidth(),
                        composition = errorComposition,
                        iterations = LottieConstants.IterateForever,
                    )
                    Text(
                        modifier = Modifier.padding(top = 8.dp),
                        text = uiState.errorMsg,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = appFont,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
        AnimatedVisibility(
            visible = !uiState.isLoading && !uiState.isError,
            enter = scaleIn(animationSpec = tween(delayMillis = 500)) + fadeIn(
                animationSpec = tween(
                    delayMillis = 500
                )
            ),
            exit = scaleOut(animationSpec = tween(delayMillis = 500)) + fadeOut(
                animationSpec = tween(
                    delayMillis = 500
                )
            )
        ) {
            uiState.word?.let { word ->
                LazyColumn {
                    item {
                        WordHeader(
                            word = word,
                            onAudioBtnClicked = onAudioBtnClicked,
                            onCopyClicked = onCopyClicked,
                            onShareClicked = onShareClicked
                        )
                    }
                    item {
                        WordBody(meanings = word.meanings)
                    }
                }
            }
        }
    }
}

@Composable
private fun ScreenHeader(
    onBackClicked: () -> Unit,
    onChangeModeClicked: () -> Unit,
    isShowImage: Boolean,
    isDarkMode: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        IconButton(
            onClick = onBackClicked
        ) {
            Icon(
                painter = painterResource(id = R.drawable.back_ic),
                contentDescription = stringResource(R.string.back_button),
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
        AnimatedVisibility(
            visible = isShowImage,
            enter = scaleIn(animationSpec = tween(delayMillis = 500)) + fadeIn(
                animationSpec = tween(
                    delayMillis = 500
                )
            ),
            exit = scaleOut(animationSpec = tween(delayMillis = 500)) + fadeOut(
                animationSpec = tween(
                    delayMillis = 500
                )
            )
        ) {
            Image(
                modifier = Modifier.size(200.dp),
                painter = painterResource(id = R.drawable.single_word_person),
                contentDescription = stringResource(R.string.person)
            )
        }
        IconButton(
            modifier = Modifier
                .size(36.dp)
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(8.dp),
            onClick = onChangeModeClicked
        ) {
            AnimatedVisibility(
                visible = isDarkMode,
                enter = scaleIn(animationSpec = tween(durationMillis = 500, delayMillis = 500)),
                exit = scaleOut(animationSpec = tween(durationMillis = 500, delayMillis = 500))
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.moon_ic),
                    contentDescription = stringResource(R.string.change_mode),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
            AnimatedVisibility(
                visible = !isDarkMode,
                enter = scaleIn(animationSpec = tween(durationMillis = 500, delayMillis = 500)),
                exit = scaleOut(animationSpec = tween(durationMillis = 500, delayMillis = 500))
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.sun_ic),
                    contentDescription = stringResource(R.string.change_mode),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }

    }
}

@Composable
private fun WordHeader(
    word: Word,
    onAudioBtnClicked: (Word) -> Unit,
    onCopyClicked: (String) -> Unit,
    onShareClicked: (Word) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.Transparent, shape = RoundedCornerShape(8.dp)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(color = MaterialTheme.colorScheme.outlineVariant, blurRadius = 20.dp)
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(12.dp)

        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = word.word,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = appFont,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier
                            .size(width = 24.dp, height = 24.dp)
                            .background(
                                color = if (word.checkAudioAvailability()) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(4.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                onAudioBtnClicked(word)
                            },
                        painter = painterResource(id = R.drawable.sound_ic),
                        contentDescription = stringResource(R.string.listen),
                        tint = if (word.checkAudioAvailability()) MaterialTheme.colorScheme.onPrimary else Color.LightGray
                    )

                    IconButton(
                        onClick = {
                            onCopyClicked(word.word)
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.copy_ic),
                            contentDescription = stringResource(R.string.copy),
                            tint = Color.Unspecified
                        )
                    }

                    IconButton(
                        onClick = {
                            onShareClicked(word)
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.share_ic),
                            contentDescription = stringResource(R.string.copy),
                            tint = Color.Unspecified
                        )
                    }
                }
            }
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = word.getWordPartOfSpeech(),
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontFamily = appFont
            )
        }
        val canvasShapeColor = MaterialTheme.colorScheme.surface
        Canvas(
            modifier = Modifier
                .size(width = 45.dp, height = 40.dp)
                .background(color = Color.Transparent)
                .shadow(
                    color = MaterialTheme.colorScheme.outlineVariant,
                    blurRadius = 24.dp,
                    offsetY = 8.dp
                )
        ) {

            val path = Path()
            path.moveTo(0f, y = 0f)
            path.quadraticBezierTo(
                x1 = this.size.width / 2,
                y1 = this.size.height,
                x2 = this.size.width,
                y2 = 0f
            )
            drawPath(path = path, color = canvasShapeColor)
        }
    }

}

@Composable
private fun WordBody(
    meanings: List<Meaning>
) {
    meanings.forEach { meaning ->
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(color = MaterialTheme.colorScheme.outlineVariant, blurRadius = 24.dp),
            color = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(8.dp)
        ) {
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Text(
                    modifier = Modifier.padding(bottom = 8.dp),
                    text = "${meaning.partOfSpeech} (${meaning.definitions.size} Definitions)",
                    fontSize = 14.sp,
                    fontFamily = appFont,
                    fontStyle = FontStyle.Italic,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(
                    modifier = Modifier
                        .size(width = 150.dp, height = 1.dp)
                        .background(color = MaterialTheme.colorScheme.primary)
                )
                meaning.definitions.forEachIndexed { index, definition ->
                    Text(
                        modifier = Modifier.padding(top = 12.dp),
                        text = "(${index + 1})",
                        fontSize = 14.sp,
                        fontFamily = appFont,
                        fontStyle = FontStyle.Italic,
                        color = MaterialTheme.colorScheme.outline
                    )
                    Text(
                        modifier = Modifier.padding(top = 8.dp),
                        text = stringResource(R.string.definition),
                        fontSize = 14.sp,
                        fontFamily = appFont,
                        fontStyle = FontStyle.Italic,
                        color = MaterialTheme.colorScheme.outline
                    )
                    Text(
                        modifier = Modifier.padding(top = 8.dp),
                        text = definition.definition,
                        fontSize = 12.sp,
                        fontFamily = appFont
                    )
                    definition.example?.let { example ->
                        Text(
                            modifier = Modifier.padding(top = 8.dp),
                            text = stringResource(R.string.example),
                            fontSize = 14.sp,
                            fontFamily = appFont,
                            fontStyle = FontStyle.Italic,
                            color = MaterialTheme.colorScheme.outline
                        )
                        Text(
                            modifier = Modifier.padding(top = 8.dp),
                            text = example,
                            fontSize = 12.sp,
                            fontFamily = appFont
                        )
                    }
                    if (definition.getAntonyms().isNotEmpty()) {
                        Text(
                            text = buildAnnotatedString {
                                withStyle(
                                    SpanStyle(
                                        fontSize = 14.sp,
                                        fontFamily = appFont,
                                        fontStyle = FontStyle.Italic,
                                        color = MaterialTheme.colorScheme.outline
                                    )
                                ) {
                                    append(stringResource(R.string.synonyms))
                                }
                                withStyle(
                                    SpanStyle(
                                        fontSize = 12.sp,
                                        fontFamily = appFont
                                    )
                                ) {
                                    append(definition.getSynonyms())
                                }
                            },
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }

                    if (definition.getAntonyms().isNotEmpty()) {
                        Text(
                            text = buildAnnotatedString {
                                withStyle(
                                    SpanStyle(
                                        fontSize = 14.sp,
                                        fontFamily = appFont,
                                        fontStyle = FontStyle.Italic,
                                        color = MaterialTheme.colorScheme.outline
                                    )
                                ) {
                                    append(stringResource(R.string.antonyms))
                                }
                                withStyle(
                                    SpanStyle(
                                        fontSize = 12.sp,
                                        fontFamily = appFont
                                    )
                                ) {
                                    append(definition.getAntonyms())
                                }
                            },
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                    if (index != meaning.definitions.size - 1) {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Divider(
                                modifier = Modifier
                                    .fillMaxWidth(0.9f)
                                    .padding(top = 12.dp)
                                    .height(1.dp)
                                    .background(color = MaterialTheme.colorScheme.onSurfaceVariant)

                            )
                        }
                    }

                }

                Spacer(modifier = Modifier.height(12.dp))
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Preview(showSystemUi = true)
@Composable
private fun SingleWordScreenPreview() {
    SingleWordScreen()
}

