package com.example.tiptime

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import com.example.tiptime.ui.theme.TipTimeTheme
import org.junit.Rule
import org.junit.Test
import java.text.NumberFormat


class TipUITests {

    @get: Rule
    val composeTestRule = createComposeRule()

    @Test
    fun calculate_20_percent_tip(){
        composeTestRule.setContent {
            TipTimeTheme {
                Surface(modifier = Modifier.fillMaxWidth()) {
                    TipTimeLayout()
                }
            }
        }
        composeTestRule.onNodeWithText("Bill Amount")
            .performTextInput("10")
        composeTestRule.onNodeWithText("Tip Percentage").performTextInput("20")
        val expectedTip = NumberFormat.getCurrencyInstance().format(2)
        composeTestRule.onNodeWithText("Tip Amount: $expectedTip").assertExists("No node with this text was found.")
        //được sử dụng để kiểm tra xem có nút nào trên giao diện người dùng có chứa văn bản "Tip Amount: $expectedTip" hay không. Nếu không có nút nào chứa văn bản này, một thông báo lỗi sẽ được hiển thị.
    }
}