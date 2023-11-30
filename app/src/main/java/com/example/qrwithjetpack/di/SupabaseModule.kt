package com.example.qrwithjetpack.di

import com.example.qrwithjetpack.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.annotiations.SupabaseExperimental
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object SupabaseModule {

    @OptIn(SupabaseExperimental::class)
    @Provides
    @Singleton
    fun provideSupabaseClient(): SupabaseClient {
        return createSupabaseClient(
            supabaseUrl = "https://ljhxyomsgfwnhbopbqoj.supabase.co",
            supabaseKey = BuildConfig.API_KEY
        ) {
            install(Postgrest)
//            install(GoTrue) {
//                flowType = FlowType.PKCE
//                scheme = "app"
//                host = "supabase.com"
//            }
//            install(Storage)
        }
    }

    @Provides
    @Singleton
    fun provideSupabaseDatabase(client: SupabaseClient): Postgrest {
        return client.postgrest
    }
//
//    @Provides
//    @Singleton
//    fun provideSupabaseGoTrue(client: SupabaseClient): GoTrue {
//        return client.gotrue
//    }
//
//
//    @Provides
//    @Singleton
//    fun provideSupabaseStorage(client: SupabaseClient): Storage {
//        return client.storage
//    }

}