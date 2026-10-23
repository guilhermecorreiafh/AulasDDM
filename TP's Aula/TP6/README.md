# AppRoomProduto: Cadastro de Produtos com Room SQLite

**Aluno(a):** Guilherme Henrique Feitoza Correia
**RA:** 237057
**Disciplina:** Programação para Dispositivos Móveis
**Professor(a):** Alessandro Ferreira

## Sobre o projeto

Aplicativo Android em Kotlin que cadastra, lista, busca, atualiza e exclui produtos
usando persistência local com Room (SQLite).

## Tecnologias

- Kotlin
- Room 2.6.1 (SQLite)
- ViewBinding
- Corrotinas (lifecycleScope)

## Parte A: Complemento prático

### A1. Limpar campos após inserir

**O que foi feito:** depois que o `insertAll` termina com sucesso, os quatro campos (nome, preço, descrição e quantidade) são esvaziados com `text.clear()` e o cursor volta para o campo Nome com `requestFocus()`. Assim o usuário já pode cadastrar o próximo produto.

Trecho de código:

```kotlin
lifecycleScope.launch {
    productDao.insertAll(product)
    Toast.makeText(this@MainActivity, "Produto inserido!", Toast.LENGTH_SHORT).show()

    binding.editProductName.text.clear()
    binding.editProductPrice.text.clear()
    binding.editProductDescription.text.clear()
    binding.editProductQuantity.text.clear()
    binding.editProductName.requestFocus()
}
```

### A2. Validação de preço

**O que foi feito:** o texto do preço é convertido com `toDoubleOrNull()`. Se o resultado for `null` (campo vazio ou texto inválido) ou menor ou igual a zero, o app mostra o erro no próprio campo e interrompe o salvamento com `return@setOnClickListener`.

Trecho de código:

```kotlin
val price = priceText.toDoubleOrNull()
if (price == null || price <= 0.0) {
    binding.editProductPrice.error = "Informe um preço válido"
    return@setOnClickListener
}
```

### A3. Lista na tela

**O que foi feito:** foi adicionado um `TextView` (`textResultado`) abaixo dos botões. Ao clicar em Listar Produtos, a função `mostrarNaTela()` monta uma linha por produto (id, nome, preço, quantidade e descrição) e exibe o texto no `TextView`. O Logcat continua recebendo a listagem também. O layout está dentro de um `ScrollView` para que listas longas possam ser roladas.

Trecho de código:

```kotlin
val linhas = products.joinToString("\n") {
    "ID ${it.id} | ${it.name} | ${formatarPreco(it.price)} | " +
        "Qtd: ${it.quantity} | ${it.description.orEmpty()}"
}
binding.textResultado.text = linhas
```

### A4. Total do estoque

**O que foi feito:** junto da lista, o app exibe a quantidade de produtos cadastrados (`products.size`) e a soma dos preços (`products.sumOf { it.price }`). Fui um pouco além e também mostro o total de unidades e o valor real do estoque, que multiplica o preço pela quantidade de cada produto. A soma simples dos preços não diz quanto vale o estoque: um produto de R$ 10,00 com 50 unidades pesa mais que um de R$ 100,00 com 1.

Trecho de código:

```kotlin
val total = products.sumOf { it.price }
val valorEstoque = products.sumOf { it.price * it.quantity }
val unidades = products.sumOf { it.quantity }
binding.textResultado.text = buildString {
    append(linhas)
    append("\n\n")
    append("Produtos cadastrados: ${products.size}\n")
    append("Soma dos preços: ${formatarPreco(total)}\n")
    append("Unidades em estoque: $unidades\n")
    append("Valor total em estoque: ${formatarPreco(valorEstoque)}")
}
```

### A5. Buscar por nome

**O que foi feito:** foi criada no DAO a função `buscarPorNome` com `LIKE`. Na tela há um campo de busca e o botão Buscar. O texto digitado é envolvido por `%` para encontrar nomes parecidos (por exemplo, "tec" encontra "Teclado").

Trecho de código:

```kotlin
// ProductDao.kt
@Query("SELECT * FROM product WHERE name LIKE :nome")
suspend fun buscarPorNome(nome: String): List<Product>

// MainActivity.kt
binding.btnBuscar.setOnClickListener {
    val texto = binding.editBusca.text.toString().trim()
    lifecycleScope.launch {
        mostrarNaTela(productDao.buscarPorNome("%$texto%"))
    }
}
```

### A6. Excluir produtos

**O que foi feito:** foram criados dois recursos. O botão Excluir Todos usa `@Query("DELETE FROM product")`. Como desafio extra, o botão Excluir por ID busca o produto com `getById` e o remove com `@Delete`. Depois da exclusão a lista é atualizada na tela.

Trecho de código:

