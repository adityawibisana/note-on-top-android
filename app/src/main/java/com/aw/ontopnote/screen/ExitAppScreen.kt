package com.aw.ontopnote.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aw.ontopnote.R


@Preview
@Composable
fun PreviewExitAppScreen() {
    ExitAppScreen {

    }
}

@Composable
fun ExitAppScreen(exitAction: () -> Unit) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()
        .padding(16.dp)) {
        Spacer(modifier = Modifier.weight(1f))
        Text(text = stringResource(id = R.string.tip_home))
        Spacer(modifier = Modifier.padding(8.dp))
        Button(onClick = { exitAction() }, modifier = Modifier.fillMaxWidth()) {
            Text(text = stringResource(id = R.string.exit))
        }
    }
}