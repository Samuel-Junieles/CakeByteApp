package com.example.cakebyteapp.data.remote

import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest

object SupabaseClient {
    private const val SUPABASE_URL = "https://memoeqamvxzlxmxxxnkt.supabase.co"
    private const val SUPABASE_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Im1lbW9lcWFtdnh6bHhteHh4bmt0Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NzgyNDgwMTEsImV4cCI6MjA5MzgyNDAxMX0.cnzcZ88UEmTWpO7DLEcvnUw9tTeivlpRvK141jL74fA"

    val client = createSupabaseClient(
        supabaseUrl = SUPABASE_URL,
        supabaseKey = SUPABASE_KEY
    ) {
        install(Postgrest)
        install(Auth)
    }
}
