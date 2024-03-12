package vk.cheysoff.presentation.screens.listScreen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import vk.cheysoff.R
import vk.cheysoff.presentation.screens.listScreen.SearchType

@Composable
fun CustomSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    active: Boolean,
    onActiveChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    placeholderText: String = "",
    onSearchTypeChange: (SearchType) -> Unit,
    searchType: SearchType
) {

    val focusRequester = remember { FocusRequester() }
    var currentSearchType by rememberSaveable { mutableStateOf(searchType) }

    val focusManager = LocalFocusManager.current
    Column(
        modifier = modifier
    ) {
        BasicTextField(
            value = query,
            onValueChange = onQueryChange,
            textStyle = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.secondary
            ),
            enabled = enabled,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { onSearch(query) }),
            singleLine = true,
            modifier = Modifier
                .height(66.dp)
                .focusRequester(focusRequester)
                .onFocusChanged { onActiveChange(it.isFocused) }
                .semantics {
                    onClick {
                        focusRequester.requestFocus()
                        true
                    }
                },
            decorationBox = { innerTextField ->
                Box(
                    contentAlignment = Alignment.CenterStart,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(size = 30.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .border(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.outline,
                            shape = RoundedCornerShape(size = 30.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)

                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                        ) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Box {
                                if (query.isEmpty()) {
                                    Text(
                                        text = placeholderText,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.secondary
                                    )
                                }
                                innerTextField()
                            }
                        }
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                        ) {
                            Icon(
                                painterResource(id = R.drawable.search_icon),
                                contentDescription = "search icon",
                                modifier = Modifier
                                    .padding(4.dp),
                                tint = MaterialTheme.colorScheme.outline,
                            )
                        }
                    }
                }
            }
        )
        LaunchedEffect(active) {
            if (!active) {
                focusManager.clearFocus()
            }
        }
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.Top
    ) {
        Button(
            modifier = Modifier
                .fillMaxHeight()
                .weight(0.5f),
            onClick = {
                onSearchTypeChange(SearchType.Local)
                currentSearchType = SearchType.Local
            },
            shape = RoundedCornerShape(bottomStart = 60.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            enabled = (currentSearchType != SearchType.Local)
        ) {
            Text(
                text = "Local",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.fillMaxSize(),
                textAlign = TextAlign.Center
            )
        }

        Spacer(
            modifier = Modifier
                .width(2.dp)
                .fillMaxHeight(0.6f)
        )

        Button(
            modifier = Modifier
                .fillMaxHeight()
                .weight(0.5f),
            onClick = {
                onSearchTypeChange(SearchType.Remote)
                currentSearchType = SearchType.Remote
            },
            shape = RoundedCornerShape(bottomEnd = 60.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            enabled = (currentSearchType != SearchType.Remote)
        ) {
            Text(
                text = "Remote",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.fillMaxSize(),
                textAlign = TextAlign.Center
            )
        }
    }
}