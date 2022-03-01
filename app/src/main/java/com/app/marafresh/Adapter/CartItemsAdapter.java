package com.app.marafresh.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.app.marafresh.CartActivity;
import com.app.marafresh.R;
import com.app.marafresh.model.CartItems;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by irving on 9/2/15.
 */
public class CartItemsAdapter extends RecyclerView.Adapter<CartItemsAdapter.CustomViewHolder> {
    private List<CartItems> profileList;
    private Context mContext;
    private DatabaseReference mRootRef;
    private int new_quant;
    private  String string_quant;
    private String mCurrentUserId;
    private FirebaseAuth mAuth;

    public CartItemsAdapter(Context context, List<CartItems> profileList) {
        this.profileList = profileList;
        this.mContext = context;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cart_card, null);

        CustomViewHolder viewHolder = new CustomViewHolder(view);
        mRootRef = FirebaseDatabase.getInstance().getReference().child("Cart");
        mRootRef.keepSynced(true);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            mCurrentUserId = user.getUid();

            System.out.println("mail: " + user.getEmail());


        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder customViewHolder, final int i) {
        final CartItems feedItem = profileList.get(i);
        final String feedKey = feedItem.getFirekey();

        //Log.d(TAG, "onBindViewHolder: " + feedKey );


//        customViewHolder.tool.setTitle(feedItem.getName());
//        customViewHolder.tool.setTitleTextColor(mContext.getResources().getColor(R.color.white_pure));
//        customViewHolder.tool.setBackgroundColor(mContext.getResources().getColor(R.color.blue));
//        customViewHolder.tool.inflateMenu(R.menu.menu_post_item);
//        customViewHolder.tool.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem menuItem) {
//                int id = menuItem.getItemId();
//
////                if (id == R.id.action_relate) {
////                    Integer vot = Integer.valueOf(feedItem.getVotes());
////                    vot++;
////                    feedItem.setVotes(String.valueOf(vot));
////
////
////                    Firebase.setAndroidContext(mContext);
////                    Firebase myFirebaseRef = new Firebase("https://shtaki.firebaseio.com/");
////                    myFirebaseRef.child("REPORTS").child(feedItem.getFireKey()).child("VOTES").setValue(String.valueOf(vot));
////                    Toast.makeText(mContext, "Vote Sucesfully Added", Toast.LENGTH_SHORT).show();
////                    customViewHolder.tvType.setText(feedItem.getVotes() + " people relate to this report");
////
////                }
//                if (id == R.id.action_share) {
//                    Intent sendIntent = new Intent();
//                    sendIntent.setAction(Intent.ACTION_SEND);
//                    sendIntent.putExtra(Intent.EXTRA_TEXT,
//                            feedItem.getName() + "\n" +
//                                    feedItem.getDesignation()+ "\n"
//                                    + feedItem.getCompany()+
//                                    "\n \n" + "Tagme" +
//                                    " https://play.google.com/store/apps/details?id=com.amusoft.shtaki");
//                    sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    sendIntent.setType("text/plain");
//                    mContext.startActivity(sendIntent);
//
//                }
//                return false;
//            }
//        });
////        if (customViewHolder.tvType != null) {
////            customViewHolder.tvType.setText(feedItem.getVotes() + " people relate to this report");
//        }
//
//
//


        if ( customViewHolder.title!= null) {
            customViewHolder.title.setText(feedItem.getTitle());
        }
        if ( customViewHolder.price!= null) {
            customViewHolder.price.setText(feedItem.getPrice());
        }
        if ( customViewHolder.quantity!= null) {
            customViewHolder.quantity.setText(feedItem.getQuantity());
        }



        if( customViewHolder.thumbnail!=null){

//                Bitmap v=retriveVideoFrameFromVideo(feedItem.getPath());

            Picasso.get().load(feedItem.getPhoto()).into(customViewHolder.thumbnail);


        }
        customViewHolder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alert = new AlertDialog.Builder(v.getRootView().getContext());
                alert.setTitle("Confirm Addition");
                alert.setMessage("Are you sure you want to Add Quantity?");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String price = feedItem.getPrice();
                        String quant = feedItem.getQuantity();
                        int pr = Integer.parseInt(price);
                        int qu = Integer.parseInt(quant);

                        int new_quant = qu + 1;

                        int total = pr * new_quant;
                        Float tot = (float) total;
                        //Log.d(TAG, "onClickPlus: " + total);
                        string_quant = String.valueOf(new_quant);
                        customViewHolder.quantity.setText(string_quant);
                        Map<String,Object> taskMap = new HashMap<String,Object>();
                        taskMap.put("Quantity", string_quant);
                        taskMap.put("TotalPerITEM", tot );
                        mRootRef.child(mCurrentUserId).child(feedItem.getFirekey()).updateChildren(taskMap);
                        Intent refresh = new Intent(mContext, CartActivity.class);
                        refresh.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        refresh.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        mContext.startActivity(refresh);


                        dialog.dismiss();
                    }
                });

                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                alert.show();

            }
        });
        customViewHolder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alert = new AlertDialog.Builder(v.getRootView().getContext());
                alert.setTitle("Confirm Reduction");
                alert.setMessage("Are you sure you want to reduce the quantity?");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String price = feedItem.getPrice();
                        String quant = feedItem.getQuantity();
                        int pr = Integer.parseInt(price);
                        int qu = Integer.parseInt(quant);

                        if(qu > 1) {
                            new_quant = qu - 1;
                            string_quant = String.valueOf(new_quant);
                            int total = pr * new_quant;
                            Float tot = (float) total;
                            customViewHolder.quantity.setText(string_quant);
                            Map<String,Object> taskMap = new HashMap<String,Object>();
                            taskMap.put("Quantity", string_quant);
                            taskMap.put("TotalPerITEM", tot );
                            mRootRef.child(mCurrentUserId).child(feedItem.getFirekey()).updateChildren(taskMap);
                            Intent refresh = new Intent(mContext, CartActivity.class);
                            refresh.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            refresh.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            mContext.startActivity(refresh);
                        }
                        else {
                            Toast.makeText(mContext, "You cannot have 0 ", Toast.LENGTH_SHORT).show();
                        }



                        dialog.dismiss();
                    }
                });

                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                alert.show();


            }
        });



