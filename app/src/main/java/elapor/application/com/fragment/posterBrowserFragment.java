package elapor.application.com.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import customfonts.MyTextView;
import elapor.application.com.elapor.MainActivity;
import elapor.application.com.elapor.R;
import elapor.application.com.utils.imageIndicatorListener;
import elapor.application.com.utils.pictureFacer;
import elapor.application.com.utils.recyclerViewPagerImageIndicator;


/**
 * Author: CodeBoy722
 *
 * this fragment handles the browsing of all images in an ArrayList of pictureFacer passed in the constructor
 * the images are loaded in a ViewPager an a RecyclerView is used as a pager indicator for
 * each image in the ViewPager
 */
public class posterBrowserFragment extends Fragment implements imageIndicatorListener {

    private  ArrayList<pictureFacer> allImages = new ArrayList<>();
    private int position;
    private Context animeContx;
    private ImageView image;
    private ViewPager imagePager;
    private MyTextView download;
    private RecyclerView indicatorRecycler;
    private int viewVisibilityController;
    private int viewVisibilitylooper;
    private ImagesPagerAdapter pagingImages;
    private int previousSelected = -1;

    Dialog dialog_save_image;
    TextView save_image;
    String url_download;
    String filename;

    Dialog dialog_informasi;
    MyTextView btn_ok;
    MyTextView text_title;
    MyTextView text_message;

    public posterBrowserFragment(){

    }

