package com.mahmoudibrahem.wordoftheday.presentation.composables.home

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
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
import com.mahmoudibrahem.wordoftheday.core.util.shadow
import com.mahmoudibrahem.wordoftheday.domain.model.Meaning
import com.mahmoudibrahem.wordoftheday.domain.model.Suggestion
import com.mahmoudibrahem.wordoftheday.domain.model.Word
import com.mahmoudibrahem.wordoftheday.presentation.ui.theme.appFont
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    context: Context = LocalContext.current,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    onNavigateToSingleWord: (String) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    HomeScreenContent(
        uiState = uiState,
        onSearchQueryChanged = viewModel::onSearchQueryChanged,
        onSearchFocusChanged = viewModel::onSearchFocusChanged,
        onGetRandomWordBtnClicked = viewModel::getRandomWordForRandomSection,
        onChangeModeBtnClicked = viewModel::onchangeModeClicked,
        onAudioBtnClicked = viewModel::onAudioButtonClicked,
        onReadMoreClicked = { onNavigateToSingleWord(it) },
        onSearchResultClicked = { onNavigateToSingleWord(it) }
    )
    LaunchedEffect(key1 = uiState.screenMsg) {
        if (uiState.screenMsg.isNotEmpty() && uiState.screenMsg.trim() != "null") {
            Toast.makeText(context, uiState.screenMsg, Toast.LENGTH_SHORT).show()
        }
    }
    DisposableEffect(key1 = lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.onScreenResumed()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun HomeScreenContent(
    uiState: HomeScreenUIState,
    onSearchQueryChanged: (String) -> Unit,
    onSearchFocusChanged: (Boolean) -> Unit,
    onGetRandomWordBtnClicked: () -> Unit,
    onAudioBtnClicked: (Word) -> Unit,
    onReadMoreClicked: (String) -> Unit,
    onSearchResultClicked: (String) -> Unit,
    onChangeModeBtnClicked: () -> Unit,
) {
    val pagerState = rememberPagerState { 2 }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .displayCutoutPadding()
            .navigationBarsPadding()
            .padding(
                horizontal = 12.dp,
                vertical = if (Build.VERSION.SDK_INT >= 33) 48.dp else 24.dp
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                focusManager.clearFocus()
            },
    ) {
        Header(
            onChangeModeBtnClicked = onChangeModeBtnClicked,
            isDarkMode = uiState.isDarkMode,
            focusManager = focusManager
        )
        Spacer(modifier = Modifier.height(12.dp))
        SearchWidget(
            modifier = Modifier.padding(horizontal = 12.dp),
            searchQuery = uiState.searchQuery,
            isLoading = uiState.isSearchLoading,
            results = uiState.searchResults,
            onSearchQueryChanged = onSearchQueryChanged,
            onSearchFocusChanged = onSearchFocusChanged,
            onResultClicked = onSearchResultClicked,
            focusRequester = focusRequester
        )
        Spacer(modifier = Modifier.height(12.dp))
        HomeTwoSections(
            modifier = Modifier.padding(24.dp),
            pagerState = pagerState,
            focusManager = focusManager
        )
        Spacer(modifier = Modifier.height(12.dp))
        HomePager(
            todayWord = uiState.todayWord,
            yesterdayWord = uiState.yesterdayWord,
            randomWord = uiState.randomWord,
            pagerState = pagerState,
            focusManager = focusManager,
            isRandomBtnLoading = uiState.isGetRandomWordBtnLoading,
            onGetRandomWordBtnClicked = onGetRandomWordBtnClicked,
            onAudioBtnClicked = onAudioBtnClicked,
            onReadMoreClicked = onReadMoreClicked
        )

    }
}


