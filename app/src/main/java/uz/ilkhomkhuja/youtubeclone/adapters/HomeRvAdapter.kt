package uz.ilkhomkhuja.youtubeclone.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import uz.ilkhomkhuja.youtubeclone.databinding.ItemVideoBinding
import uz.ilkhomkhuja.youtubeclone.models.Item

class HomeRvAdapter(
    private val videoList: List<Item>,
    var itemClickListener: OnItemClickListener
) : RecyclerView.Adapter<HomeRvAdapter.VideoVh>() {

    inner class VideoVh(private var itemVideoBinding: ItemVideoBinding) :
        RecyclerView.ViewHolder(itemVideoBinding.root) {
        fun onBind(item: Item) {
            Picasso.get().load(item.snippet.thumbnails.high.url).into(itemVideoBinding.image)
            itemVideoBinding.title.text = item.snippet.title

            itemVideoBinding.root.setOnClickListener {
                itemClickListener.onItemClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoVh {
        return VideoVh(ItemVideoBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: VideoVh, position: Int) {
        holder.onBind(videoList[position])
    }

    override fun getItemCount(): Int {
        return videoList.size
    }

    interface OnItemClickListener {
        fun onItemClick(item: Item)
    }
}