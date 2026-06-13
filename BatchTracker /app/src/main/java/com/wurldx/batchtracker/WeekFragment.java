package com.wurldx.batchtracker;


import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import java.util.Calendar;

public class WeekFragment extends Fragment {

    private static final int COLOR_ACCENT = 0xFF0047AB;
    private static final int COLOR_BG     = 0xFFE8F4FD;
    private static final int COLOR_WHITE  = 0xFFFFFFFF;
    private static final int COLOR_BORDER = 0xFFD1E5F7;
    private static final int COLOR_MUTED  = 0xFF555555;
    private static final int COLOR_DARK   = 0xFF1A1A1A;

    private Calendar currentWeekBase;
    private LinearLayout weekDaysList;
    private TextView weekTitle;
    private TextView weekDateRange;

    private static final String[] DAY_LETTERS = {"S","M","T","W","T","F","S"};
    private static final String[] DAY_FULL    = {"Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        currentWeekBase = Calendar.getInstance();

        ScrollView scroll = new ScrollView(requireContext());
        scroll.setBackgroundColor(COLOR_BG);

        LinearLayout root = new LinearLayout(requireContext());
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(20), dp(20), dp(20), dp(20));

        // Nav card
        CardView navCard = makeCard(12);
        LinearLayout navRow = new LinearLayout(requireContext());
        navRow.setOrientation(LinearLayout.HORIZONTAL);
        navRow.setPadding(dp(12), dp(16), dp(12), dp(16));
        navRow.setGravity(Gravity.CENTER_VERTICAL);

        Button prevBtn = makeNavButton("‹");
        prevBtn.setOnClickListener(v -> changeWeek(-1));

