package pl.artsoft.rozmowki

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import java.util.Locale

data class Sentence(val cat: String, val level: String, val en: String, val lv: String, val pl: String)
data class Cat(val id: String, val names: List<String>) // PL, EN, LV

class MainActivity : Activity() {

    // ---------- kolory (styl zbliżony do aplikacji nawigacji) ----------
    private val cBg = Color.parseColor("#0E151D")
    private val cCard = Color.parseColor("#19222D")
    private val cCardSel = Color.parseColor("#1F2C3B")
    private val cStroke = Color.parseColor("#2B394B")
    private val cText = Color.parseColor("#E9EFF6")
    private val cSub = Color.parseColor("#93A1B3")
    private val cGreen = Color.parseColor("#22C55E")
    private val cLv = Color.parseColor("#7DD3A8")
    private val cBar = Color.parseColor("#121A24")
    private val cPl = Color.parseColor("#F5C26B")

    // ---------- dane ----------
    private val cats = listOf(
        Cat("greenhouse", listOf("Szklarnia (praca przy pomidorach)", "Greenhouse (tomato work)", "Siltumnīca (darbs ar tomātiem)")),
        Cat("lab", listOf("Laboratorium (analizy nasion)", "Laboratory (seed testing)", "Laboratorija (sēklu analīzes)")),
        Cat("work", listOf("Praca", "Work", "Darbs")),
        Cat("interview", listOf("Rozmowa rekrutacyjna", "Job interview", "Darba intervija")),
        Cat("travel", listOf("Podróż", "Travel", "Ceļojumi")),
        Cat("shop", listOf("Sklep i zakupy", "Shops and shopping", "Veikals un pirkumi")),
        Cat("school", listOf("Szkoła", "School", "Skola")),
        Cat("transport", listOf("Transport i komunikacja", "Transport and communication", "Transports un sakari")),
        Cat("health", listOf("Lekarz, apteka, szpital", "Doctor, pharmacy, hospital", "Ārsts, aptieka, slimnīca")),
        Cat("food", listOf("Restauracja i jedzenie", "Restaurant and food", "Restorāns un ēdiens")),
        Cat("hotel", listOf("Hotel i nocleg", "Hotel and accommodation", "Viesnīca un naktsmītne")),
        Cat("office", listOf("Urząd i bank", "Office and bank", "Iestāde un banka")),
        Cat("daily", listOf("Życie codzienne", "Daily life", "Ikdiena"))
    )
    private val levels = listOf("A2", "B1", "B2", "C1", "C2")
    private val all = ArrayList<Sentence>()

