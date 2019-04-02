package com.amit.locusassignment.adapters;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.amit.locusassignment.R;
import com.amit.locusassignment.models.ListItem;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ListItemViewHolder>{

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int PERMISSION_REQUEST_CODE = 2;

    private List<ListItem> mListItems = new ArrayList<>();
    private Activity mContext;

    private ImageView slectedImage=null,selectedCancel=null;
    private int slctdPstn =0;

    private Animator currentAnimator;
    private int shortAnimationDuration;

    public RecyclerAdapter(Activity context, List<ListItem> ListItems) {
        mListItems = ListItems;
        mContext = context;
    }

    @NonNull
    @Override
    public ListItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_listitem, viewGroup, false);
        return new ListItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ListItemViewHolder viewHolder, @SuppressLint("RecyclerView") final int i) {

        final ListItem listItem = mListItems.get(i);
        viewHolder.mName.setText(listItem.getTitle());

        switch (listItem.getType()){
            case "PHOTO":
                viewHolder.mSwitch.setChecked(false);
                viewHolder.mSwitch.setVisibility(View.GONE);
                viewHolder.mRadioGroup.setVisibility(View.GONE);
                viewHolder.mRelativePhoto.setVisibility(View.VISIBLE);
                if (listItem.getDataMap().getBitmap()!=null){
                    viewHolder.mImage.setImageBitmap(listItem.getDataMap().getBitmap());
                    viewHolder.mCancel.setVisibility(View.VISIBLE);
                }
                break;

            case "SINGLE_CHOICE":
                viewHolder.mSwitch.setChecked(false);
                viewHolder.mSwitch.setVisibility(View.GONE);
                viewHolder.mRelativePhoto.setVisibility(View.GONE);
                viewHolder.mRadioGroup.setVisibility(View.VISIBLE);
                addRadioButtons(viewHolder.mRadioGroup,listItem.getDataMap().getOptions(),listItem.getDataMap().getCheckedPstn());
                break;

            case "COMMENT":
                viewHolder.mRadioGroup.setVisibility(View.GONE);
                viewHolder.mRelativePhoto.setVisibility(View.GONE);
                viewHolder.mSwitch.setVisibility(View.VISIBLE);

                if (listItem.getDataMap().isComment()){
                    viewHolder.mSwitch.setChecked(true);
                    viewHolder.mComment.setText(listItem.getDataMap().getComment());
                    viewHolder.mComment.setVisibility(View.VISIBLE);
                }else {
                    viewHolder.mSwitch.setChecked(false);
                }
                break;

        }


        viewHolder.mImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListItems.get(i).getDataMap().getBitmap()==null){
                    slectedImage = viewHolder.mImage;
                    selectedCancel = viewHolder.mCancel;
                    slctdPstn = i;
                    if (checkPermission()) {
                        dispatchTakePictureIntent();
                    } else {
                        requestPermission();
                    }
                }else {
                    enlargeImageBitmap(viewHolder.mImage,mListItems.get(i).getDataMap().getBitmap(),viewHolder.mExpendedImage,viewHolder.mframeLayout);
                }

            }
        });

        viewHolder.mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.mImage.setImageBitmap(null);
                viewHolder.mCancel.setVisibility(View.GONE);
                mListItems.get(i).getDataMap().setBitmap(null);
            }
        });

        viewHolder.mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                View radioButton = viewHolder.mRadioGroup.findViewById(checkedId);
                int idx = viewHolder.mRadioGroup.indexOfChild(radioButton);
                mListItems.get(i).getDataMap().setCheckedPstn(idx);
            }
        });

        viewHolder.mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mListItems.get(i).getDataMap().setIsComment(isChecked);
                if (isChecked){
                    viewHolder.mComment.setVisibility(View.VISIBLE);
                }else {
                    viewHolder.mComment.setVisibility(View.GONE);
                }
            }
        });

        viewHolder.mComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mListItems.get(i).getDataMap().setComment(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }

    private void addRadioButtons(RadioGroup mRadioGroup, ArrayList<String> options, int checkedPstn) {
        mRadioGroup.removeAllViews();
        for (int i = 0; i<options.size();i++){
            RadioButton radioButton = new RadioButton(mContext);
            radioButton.setId(View.generateViewId());
            radioButton.setText(options.get(i));
            if (i==checkedPstn){
                radioButton.setChecked(true);
            }
            mRadioGroup.addView(radioButton);
        }
    }

    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            return false;
        }
        return true;
    }

    public void requestPermission() {
        ActivityCompat.requestPermissions(mContext, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CODE);
    }


    public void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(mContext.getPackageManager()) != null) {
            mContext.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    public  void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            assert extras != null;
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            if (slectedImage!=null&&selectedCancel!=null){
                slectedImage.setImageBitmap(imageBitmap);
                mListItems.get(slctdPstn).getDataMap().setBitmap(imageBitmap);
                selectedCancel.setVisibility(View.VISIBLE);
            }

        }
    }


    @Override
    public int getItemCount() {
        return mListItems.size();
    }


    class ListItemViewHolder extends RecyclerView.ViewHolder{

        private TextView mName;
        private RelativeLayout mRelativePhoto;
        private ImageView mImage,mCancel,mExpendedImage;
        private RadioGroup mRadioGroup;
        private Switch mSwitch;
        private EditText mComment;
        private FrameLayout mframeLayout;

        ListItemViewHolder(@NonNull View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.tv_title);
            mRelativePhoto = itemView.findViewById(R.id.rl_photo);
            mImage = itemView.findViewById(R.id.iv_pic);
            mCancel = itemView.findViewById(R.id.iv_cancel);
            mExpendedImage = itemView.findViewById(R.id.expanded_image);
            mRadioGroup = itemView.findViewById(R.id.rg_options);
            mSwitch = itemView.findViewById(R.id.switch1);
            mComment = itemView.findViewById(R.id.et_comment);
            mframeLayout = itemView.findViewById(R.id.container);
        }
    }

    private void enlargeImageBitmap(final View thumbView, Bitmap imageResId, final ImageView expendedImageView, FrameLayout container) {

        shortAnimationDuration = mContext.getResources().getInteger(
                android.R.integer.config_shortAnimTime);
        // If there's an animation in progress, cancel it
        // immediately and proceed with this one.
        if (currentAnimator != null) {
            currentAnimator.cancel();
        }

        expendedImageView.setImageBitmap(imageResId);

        // Calculate the starting and ending bounds for the zoomed-in image.
        // This step involves lots of math. Yay, math.
        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        // The start bounds are the global visible rectangle of the thumbnail,
        // and the final bounds are the global visible rectangle of the container
        // view. Also set the container view's offset as the origin for the
        // bounds, since that's the origin for the positioning animation
        // properties (X, Y).
        thumbView.getGlobalVisibleRect(startBounds);
        container.getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        // Adjust the start bounds to be the same aspect ratio as the final
        // bounds using the "center crop" technique. This prevents undesirable
        // stretching during the animation. Also calculate the start scaling
        // factor (the end scaling factor is always 1.0).
        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            // Extend start bounds horizontally
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            // Extend start bounds vertically
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        // Hide the thumbnail and show the zoomed-in view. When the animation
        // begins, it will position the zoomed-in view in the place of the
        // thumbnail.
        thumbView.setAlpha(0f);
        expendedImageView.setVisibility(View.VISIBLE);

        // Set the pivot point for SCALE_X and SCALE_Y transformations
        // to the top-left corner of the zoomed-in view (the default
        // is the center of the view).
        expendedImageView.setPivotX(0f);
        expendedImageView.setPivotY(0f);

        // Construct and run the parallel animation of the four translation and
        // scale properties (X, Y, SCALE_X, and SCALE_Y).
        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(expendedImageView, View.X,
                        startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(expendedImageView, View.Y,
                        startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(expendedImageView, View.SCALE_X,
                        startScale, 1f))
                .with(ObjectAnimator.ofFloat(expendedImageView,
                        View.SCALE_Y, startScale, 1f));
        set.setDuration(shortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                currentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                currentAnimator = null;
            }
        });
        set.start();
        currentAnimator = set;

        // Upon clicking the zoomed-in image, it should zoom back down
        // to the original bounds and show the thumbnail instead of
        // the expanded image.
        final float startScaleFinal = startScale;
        expendedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentAnimator != null) {
                    currentAnimator.cancel();
                }

                // Animate the four positioning/sizing properties in parallel,
                // back to their original values.
                AnimatorSet set = new AnimatorSet();
                set.play(ObjectAnimator
                        .ofFloat(expendedImageView, View.X, startBounds.left))
                        .with(ObjectAnimator
                                .ofFloat(expendedImageView,
                                        View.Y,startBounds.top))
                        .with(ObjectAnimator
                                .ofFloat(expendedImageView,
                                        View.SCALE_X, startScaleFinal))
                        .with(ObjectAnimator
                                .ofFloat(expendedImageView,
                                        View.SCALE_Y, startScaleFinal));
                set.setDuration(shortAnimationDuration);
                set.setInterpolator(new DecelerateInterpolator());
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        thumbView.setAlpha(1f);
                        expendedImageView.setVisibility(View.GONE);
                        currentAnimator = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        thumbView.setAlpha(1f);
                        expendedImageView.setVisibility(View.GONE);
                        currentAnimator = null;
                    }
                });
                set.start();
                currentAnimator = set;
            }
        });
    }

    public List<ListItem> getDataList(){
        return mListItems;
    }
}
