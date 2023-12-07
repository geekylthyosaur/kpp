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
    onStartSimulation: (Int, Int, Int, Int, ClientGenerationStrategy) -> Unit
) {
    var cashRegistersCount by remember {
        mutableIntStateOf(1)
    }

    var exitsCount by remember {
        mutableIntStateOf(1)
    }

    var minServingTime by remember {
        mutableIntStateOf(1000)
    }

    var maxServingTime by remember {
        mutableIntStateOf(1000)
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
            value = cashRegistersCount.toString(),
            onValueChange = { newValue ->
                val filtered = newValue.filterIndexed { index, char -> char.isDigit() && index < 8 }
                cashRegistersCount = try {
                    max(min(filtered.toInt(), 12), 1)
                } catch (_: Exception) {
                    1
                }
            },
            label = {
                Text(
                    text = "Кількість кас (макс. 12)"
                )
            }
        )
        TextField(
            value = exitsCount.toString(),
            onValueChange = { newValue ->
                val filtered = newValue.filterIndexed { index, char -> char.isDigit() && index < 8 }
                exitsCount = try {
                    max(min(filtered.toInt(), 12), 1)
                } catch (_: Exception) {
                    1
                }
            },
            label = {
                Text(
                    text = "Кількість входів (макс. 4)"
                )
            }
        )
        TextField(
            value = minServingTime.toString(),
            onValueChange = { newValue ->
                val filtered = newValue.filterIndexed { index, char -> char.isDigit() && index < 8 }
                minServingTime = try {
                    max(filtered.toInt(), 1000)
                } catch (_: Exception) {
                    1000
                }
                maxServingTime = max(maxServingTime, minServingTime)
            },
            label = {
                Text(
                    text = "Мінімальний час на квиток (ms)"
                )
            }
        )
        TextField(
            value = maxServingTime.toString(),
            onValueChange = { newValue ->
                val filtered = newValue.filterIndexed { index, char -> char.isDigit() && index < 8 }
                maxServingTime = try {
                    max(filtered.toInt(), minServingTime)
                } catch (_: Exception) {
                    minServingTime
                }
            },
            label = {
                Text(
                    text = "Максимальний час на квиток (ms)"
                )
            }
        )
        ExposedDropdownMenuBox(
            modifier = Modifier,
            expanded = isStrategyMenuExpanded,
            onExpandedChange = { newValue ->
                isStrategyMenuExpanded = newValue
            }
        ) {
            TextField(
                value = selectedStrategy.toString(),
                onValueChange = {},
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = isStrategyMenuExpanded)
                }
            )

            ExposedDropdownMenu(
                expanded = isStrategyMenuExpanded,
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
        Button(
            onClick = {
                onStartSimulation(
                    cashRegistersCount,
                    exitsCount,
                    minServingTime,
                    maxServingTime,
                    selectedStrategy
                )
            }
        ) {
            Text(
                text = "Запуск"
            )
        }
    }
}