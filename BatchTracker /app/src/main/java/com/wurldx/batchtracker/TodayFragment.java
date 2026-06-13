package com.wurldx.batchtracker;


import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import java.util.Calendar;

public class TodayFragment extends Fragment {

    private static final int COLOR_ACCENT = 0xFF0047AB;
    private static final int COLOR_BG     = 0xFFE8F4FD;
    private static final int COLOR_WHITE  = 0xFFFFFFFF;
    private static final int COLOR_BORDER = 0xFFD1E5F7;
    private static final int COLOR_MUTED  = 0xFF555555;
    private static final int COLOR_DARK   = 0xFF1A1A1A;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        ScrollView scroll = new ScrollView(requireContext());
        scroll.setBackgroundColor(COLOR_BG);

        LinearLayout root = new LinearLayout(requireContext());
        root.setOrientation(LinearLayout.VERTICAL);
        int pad = dp(20);
        root.setPadding(pad, pad, pad, pad);

        Calendar today = Calendar.getInstance();

        // Main Card
        CardView mainCard = makeCard();
        LinearLayout mainInner = new LinearLayout(requireContext());
        mainInner.setOrientation(LinearLayout.VERTICAL);
        mainInner.setPadding(dp(20), dp(20), dp(20), dp(20));

        TextView label = new TextView(requireContext());
        label.setText("TODAY'S ANALYSIS");
        label.setTextSize(11);
        label.setLetterSpacing(0.1f);
        label.setTextColor(COLOR_MUTED);
        label.setTypeface(null, Typeface.BOLD);
        mainInner.addView(label);

        TextView batchView = new TextView(requireContext());
        batchView.setText(BatchHelper.getBatchForDate(today));
        batchView.setTextSize(72);
        batchView.setTypeface(null, Typeface.BOLD);
        batchView.setTextColor(COLOR_ACCENT);
        mainInner.addView(batchView);

        TextView dateView = new TextView(requireContext());
        dateView.setText(BatchHelper.formatDateLong(today));
        dateView.setTextSize(15);
        dateView.setTextColor(COLOR_DARK);
        dateView.setTypeface(null, Typeface.BOLD);
        mainInner.addView(dateView);

        mainCard.addView(mainInner);
        root.addView(mainCard);
        addSpacing(root, 16);

        // Section header
        TextView retroHeader = new TextView(requireContext());
        retroHeader.setText("RETROSPECTIVES");
        retroHeader.setTextSize(11);
        retroHeader.setLetterSpacing(0.1f);
        retroHeader.setTextColor(COLOR_MUTED);
        retroHeader.setTypeface(null, Typeface.BOLD);
        retroHeader.setPadding(dp(4), 0, 0, dp(8));
        root.addView(retroHeader);

        // Retro rows
        int[] intervals = {1, 3, 6, 9, 12};
        for (int months : intervals) {
            Calendar past = BatchHelper.getPastDate(today, months);
            String batch  = BatchHelper.getBatchForDate(past);

            CardView pillCard = makeCard();
            LinearLayout row = new LinearLayout(requireContext());
            row.setOrientation(LinearLayout.HORIZONTAL);
            row.setPadding(dp(16), dp(14), dp(16), dp(14));
            row.setGravity(Gravity.CENTER_VERTICAL);

            LinearLayout left = new LinearLayout(requireContext());
            left.setOrientation(LinearLayout.VERTICAL);
            left.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));

            TextView monthLabel = new TextView(requireContext());
            monthLabel.setText(months + " MONTH" + (months > 1 ? "S" : "") + " AGO");
            monthLabel.setTextSize(10);
            monthLabel.setLetterSpacing(0.08f);
            monthLabel.setTextColor(COLOR_ACCENT);
            monthLabel.setTypeface(null, Typeface.BOLD);
            left.addView(monthLabel);

            TextView pastDateView = new TextView(requireContext());
            pastDateView.setText(BatchHelper.formatDateShort(past));
            pastDateView.setTextSize(14);
            pastDateView.setTextColor(COLOR_DARK);
            pastDateView.setTypeface(null, Typeface.BOLD);
            left.addView(pastDateView);

            row.addView(left);
            row.addView(makeBadge(batch));

            pillCard.addView(row);
            root.addView(pillCard);
            addSpacing(root, 8);
        }

        scroll.addView(root);
        return scroll;
    }

    private CardView makeCard() {
        CardView card = new CardView(requireContext());
        card.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        card.setRadius(dp(12));
        card.setCardBackgroundColor(COLOR_WHITE);
        card.setCardElevation(dp(2));
        return card;
    }

    private TextView makeBadge(String text) {
        TextView badge = new TextView(requireContext());
        badge.setText(text);
        badge.setTextSize(22);
        badge.setTypeface(null, Typeface.BOLD);
        badge.setTextColor(COLOR_ACCENT);
        badge.setGravity(Gravity.CENTER);
        GradientDrawable bg = new GradientDrawable();
        bg.setColor(COLOR_BG);
        bg.setCornerRadius(dp(20));
        bg.setStroke(dp(1), COLOR_BORDER);
        badge.setBackground(bg);
        badge.setPadding(dp(14), dp(6), dp(14), dp(6));
        return badge;
    }

    private void addSpacing(LinearLayout parent, int dpVal) {
        View space = new View(requireContext());
        space.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, dp(dpVal)));
        parent.addView(space);
    }

    private int dp(int dp) {
        return Math.round(dp * requireContext().getResources().getDisplayMetrics().density);
    }
}