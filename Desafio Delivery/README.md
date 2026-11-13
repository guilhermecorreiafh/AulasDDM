# TP7 - Firebase Auth (FirebaseUI)

**Aluno(a):** Guilherme Henrique Feitoza Correia
**RA:** 237057
**Disciplina:** Programação para Dispositivos Móveis
**Professor(a):** Alessandro Ferreira

App Android em Kotlin com tela de login feita com o FirebaseUI, conforme a documentação:
https://firebase.google.com/docs/auth/android/firebaseui?hl=pt-br

## O que o app faz

- `LoginActivity` (tela inicial): botões **Entrar** e **Cadastrar novo usuário**, que abrem a tela de login do FirebaseUI com e-mail e senha. Se o e-mail ainda não tem conta, o FirebaseUI pede nome e senha e cria o usuário no Firebase.
- Se o usuário já estiver logado, o app pula direto para a tela principal.
- `MainActivity`: mostra nome e e-mail do usuário logado e tem o botão **Sair**, que chama `AuthUI.getInstance().signOut()` e volta para a tela de login.

## Dependências

- `com.firebaseui:firebase-ui-auth` (tela de login pronta)
- `com.google.firebase:firebase-bom` (define a versão do Firebase Authentication)
- Plugin `com.google.gms.google-services` para ler o `google-services.json`

## Como rodar

1. No console do Firebase, criar um projeto e adicionar um app Android com o pacote `com.example.tp7`.
2. Baixar o `google-services.json` e colocar dentro da pasta `app/`.
3. Em Authentication > Método de login, ativar **E-mail/senha**.
4. Abrir a pasta TP7 no Android Studio, fazer o Gradle sync e rodar.
