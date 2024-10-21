package social.bondoo.triviaapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import social.bondoo.triviaapp.network.QuestionAPI
import social.bondoo.triviaapp.utils.Constants
import javax.inject.Singleton
import social.bondoo.triviaapp.repository.QuestionRepository

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providerQuestionRepository(questionAPI: QuestionAPI): QuestionRepository =
        QuestionRepository(questionAPI)

    @Singleton
    @Provides
    fun provideQuestionAPI(): QuestionAPI {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(QuestionAPI::class.java)
    }

}