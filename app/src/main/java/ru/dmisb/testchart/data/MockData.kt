package ru.dmisb.testchart.data

import com.google.gson.Gson
import ru.dmisb.testchart.data.model.ChartData

object MockData {

    private val gson = Gson()

    private val dailyData = """
        {"history":[
            {"prodline_id":1,"prodline_title":"2.1","data":[
                    {"time":"2020-01-26 08:00:00","value":0.98478849760367,"total":3600,"totalPeriod":3600},
                    {"time":"2020-01-26 09:00:00","value":0.97895833333333,"total":3600,"totalPeriod":3600},
                    {"time":"2020-01-26 10:00:00","value":0.959053969577,"total":3600,"totalPeriod":3600},
                    {"time":"2020-01-26 11:00:00","value":0.98000416579879,"total":3600,"totalPeriod":3600},
                    {"time":"2020-01-26 12:00:00","value":0.98499687434882,"total":3600,"totalPeriod":3600},
                    {"time":"2020-01-26 13:00:00","value":0.98478849760367,"total":3600,"totalPeriod":3600},
                    {"time":"2020-01-26 14:00:00","value":0.97895833333333,"total":3600,"totalPeriod":3600},
                    {"time":"2020-01-26 15:00:00","value":0.959053969577,"total":3600,"totalPeriod":3600},
                    {"time":"2020-01-26 16:00:00","value":0.98000416579879,"total":3600,"totalPeriod":3600},
                    {"time":"2020-01-26 17:00:00","value":0.98499687434882,"total":3600,"totalPeriod":3600}
                ],"kim":0.96897848476113,"total":57214,"totalWork":57214,"totalPeriod":86400},
            {"prodline_id":2,"prodline_title":"2.2","data":[
                    {"time":"2020-01-26 08:00:00","value":0.99100291082297,"total":3600,"totalPeriod":3600},
                    {"time":"2020-01-26 09:00:00","value":0.95530282993917,"total":3600,"totalPeriod":3600},
                    {"time":"2020-01-26 10:00:00","value":0.93887271765017,"total":3600,"totalPeriod":3600},
                    {"time":"2020-01-26 11:00:00","value":0.90119047619048,"total":3600,"totalPeriod":3600},
                    {"time":"2020-01-26 12:00:00","value":0.99047619047619,"total":3600,"totalPeriod":3600},
                    {"time":"2020-01-26 13:00:00","value":0.83897058823529,"total":3600,"totalPeriod":3600},
                    {"time":"2020-01-26 14:00:00","value":0.84697712418301,"total":3600,"totalPeriod":3600},
                    {"time":"2020-01-26 15:00:00","value":0.90545840823664,"total":3600,"totalPeriod":3600},
                    {"time":"2020-01-26 16:00:00","value":0.96192810457516,"total":3600,"totalPeriod":3600},
                    {"time":"2020-01-26 17:00:00","value":0.90089869281046,"total":3600,"totalPeriod":3600}
                ],"kim":0.97046870315467,"total":57214,"totalWork":57214,"totalPeriod":86400},
            {"prodline_id":7,"prodline_title":"2.3","data":[
                    {"time":"2020-01-26 08:00:00","value":0.94244950304585,"total":3600,"totalPeriod":3600},
                    {"time":"2020-01-26 09:00:00","value":0.97355769230769,"total":3600,"totalPeriod":3600},
                    {"time":"2020-01-26 10:00:00","value":0.98300737415838,"total":3600,"totalPeriod":3600},
                    {"time":"2020-01-26 11:00:00","value":0.97211538461538,"total":3600,"totalPeriod":3600},
                    {"time":"2020-01-26 12:00:00","value":0.96923076923077,"total":3600,"totalPeriod":3600},
                    {"time":"2020-01-26 13:00:00","value":0.90711652508415,"total":3600,"totalPeriod":3600},
                    {"time":"2020-01-26 14:00:00","value":0.95280448717949,"total":3600,"totalPeriod":3600},
                    {"time":"2020-01-26 15:00:00","value":0.97932360955281,"total":3600,"totalPeriod":3600},
                    {"time":"2020-01-26 16:00:00","value":0.96113782051282,"total":3600,"totalPeriod":3600},
                    {"time":"2020-01-26 17:00:00","value":0.93205128205128,"total":3600,"totalPeriod":3600}
                ],"kim":0.9665557907469,"total":57214,"totalWork":57214,"totalPeriod":86400},
            {"prodline_id":9,"prodline_title":"4.0","data":[
                    {"time":"2020-01-26 08:00:00","value":0.96604239555464,"total":3600,"totalPeriod":3600},
                    {"time":"2020-01-26 09:00:00","value":0.97787610619469,"total":3600,"totalPeriod":3600},
                    {"time":"2020-01-26 10:00:00","value":0.95802469135802,"total":3600,"totalPeriod":3600},
                    {"time":"2020-01-26 11:00:00","value":0.9843621399177,"total":3600,"totalPeriod":3600},
                    {"time":"2020-01-26 12:00:00","value":0.97788065843621,"total":3600,"totalPeriod":3600},
                    {"time":"2020-01-26 13:00:00","value":0.95488313100199,"total":3600,"totalPeriod":3600},
                    {"time":"2020-01-26 14:00:00","value":0.95498188405797,"total":3600,"totalPeriod":3600},
                    {"time":"2020-01-26 15:00:00","value":0.97517666243885,"total":3600,"totalPeriod":3600},
                    {"time":"2020-01-26 16:00:00","value":0.94964680311538,"total":3600,"totalPeriod":3600},
                    {"time":"2020-01-26 17:00:00","value":0.9838738901975,"total":3600,"totalPeriod":3600}
                ],"kim":0.97728772760803,"total":57214,"totalWork":57214,"totalPeriod":86400},
            {"prodline_id":10,"prodline_title":"4.1","data":[
                    {"time":"2020-01-26 08:00:00","value":0.92005130397606,"total":3600,"totalPeriod":3600},
                    {"time":"2020-01-26 09:00:00","value":0.94679487179487,"total":3600,"totalPeriod":3600},
                    {"time":"2020-01-26 10:00:00","value":0.93437366395896,"total":3600,"totalPeriod":3600},
                    {"time":"2020-01-26 11:00:00","value":0.97478632478632,"total":3600,"totalPeriod":3600},
                    {"time":"2020-01-26 12:00:00","value":0.90106837606838,"total":3600,"totalPeriod":3600},
                    {"time":"2020-01-26 13:00:00","value":0.95515873015873,"total":3600,"totalPeriod":3600},
                    {"time":"2020-01-26 14:00:00","value":0.95555555555556,"total":3600,"totalPeriod":3600},
                    {"time":"2020-01-26 15:00:00","value":0.93290988487495,"total":3600,"totalPeriod":3600},
                    {"time":"2020-01-26 16:00:00","value":0.96846489488298,"total":3600,"totalPeriod":3600},
                    {"time":"2020-01-26 17:00:00","value":0.97082175466455,"total":3600,"totalPeriod":3600}
                ],"kim":0.93346068987785,"total":57214,"totalWork":57214,"totalPeriod":86400},
            {"prodline_id":11,"prodline_title":"4.2","data":[
                    {"time":"2020-01-26 08:00:00","value":0.95515873015873,"total":3600,"totalPeriod":3600},
                    {"time":"2020-01-26 09:00:00","value":0.95555555555556,"total":3600,"totalPeriod":3600},
                    {"time":"2020-01-26 10:00:00","value":0.93290988487495,"total":3600,"totalPeriod":3600},
                    {"time":"2020-01-26 11:00:00","value":0.96846489488298,"total":3600,"totalPeriod":3600},
                    {"time":"2020-01-26 12:00:00","value":0.97082175466455,"total":3600,"totalPeriod":3600},
                    {"time":"2020-01-26 13:00:00","value":0.96604239555464,"total":3600,"totalPeriod":3600},
                    {"time":"2020-01-26 14:00:00","value":0.97787610619469,"total":3600,"totalPeriod":3600},
                    {"time":"2020-01-26 15:00:00","value":0.95802469135802,"total":3600,"totalPeriod":3600},
                    {"time":"2020-01-26 16:00:00","value":0.9843621399177,"total":3600,"totalPeriod":3600},
                    {"time":"2020-01-26 17:00:00","value":0.97788065843621,"total":3600,"totalPeriod":3600}
                ],"kim":0.95531235181072,"total":57214,"totalWork":57214,"totalPeriod":86400},
            {"prodline_id":4,"prodline_title":"5.1","data":[
                    {"time":"2020-01-26 08:00:00","value":0.95488313100199,"total":3600,"totalPeriod":3600},
                    {"time":"2020-01-26 09:00:00","value":0.95498188405797,"total":3600,"totalPeriod":3600},
                    {"time":"2020-01-26 10:00:00","value":0.97517666243885,"total":3600,"totalPeriod":3600},
                    {"time":"2020-01-26 11:00:00","value":0.94964680311538,"total":3600,"totalPeriod":3600},
                    {"time":"2020-01-26 12:00:00","value":0.9838738901975,"total":3600,"totalPeriod":3600},
                    {"time":"2020-01-26 13:00:00","value":0.94244950304585,"total":3600,"totalPeriod":3600},
                    {"time":"2020-01-26 14:00:00","value":0.97355769230769,"total":3600,"totalPeriod":3600},
                    {"time":"2020-01-26 15:00:00","value":0.98300737415838,"total":3600,"totalPeriod":3600},
                    {"time":"2020-01-26 16:00:00","value":0.97211538461538,"total":3600,"totalPeriod":3600},
                    {"time":"2020-01-26 17:00:00","value":0.96923076923077,"total":3600,"totalPeriod":3600}
                ],"kim":0.970624465355,"total":57214,"totalWork":57214,"totalPeriod":86400},
            {"prodline_id":5,"prodline_title":"5.2","data":[
                    {"time":"2020-01-26 08:00:00","value":0.90711652508415,"total":3600,"totalPeriod":3600},
                    {"time":"2020-01-26 09:00:00","value":0.95280448717949,"total":3600,"totalPeriod":3600},
                    {"time":"2020-01-26 10:00:00","value":0.97932360955281,"total":3600,"totalPeriod":3600},
                    {"time":"2020-01-26 11:00:00","value":0.96113782051282,"total":3600,"totalPeriod":3600},
                    {"time":"2020-01-26 12:00:00","value":0.93205128205128,"total":3600,"totalPeriod":3600},
                    {"time":"2020-01-26 13:00:00","value":0.99100291082297,"total":3600,"totalPeriod":3600},
                    {"time":"2020-01-26 14:00:00","value":0.95530282993917,"total":3600,"totalPeriod":3600},
                    {"time":"2020-01-26 15:00:00","value":0.93887271765017,"total":3600,"totalPeriod":3600},
                    {"time":"2020-01-26 16:00:00","value":0.90119047619048,"total":3600,"totalPeriod":3600},
                    {"time":"2020-01-26 17:00:00","value":0.99047619047619,"total":3600,"totalPeriod":3600}
                ],"kim":0.96521072333041,"total":57214,"totalWork":57214,"totalPeriod":86400},
            {"prodline_id":6,"prodline_title":"5.3","data":[
                    {"time":"2020-01-26 08:00:00","value":0.83897058823529,"total":3600,"totalPeriod":3600},
                    {"time":"2020-01-26 09:00:00","value":0.84697712418301,"total":3600,"totalPeriod":3600},
                    {"time":"2020-01-26 10:00:00","value":0.90545840823664,"total":3600,"totalPeriod":3600},
                    {"time":"2020-01-26 11:00:00","value":0.96192810457516,"total":3600,"totalPeriod":3600},
                    {"time":"2020-01-26 12:00:00","value":0.90089869281046,"total":3600,"totalPeriod":3600},
                    {"time":"2020-01-26 13:00:00","value":0.99100291082297,"total":3600,"totalPeriod":3600},
                    {"time":"2020-01-26 14:00:00","value":0.95530282993917,"total":3600,"totalPeriod":3600},
                    {"time":"2020-01-26 15:00:00","value":0.93887271765017,"total":3600,"totalPeriod":3600},
                    {"time":"2020-01-26 16:00:00","value":0.90119047619048,"total":3600,"totalPeriod":3600},
                    {"time":"2020-01-26 17:00:00","value":0.99047619047619,"total":3600,"totalPeriod":3600}
                ],"kim":0.91591568421702,"total":57214,"totalWork":57214,"totalPeriod":86400}
        ],"data":[
            {"time":"2020-01-26 08:00:00","value":0.93470847224697,"total":3600,"totalPeriod":3600},
            {"time":"2020-01-26 09:00:00","value":0.944631043257,"total":3600,"totalPeriod":3600},
            {"time":"2020-01-26 10:00:00","value":0.95287844853914,"total":3600,"totalPeriod":3600},
            {"time":"2020-01-26 11:00:00","value":0.96121161234511,"total":3600,"totalPeriod":3600},
            {"time":"2020-01-26 12:00:00","value":0.95582359976589,"total":3600,"totalPeriod":3600},
            {"time":"2020-01-26 13:00:00","value":0.93470847224697,"total":3600,"totalPeriod":3600},
            {"time":"2020-01-26 14:00:00","value":0.944631043257,"total":3600,"totalPeriod":3600},
            {"time":"2020-01-26 15:00:00","value":0.95287844853914,"total":3600,"totalPeriod":3600},
            {"time":"2020-01-26 16:00:00","value":0.96121161234511,"total":3600,"totalPeriod":3600},
            {"time":"2020-01-26 17:00:00","value":0.95582359976589,"total":3600,"totalPeriod":3600}
        ],"kim":0.95833980562313,"total":57214,"totalPeriod":86400,"timeStep":"hour"}
    """.trimIndent()

    fun getDailyData() : ChartData =
        gson.fromJson<ChartData>(dailyData, ChartData::class.java)
}