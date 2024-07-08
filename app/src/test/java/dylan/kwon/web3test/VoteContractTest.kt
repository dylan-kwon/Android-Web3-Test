package dylan.kwon.web3test

import com.google.common.truth.Truth.assertThat
import dylan.kwon.vote_contract.VoteContract
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.web3j.crypto.Credentials
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameter
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.websocket.WebSocketService
import org.web3j.tx.RawTransactionManager
import org.web3j.tx.gas.DefaultGasProvider
import java.util.Locale

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@OptIn(ExperimentalCoroutinesApi::class)
class VoteContractTest {

    companion object {
        val CONTRACT_OWNER = System.getenv("CONTRACT_OWNER")!!
        val CONTRACT_ADDRESS = System.getenv("CONTRACT_ADDRESS")!!
        val RPC_URL: String = System.getenv("RPC_URL")!!
        val PRIVATE_KEY: String = System.getenv("PRIVATE_KEY")!!
    }

    private lateinit var web3j: Web3j
    private lateinit var contract: VoteContract

    @Before
    fun setup() {
        val credentials = Credentials.create(PRIVATE_KEY)
        val webSocketService = WebSocketService(RPC_URL, true).apply {
            connect()
        }
        web3j = Web3j.build(webSocketService)
        contract = VoteContract.load(
            CONTRACT_ADDRESS,
            web3j,
            RawTransactionManager(web3j, credentials),
            DefaultGasProvider()
        )
    }

    @Test
    fun getOwner() {
        val owner = contract.owner().send()
        assertThat(owner).isEqualTo(CONTRACT_OWNER.lowercase(Locale.getDefault()))
    }

    @Test
    fun createVote() = runTest(UnconfinedTestDispatcher()) {
        val transactionReceipt = contract.createVote(
            "Hello",
            "World",
            "https://img.hankyung.com/photo/202401/03.35225885.1.jpg",
            true,
            mutableListOf(
                "Option 1",
                "Option 2"
            )
        ).send()

        val createVoteResponse = contract.createVoteEventFlowable(
            DefaultBlockParameter.valueOf(transactionReceipt.blockNumber),
            DefaultBlockParameterName.LATEST,
        ).asFlow().firstOrNull {
            it.owner.equals(CONTRACT_OWNER, ignoreCase = true)
        }

        assertThat(createVoteResponse).isNotNull()
    }
}