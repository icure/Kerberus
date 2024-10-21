# Kerberus

## What is it?

Kerberus is a Kotlin Multiplatform proof of work captcha library. It is designed around this paper
[mCaptcha: Replacing Captchas with Rate limiters to Improve Security and Accessibility](https://dl.acm.org/doi/full/10.1145/3660628)

Kerberus is designed on top of Kryptom, a Kotlin Multiplatform cryptography library. Check it out [here](https://github.com/icure/kryptom)

Part of the implementation is based on the [mCaptcha implementation](https://github.com/mCaptcha/pow_wasm/blob/cb6bbd0e05078e1dadf546de5848d30f5d11a00c/src/lib.rs)

## How to use it?

Kerberus is provided as a standalone library that can be used either on client side to solve challenge or on server side
to validate challenge. It'd be your responsibility to implement the communication between client and server, as well as
the server implementation and difficulty configuration.

Usage is quite simple and revolve around 2 main methods:

- `resolveChallenge(config: Challenge, serializedInput: String, cryptoService: CryptoService? = null, onProgress: (Double) -> Unit = {}): Solution`
- `validateChallenge(config: Challenge, result: Solution, serializedInput: String, cryptoService: CryptoService? = null): Boolean`

The `Challenge` object is the configuration of the challenge, it contains the difficulty factor, the salts and the id of the challenge.

The `Solution` object is the result of the challenge resolution, it contains the id of the challenge and the nonces for each salt.

## Why should you prefer multiple smaller challenges over a single big one?

The main reason is that it allows for a more granular control over the difficulty of the challenge and the time it takes
to solve it. Having a single big challenge would mean that some time people would be able to solve it in a near instant
while some other time it would take them a long time to solve it. This is not ideal as it would mean that the challenge
is either too easy or too hard.

By having multiple smaller challenges, you are reducing the variance of the time it takes to solve the challenge overall.

## Wrapper and integration

[Kerberus is available Maven Central](https://central.sonatype.com/artifact/com.icure/kerberus)

For convenience and to ease integration, some wrappers are also provided for common platforms:

| Platform | Wrapper                                 |
|----------|-----------------------------------------|
| Swift    | https://github.com/icure/kerberus-swift |
| Expo     | https://github.com/icure/expo-kerberus  |
| Dart     | https://github.com/icure/kerberus-dart  |

