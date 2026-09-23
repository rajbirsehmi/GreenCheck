# Privacy Policy for GreenCheck

**Effective Date:** March 30, 2026  
**Last Updated:** March 30, 2026  
**App Name:** GreenCheck   
**Application Package Name:** `com.creative.greencheck`  
**Developer / Publisher:** Rajbir Sehmi  
**Contact Email / Support:** rajbirsinghsehmi93@gmail.com (or via GitHub: https://github.com/rajbirsehmi/GreenCheck/issues)   

---

## 1. Introduction & Overview

GreenCheck ("we", "our", or "us") is committed to protecting user privacy. This Privacy Policy outlines our data handling practices for the GreenCheck Android application (`com.creative.greencheck`).

GreenCheck is a 100% free, ad-free, open-source application designed to help users determine whether food products and ingredients are vegan.

We adhere strictly to Google Play Developer Policies, including the Google Play User Data Policy and Data Safety requirements.

---

## 2. Google Play Data Safety Disclosures

For transparency and compliance with Google Play's Data Safety form requirements:

- **Personal Data Collected:** None. We do not collect names, email addresses, phone numbers, physical addresses, financial information, biometric data, or location data.
- **Personal Data Shared:** None. We do not sell, rent, or share personal data with any third parties or advertisers.
- **Advertising & Tracking:** No third-party ad networks, tracking SDKs, or behavioral analytics (e.g., Google Analytics, Firebase Analytics, Facebook SDK) are used.
- **Security in Transit:** All network requests made by the app are encrypted in transit using standard HTTPS (TLS/SSL).
- **Accounts:** No account creation or login is required to use GreenCheck.

---

## 3. Device Permissions & Ephemeral Data Processing

GreenCheck requests specific Android device permissions to function:

### A. Camera Permission (`android.permission.CAMERA`)
- **Purpose:** Used exclusively to scan product barcodes using Google ML Kit and Android CameraX.
- **Ephemeral Processing:** Camera video frames are processed ephemerally in real-time on your device to detect barcodes. No images, video recordings, or camera feeds are saved to local storage, recorded, or transmitted over any network.

### B. Internet Permission (`android.permission.INTERNET`)
- **Purpose:** Required to connect to the Open Food Facts API to retrieve product ingredients, labels, and nutritional information when you scan a barcode or perform a manual search.

---

## 4. Local Data Storage & App Identifiers

### A. On-Device Storage
GreenCheck stores the following information locally on your device using Android's Room Database and DataStore preferences:
- **Scan & Search History:** Products and ingredients you scan or search for are saved locally on your device to enable offline history viewing.
- **App Configuration:** Local flags (such as intro completion) and daily usage limit counters.

This data is stored strictly on your local device storage and is never transmitted to cloud servers.

### B. Installation Identifier & Network Headers
To comply with Open Food Facts API fair usage guidelines and enforce per-device rate limits locally, GreenCheck generates a random, anonymous UUID (Universally Unique Identifier) stored in local DataStore.

When performing HTTP requests to Open Food Facts, GreenCheck sends a standard `User-Agent` HTTP header:
`GreenCheck - Android - 2.0 - [Anonymous Installation UUID] - https://github.com/rajbirsehmi/GreenCheck`

This installation ID is strictly anonymous, generated locally on your device, contains no personal information, and cannot be used to track you across other apps or websites.

---

## 5. Third-Party Services & Data Licensing

### Open Food Facts API
GreenCheck queries the public **Open Food Facts** database (https://world.openfoodfacts.org/) to fetch food product details.

- When you scan or search, the searched barcode number or query string is transmitted to Open Food Facts over encrypted HTTPS.
- Product data retrieved from Open Food Facts is open data published under the **Open Database License (ODbL)**.
- For more information regarding Open Food Facts' privacy practices, please review the [Open Food Facts Privacy Policy](https://world.openfoodfacts.org/terms-of-use).

---

## 6. Data Retention and Data Deletion Policy

Google Play Console requires a clear policy regarding how user data is retained and how users can request or execute data deletion.

- **Data Retention:** All user data (scan history, recent searches, preferences) resides exclusively on your local device.
- **Data Deletion Instructions:** You have full control over your data at all times. You can delete 100% of the data stored by GreenCheck at any time using either of the following methods:
  1. **In-App Clear History:** Clear your scan history from within the app interface.
  2. **Android System Settings:** Go to your Android device's **Settings > Apps > GreenCheck > Storage & Cache > Clear Storage / Clear Data**.
  3. **App Uninstallation:** Uninstalling the GreenCheck application immediately and permanently removes all locally stored app data, databases, and preferences from your device.

Because GreenCheck does not maintain user accounts or cloud databases, no remote account deletion request is required.

---

## 7. Children's Privacy (COPPA Compliance)

GreenCheck is designed for general audiences and does not knowingly collect, request, or solicit personal information from children under the age of 13 (or under 16 in applicable jurisdictions). Because GreenCheck collects zero personal information from any user, children can safely use the app without disclosing personal data.

---

## 8. Open Source Code Transparency

In compliance with open-source principles and to allow full public verification of our privacy practices, the complete source code for GreenCheck is publicly available for review:

- **GitHub Repository:** https://github.com/rajbirsehmi/GreenCheck

---

## 9. Changes to This Privacy Policy

We may update this Privacy Policy from time to time to maintain legal compliance or reflect updates to GreenCheck. Any revisions will be published in this document with an updated "Last Updated" date.

---

## 10. Contact Information

If you have questions, concerns, or requests regarding this Privacy Policy, please contact us at:

- **Developer:** Rajbir Sehmi
- **Support Email:** rajbirsinghsehmi93@gmail.com
- **GitHub Issues:** https://github.com/rajbirsehmi/GreenCheck/issues
