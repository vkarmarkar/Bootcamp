package pl.tajchert.glass_qrcards;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pl.itraff.TestApi.ItraffApi.ItraffApi;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RemoteViews;

import com.google.android.glass.app.Card;
import com.google.android.glass.media.CameraManager;
import com.google.android.glass.widget.CardScrollAdapter;
import com.google.android.glass.widget.CardScrollView;

public class ScanActivity extends Activity {

	private CardManager cardManager;
	private List<Card> mCards = new ArrayList<Card>();
	private CardScrollView mCardScrollView;
	private ExampleCardScrollAdapter adapter;
	private CameraPreview _cameraPreview;
	private RemoteViews _remoteView;

	private Context _context = this;

	// Value that tell us on which position on list there is a card
	// "tap to new scan", because sometimes on first position there is
	// result/warning card
	private int scanCardNumber = 0;

	// QRCode stuff
	private File pictureFile;
	private static final int TAKE_PICTURE_REQUEST = 1;
	private String path;
	private AppPref appPref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		android.os.Debug.waitForDebugger();
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		// Cards start
		cardManager = new CardManager(this, mCards);
		mCardScrollView = new CardScrollView(this);
		adapter = new ExampleCardScrollAdapter();
		mCardScrollView.setAdapter(adapter);
		mCardScrollView.setOnItemClickListener(adapter);
		mCardScrollView.activate();
		setContentView(mCardScrollView);

