package pl.sokolowskibartlomiej.stations.presentation.ui.settings

import android.app.LocaleManager
import android.content.Context
import android.os.Build
import android.os.LocaleList
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import pl.sokolowskibartlomiej.stations.R
import pl.sokolowskibartlomiej.stations.domain.repository.UserPreferences
import pl.sokolowskibartlomiej.stations.presentation.components.MultiSelectDialog
import pl.sokolowskibartlomiej.stations.presentation.ui.theme.ColorTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    navigateBack: () -> Unit
) {
    val preferences by viewModel.preferencesFlow.collectAsStateWithLifecycle(initialValue = UserPreferences())

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors =
                TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background),
                title = { Text(text = stringResource(id = R.string.settings)) },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(imageVector = Icons.Filled.Close, contentDescription = "")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            ThemePreferenceRow(
                currentTheme = preferences.colorTheme,
                onThemeChange = { theme ->
                    viewModel.updateNightMode(theme)
                }
            )
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                SwitchPreferenceRow(
                    title = stringResource(id = R.string.dynamic_colors_title),
                    summary = stringResource(id = R.string.dynamic_colors_summary),
                    checked = preferences.dynamicColors,
                    onCheckedChange = viewModel::updateDynamicColors
                )
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                LanguagePreferenceRow()
        }
    }
}

@Composable
private fun SwitchPreferenceRow(
    title: String,
    summary: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = summary,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            thumbContent = if (checked) {
                {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = null,
                        modifier = Modifier.size(SwitchDefaults.IconSize),
                    )
                }
            } else null
        )
    }
}

@Composable
private fun ThemePreferenceRow(currentTheme: ColorTheme, onThemeChange: (String) -> Unit) {
    val values = stringArrayResource(id = R.array.themes)
    val codes = stringArrayResource(id = R.array.themes_codes)

    SelectionPreferenceRow(
        titleId = R.string.theme_title,
        currentValue = currentTheme.value,
        values = values,
        codes = codes,
        dialogImageVector = Icons.Outlined.DarkMode,
        dialogTitleId = R.string.theme_dialog_title,
        onSave = onThemeChange
    )
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
private fun LanguagePreferenceRow() {
    val localeManager =
        LocalContext.current.getSystemService(Context.LOCALE_SERVICE) as LocaleManager
    val values = stringArrayResource(id = R.array.languages)
    val codes = stringArrayResource(id = R.array.languages_codes)
    var currentLanguage by rememberSaveable {
        mutableStateOf(localeManager.applicationLocales[0]?.language ?: "")
    }

    SelectionPreferenceRow(
        titleId = R.string.language_setting_title,
        currentValue = currentLanguage,
        values = values,
        codes = codes,
        dialogImageVector = Icons.Outlined.Language,
        dialogTitleId = R.string.language_setting_dialog_title,
        onSave = { selectedLanguageCode ->
            currentLanguage = selectedLanguageCode
            localeManager.applicationLocales = LocaleList.forLanguageTags(currentLanguage)
        }
    )
}

@Composable
private fun SelectionPreferenceRow(
    titleId: Int,
    currentValue: String,
    values: Array<String>,
    codes: Array<String>,
    dialogImageVector: ImageVector,
    dialogTitleId: Int,
    onSave: (String) -> Unit
) {
    var dialogVisible by rememberSaveable { mutableStateOf(false) }
    val getCorrectValueByCode = {
        val index = codes.indexOf(currentValue)
        values[if (index != -1) index else 0]
    }
    val (selectedValue, onValueSelected) = remember { mutableStateOf(getCorrectValueByCode()) }

    LaunchedEffect(key1 = currentValue) {
        onValueSelected(getCorrectValueByCode())
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { dialogVisible = true }
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = stringResource(id = titleId),
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = stringResource(
                    id = R.string.selection_setting_summary,
                    values[codes.indexOf(currentValue)]
                ),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }

    MultiSelectDialog(
        isVisible = dialogVisible,
        imageVector = dialogImageVector,
        titleId = dialogTitleId,
        currentValue = selectedValue,
        values = values,
        onSave = { onSave(codes[values.indexOf(it)]) },
        onDismiss = { dialogVisible = false }
    )
}