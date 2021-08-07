package uz.instat.tasklist.framework.services.widget

import android.content.Intent
import android.widget.RemoteViewsService

class WidgetService : RemoteViewsService() {

    override fun onGetViewFactory(intent: Intent) = ViewFactory(applicationContext)
}