```kotlin
// ProductDao.kt
@Query("DELETE FROM product")
suspend fun deleteAll()

@Delete
suspend fun delete(product: Product)

@Query("SELECT * FROM product WHERE id = :id LIMIT 1")
suspend fun getById(id: Int): Product?

// MainActivity.kt (excluir por id)
val produto = productDao.getById(id)
if (produto != null) {
    productDao.delete(produto)
    mostrarNaTela(productDao.getAll())
}
```

### A7. Atualizar produto

**O que foi feito:** o usuário informa o ID e o novo preço e clica em Atualizar Preço. O app busca o produto, cria uma cópia com o preço novo (`copy(price = novoPreco)`, já que `Product` é uma `data class` com `val`) e a grava com `@Update`. O novo preço passa pela mesma validação do A2.

Trecho de código:

```kotlin
// ProductDao.kt
@Update
suspend fun update(product: Product)

// MainActivity.kt
val produto = productDao.getById(id)
if (produto != null) {
    productDao.update(produto.copy(price = novoPreco))
    mostrarNaTela(productDao.getAll())
}
```

### A8. Novo campo na tabela (quantity)

**O que foi feito:** foi adicionado `quantity: Int` à entidade `Product` (valor padrão 0), um campo "Quantidade em estoque" no layout e a leitura desse campo na inserção. No `@Database`, a `version` passou de 1 para 2.

Trecho de código:

```kotlin
// Product.kt
@ColumnInfo(name = "quantity") val quantity: Int = 0

// AppDatabase.kt
@Database(entities = [Product::class], version = 2)
```

**O que aconteceu com os produtos já cadastrados e por quê:** eles foram apagados. Ao abrir o app com a versão 2, o Room percebeu que o banco do aparelho estava na versão 1 e que o esquema mudou (a tabela ganhou uma coluna). Como não existe uma `Migration` de 1 para 2 e o banco foi criado com `fallbackToDestructiveMigration()`, o Room apaga as tabelas antigas e cria tudo de novo no formato novo. Por isso a lista aparece vazia depois da atualização. Sem o `fallbackToDestructiveMigration()` e sem uma `Migration`, o app fecharia com uma exceção (`IllegalStateException: A migration from 1 to 2 was required but not found`).

## Melhorias extras

Além do que a atividade pedia, fiz alguns ajustes que achei que faziam sentido num app de verdade:

- **Banco como Singleton:** em vez de criar o banco dentro da `MainActivity`, o `AppDatabase` tem um `getInstance()` com `@Volatile` e `synchronized`. É exatamente o que expliquei na B7, então preferi já deixar implementado.
- **Confirmação antes de "Excluir Todos":** um `AlertDialog` pergunta se o usuário tem certeza, porque um toque sem querer apagaria o cadastro inteiro.
- **Preço com vírgula:** o teclado em português costuma digitar `10,50`, e o `toDoubleOrNull()` não entende vírgula. A função `lerPreco()` troca a vírgula por ponto antes de converter, e os campos de preço aceitam os dois.
- **Lista em ordem alfabética:** `getAll` e `buscarPorNome` usam `ORDER BY name COLLATE NOCASE`.
- **Moeda formatada com `NumberFormat`:** os valores aparecem como `R$ 1.234,50`, no padrão brasileiro.
- **Tag de log própria:** troquei `"TAG"` por uma constante `TAG = "AppRoomProduto"`, o que facilita filtrar no Logcat.

```kotlin
// AppDatabase.kt
companion object {
    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getInstance(context: Context): AppDatabase =
        INSTANCE ?: synchronized(this) {
            INSTANCE ?: Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "database-name"
            )
                .fallbackToDestructiveMigration()
                .build()
                .also { INSTANCE = it }
        }
}
```

## Parte B: Questões dissertativas

### B1. O que é persistência de dados? O que aconteceria com os produtos se fossem guardados apenas em uma MutableList dentro da MainActivity?

**Resposta:** Persistência de dados é a capacidade de uma aplicação guardar informações em um meio de armazenamento permanente, como um arquivo ou um banco de dados, para que elas continuem existindo depois que o app é fechado, o celular é reiniciado ou o processo é encerrado pelo sistema. No meu projeto, os produtos ficam gravados no arquivo SQLite `database-name`, dentro do armazenamento privado do app, e por isso, quando fecho o app e clico em Listar Produtos de novo, os produtos continuam lá. Se em vez do Room eu usasse uma `MutableList` dentro da `MainActivity`, os dados viveriam apenas na memória RAM, associados àquela instância da Activity. Ao fechar o app, ou até ao girar a tela (a Activity é recriada), a lista voltaria vazia e todos os produtos seriam perdidos. Uma lista em memória serve para guardar estado temporário, mas não para dados que o usuário espera encontrar depois, como um cadastro de estoque.