        LinearLayout centerInfo = new LinearLayout(requireContext());
        centerInfo.setOrientation(LinearLayout.VERTICAL);
        centerInfo.setGravity(Gravity.CENTER);
        centerInfo.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));

        weekTitle = new TextView(requireContext());
        weekTitle.setTextSize(20);
        weekTitle.setTypeface(null, Typeface.BOLD);
        weekTitle.setTextColor(COLOR_ACCENT);
        weekTitle.setGravity(Gravity.CENTER);
        centerInfo.addView(weekTitle);

        weekDateRange = new TextView(requireContext());
        weekDateRange.setTextSize(13);
        weekDateRange.setTextColor(COLOR_MUTED);
        weekDateRange.setGravity(Gravity.CENTER);
        centerInfo.addView(weekDateRange);

        Button nextBtn = makeNavButton("›");
        nextBtn.setOnClickListener(v -> changeWeek(1));

        navRow.addView(prevBtn);
        navRow.addView(centerInfo);
        navRow.addView(nextBtn);
        navCard.addView(navRow);
        root.addView(navCard);
        addSpacing(root, 12);

        weekDaysList = new LinearLayout(requireContext());
        weekDaysList.setOrientation(LinearLayout.VERTICAL);
        root.addView(weekDaysList);

        renderWeek();
        scroll.addView(root);
        return scroll;
    }

    private void changeWeek(int direction) {
        currentWeekBase.add(Calendar.DAY_OF_MONTH, direction * 7);
        renderWeek();
    }

    private void renderWeek() {
        if (weekDaysList == null) return;

        Calendar startOfWeek = BatchHelper.getStartOfWeek(currentWeekBase);
        Calendar endOfWeek = (Calendar) startOfWeek.clone();
        endOfWeek.add(Calendar.DAY_OF_MONTH, 6);

        weekTitle.setText("Week " + BatchHelper.getWeekNumber(startOfWeek));
        weekDateRange.setText(BatchHelper.formatDateShort(startOfWeek) + " – " + BatchHelper.formatDateShort(endOfWeek));
        weekDaysList.removeAllViews();

        for (int i = 0; i < 7; i++) {
            Calendar dayDate = (Calendar) startOfWeek.clone();
            dayDate.add(Calendar.DAY_OF_MONTH, i);
            boolean today = BatchHelper.isToday(dayDate);
            String batch  = BatchHelper.getBatchForDate(dayDate);

            CardView dayCard = makeCard(8);
            LinearLayout row = new LinearLayout(requireContext());
            row.setOrientation(LinearLayout.HORIZONTAL);
            row.setPadding(dp(14), dp(14), dp(14), dp(14));
            row.setGravity(Gravity.CENTER_VERTICAL);

            // Letter circle
            TextView letterView = new TextView(requireContext());
            letterView.setText(DAY_LETTERS[i]);
            letterView.setTextSize(16);
            letterView.setTypeface(null, Typeface.BOLD);
            letterView.setTextColor(today ? COLOR_WHITE : COLOR_ACCENT);
            letterView.setGravity(Gravity.CENTER);
            GradientDrawable circleBg = new GradientDrawable();
            circleBg.setShape(GradientDrawable.OVAL);
            circleBg.setColor(today ? COLOR_ACCENT : COLOR_BG);
            circleBg.setStroke(dp(1), COLOR_BORDER);
            letterView.setBackground(circleBg);
            int circleSize = dp(36);
            LinearLayout.LayoutParams circleParams = new LinearLayout.LayoutParams(circleSize, circleSize);
            circleParams.setMargins(0, 0, dp(14), 0);
            letterView.setLayoutParams(circleParams);
            row.addView(letterView);

            // Info
            LinearLayout info = new LinearLayout(requireContext());
            info.setOrientation(LinearLayout.VERTICAL);
            info.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));

            TextView dayName = new TextView(requireContext());
            dayName.setText(DAY_FULL[i]);
            dayName.setTextSize(12);
            dayName.setTextColor(COLOR_MUTED);
            info.addView(dayName);

            TextView fullDate = new TextView(requireContext());
            fullDate.setText(BatchHelper.formatDateLong(dayDate));
            fullDate.setTextSize(14);
            fullDate.setTypeface(null, Typeface.BOLD);
            fullDate.setTextColor(COLOR_DARK);
            info.addView(fullDate);

            row.addView(info);
            row.addView(makeBadge(batch, today));
            dayCard.addView(row);
            weekDaysList.addView(dayCard);
            addSpacing(weekDaysList, 8);
        }
    }

    private Button makeNavButton(String text) {
        Button btn = new Button(requireContext());
        btn.setText(text);
        btn.setTextSize(24);
        btn.setTextColor(COLOR_ACCENT);
        btn.setTypeface(null, Typeface.BOLD);
        GradientDrawable bg = new GradientDrawable();
        bg.setColor(COLOR_BG);
        bg.setCornerRadius(dp(50));
        bg.setStroke(dp(1), COLOR_BORDER);
        btn.setBackground(bg);
        int size = dp(44);
        btn.setLayoutParams(new LinearLayout.LayoutParams(size, size));
        btn.setPadding(0, 0, 0, 0);
        btn.setGravity(Gravity.CENTER);
        return btn;
    }

    private TextView makeBadge(String text, boolean highlight) {
        TextView badge = new TextView(requireContext());
        badge.setText(text);
        badge.setTextSize(20);
        badge.setTypeface(null, Typeface.BOLD);
        badge.setTextColor(highlight ? COLOR_WHITE : COLOR_ACCENT);
        badge.setGravity(Gravity.CENTER);
        GradientDrawable bg = new GradientDrawable();
        bg.setColor(highlight ? COLOR_ACCENT : COLOR_BG);
        bg.setCornerRadius(dp(20));
        bg.setStroke(dp(1), COLOR_BORDER);
        badge.setBackground(bg);
        badge.setPadding(dp(16), dp(6), dp(16), dp(6));
        return badge;
    }

    private CardView makeCard(int radiusDp) {
        CardView card = new CardView(requireContext());
        card.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        card.setRadius(dp(radiusDp));
        card.setCardBackgroundColor(COLOR_WHITE);
        card.setCardElevation(dp(2));
        return card;
    }

    private void addSpacing(ViewGroup parent, int dpVal) {
        View space = new View(requireContext());
        space.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, dp(dpVal)));
        parent.addView(space);
    }

    private int dp(int dp) {
        return Math.round(dp * requireContext().getResources().getDisplayMetrics().density);
    }
}