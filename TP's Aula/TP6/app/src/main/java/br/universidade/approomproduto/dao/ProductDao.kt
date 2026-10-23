package br.universidade.approomproduto.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import br.universidade.approomproduto.model.Product

@Dao
interface ProductDao {

    // suspend: o Room não permite consultas na main thread
    @Query("SELECT * FROM product ORDER BY name COLLATE NOCASE")
    suspend fun getAll(): List<Product>

    @Insert
    suspend fun insertAll(vararg products: Product)

    // A5: busca por nome (use "%texto%" para nomes parecidos)
    @Query("SELECT * FROM product WHERE name LIKE :nome ORDER BY name COLLATE NOCASE")
    suspend fun buscarPorNome(nome: String): List<Product>

    // A6: excluir todos
    @Query("DELETE FROM product")
    suspend fun deleteAll()

    // A6 (desafio): excluir um produto específico
    @Delete
    suspend fun delete(product: Product)

    // A7: atualizar produto
    @Update
    suspend fun update(product: Product)

    // Auxiliar para A6/A7: buscar produto pelo id
    @Query("SELECT * FROM product WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): Product?
}
