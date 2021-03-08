package com.alexsanderfranco.gistapp.api.adapters

import com.alexsanderfranco.gistapp.api.response.GistFileResponse
import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import org.json.JSONObject

/** Necessary because the attribute 'files' comes as an object but behaves as an array. */
@Suppress("unused") // Suppressed because it's used by Moshi
class ListOfFilesAdapter {
    @FromJson
    fun fromJson(listOfFilesJson: Map<String, Map<String, String>>): List<GistFileResponse> {
        val moshi = Moshi.Builder().build()
        val jsonAdapter: JsonAdapter<GistFileResponse> = moshi.adapter(GistFileResponse::class.java)
        val listOfFilesJsonObject = JSONObject(listOfFilesJson)
        val listOfFiles = mutableListOf<GistFileResponse>()
        for (key in listOfFilesJsonObject.keys()) {
            val fileJSONObject = listOfFilesJsonObject.getJSONObject(key)
            val file: GistFileResponse = jsonAdapter.fromJson(fileJSONObject.toString())!!
            listOfFiles.add(file)
        }
        return listOfFiles
    }
}