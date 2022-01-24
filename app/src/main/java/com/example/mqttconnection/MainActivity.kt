package com.example.mqttconnection

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*

class MainActivity : AppCompatActivity() {

    private lateinit var clientId:String
    private lateinit var topic:String
    private lateinit var client: MqttAndroidClient
    private val TAG ="MY TAG"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mqttinit()

    }

    //http://www.hivemq.com/demos/websocket-client/
    private fun mqttinit() {
        clientId = MqttClient.generateClientId();
        topic = "testtopic/1"
        client = MqttAndroidClient(
            this.applicationContext, "tcp://broker.hivemq.com:1883",
            clientId
        )

        button.setOnClickListener {
            connect()
        }
    }
    private fun connect(){
        try {
            val token: IMqttToken = client.connect()
            token.actionCallback = object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken) {
                    // We are connected
                    Log.d(TAG, "onSuccess")
                    sub()
                }

                override fun onFailure(asyncActionToken: IMqttToken, exception: Throwable) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    Log.d(TAG, "onFailure")
                }
            }
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }

    private fun sub() {

        try {
            client.subscribe(topic,0)
            client.setCallback(object:MqttCallback{
                override fun connectionLost(cause: Throwable?) {

                }

                override fun messageArrived(topic: String?, message: MqttMessage?) {
                    Log.d(TAG,"$topic")
                    Log.d(TAG, "msg: + ${message?.payload.toString()}")
                }

                override fun deliveryComplete(token: IMqttDeliveryToken?) {

                }

            })
        }catch (e:MqttException){

        }

    }


}