package com.example.imagesearchapp

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.example.imagesearchapp.ViewPager.DepthPageTransformer
import com.example.imagesearchapp.ViewPager.ViewPagerAdapter
import com.example.imagesearchapp.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    val pageradapter = ViewPagerAdapter(this)

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        with(binding) {
            vp2Main.adapter = pageradapter
            vp2Main.setPageTransformer(DepthPageTransformer())
            TabLayoutMediator(tlMainBottom,vp2Main) {tab, postion->
                when(postion) {
                    0 ->tab.setText("이미지 검색")
                    1 ->tab.setText("내 보관함")
                }
            }.attach()
        }


    }

    override fun onBackPressed() {
        if(binding.vp2Main.currentItem == 0) {
            var builder = AlertDialog.Builder(this)
            builder.setIcon(R.drawable.doublechat)
            builder.setTitle("종료")
            builder.setMessage("종료하시겠습니까?")

            val listener = object :DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    when(which) {
                        DialogInterface.BUTTON_POSITIVE -> if(!isFinishing) finish()
                        DialogInterface.BUTTON_NEGATIVE -> dialog?.dismiss()
                    }
                }
            }
            builder.setPositiveButton("확인",listener)
            builder.setNegativeButton("취소",listener)
            builder.show()

        }
        else { // 페이지가 제일 첫장이 아닐경우 뒤로가기 클릭시 페이지 앞으로
            binding.vp2Main.currentItem = binding.vp2Main.currentItem - 1
        }
    }
}