package vn.edu.hust.studentman

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

  private val students = mutableListOf(
    StudentModel("Nguyễn Văn An", "SV001"),
    StudentModel("Trần Thị Bảo", "SV002"),
    StudentModel("Lê Hoàng Cường", "SV003"),
    StudentModel("Phạm Thị Dung", "SV004"),
    StudentModel("Đỗ Minh Đức", "SV005"),
    StudentModel("Vũ Thị Hoa", "SV006"),
    StudentModel("Hoàng Văn Hải", "SV007"),
    StudentModel("Bùi Thị Hạnh", "SV008"),
    StudentModel("Đinh Văn Hùng", "SV009"),
    StudentModel("Nguyễn Thị Linh", "SV010"),
    StudentModel("Phạm Văn Long", "SV011"),
    StudentModel("Trần Thị Mai", "SV012"),
    StudentModel("Lê Thị Ngọc", "SV013"),
    StudentModel("Vũ Văn Nam", "SV014"),
    StudentModel("Hoàng Thị Phương", "SV015"),
    StudentModel("Đỗ Văn Quân", "SV016"),
    StudentModel("Nguyễn Thị Thu", "SV017"),
    StudentModel("Trần Văn Tài", "SV018"),
    StudentModel("Phạm Thị Tuyết", "SV019"),
    StudentModel("Lê Văn Vũ", "SV020")
  )
  private lateinit var studentAdapter: StudentAdapter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    studentAdapter = StudentAdapter(
      students,
      onEditClick = { student -> showEditStudentDialog(student) },
      onDeleteClick = { student -> deleteStudent(student) }
    )

    findViewById<RecyclerView>(R.id.recycler_view_students).run {
      adapter = studentAdapter
      layoutManager = LinearLayoutManager(this@MainActivity)
    }

    findViewById<Button>(R.id.btn_add_new).setOnClickListener {
      showAddStudentDialog()
    }
  }

  private fun showAddStudentDialog() {
    val dialogView = layoutInflater.inflate(R.layout.dialog_student, null)
    val dialog = AlertDialog.Builder(this)
      .setView(dialogView)
      .setTitle("Add New Student")
      .create()

    dialogView.findViewById<Button>(R.id.btn_save).setOnClickListener {
      val name = dialogView.findViewById<EditText>(R.id.et_student_name).text.toString()
      val studentId = dialogView.findViewById<EditText>(R.id.et_student_id).text.toString()

      if (name.isNotBlank() && studentId.isNotBlank()) {
        val newStudent = StudentModel(name, studentId)
        students.add(newStudent)
        studentAdapter.notifyItemInserted(students.size - 1)
      }

      dialog.dismiss()
    }
    dialog.show()
  }

  private fun showEditStudentDialog(student: StudentModel) {
    val dialogView = layoutInflater.inflate(R.layout.dialog_student, null)
    val dialog = AlertDialog.Builder(this)
      .setView(dialogView)
      .setTitle("Edit Student")
      .create()

    dialogView.findViewById<EditText>(R.id.et_student_name).setText(student.studentName)
    dialogView.findViewById<EditText>(R.id.et_student_id).setText(student.studentId)

    dialogView.findViewById<Button>(R.id.btn_save).setOnClickListener {
      val updatedName = dialogView.findViewById<EditText>(R.id.et_student_name).text.toString()
      val updatedId = dialogView.findViewById<EditText>(R.id.et_student_id).text.toString()

      val index = students.indexOf(student)
      if (index >= 0 && updatedName.isNotBlank() && updatedId.isNotBlank()) {
        students[index] = StudentModel(updatedName, updatedId)
        studentAdapter.notifyItemChanged(index)
      }

      dialog.dismiss()
    }
    dialog.show()
  }

  private fun deleteStudent(student: StudentModel) {
    val index = students.indexOf(student)
    if (index >= 0) {
      AlertDialog.Builder(this)
        .setTitle("Delete Student")
        .setMessage("Are you sure you want to delete this student?")
        .setPositiveButton("Yes") { _, _ ->
          students.removeAt(index)
          studentAdapter.notifyItemRemoved(index)

          Snackbar.make(
            findViewById(R.id.main),
            "Student deleted",
            Snackbar.LENGTH_LONG
          ).setAction("Undo") {
            students.add(index, student)
            studentAdapter.notifyItemInserted(index)
          }.show()
        }
        .setNegativeButton("No", null)
        .show()
    }
  }
}
