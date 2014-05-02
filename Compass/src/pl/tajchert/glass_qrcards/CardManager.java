package pl.tajchert.glass_qrcards;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.content.Context;

import com.google.android.glass.app.Card;
import com.google.android.glass.app.Card.ImageLayout;

public class CardManager {

	private List<Card> mCards = new ArrayList<Card>();

	public void setCards(List<Card> mCards) {
		this.mCards = mCards;
	}

	private Context context;

	public CardManager(Context context, List<Card> cardList) {
		if (cardList == null || context == null) {
			throw new IllegalArgumentException(
					"CardList or context can't be null");
		}
		this.context = context;
		this.mCards = cardList;
	}

	public void createScanCard() {
		Card scanCard = new Card(context);
		scanCard.setText(context.getResources().getString(
				R.string.card_scan_code));
		scanCard.setFootnote("");
		if (mCards == null) {
			mCards = new ArrayList<Card>();
		}
		mCards.add(scanCard);
	}

	public void createScanTooFastCard() {
		Card scanCard = new Card(context);
		scanCard.setText(context.getResources().getString(
				R.string.card_scan_too_fast));
		scanCard.setFootnote("");
		if (mCards == null) {
			mCards = new ArrayList<Card>();
		}
		mCards.add(scanCard);
	}

	public void createScanProgressCard() {
		Card scanCard = new Card(context);
		scanCard.setText(context.getResources().getString(
				R.string.card_scan_in_progress));
		scanCard.setFootnote("");
		if (mCards == null) {
			mCards = new ArrayList<Card>();
		}
		mCards.add(scanCard);
	}

	public void createResultCard(String content, String desc) {
		Card resultCard = new Card(context);
		resultCard.setText(content + "");
		resultCard.setFootnote(desc + "");
		if (mCards == null) {
			mCards = new ArrayList<Card>();
		}
		mCards.add(resultCard);
	}

	public void createBussinessCard() {
		Card bussinessCard = new Card(context);
		bussinessCard.setFootnote("");
		bussinessCard.setImageLayout(Card.ImageLayout.LEFT);
		bussinessCard.setText("Testing");
		bussinessCard.addImage(R.drawable.ic_launcher);

		if (mCards == null) {
			mCards = new ArrayList<Card>();
		}
		mCards.add(bussinessCard);
	}

	public void createTitleCard(String title, String bid, String bin, String id) {
		Card bussinessCard = new Card(context);
		bussinessCard.setFootnote(title);
		bussinessCard.setImageLayout(Card.ImageLayout.LEFT);
		bussinessCard.setText(bid + "                         " + bin);
		int image = 0;
		if (id.equals("[\"1\"]")) {
			image = R.drawable.rubiks;
		} else if (id.equals("[\"2\"]")) {
			image = R.drawable.dymo;
		} else if (id.equals("[\"3\"]")) {
			image = R.drawable.sphero;
		} else {
			image = R.drawable.ic_launcher;
		}

		bussinessCard.addImage(image);

		if (mCards == null) {
			mCards = new ArrayList<Card>();
		}
		mCards.add(bussinessCard);
	}

	public void createContentCard(String id, String location, String condition) {
		Card bussinessCard = new Card(context);
		bussinessCard.setFootnote("");
		bussinessCard.clearImages();
		if (id.equals("[\"1\"]")) {
			bussinessCard.addImage(R.drawable.rubiks);
		} else if (id.equals("[\"2\"]")) {
			bussinessCard.addImage(R.drawable.dymo);
		} else if (id.equals("[\"3\"]")) {
			bussinessCard.addImage(R.drawable.sphero);
		} else {
			bussinessCard.addImage(R.drawable.ic_launcher);
		}

		bussinessCard.setText(location + "            " + condition);

		if (mCards == null) {
			mCards = new ArrayList<Card>();
		}
		mCards.add(bussinessCard);
	}

	public void createSaveCard(String id) {
		Card bussinessCard = new Card(context);
		bussinessCard.setFootnote("");
		bussinessCard.clearImages();
		if (id.equals("[\"1\"]") || id.equals("[\"2\"]")
				|| id.equals("[\"3\"]")) {
			bussinessCard.setText("Save to Wish list");
		} else {
			bussinessCard.setText("No item to save");
		}

		if (mCards == null) {
			mCards = new ArrayList<Card>();
		}
		mCards.add(bussinessCard);
	}

	public void createListCards(Set<String> scans) {
		if (mCards == null) {
			mCards = new ArrayList<Card>();
		}
		String[] content;
		if (scans != null && scans.size() > 0) {
			for (String scan : scans) {
				content = scan.split(Tools.SEPARATOR);
				Card cardScan = new Card(context);
				cardScan.setText(content[0] + ""); // Main text area
				cardScan.setFootnote(content[1] + "");
				cardScan.setImageLayout(Card.ImageLayout.LEFT);
				cardScan.addImage(R.drawable.ic_launcher);
				mCards.add(cardScan);
			}
		}
	}

	public List<Card> getCards() {
		return mCards;
	}

}
