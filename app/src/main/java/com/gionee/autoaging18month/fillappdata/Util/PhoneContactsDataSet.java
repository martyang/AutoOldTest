package com.gionee.autoaging18month.fillappdata.Util;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.RawContacts;
import android.provider.ContactsContract.RawContacts.Data;

import com.gionee.autoaging18month.Utils.Log;

import java.util.Random;
//SimpleProgressDialog progress,
public class PhoneContactsDataSet {

	 public static synchronized void installPhoneContacts(Context context,
			int numContacts, boolean deleteExisting,
			String phoneNum) {

		ContentResolver resolver = context.getContentResolver();
		
		if (deleteExisting) {
			// Remove all the old data
			resolver.delete(RawContacts.CONTENT_URI, null, null);
		}

		if (numContacts < 0) {
			return;
		}
		
		Random rng = new Random(1000);
		boolean chinese = false;
//		progress.setMax(numContacts);
		
		for (int i = 1; i <= numContacts; i++) {
			if (!Configration.isTest){
				throw new IllegalStateException();
			}
			ContentValues map = new ContentValues();
			map.put(RawContacts.ACCOUNT_NAME, "Phone");
			map.put(RawContacts.ACCOUNT_TYPE, "Local Phone Account");
			Uri rawContactUri = resolver.insert(
                    RawContacts.CONTENT_URI, map);
			long rawContactId = ContentUris.parseId(rawContactUri);
			Log.i("contact_id "+rawContactId);
			// Generate a Name

			StringBuilder fullname = new StringBuilder();
			fullname.append("电话联系人");
			fullname.append(" " + i);
			chinese = true;

			map.clear();
            map.put(Data.RAW_CONTACT_ID, rawContactId);
            map.put(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE);
            map.put(StructuredName.PHONETIC_FAMILY_NAME, fullname.toString());
            resolver.insert(ContactsContract.Data.CONTENT_URI, map);
			
			
			// Create the phone number
            StringBuilder phonenum = new StringBuilder();
            if(!phoneNum.equals("")){
            	phonenum.append(Long.parseLong(phoneNum)+i-1);
            }else{
            	phonenum.append(rng.nextInt(1000));
            	phonenum.append("-");
            	phonenum.append(rng.nextInt(10000));
            }

			map.clear();
			map.put(Data.RAW_CONTACT_ID, rawContactId);
			map.put(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE);
			map.put(Phone.NUMBER, phonenum.toString());
			map.put(Phone.TYPE, Phone.TYPE_MOBILE);
			resolver.insert(ContactsContract.Data.CONTENT_URI,map);

			// Create the email address
			StringBuilder emailaddr = new StringBuilder();
			if (chinese) {
				if (rng.nextInt(1) == 1) {
					emailaddr.append(randomElement(MALE_FIRSTNAMES, rng));
				} else {
					emailaddr.append(randomElement(FEMALE_FIRSTNAMES, rng));
				}
				emailaddr.append(".");
				emailaddr.append(randomElement(LASTNAMES, rng));
			} else {
				int index;
				while (fullname.indexOf(" ") > -1) {
					index = fullname.indexOf(" ");
					fullname.replace(index, index + 1, "");
				}
				emailaddr.append(fullname);
			}
			emailaddr.append("@");
			emailaddr.append(randomElement(DOMAINS, rng));

			map.clear();
			map.put(Data.RAW_CONTACT_ID, rawContactId);
			map.put(Data.MIMETYPE, Email.CONTENT_ITEM_TYPE);
			map.put(Email.DATA, emailaddr.toString());
			map.put(Email.TYPE,Email.TYPE_MOBILE);
			resolver.insert(ContactsContract.Data.CONTENT_URI,map);
			
			
			fullname = null;
			phonenum = null;
			emailaddr = null;
			map = null;
			
//			progress.setProgress(i);
		}
	}

	static Object randomElement(Object inputset[], Random rng) {
		int idx = rng.nextInt(inputset.length);
		return inputset[idx];
	}

