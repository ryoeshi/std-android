package my.app.editdata.provider;

import android.provider.BaseColumns;
import android.net.Uri;

public final class Schedule {

	public static final String AUTHORITY = "my.app.editdata.provider.Schedule";

	public static final class Schedules implements BaseColumns {

		private Schedules() {
			;
		}

		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/schedules");

		public static final String DATE = "date";
		public static final String PLAN = "plan";

		public static final String IMPORTANCE = "importance";

	}

}
