package com.elazar.app

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.elazar.app.databinding.ItemArticleBinding

/**
 * Adaptador de la tienda: cada artículo muestra su precio y su botón de compra.
 */
class ArticleAdapter(
    private val items: List<Article>,
    private val onBuy: (Article) -> Unit
) : RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>() {

    inner class ArticleViewHolder(val binding: ItemArticleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(article: Article) {
            val context = binding.root.context
            binding.textArticleName.text = context.getString(R.string.store_article, article.index)
            binding.textArticlePrice.text = context.getString(R.string.store_price, article.price.toInt())
            binding.imageArticle.setImageResource(article.iconRes)
            binding.buttonBuy.setOnClickListener { onBuy(article) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val binding = ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ArticleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) = holder.bind(items[position])

    override fun getItemCount(): Int = items.size
}
