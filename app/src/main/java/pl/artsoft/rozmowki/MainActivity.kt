package pl.artsoft.rozmowki

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import java.util.Locale

data class Sentence(val cat: String, val level: String, val en: String, val lv: String)
data class Cat(val id: String, val names: List<String>) // PL, EN, LV

class MainActivity : Activity() {

    // ---------- dane ----------
    private val cats = listOf(
        Cat("work", listOf("Praca", "Work", "Darbs")),
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
        "lang" to listOf("Język menu", "Menu language", "Izvēlnes valoda"),
        "level" to listOf("Poziom", "Level", "Līmenis"),
        "allLevels" to listOf("Wszystkie", "All", "Visi"),
        "speed" to listOf("Tempo mowy", "Speech speed", "Runas ātrums"),
        "slow" to listOf("Wolno", "Slow", "Lēni"),
        "normal" to listOf("Normalnie", "Normal", "Normāli"),
        "cats" to listOf("Kategorie", "Categories", "Kategorijas"),
        "game" to listOf("🎮 Wyzwanie: wszystkie kategorie", "🎮 Challenge: all categories", "🎮 Izaicinājums: visas kategorijas"),
        "cont" to listOf("▶ Słuchanie ciągłe", "▶ Continuous listening", "▶ Nepārtraukta klausīšanās"),
        "quiz" to listOf("🎮 Quiz z tej kategorii", "🎮 Quiz for this category", "🎮 Viktorīna šajā kategorijā"),
        "list" to listOf("Zdania (dotknij, aby posłuchać)", "Sentences (tap to listen)", "Teikumi (pieskarieties, lai klausītos)"),
        "back" to listOf("← Wstecz", "← Back", "← Atpakaļ"),
        "stop" to listOf("■ Stop", "■ Stop", "■ Stop"),
        "shuffle" to listOf("Losowo", "Shuffle", "Jaukti"),
        "order" to listOf("Po kolei", "In order", "Pēc kārtas"),
        "gap" to listOf("Przerwa", "Pause", "Pauze"),
        "short" to listOf("Krótka", "Short", "Īsa"),
        "long" to listOf("Długa", "Long", "Gara"),
        "listenOnly" to listOf("Tylko słuch (bez tekstu)", "Listening only (no text)", "Tikai klausīšanās (bez teksta)"),
        "showText" to listOf("Pokaż tekst", "Show text", "Rādīt tekstu"),
        "repeat" to listOf("🔊 Powtórz", "🔊 Repeat", "🔊 Atkārtot"),
        "pickLv" to listOf("Wybierz tłumaczenie łotewskie:", "Choose the Latvian translation:", "Izvēlieties tulkojumu latviski:"),
        "next" to listOf("Dalej →", "Next →", "Tālāk →"),
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
        "noTts" to listOf("Syntezator mowy nie jest gotowy.", "Text-to-speech is not ready.", "Teksta pārveide runā nav gatava.")
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

    private fun t(key: String): String = str[key]?.get(uiLang) ?: key
    private fun dp(v: Int): Int = (v * resources.displayMetrics.density).toInt()

    // ---------- cykl życia ----------
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prefs = getSharedPreferences("rozmowki", Context.MODE_PRIVATE)
        uiLang = prefs.getInt("lang", 0)
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
            "cat" -> showHome()
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
                        if (p.size >= 3) all.add(Sentence(c.id, p[0].trim(), p[1].trim(), p[2].trim()))
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

    private var singleLv: String? = null

    private fun onSpoken(id: String?) {
        when (id) {
            "c_en" -> if (contActive) {
                contStage = 1
                handler.postDelayed({ contSpeak() }, gapMs)
            }
            "c_lv" -> if (contActive) {
                contIdx = if (shuffleMode) (0 until contList.size).random() else (contIdx + 1) % contList.size
                contStage = 0
                handler.postDelayed({ contSpeak() }, gapMs * 2)
            }
            "s_en" -> singleLv?.let { lv -> handler.postDelayed({ say(lv, LV, "s_lv") }, 500) }
        }
    }

    private fun showNoLatvian() {
        AlertDialog.Builder(this)
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

    // ---------- pomocnicze widoki ----------
    private fun screen(name: String, build: LinearLayout.() -> Unit) {
        stopAudio()
        screenName = name
        val col = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(16), dp(24), dp(16), dp(24))
        }
        col.build()
        setContentView(ScrollView(this).apply {
            setBackgroundColor(Color.parseColor("#F4F6FA"))
            addView(col, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
        })
    }

    private fun LinearLayout.label(text: String, sp: Float = 16f, bold: Boolean = false, color: Int = Color.parseColor("#1B2333")): TextView {
        val tv = TextView(this@MainActivity).apply {
            this.text = text
            setTextSize(TypedValue.COMPLEX_UNIT_SP, sp)
            setTextColor(color)
            if (bold) setTypeface(typeface, Typeface.BOLD)
            setPadding(0, dp(6), 0, dp(6))
        }
        addView(tv, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT))
        return tv
    }

