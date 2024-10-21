package social.bondoo.triviaapp.network

import retrofit2.http.GET
import social.bondoo.triviaapp.data.Question
import javax.inject.Singleton

@Singleton
interface QuestionAPI {
    //会自动 append 到 BASE_URL 后面
    @GET("world.json")
    suspend fun getAllQuestions():Question
}