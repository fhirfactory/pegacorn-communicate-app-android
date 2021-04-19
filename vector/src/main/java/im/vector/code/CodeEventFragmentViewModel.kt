package im.vector.code

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CodeEventFragmentViewModel : ViewModel() {
    val codeList = MutableLiveData<List<CodeEvent>>()

    fun getCodeEvents(){
        val list = mutableListOf<CodeEvent>()
        list.add(CodeEvent("CODE BLUE ADULT TCH", "TCH BLD1 L2 CENTRAL OUTPATIENTS", "28-Aug-2020 09:25:14", "#3F51B5" , true))
        list.add(CodeEvent("NURSE CALL – 27", "TCH BLD1 L2 CENTRAL OUTPATIENTS", "28-Aug-2020 09:25:14", "#3F51B5" , false))
        list.add(CodeEvent("CODE BLUE PAED", "TCH BLD1 L2 CENTRAL OUTPATIENTS", "28-Aug-2020 09:25:14", "#FFB51F27" , true))
        list.add(CodeEvent("CODE BLUE ADULT TCH", "TCH BLD1 L2 CENTRAL OUTPATIENTS", "28-Aug-2020 09:25:14", "#3F51B5" , true))
        list.add(CodeEvent("NURSE CALL – 27", "TCH BLD1 L2 CENTRAL OUTPATIENTS", "28-Aug-2020 09:25:14", "#FFB51F27" , false))
        list.add(CodeEvent("CODE BLUE PAED", "TCH BLD1 L2 CENTRAL OUTPATIENTS", "28-Aug-2020 09:25:14", "#3F51B5" , true))
        list.add(CodeEvent("CODE BLUE ADULT TCH", "TCH BLD1 L2 CENTRAL OUTPATIENTS", "28-Aug-2020 09:25:14", "#3F51B5" , true))
        list.add(CodeEvent("NURSE CALL – 27", "TCH BLD1 L2 CENTRAL OUTPATIENTS", "28-Aug-2020 09:25:14", "#3F51B5" , false))
        list.add(CodeEvent("CODE BLUE PAED", "TCH BLD1 L2 CENTRAL OUTPATIENTS", "28-Aug-2020 09:25:14", "#3F51B5" , true))
        codeList.postValue(list)
    }
}