		_cameraPreview = new CameraPreview(this);
		setContentView(_cameraPreview);

	}

	@Override
	protected void onResume() {
		super.onResume();
		// Clean card list, and get latest values
		appPref = new AppPref(this);
		mCards = new ArrayList<Card>();
		cardManager.setCards(mCards);
		// cardManager.createScanCard();
		scanCardNumber = 0;
		cardManager.createListCards(appPref.getScans());
		mCards = cardManager.getCards();
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	private void takePicture(String id, String title, String bid, String bin,
			String location, String condition) {
		// This is due saving picture that user can approve photo only after 2-3
		// seconds, otherwise scanning would fail
		// Toast.makeText(getApplicationContext(), "Wait...",
		// Toast.LENGTH_LONG).show();
		// Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// startActivityForResult(intent, TAKE_PICTURE_REQUEST);

		// Cards start
		cardManager = new CardManager(this, mCards);
		cardManager.createTitleCard(title, bid, bin, id);
		cardManager.createContentCard(id, location, condition);
		cardManager.createSaveCard(id);
		cardManager.createScanCard();
		mCardScrollView = new CardScrollView(this);
		adapter = new ExampleCardScrollAdapter();
		mCardScrollView.setAdapter(adapter);
		mCardScrollView.setOnItemClickListener(adapter);
		mCardScrollView.activate();
		setContentView(mCardScrollView);

		adapter.notifyDataSetChanged();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == TAKE_PICTURE_REQUEST && resultCode == RESULT_OK) {
			String picturePath = data
					.getStringExtra(CameraManager.EXTRA_PICTURE_FILE_PATH);
			processPicture(picturePath);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void processPicture(final String picturePath) {
		pictureFile = new File(picturePath);
		path = picturePath;
		if (pictureFile.exists()) {
			// Picture was successful taken and saved
			Log.d(Tools.TAG, "PICTURE READY");
			new GetQRCodeTask().execute();
		} else {
			// Too fast, user should wait 2 second after taking a picture, due
			// to saving it
			new TooFastInfoTask().execute();
		}
	}

	// Used to put class that user to fast jumped from taking picture to
	// uploading it (it wasn't saved yet)
	public class TooFastInfoTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			scanCardNumber = 1;
			Log.d(Tools.TAG, "Too fast card");
			// Clean list of card (to get rid of "please wait" card)
			mCards = new ArrayList<Card>();
			cardManager.setCards(mCards);
			cardManager.createScanTooFastCard();// Add card with warning
			cardManager.createScanCard();// Add card with new scan

			mCards = cardManager.getCards();
			return true;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			adapter.notifyDataSetChanged();
		}
	}

	// Picture -> qrcode content via Esponce.com -> qr code content
	public class GetQRCodeTask extends AsyncTask<Void, Void, Boolean> {
		// used to pass code content and date of scan from Background to
		// PostExecute
		private String content;
		private String date;

		@Override
		protected Boolean doInBackground(Void... params) {
			// QRCodeClient client = new QRCodeClient();
			// For scanning a file from /res folder
			// InputStream fileInputStream =
			// getResources().openRawResource(R.drawable.qr_code_test_p);
			BitmapFactory.Options bfOptions = new BitmapFactory.Options();
			bfOptions.inDither = false; // Disable Dithering mode
			bfOptions.inPurgeable = true; // Tell to gc that whether it needs
											// free memory, the Bitmap can be
											// cleared
			bfOptions.inInputShareable = true; // Which kind of reference will
												// be used to recover the Bitmap
												// data after being clear, when
												// it will be used in the future
			bfOptions.inTempStorage = new byte[32 * 1024];

			Bitmap bMap = BitmapFactory.decodeFile(path, bfOptions);
			bMap = Bitmap.createScaledBitmap(bMap, 1200, 900, false);// Lets
																		// change
																		// size
																		// to a
																		// bit
																		// smaller,
																		// I
																		// didn't
																		// experiment
																		// a lot
																		// with
																		// it so
																		// it
																		// can
																		// be
																		// not
																		// optimal
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			bMap.compress(CompressFormat.PNG, 0, bos);
			byte[] bitmapdata = bos.toByteArray();
			ByteArrayInputStream fileInputStream = new ByteArrayInputStream(
					bitmapdata);

			BufferedInputStream bis = new BufferedInputStream(fileInputStream);
			try {
				// content = client.decode(bis);
			} catch (Exception e) {
				e.printStackTrace();
			}
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm   dd.MM",
					java.util.Locale.getDefault());
			Date dt = new Date();
			date = sdf.format(dt);
			return true;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			// Clean list of card (to get rid of "please wait" card)
			mCards = new ArrayList<Card>();
			cardManager.setCards(mCards);
			scanCardNumber = 0;// tap to scan card is first

			if (content != null && appPref != null) {
				// This is content of our qr code
				Log.d(Tools.TAG, "Content: " + content);
				appPref.addScan(content + Tools.SEPARATOR + date);
				cardManager.createResultCard(content, date);
				scanCardNumber = 1;// tap to scan card is second, first is card
									// with result of current scan
			}
			// cardManager.createScanCard();
			cardManager.createListCards(appPref.getScans());// Get historical
															// scans and add as
															// card
			adapter.notifyDataSetChanged();
		}
	}

	private class ExampleCardScrollAdapter extends CardScrollAdapter implements
			OnItemClickListener {
		@Override
		public int getCount() {
			return mCards.size();
		}

		@Override
		public Object getItem(int position) {
			return mCards.get(position);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			return mCards.get(position).getView();
		}

		@Override
		public int getPosition(Object item) {
			return mCards.indexOf(item);
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Log.d(Tools.TAG, "CLICKED: " + position + ", scan pos: "
					+ scanCardNumber);
			if (position == 3) {
				_cameraPreview = new CameraPreview(getApplicationContext());
				setContentView(_cameraPreview);
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		// Handle tap events.
		case KeyEvent.KEYCODE_CAMERA:
			android.util.Log.d("CameraActivity", "Camera button pressed.");
			_cameraPreview.getCamera().takePicture(null, null,
					new SavePicture());
			android.util.Log.d("CameraActivity", "Picture taken.");

			return true;
		case KeyEvent.KEYCODE_DPAD_CENTER:
			// case KeyEvent.KEYCODE_ENTER:
			// android.util.Log.d("CameraActivity", "Tap.");
			// _cameraPreview.getCamera().takePicture(null, null,
			// new SavePicture());

			return true;
		default:
			return super.onKeyDown(keyCode, event);
		}
	}

	class SavePicture implements Camera.PictureCallback {

		public final Integer CLIENT_API_ID = 43023;
		public final String CLIENT_API_KEY = "8dcc2ca356";

		private static final String TAG = "TestApi";

		// handler that receives response from api
		@SuppressLint("HandlerLeak")
		private Handler itraffApiHandler = new Handler() {
			// callback from api
			@Override
			public void handleMessage(Message msg) {
				Log.d("MSG", "Yippie");
				// dismissWaitDialog();
				Bundle data = msg.getData();
				if (data != null) {
					Integer status = data.getInt(ItraffApi.STATUS, -1);
					String response = data.getString(ItraffApi.RESPONSE);
					Log.d("status", status + "");
					Log.d("response", response);
					// LiveCard photoCard = new LiveCard(_context, TAG);
					// photoCard.setViews(_remoteView);
					// photoCard.setDirectRenderingEnabled(true);
					if (response.contains("[\"1\"]")) {
						takePicture("[\"1\"]", "Toy 3-layer Rubik‘s Cube",
								"Bid: $3.99", "Buy: $6.00", "Location: China",
								"Condition: New");
					} else if (response.contains("[\"2\"]")) {
						takePicture("[\"2\"]", "Dymo Stamps 30576",
								"Bid: $15.95", "Buy: $20.00", "Location: USA",
								"Condition: New");
					} else if (response.contains("[\"3\"]")) {
						takePicture("[\"3\"]", "Sphero", "Bid: $70.00",
								"Buy: $100.00", "Location: USA",
								"Condition: Used");
					} else {
						takePicture("", "Sphero", "Bid: $70.00",
								"Bin: $100.00", "Location: USA",
								"Condition: Used");
					}
					// photoCard.publish(PublishMode.REVEAL);
				}
			}
		};

		@Override
		public void onPictureTaken(byte[] bytes, Camera camera) {
			android.util.Log.d("CameraActivity", "In onPictureTaken().");
			Bitmap image = BitmapFactory
					.decodeByteArray(bytes, 0, bytes.length);
			android.util.Log.d("Image:", image.toString());

			if (image != null) {
				android.util.Log.d("", "image != null");

				// chceck internet connection
				if (ItraffApi.isOnline(getApplicationContext())) {
					// showWaitDialog();
					SharedPreferences prefs = PreferenceManager
							.getDefaultSharedPreferences(getBaseContext());
					// send photo
					ItraffApi api = new ItraffApi(CLIENT_API_ID,
							CLIENT_API_KEY, TAG, true);
					Log.d("KEY", CLIENT_API_ID.toString());

					api.setMode(ItraffApi.MODE_SINGLE);

					ByteArrayOutputStream stream = new ByteArrayOutputStream();
					image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
					byte[] pictureData = stream.toByteArray();
					Log.d("about to send photo", "Yay");
					api.sendPhoto(pictureData, itraffApiHandler,
							prefs.getBoolean("allResults", true));
				}
			}

		}
	}
}
