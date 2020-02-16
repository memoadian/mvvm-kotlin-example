package com.memoadian.mvvmkotlin.repository

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.memoadian.mvvmkotlin.AppExecutors
import com.memoadian.mvvmkotlin.api.ApiEmptyResponse
import com.memoadian.mvvmkotlin.api.ApiErrorResponse
import com.memoadian.mvvmkotlin.api.ApiResponse
import com.memoadian.mvvmkotlin.api.ApiSuccessResponse

abstract class NetWorkBoundResource<ResultType, RequestType>
@MainThread constructor(private val appExecutors: AppExecutors){//mainthread sirve para llamar siempre desde el hilo principal
    private val result = MediatorLiveData<Resource<ResultType>>()//mediator es notificado de todos los live data

    init {
        result.value = Resource.loading(null)
        val dbSource = loadFromDb()
        result.addSource (dbSource) {data->
            result.removeSource (dbSource)
            if (shoouldFetch(data)) {
                fecthFromNetwork (dbSource)
            } else {
                result.addSource (dbSource) {newData->
                    setValue(Resource.success(newData))
                }
            }
        }
    }

    @MainThread
    protected abstract fun loadFromDb (): LiveData<ResultType>

    @MainThread
    protected abstract fun shoouldFetch (data: ResultType?): Boolean

    @MainThread
    private fun setValue (newValue: Resource<ResultType>) {
        if (result.value != newValue) {
            result.value = newValue
        }
    }

    protected open fun onFetchFailed () {}

    fun asLiveData() = result as LiveData<Resource<ResultType>>

    @WorkerThread
    protected open fun processResponse(response: ApiSuccessResponse<RequestType>) = response.body

    @WorkerThread
    protected abstract fun saveCallResult(item: RequestType)

    @MainThread
    protected abstract fun createCall(): LiveData<ApiResponse<RequestType>>


    private fun fecthFromNetwork (dbSource: LiveData<ResultType>){
        val apiResponse = createCall()
        result.addSource(dbSource){newData->
            setValue(Resource.loading(newData))
        }
        result.addSource(apiResponse){response->
            result.removeSource(apiResponse)
            result.removeSource(dbSource)
            when(response){
                is ApiSuccessResponse->{
                    appExecutors.diskIO.execute {
                        saveCallResult(processResponse(response))
                        appExecutors.mainThread().execute {
                            result.addSource(loadFromDb()){newData->
                                setValue(Resource.success(newData))
                            }
                        }
                    }
                }

                is ApiEmptyResponse->{
                    appExecutors.mainThread().execute {
                        result.addSource(loadFromDb()){newData->
                            setValue(Resource.success(newData))
                        }
                    }
                }

                is ApiEmptyResponse->{
                    onFetchFailed()
                    result.addSource(dbSource){newData->
                        setValue(Resource.error((response as ApiErrorResponse).errorMessage, newData))
                    }
                }
            }
        }
    }
}