    private fun makeButton(text: String, selected: Boolean = false, onClick: () -> Unit): Button =
        Button(this).apply {
            this.text = text
            isAllCaps = false
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
            setTextColor(if (selected) Color.WHITE else Color.parseColor("#1B2333"))
            setBackgroundColor(if (selected) Color.parseColor("#2F6FED") else Color.parseColor("#DCE3F2"))
            setOnClickListener { onClick() }
        }

    private fun LinearLayout.button(text: String, selected: Boolean = false, onClick: () -> Unit): Button {
        val b = makeButton(text, selected, onClick)
        addView(b, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply { topMargin = dp(8) })
        return b
    }

    private fun LinearLayout.row(items: List<Triple<String, Boolean, () -> Unit>>) {
        val r = LinearLayout(this@MainActivity).apply { orientation = LinearLayout.HORIZONTAL }
        for ((text, sel, act) in items) {
            r.addView(makeButton(text, sel, act), LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f).apply { marginEnd = dp(4) })
        }
        addView(r, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply { topMargin = dp(8) })
    }

    // ---------- ekran główny ----------
    private fun showHome() {
        currentCat = null
        screen("home") {
            label(t("title"), 28f, true, Color.parseColor("#2F6FED"))
            val pts = prefs.getInt("points", 0)
            val earned = badges.filter { pts >= it.first }.joinToString(" ") { it.second }
            label("⭐ ${t("points")}: $pts   🔥 ${t("streak")}: ${prefs.getInt("best", 0)}   $earned", 15f)

            label(t("lang"), 14f, true)
            row(listOf("Polski", "English", "Latviešu").mapIndexed { i, n ->
                Triple(n, uiLang == i) {
                    uiLang = i
                    prefs.edit().putInt("lang", i).apply()
                    showHome()
                }
            })

            label(t("level"), 14f, true)
            val lv = listOf("") + levels
            row(lv.map { l ->
                Triple(if (l.isEmpty()) t("allLevels") else l, levelFilter == l) {
                    levelFilter = l
                    showHome()
                }
            })

            label(t("speed"), 14f, true)
            row(listOf(
                Triple(t("slow"), rate < 0.9f) { rate = 0.7f; prefs.edit().putFloat("rate", rate).apply(); showHome() },
                Triple(t("normal"), rate >= 0.9f) { rate = 1.0f; prefs.edit().putFloat("rate", rate).apply(); showHome() }
            ))

            label(t("cats"), 18f, true)
            for (c in cats) {
                val n = pool(c.id).size
                button("${c.names[uiLang]}  ($n)") { showCategory(c.id) }
            }
            button(t("game")) { startQuiz(null) }
        }
    }

    // ---------- ekran kategorii ----------
    private fun showCategory(id: String) {
        currentCat = id
        val cat = cats.first { it.id == id }
        val items = pool(id)
        screen("cat") {
            button(t("back")) { showHome() }
            label(cat.names[uiLang], 24f, true, Color.parseColor("#2F6FED"))
            button(t("cont")) { startContinuous(id) }
            button(t("quiz")) { startQuiz(id) }
            label(t("list"), 14f, true)
            for (s in items) {
                val tv = TextView(this@MainActivity).apply {
                    text = "${s.level}  ·  ${s.en}\n${s.lv}"
                    setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
                    setTextColor(Color.parseColor("#1B2333"))
                    setBackgroundColor(Color.WHITE)
                    setPadding(dp(12), dp(10), dp(12), dp(10))
                    setOnClickListener {
                        singleLv = s.lv
                        say(s.en, EN, "s_en")
                    }
                }
                addView(tv, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT).apply { topMargin = dp(6) })
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
            label(cats.first { it.id == id }.names[uiLang], 20f, true, Color.parseColor("#2F6FED"))
            contInfo = label("", 14f)
            contEn = label("", 26f, true)
            contLv = label("", 24f, false, Color.parseColor("#B3261E"))
            row(listOf(
                Triple(t("order"), !shuffleMode) { shuffleMode = false; startContinuous(id) },
                Triple(t("shuffle"), shuffleMode) { shuffleMode = true; startContinuous(id) }
            ))
            label(t("gap"), 14f, true)
            row(listOf(
                Triple(t("short"), gapMs < 2000L) { gapMs = 1200L; startContinuous(id) },
                Triple(t("long"), gapMs >= 2000L) { gapMs = 3000L; startContinuous(id) }
            ))
            row(listOf(
                Triple("⏮", false) { contJump(-1) },
                Triple("⏭", false) { contJump(1) }
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
        if (contStage == 0) {
            contLv?.text = ""
            say(s.en, EN, "c_en")
        } else {
            contLv?.text = s.lv
            say(s.lv, LV, "c_lv")
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
        val wrong = p.filter { it.lv != q.lv }.shuffled().take(3).map { it.lv }
        val options = (wrong + q.lv).shuffled()
        screen("quiz") {
            button(t("back")) { if (cat == null) showHome() else showCategory(cat) }
            label("${t("question")} ${qi + 1} / ${quizList.size}    ⭐ ${prefs.getInt("points", 0)}    🔥 $streak", 14f, true)
            val enView = label(if (listenOnly) "🎧" else q.en, 24f, true)
            row(listOf(
                Triple(t("repeat"), false) { say(q.en, EN, "q") },
                Triple(if (listenOnly) t("showText") else t("listenOnly"), listenOnly) {
                    listenOnly = !listenOnly
                    showQuestion(cat)
                }
            ))
            label(t("pickLv"), 14f, true)
            val buttons = ArrayList<Button>()
            val feedback = label("", 16f, true)
            val nextBtn = makeButton(t("next")) { qi++; showQuestion(cat) }
            nextBtn.visibility = android.view.View.GONE
            for (o in options) {
                val b = makeButton(o) {}
                b.setOnClickListener {
                    for (x in buttons) {
                        x.isEnabled = false
                        if (x.text.toString() == q.lv) x.setBackgroundColor(Color.parseColor("#9BE3A5"))
                    }
                    if (o == q.lv) {
                        streak++
                        val gain = 10 + minOf(streak, 5) * 2
                        roundScore++
                        roundGain += gain
                        val total = prefs.getInt("points", 0) + gain
                        prefs.edit().putInt("points", total).apply()
                        if (streak > prefs.getInt("best", 0)) prefs.edit().putInt("best", streak).apply()
                        feedback.text = "${t("good")}  +$gain"
                        feedback.setTextColor(Color.parseColor("#1B7F2A"))
                    } else {
                        streak = 0
                        b.setBackgroundColor(Color.parseColor("#F4A3A0"))
                        feedback.text = t("bad")
                        feedback.setTextColor(Color.parseColor("#B3261E"))
                    }
                    enView.text = q.en
                    nextBtn.visibility = android.view.View.VISIBLE
                    say(q.lv, LV, "q")
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
            label(t("roundEnd"), 26f, true, Color.parseColor("#2F6FED"))
            label(stars, 40f)
            label("${t("score")}: $roundScore / ${quizList.size}", 20f, true)
            label("${t("earned")}: +$roundGain", 18f)
            label("${t("points")}: $pts", 18f)
            val earned = badges.filter { pts >= it.first }
            if (earned.isNotEmpty()) {
                label("${t("badges")}: " + earned.joinToString("  ") { "${it.second} ${it.third[uiLang]}" }, 16f)
            }
            button(t("again")) { startQuiz(cat) }
            button(t("back")) { if (cat == null) showHome() else showCategory(cat) }
        }
    }
}
