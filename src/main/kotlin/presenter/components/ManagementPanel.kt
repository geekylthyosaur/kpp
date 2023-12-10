package presenter.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.*
import domain.model.clientGeneration.*
import kotlin.math.*

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ManagementPanel(
    modifier: Modifier,
    isSimulationStarted: Boolean,
    onIsSimulationStartedChange: (Boolean) -> Unit,
    onCashRegistersCountChange: (Int) -> Unit,
    onExitsCountChange: (Int) -> Unit,
    onStartSimulation: (Int, Int, ClientGenerationStrategy) -> Unit,
    onStopSimulation: () -> Unit
) {
    var cashRegistersCount by remember {
        mutableStateOf("1")
    }

    var exitsCount by remember {
        mutableStateOf("1")
    }

    var minServingTime by remember {
        mutableStateOf("1000")
    }

    var maxServingTime by remember {
        mutableStateOf("1000")
    }

    var selectedStrategy by remember {
        mutableStateOf<ClientGenerationStrategy>(ClientGenerationStrategy.Random)
    }

    var isStrategyMenuExpanded by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = modifier
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        TextField(
            value = cashRegistersCount,
            onValueChange = { newValue ->
                val filtered = newValue.filterIndexed { index, char -> char.isDigit() && index < 8 }
                cashRegistersCount = filtered

                try {
                    max(min(filtered.toInt(), 11), 1).toString().apply {
                        onCashRegistersCountChange(this.toInt())
                    }
                } catch (_: Exception) { }
            },
            label = {
                Text(
                    text = "Кількість кас (макс. 12)"
                )
            },
            enabled = !isSimulationStarted
        )
        TextField(
            value = exitsCount,
            onValueChange = { newValue ->
                val filtered = newValue.filterIndexed { index, char -> char.isDigit() && index < 8 }
                exitsCount = filtered

                try {
                    max(min(filtered.toInt(), 4), 1).toString().apply {
                        onExitsCountChange(this.toInt())
                    }
                } catch (_: Exception) { }
            },
            label = {
                Text(
                    text = "Кількість входів (макс. 4)"
                )
            },
            enabled = !isSimulationStarted
        )
        TextField(
            value = minServingTime,
            onValueChange = { newValue ->
                val filtered = newValue.filterIndexed { index, char -> char.isDigit() && index < 8 }
                minServingTime = filtered

                try {
                    max(filtered.toInt(), 1000).toString().apply {
                        maxServingTime = max(maxServingTime.toInt(), minServingTime.toInt()).toString()
                    }
                } catch (_: Exception) { }
            },
            label = {
                Text(
                    text = "Мінімальний час на квиток (ms)"
                )
            },
            enabled = !isSimulationStarted
        )
        TextField(
            value = maxServingTime,
            onValueChange = { newValue ->
                val filtered = newValue.filterIndexed { index, char -> char.isDigit() && index < 8 }
                maxServingTime = filtered

                try {
                    max(filtered.toInt(), minServingTime.toInt()).toString()
                } catch (_: Exception) { }
            },
            label = {
                Text(
                    text = "Максимальний час на квиток (ms)"
                )
            },
            enabled = !isSimulationStarted
        )
        ExposedDropdownMenuBox(
            modifier = Modifier,
            expanded = isStrategyMenuExpanded && !isSimulationStarted,
            onExpandedChange = { newValue ->
                isStrategyMenuExpanded = newValue
            }
        ) {
            TextField(
                value = selectedStrategy.toString(),
                onValueChange = {},
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = isStrategyMenuExpanded)
                },
                label = {
                    Text(
                        text = "Стратегія появи клієнтів"
                    )
                },
                enabled = !isSimulationStarted
            )
            ExposedDropdownMenu(
                expanded = isStrategyMenuExpanded && !isSimulationStarted,
                onDismissRequest = {
                    isStrategyMenuExpanded = false
                }
            ) {
                ClientGenerationStrategy.getDefaultList().forEach { strategy ->
                    DropdownMenuItem(
                        onClick = {
                            selectedStrategy = strategy
                            isStrategyMenuExpanded = false
                        }
                    ) {
                        Text(
                            text = strategy.toString()
                        )
                    }
                }
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = {
                    onIsSimulationStartedChange(true)
                    try {
                        onStartSimulation(
                            minServingTime.toInt(),
                            maxServingTime.toInt(),
                            selectedStrategy
                        )
                    } catch (_: Exception) { }
                },
                enabled = !isSimulationStarted
            ) {
                Text(
                    text = "Запуск"
                )
            }
            Button(
                onClick = {
                    onIsSimulationStartedChange(false)
                    onStopSimulation()
                },
                enabled = isSimulationStarted
            ) {
                Text(
                    text = "Зупинка"
                )
            }
        }
    }
}