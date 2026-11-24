# Rozmówki EN–LV / Phrasebook EN–LV

**Autor / Author:** Artsoft · https://artsoft.xo.je
**Wersja / Version:** see the *Info* tab in the app / zobacz zakładkę *Info* w aplikacji

---

## 🇵🇱 POLSKI

### Co to jest
Aplikacja na Androida do słuchania rozmówek angielsko-łotewskich z tłumaczeniem polskim. Zawiera ponad 2700 pozycji (zdania i 1000 najczęstszych słów) w 14 kategoriach (szklarnia, laboratorium, praca, rozmowa rekrutacyjna, podróż, zakupy, szkoła, transport, lekarz, restauracja, hotel, urząd i bank, życie codzienne) na poziomach A2–C2. Zdania są czytane głosem syntezatora Androida: najpierw po angielsku, potem tłumaczenie (polskie, łotewskie lub oba, do wyboru w menu). Jest też słuchanie ciągłe, quiz z punktami i nagrodami oraz menu po polsku, angielsku i łotewsku.

### Czego potrzebujesz
- Telefon z Androidem **8.0 lub nowszym** (testowane na Samsung Galaxy S23 Ultra).
- Około 20 MB wolnego miejsca na aplikację oraz kilkadziesiąt MB na głosy.
- **Internet tylko na początku**, żeby pobrać głosy (angielski, łotewski, polski). Potem aplikacja działa offline.
- Zainstalowany **silnik syntezy mowy** (na Samsungu: Samsung text-to-speech i/lub *Usługi mowy od Google*) z głosami **angielskim (Wielka Brytania)**, **łotewskim** i **polskim**.

### 1. Pobranie
1. Wejdź na stronę projektu na GitHubie i otwórz plik ZIP z instalatorem (np. `rozmowki-apk.zip`) na liście plików.
2. Kliknij **Download** (Pobierz). Plik trafi do folderu *Pobrane* w telefonie. Możesz też pobrać go na komputerze i przesłać na telefon kablem USB lub przez chmurę.
3. Otwórz aplikację **Moje pliki** → *Pobrane*, dotknij pliku ZIP i wybierz **Wypakuj** (Extract). Dostaniesz plik z rozszerzeniem **.apk**.

### 2. Zezwolenie na instalację ze źródeł zewnętrznych
Android domyślnie blokuje instalację aplikacji spoza sklepu Google Play, więc trzeba to jednorazowo zezwolić:
1. Dotknij pliku **.apk**. Pojawi się komunikat, że instalacja jest zablokowana. Dotknij **Ustawienia**.
2. Włącz **Zezwalaj z tego źródła** dla aplikacji, z której instalujesz (np. *Moje pliki* albo *Chrome*).
3. Wróć i dotknij **Zainstaluj**.

Ręcznie: *Ustawienia → Aplikacje (lub Zabezpieczenia i prywatność) → Instalowanie nieznanych aplikacji* → wybierz aplikację → włącz zezwolenie. Nazwy menu różnią się zależnie od telefonu i wersji Androida. Po instalacji możesz tę zgodę wyłączyć.

Jeśli pojawi się ostrzeżenie **Google Play Protect** („Aplikacja nieznana” / „Zablokowano”), wybierz **Więcej szczegółów → Zainstaluj mimo to**. Aplikacja nie jest w Sklepie Play i nie ma podpisu wydawcy, dlatego Android pyta o zgodę. Instaluj tylko plik, który pochodzi z tego projektu.

### 3. Instalacja głosów (żeby aplikacja mówiła)
1. *Ustawienia → Zarządzanie ogólne → Zamiana tekstu na mowę* (na innych telefonach: *Ustawienia → System → Języki → Zamiana tekstu na mowę*).
2. Wybierz silnik (zalecane: **Usługi mowy od Google**, jeśli jest na liście).
3. Dotknij ikony ustawień przy silniku → **Zainstaluj dane głosowe** i pobierz: **angielski (Wielka Brytania)**, **łotewski**, **polski**.
4. Uruchom aplikację ponownie. Jeśli brakuje głosu łotewskiego, aplikacja wyświetli komunikat z przyciskiem *Zainstaluj głos*.

