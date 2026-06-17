package com.naufalsulthanfakhry0092.asesmenmobpro1.ui.screen

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
import com.naufalsulthanfakhry0092.asesmenmobpro1.model.User
import com.naufalsulthanfakhry0092.asesmenmobpro1.network.TagihanApi
import com.naufalsulthanfakhry0092.asesmenmobpro1.network.UserDataStore
import com.naufalsulthanfakhry0092.asesmenmobpro1.util.ViewModelFactory
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
    val mainViewModel: MainViewModel = viewModel(
        factory = factory,
        viewModelStoreOwner = context as ComponentActivity
    )
    val userDataStore = UserDataStore(context)
    val user by userDataStore.userFlow.collectAsState(User())

    var billName by rememberSaveable { mutableStateOf("") }
    var amountText by rememberSaveable { mutableStateOf("") }
    var peopleText by rememberSaveable { mutableStateOf("") }
    var useTax by rememberSaveable { mutableStateOf(false) }
    var taxPercentText by rememberSaveable { mutableStateOf("") }
    var result by rememberSaveable { mutableStateOf("") }
    var existingImageId by remember { mutableStateOf<String?>(null) }

    var billNameError by rememberSaveable { mutableStateOf(false) }
    var amountError by rememberSaveable { mutableStateOf(false) }
    var peopleError by rememberSaveable { mutableStateOf(false) }
    var taxError by rememberSaveable { mutableStateOf(false) }

    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    var hasLaunchedCamera by rememberSaveable { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(CropImageContract()) {
        bitmap = getCroppedImage(context.contentResolver, it)
    }

    LaunchedEffect(Unit) {
        if (id == null && bitmap == null && !hasLaunchedCamera) {
            hasLaunchedCamera = true
            val options = CropImageContractOptions(
                null, CropImageOptions(
                    imageSourceIncludeGallery = false,
                    imageSourceIncludeCamera = true,
                    fixAspectRatio = true
                )
            )
            launcher.launch(options)
        }
    }

    LaunchedEffect(id) {
        if (id == null) return@LaunchedEffect
        val data = mainViewModel.data.value.find { it.id == id } ?: return@LaunchedEffect
        billName = data.namaTagihan
        amountText = data.totalTagihan.toInt().toString()
        peopleText = data.jumlahOrang.toString()
        useTax = data.pakaiPajak
        existingImageId = data.imageId
        taxPercentText = if (data.pakaiPajak) data.persentasePajak.toString() else ""
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
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                },
                title = {
                    Text(
                        text = if (id == null) "Tambah Tagihan" else "Ubah Tagihan",
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    IconButton(
                        onClick = {
                            if (id == null && bitmap == null) {
                                Toast.makeText(context, "Foto wajib ada", Toast.LENGTH_SHORT).show()
                                return@IconButton
                            }

                            billNameError = billName.isBlank()
                            val amount = amountText.toIntOrNull()
                            amountError = amount == null || amount <= 0
                            val people = peopleText.toIntOrNull()
                            peopleError = people == null || people <= 0
                            val taxPct = taxPercentText.toDoubleOrNull() ?: 0.0
                            taxError = useTax && taxPct < 0.0

                            if (billNameError || amountError || peopleError || taxError) return@IconButton

                            val safeAmount = amount ?: 0
                            val safePeople = people ?: 1
                            val taxAmount = if (useTax) safeAmount.toDouble() * (taxPct / 100) else 0.0
                            val perPerson = (safeAmount.toDouble() + taxAmount) / safePeople

                            if (id == null) {
                                mainViewModel.saveData(user.email, billName, safeAmount, safePeople, useTax, taxPct, perPerson, bitmap!!)
                                viewModel.insert(billName, safeAmount.toDouble(), safePeople, useTax, taxPct, perPerson)
                            } else {
                                mainViewModel.updateData(user.email, id, billName, safeAmount, safePeople, useTax, taxPct, perPerson, bitmap)
                                viewModel.update(id, billName, safeAmount.toDouble(), safePeople, useTax, taxPct, perPerson)
                            }
                            navController.popBackStack()
                        }
                    ) {
                        Icon(Icons.Filled.Check, contentDescription = null)
                    }

                    if (id != null) {
                        DeleteAction(
                            delete = {
                                mainViewModel.moveToRecycleBin(user.email, id)
                                viewModel.delete(id)
                                navController.popBackStack()
                            }
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
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(
                        MaterialTheme.colorScheme.surfaceVariant,
                        RoundedCornerShape(12.dp)
                    )
                    .clickable {
                        val options = CropImageContractOptions(
                            null, CropImageOptions(
                                imageSourceIncludeGallery = false,
                                imageSourceIncludeCamera = true,
                                fixAspectRatio = true
                            )
                        )
                        launcher.launch(options)
                    },
                contentAlignment = Alignment.Center
            ) {
                if (bitmap != null) {
                    Image(
                        painter = BitmapPainter(bitmap!!.asImageBitmap()),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(12.dp))
                    )
                } else if (!existingImageId.isNullOrEmpty()) {
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(TagihanApi.getImageUrl(existingImageId!!))
                            .crossfade(true)
                            .build(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(12.dp))
                    )
                } else {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp)
                    )
                }
            }

            OutlinedTextField(
                value = billName,
                onValueChange = { billName = it },
                label = { Text("Nama Tagihan") },
                isError = billNameError,
                supportingText = { if (billNameError) Text("Nama tagihan tidak boleh kosong") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = amountText,
                onValueChange = { amountText = it },
                label = { Text("Total Bill") },
                isError = amountError,
                supportingText = { if (amountError) Text("Total bill harus lebih dari 0") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = peopleText,
                onValueChange = { peopleText = it },
                label = { Text("Jumlah Orang") },
                isError = peopleError,
                supportingText = { if (peopleError) Text("Jumlah orang harus lebih dari 0") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
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
                        result = ""
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(
                        width = 1.dp,
                        color = if (!useTax) MaterialTheme.colorScheme.primary else Color.Gray.copy(alpha = 0.5f)
                    ),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = if (!useTax) MaterialTheme.colorScheme.primaryContainer else Color.Transparent
                    )
                ) {
                    Text(
                        text = "Tanpa Pajak",
                        color = if (!useTax) MaterialTheme.colorScheme.onPrimaryContainer else Color.Gray
                    )
                }

                OutlinedButton(
                    onClick = { useTax = true },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(
                        width = 1.dp,
                        color = if (useTax) MaterialTheme.colorScheme.primary else Color.Gray.copy(alpha = 0.5f)
                    ),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = if (useTax) MaterialTheme.colorScheme.primaryContainer else Color.Transparent
                    )
                ) {
                    Text(
                        text = "Pajak",
                        color = if (useTax) MaterialTheme.colorScheme.onPrimaryContainer else Color.Gray
                    )
                }
            }

            if (useTax) {
                OutlinedTextField(
                    value = taxPercentText,
                    onValueChange = { taxPercentText = it },
                    label = { Text("Persentase Pajak (%)") },
                    isError = taxError,
                    supportingText = { if (taxError) Text("Persentase pajak tidak valid") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Button(
                onClick = {
                    val amount = amountText.toIntOrNull()
                    val people = peopleText.toIntOrNull()
                    val taxPct = taxPercentText.toDoubleOrNull() ?: 0.0

                    amountError = amount == null || amount <= 0
                    peopleError = people == null || people <= 0
                    taxError = useTax && taxPct < 0.0

                    if (amountError || peopleError || taxError) return@Button

                    val safeAmount = amount!!
                    val safePeople = people!!
                    val taxAmount = if (useTax) safeAmount.toDouble() * (taxPct / 100) else 0.0
                    val perPerson = (safeAmount.toDouble() + taxAmount) / safePeople

                    result = "Rp ${String.format(Locale.getDefault(), "%,.0f", perPerson)}"
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Hitung")
            }

            if (result.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "Per orang membayar",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Text(
                                text = result,
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                textAlign = TextAlign.Center
                            )
                        }

                        val message = "Patungan $billName:\nTotal per orang jadi $result. Buruan transfer ya!"

                        Button(
                            onClick = { shareData(context, message) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(vertical = 12.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Share, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "Bagikan")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DeleteAction(delete: () -> Unit) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    IconButton(onClick = { expanded = true }) {
        Icon(imageVector = Icons.Filled.MoreVert, contentDescription = null)
    }

    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
        DropdownMenuItem(
            text = { Text("Hapus") },
            onClick = {
                expanded = false
                delete()
            }
        )
    }
}

private fun shareData(context: Context, message: String) {
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, message)
    }
    if (shareIntent.resolveActivity(context.packageManager) != null) {
        context.startActivity(shareIntent)
    }
}

private fun getCroppedImage(resolver: ContentResolver, result: CropImageView.CropResult): Bitmap? {
    if (!result.isSuccessful) return null
    val uri = result.uriContent ?: return null
    return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
        MediaStore.Images.Media.getBitmap(resolver, uri)
    } else {
        ImageDecoder.decodeBitmap(ImageDecoder.createSource(resolver, uri))
    }
}