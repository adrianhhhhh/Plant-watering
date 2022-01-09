package compmobiles.projet

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import compmobiles.projet.databinding.ActivityListeArrosageBinding

class ListeArrosageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListeArrosageBinding
    private lateinit var viewmodel: ListeArrosageViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityListeArrosageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbararrosage)

        viewmodel = ViewModelProvider(this).get(ListeArrosageViewModel::class.java)

        val dao = PlantesDatabase.getDatabase(application).plantesDao()

        if (viewmodel.plantesAArroser == null) {
            var plantes: List<Plante>? = null
            val t = Thread {
                plantes = dao.loadAllPlantes()
            }
            t.start()
            t.join()
            viewmodel.plantesAArroser = filtrerPlantesAArroser(plantes!!)
        }

        if (viewmodel.plantesCochees == null) {
            viewmodel.plantesCochees = mutableListOf<Plante>()
        }

        val listeArrosageAdapter = ListeArrosageAdapter(this, viewmodel.plantesAArroser!!, viewmodel.plantesCochees!!)
        //binding.recyclerplantes.hasFixedSize()
        binding.recyclerarrosage.layoutManager = LinearLayoutManager(this)
        binding.recyclerarrosage.adapter = listeArrosageAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_liste_arrosage, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_valider -> { true }
            else -> super.onOptionsItemSelected(item)
        }
    }
}