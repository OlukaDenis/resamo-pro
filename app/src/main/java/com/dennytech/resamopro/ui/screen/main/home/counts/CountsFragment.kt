package com.dennytech.resamopro.ui.screen.main.home.counts

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
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
import com.dennytech.domain.models.UserDomainModel.Companion.isAdmin
import com.dennytech.resamopro.R
import com.dennytech.resamopro.ui.MainViewModel
import com.dennytech.resamopro.ui.components.DateFilter
import com.dennytech.resamopro.ui.components.InsightCard
import com.dennytech.resamopro.ui.components.LoadingCircle
import com.dennytech.resamopro.ui.components.VerticalSpacer
import com.dennytech.resamopro.ui.screen.main.home.CountsCards
import com.dennytech.resamopro.ui.theme.DeepSeaBlue
import com.dennytech.resamopro.ui.theme.Dimens
import com.dennytech.resamopro.ui.theme.TruliBlue
import com.dennytech.resamopro.utils.Helpers.formatCurrency

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountsFragment(
    viewModel: CountsViewModel = hiltViewModel(),
    mainViewModel: MainViewModel = hiltViewModel(),
    navigateUp: () -> Unit
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
                        text = stringResource(R.string.sales_report),
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navigateUp() }) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBack,
                            contentDescription = stringResource(id = R.string.back),
                            tint = Color.Black
                        )
                    }
                },
            )
        }
    ) { values ->

        Column(
            modifier = Modifier.padding(values)
        ) {

            val counts = viewModel.state.counts

            Column(
                modifier = Modifier
                    .padding(Dimens._16dp),
            ) {

                VerticalSpacer(Dimens._16dp)

                DateFilter(
                    startDate = viewModel.state.startDate,
                    endDate = viewModel.state.endDate,
                    endDateChange = {viewModel.onEvent(CountsEvent.EndDateChanged(it))},
                    startDateChange = {viewModel.onEvent(CountsEvent.StartDateChanged(it))},
                    onSubmit = {viewModel.onEvent(CountsEvent.SubmitFilter)}
                )

                VerticalSpacer(Dimens._16dp)

                Text(
                    text = "Report",
                    textAlign = TextAlign.Center,
                    fontSize = Dimens._16sp,
                    color = DeepSeaBlue
                )

                VerticalSpacer(Dimens._16dp)

                if (viewModel.state.loadingCounts) {
                    LoadingCircle()
                }

                CountsCards(counts = counts)

               mainViewModel.state.user?.let {
                   if(it.isAdmin()) {
                       VerticalSpacer(Dimens._16dp)

                       InsightCard(
                           title = "Generated Revenue",
                           value = viewModel.state.revenue.toDouble().formatCurrency()
                       )
                   }
               }
            }
        }
    }
}