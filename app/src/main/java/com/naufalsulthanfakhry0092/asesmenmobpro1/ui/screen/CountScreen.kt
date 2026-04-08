package com.naufalsulthanfakhry0092.asesmenmobpro1.ui.screen

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.naufalsulthanfakhry0092.asesmenmobpro1.ui.theme.AsesmenMobpro1Theme
import com.naufalsulthanfakhry0092.mobpro1.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountScreen(
    navController: NavHostController,
    billName: String,
    onBillNameChange: (String) -> Unit,
    amountText: String,
    onAmountTextChange: (String) -> Unit,
    peopleText: String,
    onPeopleTextChange: (String) -> Unit,
    useTax: Boolean,
    onUseTaxChange: (Boolean) -> Unit,
    taxPercentText: String,
    onTaxPercentTextChange: (String) -> Unit,
    result: String,
    onResultChange: (String) -> Unit
) {
    val context = LocalContext.current

    var billNameError by rememberSaveable { mutableStateOf(false) }
    var amountError by rememberSaveable { mutableStateOf(false) }
    var peopleError by rememberSaveable { mutableStateOf(false) }
    var taxError by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                    navigationIconContentColor = MaterialTheme.colorScheme.primary
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kembali"
                        )
                    }
                },
                title = { Text(stringResource(id = R.string.app_name), fontWeight = FontWeight.Bold) }
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
                onValueChange = onBillNameChange,
                label = { Text(stringResource(id = R.string.nama_tagihan)) },
                isError = billNameError,
                leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null) },
                trailingIcon = { IconPicker(billNameError, "") },
                supportingText = { ErrorHint(billNameError) },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = amountText,
                onValueChange = onAmountTextChange,
                label = { Text(stringResource(id = R.string.total_bill)) },
                isError = amountError,
                leadingIcon = {
                    Text(
                        text = "Rp",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(start = 16.dp, end = 8.dp)
                    )
                },
                trailingIcon = { IconPicker(amountError, "") },
                supportingText = { ErrorHint(amountError) },
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
                onValueChange = onPeopleTextChange,
                label = { Text(stringResource(id = R.string.jumlah_orang)) },
                isError = peopleError,
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                trailingIcon = { IconPicker(peopleError, "Orang") },
                supportingText = { ErrorHint(peopleError) },
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
                        onUseTaxChange(false)
                        onTaxPercentTextChange("")
                        taxError = false
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, if (!useTax) MaterialTheme.colorScheme.primary else Color.Gray.copy(alpha = 0.5f)),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = if (!useTax) MaterialTheme.colorScheme.primaryContainer else Color.Transparent
                    )
                ) {
                    Text(
                        text = stringResource(id = R.string.tanpa_pajak),
                        color = if (!useTax) MaterialTheme.colorScheme.onPrimaryContainer else Color.Gray
                    )
                }

                OutlinedButton(
                    onClick = { onUseTaxChange(true) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, if (useTax) MaterialTheme.colorScheme.primary else Color.Gray.copy(alpha = 0.5f)),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = if (useTax) MaterialTheme.colorScheme.primaryContainer else Color.Transparent
                    )
                ) {
                    Text(
                        text = stringResource(id = R.string.pajak_opsi),
                        color = if (useTax) MaterialTheme.colorScheme.onPrimaryContainer else Color.Gray
                    )
                }
            }

            if (useTax) {
                OutlinedTextField(
                    value = taxPercentText,
                    onValueChange = onTaxPercentTextChange,
                    label = { Text(stringResource(id = R.string.persentase_pajak)) },
                    isError = taxError,
                    trailingIcon = { IconPicker(taxError, "%") },
                    supportingText = { ErrorHint(taxError) },
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
                    billNameError = billName.isEmpty()

                    val amount = amountText.toDoubleOrNull()
                    amountError = amount == null || amount <= 0.0

                    val people = peopleText.toIntOrNull()
                    peopleError = people == null || people <= 0

                    val taxPct = taxPercentText.toDoubleOrNull()
                    taxError = useTax && (taxPct == null || taxPct < 0.0)

                    if (billNameError || amountError || peopleError || taxError) {
                        onResultChange("")
                        return@Button
                    }

                    val safeAmount = amount ?: 0.0
                    val safePeople = people ?: 1
                    val safeTaxPct = taxPct ?: 0.0

                    val taxAmount = if (useTax) safeAmount.times(safeTaxPct.div(100)) else 0.0
                    val perPerson = safeAmount.plus(taxAmount).div(safePeople)
                    onResultChange("Rp ${String.format(java.util.Locale.getDefault(), "%,.0f", perPerson)}")
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
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
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
                            onClick = { shareData(context, message) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(vertical = 12.dp)
                        ) {
                            Icon(Icons.Default.Share, contentDescription = null)
                            Spacer(Modifier.width(8.dp))
                            Text(stringResource(id = R.string.share))
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
            navController = rememberNavController(),
            billName = "",
            onBillNameChange = {},
            amountText = "",
            onAmountTextChange = {},
            peopleText = "",
            onPeopleTextChange = {},
            useTax = false,
            onUseTaxChange = {},
            taxPercentText = "",
            onTaxPercentTextChange = {},
            result = "",
            onResultChange = {}
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
fun IconPicker(isError: Boolean, unit: String) {
    if (isError) {
        Icon(imageVector = Icons.Filled.Warning, contentDescription = null)
    } else if (unit.isNotEmpty()) {
        Text(text = unit)
    }
}

private fun shareData(context: Context, message: String) {
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text" + Char(47) + "plain"
        putExtra(Intent.EXTRA_TEXT, message)
    }
    if (shareIntent.resolveActivity(context.packageManager) != null) {
        context.startActivity(shareIntent)
    }
}