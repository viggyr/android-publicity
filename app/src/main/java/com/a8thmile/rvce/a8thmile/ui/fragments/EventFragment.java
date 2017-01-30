package com.a8thmile.rvce.a8thmile.ui.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import github.chenupt.springindicator.SpringIndicator;

import com.a8thmile.rvce.a8thmile.R;
import com.a8thmile.rvce.a8thmile.models.EventFields;
import com.a8thmile.rvce.a8thmile.ui.Activities.HomeActivity;
import com.a8thmile.rvce.a8thmile.ui.Activities.SubEventActivity;
import com.a8thmile.rvce.a8thmile.ui.Adapters.EventAdapter;
import com.a8thmile.rvce.a8thmile.ui.EventItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;


public class EventFragment extends Fragment {
    private List<EventItem> eventItems;
    private List<EventFields> eventFields;
    private List<EventFields> cultural;
    private List<EventFields> technical;
    private List<EventFields> informal;
    private List<EventFields> photography;
    private List<EventFields> engaging;
    private List<EventFields> sports;
    private List<EventFields> flagship;
    private List<EventFields> fine_arts;
    private List<EventFields> literary;

    private String token;
    private String user_id;

    private HashMap<Integer,List<EventFields>> eventMap;
    private HashMap<Integer,String> eventCategory;
    private static final int NUM_PAGES = 5;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    SpringIndicator springIndicator;


    private static int[] images=
            {

            R.drawable.technical,
            R.drawable.cultural,
            R.drawable.sports,
            R.drawable.informal,
            R.drawable.arts,
            R.drawable.literary,
            R.drawable.photography,
            R.drawable.engaging,
            R.drawable.flagship,
            };


    public EventFragment() {
        // Required empty public constructor
    }
public  void changeToolbarColor(int position)
{
    ((HomeActivity)getActivity()).changeActionbar(position);
}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_event, container, false);

        technical=new ArrayList<EventFields>();
        cultural=new ArrayList<EventFields>();
        informal=new ArrayList<EventFields>();
        flagship=new ArrayList<EventFields>();
        sports=new ArrayList<EventFields>();
        photography=new ArrayList<EventFields>();
        literary =new ArrayList<EventFields>();
        fine_arts=new ArrayList<EventFields>();
        engaging=new ArrayList<EventFields>();
        eventCategory=new HashMap<Integer, String>();
        eventMap=new HashMap<Integer, List<EventFields>>();
        eventMap.put(0,technical);
        eventCategory.put(0,"Technical Events");
        eventMap.put(1,cultural);
        eventCategory.put(1,"Cultural Events");
        eventMap.put(2,sports);
        eventCategory.put(2,"Sports and Gaming Events");
        eventMap.put(3,informal);
        eventCategory.put(3,"Informal Events");
        eventMap.put(4,fine_arts);
        eventCategory.put(4,"Fine Arts Events");
        eventMap.put(5,literary);
        eventCategory.put(5,"Literary Events");
        eventMap.put(6,photography);
        eventCategory.put(6,"Photography Events");
        eventMap.put(7,engaging);
        eventCategory.put(7,"Engaging Events");
        eventMap.put(8,flagship);
        eventCategory.put(8,"FlagShip Events");
        //toolbar = (Toolbar) findViewById(R.id.toolbar);
        //ListView lv = (ListView) view.findViewById(R.id.eventList);
        mPager = (ViewPager) view.findViewById(R.id.pager);
        springIndicator = (SpringIndicator) view.findViewById(R.id.indicator);
        eventItems = new ArrayList<EventItem>();
        int[] backColId = {R.drawable.gradient,R.drawable.gradient3,R.drawable.gradient4,R.drawable.gradient5,R.drawable.gradient6,R.drawable.gradient7,R.drawable.gradient8,R.drawable.gradient9,R.drawable.gradient10};
        final String[] titles = {"TECHNICAL","CULTURAL","SPORTS AND GAMING","INFORMAL","FINE ARTS","LITERARY","PHOTOGRAPHY","ENGAGING","FLAGSHIP"};
        for(int i=0;i<titles.length;i++)
        {
            EventItem eventItem=new EventItem(backColId[i],images[i],titles[i]);
            eventItems.add(eventItem);
        }


        //springIndicator.bringToFront();
        //mPager.setAdapter(mPagerAdapter);
      /*  mPager.setOnClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

               Intent subIntent=new Intent(getActivity(), SubEventActivity.class);
                Bundle bundle=new Bundle();
               bundle.putParcelableArrayList("subevents", (ArrayList<? extends Parcelable>) eventMap.get(i));
               subIntent.putExtras(bundle);
               subIntent.putExtra("category",eventCategory.get(i));
               subIntent.putExtra("user_id",user_id);
               subIntent.putExtra("token",token);
               startActivity(subIntent);
           }
       });*/
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
     eventFields=((HomeActivity)getActivity()).getEvents();
     token=((HomeActivity)getActivity()).getToken();
        user_id=((HomeActivity)getActivity()).getId();
        if(eventFields!=null)
       splitEventList(eventFields);
      mPagerAdapter = new EventAdapter(getContext(),getChildFragmentManager(),R.layout.event_list_item,
                eventItems,eventMap,token,user_id,eventCategory,this);
        mPager.setAdapter(mPagerAdapter);

        //mPager.setPageTransformer(true, new ZoomOutPageTransformer());
        springIndicator.setViewPager(mPager);
    }
    public void splitEventList(List<EventFields> eventFields) {
        for (EventFields event:eventFields)
        {
            switch (event.getType())
            {
                case 1:
                    technical.add(event);
                    break;
                case 2:
                    cultural.add(event);
                    break;
                case 3:
                    sports.add(event);
                    break;
                case 4:
                    informal.add(event);
                    break;
                case 5:
                    literary.add(event);
                    break;
                case 6:
                    photography.add(event);
                    break;
                case 7:
                    fine_arts.add(event);
                    break;
                case 8:
                    engaging.add(event);
                    break;
                case 9:
                    flagship.add(event);
                    break;

            }
        }
    }


    public class ZoomOutPageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.85f;
        private static final float MIN_ALPHA = 0.5f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();
            int pageHeight = view.getHeight();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0);

            } else if (position <= 1) { // [-1,1]
                // Modify the default slide transition to shrink the page as well
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                if (position < 0) {
                    view.setTranslationX(horzMargin - vertMargin / 2);
                } else {
                    view.setTranslationX(-horzMargin + vertMargin / 2);
                }

                // Scale the page down (between MIN_SCALE and 1)
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

                // Fade the page relative to its size.
                view.setAlpha(MIN_ALPHA +
                        (scaleFactor - MIN_SCALE) /
                                (1 - MIN_SCALE) * (1 - MIN_ALPHA));

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0);
            }
        }
    }

}
