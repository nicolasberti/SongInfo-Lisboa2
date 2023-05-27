import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ayds.lisboa.songinfo.R
import ayds.lisboa.songinfo.moredetails.domain.entities.Card
import ayds.lisboa.songinfo.moredetails.presentation.OtherInfoView
import ayds.lisboa.songinfo.utils.UtilsInjector
import com.squareup.picasso.Picasso

class CardAdapter(
    private val otherInfoView: OtherInfoView,
    private val cardItems: List<Card>
    ) : RecyclerView.Adapter<CardAdapter.CardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_item, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val card = cardItems[position]
        holder.textMoreDetails.text = card.description

        holder.textSource.text = card.source.name
        holder.urlButton.setOnClickListener { UtilsInjector.navigationUtils.openExternalUrl(otherInfoView, card.infoUrl) }


        val picasso =  Picasso.get()
        val requestCreator = picasso.load(card.sourceLogoUrl)
        requestCreator.into(holder.imageView)
    }

    override fun getItemCount(): Int {
        return cardItems.size
    }

    inner class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textMoreDetails: TextView = itemView.findViewById(R.id.textMoreDetails)
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val textSource: TextView = itemView.findViewById(R.id.textSource)
        val urlButton: Button = itemView.findViewById(R.id.openUrlButton)
    }
}