    // ---------- teksty interfejsu: PL, EN, LV ----------
    private val str = mapOf(
        "title" to listOf("Rozmówki EN–LV", "Phrasebook EN–LV", "Sarunvārdnīca EN–LV"),
        "points" to listOf("Punkty", "Points", "Punkti"),
        "streak" to listOf("Najlepsza seria", "Best streak", "Labākā sērija"),
        "lang" to listOf("JĘZYK MENU", "MENU LANGUAGE", "IZVĒLNES VALODA"),
        "trans" to listOf("JĘZYK TŁUMACZENIA", "TRANSLATION LANGUAGE", "TULKOJUMA VALODA"),
        "bothTr" to listOf("Oba", "Both", "Abas"),
        "transLbl" to listOf("Języki tłumaczenia", "Translation languages", "Tulkojuma valodas"),
        "pickPl" to listOf("WYBIERZ TŁUMACZENIE POLSKIE", "CHOOSE THE POLISH TRANSLATION", "IZVĒLIETIES TULKOJUMU POĻU VALODĀ"),
        "level" to listOf("POZIOM", "LEVEL", "LĪMENIS"),
        "allLevels" to listOf("Wszystkie", "All", "Visi"),
        "speed" to listOf("TEMPO MOWY", "SPEECH SPEED", "RUNAS ĀTRUMS"),
        "slow" to listOf("Wolno", "Slow", "Lēni"),
        "normal" to listOf("Normalnie", "Normal", "Normāli"),
        "cats" to listOf("KATEGORIE", "CATEGORIES", "KATEGORIJAS"),
        "game" to listOf("🎮  Wyzwanie: wszystkie kategorie", "🎮  Challenge: all categories", "🎮  Izaicinājums: visas kategorijas"),
        "cont" to listOf("▶  Słuchanie ciągłe", "▶  Continuous listening", "▶  Nepārtraukta klausīšanās"),
        "quiz" to listOf("🎮  Quiz z tej kategorii", "🎮  Quiz for this category", "🎮  Viktorīna šajā kategorijā"),
        "list" to listOf("ZDANIA (DOTKNIJ, ABY POSŁUCHAĆ)", "SENTENCES (TAP TO LISTEN)", "TEIKUMI (PIESKARIETIES, LAI KLAUSĪTOS)"),
        "back" to listOf("‹  Wstecz", "‹  Back", "‹  Atpakaļ"),
        "stop" to listOf("■  Stop", "■  Stop", "■  Stop"),
        "shuffle" to listOf("Losowo", "Shuffle", "Jaukti"),
        "order" to listOf("Po kolei", "In order", "Pēc kārtas"),
        "gap" to listOf("PRZERWA", "PAUSE", "PAUZE"),
        "short" to listOf("Krótka", "Short", "Īsa"),
        "long" to listOf("Długa", "Long", "Gara"),
        "listenOnly" to listOf("Tylko słuch", "Listening only", "Tikai klausīšanās"),
        "showText" to listOf("Pokaż tekst", "Show text", "Rādīt tekstu"),
        "repeat" to listOf("🔊  Powtórz", "🔊  Repeat", "🔊  Atkārtot"),
        "pickLv" to listOf("WYBIERZ TŁUMACZENIE ŁOTEWSKIE", "CHOOSE THE LATVIAN TRANSLATION", "IZVĒLIETIES TULKOJUMU LATVIEŠU VALODĀ"),
        "next" to listOf("Dalej  →", "Next  →", "Tālāk  →"),
        "question" to listOf("Pytanie", "Question", "Jautājums"),
        "good" to listOf("✔ Dobrze!", "✔ Correct!", "✔ Pareizi!"),
        "bad" to listOf("✘ Źle. Poprawna odpowiedź jest zaznaczona.", "✘ Wrong. The correct answer is marked.", "✘ Nepareizi. Pareizā atbilde ir atzīmēta."),
        "roundEnd" to listOf("Koniec rundy", "Round finished", "Raunds beidzies"),
        "score" to listOf("Wynik", "Score", "Rezultāts"),
        "earned" to listOf("Zdobyte punkty", "Points earned", "Nopelnītie punkti"),
        "again" to listOf("Nowa runda", "New round", "Jauns raunds"),
        "badges" to listOf("Nagrody", "Rewards", "Balvas"),
        "noLv" to listOf(
            "Brak łotewskiego głosu w telefonie. Zainstaluj dane głosowe (Ustawienia → Zarządzanie ogólne → Zamiana tekstu na mowę), a potem uruchom aplikację ponownie.",
            "No Latvian voice on this phone. Install voice data (Settings → General management → Text-to-speech), then restart the app.",
            "Tālrunī nav latviešu balss. Instalējiet balss datus (Iestatījumi → Vispārējā pārvaldība → Teksta pārveide runā) un restartējiet lietotni."
        ),
        "install" to listOf("Zainstaluj głos", "Install voice", "Instalēt balsi"),
        "close" to listOf("Zamknij", "Close", "Aizvērt"),
        "exit" to listOf("✕  Zamknij aplikację", "✕  Close app", "✕  Aizvērt lietotni"),
        "noTts" to listOf("Syntezator mowy nie jest gotowy.", "Text-to-speech is not ready.", "Teksta pārveide runā nav gatava."),
        // zakładki i ekran Info
        "tabHome" to listOf("Menu", "Menu", "Izvēlne"),
        "tabInfo" to listOf("Info", "Info", "Info"),
        "infoTitle" to listOf("Informacje o aplikacji", "About the app", "Par lietotni"),
        "appData" to listOf("DANE APLIKACJI", "APP DATA", "LIETOTNES DATI"),
        "version" to listOf("Wersja", "Version", "Versija"),
        "sentencesLbl" to listOf("Liczba zdań", "Sentences", "Teikumu skaits"),
        "catsLbl" to listOf("Kategorie", "Categories", "Kategorijas"),
        "levelsLbl" to listOf("Poziomy", "Levels", "Līmeņi"),
        "voiceLbl" to listOf("Głos", "Voice", "Balss"),
        "voiceVal" to listOf("Syntezator mowy Androida", "Android text-to-speech", "Android teksta pārveide runā"),
        "langsLbl" to listOf("Języki menu", "Menu languages", "Izvēlnes valodas"),
        "internetLbl" to listOf("Internet", "Internet", "Internets"),
        "internetVal" to listOf("Niepotrzebny po pobraniu głosów", "Not needed once voices are downloaded", "Nav vajadzīgs pēc balsu lejupielādes"),
        "authorHdr" to listOf("AUTOR", "AUTHOR", "AUTORS"),
        "authorLbl" to listOf("Autor", "Author", "Autors"),
        "siteLbl" to listOf("Strona WWW", "Website", "Mājaslapa"),
        "openSite" to listOf("🌐  Odwiedź artsoft.xo.je", "🌐  Visit artsoft.xo.je", "🌐  Apmeklēt artsoft.xo.je"),
        "donHdr" to listOf("WESPRZYJ AUTORA", "SUPPORT THE AUTHOR", "ATBALSTI AUTORU"),
        "donText" to listOf(
            "Aplikacja jest darmowa. Jeśli Ci się przydaje, możesz dobrowolnie wesprzeć jej rozwój przelewem.",
            "The app is free. If you find it useful, you can voluntarily support its development with a bank transfer.",
            "Lietotne ir bezmaksas. Ja tā jums noder, varat brīvprātīgi atbalstīt tās attīstību ar bankas pārskaitījumu."
        ),
        "donRecipient" to listOf("Odbiorca", "Recipient", "Saņēmējs"),
        "donCopy" to listOf("📋  Kopiuj numer IBAN", "📋  Copy IBAN", "📋  Kopēt IBAN"),
        "donCopied" to listOf("Numer IBAN skopiowany", "IBAN copied", "IBAN nokopēts"),
        "donThanks" to listOf("Dziękuję za wsparcie! 🙏", "Thank you for your support! 🙏", "Paldies par atbalstu! 🙏"),
        "rights" to listOf("© 2026 Artsoft. Wszelkie prawa zastrzeżone.", "© 2026 Artsoft. All rights reserved.", "© 2026 Artsoft. Visas tiesības aizsargātas.")
    )

