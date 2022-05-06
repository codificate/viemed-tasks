package com.tasks.viemed.domain.use_cases

import com.apollographql.apollo3.exception.ApolloException
import com.tasks.viemed.base.DataResource
import com.tasks.viemed.domain.repository.ViemedApiRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GenerateTokenUseCase @Inject constructor(private val apiRepository: ViemedApiRepository) {

    operator fun invoke(): Flow<DataResource<String>> = flow {
        try {
            emit(DataResource.Loading())
            val tokenGenerated = apiRepository.generateToken()
            when {
                tokenGenerated.data != null -> emit(DataResource.Success(tokenGenerated.data?.generateAccessToken.orEmpty()))
                tokenGenerated.hasErrors() -> tokenGenerated.errors?.last()?.let {
                    emit(DataResource.Error(it.message))
                }
                else -> emit(DataResource.Error("An error occurred retrieving the token"))
            }
        } catch (e: ApolloException) {
            emit(DataResource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        } catch (e: HttpException) {
            emit(DataResource.Error(e.localizedMessage ?: "An unexpected error occurred"))
        } catch (e: IOException) {
            emit(DataResource.Error("Couldn't reach server. Check your internet connection."))
        }
    }

}