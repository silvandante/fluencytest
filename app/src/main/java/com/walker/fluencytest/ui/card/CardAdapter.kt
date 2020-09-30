package com.walker.fluencytest.ui.card

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.app.DownloadManager
import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.viewpager.widget.PagerAdapter
import com.walker.fluencytest.R
import com.walker.fluencytest.data.models.cards.dto.CardDataDto
import com.walker.fluencytest.utils.NonSwipeableViewPager
import com.walker.fluencytest.utils.OnSwipeTouchListener
import kotlinx.android.synthetic.main.card_item.view.*
import rm.com.audiowave.AudioWaveView
import rm.com.audiowave.OnProgressListener
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt


class CardAdapter(
    private val context: Context,
    private val cardModelArrayList: List<CardDataDto>,
    var scale: Float,
    var viewPager: NonSwipeableViewPager,
    var progressBar: ProgressBar,
    var percentageText: TextView,
    var manyToGoText: TextView,
    val downloadmanager: DownloadManager
) : PagerAdapter()  {

    lateinit var front_anim: AnimatorSet
    lateinit var back_anim: AnimatorSet
    lateinit var container: ViewGroup
    var currentPosition = 0


    override fun getCount(): Int {
        return cardModelArrayList.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        this.container = container


        var listenedToAudio = false

        Log.d("VIEW_COUNT:", position.toString())


        var isFront = false

        var mediaPlayer = MediaPlayer()
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)

        var view = LayoutInflater.from(context).inflate(R.layout.card_item, container, false)!!

        //get data color: #FC2F3B
        val model: CardDataDto = cardModelArrayList[position]

        val splittedField = model.notes[0]?.field?.split("<br><")!!

        val fieldFrente: String

        fieldFrente = if (splittedField[0].length==17){
            model.notes[0]!!.field.split("</audio><br>")[1]
        } else {
            splittedField[0]
        }

        val splittedField2 = model.notes[1]?.field?.split("<br><")!!

        val fieldVerso: String = if (splittedField2[0]?.length==17){
            model.notes[1]!!.field.split("</audio><br>")[1]
        } else {
            splittedField2[0]
        }


        view.cardFront.cameraDistance = 8000 * scale
        view.cardBack.cameraDistance = 8000 * scale

        front_anim = AnimatorInflater.loadAnimator(context, R.animator.front_animator) as AnimatorSet
        back_anim = AnimatorInflater.loadAnimator(context, R.animator.back_animator) as AnimatorSet
        //set data to view
        view.fieldFrente.text = fieldFrente
        view.fieldFrente2.text = fieldFrente
        view.fieldVerso.text = fieldVerso

        view.setOnTouchListener(object : OnSwipeTouchListener(context) {
            override fun onSwipeRight() {
                if (isFront) {
                    view.cardFront.visibility = View.VISIBLE
                    front_anim.setTarget(view.cardBack)
                    back_anim.setTarget(view.cardFront)
                    front_anim.start()
                    back_anim.start()
                    isFront = false

                } else {
                    front_anim.setTarget(view.cardFront)
                    back_anim.setTarget(view.cardBack)
                    front_anim.start()
                    back_anim.start()
                    isFront = true
                    back_anim.doOnEnd {
                        if(isFront)
                            view.cardFront.visibility = View.GONE
                    }
                }
            }

            override fun onSwipeLeft() {
                if (!isFront) {
                    front_anim.setTarget(view.cardFront)
                    back_anim.setTarget(view.cardBack)
                    front_anim.start()
                    back_anim.start()
                    isFront = true
                    back_anim.doOnEnd {
                        if(isFront)
                            view.cardFront.visibility = View.GONE
                    }
                } else {
                    view.cardFront.visibility = View.VISIBLE
                    front_anim.setTarget(view.cardBack)
                    back_anim.setTarget(view.cardFront)
                    front_anim.start()
                    back_anim.start()
                    isFront = false
                }
            }
        })

        val downloadFolder = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)

        try {

            mediaPlayer.setDataSource(context, Uri.parse(downloadFolder?.path + model.pathAudio))

        } catch(e: Exception) {

            Toast.makeText(context, "Erro ao recuperar áudio. Fazendo download novamente...", Toast.LENGTH_SHORT).show()


            try{
                mediaPlayer.setDataSource(context, Uri.parse(model.originalUrlAudio))
            } catch (e: java.lang.Exception){
                Toast.makeText(context, "Erro! Você está sem internet?", Toast.LENGTH_SHORT).show()
            }

            val request = DownloadManager.Request(Uri.parse(model.originalUrlAudio))

            request.setDestinationInExternalFilesDir(
                context,
                Environment.DIRECTORY_DOWNLOADS,
                "/cards_"+model.card?.id+"_audio.mp3"
            )

            model.pathAudio = "/cards_"+count+"_audio.mp3"

            downloadmanager.enqueue(request)

        }


        mediaPlayer.setOnCompletionListener {
            listenedToAudio = true
            if(!isFront) {
                view.visualizer.waveColor = ContextCompat.getColor(context, R.color.pink)
                view.playButton.background.setTint(
                    ContextCompat.getColor(
                        context,
                        R.color.colorPrimary
                    )
                )
            } else {
                view.visualizer2.waveColor = ContextCompat.getColor(context, R.color.pink)
                view.playButton2.background.setTint(
                    ContextCompat.getColor(
                        context,
                        R.color.colorPrimary
                    )
                )
            }
        }

        try {
            val encoded = Files.readAllBytes(Paths.get(downloadFolder?.path + model.pathAudio))
            view.visualizer.setRawData(encoded)
            view.visualizer2.setRawData(encoded)

        } catch (e: IOException) {
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }

        mediaPlayer.setOnPreparedListener{ media ->
            val mediaPlayer_ = media

            val duration = media.duration

            val durationSeconds = TimeUnit.MILLISECONDS.toSeconds(duration.toLong())

            var hours = durationSeconds / 3600;
            var minutes = (durationSeconds % 3600) / 60;
            var seconds = durationSeconds % 60;

            var durationString = String.format("%02d:%02d:%02d", hours, minutes, seconds);

            view.audioSize2.text = durationString
            view.audioSize.text = durationString

            view.playButton.setOnClickListener{playButton ->
                if(mediaPlayer_.isPlaying)
                    mediaPlayer_.stop()
                if(playButton.id==R.id.playButton) {
                    try {
                        playButton.background.setTint(
                            ContextCompat.getColor(
                                context,
                                R.color.pink
                            )
                        )
                        view.visualizer.waveColor = ContextCompat.getColor(context, R.color.gray)
                        mediaPlayer_.start()
                        initializeAudioWaveVisualizerProgress(view.visualizer, mediaPlayer_)
                    } catch (e: Exception) {
                        var toast = Toast.makeText(
                            context,
                            "Error" + e.message.toString(),
                            Toast.LENGTH_SHORT
                        )
                        toast.show()
                    }
                }
            }

            view.playButton2.setOnClickListener{playButton2 ->
                if(mediaPlayer_.isPlaying)
                    mediaPlayer_.stop()
                if(isFront&&playButton2.id==R.id.playButton2) {
                    try {
                        playButton2.background.setTint(
                            ContextCompat.getColor(
                                context,
                                R.color.pink
                            )
                        )
                        initializeAudioWaveVisualizerProgress(view.visualizer2, mediaPlayer_)
                        view.visualizer2.waveColor = ContextCompat.getColor(context, R.color.gray)
                        mediaPlayer_.start()
                    } catch (e: Exception) {
                        var toast = Toast.makeText(
                            context,
                            "Error" + e.message.toString(),
                            Toast.LENGTH_SHORT
                        )
                        toast.show()
                    }
                }
            }

            view.findViewById<Button>(R.id.button_easy).setOnClickListener {
                if(listenedToAudio) {
                    view.findViewById<Button>(R.id.button_easy)
                        .setBackgroundResource(R.drawable.round_button_pressed)
                    view.findViewById<Button>(R.id.button_easy).isClickable = false
                    clickedInScoreButton(position,mediaPlayer)
                } else {
                    Toast.makeText(context, "Escute o áudio ao menos 1 vez", Toast.LENGTH_SHORT).show()
                }
            }

            view.findViewById<Button>(R.id.button_medium).setOnClickListener {
                if(listenedToAudio) {
                    view.findViewById<Button>(R.id.button_medium).setBackgroundResource(R.drawable.round_button_pressed)
                    view.findViewById<Button>(R.id.button_medium).isClickable = false
                    clickedInScoreButton(position,mediaPlayer)
                } else {
                    Toast.makeText(context, "Escute o áudio ao menos 1 vez", Toast.LENGTH_SHORT).show()
                }
            }

            view.findViewById<Button>(R.id.button_hard).setOnClickListener {
                if(listenedToAudio) {
                    view.findViewById<Button>(R.id.button_hard).setBackgroundResource(R.drawable.round_button_pressed)
                    view.findViewById<Button>(R.id.button_hard).isClickable = false
                    clickedInScoreButton(position,mediaPlayer)
                } else {
                    Toast.makeText(context, "Escute o áudio ao menos 1 vez", Toast.LENGTH_SHORT).show()
                }
            }

            view.findViewById<Button>(R.id.button_dont_remember).setOnClickListener {
                if(listenedToAudio) {
                    view.findViewById<Button>(R.id.button_dont_remember).setBackgroundResource(R.drawable.round_button_pressed)
                    view.findViewById<Button>(R.id.button_dont_remember).isClickable = false
                    clickedInScoreButton(position,mediaPlayer)
                } else {
                    Toast.makeText(context, "Escute o áudio ao menos 1 vez", Toast.LENGTH_SHORT).show()
                }
            }

        }

        mediaPlayer.prepareAsync()

        container.addView(view)

        return view
    }

    private fun initializeAudioWaveVisualizerProgress(visualizer: AudioWaveView, mediaPlayer: MediaPlayer){

        val handler = Handler()
        handler.postDelayed(object : Runnable{
            override fun run() {
                try{
                    visualizer.progress = (mediaPlayer.currentPosition / mediaPlayer.duration.toFloat()) * 100F
                    handler.postDelayed(this,10 )
                } catch (e: java.lang.Exception){
                    visualizer.progress = 0f
                }
            }

        },0)

    }

    fun clickedInScoreButton(position: Int, mediaPlayer: MediaPlayer) {
        //(view.background as GradientDrawable).setStroke(2, ContextCompat.getColor(context, R.color.black))
        if (position < cardModelArrayList.size - 1)
            nextViewPagerCard(mediaPlayer)
        else {
            Toast.makeText(
                context,
                "Parabéns! Você concluiu esse deck. ",
                Toast.LENGTH_SHORT
            ).show()
            val activity: CardActivity = context as CardActivity
            activity.finish()
        }
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    fun nextViewPagerCard(mediaPlayer: MediaPlayer){
        mediaPlayer.stop()
        viewPager.postDelayed({
            progressBar.progress = progressBar.progress + 1
            percentageText.text =
                ((progressBar.progress.toDouble() / cardModelArrayList.size) * 100).roundToInt()
                    .toString() + "%"
            manyToGoText.text =
                progressBar.progress.toString() + "/" + cardModelArrayList.size.toString()
            currentPosition += 1
            viewPager.currentItem = viewPager.currentItem + 1
        }, 750)
    }
}