    // nagrody: próg punktów, ikona, nazwa PL/EN/LV
    private val badges = listOf(
        Triple(100, "🥉", listOf("Brąz", "Bronze", "Bronza")),
        Triple(500, "🥈", listOf("Srebro", "Silver", "Sudrabs")),
        Triple(1000, "🥇", listOf("Złoto", "Gold", "Zelts")),
        Triple(2500, "🏆", listOf("Mistrz", "Master", "Meistars"))
    )

    // ---------- stan ----------
    private lateinit var prefs: android.content.SharedPreferences
    private var uiLang = 0
    private var trans = 2 // 0 = polski, 1 = łotewski, 2 = oba
    private var levelFilter = ""
    private var rate = 1.0f
    private var shuffleMode = false
    private var gapMs = 1200L
    private var listenOnly = false
    private var tts: TextToSpeech? = null
    private var ttsReady = false
    private val handler = Handler(Looper.getMainLooper())
    private var screenName = "home"
    private var currentCat: String? = null

    private val EN = Locale("en", "GB")
    private val LV = Locale("lv", "LV")
    private val PL = Locale("pl", "PL")

    private fun codes(): List<String> = when (trans) { 0 -> listOf("pl"); 1 -> listOf("lv"); else -> listOf("pl", "lv") }
    private fun tr(s: Sentence, code: String): String = if (code == "pl") s.pl else s.lv
    private fun locOf(code: String): Locale = if (code == "pl") PL else LV
    private fun quizCode(): String = if (trans == 0) "pl" else "lv"

    // słuchanie ciągłe
    private var contActive = false
    private var contList: List<Sentence> = emptyList()
    private var contIdx = 0
    private var contStage = 0
    private var contEn: TextView? = null
    private var contLv: TextView? = null
    private var contInfo: TextView? = null

    // quiz
    private var quizList: List<Sentence> = emptyList()
    private var qi = 0
    private var roundScore = 0
    private var roundGain = 0
    private var streak = 0

    private fun appVer(): String =
        try { packageManager.getPackageInfo(packageName, 0).versionName ?: "?" } catch (e: Exception) { "?" }

    private fun t(key: String): String = str[key]?.get(uiLang) ?: key
    private fun dp(v: Int): Int = (v * resources.displayMetrics.density).toInt()

