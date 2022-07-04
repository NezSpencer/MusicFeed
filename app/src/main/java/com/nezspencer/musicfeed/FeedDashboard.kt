package com.nezspencer.musicfeed

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.AsyncImage
import com.nezspencer.musicfeed.data.Song
import com.nezspencer.musicfeed.ui.theme.Black_EBEBF5_Alpha40
import com.nezspencer.musicfeed.ui.theme.Gray_767680

@Composable
fun FeedDashboard(modifier: Modifier = Modifier, viewModel: FeedViewModel) {

    LaunchedEffect(true) {
        viewModel.initialize()
    }

    ConstraintLayout(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
    ) {
        val (titleView, searchInputView, feedListView, footerProgress) = createRefs()
        val topGuide = createGuidelineFromTop(.1f)
        val startGuide = createGuidelineFromStart(16.dp)
        val endGuide = createGuidelineFromEnd(16.dp)

        TitleText(
            text = stringResource(R.string.discover),
            modifier = Modifier.constrainAs(titleView) {
                top.linkTo(topGuide)
                start.linkTo(startGuide)
                end.linkTo(endGuide)
                width = Dimension.fillToConstraints
            })

        SearchInputView(
            text = viewModel.screenData.query,
            searchInProgress = viewModel.screenData.showSearchProgress,
            onTextChanged = {
                viewModel.updateSearchFilter(it)
            },
            modifier = Modifier.constrainAs(searchInputView) {
                top.linkTo(titleView.bottom, 14.dp)
                start.linkTo(startGuide)
                end.linkTo(endGuide)
                width = Dimension.fillToConstraints
            })

        val gridListState = rememberLazyGridState()
        LazyVerticalGrid(
            state = gridListState,
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
            modifier = Modifier
                .background(Color.Black)
                .constrainAs(feedListView) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(searchInputView.bottom, 10.dp)
                    bottom.linkTo(footerProgress.top)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }) {
            items(viewModel.screenData.songs) { song ->
                FeedItem(song = song)
            }
        }

        gridListState.OnBottomReached(buffer = 1) {
            viewModel.loadMore()
        }

        if (viewModel.screenData.showFooterProgress || viewModel.screenData.showError) {
            Row(verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .background(Color.Black)
                    .height(50.dp)
                    .clickable { viewModel.loadMore() }
                    .constrainAs(footerProgress) {
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        width = Dimension.fillToConstraints
                    }) {
                if (viewModel.screenData.showFooterProgress) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colors.onBackground,
                        strokeWidth = 2.dp
                    )
                } else {
                    BodyText(
                        text = viewModel.screenData.errorMessage,
                        color = MaterialTheme.colors.error,
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp)
                    )

                    Image(
                        painter = painterResource(id = R.drawable.ic_refresh),
                        contentDescription = stringResource(R.string.retry),
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(24.dp),
                        colorFilter = ColorFilter.tint(MaterialTheme.colors.error)
                    )
                }
            }
        } else {
            Spacer(modifier = Modifier.constrainAs(footerProgress) {
                bottom.linkTo(parent.bottom)
            })
        }

        if (viewModel.screenData.showScreenProgress) {
            DashboardProgressScreen()
        }
    }
}

@Composable
fun SearchInputView(
    modifier: Modifier = Modifier,
    text: String,
    searchInProgress: Boolean = false,
    onTextChanged: (String) -> Unit
) {
    val focusManager = LocalFocusManager.current

    TextField(
        value = text,
        onValueChange = onTextChanged,
        leadingIcon = {
            if (searchInProgress) {
                CircularProgressIndicator(
                    modifier = Modifier.size(22.dp),
                    color = MaterialTheme.colors.onBackground,
                    strokeWidth = 1.dp
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.ic_search),
                    contentDescription = stringResource(R.string.search),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .width(24.dp)
                        .height(22.dp)
                )
            }
        },
        placeholder = {
            BodyText(
                text = stringResource(id = R.string.search),
                color = Black_EBEBF5_Alpha40
            )
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = {
            focusManager.clearFocus()
            onTextChanged(text)
        }),
        shape = MaterialTheme.shapes.large,
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Gray_767680,
            textColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        modifier = modifier
    )
}

@Composable
fun FeedItem(modifier: Modifier = Modifier, song: Song) {
    Card(shape = MaterialTheme.shapes.large, elevation = 1.dp, modifier = modifier.size(184.dp)) {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (listenerCountView, nameView, genreView) = createRefs()
            AsyncImage(
                model = song.currentTrack.artworkUrl,
                contentDescription = song.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Card(
                backgroundColor = Color.White.copy(alpha = 0.44f),
                shape = MaterialTheme.shapes.medium,
                elevation = 0.dp,
                modifier = Modifier
                    .wrapContentSize()
                    .height(19.dp)
                    .constrainAs(listenerCountView) {
                        top.linkTo(parent.top, 8.dp)
                        start.linkTo(parent.start, 9.dp)
                    }) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(start = 3.dp, end = 3.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_listener_icon),
                        contentDescription = null,
                        contentScale = ContentScale.Fit
                    )

                    Spacer(modifier = Modifier.width(3.dp))

                    CaptionText(
                        text = song.listenerCount.toString(),
                        style = MaterialTheme.typography.caption.copy(fontSize = 11.sp),
                        color = Color.Black
                    )
                }
            }

            BodyText(text = song.name, modifier = Modifier.constrainAs(nameView) {
                start.linkTo(listenerCountView.start)
                bottom.linkTo(genreView.top, 2.dp)
            })

            CaptionText(
                text = song.genres.joinToString(),
                modifier = Modifier.constrainAs(genreView) {
                    bottom.linkTo(parent.bottom, 9.dp)
                    start.linkTo(listenerCountView.start)
                })
        }
    }
}

@Composable
fun DashboardProgressScreen(
    modifier: Modifier = Modifier,
    progressSize: Dp = 50.dp,
    progressColor: Color = MaterialTheme.colors.onBackground,
    strokeWidth: Dp = 5.dp
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .gesturesDisabled(), contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(progressSize),
            color = progressColor,
            strokeWidth = strokeWidth
        )
    }
}