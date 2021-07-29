package root.helper;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Component;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;

import root.constant.CreateReportSampleConstants;

@Component
public class GoogleDriveApi {
    private static final String APPLICATION_NAME = "Drive API Java Sample";

    private static final java.io.File DATA_STORE_DIR = new java.io.File(System.getProperty("user.home"),
            ".credentials/drive-java-quickstart");

    private static FileDataStoreFactory DATA_STORE_FACTORY;

    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    private static HttpTransport HTTP_TRANSPORT;

    private static final List<String> SCOPES = Arrays.asList(new String[] { "https://www.googleapis.com/auth/drive" });

    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
        } catch (final Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }

    public static Credential authorize() throws IOException {
        final InputStream is = GoogleDriveApi.class.getResourceAsStream(
                "/client_secret_1055021562760-jptor96mvo9993i0iujbakg401v1bgee.apps.googleusercontent.com.json");
        final GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(is));
        final GoogleAuthorizationCodeFlow flow = (new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY,
                clientSecrets, SCOPES)).setDataStoreFactory(DATA_STORE_FACTORY).setAccessType("offline").build();
        final Credential credential = (new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()))
                .authorize("user");
        return credential;
    }

    public Drive connectDrive() throws IOException {
        final Credential credential = authorize();
        return (new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)).setApplicationName(APPLICATION_NAME)
                .build();
    }

    public void upload(final Drive service, final String folderId, final String filePath, final String fileName,
            final String mimeType) throws IOException {
        final File fileMetadata = new File();
        fileMetadata.setName(fileName);
        if (Objects.nonNull(folderId))
            fileMetadata.setParents(Collections.singletonList(folderId));
        final java.io.File localFilePath = new java.io.File(
                String.valueOf(filePath) + CreateReportSampleConstants.FILE_NAME);
        final FileContent mediaContent = new FileContent(mimeType, localFilePath);
        service.files().create(fileMetadata, mediaContent).setFields("id, name, webContentLink, webViewLink").execute();
    }
}
