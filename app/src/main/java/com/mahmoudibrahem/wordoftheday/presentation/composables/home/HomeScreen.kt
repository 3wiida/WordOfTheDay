package com.mahmoudibrahem.wordoftheday.presentation.composables.home

import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.mahmoudibrahem.wordoftheday.R
import com.mahmoudibrahem.wordoftheday.domain.model.Suggestion
import com.mahmoudibrahem.wordoftheday.presentation.ui.theme.AppMainColor
import com.mahmoudibrahem.wordoftheday.presentation.ui.theme.PlaceHolderColor
import com.mahmoudibrahem.wordoftheday.presentation.ui.theme.appFont

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    HomeScreenContent(
        uiState = uiState,
        onSearchQueryChanged = viewModel::onSearchQueryChanged,
        onSearchFocusChanged = viewModel::onSearchFocusChanged
    )
}

@Composable
private fun HomeScreenContent(
    uiState: HomeScreenUIState,
    onSearchQueryChanged: (String) -> Unit,
    onSearchFocusChanged: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp, vertical = 24.dp)
            .statusBarsPadding(),
    ) {
        Header()
        Spacer(modifier = Modifier.height(12.dp))
        SearchWidget(
            searchQuery = uiState.searchQuery,
            results = uiState.searchResults,
            onSearchQueryChanged = onSearchQueryChanged,
            onSearchFocusChanged = onSearchFocusChanged
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Header(
    onChangeModeBtnClicked: () -> Unit = {}
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        fontFamily = appFont,
                        color = Color.Black,
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
                        color = Color.Black,
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
                .size(42.dp)
                .padding(8.dp)
                .background(color = AppMainColor, shape = RoundedCornerShape(8.dp)),
            onClick = onChangeModeBtnClicked
        ) {
            Icon(
                painter = painterResource(id = R.drawable.moon_ic),
                contentDescription = stringResource(R.string.change_mode),
                tint = Color.Unspecified
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchWidget(
    searchQuery: String = "",
    results: List<Suggestion> = emptyList(),
    onSearchQueryChanged: (String) -> Unit = {},
    onSearchFocusChanged: (Boolean) -> Unit
) {
    val context= LocalContext.current
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.laoding_anim))
    Column(
        modifier = Modifier
            .shadow(
                elevation = 4.dp,
                spotColor = PlaceHolderColor,
                ambientColor = PlaceHolderColor
            )
            .background(color = Color.White, shape = RoundedCornerShape(8.dp))
            .animateContentSize()
            .onFocusChanged {
                Toast.makeText(context, "${it.isFocused}", Toast.LENGTH_SHORT).show()
                onSearchFocusChanged(it.isFocused)
            },

        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .background(
                    color = Color.White,
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
                    .padding(top = 4.dp),
                value = searchQuery,
                onValueChange = onSearchQueryChanged,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = Color.Transparent
                ),
                placeholder = {
                    Text(
                        text = stringResource(R.string.search_placeholder),
                        color = PlaceHolderColor,
                        fontSize = 16.sp
                    )
                },
                textStyle = MaterialTheme.typography.labelMedium,

                )
        }

        if (searchQuery.isNotEmpty() && results.isEmpty()) {
            LottieAnimation(
                modifier = Modifier
                    .size(56.dp)
                    .fillMaxWidth()
                    .background(color = Color.White),
                composition = composition,
                iterations = LottieConstants.IterateForever,
            )
        }

        LazyColumn(
            modifier = Modifier
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(bottomEnd = 8.dp, bottomStart = 8.dp)
                )
                .padding(horizontal = 12.dp)
        ) {
            items(results) { result ->
                SearchResultItem(item = result)
            }
        }
    }
}

@Composable
private fun SearchResultItem(item: Suggestion) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(32.dp),
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
            color = Color.Black,
            fontSize = 16.sp
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun HomeScreenPreview() {
    HomeScreen()
}