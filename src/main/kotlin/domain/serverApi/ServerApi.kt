package domain.serverApi

import domain.model.client.*
import domain.model.configuration.*
import domain.model.drawable.*
import domain.model.log.*
import domain.model.utilModels.*
import io.socket.client.*
import io.socket.client.Socket
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import org.json.JSONObject

class ServerApi {

    companion object {
        private const val SOCKET_URL = "http://localhost:9092"
    }

    private lateinit var socket: Socket

    fun connect() {
        val options = IO.Options()
        options.forceNew = true

        socket = IO.socket(SOCKET_URL, options)
        socket.connect()

        setupDefaultSockets()
    }

    fun disconnect() {
        socket.disconnect()
    }

    fun sendConfiguration(configuration: Configuration) {
        socket.emit("configuration", JSONObject(Json.encodeToString(configuration)))
    }

    fun connectServerLogicSockets(
        onClientCreated: (DrawableClient) -> Unit,
        onClientServed: (ServedClientLog) -> Unit,
        onCashRegisterClosed: (CashRegisterChange) -> Unit
    ) {
        socket.on("client_created") { args ->
            try {
                val client = Json.decodeFromString<DrawableClient>(args[0].toString())
                onClientCreated(client)
            } catch (e: Exception) {
                println("Error when created client: ${e.message}")
            }
        }

        socket.on("client_served") { args ->
            try {
                val servedClientLog = Json.decodeFromString<ServedClientLog>(args[0].toString())
                onClientServed(servedClientLog)
            } catch (e: Exception) {
                println("Error when served client: ${e.message}")
            }
        }

        socket.on("change_cash_register") { args ->
            try {
                val cashRegisterChange = Json.decodeFromString<CashRegisterChange>(args[0].toString())
                cashRegisterChange.clientIds.addAll(cashRegisterChange.clients.map { it.id })

                onCashRegisterClosed(cashRegisterChange)
            } catch (e: Exception) {
                println("Error changing cash register: ${e.message}")
            }
        }
    }

    fun notifyAboutClientService(clientService: ClientService) {
        socket.emit("client_reached_cash_register", JSONObject(Json.encodeToString(clientService)))
    }

    fun notifyAboutSimulationStop() {
        socket.off("client_created")
        socket.off("client_served")
        socket.off("change_cash_register")

        socket.emit("stop_generation", JSONObject())
    }

    private fun setupDefaultSockets() {
        socket.on(Socket.EVENT_CONNECT) {
            println("Connected socket to $SOCKET_URL")
        }

        socket.on(Socket.EVENT_DISCONNECT) {
            println("Disconnected socket from $SOCKET_URL")
        }
    }
}