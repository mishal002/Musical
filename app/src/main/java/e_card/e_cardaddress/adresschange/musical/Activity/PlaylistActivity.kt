package e_card.e_cardaddress.adresschange.musical.Activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import e_card.e_cardaddress.adresschange.musical.databinding.ActivityPlaylistBinding

class PlaylistActivity : AppCompatActivity() {
    lateinit var binding: ActivityPlaylistBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setTheme(R.style.Theme_Musical)
        binding = ActivityPlaylistBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.backBtn.setOnClickListener {
            onBackPressed()
        }
    }
}