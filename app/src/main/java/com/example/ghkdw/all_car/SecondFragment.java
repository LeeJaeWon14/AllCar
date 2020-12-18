package com.example.ghkdw.all_car;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by ghkdw on 2019-10-01.
 */

public class SecondFragment extends Fragment {
    View view;
    ImageView top, bottom;
    Animation anim, click_anim, alpha_anim;
    Context context;
    TextView textTop, textBottom;
    String code;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        code = getArguments().getString("code");
    }

    public static SecondFragment newInstance(int page, String title, String code) {
        SecondFragment fragment = new SecondFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("SomeTitle", title);
        args.putString("code", code);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_second, container, false);
        context = container.getContext();
        anim = AnimationUtils.loadAnimation(context, R.anim.load_animation);
        click_anim = AnimationUtils.loadAnimation(context, R.anim.click_animation);
        alpha_anim = AnimationUtils.loadAnimation(context, R.anim.alpha_animation);

        top = (ImageView)view.findViewById(R.id.imgTop);
        bottom = (ImageView)view.findViewById(R.id.imgBottom);
        textTop = (TextView)view.findViewById(R.id.textTop);
        textBottom = (TextView)view.findViewById(R.id.textBottom);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        top.startAnimation(anim);
        bottom.startAnimation(anim);
        textTop.startAnimation(anim);
        textBottom.startAnimation(anim);

        top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*top.startAnimation(click_anim);
                bottom.startAnimation(alpha_anim);
                textTop.startAnimation(alpha_anim);
                textBottom.startAnimation(alpha_anim);*/

                //Toast.makeText(context, "상부", Toast.LENGTH_SHORT).show();

                Intent topIntent = new Intent(context, TopRepairActivity.class);
                topIntent.putExtra("code", code);
                startActivity(topIntent);
            }
        });

        bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*bottom.startAnimation(click_anim);
                top.startAnimation(alpha_anim);
                textTop.startAnimation(alpha_anim);
                textBottom.startAnimation(alpha_anim);*/

                //Toast.makeText(context, "하부", Toast.LENGTH_SHORT).show();

                Intent bottomIntent = new Intent(context, BottomActivity.class);
                bottomIntent.putExtra("code", code);
                startActivity(bottomIntent);
            }
        });
    }
}
