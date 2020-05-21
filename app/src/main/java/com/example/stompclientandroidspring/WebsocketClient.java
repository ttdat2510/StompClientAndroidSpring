package com.example.stompclientandroidspring;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.StompHeader;

public class WebsocketClient {
  String WEBSOCKET_CONNECT_URL = "ws://5cdecd96.ngrok.io/ws/websocket";
  String WEBSOCKET_TOPIC = "/notification/VID1001";

  CompositeDisposable compositeDisposable;
  private StompClient mStompClient;

  private static final String TAG = "WebsocketClient";

  public void connectWebSocket() {
    Log.d(TAG, "connectWebSocket: ");
    compositeDisposable = new CompositeDisposable();
    mStompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, WEBSOCKET_CONNECT_URL);


    List<StompHeader> headers = new ArrayList<>();
    headers.add(new StompHeader("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ2aWQiOiJWSUQxMDAxIiwic3ViIjoiMTIzIiwiaWF0IjoxNTkwMDM0MTI5LCJleHAiOjE1OTAyMDY5Mjl9.8ipq0upyJIKGmAsOyOrW0YtzpQnUw3Yde-NIpZbMMaI"));
    mStompClient.connect(headers);

    Disposable lifecycle = mStompClient.lifecycle().subscribe(lifecycleEvent -> {
      switch (lifecycleEvent.getType()) {
        case OPENED:
          Log.d(TAG, "Stomp Connection Opened");
          break;
        case ERROR:
          Log.d(TAG, "Error ", lifecycleEvent.getException());
          break;
        case CLOSED:
          Log.d(TAG, "Stomp Connection Closed");

          break;
        case FAILED_SERVER_HEARTBEAT:
          Log.d(TAG, "Failed Server Heartbeat ");
          break;
      }
    });
    if (!mStompClient.isConnected()) {
      mStompClient.connect();
    }

    Disposable topic = mStompClient.topic(WEBSOCKET_TOPIC).subscribe(stompMessage -> {
      Log.d(TAG, stompMessage.getPayload());
      // Do your code here when ever you receive data from server.
    }, throwable -> Log.d(TAG, throwable.getMessage().toString() + ""));
    compositeDisposable.add(lifecycle);
    compositeDisposable.add(topic);
  }
}
//
//  public static void connectSocket() {
//    if (mStompClient != null) {
//      if (!mStompClient.isConnected()) {
//        connectWebSocket();
//      }
//    }
//  }
//
//    public static void disconnectSocket () {
//      if (mStompClient != null) {
//        if (mStompClient.isConnected()) {
//          mStompClient.disconnect();
////                compositeDisposable.dispose();
//        }
//      }
//
//    }
//  }
