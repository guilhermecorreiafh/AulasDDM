# Delivery DDM

Aplicativo Android em Kotlin para cadastro de itens por restaurantes e criação de pedidos por clientes. O projeto mantém a estrutura estudada nas aulas: telas em `view`, modelos em `model`, Adapter em `adapter`, layouts XML, `findViewById` e navegação com `Intent`.

## Funcionalidades

- Login e cadastro por e-mail e senha com FirebaseUI.
- Escolha inicial do perfil Cliente ou Restaurante, guardado em `usuarios/{uid}`.
- Restaurantes cadastram itens; cada item guarda o ID e o nome de quem o cadastrou.
- Clientes selecionam itens de um único restaurante e gravam o pedido com seus dados, itens, total e horário do servidor.
- O restaurante consulta somente os itens associados ao seu usuário. O aplicativo não lista pedidos para restaurantes.

## Executar

Abra esta pasta no Android Studio, aguarde a sincronização do Gradle e execute o app em um emulador ou dispositivo Android.

Antes de executar, conclua a configuração descrita abaixo. O `google-services.json` que veio na cópia do TP7 aponta para o projeto `tp07-bd6fd`; substitua-o pelo arquivo do projeto Firebase de delivery antes de usar o app.

## Configuração Firebase necessária

1. No Firebase Console, abra o projeto de delivery, selecione o app Android registrado com o pacote `com.example.tp7` e baixe seu `google-services.json`.
2. Substitua `app/google-services.json` por esse arquivo e sincronize o Gradle. As chaves não são inseridas no código Kotlin.
3. Em **Authentication → Método de login**, ative **E-mail/senha**.
4. Crie o **Cloud Firestore** em modo de teste para o exercício.

O Firestore usa as coleções `usuarios`, `itens` e `pedidos`; elas são criadas automaticamente quando o primeiro documento é gravado. O modo de teste é destinado à atividade e não configura regras de produção.

## Roteiro de verificação manual

1. Crie uma conta, escolha Restaurante e cadastre dois ou três itens.
2. Saia e crie outra conta como Cliente. Confira o cardápio, marque itens do mesmo restaurante e crie um pedido.
3. Tente marcar um item de outro restaurante sem limpar a seleção; o app deve impedir a mistura. Desmarque tudo e confira que pode escolher outro restaurante.
4. No Firestore Console, confira `usuarios`, `itens` e `pedidos`, incluindo `clienteId`, `clienteNome`, itens, total e `criadoEm`.
5. Feche e abra o app em cada conta e confirme que o perfil já escolhido encaminha para a tela correspondente. Use **Sair** para voltar ao login.

## Se algo falhar

- Login: confirme que E-mail/senha está ativo e que o JSON é do projeto de delivery e do pacote `com.example.tp7`.
- Falha ao ler ou salvar: confira a configuração e as regras do Firestore no projeto de delivery.
- Cardápio sem itens: confirme no console se os documentos em `itens` têm `nome`, `preco`, `restauranteId` e `restauranteNome`.
