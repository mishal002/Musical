package e_card.e_cardaddress.adresschange.musical.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import e_card.e_cardaddress.adresschange.musical.Activity.PlayerActivity
import e_card.e_cardaddress.adresschange.musical.Data_Class.Music
import e_card.e_cardaddress.adresschange.musical.Data_Class.formatDuration
import e_card.e_cardaddress.adresschange.musical.R
import e_card.e_cardaddress.adresschange.musical.databinding.MusicViewBinding

class MusicAdapter(private val context: Context, private val musicList: ArrayList<Music>) :
    RecyclerView.Adapter<MusicAdapter.MyHolde>() {

    class MyHolde(binding: MusicViewBinding) : RecyclerView.ViewHolder(binding.root) {

        val title = binding.songNameMV
        val album = binding.songAlbumMV
        val image = binding.imageMV
        val duration = binding.songDuration
        val root = binding.root
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolde {
        return MyHolde(MusicViewBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: MyHolde, position: Int) {

        holder.title.text = musicList[position].title
        holder.album.text = musicList[position].album
        holder.duration.text = formatDuration(musicList[position].duration)
        holder.root.setOnClickListener {

            var i = Intent(context, PlayerActivity::class.java)
            i.putExtra("index", position)
            i.putExtra("class", "MusicAdapter")
            context.startActivity(i)
        }

        Glide.with(context)
            .load(musicList[position].artUri)
            .apply(RequestOptions().placeholder(R.drawable.music_logo).centerCrop())
            .into(holder.image)
    }

    override fun getItemCount(): Int {
        return musicList.size
    }
}