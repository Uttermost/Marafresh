package com.app.marafresh.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.app.marafresh.ProductDetailActivity;
import com.app.marafresh.R;
import com.app.marafresh.model.Cart;
import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by irving on 9/2/15.
 */
public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CustomViewHolder> {
    private List<Cart> profileList;
    private Context mContext;
    private DatabaseReference mDatabase;

    public CartAdapter(Context context, List<Cart> profileList) {
        this.profileList = profileList;
        this.mContext = context;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.product_list_item, null);

        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder customViewHolder, int i) {
        final Cart feedItem = profileList.get(i);




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
   if (customViewHolder.views != null) {
    customViewHolder.views.setText(feedItem.getQuantity());
        }





        if ( customViewHolder.title!= null) {
            customViewHolder.title.setText(feedItem.getTitle());
        }
        if ( customViewHolder.price!= null) {
            customViewHolder.price.setText("Price "+ feedItem.getPrice()+ " KES");
        }




        if( customViewHolder.thumbnail!=null){

//                Bitmap v=retriveVideoFrameFromVideo(feedItem.getPath());

            Picasso.get().load(feedItem.getPhoto()).into(customViewHolder.thumbnail);


        }



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
                int position = holder.getAdapterPosition();


                Intent xbrew = new Intent(mContext, ProductDetailActivity.class);
                xbrew.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                xbrew.putExtra("firekey",
                        profileList.get(position).getFirekey().toString());
                xbrew.putExtra("subcat",
                        profileList.get(position).getCategory()).toString();
                xbrew.putExtra("paybill",
                        profileList.get(position).getPaybill()).toString();
                xbrew.putExtra("detail_data",feedItem);


                Log.e("FKey",xbrew.toString());
                mContext.startActivity(xbrew);


            }
        };

        //Handle click event on both title and image click
         customViewHolder.thumbnail.setOnClickListener(clickListener);
       customViewHolder.title.setOnClickListener(clickListener);
        customViewHolder.price.setOnClickListener(clickListener);
        customViewHolder.view_more.setOnClickListener(clickListener);





        customViewHolder.thumbnail.setTag(customViewHolder);
        customViewHolder.title.setTag(customViewHolder);
        customViewHolder.price.setTag(customViewHolder);
        customViewHolder.view_more.setTag(customViewHolder);


        //customViewHolder.Country.setOnClickListener(clickListener);
        //customViewHolder.City.setOnClickListener(clickListener);
        //customViewHolder.DetailedJobProfile.setOnClickListener(clickListener);
//        customViewHolder.tvDescription.setTag(customViewHolder);
        customViewHolder.cardView.setTag(customViewHolder);

        setAnimation(customViewHolder, i);




    }

    /**
     * Here is the key method to apply the animation
     */
    private void setAnimation(CustomViewHolder viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        int lastPosition = position - 1;
        if (position > lastPosition) {
            final Animation fade = AnimationUtils.loadAnimation(mContext, R.anim.fade_in);
            viewToAnimate.cardView.setAnimation(fade);
            lastPosition = position;
        }
    }


    @Override
    public int getItemCount() {
        return (null != profileList ? profileList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        //protected CircleImageView imPhoto;
        protected Toolbar tool;

        protected CardView cardView;

        protected ImageView thumbnail;
        protected TextView title,view_more,views;
        protected TextView price;



        public CustomViewHolder(View v) {
            super(v);

            cardView = (CardView)v.findViewById( R.id.cardView);
            thumbnail = (ImageView)v.findViewById( R.id.image);
            title = (TextView)v.findViewById( R.id.name);
            price = (TextView)v.findViewById( R.id.amount);

            view_more= (TextView)v.findViewById( R.id.customizable);
            views= (TextView)v.findViewById( R.id.desc);







        }
    }
}
