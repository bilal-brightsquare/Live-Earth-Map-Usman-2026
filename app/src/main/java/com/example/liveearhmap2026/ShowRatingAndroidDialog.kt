package com.example.liveearhmap2026
import android.app.Activity
import android.content.Context
import com.google.android.gms.tasks.Task
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory

object ShowRatingAndroidDialog {

    var reviewManager: ReviewManager? = null
    var reviewInfo:ReviewInfo? = null


    fun initializeRateDialog(applicationContext:Context){
        try {
            if (reviewManager==null || reviewInfo==null){
                reviewManager = ReviewManagerFactory.create(applicationContext)
                val manager = reviewManager?.requestReviewFlow()
                manager?.addOnCompleteListener { task: Task<ReviewInfo?> ->
                    if (task.isSuccessful) {
                        reviewInfo = task.result
                    }
                }
            }
        } catch (e: Exception) {
        }
    }

    fun showRateDialog(context: Activity, runnable: Runnable){
        try {
            if (reviewInfo != null) {
                val flow = reviewManager!!.launchReviewFlow(context, reviewInfo!!)
                flow.addOnCompleteListener {
                    if (runnable!=null)
                        runnable.run()
                }
            } else {
                if (runnable!=null)
                    runnable.run()
            }
        } catch (e: Exception) {
            if (runnable!=null)
                runnable.run()
        }
    }

}