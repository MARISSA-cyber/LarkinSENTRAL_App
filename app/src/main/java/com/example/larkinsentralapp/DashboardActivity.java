package com.example.larkinsentralapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import android.widget.EditText;
import android.widget.Toast;

public class DashboardActivity extends AppCompatActivity {

    private ImageButton menuButton;
    private LinearLayout floatingMenu;

    private EditText originInput, destinationInput;
    private RadioGroup tripTypeGroup;
    private RadioButton oneWayTrip, returnTrip;
    private TextView departDateText, returnDateText;
    private Button searchTripButton, buttonLogOut;

    private TextView menuProfile, menuTicketHistory, menuFacilities, menuFAQ, menuContactUs,menuFeedback ,menuAbout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.booking_form);
        EditText originInput = findViewById(R.id.originInput);
        EditText destinationInput = findViewById(R.id.destinationInput);
        Button searchBtn = findViewById(R.id.searchTripButton);

        menuButton = findViewById(R.id.menuButton);
        floatingMenu = findViewById(R.id.floatingMenu);

        originInput = findViewById(R.id.originInput);
        destinationInput = findViewById(R.id.destinationInput);


        tripTypeGroup = findViewById(R.id.tripTypeGroup);
        oneWayTrip = findViewById(R.id.oneWayTrip);
        returnTrip = findViewById(R.id.returnTrip);

        departDateText = findViewById(R.id.departDateText);
        returnDateText = findViewById(R.id.returnDateText);

        searchTripButton = findViewById(R.id.searchTripButton);

        menuProfile = findViewById(R.id.menuProfile);
        menuTicketHistory = findViewById(R.id.menuTicketHistory);
        menuFacilities = findViewById(R.id.menuFacilities);
        menuFAQ = findViewById(R.id.menuFAQ);
        menuContactUs = findViewById(R.id.menuContactUs);
        menuFeedback= findViewById(R.id.menuFeedback);
        menuAbout= findViewById(R.id.menuInformation);
        buttonLogOut =findViewById(R.id.buttonLogout);

        menuButton.setOnClickListener(v -> {
            if (floatingMenu.getVisibility() == View.GONE) {
                floatingMenu.setVisibility(View.VISIBLE);
            } else {
                floatingMenu.setVisibility(View.GONE);
            }
        });

        tripTypeGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.returnTrip) {
                returnDateText.setVisibility(View.VISIBLE);
            } else {
                returnDateText.setVisibility(View.GONE);
            }
        });

        departDateText.setOnClickListener(v -> showDatePicker(departDateText));
        returnDateText.setOnClickListener(v -> showDatePicker(returnDateText));

        searchTripButton.setOnClickListener(v -> {
            String origin = originInput.getText().toString().trim();
            String destination = destinationInput.getText().toString().trim();
            String departDate = departDateText.getText().toString().trim();
            String returnDate = returnDateText.getText().toString().trim();

            if (origin.isEmpty() || destination.isEmpty() || departDate.isEmpty()) {
                Toast.makeText(DashboardActivity.this, "Please fill origin, destination, and departure date", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean isTwoWay = returnTrip.isChecked() && !returnDate.isEmpty();

            Intent intent = new Intent(DashboardActivity.this, SearchResultActivity.class);
            intent.putExtra("origin", origin);
            intent.putExtra("destination", destination);
            intent.putExtra("departDate", departDate);
            intent.putExtra("returnDate", isTwoWay ? returnDate : "");
            intent.putExtra("isReturnTrip", isTwoWay);
            startActivity(intent);
        });
        searchBtn.setOnClickListener(v -> {

<<<<<<< Updated upstream
        menuProfile.setOnClickListener(v -> openActivity(UserProfileActivity.class));
        menuTicketHistory.setOnClickListener(v -> openActivity(OrderHistoryActivity.class));
        menuFacilities.setOnClickListener(v -> openActivity(FacilitiesActivity.class));
        menuFAQ.setOnClickListener(v -> openActivity(FAQActivity.class));
        menuContactUs.setOnClickListener(v -> openActivity(ReachUsActivity.class));
        menuFeedback.setOnClickListener(v -> openActivity(FeedbackActivity.class));
        menuAbout.setOnClickListener(v -> openActivity(AboutActivity.class));
        buttonLogOut.setOnClickListener(v -> openActivity(LoginActivity.class));
    }
=======
            String from = originInput.getText().toString().trim();
            String to = destinationInput.getText().toString().trim();
            String date = dateButton.getText().toString();

            if (from.isEmpty() || to.isEmpty()) {
                Toast.makeText(this, "Please enter origin and destination", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(DashboardActivity.this, BookingSummaryActivity.class);

            intent.putExtra("FROM", from);
            intent.putExtra("TO", to);
            intent.putExtra("DATE", date);

            startActivity(intent);
        });
>>>>>>> Stashed changes

    private void showDatePicker(final TextView target) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(
                DashboardActivity.this,
                (view, year, month, dayOfMonth) -> target.setText(dayOfMonth + "/" + (month + 1) + "/" + year),
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        dialog.show();
    }

    private void openActivity(Class<?> activityClass) {
        Intent intent = new Intent(DashboardActivity.this, activityClass);
        startActivity(intent);
    }
}