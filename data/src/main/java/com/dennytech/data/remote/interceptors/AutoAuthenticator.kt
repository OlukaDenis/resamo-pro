package com.dennytech.data.remote.interceptors

//class AutoAuthenticator @Inject constructor(
//    private val autoLoginUseCase: AutoLoginUseCase
//) : Authenticator {
//
//    override fun authenticate(route: Route?, response: Response): Request {
//        return runBlocking {
//            val token = autoLoginUseCase()
//            if (token != null) {
//                response.request.newBuilder()
//                    .header("Authorization", "Bearer $token")
//                    .build()
//            } else {
//                response.request.newBuilder().build()
//            }
//        }
//    }
//}