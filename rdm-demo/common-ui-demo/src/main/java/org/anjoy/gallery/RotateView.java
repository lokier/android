package org.anjoy.gallery;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.rdm.common.ui.demo.R;


public class RotateView extends AbsoluteLayout implements OnGestureListener {


	private GestureDetector gd;
	private int[] imgs = new int[] {R.drawable.btn_bg01,R.drawable.btn_bg02,R.drawable.btn_bg03,R.drawable.btn_bg04 };
	//private int[] imgs = new int[] {R.drawable.image01,R.drawable.image02,R.drawable.image03,R.drawable.image04 };
	private Drawable[] ds = new Drawable[4];

	private static int OVAL_A = 100, OVAL_B = 20, OVAL_CENTER_X, OVAL_CENTER_Y;

	private int LEFT_BORDER, RIGHT_BORDER, TOP_BORDER, BOTTOM_BORDER;
	private int OFFSET_WIDTH, OFFSET_HEIGHT;

	private boolean[] up = { true, true, true, false };
	private boolean running = false;

	private List<LayoutParams> params = new ArrayList<AbsoluteLayout.LayoutParams>();
	private List<View> list = new ArrayList<View>();
	private List<View> sortList = new ArrayList<View>();

	int index = 0;

	private OnItemClickListener onItemClickListener;

	public interface OnItemClickListener {
		public void onItemClick(int position, View v);
	}
	//获取图形顺利
	@Override
	protected int getChildDrawingOrder(int childCount, int i) {
		// TODO Auto-generated method stub
		return list.indexOf(sortList.get(i));
	}

	public RotateView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		setChildrenDrawingOrderEnabled(true);
		Activity ac = (Activity) context;
		DisplayMetrics dm = new DisplayMetrics();
		ac.getWindowManager().getDefaultDisplay().getMetrics(dm);
		OVAL_CENTER_X = dm.widthPixels / 2;
		OVAL_CENTER_Y = dm.heightPixels / 2;

