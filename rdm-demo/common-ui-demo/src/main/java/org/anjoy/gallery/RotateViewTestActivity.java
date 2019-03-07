package org.anjoy.gallery;


import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.rdm.common.ui.demo.R;

import org.anjoy.gallery.RotateView.OnItemClickListener;;

public class RotateViewTestActivity extends Activity {
	private RotateView rotateView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rotate);
		initView();
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
	}

	private void initView() {
		// TODO Auto-generated method stub
		rotateView = (RotateView) findViewById(R.id.rotateView);
		rotateView.setAdapter(new MyAdapter());
		rotateView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(int position, View v) {
				// TODO Auto-generated method stub
				Toast.makeText(getBaseContext(), "this is item " + position, 0)
						.show();
			}
		});
	}

	class MyAdapter extends BaseAdapter {

		private int[] imgs = new int[] { R.drawable.btn_bg01,
				R.drawable.btn_bg02, R.drawable.btn_bg03, R.drawable.btn_bg04 };

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return imgs.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return imgs[position];
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			Button btn = new Button(RotateViewTestActivity.this);
			btn.setBackgroundResource(imgs[position]);
			return btn;
		}
	}
}
