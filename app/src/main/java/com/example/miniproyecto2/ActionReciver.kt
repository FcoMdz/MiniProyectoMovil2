import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class ActionReciver: BroadcastReceiver(){
    override fun onReceive(context: Context?, intent: Intent?){
        if(intent?.action=="PLAY_ACTION"){
            Toast.makeText(context, "Reproducir presionado", Toast.LENGTH_SHORT
            )
        }
    }
}