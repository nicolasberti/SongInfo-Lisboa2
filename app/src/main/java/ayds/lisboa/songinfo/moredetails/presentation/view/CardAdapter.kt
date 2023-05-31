package ayds.lisboa.songinfo.moredetails.presentation.view

import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ayds.lisboa.songinfo.R
import ayds.lisboa.songinfo.moredetails.domain.entities.Card
import ayds.lisboa.songinfo.utils.UtilsInjector
import com.squareup.picasso.Picasso

class CardAdapter(
    private val otherInfoView: OtherInfoView,
    private val cardItems: List<Card>
) : RecyclerView.Adapter<CardAdapter.CardViewHolder>() {

    inner class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textMoreDetails: TextView = itemView.findViewById(R.id.textMoreDetails)
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val textSource: TextView = itemView.findViewById(R.id.textSource)
        val urlButton: Button = itemView.findViewById(R.id.openUrlButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_item, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val card = cardItems[position]
        setTextView(holder.textSource, card.source.name)
        setTextViewHtml(holder.textMoreDetails, card.description)
        updateListenerUrl(holder.urlButton, card.infoUrl)
        setImageView(holder.imageView, card.sourceLogoUrl)
    }
    override fun getItemCount(): Int {
        return cardItems.size
    }
    private fun setTextView(textView: TextView, text: String){
        textView.text = text
    }
    @Suppress("DEPRECATION")
    private fun setTextViewHtml(textView: TextView, text: String){
        textView.text = Html.fromHtml(text)
    }

    private fun updateListenerUrl(button: Button, url: String) {
        button.setOnClickListener { UtilsInjector.navigationUtils.openExternalUrl(otherInfoView, url) }
    }

    private fun setImageView(imageView: ImageView, image: String){
        val picasso =  Picasso.get()
        val requestCreator = picasso.load(image)
        requestCreator.into(imageView)
    }
}