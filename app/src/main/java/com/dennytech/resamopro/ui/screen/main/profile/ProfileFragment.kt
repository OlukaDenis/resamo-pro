package com.dennytech.resamopro.ui.screen.main.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import com.dennytech.resamopro.R
import com.dennytech.resamopro.ui.MainViewModel
import com.dennytech.resamopro.ui.components.AccountIcon
import com.dennytech.resamopro.ui.components.CustomButton
import com.dennytech.resamopro.ui.components.HomeCardItem
import com.dennytech.resamopro.ui.components.HorizontalSpacer
import com.dennytech.resamopro.ui.components.ProductLabel
import com.dennytech.resamopro.ui.components.VerticalSpacer
import com.dennytech.resamopro.ui.theme.DeepSeaBlue
import com.dennytech.resamopro.ui.theme.Dimens
import com.dennytech.resamopro.ui.theme.Grey200
import com.dennytech.resamopro.ui.theme.RedLight400
import com.dennytech.resamopro.ui.theme.RedLight800
import com.dennytech.resamopro.ui.theme.TruliBlueLight900
import com.dennytech.resamopro.ui.theme.TruliRed

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileFragment(
    viewModel: MainViewModel = hiltViewModel(),
    navigateToAuth: () -> Unit,
    navigateToUsers: () -> Unit,
) {

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.onPrimary
                ),
                title = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(id = R.string.account),
                        textAlign = TextAlign.Center
                    )
                },
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxHeight()
                .verticalScroll(rememberScrollState())
        ) {

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Dimens._14dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White,
                ),
                shape = RoundedCornerShape(Dimens._8dp),
            ) {

                Column(
                    modifier = Modifier.padding(Dimens._16dp)
                ) {
                    viewModel.state.user?.let {user ->

                        Text(
                            text = user.fullName,
                            fontSize = Dimens._18sp,
                            color = DeepSeaBlue
                        )
                        VerticalSpacer(Dimens._4dp)
                        Text(
                            text = user.email,
                            fontSize = Dimens._14sp,
                            color = Color.DarkGray
                        )
                        VerticalSpacer(Dimens._4dp)
                        Text(
                            text = user.phone,
                            fontSize = Dimens._14sp,
                            color = Color.DarkGray
                        )
                        VerticalSpacer(Dimens._4dp)
                        ProductLabel(
                            modifier = Modifier.padding(horizontal = Dimens._8dp, vertical = Dimens._4dp),
                            title = if(user.role == 1) "Admin" else "Employee",
                            fontSize = Dimens._10sp
                        )
                    }
                }
            }


            Column(
                modifier = Modifier
                    .padding(Dimens._16dp),
            ) {

               viewModel.state.user?.let { user ->
                   if (user.role == 1) {
                       HomeCardItem(
                           onClick = { navigateToUsers() },
                           title = "User Management",
                           icon = { AccountIcon() }
                       )
                   }
               }

                VerticalSpacer(Dimens._50dp)

                CustomButton(
                    modifier = Modifier.fillMaxWidth(),
                    title = "Logout",
                    colors = ButtonDefaults.buttonColors(
                        containerColor = TruliRed
                    ),
                    onClick = {
                        viewModel.logout()
                        navigateToAuth()
                    }
                )
            }
        }
    }
}