package com.example.estatehouse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.estatehouse.adapter.HomepageAdapter;
import com.example.estatehouse.entity.House;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomepageScreen extends AppCompatActivity {

    FirebaseFirestore db;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    CollectionReference houseRef;
    Spinner spinnerCountries, spinnerCities;
    RadioGroup rdGroupType;
    RadioButton rdBuy, rdRent;
    List<House> houses;
    ListView listView;
    HomepageAdapter homepageAdapter;
    ImageView helpView, languageView, sellerView, loginView, homePageView, notiView, profileView;
    Button btnFilter;
    private int typeSelectedDefault = 0;
    AlertDialog dialogLanguage;
    AlertDialog.Builder builderLanguage;
    String[] languages = {"US (default)", "VN"};
    String languageSelected = "";
    TextView txtViewHelp, txtViewLanguage, txtViewSeller, txtViewlogin, txtViewTitle, txtViewLocation, txtViewType, txtViewRecommend, txtViewHomepage, txtViewNoti, txtViewProfile;
    TextView [] componentTextView;
    List<TextView> arrayTextViewComponent;
    String [] componentVNTranslate;
    String [] componentUSTranslate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage_screen);

        //Ánh xạ
        anhXa();

        ArrayAdapter<CharSequence> adapterCountry = ArrayAdapter.createFromResource(this, R.array.countries, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> adapterCity = ArrayAdapter.createFromResource(this, R.array.cities, android.R.layout.simple_spinner_item);
        adapterCountry.setDropDownViewResource(android.R.layout.simple_spinner_item);
        adapterCity.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinnerCountries.setAdapter(adapterCountry);
        spinnerCities.setAdapter(adapterCity);

        //setOnClick
        helpView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomepageScreen.this, HelpScreen.class);
                startActivity(intent);
            }
        });
        languageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builderLanguage = new AlertDialog.Builder(HomepageScreen.this);
                builderLanguage.setTitle("Avaiable languages ...");
                builderLanguage.setSingleChoiceItems(languages, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        languageSelected = languages[i];
                    }
                });
                builderLanguage.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                builderLanguage.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(languageSelected.equalsIgnoreCase("VN")){
                            for (int k = 0; k < componentVNTranslate.length; k++)
                                arrayTextViewComponent.get(k).setText(componentVNTranslate[k]);
                            btnFilter.setText("Tìm kiếm");
                            rdBuy.setText("MUA");
                            rdRent.setText("THUÊ");
                            Toast.makeText(HomepageScreen.this, "Ngôn ngữ của bạn đã được thay đổi thành :: " + languageSelected, Toast.LENGTH_LONG).show();
                        } else{
                            for (int k = 0; k < componentVNTranslate.length; k++)
                                arrayTextViewComponent.get(k).setText(componentUSTranslate[k]);
                            btnFilter.setText("Filter");
                            rdBuy.setText("BUY");
                            rdRent.setText("RENT");
                            Toast.makeText(HomepageScreen.this, "Your language have been changed to :: " + languageSelected, Toast.LENGTH_LONG).show();
                        }
                    }
                });
                dialogLanguage = builderLanguage.create();
                dialogLanguage.show();
            }
        });
        sellerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isLogged()){
                    Intent intent = new Intent(HomepageScreen.this, SellerScreen.class);
                    startActivity(intent);
                } else{
                    Intent intent = new Intent(HomepageScreen.this, LoginScreen.class);
                    startActivity(intent);
                }
            }
        });
        loginView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isLogged()){
                    Intent intent = new Intent(HomepageScreen.this, LoginScreen.class);
                    startActivity(intent);
                }
            }
        });
        txtViewlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isLogged()){
                    txtViewlogin.setText("Login");
                    mAuth.signOut();
                    Intent intent = new Intent(HomepageScreen.this, LoginScreen.class);
                    startActivity(intent);
                    Toast.makeText(HomepageScreen.this, "Logout success", Toast.LENGTH_LONG).show();
                }
            }
        });
        homePageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(HomepageScreen.this, "You're here now!", Toast.LENGTH_LONG).show();
            }
        });
        notiView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(HomepageScreen.this, "Notes: some new estate house has just added recently. Check it now", Toast.LENGTH_LONG).show();
            }
        });
        profileView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isLogged()){
                    Intent intent = new Intent(HomepageScreen.this, ProfieScreen.class);
                    startActivity(intent);
                } else{
                    Intent intent = new Intent(HomepageScreen.this, LoginScreen.class);
                    startActivity(intent);
                }
            }
        });
        rdGroupType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.radioButtonBuy:
                        typeSelectedDefault = 0;
                        break;
                    case R.id.radioButtonRent:
                        typeSelectedDefault = 1;
                        break;
                }
            }
        });
        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String countrySelected = spinnerCountries.getSelectedItem().toString();
                String citySelected = spinnerCities.getSelectedItem().toString();
                String typeSelected = "";
                if(typeSelectedDefault == 0)
                    typeSelected = "BUY";
                else
                    typeSelected = "RENT";
                if(!countrySelected.equalsIgnoreCase("Select country") && !citySelected.equalsIgnoreCase("Select city")){
                    Toast.makeText(HomepageScreen.this, "Querying...", Toast.LENGTH_LONG).show();
                    houseRef.whereArrayContainsAny("tags", Arrays.asList(countrySelected, citySelected))
                            .whereEqualTo("type", typeSelected)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if(task.isSuccessful()){
                                        houses.clear();
                                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                            House house = documentSnapshot.toObject(House.class);
                                            houses.add(house);
                                            Log.d("FILTERRED", house.toString());
                                        }
                                        homepageAdapter = new HomepageAdapter(houses, HomepageScreen.this);
                                        listView.setAdapter(homepageAdapter);
                                    } else Log.w("ERROR-FIRE", "Error getting documents.", task.getException());
                                }
                            });
                }//end if
            }
        });

        //Get recommended for new
        houses = new ArrayList<House>();
        houseRef.whereArrayContains("tags", "NEW")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                House house = documentSnapshot.toObject(House.class);
                                house.setDocumentId(documentSnapshot.getId());
                                houses.add(house);
                            }
                            homepageAdapter = new HomepageAdapter(houses, HomepageScreen.this);
                            listView.setAdapter(homepageAdapter);
                        } else
                            Log.w("ERROR-FIRE", "Error getting documents.", task.getException());
                    }
                });

    }//end onCreate

    @Override
    public void onStart() {
        super.onStart();
        if(isLogged()){
            txtViewlogin.setText("Logout");
            txtViewlogin.getPaint().setUnderlineText(true);
            txtViewlogin.setTextColor(Color.BLUE);

        } else{
            txtViewlogin.setText("Login");
            txtViewlogin.getPaint().setUnderlineText(false);
            txtViewlogin.setTextColor(Color.BLACK);
        }
    }//end onCreate

    private boolean isLogged() {
        currentUser = mAuth.getCurrentUser();
        if(currentUser == null)
            return false;
        return true;
    }

    private void anhXa() {
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        houseRef = db.collection("houses");
        helpView = findViewById(R.id.hp_helpIcon);
        languageView = findViewById(R.id.hp_languageIcon);
        sellerView = findViewById(R.id.hp_sellerIcon);
        loginView = findViewById(R.id.hp_loginIcon);
        homePageView = findViewById(R.id.hp_homePageIcon);
        notiView = findViewById(R.id.hp_notiIcon);
        profileView = findViewById(R.id.hp_profileIcon);
        spinnerCountries = findViewById(R.id.spinner_countries);
        spinnerCities = findViewById(R.id.spinner_cities);
        rdGroupType = findViewById(R.id.rdGroupType);
        btnFilter = findViewById(R.id.hp_btnFilter);
        rdBuy = findViewById(R.id.radioButtonBuy);
        rdRent = findViewById(R.id.radioButtonRent);
        listView = findViewById(R.id.cart_listView);
        txtViewHelp = findViewById(R.id.hp_txtViewHelp);
        txtViewLanguage = findViewById(R.id.hp_txtViewLanguage);
        txtViewSeller = findViewById(R.id.hp_txtViewSeller);
        txtViewlogin = findViewById(R.id.hp_txtViewLogin);
        txtViewTitle = findViewById(R.id.hp_txtViewTitle);
        txtViewLocation = findViewById(R.id.hp_txtViewLocation);
        txtViewType = findViewById(R.id.hp_txtViewType);
        txtViewRecommend = findViewById(R.id.hp_txtViewRecommend);
        txtViewHomepage = findViewById(R.id.hp_txtViewHomepage);
        txtViewNoti = findViewById(R.id.hp_txtViewNoti);
        txtViewProfile = findViewById(R.id.hp_txtViewProfile);
        componentTextView = new TextView[] {txtViewHelp, txtViewLanguage, txtViewSeller, txtViewlogin, txtViewTitle, txtViewLocation, txtViewType, txtViewRecommend, txtViewHomepage, txtViewNoti, txtViewProfile};
        arrayTextViewComponent = new ArrayList<TextView>();
        componentVNTranslate = new String[] {"Trợ giúp", "Ngôn ngữ", "Bán hàng", "Đăng nhập", "Lựa Chọn Căn Nhà Tuyệt Nhất", "Vị trí", "Loại thuê", "Được đề xuất cho bạn", "Trang chủ", "Thông báo(s)", "Cá nhân"};
        componentUSTranslate = new String[] {"Help", "Language", "Seller", "Login", "Find Your Best House", "Location", "Type", "Recommended for your", "Homepage", "Notification(s)", "Profile"};
        for (int k = 0; k < componentTextView.length; k++) {
            arrayTextViewComponent.add(componentTextView[k]);
        }
    }


    private void addNewDocument(String fn, String ln) {
        Map<String, Object> user = new HashMap<>();
        user.put("first_name", fn);
        user.put("last_name", ln);
        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("SUCCESS FIRESTORE", "DocumentSnapshot added with ID: " + documentReference.getId());
                        Toast.makeText(HomepageScreen.this, "DocumentSnapshot added with ID: " + documentReference.getId(), Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("FAILED FIRESTORE", "Error adding document", e);
                        Toast.makeText(HomepageScreen.this, "Error adding document" + e, Toast.LENGTH_LONG).show();
                    }
                });
    }
}