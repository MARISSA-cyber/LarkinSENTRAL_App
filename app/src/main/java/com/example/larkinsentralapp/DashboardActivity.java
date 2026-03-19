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

public class DashboardActivity extends AppCompatActivity {

    private ImageButton menuButton;
    private LinearLayout floatingMenu;

    private EditText originInput, destinationInput;
    private RadioGroup tripTypeGroup;
    private RadioButton oneWayTrip, returnTrip;
    private TextView departDateText, returnDateText;
    private Button searchTripButton, buttonLogOut;

    private TextView menuProfile, menuTicketHistory, menuFacilities, menuFAQ,
            menuContactUs, menuFeedback, menuAbout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.booking_form);

        // Initialize views
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
        menuFeedback = findViewById(R.id.menuFeedback);
        menuAbout = findViewById(R.id.menuInformation);
        buttonLogOut = findViewById(R.id.buttonLogout);

        // Toggle menu
        menuButton.setOnClickListener(v -> {
            if (floatingMenu.getVisibility() == View.GONE) {
                floatingMenu.setVisibility(View.VISIBLE);
            } else {
                floatingMenu.setVisibility(View.GONE);
            }
        });

        // Trip type logic
        tripTypeGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.returnTrip) {
                returnDateText.setVisibility(View.VISIBLE);
            } else {
                returnDateText.setVisibility(View.GONE);
            }
        });

        // Date pickers
        departDateText.setOnClickListener(v -> showDatePicker(departDateText));
        returnDateText.setOnClickListener(v -> showDatePicker(returnDateText));

        // Search button
        searchTripButton.setOnClickListener(v -> {
            String origin = originInput.getText().toString().trim();
            String destination = destinationInput.getText().toString().trim();
            String departDate = departDateText.getText().toString().trim();
            String returnDate = returnDateText.getText().toString().trim();

            if (origin.isEmpty() || destination.isEmpty() || departDate.isEmpty()) {
                Toast.makeText(DashboardActivity.this,
                        "Please fill origin, destination, and departure date",
                        Toast.LENGTH_SHORT).show();
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

        // Menu navigation
        menuProfile.setOnClickListener(v -> openActivity(UserProfileActivity.class));
        menuTicketHistory.setOnClickListener(v -> openActivity(OrderHistoryActivity.class));
        menuFacilities.setOnClickListener(v -> openActivity(FacilitiesActivity.class));
        menuFAQ.setOnClickListener(v -> openActivity(FAQActivity.class));
        menuContactUs.setOnClickListener(v -> openActivity(ReachUsActivity.class));
        menuFeedback.setOnClickListener(v -> openActivity(FeedbackActivity.class));
        menuAbout.setOnClickListener(v -> openActivity(AboutActivity.class));
        buttonLogOut.setOnClickListener(v -> openActivity(LoginActivity.class));
    }

    private void showDatePicker(final TextView target) {
        Calendar calendar = Calendar.getInstance();

        DatePickerDialog dialog = new DatePickerDialog(
                DashboardActivity.this,
                (view, year, month, dayOfMonth) ->
                        target.setText(dayOfMonth + "/" + (month + 1) + "/" + year),
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