@Composable
private fun Header(
    onChangeModeBtnClicked: () -> Unit = {},
    isDarkMode: Boolean,
    focusManager: FocusManager
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                focusManager.clearFocus()
            },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        fontFamily = appFont,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Normal,
                        letterSpacing = 0.5.sp
                    )
                ) {
                    append(stringResource(R.string.learn_something))
                }
                withStyle(
                    style = SpanStyle(
                        fontFamily = appFont,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 22.sp,
                        letterSpacing = 0.5.sp
                    )
                ) {
                    append(stringResource(R.string._new))
                }
            },
            lineHeight = TextUnit(32f, TextUnitType.Sp)
        )

        IconButton(
            modifier = Modifier
                .size(36.dp)
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(8.dp),
            onClick = onChangeModeBtnClicked
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchWidget(
    modifier: Modifier = Modifier,
    searchQuery: String,
    isLoading: Boolean,
    results: List<Suggestion> = emptyList(),
    onSearchQueryChanged: (String) -> Unit = {},
    onSearchFocusChanged: (Boolean) -> Unit,
    onResultClicked: (String) -> Unit,
    focusRequester: FocusRequester
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.laoding_anim))
    Surface(
        modifier = modifier
            .shadow(color = MaterialTheme.colorScheme.outlineVariant, blurRadius = 20.dp)
            .animateContentSize()
            .onFocusChanged {
                onSearchFocusChanged(it.isFocused)
            },
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surface,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .background(
                        color = Color.Transparent,
                        shape = if (results.isEmpty()) RoundedCornerShape(8.dp) else RoundedCornerShape(
                            topEnd = 8.dp,
                            topStart = 8.dp
                        )
                    )
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.search_ic),
                    contentDescription = stringResource(R.string.search),
                    tint = Color.Unspecified
                )
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 4.dp)
                        .focusRequester(focusRequester),
                    value = searchQuery,
                    onValueChange = onSearchQueryChanged,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        unfocusedBorderColor = Color.Transparent,
                        focusedBorderColor = Color.Transparent
                    ),
                    placeholder = {
                        Text(
                            text = stringResource(R.string.search_placeholder),
                            color = MaterialTheme.colorScheme.outline,
                            fontSize = 16.sp
                        )
                    },
                    textStyle = MaterialTheme.typography.labelMedium,
                    singleLine = true
                )
            }

            if (isLoading) {
                LottieAnimation(
                    modifier = Modifier
                        .size(56.dp)
                        .fillMaxWidth()
                        .background(color = Color.Transparent),
                    composition = composition,
                    iterations = LottieConstants.IterateForever,
                )
            }
            if (!isLoading) {
                LazyColumn(
                    modifier = Modifier
                        .background(
                            color = Color.Transparent,
                            shape = RoundedCornerShape(bottomEnd = 8.dp, bottomStart = 8.dp)
                        )
                        .padding(horizontal = 12.dp)
                ) {
                    items(results) { result ->
                        SearchResultItem(
                            item = result,
                            onResultClicked = onResultClicked
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchResultItem(
    item: Suggestion,
    onResultClicked: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(32.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                onResultClicked(item.word)
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.dictionary_ic),
            contentDescription = stringResource(R.string.word_icon),
            tint = Color.Unspecified
        )
        Text(
            modifier = Modifier
                .offset(y = (-3).dp)
                .fillMaxWidth()
                .padding(start = 12.dp),
            text = item.word,
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 16.sp
        )
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun HomeTwoSections(
    modifier: Modifier = Modifier,
    pagerState: PagerState,
    focusManager: FocusManager,
    scope: CoroutineScope = rememberCoroutineScope()
) {
    var currentPage by remember { mutableIntStateOf(pagerState.currentPage) }
    LaunchedEffect(key1 = pagerState.currentPage) {
        currentPage = pagerState.currentPage
    }
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(40.dp)
            .shadow(color = MaterialTheme.colorScheme.outlineVariant, blurRadius = 20.dp),
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(8.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            ClickableText(
                modifier = Modifier
                    .fillMaxHeight()
                    .drawBehind {
                        if (currentPage == 0) {
                            drawRoundRect(
                                color = Color(0xFFf64538),
                                cornerRadius = CornerRadius(x = 8.dp.toPx(), y = 8.dp.toPx())
                            )
                        }
                    }
                    .wrapContentHeight(align = Alignment.CenterVertically)
                    .weight(1f),
                text = AnnotatedString(text = stringResource(R.string.today_s_word)),
                style = TextStyle(
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                    fontFamily = appFont,
                    color = if (currentPage == 0) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                ),
                onClick = {
                    currentPage = 0
                    scope.launch(Dispatchers.Main) {
                        pagerState.animateScrollToPage(0)
                    }
                    focusManager.clearFocus()
                }
            )
            ClickableText(
                modifier = Modifier
                    .fillMaxHeight()
                    .drawBehind {
                        if (currentPage == 1) {
                            drawRoundRect(
                                color = Color(0xFFf64538),
                                cornerRadius = CornerRadius(x = 8.dp.toPx(), y = 8.dp.toPx())
                            )
                        }
                    }
                    .wrapContentHeight(align = Alignment.CenterVertically)
                    .weight(1f),
                text = AnnotatedString(text = stringResource(R.string.random)),
                style = TextStyle(
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                    fontFamily = appFont,
                    color = if (currentPage == 1) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                ),
                onClick = {
                    currentPage = 1
                    focusManager.clearFocus()
                    scope.launch(Dispatchers.Main) {
                        pagerState.animateScrollToPage(1)
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun HomePager(
    todayWord: Word?,
    yesterdayWord: Word?,
    pagerState: PagerState,
    randomWord: Word?,
    focusManager: FocusManager,
    isRandomBtnLoading: Boolean,
    onGetRandomWordBtnClicked: () -> Unit,
    onAudioBtnClicked: (Word) -> Unit,
    onReadMoreClicked: (String) -> Unit
) {
    val todaySectionLoader by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.loader))
    val randomWordSectionLoader by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.loader))
    HorizontalPager(state = pagerState) { position ->
        if (position == 0) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.TopCenter
            ) {
                AnimatedVisibility(
                    visible = todayWord != null,
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
                    TodayWord(
                        todayWord = todayWord,
                        yesterdayWord = yesterdayWord,
                        focusManager = focusManager,
                        onAudioBtnClicked = onAudioBtnClicked,
                        onReadMoreClicked = onReadMoreClicked

                    )
                }
                AnimatedVisibility(
                    visible = todayWord == null,
                    enter = scaleIn(animationSpec = tween(durationMillis = 500)) + fadeIn(
                        animationSpec = tween(
                            durationMillis = 500
                        )
                    ),
                    exit = scaleOut(animationSpec = tween(durationMillis = 500)) + fadeOut(
                        animationSpec = tween(
                            durationMillis = 500
                        )
                    )
                ) {
                    LottieAnimation(
                        modifier = Modifier.size(200.dp),
                        composition = todaySectionLoader,
                        iterations = LottieConstants.IterateForever
                    )
                }
            }
        } else {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.TopCenter
            ) {
                AnimatedVisibility(
                    visible = randomWord != null,
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
                    RandomWord(
                        word = randomWord,
                        isRandomBtnLoading = isRandomBtnLoading,
                        focusManager = focusManager,
                        onGetRandomWordBtnClicked = onGetRandomWordBtnClicked,
                        onAudioBtnClicked = onAudioBtnClicked,
                        onReadMoreClicked = onReadMoreClicked
                    )
                }
                AnimatedVisibility(
                    visible = randomWord == null,
                    enter = scaleIn(animationSpec = tween(durationMillis = 500)) + fadeIn(
                        animationSpec = tween(
                            durationMillis = 500
                        )
                    ),
                    exit = scaleOut(animationSpec = tween(durationMillis = 500)) + fadeOut(
                        animationSpec = tween(
                            durationMillis = 500
                        )
                    )
                ) {
                    LottieAnimation(
                        modifier = Modifier.size(200.dp),
                        composition = randomWordSectionLoader,
                        iterations = LottieConstants.IterateForever
                    )
                }
            }
        }
    }
}

@Composable
private fun TodayWord(
    todayWord: Word?,
    yesterdayWord: Word?,
    focusManager: FocusManager,
    onAudioBtnClicked: (Word) -> Unit,
    onReadMoreClicked: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                focusManager.clearFocus()
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        todayWord?.let {
            item {
                WordItem(
                    word = it,
                    timeId = 0,
                    onAudioBtnClicked = onAudioBtnClicked,
                    onReadMoreClicked = onReadMoreClicked
                )
            }
        }
        item {
            Spacer(modifier = Modifier.height(12.dp))
        }
        yesterdayWord?.let {
            item {
                WordItem(
                    word = it,
                    timeId = 1,
                    onAudioBtnClicked = onAudioBtnClicked,
                    onReadMoreClicked = onReadMoreClicked
                )
            }
        }
    }
}

@Composable
private fun RandomWord(
    word: Word?,
    isRandomBtnLoading: Boolean,
    focusManager: FocusManager,
    onGetRandomWordBtnClicked: () -> Unit,
    onAudioBtnClicked: (Word) -> Unit,
    onReadMoreClicked: (String) -> Unit
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.laoding_anim))
    LazyColumn(
        modifier = Modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null
        ) {
            focusManager.clearFocus()
        }
    ) {
        item {
            word?.let {
                WordItem(
                    word = it,
                    isRandomWord = true,
                    isExpandableItem = false,
                    onAudioBtnClicked = onAudioBtnClicked,
                    onReadMoreClicked = onReadMoreClicked
                )
            }
        }
        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
        item {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .padding(8.dp),
                onClick = onGetRandomWordBtnClicked,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    disabledContainerColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                shape = RoundedCornerShape(8.dp),
                enabled = !isRandomBtnLoading
            ) {
                if (isRandomBtnLoading) {
                    LottieAnimation(
                        modifier = Modifier.size(40.dp),
                        composition = composition,
                        iterations = LottieConstants.IterateForever,
                    )
                } else {
                    Text(
                        text = stringResource(R.string.get_random_word),
                        fontFamily = appFont,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}


@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
private fun WordItem(
    word: Word,
    timeId: Int = 0,
    isExpandableItem: Boolean = true,
    isRandomWord: Boolean = false,
    onAudioBtnClicked: (Word) -> Unit,
    onReadMoreClicked: (String) -> Unit
) {
    var isExpanded by remember { mutableStateOf(timeId == 0) }
    val arrowDirection = animateFloatAsState(
        targetValue = if (isExpanded) -1f else 1f,
        label = stringResource(R.string.arrow_animation)
    )
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(color = MaterialTheme.colorScheme.outlineVariant, blurRadius = 20.dp)
            .animateContentSize(),
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = word.word,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = appFont
                    )
                    Text(
                        text = word.getWordPartOfSpeech(),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontFamily = appFont
                    )
                }

                Icon(
                    modifier = Modifier
                        .size(width = 32.dp, height = 32.dp)
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

            }

            Spacer(modifier = Modifier.height(12.dp))

            if (!isRandomWord) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.star_ic),
                        contentDescription = stringResource(R.string.star),
                        tint = Color.Unspecified
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (timeId == 0) stringResource(R.string.today) else stringResource(R.string.yesterday),
                        color = if (timeId == 0) Color.Green else Color.Red,
                        fontFamily = appFont,
                        fontSize = 16.sp
                    )
                }
            }

            if (isExpandableItem) {
                if (isExpanded) {
                    Spacer(modifier = Modifier.height(8.dp))
                    WordExpandedSection(
                        meaning = word.meanings[0],
                        onReadMoreClicked = { onReadMoreClicked(word.word) }
                    )
                }
            } else {
                Spacer(modifier = Modifier.height(8.dp))
                WordExpandedSection(
                    meaning = word.meanings[0],
                    onReadMoreClicked = { onReadMoreClicked(word.word) }
                )
            }

            if (isExpandableItem) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        modifier = Modifier
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                isExpanded = !isExpanded
                            }
                            .graphicsLayer {
                                scaleY = arrowDirection.value
                                scaleX = arrowDirection.value
                            },
                        painter = painterResource(id = R.drawable.down_arrow_ic),
                        contentDescription = stringResource(R.string.down_arrow),
                        tint = Color.Unspecified
                    )
                }
            }
        }
    }
}


