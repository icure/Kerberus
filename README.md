# Kerberus

## What is it?

Kerberus is a Kotlin Multiplatform proof of work captcha library. It is designed around this paper
[mCaptcha: Replacing Captchas with Rate limiters to Improve Security and Accessibility](https://dl.acm.org/doi/full/10.1145/3660628)

Kerberus is designed on top of Kryptom, a Kotlin Multiplatform cryptography library. Check it out [here](https://github.com/icure/kryptom)

## How to use it?

Kerberus is provided as a standalone library that can be used either on client side to solve challenge or on server side
to validate challenge. It'd be your responsibility to implement the communication between client and server, as well as
the server implementation and difficulty configuration.

Usage is quite simple and revolve around 2 main methods:

- `resolveChallenge(config: Challenge, serializedInput: String, cryptoService: CryptoService? = null, onProgress: (Double) -> Unit = {}): Solution`
- `validateChallenge(config: Challenge, result: Solution, serializedInput: String, cryptoService: CryptoService? = null): Boolean`

The `Challenge` object is the configuration of the challenge, it contains the difficulty factor, the salts and the id of the challenge.

The `Solution` object is the result of the challenge resolution, it contains the id of the challenge and the nonces for each salt.

## Wrapper and integration

[Kerberus is available Maven Central](https://central.sonatype.com/artifact/com.icure/kerberus)

For convenience and to ease integration, some wrappers are also provided for common platforms:

| Platform | Wrapper                                 |
|----------|-----------------------------------------|
| Swift    | https://github.com/icure/kerberus-swift |
| Expo     | https://github.com/icure/expo-kerberus  |

