
package com.zeitraub.calendar.view.preferences;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.preference.MultiSelectListPreference;
import android.provider.CalendarContract;
import android.view.View;
import android.view.ViewGroup;

/**
 * Preferences for preferences
 * 
 * @author Torsten
 *
 */
public class CalendarListPreference extends MultiSelectListPreference {

    @Override
    protected View onCreateView(ViewGroup parent) {
        return super.onCreateView(parent);
    }

    public CalendarListPreference(Context context) {
        super(context);

        ContentResolver cr;
        Cursor cursor;
        String[] projection = new String[] { CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, CalendarContract.Calendars._ID };
        String selection = "(" + CalendarContract.Calendars.VISIBLE + " = ?)";
        String[] selectionArgs = new String[] { "1" };
        
        List<CharSequence> entries = new ArrayList<CharSequence>();
        List<CharSequence> entriesValues = new ArrayList<CharSequence>();

        cr = context.getContentResolver();
        cursor = cr.query(CalendarContract.Calendars.CONTENT_URI, projection, selection, selectionArgs, null);

        while (cursor.moveToNext()) {
            String name = cursor.getString(0);
            String id = cursor.getString(1);            
            entries.add(name);
            entriesValues.add(id);
        }

        setEntries(entries.toArray(new CharSequence[] {}));
        setEntryValues(entriesValues.toArray(new CharSequence[] {}));
    }
    
    public void update(Set<String> values) {
        setValues(values);
    }
    
}
