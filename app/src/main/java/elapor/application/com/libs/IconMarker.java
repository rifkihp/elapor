package elapor.application.com.libs;

import elapor.application.com.elapor.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

public class IconMarker {

	Context context;
	float density;
	
	public IconMarker(Context context, float density) {
		this.context = context;		
		this.density = density;
	}

	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
		  Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
		      bitmap.getHeight(), Config.ARGB_8888);
		  Canvas canvas = new Canvas(output);
		 
		  final int color = 0xff424242;
		  final Paint paint = new Paint();
		  final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		  final RectF rectF = new RectF(rect);
		  final float roundPx = 5;
		 
		  paint.setAntiAlias(true);
		  canvas.drawARGB(0, 0, 0, 0);
		  paint.setColor(color);
		  canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		 
		  paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		  canvas.drawBitmap(bitmap, rect, rect, paint);
		 
		  return output;
	}
	
	
	public Bitmap DrawMarker(Bitmap source) {
		
		/*0.75 - ldpi
		1.0 - mdpi
		1.5 - hdpi
		2.0 - xhdpi
		3.0 - xxhdpi
		4.0 - xxxhdpi*/
		
		
		Bitmap img1 = null;
		Bitmap bmp = null;
		
		if(density==3.0) { //xxhdpi
			img1 = getResizedBitmap(source, 101, 101);
			img1 = getRoundedCornerBitmap(img1);
		    
		    bmp = getResizedBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.frame), 115, 106);	    
		} else
		if(density==2.0) { //xhdpi
			img1 = getResizedBitmap(source, 64, 64);
			img1 = getRoundedCornerBitmap(img1);
			    
			bmp = getResizedBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.frame), 78, 69);	    
		} else
		if(density==1.5) { //hdpi
			img1 = getResizedBitmap(source, 48, 48);
			img1 = getRoundedCornerBitmap(img1);
				    
			bmp = getResizedBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.frame), 62, 53);	    
		} else
		if(density==1.0) { //mdpi
			img1 = getResizedBitmap(source, 32, 32);
			img1 = getRoundedCornerBitmap(img1);
					    
			bmp = getResizedBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.frame), 46, 37);	    
		}
			
				
	    
	    Bitmap output = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), Bitmap.Config.ARGB_8888);
	    
	    //paint background
	    Canvas canvas1 = new Canvas(output);
	    canvas1.drawBitmap(bmp, 0, 0, null);
	    canvas1.drawBitmap(img1, 2, 2, null);
	    
	    
	    //write new bitmap
	    //Bitmap output1 = Bitmap.createBitmap(img1.getWidth(), img1.getHeight(), Bitmap.Config.ARGB_8888);
	    //Canvas canvas = new Canvas(output1);
	    
	    /*final int color = 0xff424242;
	    final Paint paint = new Paint();
	    final Rect rect = new Rect(0, 0, img1.getWidth(), img1.getHeight());
	    final RectF rectF = new RectF(rect);	
	
	    paint.setAntiAlias(true);
	    canvas.drawARGB(0, 0, 0, 0);
	    paint.setColor(color);
	    canvas.drawOval(rectF, paint);
	
	    paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
	    canvas.drawBitmap(img1, rect, rect, paint);
	    Bitmap img=getResizedBitmap(output1,bmp.getHeight()*3/4-4,bmp.getWidth()*3/4);*/
	
	    
	
	    return output;
	
	}

	public Bitmap DrawMarker() {
		
		/*0.75 - ldpi
		1.0 - mdpi
		1.5 - hdpi
		2.0 - xhdpi
		3.0 - xxhdpi
		4.0 - xxxhdpi*/
		
		
		Bitmap img1 = null;
		Bitmap bmp = null;
		
		if(density==3.0) { //xxhdpi
			img1 = getResizedBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.profile), 101, 101);
			img1 = getRoundedCornerBitmap(img1);
		    
		    bmp = getResizedBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.frame), 115, 106);	    
		} else
		if(density==2.0) { //xhdpi
			img1 = getResizedBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.profile), 64, 64);
			img1 = getRoundedCornerBitmap(img1);
			    
			bmp = getResizedBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.frame), 78, 69);	    
		} else
		if(density==1.5) { //hdpi
			img1 = getResizedBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.profile), 48, 48);
			img1 = getRoundedCornerBitmap(img1);
				    
			bmp = getResizedBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.frame), 62, 53);	    
		} else
		if(density==1.0) { //mdpi
			img1 = getResizedBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.profile), 32, 32);
			img1 = getRoundedCornerBitmap(img1);
					    
			bmp = getResizedBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.frame), 46, 37);	    
		}
			
				
	    
	    Bitmap output = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), Bitmap.Config.ARGB_8888);
	    
	    //paint background
	    Canvas canvas1 = new Canvas(output);
	    canvas1.drawBitmap(bmp, 0, 0, null);
	    canvas1.drawBitmap(img1, 2, 2, null);
	    
	    
	    //write new bitmap
	    //Bitmap output1 = Bitmap.createBitmap(img1.getWidth(), img1.getHeight(), Bitmap.Config.ARGB_8888);
	    //Canvas canvas = new Canvas(output1);
	    
	    /*final int color = 0xff424242;
	    final Paint paint = new Paint();
	    final Rect rect = new Rect(0, 0, img1.getWidth(), img1.getHeight());
	    final RectF rectF = new RectF(rect);	
	
	    paint.setAntiAlias(true);
	    canvas.drawARGB(0, 0, 0, 0);
	    paint.setColor(color);
	    canvas.drawOval(rectF, paint);
	
	    paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
	    canvas.drawBitmap(img1, rect, rect, paint);
	    Bitmap img=getResizedBitmap(output1,bmp.getHeight()*3/4-4,bmp.getWidth()*3/4);*/
	
	    
	
	    return output;
	
	}
	
	public  Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
	
	    int width = bm.getWidth();	
	    int height = bm.getHeight();
	
	    float scaleWidth = ((float) newWidth) / width;	
	    float scaleHeight = ((float) newHeight) / height;
	
	    // CREATE A MATRIX FOR THE MANIPULATION	
	    Matrix matrix = new Matrix();
	
	    // RESIZE THE BIT MAP
	    matrix.postScale(scaleWidth, scaleHeight);
	
	// RECREATE THE NEW BITMAP
	
	    Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
	
	    return resizedBitmap;
	
	}

}