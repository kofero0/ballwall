package mdrew.ballwall

sealed interface Result<Data,Error>{
    data class Success<Data,Error>(val data:Data): Result<Data,Error>
    data class Failure<Data,Error>(val error:Error): Result<Data,Error>
}