### B2. Diferença entre usar o SQLite diretamente e usar o Room. Cite pelo menos duas vantagens do Room.

**Resposta:** Com o SQLite direto, usando `SQLiteOpenHelper`, preciso escrever na mão os comandos `CREATE TABLE`, `INSERT`, `SELECT` etc. em forma de texto, converter manualmente cada linha do `Cursor` em objeto e cuidar do ciclo de abrir e fechar o banco. Isso gera muito código repetitivo e propenso a erros. O Room é uma camada sobre o SQLite que automatiza grande parte disso: eu descrevo a tabela com uma `@Entity`, os comandos em um `@Dao` e o Room gera o código real na compilação. Uma vantagem é a verificação das consultas SQL em tempo de compilação: se eu errar o nome de uma coluna em um `@Query`, o build falha em vez de o app quebrar na execução. Outra vantagem é o menor volume de código, porque anotações como `@Insert`, `@Update` e `@Delete` dispensam o SQL, e o Room converte o resultado direto em `List<Product>`. Há ainda o suporte nativo a corrotinas (`suspend`), que ajuda a não bloquear a tela, e o suporte a migrações de esquema.

### B3. Papel de Entity, DAO e Database e como se relacionam no projeto.

**Resposta:** A `Entity` descreve a estrutura dos dados. No projeto, a classe `Product` anotada com `@Entity` vira a tabela `product`, e cada atributo (`id`, `name`, `price`, `description`, `quantity`) vira uma coluna. O `DAO` (Data Access Object) é a interface `ProductDao`, que reúne as operações possíveis sobre os dados: `getAll`, `insertAll`, `buscarPorNome`, `update`, `delete` e `deleteAll`. Ele diz o que pode ser feito com a tabela, e o Room implementa essas funções. O `Database` é a classe abstrata `AppDatabase`, anotada com `@Database`, que junta tudo: lista as entidades (`entities = [Product::class]`), define a versão e expõe os DAOs pela função `productDao()`. Na `MainActivity`, o `Room.databaseBuilder` cria o `AppDatabase`, dele obtenho o `ProductDao` e é por meio dele que o app lê e grava objetos `Product`. Resumindo: a Entity é o modelo, o DAO são as operações e o Database é a ponte que cria o arquivo e conecta as duas peças.

### B4. Por que o Android não permite acessar o banco na thread principal? O que é ANR? Como `suspend` e `lifecycleScope.launch` resolvem?

**Resposta:** A thread principal (UI thread) é a responsável por desenhar a tela e tratar os toques do usuário. Acesso a banco de dados envolve leitura e escrita em disco, que podem demorar. Se isso acontecesse na UI thread, a tela ficaria congelada enquanto a operação não terminasse, e por isso o Room lança o erro "Cannot access database on the main thread". Quando a thread principal fica bloqueada por tempo demais (cerca de cinco segundos para eventos de entrada), o Android mostra o ANR (Application Not Responding), a caixa de diálogo "o app não está respondendo", oferecendo fechar o aplicativo. No meu projeto, as funções do DAO são marcadas com `suspend`, o que permite ao Room executá-las fora da thread principal e suspender a corrotina até o resultado chegar, sem travar a interface. O `lifecycleScope.launch` abre a corrotina atrelada ao ciclo de vida da Activity: o trabalho acontece em segundo plano, o código continua de forma sequencial e legível depois do retorno (por exemplo, atualizando o `TextView`) e, se a Activity for destruída, a corrotina é cancelada automaticamente, evitando vazamentos e falhas.

### B5. O que faz `fallbackToDestructiveMigration()`? Por que é perigoso na Play Store? O que seria uma Migration?

**Resposta:** O `fallbackToDestructiveMigration()` diz ao Room o que fazer quando o banco existente no aparelho tem uma versão diferente da que o código espera e não há uma migração definida: ele apaga todas as tabelas e as recria vazias no esquema novo. Isso é conveniente durante o desenvolvimento, porque evita que o app quebre a cada mudança na entidade, como aconteceu no A8, quando os produtos antigos sumiram. Em um app publicado é perigoso porque, ao atualizar o aplicativo, todos os usuários perderiam os dados que cadastraram (produtos, histórico, registros), o que seria uma perda irrecuperável e geraria avaliações negativas. Uma `Migration` é uma classe que ensina o Room a converter o banco de uma versão para outra preservando os dados, por exemplo `Migration(1, 2)` executando `ALTER TABLE product ADD COLUMN quantity INTEGER NOT NULL DEFAULT 0`. Ela é registrada com `.addMigrations(...)` no `databaseBuilder`. Com ela, os produtos já cadastrados continuariam existindo, apenas com o novo campo preenchido com o valor padrão.

