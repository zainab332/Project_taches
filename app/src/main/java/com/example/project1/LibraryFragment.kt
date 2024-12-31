import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.project1.R
import com.example.project1.classes.Task
import com.example.project1.classes.TaskPriority
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.components.Description

class LibraryFragment : Fragment() {

    private val taskViewModel: TaskViewModel by activityViewModels()

    @SuppressLint("MissingInflatedId")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_library, container, false)

        val barChart = view.findViewById<BarChart>(R.id.barChart)
        val pieChart = view.findViewById<PieChart>(R.id.pieChart)
        val lineChart = view.findViewById<LineChart>(R.id.lineChart)

        taskViewModel.tasks.observe(viewLifecycleOwner, Observer { tasks ->
            updateBarChart(barChart, tasks)
            updatePieChart(pieChart, tasks)
            updateLineChart(lineChart, tasks)
        })

        val cardViewBar = view.findViewById<CardView>(R.id.cardBarChart)
        val cardViewPie = view.findViewById<CardView>(R.id.cardPieChart)
        val cardViewLine = view.findViewById<CardView>(R.id.cardLineChart)

        barChart.setOnLongClickListener {
            showChartDialog("Bar Chart", getString(R.string.BarDesc), barChart)
            true
        }

        pieChart.setOnLongClickListener {
            showChartDialog("Pie Chart", getString(R.string.PieDesc), pieChart)
            true
        }

        lineChart.setOnLongClickListener {
            showChartDialog("Line Chart", getString(R.string.LineDesc), lineChart)
            true
        }

        return view
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateBarChart(barChart: BarChart, tasks: List<Task>) {
        if (tasks.isNullOrEmpty()) {
            barChart.clear()
            barChart.invalidate()
            return
        }

        val tasksByMonth = tasks.filter { it.isCompleted }
            .groupingBy { it.creationDate.month.value }
            .eachCount()

        val barEntries = tasksByMonth.map { BarEntry(it.key.toFloat(), it.value.toFloat()) }
        val barDataSet = BarDataSet(barEntries, "Tâches terminées par mois")
        barDataSet.color = ContextCompat.getColor(requireContext(), R.color.blue)
        val barData = BarData(barDataSet)

        barChart.description.isEnabled = false
        barChart.data = barData
        barChart.invalidate()
    }

    private fun updatePieChart(pieChart: PieChart, tasks: List<Task>) {
        if (tasks.isNullOrEmpty()) {
            pieChart.clear()
            pieChart.invalidate()
            return
        }

        val tasksByCategory = tasks.groupingBy { it.category.title }.eachCount()

        val pieEntries = tasksByCategory.map { PieEntry(it.value.toFloat(), it.key) }
        val pieDataSet = PieDataSet(pieEntries, "Répartition par catégorie")
        pieDataSet.colors = listOf(
            ContextCompat.getColor(requireContext(), R.color.light_blue),
            ContextCompat.getColor(requireContext(), R.color.blue),
            ContextCompat.getColor(requireContext(), R.color.grey),
            ContextCompat.getColor(requireContext(), R.color.gray)
        )
        val pieData = PieData(pieDataSet)

        pieChart.description.isEnabled = false
        pieChart.data = pieData
        pieChart.invalidate()
    }

    private fun updateLineChart(lineChart: LineChart, tasks: List<Task>) {
        if (tasks.isNullOrEmpty()) {
            lineChart.clear()
            lineChart.invalidate()
            return
        }

        val tasksByPriority = tasks.groupingBy { it.priority.ordinal }.eachCount()

        val lineEntries = tasksByPriority.map { Entry(it.key.toFloat(), it.value.toFloat()) }
        val lineDataSet = LineDataSet(lineEntries, "Priorités des tâches")
        lineDataSet.color = ContextCompat.getColor(requireContext(), R.color.blue)
        lineDataSet.setCircleColor(ContextCompat.getColor(requireContext(), R.color.light_blue))
        lineDataSet.lineWidth = 2f
        val lineData = LineData(lineDataSet)

        lineChart.description.isEnabled = false
        lineChart.data = lineData
        lineChart.invalidate()
    }

    @SuppressLint("ResourceAsColor")
    private fun showChartDialog(title: String, description: String, chart: View) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_chart, null)
        val chartContainer = dialogView.findViewById<ViewGroup>(R.id.chartContainer)
        val descriptionTextView = dialogView.findViewById<TextView>(R.id.chartDescription)

        descriptionTextView.text = description

        // Recréation de la charte selon le type
        when (chart) {
            is BarChart -> {
                val newChart = BarChart(requireContext())
                newChart.layoutParams = chart.layoutParams
                newChart.data = chart.data
                newChart.description.isEnabled = false
                chartContainer.addView(newChart)
            }
            is PieChart -> {
                val newChart = PieChart(requireContext())
                newChart.layoutParams = chart.layoutParams
                newChart.data = chart.data
                newChart.description.isEnabled = false
                chartContainer.addView(newChart)
            }
            is LineChart -> {
                val newChart = LineChart(requireContext())
                newChart.layoutParams = chart.layoutParams
                newChart.data = chart.data
                newChart.description.isEnabled = false
                chartContainer.addView(newChart)
            }
        }

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()
        dialog.show()
        dialog.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            attributes?.windowAnimations = R.style.DialogAnimation
            setGravity(Gravity.CENTER)
        }
    }

}
