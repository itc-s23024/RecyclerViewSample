package jp.ac.it_college.std.s23024.recyclerviewsample

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import jp.ac.it_college.std.s23024.recyclerviewsample.databinding.ActivityMainBinding
import kotlinx.serialization.json.Json

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // メニューの JSON データファイルを読み込む
        val jsonString = resources.openRawResource(R.raw.set_meal).reader().readText()
        // jsonString をパース(解析)して,ちゃんとしたオブジェクト(SetMeal)にする。 <- buildにalias(libs.plugins.jetbrains.kotlinx.serialization)を追加して
        val setMealList = Json.decodeFromString<List<SetMeal>>(jsonString)

        // RecyclerView を使うための処理
        binding.setMealList.apply {
            // RecyclerView でデータの表示形式を管理しているレイアウトマネージャを用意。
            //　今回は ListView のような表示形式を実現する　LinearLayoutManager を使用。
            val linearLayoutManager = LinearLayoutManager(this@MainActivity)
            //レイアウトマネージャをセット
            layoutManager = linearLayoutManager

            // データを管理するアダプタのインスタンスを生成する
            val setMealAdapter = SetMealAdapter(setMealList) { item ->
                // 今回は選ばれた定食の名前をトーストする
                Toast.makeText(
                    this@MainActivity,
                    "選択された定食: ${item.name}",
                    Toast.LENGTH_LONG
                ).show()
            }
            adapter = setMealAdapter

            // アイテムの区切り線を入れたい
            addItemDecoration(
                DividerItemDecoration(this@MainActivity, linearLayoutManager.orientation)
            )
        }
    }
}