		Drawable d = context.getResources().getDrawable(R.drawable.btn_bg02);
		OFFSET_WIDTH = d.getIntrinsicWidth() / 2;
		OFFSET_HEIGHT = d.getIntrinsicHeight() / 2;
		gd = new GestureDetector(this);
		// 上下左右四个边界
		LEFT_BORDER = OVAL_CENTER_X - OVAL_A;
		RIGHT_BORDER = OVAL_CENTER_X + OVAL_A;
		TOP_BORDER = OVAL_CENTER_Y - OVAL_B;
		BOTTOM_BORDER = OVAL_CENTER_Y + OVAL_B;

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		gd.onTouchEvent(event);
		//return super.onTouchEvent(event);
		return true;
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
						   float velocityY) {
		// TODO Auto-generated method stub
		running = false;
		new ScrollingThread((int) velocityX).start();
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
							float distanceY) {
		scroll(distanceX);
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	private int getOvalYByX(int x, boolean up) {
		int a = x - OVAL_CENTER_X;
		int aa = OVAL_B * OVAL_B * (OVAL_A * OVAL_A - a * a);
		double aaa = divide(aa, OVAL_A * OVAL_A).doubleValue();
		int y = (int) Math.sqrt(aaa);
		if (up) {
			y = -y;
		}
		return y + OVAL_CENTER_Y;
	}

	public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
		this.onItemClickListener = onItemClickListener;
	}

	public void setAdapter(BaseAdapter adapter) {
		if (adapter.getCount() != 4) {
			Toast.makeText(getContext(), "Child count of adapter must be 4", 0)
					.show();
		}
		//把当前位置的图片添加到view中
		View convertView = new View(getContext());
		LayoutParams params01 = new LayoutParams(-2, -2, LEFT_BORDER
				- OFFSET_WIDTH, OVAL_CENTER_Y - OFFSET_HEIGHT);
		LayoutParams params02 = new LayoutParams(-2, -2, OVAL_CENTER_X
				- OFFSET_WIDTH, TOP_BORDER - OFFSET_HEIGHT);
		LayoutParams params03 = new LayoutParams(-2, -2, RIGHT_BORDER
				- OFFSET_WIDTH, OVAL_CENTER_Y - OFFSET_HEIGHT);
		LayoutParams params04 = new LayoutParams(-2, -2, OVAL_CENTER_X
				- OFFSET_WIDTH, BOTTOM_BORDER - OFFSET_HEIGHT);

		params.add(params01);
		params.add(params02);
		params.add(params03);
		params.add(params04);

		for (int i = 0; i < adapter.getCount(); i++) {
			View v = adapter.getView(i, convertView, this);
			addView(v, params.get(i));
			if (up[i]) {
				setAlphaByY(v, params.get(i).y + OFFSET_HEIGHT - OVAL_CENTER_Y);
			}
			list.add(v);
			sortList.add(v);
		}
		//在adapter容器中遍历图片
		for (int i = 0; i < adapter.getCount(); i++) {
			ds[i] = getContext().getResources().getDrawable(imgs[i]);
		}

		for (View v : list) {
			v.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Button btn = (Button) v;
					if (onItemClickListener != null) {
						onItemClickListener.onItemClick(list.indexOf(btn), btn);
					}
				}
			});
		}
	}

	private void setAlphaByY(View v, int y) {
		// TODO Auto-generated method stub
		int a = 255;
		int b = -divide(200, OVAL_B).intValue();
		int alpha = a + b * (-y);
		v.getBackground().setAlpha(alpha);
	}
	//根据当前位置的图片滚动图片
	private void scroll(float distanceX) {
		boolean scrolling2left = distanceX >= 0;
		//获取当前x绝对值
		int offset = (int) Math.abs(distanceX);
		List<int[]> loc = new ArrayList<int[]>();
		sortList.removeAll(sortList);
		for (View v : list) {
			LayoutParams params = (LayoutParams) v.getLayoutParams();
			int x = params.x + OFFSET_WIDTH;
			int y = params.y + OFFSET_HEIGHT;
			int i = list.indexOf(v);
			if (scrolling2left) {
				// 左滑动
				if (up[i]) {
					// 中心上方按钮向右
					if (x + offset > RIGHT_BORDER) {
						// 滑动后会超出右边界,上变下，y取负
						x += offset;
						x -= (2 * (x - RIGHT_BORDER));
						up[i] = false;
						y = getOvalYByX(x, up[i]);
					} else {
						// 正常滑动，y取正
						x += offset;
						y = getOvalYByX(x, up[i]);
					}

				} else {
					// 中心下方按钮向左
					if (x - offset < LEFT_BORDER) {
						// 滑动后会超出左边界，下变上，y取正
						x -= offset;
						x += (2 * (LEFT_BORDER - x));
						up[i] = true;
						y = getOvalYByX(x, up[i]);
					} else {
						// 正常滑动，y取负
						x -= offset;
						y = getOvalYByX(x, up[i]);
					}

				}
			} else {
				// 右滑动
				if (up[i]) {
					// 中心上方按钮向左
					if (x - offset < LEFT_BORDER) {
						// 滑动后会超出左边界,上变下，y取负
						x -= offset;
						x += (2 * (LEFT_BORDER - x));
						up[i] = false;
						y = getOvalYByX(x, up[i]);
					} else {
						// 正常滑动，y取正
						x -= offset;
						y = getOvalYByX(x, up[i]);
					}

				} else {
					// 中心下方按钮向右
					if (x + offset > RIGHT_BORDER) {
						// 滑动后会超出右边界，下变上，y取正
						x += offset;
						x -= (2 * (x - RIGHT_BORDER));
						up[i] = true;
						y = getOvalYByX(x, up[i]);
					} else {
						// 正常滑动，y取负
						x += offset;
						y = getOvalYByX(x, up[i]);
					}
				}
			}
			loc.add(new int[] { x - OFFSET_WIDTH, y - OFFSET_HEIGHT });
			list.set(i, v);
		}
		sortList.addAll(list);
		Collections.sort(sortList, new Comparator<View>() {

			@Override
			public int compare(View lhs, View rhs) {
				// TODO Auto-generated method stub
				int index = list.indexOf(lhs);
				if (up[index])
					return -1;
				else {
					return 1;
				}
			}

		});
		for (View v : list) {
			int i = list.indexOf(v);
			int[] location = loc.get(i);
			LayoutParams params = (LayoutParams) v.getLayoutParams();
			params.x = location[0];
			params.y = location[1];
			v.setLayoutParams(params);
			if (up[i]) {
				setAlphaByY(v, location[1] + OFFSET_HEIGHT - OVAL_CENTER_Y);
			} else {
				v.getBackground().setAlpha(255);
			}
		}
		invalidate();
	}
	//添加滚动线程进行通信滚动。
	class ScrollingThread extends Thread {

		private float velocity;
		private float accelerate;
		private boolean left = false;

		private Handler handler = new Handler(new Callback() {

			@Override
			public boolean handleMessage(Message msg) {
				// TODO Auto-generated method stub
				/*if (msg.what == 0) {
					int distanceX = msg.arg1;
					scroll(distanceX);
				}*/
				int distanceX =msg.arg1;
				scroll(distanceX);
				return false;
			}
		});

		public ScrollingThread(int maxVelocity) {
			// TODO Auto-generated constructor stub
			this.velocity = divide(maxVelocity, 1000).floatValue();
			if (Math.abs(velocity) > 0.8f) {
				accelerate = divide(left ? 12f : 12f, 1000).floatValue();
				running = true;
				left = velocity > 0;
				velocity = Math.abs(velocity);
				accelerate = Math.abs(accelerate);
			}
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			while (running && velocity > 0) {
				velocity -= accelerate;
				int offset = (int) (velocity * 5);
				handler.sendMessage(handler.obtainMessage(0, left ? -offset: offset, 0));
				try {
					Thread.sleep(5);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			super.run();
		}
	}


	private BigDecimal divide(float a, float b) {
		return new BigDecimal(a).divide(new BigDecimal(b), 8,
				BigDecimal.ROUND_HALF_UP);
	}
}