### 4. Użycie
- Wybierz **język menu**, **język tłumaczenia**, **poziom** i **tempo mowy** na ekranie głównym.
- Wejdź w kategorię: dotknij zdania, żeby je usłyszeć, albo włącz **Słuchanie ciągłe**.
- **Quiz** daje punkty (serie dają bonus) i nagrody (brąz, srebro, złoto, mistrz).
- Zakładka **Info** pokazuje numer wersji, dane aplikacji, autora i sekcję **Wesprzyj autora**.
- Przycisk **Zamknij aplikację** zamyka program.

### Aktualizacja i odinstalowanie
Każda wersja instalatora jest podpisana kluczem zbudowanym na nowo, więc Android nie pozwoli nadpisać starej wersji. **Przed instalacją nowej wersji odinstaluj starą** (przytrzymaj ikonę → *Odinstaluj*). Punkty i serie zapisane w aplikacji zostaną wtedy zresetowane.

### Rozwiązywanie problemów
| Problem | Co zrobić |
|---|---|
| „Aplikacja nie została zainstalowana” | Odinstaluj starą wersję i spróbuj ponownie. |
| Nie można otworzyć pliku APK | Upewnij się, że plik ZIP został wypakowany i że zezwoliłeś na instalację z tego źródła. |
| Brak dźwięku | Zwiększ głośność multimediów, sprawdź silnik syntezy mowy i pobrane głosy. |
| Polskie zdania czytane dziwnie | Zainstaluj polski głos w ustawieniach zamiany tekstu na mowę. |
| Komunikat o braku łotewskiego głosu | Pobierz głos łotewski (punkt 3) i uruchom aplikację ponownie. |

### Dla twórców
Kod: Kotlin, bez zewnętrznych bibliotek. Zdania są w `app/src/main/assets/*.tsv` (kolumny: poziom, angielski, łotewski, polski; rozdzielone tabulatorem). APK budowany jest przez GitHub Actions (`.github/workflows/build.yml`), artefakt **Rozmowki-APK**.

---

## 🇬🇧 ENGLISH

### What is it
An Android app for listening to English–Latvian phrasebook sentences, with Polish translations. It contains over 2,700 entries (sentences and 1,000 most common words) in 14 categories (greenhouse, laboratory, work, job interview, travel, shopping, school, transport, health, restaurant, hotel, office and bank, daily life) at levels A2–C2. Sentences are read aloud by the Android speech engine: English first, then the translation (Polish, Latvian or both, selectable in the menu). It also has continuous listening, a quiz with points and rewards, and a menu in Polish, English and Latvian.

### Requirements
- An Android phone running **Android 8.0 or newer** (tested on a Samsung Galaxy S23 Ultra).
- About 20 MB of free space for the app plus a few dozen MB for voices.
- **Internet only at the start**, to download voices (English, Latvian, Polish). After that the app works offline.
- A **text-to-speech engine** installed (on Samsung: Samsung text-to-speech and/or *Speech Services by Google*) with **English (UK)**, **Latvian** and **Polish** voices.

### 1. Download
1. Open the project page on GitHub and find the ZIP file with the installer (e.g. `rozmowki-apk.zip`) in the file list.
2. Tap **Download**. The file goes to your phone's *Downloads* folder. You can also download it on a computer and copy it to the phone by USB cable or through cloud storage.
3. Open **My Files** → *Downloads*, tap the ZIP file and choose **Extract**. You will get a file ending in **.apk**.

### 2. Allow installation from external sources
By default Android blocks apps that don't come from Google Play, so you need to allow it once:
1. Tap the **.apk** file. A message says the installation is blocked. Tap **Settings**.
2. Turn on **Allow from this source** for the app you are installing from (e.g. *My Files* or *Chrome*).
3. Go back and tap **Install**.

Manually: *Settings → Apps (or Security and privacy) → Install unknown apps* → choose the app → turn on the permission. Menu names vary by phone and Android version. You can turn the permission off again after installing.

If **Google Play Protect** shows a warning ("Unknown app" / "Blocked"), choose **More details → Install anyway**. The app is not in the Play Store and has no publisher signature, so Android asks for confirmation. Only install the file that comes from this project.