@Composable
private fun WordExpandedSection(
    meaning: Meaning,
    onReadMoreClicked: () -> Unit
) {
    Column {
        Text(
            modifier = Modifier.padding(bottom = 8.dp),
            text = meaning.partOfSpeech,
            fontSize = 14.sp,
            fontFamily = appFont,
            fontStyle = FontStyle.Italic
        )
        Divider(
            modifier = Modifier
                .size(width = 50.dp, height = 1.dp)
                .background(color = MaterialTheme.colorScheme.onSurfaceVariant)
        )
        Text(
            modifier = Modifier.padding(top = 12.dp),
            text = stringResource(R.string.definition),
            fontSize = 14.sp,
            fontFamily = appFont,
            fontStyle = FontStyle.Italic,
            color = MaterialTheme.colorScheme.outline
        )
        Text(
            modifier = Modifier.padding(top = 8.dp),
            text = meaning.definitions[0].definition,
            fontSize = 12.sp,
            fontFamily = appFont
        )
        meaning.definitions[0].example?.let { example ->
            Text(
                modifier = Modifier.padding(top = 12.dp),
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
        Spacer(modifier = Modifier.height(12.dp))
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(4.dp)
                )
                .padding(8.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    onReadMoreClicked()
                }
        ) {
            Text(
                text = stringResource(R.string.read_more),
                fontFamily = appFont,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
private fun HomeScreenPreview() {
    HomeScreen()
}