package com.mahmoudibrahem.wordoftheday.presentation.composables.onboarding

import androidx.compose.ui.Alignment
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.mahmoudibrahem.wordoftheday.R
import com.mahmoudibrahem.wordoftheday.presentation.ui.theme.appFont

@Composable
fun OnBoardingScreen(
    viewModel: OnboardingViewModel = hiltViewModel(),
    onNavigateToHome: () -> Unit = {}
) {
    OnboardingScreenContent(
        onGetStartedBtnClicked = {
            viewModel.onGetStartedBtnClicked()
            onNavigateToHome()
        }
    )
}

@Composable
private fun OnboardingScreenContent(
    onGetStartedBtnClicked: () -> Unit
) {
    val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.onboarding_anim))
    Column(
        Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(horizontal = 32.dp, vertical = 32.dp)
    ) {
        Image(
            modifier = Modifier.size(200.dp),
            painter = painterResource(id = R.drawable.onboarding_app_name),
            contentDescription = stringResource(R.string.image)
        )
        Text(
            modifier = Modifier,
            text = stringResource(R.string.vocabulary_builder),
            color = MaterialTheme.colorScheme.primary,
            fontFamily = appFont,
            fontSize = 16.sp
        )
        Text(
            modifier = Modifier,
            text = stringResource(R.string.onboarding_des),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontFamily = appFont,
            fontSize = 14.sp
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Bottom
        ) {
            LottieAnimation(
                modifier = Modifier.fillMaxWidth().height(300.dp),
                composition = composition,
                iterations = LottieConstants.IterateForever
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 12.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        onGetStartedBtnClicked()
                    }
            ) {
                Text(
                    modifier = Modifier.align(Alignment.CenterStart),
                    text = stringResource(R.string.get_started),
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontFamily = appFont,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                Icon(
                    modifier = Modifier.align(Alignment.CenterEnd),
                    painter = painterResource(id = R.drawable.right_arrow_ic),
                    contentDescription = stringResource(R.string.right_arrow),
                    tint = Color.Unspecified
                )
            }
        }
    }
}


@Preview(showSystemUi = true)
@Composable
private fun OnboardingScreenPreview() {
    OnBoardingScreen()
}