	// 100 Male first names chosen randomly from a bigger list
	private static final String[] MALE_FIRSTNAMES = new String[] { "Achilles",
		"Adalbert","Archibald", "Armondo", "Buddy", "Carrol", "Casimir", "Darin", "Davis", 
		"Devon", "Dick", "Diederich", "Franklin", "Fritz", "Henrik", "Hilliard", "Howi", "Yusuke",
		"Zahir", "Zakary", "Zohair","Suss","Lee" };

	// 100 Female first names chosen randomly fron a bigger list
	private static final String[] FEMALE_FIRSTNAMES = new String[] { "Adeola",
			"Ahulani", "Alesha", "Amie", "Angeline", "Angila", "Anya", "Areli",
			"Aretha", "Aurore", "Aviva", "Bedriska", "Brett", "Caitlynn",
			"Carmen", "Chandelle", "Charline", "Chikako", "China", "Cleta",
			"Coretta", "Cricket", "Crystani", "Cybele", "Cynara", "Damia",
			"Delpha", "Dolly", "Domoniqu", "Dreamalynn", "Eboni", "Eibhlin",
			"Eugenie", "Frayda", "Funsani", "Gertie", "Ghislaine", "Gianni",
			"Giustina", "Gosling", "Grazia", "Hachi", "Hexia", "Iku", "Ikuko",
			"Isha", "Isis", "Jannet", "Jeanie", "Kalika", "Kazuma", "Keyla",
			"Lanita", "Laurene", "Leida", "Lokelani", "Lore", "Ludie",
			"Ludivina", "Margo", "Margrett", "Marika", "Marilynn", "Mechola",
			"Meenatchi", "Minga", "Momiji", "Morgwen", "Motoko", "Nikia",
			"Nisha", "Nita", "Niu", "Olaug", "Olayinka", "Ollie", "Osyka",
			"Quenda", "Raruku", "Ryba", "Sarojini", "Srivatsan", "Stephain",
			"Swan", "Sydney", "Tandra", "Terezia", "Tiffani", "Tira", "Umeki",
			"Ushriya", "Vaughnie", "Verity", "Vicky", "Wanaka", "Willamina",
			"Yucie", "Yukino", "Yunlong", "Zia" };

	// 100 last names chosen randomly from a bigger list
	private static final String[] LASTNAMES = new String[] { "Amesbury",
			"Bacich", "Balbuena", "Bardwell", "Barino", "Bernatowi", "Bono",
			"Bonte", "Buhrke", "Buist", "Catrone", "Chang", "Cones", "Cragg",
			"Crnich", "Czupryna", "Dacpano", "Dapice", "Dare", "Degan",
			"Delagol", "Devotie", "Dibiase", "Dorne", "Dutka", "Eakles",
			"Eddinger", "Ehinger", "Estacion", "Figurski", "Filippo", "Gaiser",
			"Gautreau", "Gillogly", "Goertzen", "Henne", "Hiersche", "Hor",
			"Huang", "Juckett", "Kegel", "Laorange", "Leuasseur", "Ligons",
			"Lindsey", "Lingao", "Loosey", "Losecco", "Lotzer", "Makino",
			"Mamudoski", "Marchel", "Marchetti", "Mcspadden", "Merriam",
			"Mesia", "Moises", "Montandon", "Morrissey", "Okeson", "Olgvin",
			"Orlowsky", "Partingto", "Pavletic", "Pinela", "Plane", "Popek",
			"Prada", "Prettyman", "Pullum", "Reidler", "Romay", "Ruttman",
			"Samona", "Samul", "Searl", "Seppanen", "Shelvy", "Skulski",
			"Spadard", "Spiwak", "Squiers", "Stancil", "Staszak", "Stuckey",
			"Sturrock", "Sutton", "Sweazy", "Taragjini", "Tisinger", "Varnum",
			"Veile", "Viniard", "Wakenight", "Weible", "Wein", "Wittlinge",
			"Woodham", "Yanacek", "Zemon" };

	private static final String[] DOMAINS = new String[] { "gmail.com",
			"yahoo.com", "sohu.com", "126.net", "163.com",
			"sina.com.cn" };


}
