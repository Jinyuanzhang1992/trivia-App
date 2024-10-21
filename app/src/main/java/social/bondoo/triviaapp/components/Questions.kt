package social.bondoo.triviaapp.components

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import social.bondoo.triviaapp.data.QuestionItem
import social.bondoo.triviaapp.screens.QuestionsViewModel
import social.bondoo.triviaapp.utils.AppColours

@Composable
fun Questions(viewModel: QuestionsViewModel) {
    //因为我们在 Question 中定义的是ArrayList<QuestionItem>()，这里我们需要转换成MutableList<QuestionItem>类型，不然会报错
    val questions = viewModel.data.value.data?.toMutableList()
    val questionIndex = remember { mutableIntStateOf(0) }
    if (viewModel.data.value.loading == true) {
        Log.d("Loading", "Questions: Questions: ...Loading...")
    } else {
        val question =
            try {
                questions?.get(questionIndex.intValue)
            } catch (e: Exception) {
                null
            }

        if (questions != null) {
            MainContent(
                question = question!!,
                questionIndex = questionIndex,
                totalSize = questions.size
            ) {
                questionIndex.intValue = it + 1
                Log.d("total", "Questions: ${questionIndex.intValue}")
            }
        }
    }
}

@Composable
fun MainContent(
    question: QuestionItem,
    questionIndex: MutableState<Int>,
    totalSize: Int = 100,
    onNextClicked: (Int) -> Unit
) {
    val choicesState = remember(question) { question.choices.toMutableList() }
    val pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
    val answerState = remember(question) { mutableStateOf<Int?>(null) }
    val correctAnswerState = remember(question) { mutableStateOf<Boolean?>(null) }
    val updateAnswer: (Int) -> Unit = remember(question) {
        {
            answerState.value = it
            correctAnswerState.value = choicesState[it] == question.answer
        }
    }
    val correctCount = remember { mutableIntStateOf(0) }
    Surface(
        modifier =
        Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        color = AppColours.mDarkPurple
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {

            if (questionIndex.value >= 3) {
                ShowProgress(
                    score = questionIndex.value,
                    correctCount = correctCount.intValue,
                    totalSize = totalSize
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
            QuestionTracker(counter = questionIndex.value + 1, outOf = totalSize)
            DrawDottedLine(pathEffect)
            Column {
                Text(
                    modifier = Modifier
                        .padding(6.dp)
                        .align(alignment = Alignment.Start)
                        .fillMaxWidth()
                        .fillMaxHeight(0.3f),
                    text = question.question,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 22.sp,
                    color = AppColours.mOffWhite
                )
                choicesState.forEachIndexed { index, answerText ->
                    Row(
                        modifier = Modifier
                            .padding(3.dp)
                            .fillMaxWidth()
                            .clickable(
                                onClick = { updateAnswer(index) },
                                indication = null,
                                interactionSource = remember { MutableInteractionSource() }
                            )
                            .border(
                                width = 4.dp, brush = Brush.linearGradient(
                                    colors = listOf(
                                        AppColours.mOffDarkPurple,
                                        AppColours.mOffDarkPurple
                                    )
                                ),
                                shape = RoundedCornerShape(15.dp)
                            )
                            .clip(
                                RoundedCornerShape(
                                    topStartPercent = 50,
                                    topEndPercent = 50,
                                    bottomStartPercent = 50,
                                    bottomEndPercent = 50
                                )
                            )
                            .background(Color.Transparent),
                        verticalAlignment = Alignment.CenterVertically
                    )
                    {
                        RadioButton(
                            selected = (answerState.value == index),
                            onClick = {
                                updateAnswer(index)
                            },
                            modifier = Modifier.padding(start = 16.dp),
                            colors = RadioButtonDefaults.colors(
                                selectedColor =
                                if (correctAnswerState.value == true && index == answerState.value) {
                                    Color.Green.copy(alpha = 0.2f)
                                } else {
                                    Color.Red.copy(alpha = 0.2f)
                                }
                            ),
                        )
                        val annotatedString = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    fontWeight = FontWeight.Light,
                                    color = if (correctAnswerState.value == true && index == answerState.value) {
                                        Color.Green
                                    } else if (correctAnswerState.value == false && index == answerState.value) {
                                        Color.Red
                                    } else {
                                        AppColours.mOffWhite
                                    },
                                    fontSize = 17.sp
                                )
                            ) {
                                append(answerText)
                            }
                        }
                        Text(
                            text = annotatedString,
                            modifier = Modifier.padding(6.dp),
                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    modifier = Modifier
                        .padding(3.dp)
                        .align(alignment = Alignment.CenterHorizontally)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(34.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AppColours.mLightBlue,
                    ),
                    onClick = {
                        onNextClicked(questionIndex.value)
                        if (correctAnswerState.value == true) {
                            correctCount.intValue += 1
                            Log.d("next", "MainContent: Correct Answer: ${correctCount.intValue}")
                        }
                    }
                ) {
                    Text(
                        text = "Next",
                        modifier = Modifier.padding(4.dp),
                        fontSize = 17.sp,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ShowProgress(
    score: Int = 100,
    correctCount: Int = 100,
    totalSize: Int = 120
) {
    val gradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFFF95075),
            Color(0xFFBE6BE5)
        )
    )
    val progressFactor by remember(score,totalSize) {
        mutableFloatStateOf(maxOf(0.2f, score.toFloat() / totalSize))
    }
    Row(
        modifier = Modifier
            .padding(3.dp)
            .fillMaxWidth()
            .height(45.dp)
            .border(
                width = 4.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        AppColours.mOffDarkPurple,
                        AppColours.mOffDarkPurple
                    )
                ),
                shape = RoundedCornerShape(34.dp)
            )
            .clip(
                RoundedCornerShape(
                    topStartPercent = 50,
                    topEndPercent = 50,
                    bottomStartPercent = 50,
                    bottomEndPercent = 50
                )
            )
            .background(Color.Transparent),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Log.d("pro", "ShowProgress: $progressFactor")
        Button(
            contentPadding = PaddingValues(1.dp),
            onClick = { },
            modifier = Modifier
                .fillMaxWidth(progressFactor)
                .background(
                    brush = gradient,
                ),
            enabled = false,
            elevation = null,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent
            )
        ) {
            Text(
                text = correctCount.toString(),
                modifier = Modifier
                    .clip(RoundedCornerShape(23.dp))
                    .padding(6.dp),
                color = AppColours.mOffWhite,
                textAlign = TextAlign.Center,
                fontSize = 17.sp,
            )
        }
    }
}

@Composable
fun DrawDottedLine(pathEffect: PathEffect) {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
    ) {
        drawLine(
            color = AppColours.mLightGray,
            pathEffect = pathEffect,
            start = Offset(0f, 0f),
            end = Offset(size.width, 0f)
        )
    }
}


@Composable
fun QuestionTracker(
    counter: Int = 10,
    outOf: Int = 100
) {
    Text(
        text = buildAnnotatedString {
            withStyle(style = ParagraphStyle(textIndent = TextIndent.None))
            {
                withStyle(
                    style = SpanStyle(
                        color = AppColours.mLightGray,
                        fontWeight = FontWeight.Bold,
                        fontSize = 27.sp
                    )
                ) {
                    append("Question: $counter/")
                    withStyle(
                        style = SpanStyle(
                            color = AppColours.mLightGray,
                            fontWeight = FontWeight.Light,
                            fontSize = 14.sp
                        )
                    ) {
                        append("$outOf")
                    }
                }
            }
        },
        modifier = Modifier.padding(4.dp)
    )
}
