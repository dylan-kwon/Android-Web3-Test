# Android Web3j Test

## Install CLI

```
curl -L get.web3j.io | sh && source ~/.web3j/source.sh
```

## Generate Contract Code from ABI and Bin

- [Guide](https://docs.web3j.io/4.8.7/smart_contracts/construction_and_deployment/#solidity-smart-contract-wrappers)

```
web3j generate solidity -a=./vote-contract/abi/VoteContract.abi -b=./vote-contract/abi/VoteContract.bin -o=./vote-contract/src/main/java -p=dylan.kwon.vote_contract
```

> Output: ./${PROJECT_ROOT}/[vote-contract](./vote-contract)

## Test

- [Create and Restore Crypto Wallet (BIP-39)](./app/src/test/java/dylan/kwon/web3test/CryptoWalletTest.kt)
- [Call the Smart Contract](./app/src/test/java/dylan/kwon/web3test/ContractTest.kt)

### ENV

| Name             | Description                                               |
|:-----------------|:----------------------------------------------------------|
| CONTRACT_OWNER   | Public key of the wallet that deployed the contract       |
| CONTRACT_ADDRESS | Address of contract                                       |
| RPC_URL          | WebSocket URL that supports `eth_newfilter`               |
| PRIVATE_KEY      | Private key of the caller's wallet for the smart contract |