package com.elazar.app

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.elazar.app.databinding.ActivityStoreBinding
import com.elazar.app.databinding.ItemPromoBinding

/**
 * Vista 6. Tienda.
 *
 * El jugador gasta su saldo en artículos y cosméticos y consulta las promociones.
 */
class StoreActivity : BaseActivity() {

    private lateinit var binding: ActivityStoreBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar(binding.toolbar)

        binding.textBalance.text = money(balance)

        binding.recyclerArticles.layoutManager = LinearLayoutManager(this)
        binding.recyclerArticles.adapter = ArticleAdapter(articles()) { buy(it) }

        setupPromotions()
    }

    private fun articles(): List<Article> = listOf(
        Article(1, 120.0, R.drawable.ic_game_dice),
        Article(2, 180.0, R.drawable.ic_game_shooter),
        Article(3, 240.0, R.drawable.ic_game_wheel),
        Article(4, 320.0, R.drawable.ic_game_poker)
    )

    private fun setupPromotions() {
        val promos = resources.getStringArray(R.array.promotions)
        val inflater = LayoutInflater.from(this)
        binding.layoutPromos.removeAllViews()
        promos.forEach { promo ->
            val row = ItemPromoBinding.inflate(inflater, binding.layoutPromos, false)
            row.textPromo.text = promo
            binding.layoutPromos.addView(row.root)
        }
    }

    /** Compra simulada: descuenta el saldo si alcanza. */
    private fun buy(article: Article) {
        if (balance < article.price) {
            toast(getString(R.string.store_error_balance))
            return
        }
        balance -= article.price
        binding.textBalance.text = money(balance)
        toast(getString(R.string.store_ok, getString(R.string.store_article, article.index)))
    }

    private fun toast(message: String) =
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}
