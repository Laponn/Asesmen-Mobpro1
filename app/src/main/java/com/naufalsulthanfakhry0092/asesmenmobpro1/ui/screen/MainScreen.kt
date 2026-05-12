package com.naufalsulthanfakhry0092.asesmenmobpro1.ui.screen

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.naufalsulthanfakhry0092.asesmenmobpro1.model.Tagihan
import com.naufalsulthanfakhry0092.asesmenmobpro1.ui.theme.AsesmenMobpro1Theme
import com.naufalsulthanfakhry0092.mobpro1.R
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController) {
    val context = LocalContext.current
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.app_name))
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    Toast.makeText(context, R.string.belum_bisa, Toast.LENGTH_SHORT).show()
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
            modifier = Modifier.padding(paddingValues)
        )
    }


}

@Composable
fun ScreenContent(
    modifier: Modifier = Modifier
) {
    val viewModel: MainViewModel = viewModel()
    val data = viewModel.data
//    val data = emptyList<Tagihan>()
    val context = LocalContext.current

    if (data.isEmpty()){
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = stringResource(id = R.string.list_kosong))
        }
    } else{
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 84.dp)
        ) {
            items(data) { tagihan ->
                val pesan = stringResource(R.string.x_diklik, tagihan.namaTagihan)
                ListItem(
                    tagihan = tagihan,
                    onClick = {
                        Toast.makeText(context, pesan,Toast.LENGTH_SHORT).show()
                    }
                )
                HorizontalDivider()
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

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun MainScreenPreview() {
    AsesmenMobpro1Theme {
        MainScreen(rememberNavController())
    }
}