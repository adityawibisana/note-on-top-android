package com.aw.ontopnote.screen

import android.content.Intent
import android.os.Build
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.aw.ontopnote.R


@Composable
fun OverlayPermissionScreen(goToOverlayPermission: ManagedActivityResultLauncher<Intent, ActivityResult>,
                            overlayPermissionIntent: Intent) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()
        .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Text(text = stringResource(id = R.string.allow_display_over_app_permission), textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(16.dp))

        val openDialog = remember { mutableStateOf(false)  }
        Button(onClick = {
            openDialog.value = true
        }, modifier = Modifier.fillMaxWidth()) {
            Text(text = stringResource(id = R.string.allow))
        }

        if (openDialog.value) {
            AlertDialog(
                onDismissRequest = {
                    openDialog.value = false
                },
                confirmButton = {
                    Button(onClick = {
                        openDialog.value = false
                        goToOverlayPermission.launch(overlayPermissionIntent)
                    }) {
                        Text(stringResource(id = R.string.allow))
                    }
                },
                title = {
                    Text(text = stringResource(id = R.string.allow_permission))
                },
                dismissButton = {
                    Button(
                        onClick = {
                            openDialog.value = false
                        }) {
                        Text(stringResource(id = R.string.dismiss))
                    }
                },
            )
        }
    }
}
