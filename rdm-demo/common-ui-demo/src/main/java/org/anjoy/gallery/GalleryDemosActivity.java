package org.anjoy.gallery;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.rdm.common.ui.demo.R;

public class GalleryDemosActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        int [] images={R.drawable.image01,R.drawable.image02,R.drawable.image03,R.drawable.image04,R.drawable.image05};
        ImageAdapter adapter = new ImageAdapter(this, images);
        adapter.createReflectedImages();
        GalleryFlow galleryflow =(GalleryFlow) findViewById(R.id.gallery);
        galleryflow.setFadingEdgeLength(0);
      //ͼƬ֮��ļ��
        galleryflow.setSpacing(10);
        galleryflow.setAdapter(adapter);
        galleryflow.setOnItemClickListener(new OnItemClickListener() {
        	@Override
        	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
        			long arg3) {
        		// TODO Auto-generated method stub
        		Toast.makeText(getApplicationContext(), String.valueOf(arg2), Toast.LENGTH_LONG).show();
        	}
		});
        //����Ĭ����ʾͼƬ
        galleryflow.setSelection(4);
    }
}