    public posterBrowserFragment(ArrayList<pictureFacer> allImages, int imagePosition, Context anim) {
        this.allImages = allImages;
        this.position = imagePosition;
        this.animeContx = anim;

        dialog_informasi = new Dialog(animeContx);
        dialog_informasi.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_informasi.setCancelable(true);
        dialog_informasi.setContentView(R.layout.informasi_dialog);

        btn_ok = (MyTextView) dialog_informasi.findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog_informasi.dismiss();
            }
        });

        text_title = (MyTextView) dialog_informasi.findViewById(R.id.text_title);
        text_message = (MyTextView) dialog_informasi.findViewById(R.id.text_dialog);

        dialog_save_image = new Dialog( this.animeContx);
        dialog_save_image.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog_save_image.setContentView(R.layout.save_image_dialog);
        save_image = (TextView) dialog_save_image.findViewById(R.id.txtSaveImage);

        save_image.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                dialog_save_image.dismiss();

                MainActivity.imageLoader.loadImage(url_download, MainActivity.imageOptions, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        saveImageFile(loadedImage, filename);
                        super.onLoadingComplete(imageUri, view, loadedImage);
                    }
                });

            }
        });
    }

    private String getPathFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "Pictures");
        if (!file.exists()) {
            file.mkdirs();
        }


        file = new File(file.getPath(), getResources().getString(R.string.app_name));
        if (!file.exists()) {
            file.mkdirs();
        }
        //String uriSting = (file.getAbsolutePath() + "/" + url);

        return file.getAbsolutePath();
    }

    public String saveImageFile(Bitmap bitmap, String url) {
        String filename = null;
        Boolean is_saved = false;
        String error_message = "Failed to save image.";
        try {
            filename = getPathFilename();
            //if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            //File sdCard = Environment.getExternalStorageDirectory();
            //File dir = new File(sdCard.getAbsolutePath() + File.pathSeparator + "Pictures");
            //if (!dir.exists()) {
            //dir.mkdirs();
            //}

            File file = new File(filename, url);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            FileOutputStream f = null;

            f = new FileOutputStream(file);
            if (f != null) {
                f.write(baos.toByteArray());
                f.flush();
                f.close();
            }

            Toast.makeText(animeContx, "File saved to "+filename+"/"+url, Toast.LENGTH_LONG).show();
            is_saved = true;

            MediaScannerConnection.scanFile(animeContx,
                    new String[] { file.toString() }, null,
                    new MediaScannerConnection.OnScanCompletedListener() {

                        @Override
                        public void onScanCompleted(String path, Uri uri) {
                            // TODO Auto-generated method stub

                        }
                    });
            //}
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            //Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
            error_message = e.getMessage();
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            //Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
            error_message = e.getMessage();
            e.printStackTrace();
        }

        if(!is_saved) {
            text_message.setText(error_message);
            text_title.setText("ERROR");
            dialog_informasi.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog_informasi.show();
            //alert.showAlertDialog(context, "Kesalahan", error_message, is_saved);
        }

        return filename;
    }

    public void showDialogSaveAs() {
        dialog_save_image.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog_save_image.show();
    }


    public static posterBrowserFragment newInstance(ArrayList<pictureFacer> allImages, int imagePosition, Context anim) {
        posterBrowserFragment fragment = new posterBrowserFragment(allImages,imagePosition,anim);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.picture_browser, container, false);

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /**
         * initialisation of the recyclerView visibility control integers
         */
        viewVisibilityController = 0;
        viewVisibilitylooper = 0;

        /**
         * setting up the viewPager with images
         */
        imagePager = view.findViewById(R.id.imagePager);
        pagingImages = new ImagesPagerAdapter();
        imagePager.setAdapter(pagingImages);
        imagePager.setOffscreenPageLimit(3);
        imagePager.setCurrentItem(position);//displaying the image at the current position passed by the ImageDisplay Activity


        /**
         * setting up the recycler view indicator for the viewPager
         */
        indicatorRecycler = view.findViewById(R.id.indicatorRecycler);
        indicatorRecycler.hasFixedSize();
        indicatorRecycler.setLayoutManager(new GridLayoutManager(getContext(),1,RecyclerView.HORIZONTAL,false));
        RecyclerView.Adapter indicatorAdapter = new recyclerViewPagerImageIndicator(allImages,getContext(),this);
        indicatorRecycler.setAdapter(indicatorAdapter);

        //adjusting the recyclerView indicator to the current position of the viewPager, also highlights the image in recyclerView with respect to the
        //viewPager's position
        allImages.get(position).setSelected(true);
        previousSelected = position;
        indicatorAdapter.notifyDataSetChanged();
        indicatorRecycler.scrollToPosition(position);


        /**
         * this listener controls the visibility of the recyclerView
         * indication and it current position in respect to the image ViewPager
         */
        imagePager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if(previousSelected != -1){
                    allImages.get(previousSelected).setSelected(false);
                    previousSelected = position;
                    allImages.get(position).setSelected(true);
                    indicatorRecycler.getAdapter().notifyDataSetChanged();
                    indicatorRecycler.scrollToPosition(position);
                }else{
                    previousSelected = position;
                    allImages.get(position).setSelected(true);
                    indicatorRecycler.getAdapter().notifyDataSetChanged();
                    indicatorRecycler.scrollToPosition(position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        indicatorRecycler.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                /**
                 *  uncomment the below condition to control recyclerView visibility automatically
                 *  when image is clicked also uncomment the condition set on the image's onClickListener in the ImagesPagerAdapter adapter
                 */
                /*if(viewVisibilityController == 0){
                    indicatorRecycler.setVisibility(View.VISIBLE);
                    visibiling();
                }else{
                    viewVisibilitylooper++;
                }*/
                return false;
            }
        });

    }


    /**
     * this method of the imageIndicatorListerner interface helps in communication between the fragment and the recyclerView Adapter
     * each time an iten in the adapter is clicked the position of that item is communicated in the fragment and the position of the
     * viewPager is adjusted as follows
     * @param ImagePosition The position of an image item in the RecyclerView Adapter
     */
    @Override
    public void onImageIndicatorClicked(int ImagePosition) {

        //the below lines of code highlights the currently select image in  the indicatorRecycler with respect to the viewPager position
        if(previousSelected != -1){
            allImages.get(previousSelected).setSelected(false);
            previousSelected = ImagePosition;
            indicatorRecycler.getAdapter().notifyDataSetChanged();
        }else{
            previousSelected = ImagePosition;
        }

        imagePager.setCurrentItem(ImagePosition);
    }

    /**
     * the imageViewPager's adapter
     */
    private class ImagesPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return allImages.size();
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup containerCollection, int position) {
            LayoutInflater layoutinflater = (LayoutInflater) containerCollection.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutinflater.inflate(R.layout.picture_browser_pager,null);
               image = view.findViewById(R.id.image);
            download = view.findViewById(R.id.download);


            ViewCompat.setTransitionName(image, String.valueOf(position)+"picture");

            final pictureFacer pic = allImages.get(position);
            Glide.with(animeContx)
                    .load(pic.getImageUri())
                    .apply(new RequestOptions().fitCenter())
                    .into(image);

            download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    url_download = pic.getImageUri();
                    filename = pic.getPicturName();
                    showDialogSaveAs();
                    //Toast.makeText(animeContx, pic.getImageUri(), Toast.LENGTH_LONG).show();
                }
            });

            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(indicatorRecycler.getVisibility() == View.GONE){
                        indicatorRecycler.setVisibility(View.VISIBLE);
                    }else{
                        indicatorRecycler.setVisibility(View.GONE);
                    }

                    /**
                     * uncomment the below condition and comment the one above to control recyclerView visibility automatically
                     * when image is clicked
                     */
                    /*if(viewVisibilityController == 0){
                     indicatorRecycler.setVisibility(View.VISIBLE);
                     visibiling();
                 }else{
                     viewVisibilitylooper++;
                 }*/

                }
            });



            ((ViewPager) containerCollection).addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup containerCollection, int position, Object view) {
            ((ViewPager) containerCollection).removeView((View) view);
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == ((View) object);
        }
    }

    /**
     * function for controlling the visibility of the recyclerView indicator
     */
    private void visibiling(){
        viewVisibilityController = 1;
        final int checker = viewVisibilitylooper;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(viewVisibilitylooper > checker){
                   visibiling();
                }else{
                   indicatorRecycler.setVisibility(View.GONE);
                   viewVisibilityController = 0;

                   viewVisibilitylooper = 0;
                }
            }
        }, 4000);
    }

}
