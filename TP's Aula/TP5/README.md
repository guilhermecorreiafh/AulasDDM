# Filmes Favoritos (MVVM + View Binding + Coil)

App Android em Kotlin para visualizar, adicionar, editar e remover filmes favoritos (nome, nota e foto).

## Arquitetura MVVM
- `model/` -> `Filme.kt` (data class) e `FilmeRepository.kt` (dados em memória)
- `viewmodel/` -> `FilmeViewModel.kt` (estado + lógica, LiveData)
- `view/` -> `MainActivity.kt`, `FilmeAdapter.kt` + layouts XML

## View Binding
Ativado em `app/build.gradle.kts` (`buildFeatures { viewBinding = true }`).
Classes geradas: `ActivityMainBinding`, `ItemFilmeBinding`, `DialogFilmeBinding`.

## Coil
`implementation("io.coil-kt:coil:2.7.0")` e uso via `imageView.load(url)`.

## Como rodar
Abra a pasta no Android Studio, aguarde o Gradle sync e execute em um emulador/dispositivo.
