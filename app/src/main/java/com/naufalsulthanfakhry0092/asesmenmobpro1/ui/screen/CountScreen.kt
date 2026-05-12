package com.naufalsulthanfakhry0092.asesmenmobpro1.ui.screen

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.naufalsulthanfakhry0092.asesmenmobpro1.ui.theme.AsesmenMobpro1Theme
import com.naufalsulthanfakhry0092.asesmenmobpro1.util.ViewModelFactory
import com.naufalsulthanfakhry0092.mobpro1.R
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountScreen(
    navController: NavHostController,
    id: Long? = null
) {
    val context = LocalContext.current
    val factory = ViewModelFactory(context)
    val viewModel: DetailViewModel = viewModel(factory = factory)

    var billName by rememberSaveable { mutableStateOf("") }
    var amountText by rememberSaveable { mutableStateOf("") }
    var peopleText by rememberSaveable { mutableStateOf("") }
    var useTax by rememberSaveable { mutableStateOf(false) }
    var taxPercentText by rememberSaveable { mutableStateOf("") }
    var result by rememberSaveable { mutableStateOf("") }

    var billNameError by rememberSaveable { mutableStateOf(false) }
    var amountError by rememberSaveable { mutableStateOf(false) }
    var peopleError by rememberSaveable { mutableStateOf(false) }
    var taxError by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (id == null) return@LaunchedEffect

        val data = viewModel.getTagihan(id) ?: return@LaunchedEffect

        billName = data.namaTagihan
        amountText = data.totalTagihan.toString()
        peopleText = data.jumlahOrang.toString()
        useTax = data.pakaiPajak
        taxPercentText = if (data.pakaiPajak) {
            data.persentasePajak.toString()
        } else {
            ""
        }
        result = "Rp ${String.format(Locale.getDefault(), "%,.0f", data.hasilPerOrang)}"
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                    navigationIconContentColor = MaterialTheme.colorScheme.primary
                ),
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.kembali)
                        )
                    }
                },
                title = {
                    Text(
                        text = if (id == null) {
                            stringResource(id = R.string.tambah_tagihan)
                        } else {
                            stringResource(id = R.string.ubah_tagihan)
                        },
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    IconButton(
                        onClick = {
                            billNameError = billName.isBlank()

                            val amount = amountText.toDoubleOrNull()
                            amountError = amount == null || amount <= 0.0

                            val people = peopleText.toIntOrNull()
                            peopleError = people == null || people <= 0

                            val taxPct = taxPercentText.toDoubleOrNull() ?: 0.0
                            taxError = useTax && taxPct < 0.0

                            if (billNameError || amountError || peopleError || taxError) {
                                Toast.makeText(
                                    context,
                                    R.string.invalid,
                                    Toast.LENGTH_LONG
                                ).show()
                                return@IconButton
                            }

                            val safeAmount = amount ?: 0.0
                            val safePeople = people ?: 1
                            val safeTaxPct = if (useTax) taxPct else 0.0

                            val taxAmount = if (useTax) {
                                safeAmount * (safeTaxPct / 100)
                            } else {
                                0.0
                            }

                            val perPerson = (safeAmount + taxAmount) / safePeople

                            if (id == null) {
                                viewModel.insert(
                                    namaTagihan = billName,
                                    totalTagihan = safeAmount,
                                    jumlahOrang = safePeople,
                                    pakaiPajak = useTax,
                                    persentasePajak = safeTaxPct,
                                    hasilPerOrang = perPerson
                                )
                            } else {
                                viewModel.update(
                                    id = id,
                                    namaTagihan = billName,
                                    totalTagihan = safeAmount,
                                    jumlahOrang = safePeople,
                                    pakaiPajak = useTax,
                                    persentasePajak = safeTaxPct,
                                    hasilPerOrang = perPerson
                                )
                            }

                            navController.popBackStack()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = stringResource(id = R.string.simpan)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp, vertical = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = billName,
                onValueChange = {
                    billName = it
                    billNameError = false
                },
                label = {
                    Text(text = stringResource(id = R.string.nama_tagihan))
                },
                isError = billNameError,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null
                    )
                },
                trailingIcon = {
                    IconPicker(billNameError, "")
                },
                supportingText = {
                    ErrorHint(billNameError)
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = amountText,
                onValueChange = {
                    amountText = it
                    amountError = false
                },
                label = {
                    Text(text = stringResource(id = R.string.total_bill))
                },
                isError = amountError,
                leadingIcon = {
                    Text(
                        text = "Rp",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(start = 16.dp, end = 8.dp)
                    )
                },
                trailingIcon = {
                    IconPicker(amountError, "")
                },
                supportingText = {
                    ErrorHint(amountError)
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = peopleText,
                onValueChange = {
                    peopleText = it
                    peopleError = false
                },
                label = {
                    Text(text = stringResource(id = R.string.jumlah_orang))
                },
                isError = peopleError,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null
                    )
                },
                trailingIcon = {
                    IconPicker(peopleError, "Orang")
                },
                supportingText = {
                    ErrorHint(peopleError)
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        useTax = false
                        taxPercentText = ""
                        taxError = false
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(
                        width = 1.dp,
                        color = if (!useTax) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            Color.Gray.copy(alpha = 0.5f)
                        }
                    ),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = if (!useTax) {
                            MaterialTheme.colorScheme.primaryContainer
                        } else {
                            Color.Transparent
                        }
                    )
                ) {
                    Text(
                        text = stringResource(id = R.string.tanpa_pajak),
                        color = if (!useTax) {
                            MaterialTheme.colorScheme.onPrimaryContainer
                        } else {
                            Color.Gray
                        }
                    )
                }

                OutlinedButton(
                    onClick = {
                        useTax = true
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(
                        width = 1.dp,
                        color = if (useTax) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            Color.Gray.copy(alpha = 0.5f)
                        }
                    ),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = if (useTax) {
                            MaterialTheme.colorScheme.primaryContainer
                        } else {
                            Color.Transparent
                        }
                    )
                ) {
                    Text(
                        text = stringResource(id = R.string.pajak_opsi),
                        color = if (useTax) {
                            MaterialTheme.colorScheme.onPrimaryContainer
                        } else {
                            Color.Gray
                        }
                    )
                }
            }

            if (useTax) {
                OutlinedTextField(
                    value = taxPercentText,
                    onValueChange = {
                        taxPercentText = it
                        taxError = false
                    },
                    label = {
                        Text(text = stringResource(id = R.string.persentase_pajak))
                    },
                    isError = taxError,
                    trailingIcon = {
                        IconPicker(taxError, "%")
                    },
                    supportingText = {
                        ErrorHint(taxError)
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                onClick = {
                    billNameError = billName.isBlank()

                    val amount = amountText.toDoubleOrNull()
                    amountError = amount == null || amount <= 0.0

                    val people = peopleText.toIntOrNull()
                    peopleError = people == null || people <= 0

                    val taxPct = taxPercentText.toDoubleOrNull() ?: 0.0
                    taxError = useTax && taxPct < 0.0

                    if (billNameError || amountError || peopleError || taxError) {
                        result = ""
                        return@Button
                    }

                    val safeAmount = amount ?: 0.0
                    val safePeople = people ?: 1
                    val safeTaxPct = if (useTax) taxPct else 0.0

                    val taxAmount = if (useTax) {
                        safeAmount * (safeTaxPct / 100)
                    } else {
                        0.0
                    }

                    val perPerson = (safeAmount + taxAmount) / safePeople

                    result = "Rp ${String.format(Locale.getDefault(), "%,.0f", perPerson)}"
                }
            ) {
                Text(
                    text = stringResource(id = R.string.count),
                    style = MaterialTheme.typography.titleMedium
                )
            }

            if (result.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Per orang bayar:",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = result,
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        val message = "Patungan $billName:\nTotal per orang jadi $result. Buruan transfer ya!"

                        Button(
                            onClick = {
                                shareData(context, message)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(vertical = 12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = null
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = stringResource(id = R.string.share))
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun CountScreenPreview() {
    AsesmenMobpro1Theme {
        CountScreen(
            navController = rememberNavController()
        )
    }
}

@Composable
fun ErrorHint(isError: Boolean) {
    if (isError) {
        Text(text = stringResource(R.string.error_message))
    }
}

@Composable
fun IconPicker(
    isError: Boolean,
    unit: String
) {
    if (isError) {
        Icon(
            imageVector = Icons.Filled.Warning,
            contentDescription = null
        )
    } else if (unit.isNotEmpty()) {
        Text(text = unit)
    }
}

private fun shareData(
    context: Context,
    message: String
) {
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text" + Char(47) + "plain"
        putExtra(Intent.EXTRA_TEXT, message)
    }

    if (shareIntent.resolveActivity(context.packageManager) != null) {
        context.startActivity(shareIntent)
    }
}