# Rozmówki EN–LV (Android)

Aplikacja do słuchania rozmówek angielsko-łotewskich: 1194 zdań, 12 kategorii, poziomy A2–C2,
słuchanie ciągłe (zdanie po angielsku, potem tłumaczenie: polskie, łotewskie lub oba – do wyboru w menu), quiz z punktami i nagrodami,
menu po polsku / angielsku / łotewsku. Głos: wbudowany syntezator mowy Androida (offline po pobraniu głosów).

## Najprostszy sposób na APK (bez instalowania czegokolwiek): GitHub
1. Załóż darmowe konto na github.com i utwórz nowe repozytorium (prywatne może być).
2. Wgraj całą zawartość tego folderu (przycisk "Add file → Upload files"; folder `.github` też!).
3. Wejdź w zakładkę **Actions** → "Build APK" → poczekaj ok. 5 min na zielony znaczek.
4. Otwórz ukończone uruchomienie → na dole **Artifacts → Rozmowki-APK** → pobierz ZIP z plikiem `.apk`.
5. Przenieś APK na telefon, otwórz go i zezwól na instalację z tego źródła.

## Alternatywa: Android Studio na laptopie
File → Open → ten folder → Build → Build APK(s). Plik: app/build/outputs/apk/.

## Przed pierwszym użyciem na Samsungu
Ustawienia → Zarządzanie ogólne → Zamiana tekstu na mowę → silnik Google lub Samsung →
pobierz głosy: angielski (UK) i łotewski. Aplikacja sama przypomni, jeśli brakuje łotewskiego.

## Dane
Zdania: app/src/main/assets/*.tsv (poziom, angielski, łotewski, polski; rozdzielone tabulatorem). Można je dopisywać.
