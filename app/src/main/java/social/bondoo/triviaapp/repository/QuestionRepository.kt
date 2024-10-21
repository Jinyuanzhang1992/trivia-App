package social.bondoo.triviaapp.repository

import android.util.Log
import social.bondoo.triviaapp.data.DataOrException
import social.bondoo.triviaapp.data.Question
import social.bondoo.triviaapp.network.QuestionAPI
import javax.inject.Inject

class QuestionRepository @Inject constructor(private val questionAPI: QuestionAPI) {
    private val dataOrException = DataOrException<Question,
            Boolean,
            Exception>()

    suspend fun getAllQuestions():DataOrException<Question,Boolean,Exception> {
        try {
            dataOrException.loading = true
            dataOrException.data = questionAPI.getAllQuestions()
            if (dataOrException.data.toString().isNotEmpty()) dataOrException.loading = false
        } catch (e: Exception) {
            dataOrException.e = e
            Log.d("Exc", "getAllQuestions: ${dataOrException.e!!.localizedMessage}")
        } finally {
            dataOrException.loading = false
        }
        return dataOrException
    }
}