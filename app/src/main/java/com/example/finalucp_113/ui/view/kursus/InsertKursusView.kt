package com.example.finalucp_113.ui.view.kursus

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.finalucp_113.ui.dropdown.DynamicSelectedTextField
import com.example.finalucp_113.ui.navigation.DestinasiNavigasi
import com.example.finalucp_113.ui.theme.Pink80
import com.example.finalucp_113.ui.theme.PinkMedium
import com.example.finalucp_113.ui.viewmodel.instruktur.HomeInstrukturViewModel
import com.example.finalucp_113.ui.viewmodel.instruktur.InstrukturPenyediaViewModel
import com.example.finalucp_113.ui.viewmodel.instruktur.InstrukturUiState
import com.example.finalucp_113.ui.viewmodel.kursus.FormErrorState
import com.example.finalucp_113.ui.viewmodel.kursus.InsertKursusEvent
import com.example.finalucp_113.ui.viewmodel.kursus.InsertKursusUIState
import com.example.finalucp_113.ui.viewmodel.kursus.InsertKursusViewModel
import com.example.finalucp_113.ui.viewmodel.kursus.KursusPenyediaViewModel
import kotlinx.coroutines.launch

object DestinasiInsertKursus : DestinasiNavigasi {
    override val route = "insert_kursus"
    override val titleRes = "Insert kursus"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsertKursusView(
    onBack: () -> Unit,
    onNavigate: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: InsertKursusViewModel = viewModel(factory = KursusPenyediaViewModel.Factory),
    viewModelInstruktur : HomeInstrukturViewModel = viewModel(factory = InstrukturPenyediaViewModel.Factory)
) {
    val uiState = viewModel.uiState
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val instrukturUiState = viewModelInstruktur.instrukturUiState

    LaunchedEffect(uiState.snackBarMessage) {
        uiState.snackBarMessage?.let { message ->
            coroutineScope.launch {
                snackbarHostState.showSnackbar(message)
                viewModel.resetSnackBarMessage()
            }
        }
    }

    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Tambah Kursus",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color(0xFF46051C)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Kembali",
                            tint = Color.Black
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Pink80
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFF46051C))
        ) {
            InsertBodyKursus(
                insertKursusUIState = viewModel.uiState,
                onValueChange = { updateEvent -> viewModel.updateState(updateEvent) },
                onClick = {
                    viewModel.saveData(
                        onSuccess = {
                            onNavigate()
                        }
                    )
                }
            )
        }
    }
}



@Composable
fun InsertBodyKursus(
    modifier: Modifier = Modifier,
    onValueChange: (InsertKursusEvent) -> Unit,
    insertKursusUIState: InsertKursusUIState,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Pink80)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Formulir Kursus",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(bottom = 16.dp),
                textAlign = TextAlign.Center,
                color = PinkMedium
            )

            FormKursus(
                insertKursusEvent = insertKursusUIState.kursusEvent,
                onValueChange = onValueChange,
                errorState = insertKursusUIState.isEntryValid,
                viewModelInstruktur = viewModel(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = PinkMedium)
            ) {
                Text(text = "Simpan", color = Color.White)
            }
        }
    }
}

@Composable
fun FormKursus(
    insertKursusEvent: InsertKursusEvent,
    onValueChange: (InsertKursusEvent) -> Unit,
    errorState: FormErrorState,
    viewModelInstruktur: HomeInstrukturViewModel,
    modifier: Modifier = Modifier
) {
    val instrukturUiState = viewModelInstruktur.instrukturUiState
    val kategoriOptions = listOf("Saintek", "Soshum")


    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = insertKursusEvent.id_kursus,
        onValueChange = { onValueChange(insertKursusEvent.copy(id_kursus = it))},
        label = { Text("ID Kursus") },
        isError = errorState.id_kursus != null,
        placeholder = { Text("Masukkan ID Kursus") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )

    if (errorState.id_kursus != null) {
        Text(text = errorState.id_kursus, color = Color.Red, fontSize = 12.sp)
    }
    Spacer(modifier = Modifier.height(16.dp)

    )


    Column(modifier = modifier.fillMaxWidth()) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = insertKursusEvent.nama_kursus,
            onValueChange = { onValueChange(insertKursusEvent.copy(nama_kursus = it)) },
            label = { Text("Nama") },
            isError = errorState.nama_kursus != null,
            placeholder = { Text("Masukkan nama") }
        )
        if (errorState.nama_kursus != null) {
            Text(text = errorState.nama_kursus ?: "", color = Color.Red, fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(16.dp)
        )

        Column(
            modifier = modifier.fillMaxWidth()) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = insertKursusEvent.deskripsi,
                onValueChange = { onValueChange(insertKursusEvent.copy(deskripsi = it)) },
                label = { Text("Deskripsi") },
                isError = errorState.deskripsi != null,
                placeholder = { Text("Masukkan Deskripsi") }
            )
            if (errorState.deskripsi != null) {
                Text(text = errorState.deskripsi ?: "", color = Color.Red, fontSize = 12.sp)
            }

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Kategori")
            kategoriOptions.forEach { option ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = insertKursusEvent.kategori == option,
                        onClick = { onValueChange(insertKursusEvent.copy(kategori = option)) }
                    )
                    Text(option)
                }
            }

            Column(modifier = modifier.fillMaxWidth()) {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = insertKursusEvent.harga,
                    onValueChange = { onValueChange(insertKursusEvent.copy(harga = it)) },
                    label = { Text("Harga") },
                    isError = errorState.harga != null,
                    placeholder = { Text("Masukkan Harga (contoh: 100000)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                if (errorState.harga != null) {
                    Text(
                        text = errorState.harga ?: "",
                        color = Color.Red,
                        fontSize = 12.sp
                    )
                }

                Spacer(
                    modifier = Modifier.height(16.dp)
                )
            }

        }
    }
    when (instrukturUiState) {
        is InstrukturUiState.Loading -> {
            CircularProgressIndicator(modifier = Modifier.padding(16.dp))
        }

        is InstrukturUiState.Error -> {
            Text("Gagal mengambil data Instruktur", color = MaterialTheme.colorScheme.error)
        }

        is InstrukturUiState.Success -> {
            val instrukturList = instrukturUiState.instruktur
            Column(
                modifier = modifier,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                DynamicSelectedTextField(
                    selectedValue = insertKursusEvent.id_instruktur.toString(),
                    options = instrukturList.map { it.id_instruktur.toString() },
                    label = "Pilih ID Instruktur",
                    onValueChangedEvent = { selectedId: String ->
                        onValueChange(insertKursusEvent.copy(id_instruktur = selectedId))
                    }
                )
            }
        }
    }
}