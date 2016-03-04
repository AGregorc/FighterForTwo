package si.proba;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;


public class Kugla extends Activity  {

    private static float y;
    private static float maxH;
    private static float maxW;
    private static float middleH;
    private static float middleW;
    private ImageView myPlane;
    private ImageView myBullet;
    private boolean bulletFlying;
    private static final float BSTART = 100;
    private static float bullX;

    final static long REFRESH_TIME = 10; // miliseconds
    final Handler handler = new Handler();
    Runnable runnable;
    Runnable bull;
    MotionEvent lastEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kugla);


        runnable = new Runnable() {
            @Override
            public void run() {
                // call your method
                changePosition(lastEvent);
                handler.postDelayed(this, REFRESH_TIME);
            }
        };


        myPlane =  (ImageView) findViewById(R.id.moj);
        myBullet = (ImageView) findViewById(R.id.bullet);
        myBullet.setVisibility(View.INVISIBLE);
        bulletFlying = false;

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        maxH = displaymetrics.heightPixels;
        maxW = displaymetrics.widthPixels;
        middleW = maxW/2;
        middleH = maxH/2;
        y = middleH-195;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getX() > middleW) {
            int action = event.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    handler.post(runnable);
                    lastEvent = event;
                    break;
                case MotionEvent.ACTION_MOVE:
                    lastEvent = event;
                    break;
                case MotionEvent.ACTION_UP:
                    handler.removeCallbacks(runnable);
                    break;
            }
        }
        else {
            //Log.i("BUM", "Izstrelili smo raketo");
            newBullet();
            bull = new Runnable() {
                @Override
                public void run() {
                    // call your method
                    changeBulletPosition();
                    handler.postDelayed(this, REFRESH_TIME);
                }
            };
            handler.post(bull);
        }
        return true;
    }

    public void newBullet() {
        if (!bulletFlying) {
            bulletFlying = true;
            myBullet.setY(y+120);
            bullX = BSTART;
            myBullet.setX(BSTART);
            myBullet.setVisibility(View.VISIBLE);
        }
    }

    public void changeBulletPosition() {
        bullX += 10;
        myBullet.setX(bullX);
        if (bullX > maxW) {
            myBullet.setVisibility(View.INVISIBLE);
            bullX = 100;
            bulletFlying = false;
            handler.removeCallbacks(bull);
        }
    }

    public void changePosition(MotionEvent event) {
        int temp = (int)event.getY();
        if (temp < middleH) {
            y -= 3;
        }
        else y += 3;
        if(y < -100) y = -100;
        else if(y > maxH-290) y = maxH-290;
        myPlane.setY(y);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_kugla, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}