### 3. Install voices (so the app can speak)
1. *Settings → General management → Text-to-speech* (on other phones: *Settings → System → Languages → Text-to-speech*).
2. Choose an engine (recommended: **Speech Services by Google**, if listed).
3. Tap the settings icon next to the engine → **Install voice data** and download **English (UK)**, **Latvian** and **Polish**.
4. Restart the app. If the Latvian voice is missing, the app shows a message with an *Install voice* button.

### 4. Using the app
- Choose the **menu language**, **translation language**, **level** and **speech speed** on the home screen.
- Open a category: tap a sentence to hear it, or start **Continuous listening**.
- The **quiz** gives points (streaks give a bonus) and rewards (bronze, silver, gold, master).
- The **Info** tab shows the version number, app data, the author and a **Support the author** section.
- The **Close app** button quits the program.

### Updating and uninstalling
Each installer build is signed with a freshly generated key, so Android will not let a new version overwrite the old one. **Uninstall the old version before installing a new one** (long-press the icon → *Uninstall*). Points and streaks saved in the app will be reset.

### Troubleshooting
| Problem | What to do |
|---|---|
| "App not installed" | Uninstall the old version and try again. |
| The APK file won't open | Make sure the ZIP was extracted and that you allowed installing from this source. |
| No sound | Raise the media volume and check the speech engine and downloaded voices. |
| Polish sentences sound odd | Install the Polish voice in the text-to-speech settings. |
| Message about a missing Latvian voice | Download the Latvian voice (step 3) and restart the app. |

### For developers
Kotlin, no external libraries. Sentences live in `app/src/main/assets/*.tsv` (columns: level, English, Latvian, Polish; tab-separated). The APK is built by GitHub Actions (`.github/workflows/build.yml`), artifact **Rozmowki-APK**.

## 🇱🇻 LATVIEŠU

### Kas tā ir
Android lietotne angļu–latviešu sarunvārdnīcas teikumu klausīšanai ar tulkojumu poļu valodā. Tajā ir vairāk nekā 2700 ierakstu (teikumi un 1000 biežāko vārdu) 14 kategorijās (siltumnīca, laboratorija, darbs, darba intervija, ceļojumi, iepirkšanās, skola, transports, ārsts, restorāns, viesnīca, iestāde un banka, ikdiena) A2–C2 līmeņos. Teikumus nolasa Android runas sintezators: vispirms angliski, pēc tam tulkojumu (poliski, latviski vai abus, izvēlnē var izvēlēties). Ir arī nepārtraukta klausīšanās, viktorīna ar punktiem un balvām, kā arī izvēlne poļu, angļu un latviešu valodā.

### Kas nepieciešams
- Android tālrunis ar **Android 8.0 vai jaunāku versiju** (pārbaudīts uz Samsung Galaxy S23 Ultra).
- Apmēram 20 MB brīvas vietas lietotnei un vēl daži desmiti MB balsīm.
- **Internets tikai sākumā**, lai lejupielādētu balsis (angļu, latviešu, poļu). Pēc tam lietotne darbojas bez interneta.
- Uzstādīts **teksta pārveides runā dzinējs** (Samsung tālruņos: Samsung text-to-speech un/vai *Google runas pakalpojumi*) ar **angļu (Lielbritānija)**, **latviešu** un **poļu** balsīm.

### 1. Lejupielāde
1. Atveriet projekta lapu GitHub un failu sarakstā atrodiet ZIP failu ar instalētāju (piemēram, `rozmowki-apk.zip`).
2. Pieskarieties **Download** (Lejupielādēt). Fails nonāks tālruņa mapē *Lejupielādes*. To var lejupielādēt arī datorā un pārsūtīt uz tālruni ar USB kabeli vai caur mākoņkrātuvi.
3. Atveriet lietotni **Mani faili** → *Lejupielādes*, pieskarieties ZIP failam un izvēlieties **Izvilkt** (Extract). Jūs iegūsiet failu ar paplašinājumu **.apk**.

