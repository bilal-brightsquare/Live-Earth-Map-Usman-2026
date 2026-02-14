package com.example.liveearhmap2026

import android.app.Activity
import android.util.Log
import android.widget.Toast
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClient.SkuType
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesResponseListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchasesParams
import com.android.billingclient.api.SkuDetailsParams
class BillingClassByBilal(val activity: Activity) {
    companion object {
        val priceslist: ArrayList<String> = ArrayList()
        val titlelist: ArrayList<String> = ArrayList()
        var productID = "monthly"
        var billingClient: BillingClient? = null
        var purchaseCallBack: ((Boolean) -> Unit)? = null
//        var isPurchased=false
    }
    var isPremiumUser = false


    private fun verifySubPurchase(purchases: Purchase) {
        val acknowledgePurchaseParams = AcknowledgePurchaseParams
            .newBuilder()
            .setPurchaseToken(purchases.purchaseToken)
            .build()
        billingClient!!.acknowledgePurchase(acknowledgePurchaseParams) { billingResult ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                isPremiumUser = true
            }
        }
    }

    fun initalizeBilling(callBack: (Boolean) -> Unit) {
        // initalize billing library
        if (billingClient == null) {
            billingClient = BillingClient.newBuilder(activity)
                .enablePendingPurchases()
                .setListener { billingResult: BillingResult, list: List<Purchase>? ->
                    if (list != null) {
                        for (purchase in list) {
                            if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED || purchase.purchaseState == Purchase.PurchaseState.UNSPECIFIED_STATE) {
                                Log.e("BillingClassByBilal", "premium user")
                                isPremiumUser=true
                                verifySubPurchase(purchase)
                                purchaseCallBack?.invoke(true)
                                return@setListener
                            } else {
                                Log.e("BillingClassByBilal", "not premium")
                            }
                        }
                        purchaseCallBack?.invoke(false)
                    }
                }.build()

            billingClient!!.startConnection(object : BillingClientStateListener {
                override fun onBillingSetupFinished(billingResult: BillingResult) {
                    Log.e("BillingClassByBilal", "billing setup completed")

                    if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {

                        // The BillingClient is ready. You can query purchases here.
                        Log.e("BillingClassByBilal", "billing response is OK")

                        val param =
                            QueryPurchasesParams.newBuilder()
                                .setProductType(BillingClient.ProductType.SUBS).build()

                        billingClient!!.queryPurchasesAsync(
                            param,
                            PurchasesResponseListener { billingResult, purchases ->
                                Log.e("BillingClassByBilal", "size = ${purchases.size}")
                                if (purchases.isEmpty()) {
                                    getPrize {
//                                        isPremiumUser=false
                                    }
                                    Log.e("BillingClassByBilal", "purchase list is empty")
                                    callBack.invoke(false)
                                }
                                else{
                                    callBack.invoke(true)
                                    getPrize {

                                    }
                                    purchases.forEach {
                                        if (it.purchaseState == Purchase.PurchaseState.PURCHASED) {
                                            Log.e("BillingClassByBilal", "purchased item packageName "+it.packageName)
                                        } else {
                                            Log.e("BillingClassByBilal", "Not purchased item packageName "+it.packageName)
                                        }
                                    }
                                    for (purchase in purchases) {

                                    }
                                }
                            })
                    } else {
                        callBack.invoke(false)
                    }
                }

                override fun onBillingServiceDisconnected() {}
            })
        }
        else {
            callBack.invoke(isPremiumUser)
        }
    }

    private fun querySkuDetailsAsync(skuList: List<String>) {
        val params = SkuDetailsParams.newBuilder()
            .setSkusList(skuList)
            .setType(SkuType.SUBS)
            .build()

        billingClient?.querySkuDetailsAsync(
            params
        ) { billingResult, skuDetailsList ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && skuDetailsList != null) {
                if (skuDetailsList.size >= 1) {
                    if (!isPremiumUser) {
                        val skuDetails = skuDetailsList[0]
                        val builder: BillingFlowParams =
                            BillingFlowParams.newBuilder().setSkuDetails(skuDetails).build()
                        billingClient?.launchBillingFlow(activity, builder)

                    } else {
                        Toast.makeText(activity, "Already a premium user", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun getPrize(callBack: (String) -> Unit): String {
        val productList = ArrayList<QueryProductDetailsParams.Product>()

        productList.add(
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId("monthly")
                .setProductType(BillingClient.ProductType.SUBS)
                .build()
        )
        productList.add(
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId("weekly")
                .setProductType(BillingClient.ProductType.SUBS)
                .build()
        )


        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(productList)
            .build()
        billingClient!!.queryProductDetailsAsync(
            params
        ) { billingResult: BillingResult?, list: List<ProductDetails> ->
            try {
                if (list.size != 0) {
                    priceslist.clear()
                    titlelist.clear()
                    Log.e("BillingClassByBilal", "list \n" + list.toString())
                    for ((index, element) in list.withIndex()) {
                        Log.e("BillingClassByBilal", " title " + index + (list[index].name))
                        titlelist.add(list[index].name)
                        Log.e(
                            "BillingClassByBilal",
                            " price " + index + (list[index].subscriptionOfferDetails?.get(0)?.pricingPhases?.pricingPhaseList?.get(
                                0
                            )?.formattedPrice ?: "0")
                        )
                        priceslist.add(
                            list[index].subscriptionOfferDetails?.get(0)?.pricingPhases?.pricingPhaseList?.get(
                                0
                            )?.formattedPrice ?: "0"
                        )
//                        Log.e("BillingClassByBilal", " price2 "+index + (list[index].subscriptionOfferDetails?.get(0)?.pricingPhases?.pricingPhaseList?.get(0)?.formattedPrice ?: "0"))
//                        priceslist.add(list[index].subscriptionOfferDetails?.get(0)?.pricingPhases?.pricingPhaseList?.get(1)?.formattedPrice ?: "0")
                    }
                } else {
                    callBack.invoke(
                        "0"
                    )
                }

            } catch (_: Exception) {

            }
        }
        return ""
    }

    fun purchasePremium(callBack: (Boolean) -> Unit) {
        purchaseCallBack = callBack
        querySkuDetailsAsync(listOf(productID))
    }

}