package com.example.sosapplication

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat

class WhatsAppAccesibilityService : AccessibilityService() {
    override fun onAccessibilityEvent(p0: AccessibilityEvent?) {
        if (rootInActiveWindow == null) {
            return
        }
        val rootNodeInfo: AccessibilityNodeInfoCompat =
            AccessibilityNodeInfoCompat.wrap(rootInActiveWindow)
        val messageNodeList: List<AccessibilityNodeInfoCompat> =
            rootNodeInfo.findAccessibilityNodeInfosByViewId("com.whatsapp:id/entry")
        if (messageNodeList == null || messageNodeList.isEmpty()) {
            return
        }
        val messageField: AccessibilityNodeInfoCompat = messageNodeList.get(0)
        if (messageField == null || messageField.text.length == 0 || !messageField.text.endsWith("  ")) {
            return
        }
        val sendMessageNodeList: List<AccessibilityNodeInfoCompat> =
            rootNodeInfo.findAccessibilityNodeInfosByViewId("com.whatsapp:id/send")
        if (sendMessageNodeList == null || sendMessageNodeList.isEmpty()) {
            return
        }
        val sendMessage: AccessibilityNodeInfoCompat = sendMessageNodeList.get(0)
        if (!sendMessage.isVisibleToUser) {
            return
        }
        sendMessage.performAction(AccessibilityNodeInfo.ACTION_CLICK)
        try {
            Thread.sleep(200)
            performGlobalAction(GLOBAL_ACTION_BACK)
            Thread.sleep(200)
        } catch (ignored: InterruptedException) {
        }
        performGlobalAction(GLOBAL_ACTION_BACK)
    }

    override fun onInterrupt() {
        TODO("Not yet implemented")
    }
}