//        if (customViewHolder.imPhoto != null) {
//            if (feedItem.getImage() != null) {
//                byte[] imageAsBytes = Base64.decode(feedItem.getImage().getBytes(), Base64.DEFAULT);
//                customViewHolder.imPhoto.setImageURI(null);
//                customViewHolder.imPhoto.setImageBitmap(null);
//                customViewHolder.imPhoto.setImageBitmap(
//                        BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length)
//                );
//
//            } else if ("-Jw7R2M88Bo-6AziZxB4".contentEquals(feedItem.getImage())) {
//                customViewHolder.imPhoto.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.ic_launcher));
//
//            } else {
//                customViewHolder.imPhoto.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.ic_launcher));
//            }
//
//
//        }


        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomViewHolder holder = (CustomViewHolder) view.getTag();
                int position = holder.getPosition();


//                Intent xbrew = new Intent(mContext, ProductDetailsActivity.class);
//                xbrew.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                xbrew.putExtra("firekey",
//                        profileList.get(position).getFirekey().toString());
//                xbrew.putExtra("firekey",
//                        profileList.get(position).getFirekey().toString());

                //mContext.startActivity(xbrew);


            }

        };

        //Handle click event on both title and image click

        customViewHolder.title.setOnClickListener(clickListener);
        customViewHolder.price.setOnClickListener(clickListener);
        customViewHolder.quantity.setOnClickListener(clickListener);
//        customViewHolder.t.setOnClickListener(clickListener);


        customViewHolder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alert = new AlertDialog.Builder(v.getRootView().getContext());
                alert.setTitle("Confirm Delete");
                alert.setMessage("Are you sure you want to delete?");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mRootRef.child(mCurrentUserId).child(feedItem.getFirekey()).removeValue();
                        //mContext.startActivity(new Intent(mContext, CartActivity.class));
                        Intent xbrew = new Intent(mContext, CartActivity.class);
                        xbrew.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(xbrew);
                        dialog.dismiss();
                    }
                });

                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                alert.show();


            }
        });


        customViewHolder.thumbnail.setTag(customViewHolder);
        customViewHolder.title.setTag(customViewHolder);
        customViewHolder.price.setTag(customViewHolder);
        customViewHolder.quantity.setOnClickListener(clickListener);
        //customViewHolder.City.setOnClickListener(clickListener);
        //customViewHolder.DetailedJobProfile.setOnClickListener(clickListener);
//        customViewHolder.tvDescription.setTag(customViewHolder);



    }

    /**
     * Here is the key method to apply the animation
     */
//    private void setAnimation(CustomViewHolder viewToAnimate, int position) {
//        // If the bound view wasn't previously displayed on screen, it's animated
//        int lastPosition = position - 1;
//        if (position > lastPosition) {
//            final Animation fade = AnimationUtils.loadAnimation(mContext, R.anim.fade_in);
//            viewToAnimate.t.setAnimation(fade);
//            lastPosition = position;
//        }
//    }


    @Override
    public int getItemCount() {
        return (null != profileList ? profileList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        //protected ImageView imPhoto;
        protected Toolbar tool;

        protected CardView cardView;
        protected ImageView thumbnail;
        protected TextView title;
        protected TextView price,quantity;
        protected ImageView overflow;
        protected TextView remove;
        protected TextView plus;
        protected TextView minus;
        private int new_quant;




        protected ImageView Thumbnail;
        protected CardView card_view;


        public CustomViewHolder(View v) {
            super(v);

            cardView = (CardView)v.findViewById( R.id.card_view );
            thumbnail = (ImageView)v.findViewById( R.id.thumbnail );
            title = (TextView)v.findViewById( R.id.title );
            price = (TextView)v.findViewById( R.id.price );
            quantity= (TextView)v.findViewById( R.id.quantity);
            remove = (TextView) v.findViewById(R.id.cart_remove);
            plus = (TextView)v. findViewById(R.id.cart_plus_img);
            minus = (TextView)v.findViewById(R.id.cart_minus_img);








        }
    }
}
