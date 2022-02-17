import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.core.view.isVisible
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class MainActivity2 : AppCompatActivity(){
    var value : Int =  0
    var map : HashMap<View, Int> = HashMap()
    var index :Int = 0
    lateinit var relativeView : RelativeLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        supportActionBar?.hide()
        val add : ImageView = findViewById<ImageView>(R.id.add)
        val rate : ImageView = findViewById<ImageView>(R.id.rating)
        val linearView : LinearLayout = findViewById(R.id.insideScrollView)
        relativeView = findViewById(R.id.mainRelativeLayout)
        //adding card
        add.setOnClickListener{
            TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { view, hoursOfDay, minute ->
                addCard(linearView, hoursOfDay, minute)
            },12,0, false).show()
        }
        //adding rating
        rate.setOnClickListener{
            val view : View = layoutInflater.inflate(R.layout.rating_bar, null)
            view.isVisible = false

            relativeView.addView(view)
            view.isVisible = true
            val done : Button = view.findViewById(R.id.ratingDone)
            done.setOnClickListener{
                relativeView.removeView(view)
            }
        }
    }
    fun addCard(layout: LinearLayout, hours:Int, minute : Int){
        var view : View = layoutInflater.inflate(R.layout.alarm_card,null)
        map.put(view, index++)
        var textview : TextView = view.findViewById(R.id.textAlarm)//update the time of that particular view
        textview.setText("$hours : $minute")
        layout.addView(view)

        //custom toast
            var view2: View = layoutInflater.inflate(R.layout.custom_toast, null)
            var toast: Toast = Toast(this@MainActivity2)
            toast.duration = Toast.LENGTH_LONG
            toast.view = view2
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0)
            toast.show()

        //timer start
        val intent  :Intent = Intent(this@MainActivity2, AlarmManager2::class.java)
        val pending : PendingIntent = PendingIntent.getBroadcast(applicationContext, value++,intent,0)
        Toast.makeText(this, "value :  $value", Toast.LENGTH_SHORT).show()
        val alarmanager : AlarmManager= getSystemService(Context.ALARM_SERVICE)as AlarmManager
        //time
        val simpleDateFormat = SimpleDateFormat("HH:mm:ss")
        val currentDateAndTime: String = simpleDateFormat.format(Date())
        //converion to int
        //hour
        var hourValue : Long  = (((currentDateAndTime[0]-'0')*10)+((currentDateAndTime[1])-'0') ).toLong()
        //minute
        var minuteValue : Long = (((currentDateAndTime[3]-'0')*10)+((currentDateAndTime[4])-'0')).toLong()
        //second
        var secondValue : Long = (((currentDateAndTime[6]-'0')*10)+((currentDateAndTime[7])-'0')).toLong()
        //diff
        var hourDiff : Long = Math.abs(hourValue-hours)
        var minuteDiff : Long = Math.abs(minuteValue-minute)
//        var secondDiff : Long = Math.abs(secondValue-)
        //time
        var hoursInmilli : Long = hourDiff*60*60*1000;
        var minuteInmilli : Long = minuteDiff*60*1000;
        var secondInmilli : Long = secondValue*1000;
        //setting alarm manager
        //////////////////////////////////////////////
        println("system time "+currentDateAndTime)
        println("hour value "+hourValue)
        println("minute value "+minuteValue)
        println("hour diff "+hourDiff)
        println("minute diff "+minuteDiff)
        //////////////////////////////////////////////
        var realTime : Long = hoursInmilli+minuteInmilli
        println("The real Time $realTime")
        println(realTime+System.currentTimeMillis())
        alarmanager.setExact(AlarmManager.RTC_WAKEUP,System.currentTimeMillis()+realTime,pending)
    }
    class AlarmManager2 : BroadcastReceiver(){
        override fun onReceive(context : Context, intent : Intent?) {
            Toast.makeText(context, "Alarm Ringing", Toast.LENGTH_LONG).show()
            var mu = MediaPlayer.create(context, R.raw.alarm)
            mu.start()

        }
    }
}
