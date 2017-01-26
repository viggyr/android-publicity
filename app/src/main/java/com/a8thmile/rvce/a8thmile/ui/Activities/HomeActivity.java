package com.a8thmile.rvce.a8thmile.ui.Activities;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.a8thmile.rvce.a8thmile.R;
import com.a8thmile.rvce.a8thmile.events.EventPresenter;
import com.a8thmile.rvce.a8thmile.events.EventPresenterImpl;
import com.a8thmile.rvce.a8thmile.events.EventView;
import com.a8thmile.rvce.a8thmile.models.EventFields;
import com.a8thmile.rvce.a8thmile.models.EventResponse;
import com.a8thmile.rvce.a8thmile.ui.fragments.ContactFragment;
import com.a8thmile.rvce.a8thmile.ui.fragments.EventFragment;
import com.a8thmile.rvce.a8thmile.ui.fragments.HomeFragment;
import com.a8thmile.rvce.a8thmile.ui.fragments.Hospitality;
import com.a8thmile.rvce.a8thmile.ui.fragments.SponserFragment;
import com.a8thmile.rvce.a8thmile.ui.fragments.TourFragment;
import com.a8thmile.rvce.a8thmile.ui.fragments.WishListFragment;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import java.util.HashMap;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements EventView,GoogleApiClient.OnConnectionFailedListener,GoogleApiClient.ConnectionCallbacks {
    public NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
    private ImageView imgNavHeaderBg, imgProfile;
    private TextView txtName, txtEmail;
    private Toolbar toolbar;
    private TextView textView;
    private FloatingActionButton fab;
    public static int navItemIndex = 0;

    private String token;
    private String id;

    private static final String TAG_HOME = "home";
    private static final String TAG_EVENTS = "events";
    private static final String TAG_SPONSERS = "sponsors";
    private static final String TAG_HOSPITALITY = "hospitality";
    private static final String TAG_CONTACT = "contact";
    private static final String TAG_TOUR = "tour";
    private static final String TAG_WISHLIST = "wishlist";
    public static String CURRENT_TAG = TAG_HOME;

    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;
    private String UserName;
    private String UserEmail;
    private String[] activityTitles;
    private GoogleApiClient mGoogleApiClient;
    private EventPresenter eventPresenter;
    private List<EventFields> eventFieldsList;
    public HomeActivity() {
    }

    public List<EventFields> getEvents() {
        return eventFieldsList;
    }
public String getId(){return id;}
    public String getToken(){return token;}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        textView=(TextView)toolbar.findViewById(R.id.toolbar_title);
        UserName=getIntent().getStringExtra("userName");
        UserEmail=getIntent().getStringExtra("email");
        id=getIntent().getStringExtra("id");
        token=getIntent().getStringExtra("token");
        Log.v("test","token "+token);
        Log.v("test","id "+id);

        mGoogleApiClient= new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();

        eventPresenter=new EventPresenterImpl(this);
        //mGoogleApiClient=getIntent().getStringExtra("apiclient");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mHandler = new Handler();
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        //fab = (FloatingActionButton) findViewById(R.id.fab);

        // Navigation view header

        View headerLayout =
                navigationView.inflateHeaderView(R.layout.nav_header_main);
        navHeader = headerLayout.findViewById(R.id.view_container);
        //navHeader = navigationView.getHeaderView(0); // DIDN'T WORK IN v23.0.0 SO DID ABOVE JUGAAD- ALEKH

        navHeader = navigationView.getHeaderView(0);
        navigationView.setItemIconTintList(null);
        txtName = (TextView) navHeader.findViewById(R.id.name);
       // txtEmail = (TextView) navHeader.findViewById(R.id.email);
        imgNavHeaderBg = (ImageView) navHeader.findViewById(R.id.img_header_bg);

        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);

        loadNavHeader(UserName);

        // initializing navigation menu
        setUpNavigationView();

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            loadHomeFragment();
        }
        eventPresenter.eventRequest(token);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    private void loadNavHeader(String UserName) {
        // name, website
        txtName.setText(UserName);



    }
    private void loadHomeFragment() {
        // selecting appropriate nav menu item
        selectNavMenu();

        // set toolbar title
        setToolbarTitle();

        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();

            // show or hide the fab button
           // toggleFab();
            return;
        }
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);

                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }

        // show or hide the fab button
      //  toggleFab();

        //Closing drawer on item click
        drawer.closeDrawers();

        // refresh toolbar menu
        invalidateOptionsMenu();
    }

   private void signOut() {
       Log.v("test","signout");
        if(mGoogleApiClient.isConnected()) {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
            new ResultCallback<Status>() {
                @Override
                public void onResult(Status status) {
                    // Get sign out result
                    Log.v("test","signout2");
                    finish();
                }
            });

        }
       }


    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:
                // home
                HomeFragment homeFragment = new HomeFragment();
                return homeFragment;
            case 1:
                // photos
                EventFragment eventFragment = new EventFragment();
                return eventFragment;
            case 2:
                // movies fragment
                SponserFragment sponserFragment = new SponserFragment();
                return sponserFragment;
            case 3:
                // notifications fragment
                Hospitality hospitality = new Hospitality();
                return hospitality;

            case 4:
                // settings fragment
                ContactFragment contactFragment = new ContactFragment();
                return contactFragment;
            case 5:
                TourFragment tourFragment=new TourFragment();
                return tourFragment;
            case 6:
                WishListFragment wishListFragment=new WishListFragment();
                return wishListFragment;

            default:
                return new HomeFragment();
        }
    }
    private void setToolbarTitle() {
      //  getSupportActionBar().setTitle(activityTitles[navItemIndex]);
        textView.setText(activityTitles[navItemIndex]);

    }

    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }
    private void setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.nav_home:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_HOME;
                        break;
                    case R.id.nav_events:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_EVENTS;
                        break;
                    case R.id.nav_sponsers:
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_SPONSERS;
                        break;
                    case R.id.nav_hospitality:
                        navItemIndex = 3;
                        CURRENT_TAG = TAG_HOSPITALITY;
                        break;
                    case R.id.nav_contact:
                        navItemIndex = 4;
                        CURRENT_TAG = TAG_CONTACT;
                        break;
                    case R.id.nav_tour:
                        navItemIndex = 5;
                        CURRENT_TAG = TAG_TOUR;
                        break;
                    case R.id.nav_wishlist:
                        navItemIndex = 6;
                        CURRENT_TAG = TAG_WISHLIST;
                        break;
                    case R.id.nav_logout:
                        navItemIndex=7;
                        signOut();
                        break;
                    default:
                        navItemIndex = 0;
                }

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);
                if(navItemIndex!=7)
                loadHomeFragment();

                return true;
            }
        });


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawer.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }

        Log.v("test","backpressed");
        // This code loads home fragment when back key is pressed
        // when user is in other fragment than home
        if (shouldLoadHomeFragOnBackPress) {
            // checking if user is on other navigation menu
            // rather than home
            if (navItemIndex != 0&&navItemIndex!=7) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_HOME;
                loadHomeFragment();
                return;
            }
        }

        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        // show menu only when home fragment is selected

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }





    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
    Log.v("test","connected in home");

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void showFailureMessage(String message) {
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }

    @Override
    public void loadData(EventResponse eventResponse) {
        this.eventFieldsList=eventResponse.getResults();
        Log.v("test","events "+eventFieldsList);

    }
}
