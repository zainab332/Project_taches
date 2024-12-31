package com.example.project1.activities

import LibraryFragment
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import TaskViewModel
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.my_calendr_chat.CardData
import com.example.project1.AboutUsFragment
import com.example.project1.CardAdapter
import com.example.project1.HomeFragment
import com.example.project1.R
import com.example.project1.CategoryFragment
import com.example.project1.ClendarFragment
import com.example.project1.SettingsFragment
import com.example.project1.classes.Category
import com.example.project1.classes.Task
import com.example.project1.classes.TaskPriority
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import java.time.LocalDate
import java.util.Calendar
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private val taskViewModel: TaskViewModel by viewModels()
    private lateinit var fab: FloatingActionButton
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var textView: TextView
    private var dueDate: LocalDate? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val savedLanguage = getSavedLanguage()
        if (savedLanguage != null) {
            setLocale(savedLanguage)
        }
        setContentView(R.layout.activity_main)

        // Initialisation des vues
        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        fab = findViewById(R.id.fab)
        drawerLayout = findViewById(R.id.drawer_layout)
        val navigationView: NavigationView = findViewById(R.id.nav_view)
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)

        setSupportActionBar(toolbar)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.open_navigation_bar, R.string.close_navigation_bar
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> replaceFragment(HomeFragment()) // Remplacez par vos fragments
                R.id.nav_settings -> replaceFragment(SettingsFragment())
                R.id.nav_share -> {
                    val shareIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, getString(R.string.MyApp))
                        type = "text/plain"
                    }
                    startActivity(Intent.createChooser(shareIntent, getString(R.string.ShareBy)))
                    true
                }
                R.id.nav_about -> replaceFragment(AboutUsFragment())
                R.id.nav_logout -> {
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                }
                else -> {
                    Toast.makeText(this, "Option non prise en charge", Toast.LENGTH_SHORT).show()
                }
            }
            // Marquer l'élément comme sélectionné
            menuItem.isChecked = true
            // Fermer le drawer
            drawerLayout.closeDrawers()
            true
        }



        if (savedInstanceState == null) {
            replaceFragment(HomeFragment())
            navigationView.setCheckedItem(R.id.nav_home)
        }

        // Configuration de la BottomNavigationView
        bottomNavigationView.setOnItemSelectedListener { item ->
            val fragment = when (item.itemId) {
                R.id.home -> HomeFragment()
                R.id.shorts -> CategoryFragment()
                R.id.subscriptions -> ClendarFragment()
                R.id.library -> LibraryFragment()
                else -> null
            }
            fragment?.let {
                replaceFragment(it)  // Remplacement du fragment affiché
                true
            } ?: false
        }

        fab.setOnClickListener {
            showBottomDialog()
        }
    }

    // Fonction pour remplacer un fragment affiché dans la vue principale
    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_layout, fragment)
        fragmentTransaction.commit()
    }

    // Fonction pour afficher un dialogue de menu en bas de l'écran
    @RequiresApi(Build.VERSION_CODES.O)
    private fun showBottomDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)  // Supprime le titre par défaut du dialogue
        dialog.setContentView(R.layout.botoom_sheet_layout)

        val addTask = dialog.findViewById<LinearLayout>(R.id.layoutTask)
        val shortsLayout = dialog.findViewById<LinearLayout>(R.id.layoutChallenge)
        val liveLayout = dialog.findViewById<LinearLayout>(R.id.layoutCategory)

        addTask.setOnClickListener {
            dialog.dismiss()
            showAddTaskDialog()
        }

        shortsLayout.setOnClickListener {
            dialog.dismiss()
            showAddCategoryDialog()
        }

        liveLayout.setOnClickListener {
            dialog.dismiss()
            showRecyclerViewDialog()
        }

        dialog.show()
        dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.window?.setGravity(Gravity.BOTTOM) //Positionne le dialogue en bas de l'écran.
    }
    //////////////////////////////
    private fun getSavedLanguage(): String? {
        val sharedPreferences = this.getSharedPreferences("LanguageSettings", Context.MODE_PRIVATE)
        return sharedPreferences.getString("SelectedLanguage", null)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showAddTaskDialog() {
        val dialogTask = Dialog(this)
        dialogTask.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogTask.setContentView(R.layout.tache_alert)
        textView = dialogTask.findViewById(R.id.exem)
        val titleEditText = dialogTask.findViewById<EditText>(R.id.nomtask)
        val button = dialogTask.findViewById<ImageView>(R.id.calen)
        val spinner = dialogTask.findViewById<Spinner>(R.id.spinnerP)
        val btn_add = dialogTask.findViewById<Button>(R.id.add_task_btn)
        val btn_dismiss = dialogTask.findViewById<Button>(R.id.dismiss_task_btn)

        val priorities = listOf(getString(R.string.priorite)) + TaskPriority.values().map {
            when (it) {
                TaskPriority.LOW -> getString(R.string.Low)
                TaskPriority.MEDIUM -> getString(R.string.Medium)
                TaskPriority.HIGH -> getString(R.string.High)
            }
        }

        // Adapter pour le Spinner
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item, // Mise en page par défaut
            priorities
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedPriority = priorities[position]
                textView.text = if (position == 0) {
                    getString(R.string.NoPrio)
                } else {
                    getString(R.string.SelectedPrio)+" : $selectedPriority"
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Aucun élément sélectionné
            }
        }

        //// spinner Category
        val spinnerCategory = dialogTask.findViewById<Spinner>(R.id.spinnerCategory)

        // Récupérer les catégories existantes
        val categories = listOf(getString(R.string.categorie)) + (taskViewModel.categories.value?.map { it.title } ?: emptyList())

        val categoryAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            categories
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }

        spinnerCategory.adapter = categoryAdapter

        spinnerCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedCategory = categories[position]
                if (position == 0) {
                    textView.text = getString(R.string.Nothing)
                } else {
                    textView.text = getString(R.string.SelectedCat)+" : ${selectedCategory}"
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Aucun élément sélectionné
            }
        }


        btn_dismiss.setOnClickListener{
            dialogTask.dismiss()
        }
        btn_add.setOnClickListener {
            val title = titleEditText.text.toString()
            val priority = when (spinner.selectedItem.toString()) {
                getString(R.string.Low) -> TaskPriority.LOW
                getString(R.string.Medium) -> TaskPriority.MEDIUM
                getString(R.string.High) -> TaskPriority.HIGH
                else -> null
            }
            val categoryTitle = spinnerCategory.selectedItem.toString()
            val category = taskViewModel.categories.value?.find { it.title == categoryTitle }

            if (title.isBlank() || priority == null || category == null) {
                Toast.makeText(this, getString(R.string.FillAll), Toast.LENGTH_SHORT).show()
            } else {
                // Créer une nouvelle tâche
                val newTask = Task(id = taskViewModel.getTasksSize()+1,
                    title,
                    isCompleted = false,
                    priority = priority,
                    category = category!!,
                    creationDate = LocalDate.now(),
                    isFavorite = false,
                    isArchived = false,
                    dueDate = dueDate)

                // Ajouter la tâche dans le ViewModel
                taskViewModel.addTask(newTask)

                Toast.makeText(this, getString(R.string.AddedTask), Toast.LENGTH_SHORT).show()
                dialogTask.dismiss()
            }
        }

        button.setOnClickListener {
            openDatePicker()
        }

        dialogTask.show() // Affiche le dialog
        dialogTask.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialogTask.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogTask.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialogTask.window?.setGravity(Gravity.CENTER)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun openDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            R.style.DialogTheme,
            { _, selectedYear, selectedMonth, selectedDay ->
                dueDate = LocalDate.of(selectedYear, selectedMonth + 1, selectedDay) // Sauvegarder la date
                textView.text = "$selectedYear.${selectedMonth + 1}.$selectedDay"
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }


    private fun showRecyclerViewDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.payant, null)
        val recyclerViewDialog = dialogView.findViewById<RecyclerView>(R.id.view_pager)

        val cardDataList = listOf(
            CardData("Zainba Arhamni", "Superbe application !", "Très intuitive et bien conçue.", R.drawable.zainab, R.drawable.stars),
            CardData("WiSaal Kamil", "Quelques bugs.", "Mais globalement satisfait.", R.drawable.wissal, R.drawable.stars),
            CardData("Ahmed Ben Moussa", "J'adore l'interface.", "L'expérience utilisateur est géniale.", R.drawable.ahmad, R.drawable.rating)
        )

        recyclerViewDialog.adapter = CardAdapter(cardDataList)
        recyclerViewDialog.layoutManager = LinearLayoutManager(this,  LinearLayoutManager.HORIZONTAL, false)

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setPositiveButton(getString(R.string.Dismiss), null)
            .create()

        dialog.show()
    }

    //add category dialog
    @RequiresApi(Build.VERSION_CODES.O)
    private fun showAddCategoryDialog() {
        val dialogCat = Dialog(this)
        dialogCat.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialogCat.setContentView(R.layout.category_alert)
        val titleCat = dialogCat.findViewById<EditText>(R.id.nomCategory)
        val subtitleCat = dialogCat.findViewById<EditText>(R.id.subtitle)
        val btn_add_cat = dialogCat.findViewById<Button>(R.id.add_cat_btn)
        val btn_dismiss_cat = dialogCat.findViewById<Button>(R.id.dismiss_cat_btn)

        btn_dismiss_cat.setOnClickListener{
            dialogCat.dismiss()
        }
        btn_add_cat.setOnClickListener {
            val title = titleCat.text.toString()
            val subtitle = subtitleCat.text?.toString() ?: getString(R.string.NothingAdded)

            if (title.isBlank()) {
                Toast.makeText(this, getString(R.string.titleNeeded), Toast.LENGTH_SHORT).show()
            } else {
                val newCat = Category(taskViewModel.getCategorySize(),title, subtitle, R.drawable.work)
                taskViewModel.addCategory(newCat)

                Toast.makeText(this, getString(R.string.CategoryAdded), Toast.LENGTH_SHORT).show()
                dialogCat.dismiss()
            }
        }

        dialogCat.show() // Affiche le dialog
        dialogCat.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialogCat.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogCat.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialogCat.window?.setGravity(Gravity.CENTER)
    }
    private fun loadLocale() {
        val sharedPreferences: SharedPreferences = getSharedPreferences("Settings", Context.MODE_PRIVATE)
        val languageCode = sharedPreferences.getString("App_Language", Locale.getDefault().language) ?: Locale.getDefault().language
        setLocale(languageCode)
    }

    private fun setLocale(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = Configuration(resources.configuration)
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
        val sharedPreferences: SharedPreferences = getSharedPreferences("Settings", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("App_Language", languageCode).apply()

    }
}

