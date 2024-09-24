package net.kenevans.baseapi.definitions;

import net.kenevans.baseapi.dto.SchemaSubject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface SchemaSubjectTopicService {

    @GET("schema/subjects/{subject}/versions/latest")
    Call<SchemaSubject> getTopicId(
            @Path("subject") String subject
    );
}
