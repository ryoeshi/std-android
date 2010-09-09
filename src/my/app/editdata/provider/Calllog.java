package my.app.editdata.provider;

import android.net.Uri;
import android.provider.BaseColumns;

public class Calllog {

	public static final String AUTHORITY = "my.app.editdata.provider.Calllog";

	public static final class Calllogs implements BaseColumns {

		private Calllogs() {
			;
		}

		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/calllogs");

		public static final String DATE = "date";
		public static final String TYPE = "type";
		public static final String NUMBER = "number";

		public static final String IMPORTANCE = "importance";

	}
}
