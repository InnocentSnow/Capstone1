package com.mango.vr.vrplayer.vr;

import android.os.Bundle;

import com.google.vr.sdk.base.GvrActivity;
import com.google.vr.sdk.base.GvrView;
import com.mango.vr.vrplayer.R;
import com.mango.vr.vrplayer.data.PhotoQueue;
import com.mango.vr.vrplayer.network.Network;
import com.mango.vr.vrplayer.network.Receiver;

public class MainActivity extends GvrActivity
{
    private PhotoQueue mPhotoQueue = new PhotoQueue();
    private Network mNetwork = new Network(mPhotoQueue);
    private VRRenderer mRenderer = new VRRenderer(this, mPhotoQueue, mNetwork);

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_camera);

        GvrView gvrView = (GvrView)findViewById(R.id.gvr_view);
        gvrView.setRenderer(mRenderer);
        gvrView.setTransitionViewEnabled(true);
        gvrView.setOnCardboardBackButtonListener(new Runnable()
        {
            @Override
            public void run()
            {
                onBackPressed();
            }
        });

        setGvrView(gvrView);
        mNetwork.connect();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        mNetwork.disconnect();
    }
}
