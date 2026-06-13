package com.wurldx.batchtracker;
import android.app.Dialog;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import java.util.Calendar;

public class CalendarFragment extends Fragment {

    private static final int COLOR_ACCENT = 0xFF0047AB;
    private static final int COLOR_BG     = 0xFFE8F4FD;
    private static final int COLOR_WHITE  = 0xFFFFFFFF;
    private static final int COLOR_BORDER = 0xFFD1E5F7;
    private static final int COLOR_MUTED  = 0xFF555555;
    private static final int COLOR_DARK   = 0xFF1A1A1A;

    private int displayMonth, displayYear;
    private TextView monthYearDisplay;
    private GridLayout calendarGrid;

    private static final String[] DAY_HEADERS = {"Sun","Mon","Tue","Wed","Thu","Fri","Sat"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        Calendar today = Calendar.getInstance();
        displayMonth = today.get(Calendar.MONTH);
        displayYear  = today.get(Calendar.YEAR);

        ScrollView scroll = new ScrollView(requireContext());
        scroll.setBackgroundColor(COLOR_BG);

        LinearLayout root = new LinearLayout(requireContext());
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(16), dp(16), dp(16), dp(16));

        CardView calCard = new CardView(requireContext());
        calCard.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        calCard.setRadius(dp(16));
        calCard.setCardBackgroundColor(COLOR_WHITE);
        calCard.setCardElevation(dp(2));

        LinearLayout calInner = new LinearLayout(requireContext());
        calInner.setOrientation(LinearLayout.VERTICAL);
        calInner.setPadding(dp(16), dp(20), dp(16), dp(20));

        // Nav row
        LinearLayout navRow = new LinearLayout(requireContext());
        navRow.setOrientation(LinearLayout.HORIZONTAL);
        navRow.setGravity(Gravity.CENTER_VERTICAL);
        navRow.setPadding(0, 0, 0, dp(16));

        Button prevBtn = makeNavBtn("‹");
        prevBtn.setOnClickListener(v -> changeMonth(-1));

