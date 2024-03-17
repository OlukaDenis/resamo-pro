package com.dennytech.resamopro.ui.components

import android.opengl.Visibility
import androidx.compose.foundation.Image
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircleOutline
import androidx.compose.material.icons.rounded.ArrowDownward
import androidx.compose.material.icons.rounded.ArrowUpward
import androidx.compose.material.icons.rounded.Cancel
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Store
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.dennytech.resamopro.R
import com.dennytech.resamopro.ui.theme.Black800

@Composable
fun QrIcon(
    modifier: Modifier = Modifier,
    tint: Color = Black800
) {
    Image(
        painter = painterResource(id = R.drawable.ic_qr),
        contentDescription = null,
        alignment = Alignment.Center,
        contentScale = ContentScale.Fit,
        colorFilter = ColorFilter.tint(color = tint),
        modifier = modifier
    )
}

@Composable
fun HorizMenuIcon(
    modifier: Modifier = Modifier,
    tint: Color = Black800
) {
    Image(
        painter = painterResource(id = R.drawable.ic_horiz_menu),
        contentDescription = null,
        alignment = Alignment.Center,
        contentScale = ContentScale.Fit,
        colorFilter = ColorFilter.tint(color = tint),
        modifier = modifier
    )
}

@Composable
fun WalletOutlineIcon(
    modifier: Modifier = Modifier,
    tint: Color = Black800
) {
    Image(
        painter = painterResource(id = R.drawable.ic_wallet_outline),
        contentDescription = null,
        alignment = Alignment.Center,
        contentScale = ContentScale.Fit,
        colorFilter = ColorFilter.tint(color = tint),
        modifier = modifier
    )
}

@Composable
fun DownwardArrowIcon(
    modifier: Modifier = Modifier,
    tint: Color = Black800
) {
    Icon(
        imageVector = Icons.Rounded.ArrowDownward,
        contentDescription = stringResource(id = R.string.home),
        tint = tint,
        modifier = modifier
    )
}

@Composable
fun UpwardArrowIcon(
    modifier: Modifier = Modifier,
    tint: Color = Black800
) {
    Icon(
        imageVector = Icons.Rounded.ArrowUpward,
        contentDescription = stringResource(id = R.string.home),
        tint = tint,
        modifier = modifier
    )
}


@Composable
fun defaultIconTint() = Color.Gray
@Composable
fun HomeIcon(
    modifier: Modifier = Modifier,
    tint: Color = defaultIconTint()
) {
    Icon(
        painter = painterResource(id = R.drawable.ic_wallet),
        contentDescription = stringResource(id = R.string.home),
        tint = tint,
        modifier = modifier
    )
}

@Composable
fun AccountIcon(
    modifier: Modifier = Modifier,
    tint: Color = defaultIconTint()
) {
    Icon(
        painter = painterResource(id = R.drawable.ic_profile),
        contentDescription = stringResource(id = R.string.account),
        tint = tint,
        modifier = modifier
    )
}

@Composable
fun NotificationIcon(
    modifier: Modifier = Modifier,
    tint: Color = defaultIconTint()
) {
    Icon(
        painter = painterResource(id = R.drawable.ic_notifications),
        contentDescription = stringResource(R.string.notifications),
        tint = tint,
        modifier = modifier
    )
}

@Composable
fun TransferIcon(
    modifier: Modifier = Modifier,
    tint: Color = defaultIconTint()
) {
    Icon(
        painter = painterResource(id = R.drawable.ic_transfer),
        contentDescription = stringResource(R.string.transfer),
        tint = tint,
        modifier = modifier
    )
}

@Composable
fun CheckCircleIcon(
    modifier: Modifier = Modifier,
    tint: Color = defaultIconTint()
) {
    Icon(
        imageVector = Icons.Rounded.CheckCircle,
        contentDescription = stringResource(id = R.string.account),
        tint = tint,
        modifier = modifier
    )
}

@Composable
fun CancelCircleIcon(
    modifier: Modifier = Modifier,
    tint: Color = defaultIconTint()
) {
    Icon(
        imageVector = Icons.Rounded.Cancel,
        contentDescription = stringResource(id = R.string.account),
        tint = tint,
        modifier = modifier
    )
}

@Composable
fun AddCircleIcon(
    modifier: Modifier = Modifier,
    tint: Color = defaultIconTint()
) {
    Icon(
        imageVector = Icons.Rounded.AddCircleOutline,
        contentDescription = stringResource(id = R.string.account),
        tint = tint,
        modifier = modifier
    )
}

@Composable
fun StoreFrontIcon(
    modifier: Modifier = Modifier,
    tint: Color = defaultIconTint()
) {
    Icon(
        imageVector = Icons.Rounded.Store,
        contentDescription = stringResource(id = R.string.account),
        tint = tint,
        modifier = modifier
    )
}

@Composable
fun MoreVertIcon(
    modifier: Modifier = Modifier,
    tint: Color = defaultIconTint()
) {
    Icon(
        imageVector = Icons.Rounded.MoreVert,
        contentDescription = stringResource(id = R.string.account),
        tint = tint,
        modifier = modifier
    )
}


@Composable
fun VisibilityIcon(
    modifier: Modifier = Modifier,
    tint: Color = defaultIconTint()
) {
    Icon(
        imageVector = Icons.Rounded.Visibility,
        contentDescription = stringResource(id = R.string.account),
        tint = tint,
        modifier = modifier
    )
}