### B6. Vantagem do ViewBinding sobre `findViewById`. Que tipo de erro ele evita?

**Resposta:** O ViewBinding gera automaticamente uma classe (no projeto, `ActivityMainBinding`) com uma referência direta a cada componente do layout que tem `id`, de modo que acesso `binding.editProductName` em vez de chamar `findViewById(R.id.editProductName)`. A principal vantagem é a segurança de tipos: cada propriedade já tem o tipo correto (`EditText`, `Button`), sem necessidade de conversão manual. Também há segurança contra nulos: o binding só contém views que existem no layout, então não ocorre o `NullPointerException` causado por um id que não está presente no layout carregado. Com `findViewById`, é possível errar o id, usar um id de outro layout ou fazer o cast para o tipo errado, e esses erros só aparecem quando o app roda, normalmente com o app fechando. Com o ViewBinding, esses problemas viram erros de compilação, que são encontrados e corrigidos mais cedo. O código também fica mais curto e legível.

### B7. Por que em apps maiores o banco deve ser Singleton? O que poderia acontecer se várias telas criassem cada uma o seu banco?

**Resposta:** Criar uma instância de `AppDatabase` é uma operação cara: abre o arquivo, configura conexões e pools de threads. Por isso a recomendação é manter uma única instância para o app inteiro, normalmente com o padrão Singleton, por exemplo em um `companion object` com `@Volatile` e `synchronized`, ou com injeção de dependência. Se cada tela criasse o seu próprio banco, haveria várias conexões abertas para o mesmo arquivo, o que desperdiça memória e processamento e pode causar problemas de concorrência, como conflitos de escrita e erros de "database is locked". Além disso, as telas deixariam de ser notificadas das mudanças feitas por outra instância (por exemplo, o uso de `Flow`/`LiveData` que observam alterações depende de a instância ser a mesma) e, quando o esquema muda, as instâncias poderiam se comportar de forma inconsistente. No tutorial o banco era criado direto na `MainActivity` porque o app tem só uma tela e é um exemplo didático. Mesmo assim, neste projeto mudei para um `getInstance()` no próprio `AppDatabase` (ver Melhorias extras), que é como eu faria num app real.

### B8. Reflexão pessoal: dificuldades e um app real onde o Room seria útil.

**Resposta:** A parte que mais me tomou tempo foi deixar o projeto compilando. Sem o plugin `kotlin-kapt` e o `viewBinding = true` no Gradle, o `ActivityMainBinding` simplesmente não existe e o Android Studio enche a tela de erro, e só depois de sincronizar e dar Rebuild é que ficou claro que o problema era configuração, não código. Outra coisa que demorei a entender foi o motivo de todas as funções do DAO serem `suspend`. Na prática isso me obrigou a colocar cada chamada ao banco dentro de um `lifecycleScope.launch`, e aí fez sentido o erro "Cannot access database on the main thread". Também me pegou de surpresa o A8: subi a versão do banco e os produtos sumiram, e só entendi lendo sobre o `fallbackToDestructiveMigration()`.

Testando o app, apareceram problemas que o roteiro não citava. O teclado do celular digitava o preço com vírgula (`10,50`) e o app recusava como inválido, então criei a função `lerPreco()` para aceitar os dois formatos. Percebi também que a "soma dos preços" não dizia quanto o estoque vale, porque ignora a quantidade, e acrescentei o cálculo de preço vezes quantidade. Por fim, coloquei uma confirmação no "Excluir Todos", porque um toque sem querer apagava tudo, e mudei a criação do banco para um Singleton, seguindo o que respondi na B7.

Um app real em que o Room seria útil é o controle de pedidos de uma lanchonete pequena. O atendente registra os pedidos no celular mesmo quando a internet cai, e no fim do dia consulta quanto vendeu e quais itens saíram mais. Eu usaria uma entidade `Item` (nome e preço, parecida com o `Product` daqui) e uma `Pedido` ligada a ela, com um DAO para registrar pedidos e consultas de total por dia. E, diferente deste projeto, usaria uma `Migration` de verdade a cada versão nova, para não apagar o histórico de vendas.

## Como executar

1. No Android Studio, crie um projeto **Empty Views Activity** em Kotlin com o pacote `br.universidade.approomproduto` (API mínima 24).
2. Copie a pasta `app/src/main/java/br/universidade/approomproduto` e o `activity_main.xml` deste repositório para o projeto.
3. Adicione ao `build.gradle.kts` do módulo `:app` os trechos de `gradle-trechos.kts.txt` (kapt, viewBinding e Room) e clique em **Sync Now**.
4. No `AndroidManifest.xml`, a activity deve apontar para `.view.MainActivity`.
5. Execute em um emulador ou celular.
