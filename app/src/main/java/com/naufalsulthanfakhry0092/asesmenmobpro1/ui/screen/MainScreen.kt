package com.naufalsulthanfakhry0092.asesmenmobpro1.ui.screen

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.naufalsulthanfakhry0092.asesmenmobpro1.model.Tagihan
import com.naufalsulthanfakhry0092.asesmenmobpro1.navigation.Screen
import com.naufalsulthanfakhry0092.asesmenmobpro1.ui.theme.AsesmenMobpro1Theme
import com.naufalsulthanfakhry0092.asesmenmobpro1.util.SettingsDataStore
import com.naufalsulthanfakhry0092.asesmenmobpro1.util.ViewModelFactory
import com.naufalsulthanfakhry0092.mobpro1.R
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController) {
    val context = LocalContext.current
    val dataStore = SettingsDataStore(context)
    val showList by dataStore.layoutFlow.collectAsState(initial = true)
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.app_name))
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                actions = {
                    IconButton(
                        onClick = {
                            navController.navigate(Screen.RecycleBin.route)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.History,
                            contentDescription = stringResource(id = R.string.recycle_bin),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    IconButton(
                        onClick = {
                            coroutineScope.launch {
                                dataStore.saveLayout(!showList)
                            }
                        }
                    ) {
                        Icon(
                            painter = painterResource(
                                id = if (showList) {
                                    R.drawable.outline_grid_view_24
                                } else {
                                    R.drawable.baseline_view_list_24
                                }
                            ),
                            contentDescription = stringResource(
                                id = if (showList) {
                                    R.string.grid
                                } else {
                                    R.string.list
                                }
                            ),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Screen.FormBaru.route)
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = stringResource(id = R.string.tambah_tagihan),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    ) { paddingValues ->
        ScreenContent(
            showList = showList,
            modifier = Modifier.padding(paddingValues),
            navController = navController
        )
    }
}

@Composable
fun ScreenContent(
    showList: Boolean,
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    val context = LocalContext.current
    val factory = ViewModelFactory(context)
    val viewModel: MainViewModel = viewModel(factory = factory)
    val data by viewModel.data.collectAsState()

    if (data.isEmpty()) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = stringResource(id = R.string.list_kosong))
        }
    } else {
        if (showList) {
            LazyColumn(
                modifier = modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 84.dp)
            ) {
                items(data) { tagihan ->
                    ListItem(
                        tagihan = tagihan,
                        onClick = {
                            navController.navigate(Screen.FormUbah.withId(tagihan.id))
                        }
                    )
                    HorizontalDivider()
                }
            }
        } else {
            LazyVerticalStaggeredGrid(
                modifier = modifier.fillMaxSize(),
                columns = StaggeredGridCells.Fixed(2),
                verticalItemSpacing = 8.dp,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(8.dp, 8.dp, 8.dp, 84.dp)
            ) {
                items(data) { tagihan ->
                    GridItem(tagihan = tagihan) {
                        navController.navigate(Screen.FormUbah.withId(tagihan.id))
                    }
                }
            }
        }
    }
}

@Composable
fun ListItem(
    tagihan: Tagihan,
    onClick: () -> Unit
) {
    val rupiah = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("id-ID"))

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = tagihan.namaTagihan,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Total: ${rupiah.format(tagihan.totalTagihan)}",
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            text = "Jumlah orang: ${tagihan.jumlahOrang}",
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            text = if (tagihan.pakaiPajak) {
                "Pajak: ${tagihan.persentasePajak}%"
            } else {
                "Tanpa pajak"
            },
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            text = "Per orang: ${rupiah.format(tagihan.hasilPerOrang)}",
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight.SemiBold
        )

        Text(
            text = tagihan.tanggalDibuat,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
fun GridItem(
    tagihan: Tagihan,
    onClick: () -> Unit
) {
    val rupiah = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("id-ID"))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(
            width = 1.dp,
            color = DividerDefaults.color
        )
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = tagihan.namaTagihan,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Total: ${rupiah.format(tagihan.totalTagihan)}",
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = "Jumlah orang: ${tagihan.jumlahOrang}",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = if (tagihan.pakaiPajak) {
                    "Pajak: ${tagihan.persentasePajak}%"
                } else {
                    "Tanpa pajak"
                },
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = "Per orang: ${rupiah.format(tagihan.hasilPerOrang)}",
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = tagihan.tanggalDibuat,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun MainScreenPreview() {
    AsesmenMobpro1Theme {
        MainScreen(rememberNavController())
    }
}