package com.ribal.cutline.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.ribal.cutline.R;
import com.ribal.cutline.model.ImagePage;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class ViewPageAdapter extends PagerAdapter {

    Context context;
    List<ImagePage> imagePageList;
    LayoutInflater inflater;

    public ViewPageAdapter(Context context, List<ImagePage> imagePageList) {
        this.context = context;
        this.imagePageList = imagePageList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return imagePageList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ((ViewPager)container).removeView((View)object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        //inflate
        View view = inflater.inflate(R.layout.view_pager_item,container,false);

        //view
        ImageView barber_image = view.findViewById(R.id.img_page);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,"Image Dashboard", Toast.LENGTH_SHORT).show();
            }
        });

        Picasso.get().load(imagePageList.get(position).getImage()).into(barber_image);

        container.addView(view);
        return view;
    }
}