### 2. Atļauja instalēt no ārējiem avotiem
Android pēc noklusējuma bloķē lietotnes, kas nav no Google Play veikala, tāpēc atļauja jādod vienu reizi:
1. Pieskarieties **.apk** failam. Parādīsies paziņojums, ka instalēšana ir bloķēta. Pieskarieties **Iestatījumi**.
2. Ieslēdziet **Atļaut no šī avota** lietotnei, no kuras instalējat (piemēram, *Mani faili* vai *Chrome*).
3. Atgriezieties un pieskarieties **Instalēt**.

Manuāli: *Iestatījumi → Lietotnes (vai Drošība un privātums) → Nezināmu lietotņu instalēšana* → izvēlieties lietotni → ieslēdziet atļauju. Izvēļņu nosaukumi atšķiras atkarībā no tālruņa un Android versijas. Pēc instalēšanas atļauju var atkal izslēgt.

Ja parādās **Google Play Protect** brīdinājums („Nezināma lietotne” / „Bloķēts”), izvēlieties **Papildinformācija → Tomēr instalēt**. Lietotnes nav Play veikalā un tai nav izdevēja paraksta, tāpēc Android lūdz apstiprinājumu. Instalējiet tikai to failu, kas nāk no šī projekta.

### 3. Balsu instalēšana (lai lietotne varētu runāt)
1. *Iestatījumi → Vispārējā pārvaldība → Teksta pārveide runā* (citos tālruņos: *Iestatījumi → Sistēma → Valodas → Teksta pārveide runā*).
2. Izvēlieties dzinēju (ieteicams: **Google runas pakalpojumi**, ja tas ir sarakstā).
3. Pieskarieties iestatījumu ikonai pie dzinēja → **Instalēt balss datus** un lejupielādējiet: **angļu (Lielbritānija)**, **latviešu**, **poļu**.
4. Restartējiet lietotni. Ja trūkst latviešu balss, lietotne parādīs paziņojumu ar pogu *Instalēt balsi*.

### 4. Lietošana
- Sākuma ekrānā izvēlieties **izvēlnes valodu**, **tulkojuma valodu**, **līmeni** un **runas ātrumu**.
- Atveriet kategoriju: pieskarieties teikumam, lai to dzirdētu, vai ieslēdziet **Nepārtraukto klausīšanos**.
- **Viktorīna** dod punktus (sērijas dod bonusu) un balvas (bronza, sudrabs, zelts, meistars).
- Cilne **Info** rāda versijas numuru, lietotnes datus, autoru un sadaļu **Atbalsti autoru**.
- Poga **Aizvērt lietotni** aizver programmu.

### Atjaunināšana un noņemšana
Katrs instalētāja būvējums tiek parakstīts ar no jauna izveidotu atslēgu, tāpēc Android neļaus jauno versiju uzstādīt pāri vecajai. **Pirms jaunās versijas instalēšanas noņemiet veco** (turiet nospiestu ikonu → *Atinstalēt*). Lietotnē saglabātie punkti un sērijas tiks atiestatīti.

### Problēmu risināšana
| Problēma | Ko darīt |
|---|---|
| „Lietotne nav instalēta” | Noņemiet veco versiju un mēģiniet vēlreiz. |
| APK failu nevar atvērt | Pārliecinieties, ka ZIP ir izvilkts un ka esat atļāvis instalēšanu no šī avota. |
| Nav skaņas | Palieliniet multivides skaļumu, pārbaudiet runas dzinēju un lejupielādētās balsis. |
| Poļu teikumi skan dīvaini | Instalējiet poļu balsi teksta pārveides runā iestatījumos. |
| Paziņojums par trūkstošu latviešu balsi | Lejupielādējiet latviešu balsi (3. punkts) un restartējiet lietotni. |

### Izstrādātājiem
Kotlin, bez ārējām bibliotēkām. Teikumi atrodas `app/src/main/assets/*.tsv` (kolonnas: līmenis, angļu, latviešu, poļu; atdalītas ar tabulāciju). APK tiek būvēts ar GitHub Actions (`.github/workflows/build.yml`), artefakts **Rozmowki-APK**.

---

© 2026 Artsoft. All rights reserved. / Wszelkie prawa zastrzeżone. / Visas tiesības aizsargātas.