        monthYearDisplay = new TextView(requireContext());
        monthYearDisplay.setTextSize(20);
        monthYearDisplay.setTypeface(null, Typeface.BOLD);
        monthYearDisplay.setTextColor(COLOR_ACCENT);
        monthYearDisplay.setGravity(Gravity.CENTER);
        monthYearDisplay.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));

        Button nextBtn = makeNavBtn("›");
        nextBtn.setOnClickListener(v -> changeMonth(1));

        navRow.addView(prevBtn);
        navRow.addView(monthYearDisplay);
        navRow.addView(nextBtn);
        calInner.addView(navRow);

        calendarGrid = new GridLayout(requireContext());
        calendarGrid.setColumnCount(7);
        calendarGrid.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        calInner.addView(calendarGrid);

        calCard.addView(calInner);
        root.addView(calCard);

        renderCalendar();
        scroll.addView(root);
        return scroll;
    }

    private void changeMonth(int direction) {
        displayMonth += direction;
        if (displayMonth > 11) { displayMonth = 0; displayYear++; }
        else if (displayMonth < 0) { displayMonth = 11; displayYear--; }
        renderCalendar();
    }

    private void renderCalendar() {
        if (calendarGrid == null) return;
        calendarGrid.removeAllViews();

        monthYearDisplay.setText(BatchHelper.getMonthYear(displayMonth, displayYear));
        Calendar today = Calendar.getInstance();

        for (String header : DAY_HEADERS) {
            TextView hv = new TextView(requireContext());
            hv.setText(header);
            hv.setTextSize(11);
            hv.setTextColor(COLOR_MUTED);
            hv.setTypeface(null, Typeface.BOLD);
            hv.setGravity(Gravity.CENTER);
            hv.setPadding(0, 0, 0, dp(8));
            GridLayout.LayoutParams lp = new GridLayout.LayoutParams();
            lp.width = 0;
            lp.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1, GridLayout.FILL, 1f);
            hv.setLayoutParams(lp);
            calendarGrid.addView(hv);
        }

        Calendar firstDay = Calendar.getInstance();
        firstDay.set(displayYear, displayMonth, 1);
        int startOffset  = firstDay.get(Calendar.DAY_OF_WEEK) - 1;
        int daysInMonth  = firstDay.getActualMaximum(Calendar.DAY_OF_MONTH);

        for (int i = 0; i < startOffset; i++) {
            View empty = new View(requireContext());
            GridLayout.LayoutParams lp = new GridLayout.LayoutParams();
            lp.width = 0;
            lp.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1, GridLayout.FILL, 1f);
            empty.setLayoutParams(lp);
            calendarGrid.addView(empty);
        }

        for (int day = 1; day <= daysInMonth; day++) {
            final int fd = day;
            boolean isToday = displayYear == today.get(Calendar.YEAR) &&
                              displayMonth == today.get(Calendar.MONTH) &&
                              day == today.get(Calendar.DAY_OF_MONTH);

            TextView cell = new TextView(requireContext());
            cell.setText(String.valueOf(day));
            cell.setTextSize(15);
            cell.setTypeface(null, Typeface.BOLD);
            cell.setGravity(Gravity.CENTER);

            GradientDrawable cellBg = new GradientDrawable();
            cellBg.setCornerRadius(dp(8));
            cellBg.setColor(isToday ? COLOR_WHITE : COLOR_BG);
            if (isToday) cellBg.setStroke(dp(2), COLOR_ACCENT);
            cell.setTextColor(isToday ? COLOR_ACCENT : COLOR_DARK);
            cell.setBackground(cellBg);

            GridLayout.LayoutParams lp = new GridLayout.LayoutParams();
            lp.width = 0;
            lp.height = dp(44);
            lp.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1, GridLayout.FILL, 1f);
            lp.setMargins(dp(3), dp(3), dp(3), dp(3));
            cell.setLayoutParams(lp);

            cell.setOnClickListener(v -> openDayModal(fd, displayMonth, displayYear));
            cell.setOnTouchListener((v, event) -> {
                if (event.getAction() == android.view.MotionEvent.ACTION_DOWN) {
                    cellBg.setColor(COLOR_ACCENT);
                    cell.setTextColor(COLOR_WHITE);
                } else if (event.getAction() == android.view.MotionEvent.ACTION_UP ||
                           event.getAction() == android.view.MotionEvent.ACTION_CANCEL) {
                    cellBg.setColor(isToday ? COLOR_WHITE : COLOR_BG);
                    cell.setTextColor(isToday ? COLOR_ACCENT : COLOR_DARK);
                }
                return false;
            });

            calendarGrid.addView(cell);
        }
    }

    private void openDayModal(int day, int month, int year) {
        Calendar selected = Calendar.getInstance();
        selected.set(year, month, day);

        Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        if (dialog.getWindow() != null)
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        LinearLayout modalRoot = new LinearLayout(requireContext());
        modalRoot.setOrientation(LinearLayout.VERTICAL);
        modalRoot.setPadding(dp(24), dp(24), dp(24), dp(24));
        GradientDrawable modalBg = new GradientDrawable();
        modalBg.setColor(COLOR_WHITE);
        modalBg.setCornerRadius(dp(20));
        modalRoot.setBackground(modalBg);

        // Close row
        LinearLayout closeRow = new LinearLayout(requireContext());
        closeRow.setGravity(Gravity.END);
        TextView closeBtn = new TextView(requireContext());
        closeBtn.setText("✕");
        closeBtn.setTextSize(20);
        closeBtn.setTextColor(COLOR_MUTED);
        closeBtn.setOnClickListener(v -> dialog.dismiss());
        closeRow.addView(closeBtn);
        modalRoot.addView(closeRow);

        TextView dateText = new TextView(requireContext());
        dateText.setText(BatchHelper.formatDateLong(selected));
        dateText.setTextSize(14);
        dateText.setTextColor(COLOR_MUTED);
        dateText.setGravity(Gravity.CENTER);
        dateText.setTypeface(null, Typeface.BOLD);
        modalRoot.addView(dateText);

        TextView batchText = new TextView(requireContext());
        batchText.setText(BatchHelper.getBatchForDate(selected));
        batchText.setTextSize(72);
        batchText.setTypeface(null, Typeface.BOLD);
        batchText.setTextColor(COLOR_ACCENT);
        batchText.setGravity(Gravity.CENTER);
        batchText.setPadding(0, dp(4), 0, dp(16));
        modalRoot.addView(batchText);

        View divider = new View(requireContext());
        divider.setBackgroundColor(COLOR_BORDER);
        divider.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(1)));
        modalRoot.addView(divider);
        addSpacing(modalRoot, 12);

        int[] intervals = {1, 3, 6, 9, 12};
        for (int months : intervals) {
            Calendar past     = BatchHelper.getPastDate(selected, months);
            String pastBatch  = BatchHelper.getBatchForDate(past);

            LinearLayout row = new LinearLayout(requireContext());
            row.setOrientation(LinearLayout.HORIZONTAL);
            row.setGravity(Gravity.CENTER_VERTICAL);
            row.setPadding(dp(14), dp(12), dp(14), dp(12));
            GradientDrawable rowBg = new GradientDrawable();
            rowBg.setColor(COLOR_BG);
            rowBg.setCornerRadius(dp(8));
            rowBg.setStroke(dp(1), COLOR_BORDER);
            row.setBackground(rowBg);

            TextView lbl = new TextView(requireContext());
            lbl.setText(months + " Month" + (months > 1 ? "s" : ""));
            lbl.setTextSize(13);
            lbl.setTypeface(null, Typeface.BOLD);
            lbl.setTextColor(COLOR_ACCENT);
            lbl.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
            row.addView(lbl);

            TextView dateVal = new TextView(requireContext());
            dateVal.setText(BatchHelper.formatDateShort(past));
            dateVal.setTextSize(13);
            dateVal.setTextColor(COLOR_DARK);
            dateVal.setTypeface(null, Typeface.BOLD);
            row.addView(dateVal);

            View gap = new View(requireContext());
            gap.setLayoutParams(new LinearLayout.LayoutParams(dp(8), ViewGroup.LayoutParams.MATCH_PARENT));
            row.addView(gap);

            TextView badgeView = new TextView(requireContext());
            badgeView.setText(pastBatch);
            badgeView.setTextSize(14);
            badgeView.setTypeface(null, Typeface.BOLD);
            badgeView.setTextColor(COLOR_ACCENT);
            badgeView.setGravity(Gravity.CENTER);
            GradientDrawable badgeBg = new GradientDrawable();
            badgeBg.setColor(COLOR_WHITE);
            badgeBg.setCornerRadius(dp(4));
            badgeBg.setStroke(dp(1), COLOR_BORDER);
            badgeView.setBackground(badgeBg);
            badgeView.setPadding(dp(8), dp(2), dp(8), dp(2));
            row.addView(badgeView);

            modalRoot.addView(row);
            addSpacing(modalRoot, 8);
        }

        dialog.setContentView(modalRoot);
        if (dialog.getWindow() != null)
            dialog.getWindow().setLayout(
                    (int)(requireContext().getResources().getDisplayMetrics().widthPixels * 0.88),
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    private Button makeNavBtn(String text) {
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