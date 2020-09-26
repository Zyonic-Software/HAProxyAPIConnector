package com.zyonicsoftware.haproxyapi.connector;

import com.zyonicsoftware.haproxyapi.connector.exceptions.InvalidAPIKeySuppliedException;
import com.zyonicsoftware.haproxyapi.connector.exceptions.PortAlreadyInUseException;
import com.zyonicsoftware.haproxyapi.connector.exceptions.InvalidDestinationAliasException;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class HaProxyConnector {

    private final String apiUrl;
    private final String apiKey;

    public HaProxyConnector(final String apiUrl, final String apiKey) {
        this.apiUrl = apiUrl;
        this.apiKey = apiKey;
    }


    public void registerPort(@NotNull final String proxyName, @NotNull final ProxyMode mode, @NotNull final String clientTimeout, @NotNull final String serverTimeout, int inputPort, @NotNull final String destinationAlias, final int destinationPort, @Nullable String argument) throws IOException, PortAlreadyInUseException, InvalidDestinationAliasException, InvalidAPIKeySuppliedException {

        if(argument == null) {
            argument = "check";
        }

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("proxyName", proxyName)
                .addFormDataPart("mode", mode.toString())
                .addFormDataPart("clientTimeout", clientTimeout)
                .addFormDataPart("serverTimeout", serverTimeout)
                .addFormDataPart("inputPort", String.valueOf(inputPort))
                .addFormDataPart("destinationAlias", destinationAlias)
                .addFormDataPart("destinationPort", String.valueOf(destinationPort))
                .addFormDataPart("argument", argument)
                .build();
        Request request = new Request.Builder()
                .url(this.apiUrl + "/haproxy/api/registerport")
                .method("POST", body)
                .addHeader("key", this.apiKey)
                .build();
        Response response = client.newCall(request).execute();

        if(response.isSuccessful()) {
            if (response.body().string().contains("status")) {
                JSONObject jsonObject = new JSONObject(response.body().string());
                String status = jsonObject.getString("status");

                if(status.equals("port already used")) {
                    throw new PortAlreadyInUseException();
                } else if(status.equals("destination invalid")) {
                    throw new InvalidDestinationAliasException(destinationAlias);
                } else if(status.equals("key invalid ")) {
                    throw new InvalidAPIKeySuppliedException();
                }
            }
        }
    }

    public void deleteProxy(@NotNull final String proxyName, @NotNull final String destinationAlias) throws IOException, InvalidAPIKeySuppliedException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("proxyName", proxyName)
                .addFormDataPart("destinationAlias", destinationAlias)
                .build();
        Request request = new Request.Builder()
                .url(this.apiUrl + "/haproxy/api/deleteproxy")
                .method("POST", body)
                .addHeader("key", this.apiKey)
                .build();
        Response response = client.newCall(request).execute();

        if(response.isSuccessful()) {
            JSONObject jsonObject = new JSONObject(response.body().string());
            String status = jsonObject.getString("status");

            if(status.equals("key invalid ")) {
                throw new InvalidAPIKeySuppliedException();
            }
        }
    }

    public JSONArray getProxys() throws IOException, InvalidAPIKeySuppliedException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url(this.apiUrl + "/haproxy/api/getproxys")
                .method("GET", null)
                .addHeader("key", this.apiKey)
                .build();
        Response response = client.newCall(request).execute();

        if(response.isSuccessful()) {
            String responseString = response.body().string();
            JSONObject jsonObject = new JSONObject(responseString);
            if(jsonObject.toString().contains("status")) {
                if (responseString.equals("key invalid ")) {
                    throw new InvalidAPIKeySuppliedException();
                }
                return null;
            }
            return jsonObject.getJSONArray("proxys");
        }
        return null;
    }
}