    // ---------- cykl życia ----------
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = cBg
        window.navigationBarColor = cBar
        prefs = getSharedPreferences("rozmowki", Context.MODE_PRIVATE)
        uiLang = prefs.getInt("lang", 0)
        trans = prefs.getInt("trans", 2)
        rate = prefs.getFloat("rate", 1.0f)
        loadData()
        tts = TextToSpeech(this) { status ->
            ttsReady = status == TextToSpeech.SUCCESS
            if (ttsReady) {
                tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                    override fun onStart(utteranceId: String?) {}
                    override fun onDone(utteranceId: String?) {
                        runOnUiThread { onSpoken(utteranceId) }
                    }
                    @Deprecated("Deprecated in Java")
                    override fun onError(utteranceId: String?) {}
                })
                val r = tts?.isLanguageAvailable(LV) ?: TextToSpeech.LANG_NOT_SUPPORTED
                if (r < TextToSpeech.LANG_AVAILABLE) showNoLatvian()
            }
        }
        showHome()
    }

    override fun onDestroy() {
        stopAudio()
        tts?.shutdown()
        super.onDestroy()
    }

    override fun onPause() {
        stopAudio()
        super.onPause()
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        when (screenName) {
            "home" -> super.onBackPressed()
            "cat", "info" -> showHome()
            else -> if (currentCat != null) showCategory(currentCat!!) else showHome()
        }
    }

    private fun loadData() {
        for (c in cats) {
            try {
                assets.open(c.id + ".tsv").bufferedReader(Charsets.UTF_8).useLines { lines ->
                    for (line in lines) {
                        if (line.isBlank() || line.startsWith("#")) continue
                        val p = line.split("\t")
                        if (p.size >= 3) all.add(Sentence(c.id, p[0].trim(), p[1].trim(), p[2].trim(), if (p.size >= 4) p[3].trim() else p[1].trim()))
                    }
                }
            } catch (e: Exception) { /* brak pliku kategorii */ }
        }
    }

    private fun pool(cat: String?): List<Sentence> =
        all.filter { (cat == null || it.cat == cat) && (levelFilter.isEmpty() || it.level == levelFilter) }

    // ---------- audio ----------
    private fun say(text: String, loc: Locale, id: String) {
        val engine = tts
        if (engine == null || !ttsReady) {
            android.widget.Toast.makeText(this, t("noTts"), android.widget.Toast.LENGTH_SHORT).show()
            return
        }
        engine.setLanguage(loc)
        engine.setSpeechRate(rate)
        engine.speak(text, TextToSpeech.QUEUE_FLUSH, null, id)
    }

    private fun stopAudio() {
        contActive = false
        handler.removeCallbacksAndMessages(null)
        tts?.stop()
        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    private val singleQueue = ArrayList<Pair<String, Locale>>()

    private fun onSpoken(id: String?) {
        when (id) {
            "c_en" -> if (contActive) {
                contStage = 1
                handler.postDelayed({ contSpeak() }, gapMs)
            }
            "c_tr" -> if (contActive) {
                if (contStage < codes().size) {
                    contStage++
                    handler.postDelayed({ contSpeak() }, gapMs)
                    return
                }
                contIdx = if (shuffleMode) (0 until contList.size).random() else (contIdx + 1) % contList.size
                contStage = 0
                handler.postDelayed({ contSpeak() }, gapMs * 2)
            }
            "s_en", "s_tr" -> if (singleQueue.isNotEmpty()) {
                val n = singleQueue.removeAt(0)
                handler.postDelayed({ say(n.first, n.second, "s_tr") }, 500)
            }
        }
    }

    private fun quitApp() {
        stopAudio()
        finishAndRemoveTask()
    }

    private fun showNoLatvian() {
        AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert)
            .setMessage(t("noLv"))
            .setPositiveButton(t("install")) { _, _ ->
                try {
                    startActivity(Intent("android.speech.tts.engine.INSTALL_TTS_DATA"))
                } catch (e: Exception) {
                    try { startActivity(Intent("com.android.settings.TTS_SETTINGS")) } catch (e2: Exception) {}
                }
            }
            .setNegativeButton(t("close"), null)
            .show()
    }

    // ---------- styl: tła i przyciski ----------
    private fun shape(fill: Int, stroke: Int): GradientDrawable = GradientDrawable().apply {
        setColor(fill)
        cornerRadius = dp(14).toFloat()
        setStroke(dp(1), stroke)
    }

    // stany: 0 zwykły, 1 wybrany (zielony pasek), 2 dobrze, 3 źle, 4 główny (zielony)
    private fun bg(state: Int): Drawable = when (state) {
        1 -> LayerDrawable(arrayOf<Drawable>(shape(cGreen, cGreen), shape(cCardSel, cCardSel))).apply {
            setLayerInset(1, dp(5), 0, 0, 0)
        }
        2 -> shape(Color.parseColor("#12391F"), cGreen)
        3 -> shape(Color.parseColor("#3B1519"), Color.parseColor("#EF4444"))
        4 -> shape(cGreen, cGreen)
        else -> shape(cCard, cStroke)
    }

    private fun styleBtn(b: Button, state: Int) {
        b.background = bg(state)
        b.setTextColor(if (state == 4) Color.parseColor("#06210F") else cText)
    }

    private fun makeButton(text: String, state: Int = 0, small: Boolean = false, left: Boolean = false, onClick: () -> Unit): Button =
        Button(this).apply {
            this.text = text
            isAllCaps = false
            stateListAnimator = null
            minWidth = 0
            minimumWidth = 0
            minHeight = 0
            minimumHeight = 0
            setTextSize(TypedValue.COMPLEX_UNIT_SP, if (small) 14f else 16f)
            setTypeface(typeface, Typeface.BOLD)
            if (small) setPadding(dp(4), dp(13), dp(4), dp(13)) else setPadding(dp(14), dp(14), dp(14), dp(14))
            gravity = if (left) (Gravity.START or Gravity.CENTER_VERTICAL) else Gravity.CENTER
            styleBtn(this, state)
            setOnClickListener { onClick() }
        }

    private fun LinearLayout.label(text: String, sp: Float = 16f, bold: Boolean = false, color: Int = cText): TextView {
        val tv = TextView(this@MainActivity).apply {
            this.text = text
            setTextSize(TypedValue.COMPLEX_UNIT_SP, sp)
            setTextColor(color)
            if (bold) setTypeface(typeface, Typeface.BOLD)
            setPadding(dp(2), dp(6), dp(2), dp(6))
        }
        addView(tv, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
        return tv
    }

    private fun LinearLayout.section(text: String) {
        val tv = label(text, 12f, true, cSub)
        (tv.layoutParams as LinearLayout.LayoutParams).topMargin = dp(14)
        tv.letterSpacing = 0.08f
    }

    private fun LinearLayout.button(text: String, state: Int = 0, left: Boolean = false, onClick: () -> Unit): Button {
        val b = makeButton(text, state, false, left, onClick)
        addView(b, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply { topMargin = dp(8) })
        return b
    }

    // wiersz przycisków: tekst, czy wybrany, akcja; opcjonalne wagi szerokości
    private fun LinearLayout.row(items: List<Triple<String, Boolean, () -> Unit>>, weights: List<Float>? = null) {
        val r = LinearLayout(this@MainActivity).apply { orientation = LinearLayout.HORIZONTAL }
        items.forEachIndexed { i, (text, sel, act) ->
            val w = weights?.get(i) ?: 1f
            val lp = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, w)
            if (i < items.size - 1) lp.marginEnd = dp(6)
            r.addView(makeButton(text, if (sel) 1 else 0, true) { act() }, lp)
        }
        addView(r, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply { topMargin = dp(8) })
    }

    private fun LinearLayout.card(build: LinearLayout.() -> Unit) {
        val c = LinearLayout(this@MainActivity).apply {
            orientation = LinearLayout.VERTICAL
            background = shape(cCard, cStroke)
            setPadding(dp(16), dp(10), dp(16), dp(10))
        }
        c.build()
        addView(c, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply { topMargin = dp(8) })
    }

    private fun LinearLayout.infoRow(key: String, value: String) {
        val r = LinearLayout(this@MainActivity).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(0, dp(8), 0, dp(8))
        }
        r.addView(TextView(this@MainActivity).apply {
            text = key
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 12f)
            setTextColor(cSub)
        })
        r.addView(TextView(this@MainActivity).apply {
            text = value
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
            setTextColor(cText)
        })
        addView(r, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
    }

    // ekran z ewentualnym dolnym paskiem zakładek (Menu / Info)
    private fun screen(name: String, tabs: Boolean = false, build: LinearLayout.() -> Unit) {
        stopAudio()
        screenName = name
        val col = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(16), dp(20), dp(16), dp(24))
        }
        col.build()
        val scroll = ScrollView(this).apply {
            setBackgroundColor(cBg)
            addView(col, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
        }
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(cBg)
            addView(scroll, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f))
            if (tabs) addView(tabBar(name), LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
        }
        setContentView(root)
    }

    private fun tabBar(active: String): LinearLayout {
        val bar = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            setBackgroundColor(cBar)
        }
        fun tab(text: String, on: Boolean, act: () -> Unit) {
            val tv = TextView(this).apply {
                this.text = text
                gravity = Gravity.CENTER
                setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f)
                setTypeface(typeface, Typeface.BOLD)
                setTextColor(if (on) cGreen else cSub)
                setPadding(0, dp(14), 0, dp(14))
                setOnClickListener { act() }
            }
            bar.addView(tv, LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f))
        }
        tab("🏠  " + t("tabHome"), active == "home") { showHome() }
        tab("ℹ️  " + t("tabInfo"), active == "info") { showInfo() }
        return bar
    }

    // ---------- ekran główny ----------
    private fun showHome() {
        currentCat = null
        screen("home", true) {
            label(t("title"), 28f, true)
            label("${t("version")} ${appVer()}", 12f, false, cSub)
            val pts = prefs.getInt("points", 0)
            val earned = badges.filter { pts >= it.first }.joinToString(" ") { it.second }
            card {
                label("⭐  ${t("points")}: $pts", 16f, true)
                label("🔥  ${t("streak")}: ${prefs.getInt("best", 0)}", 16f, false, cSub)
                if (earned.isNotEmpty()) label(earned, 22f)
            }

            section(t("lang"))
            row(listOf("Polski", "English", "Latviešu").mapIndexed { i, n ->
                Triple(n, uiLang == i) {
                    uiLang = i
                    prefs.edit().putInt("lang", i).apply()
                    showHome()
                }
            })

            section(t("trans"))
            row(listOf(
                Triple("Polski", trans == 0) { trans = 0; prefs.edit().putInt("trans", 0).apply(); showHome() },
                Triple("Latviešu", trans == 1) { trans = 1; prefs.edit().putInt("trans", 1).apply(); showHome() },
                Triple(t("bothTr"), trans == 2) { trans = 2; prefs.edit().putInt("trans", 2).apply(); showHome() }
            ))

            section(t("level"))
            val lv = listOf("") + levels
            row(lv.map { l ->
                Triple(if (l.isEmpty()) t("allLevels") else l, levelFilter == l) {
                    levelFilter = l
                    showHome()
                }
            }, listOf(1.9f, 1f, 1f, 1f, 1f, 1f))

            section(t("speed"))
            row(listOf(
                Triple(t("slow"), rate < 0.9f) { rate = 0.7f; prefs.edit().putFloat("rate", rate).apply(); showHome() },
                Triple(t("normal"), rate >= 0.9f) { rate = 1.0f; prefs.edit().putFloat("rate", rate).apply(); showHome() }
            ))

            section(t("cats"))
            for (c in cats) {
                val n = pool(c.id).size
                button("${c.names[uiLang]}   ·   $n", 0, true) { showCategory(c.id) }
            }
            button(t("game"), 4) { startQuiz(null) }
            button(t("exit")) { quitApp() }
        }
    }

    // ---------- ekran Info ----------
    private fun showInfo() {
        currentCat = null
        val ver = appVer()
        screen("info", true) {
            label(t("infoTitle"), 26f, true)

            section(t("appData"))
            card {
                infoRow(t("version"), ver)
                infoRow(t("sentencesLbl"), all.size.toString())
                infoRow(t("catsLbl"), cats.size.toString())
                infoRow(t("levelsLbl"), levels.joinToString(", "))
                infoRow(t("langsLbl"), "Polski, English, Latviešu")
                infoRow(t("transLbl"), "Polski, Latviešu")
                infoRow(t("voiceLbl"), t("voiceVal"))
                infoRow(t("internetLbl"), t("internetVal"))
            }

            section(t("authorHdr"))
            card {
                infoRow(t("authorLbl"), "Artsoft")
                infoRow(t("siteLbl"), "artsoft.xo.je")
            }
            button(t("openSite"), 4) {
                try {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://artsoft.xo.je")))
                } catch (e: Exception) { }
            }
            section(t("donHdr"))
            card {
                label(t("donText"), 14f, false, cSub)
                infoRow(t("donRecipient"), "Artur Knez")
                infoRow("IBAN", "NL65 REVO 4503 7824 60")
            }
            button(t("donCopy"), 4) {
                val cm = getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
                cm.setPrimaryClip(android.content.ClipData.newPlainText("IBAN", "NL65 REVO 4503 7824 60".replace(" ", "")))
                android.widget.Toast.makeText(this@MainActivity, t("donCopied"), android.widget.Toast.LENGTH_SHORT).show()
            }
            label(t("donThanks"), 14f, false, cSub).gravity = Gravity.CENTER
            button(t("exit")) { quitApp() }
            label(t("rights"), 12f, false, cSub).gravity = Gravity.CENTER
        }
    }

    // ---------- ekran kategorii ----------
    private fun showCategory(id: String) {
        currentCat = id
        val cat = cats.first { it.id == id }
        val items = pool(id)
        screen("cat") {
            button(t("back")) { showHome() }
            label(cat.names[uiLang], 24f, true).also {
                (it.layoutParams as LinearLayout.LayoutParams).topMargin = dp(10)
            }
            button(t("cont"), 4) { startContinuous(id) }
            button(t("quiz")) { startQuiz(id) }
            section(t("list"))
            for (s in items) {
                val sb = SpannableStringBuilder()
                val tag = s.level + "\n"
                sb.append(tag)
                sb.setSpan(ForegroundColorSpan(cSub), 0, tag.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                sb.setSpan(RelativeSizeSpan(0.8f), 0, tag.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                val a = sb.length
                sb.append(s.en)
                sb.setSpan(ForegroundColorSpan(cText), a, sb.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                sb.setSpan(StyleSpan(Typeface.BOLD), a, sb.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                for (code in codes()) {
                    val b = sb.length
                    sb.append("\n" + tr(s, code))
                    sb.setSpan(ForegroundColorSpan(if (code == "pl") cPl else cLv), b, sb.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
                val tv = TextView(this@MainActivity).apply {
                    text = sb
                    setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
                    background = shape(cCard, cStroke)
                    setPadding(dp(14), dp(10), dp(14), dp(12))
                    setOnClickListener {
                        singleQueue.clear()
                        for (code in codes()) singleQueue.add(Pair(tr(s, code), locOf(code)))
                        say(s.en, EN, "s_en")
                    }
                }
                addView(tv, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply { topMargin = dp(8) })
            }
        }
    }

    // ---------- słuchanie ciągłe ----------
    private fun startContinuous(id: String) {
        val items = pool(id)
        if (items.isEmpty()) return
        contList = if (shuffleMode) items.shuffled() else items
        contIdx = 0
        contStage = 0
        screen("cont") {
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            button(t("back")) { showCategory(id) }
            label(cats.first { it.id == id }.names[uiLang], 20f, true).also {
                (it.layoutParams as LinearLayout.LayoutParams).topMargin = dp(10)
            }
            card {
                contInfo = label("", 13f, false, cSub)
                contEn = label("", 26f, true)
                contLv = label("", 24f, false, cLv)
            }
            row(listOf(
                Triple("⏮", false) { contJump(-1) },
                Triple("⏭", false) { contJump(1) }
            ))
            row(listOf(
                Triple(t("order"), !shuffleMode) { shuffleMode = false; startContinuous(id) },
                Triple(t("shuffle"), shuffleMode) { shuffleMode = true; startContinuous(id) }
            ))
            section(t("gap"))
            row(listOf(
                Triple(t("short"), gapMs < 2000L) { gapMs = 1200L; startContinuous(id) },
                Triple(t("long"), gapMs >= 2000L) { gapMs = 3000L; startContinuous(id) }
            ))
            button(t("stop")) { stopAudio() }
        }
        contActive = true
        contSpeak()
    }

    private fun contJump(d: Int) {
        if (contList.isEmpty()) return
        handler.removeCallbacksAndMessages(null)
        tts?.stop()
        contIdx = (contIdx + d + contList.size) % contList.size
        contStage = 0
        contActive = true
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        contSpeak()
    }

    private fun contSpeak() {
        if (!contActive || contList.isEmpty()) return
        val s = contList[contIdx]
        contInfo?.text = "${contIdx + 1} / ${contList.size}   ·   ${s.level}"
        contEn?.text = s.en
        val cs = codes()
        if (contStage == 0) {
            contLv?.text = ""
            say(s.en, EN, "c_en")
        } else {
            val sb = SpannableStringBuilder()
            for (i in 0 until minOf(contStage, cs.size)) {
                if (sb.isNotEmpty()) sb.append("\n")
                val a = sb.length
                sb.append(tr(s, cs[i]))
                sb.setSpan(ForegroundColorSpan(if (cs[i] == "pl") cPl else cLv), a, sb.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
            contLv?.text = sb
            val cur = cs[minOf(contStage, cs.size) - 1]
            say(tr(s, cur), locOf(cur), "c_tr")
        }
    }

    // ---------- gra / quiz ----------
    private fun startQuiz(cat: String?) {
        currentCat = cat
        val p = pool(cat)
        if (p.size < 4) return
        quizList = p.shuffled().take(10)
        qi = 0
        roundScore = 0
        roundGain = 0
        streak = 0
        showQuestion(cat)
    }

    private fun showQuestion(cat: String?) {
        if (qi >= quizList.size) { showRoundEnd(cat); return }
        val q = quizList[qi]
        val p = pool(cat)
        val qc = quizCode()
        val ans = tr(q, qc)
        val wrong = p.filter { tr(it, qc) != ans }.shuffled().take(3).map { tr(it, qc) }
        val options = (wrong + ans).shuffled()
        screen("quiz") {
            button(t("back")) { if (cat == null) showHome() else showCategory(cat) }
            label("${t("question")} ${qi + 1} / ${quizList.size}     ⭐ ${prefs.getInt("points", 0)}     🔥 $streak", 14f, true, cSub).also {
                (it.layoutParams as LinearLayout.LayoutParams).topMargin = dp(10)
            }
            val enView = label(if (listenOnly) "🎧" else q.en, 24f, true)
            row(listOf(
                Triple(t("repeat"), false) { say(q.en, EN, "q") },
                Triple(if (listenOnly) t("showText") else t("listenOnly"), listenOnly) {
                    listenOnly = !listenOnly
                    showQuestion(cat)
                }
            ))
            section(t(if (qc == "pl") "pickPl" else "pickLv"))
            val buttons = ArrayList<Button>()
            val feedback = label("", 16f, true)
            val nextBtn = makeButton(t("next"), 4) { qi++; showQuestion(cat) }
            nextBtn.visibility = View.GONE
            for (o in options) {
                val b = makeButton(o, 0, false, true) {}
                b.setOnClickListener {
                    for (x in buttons) {
                        x.isEnabled = false
                        if (x.text.toString() == ans) styleBtn(x, 2)
                    }
                    if (o == ans) {
                        streak++
                        val gain = 10 + minOf(streak, 5) * 2
                        roundScore++
                        roundGain += gain
                        val total = prefs.getInt("points", 0) + gain
                        prefs.edit().putInt("points", total).apply()
                        if (streak > prefs.getInt("best", 0)) prefs.edit().putInt("best", streak).apply()
                        feedback.text = "${t("good")}  +$gain"
                        feedback.setTextColor(cGreen)
                    } else {
                        streak = 0
                        styleBtn(b, 3)
                        feedback.text = t("bad")
                        feedback.setTextColor(Color.parseColor("#F87171"))
                    }
                    enView.text = q.en
                    nextBtn.visibility = View.VISIBLE
                    say(ans, locOf(qc), "q")
                }
                buttons.add(b)
                addView(b, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply { topMargin = dp(8) })
            }
            addView(nextBtn, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply { topMargin = dp(12) })
        }
        say(q.en, EN, "q")
    }

    private fun showRoundEnd(cat: String?) {
        val stars = when {
            roundScore >= 9 -> "⭐⭐⭐"
            roundScore >= 7 -> "⭐⭐"
            roundScore >= 5 -> "⭐"
            else -> "·"
        }
        val pts = prefs.getInt("points", 0)
        screen("end") {
            label(t("roundEnd"), 26f, true)
            card {
                label(stars, 40f).gravity = Gravity.CENTER
                label("${t("score")}: $roundScore / ${quizList.size}", 20f, true)
                label("${t("earned")}: +$roundGain", 18f, false, cLv)
                label("${t("points")}: $pts", 18f)
                val earned = badges.filter { pts >= it.first }
                if (earned.isNotEmpty()) {
                    label("${t("badges")}: " + earned.joinToString("  ") { "${it.second} ${it.third[uiLang]}" }, 16f, false, cSub)
                }
            }
            button(t("again"), 4) { startQuiz(cat) }
            button(t("back")) { if (cat == null) showHome() else showCategory(cat) }
        }
    }
}
