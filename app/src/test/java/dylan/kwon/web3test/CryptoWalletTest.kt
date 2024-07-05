package dylan.kwon.web3test

import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.junit.rules.TestName
import org.web3j.crypto.WalletUtils
import java.io.File

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class CryptoWalletTest {

    @get:Rule
    val tempFolderRule = TemporaryFolder()

    @get:Rule
    val testNameRule = TestName()

    private lateinit var dir: File

    private val password = "password"

    @Before
    fun setup() {
        dir = tempFolderRule.newFolder("./crypto-wallet")
    }

    @Test
    fun `Create and Restore Wallet Success`() {
        // Generate Wallet
        val newWallet = WalletUtils.generateBip39Wallet(
            password, dir
        )
        val newWalletCredentials = WalletUtils.loadBip39Credentials(
            password, newWallet.mnemonic
        )

        // Restore Wallet
        val restoreWallet = WalletUtils.generateBip39WalletFromMnemonic(
            password, newWallet.mnemonic, dir
        )
        val restoreWalletCredentials = WalletUtils.loadBip39Credentials(
            password, restoreWallet.mnemonic
        )

        // Assertion
        assertThat(newWalletCredentials).isEqualTo(restoreWalletCredentials)
    }

    @Test
    fun `Different Password`() {
        // Generate Wallet
        val newWallet = WalletUtils.generateBip39Wallet(
            password, dir
        )
        val newWalletCredentials = WalletUtils.loadBip39Credentials(
            password, newWallet.mnemonic
        )

        // Restore Wallet
        val differentPassword = "abcd"
        val restoreWallet = WalletUtils.generateBip39WalletFromMnemonic(
            differentPassword, newWallet.mnemonic, dir
        )
        val restoreWalletCredentials = WalletUtils.loadBip39Credentials(
            differentPassword, restoreWallet.mnemonic
        )

        // Assertion
        assertThat(newWalletCredentials).isNotEqualTo(restoreWalletCredentials)
    }

    @Test
    fun `Different Mnemonic`() {
        // Generate Wallet
        val newWallet = WalletUtils.generateBip39Wallet(
            password, dir
        )
        val newWalletCredentials = WalletUtils.loadBip39Credentials(
            password, newWallet.mnemonic
        )

        // Restore Wallet
        val differentMnemonic = "abcd"
        val restoreWallet = WalletUtils.generateBip39WalletFromMnemonic(
            password, differentMnemonic, dir
        )
        val restoreWalletCredentials = WalletUtils.loadBip39Credentials(
            password, differentMnemonic
        )

        // Assertion
        assertThat(newWalletCredentials).isNotEqualTo(restoreWalletCredentials)
    }
}