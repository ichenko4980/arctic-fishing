package com.Zomfaka3.Tokpor.presentation.dialog

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.Zomfaka3.Tokpor.R
import com.Zomfaka3.Tokpor.core.theme.DeepWater
import com.Zomfaka3.Tokpor.core.theme.FrostWhite
import com.Zomfaka3.Tokpor.core.theme.TextPrimary
import com.Zomfaka3.Tokpor.core.theme.TextSecondary
import com.Zomfaka3.Tokpor.core.theme.TrophyCoral

@Composable
fun PauseDialog(
    onResume: () -> Unit,
    onEndSession: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onResume,
        shape = RoundedCornerShape(24.dp),
        containerColor = FrostWhite,
        title = {
            Text(
                text = stringResource(R.string.dialog_pause_title),
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary
            )
        },
        text = {
            Text(
                text = stringResource(R.string.dialog_pause_text),
                style = MaterialTheme.typography.bodyLarge,
                color = TextSecondary
            )
        },
        confirmButton = {
            TextButton(onClick = onResume) {
                Text(
                    text = stringResource(R.string.cta_resume),
                    style = MaterialTheme.typography.labelLarge,
                    color = DeepWater
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onEndSession) {
                Text(
                    text = stringResource(R.string.cta_end_session),
                    style = MaterialTheme.typography.labelLarge,
                    color = TrophyCoral
                )
            }
        }
    )
}
