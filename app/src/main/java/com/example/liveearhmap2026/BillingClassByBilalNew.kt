package com.example.liveearhmap2026

import android.app.Activity
import android.util.Log
import android.widget.Toast
import com.android.billingclient.api.*

class BillingClassByBilalNew(private val activity: Activity) {

    companion object {
        val pricesList = ArrayList<String>()
        val titleList = ArrayList<String>()

        // CHANGE PRODUCT ID BEFORE PURCHASE
        var productID = "weekly"   // weekly / monthly / annual
        var offerID = "trial"      // free trial offerId

        var billingClient: BillingClient? = null
        var purchaseCallBack: ((Boolean) -> Unit)? = null
        var isPurchased=false
    }

    /* ---------------- PURCHASE ACK ---------------- */

    private fun acknowledge(purchase: Purchase) {
        if (purchase.isAcknowledged) return

        val params = AcknowledgePurchaseParams
            .newBuilder()
            .setPurchaseToken(purchase.purchaseToken)
            .build()

        billingClient?.acknowledgePurchase(params) {
            if (it.responseCode == BillingClient.BillingResponseCode.OK) {
                isPurchased = true
            }
        }
    }

    /* ---------------- INIT BILLING ---------------- */

    fun initializeBilling(callBack: (Boolean) -> Unit) {
        if (billingClient != null) {
            callBack.invoke(isPurchased)
            return
        }

        billingClient = BillingClient.newBuilder(activity)
            .enablePendingPurchases()
            .setListener { _, purchases ->
                purchases?.forEach {
                    if (it.purchaseState == Purchase.PurchaseState.PURCHASED) {
                        isPurchased = true
                        acknowledge(it)
                        purchaseCallBack?.invoke(true)
                        return@setListener
                    }
                }
                purchaseCallBack?.invoke(false)
            }
            .build()

        billingClient!!.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(result: BillingResult) {
                if (result.responseCode == BillingClient.BillingResponseCode.OK) {
                    queryActiveSubscriptions(callBack)
                    fetchProducts()
                } else {
                    callBack(false)
                }
            }

            override fun onBillingServiceDisconnected() {}
        })
    }

    /* ---------------- CHECK ACTIVE SUBS ---------------- */

    private fun queryActiveSubscriptions(callBack: (Boolean) -> Unit) {
        val params = QueryPurchasesParams.newBuilder()
            .setProductType(BillingClient.ProductType.SUBS)
            .build()

        billingClient?.queryPurchasesAsync(params) { _, purchases ->
            isPurchased = purchases.any {
                it.purchaseState == Purchase.PurchaseState.PURCHASED
            }
            callBack.invoke(isPurchased)
        }
    }

    /* ---------------- FETCH PRODUCTS ---------------- */

    private fun fetchProducts() {
        val products = listOf(
            "monthly",
            "annual",
            "weekly"
        ).map {
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId(it)
                .setProductType(BillingClient.ProductType.SUBS)
                .build()
        }

        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(products)
            .build()

        billingClient?.queryProductDetailsAsync(params) { _, list ->
            pricesList.clear()
            titleList.clear()

            list.forEach { product ->
                titleList.add(product.name)

                val price = product.subscriptionOfferDetails
                    ?.firstOrNull()
                    ?.pricingPhases
                    ?.pricingPhaseList
                    ?.firstOrNull()
                    ?.formattedPrice ?: "0"

                pricesList.add(price)

                Log.e("Billing", "Product ${product.productId} price $price")
            }
        }
    }

    /* ---------------- PURCHASE FLOW ---------------- */

    fun purchasePremium(callBack: (Boolean) -> Unit) {
        purchaseCallBack = callBack

        if (isPurchased) {
            Toast.makeText(activity, "Already Premium", Toast.LENGTH_LONG).show()
            callBack(true)
            return
        }

        val product = QueryProductDetailsParams.Product.newBuilder()
            .setProductId(productID)
            .setProductType(BillingClient.ProductType.SUBS)
            .build()

        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(listOf(product))
            .build()

        billingClient?.queryProductDetailsAsync(params) { _, list ->
            if (list.isEmpty()) return@queryProductDetailsAsync

            val productDetails = list[0]

            // Find FREE TRIAL OFFER
            val offer = productDetails.subscriptionOfferDetails
                ?.firstOrNull { it.offerId == offerID }
                ?: productDetails.subscriptionOfferDetails?.firstOrNull()

            if (offer == null) return@queryProductDetailsAsync

            val billingParams = BillingFlowParams.newBuilder()
                .setProductDetailsParamsList(
                    listOf(
                        BillingFlowParams.ProductDetailsParams.newBuilder()
                            .setProductDetails(productDetails)
                            .setOfferToken(offer.offerToken)
                            .build()
                    )
                )
                .build()

            billingClient?.launchBillingFlow(activity, billingParams)
        }
    }


    //////

    fun fetchWeeklyPrice(onPriceFetched: (String) -> Unit) {

        val product = QueryProductDetailsParams.Product.newBuilder()
            .setProductId("weekly")
            .setProductType(BillingClient.ProductType.SUBS)
            .build()

        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(listOf(product))
            .build()

        billingClient?.queryProductDetailsAsync(params) { _, productDetailsList ->

            if (productDetailsList.isEmpty()) {
                onPriceFetched("0")
                return@queryProductDetailsAsync
            }

            val productDetails = productDetailsList[0]

            // Get Weekly Price (Paid phase, NOT free trial)
            val weeklyPrice = productDetails.subscriptionOfferDetails
                ?.flatMap { it.pricingPhases.pricingPhaseList }
                ?.firstOrNull { it.priceAmountMicros > 0 }
                ?.formattedPrice
                ?: "0"

            onPriceFetched(weeklyPrice)
        }
    }

}
