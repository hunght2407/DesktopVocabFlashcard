package storage;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.Drive.Files;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.google.api.services.drive.model.ParentReference;

public class GoogleDrive {
	/** Application name. */
	private static final String APPLICATION_NAME = "Drive API Java Quickstart";

	/** Directory to store user credentials for this application. */
	private static final java.io.File DATA_STORE_DIR = new java.io.File(System.getProperty("user.home"),
			".credentials/drive-java-quickstart");

	/** Global instance of the {@link FileDataStoreFactory}. */
	private static FileDataStoreFactory DATA_STORE_FACTORY;

	/** Global instance of the JSON factory. */
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

	/** Global instance of the HTTP transport. */
	private static HttpTransport HTTP_TRANSPORT;

	/**
	 * Global instance of the scopes required by this quickstart.
	 *
	 * If modifying these scopes, delete your previously saved credentials at
	 * ~/.credentials/drive-java-quickstart
	 */
	private static final List<String> SCOPES = Arrays.asList(DriveScopes.DRIVE);

	static {
		try {
			HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
			DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
		} catch (Throwable t) {
			t.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * Creates an authorized Credential object.
	 * 
	 * @return an authorized Credential object.
	 * @throws IOException
	 */
	public static Credential authorize() throws IOException {
		// Load client secrets.
		InputStream in = GoogleDrive.class.getResourceAsStream("/client_secret.json");
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

		// Build flow and trigger user authorization request.
		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY,
				clientSecrets, SCOPES).setDataStoreFactory(DATA_STORE_FACTORY).setAccessType("offline").build();
		Credential credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
		System.out.println("Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
		return credential;
	}

	/**
	 * Build and return an authorized Drive client service.
	 * 
	 * @return an authorized Drive client service
	 * @throws IOException
	 */
	public static Drive getDriveService() throws IOException {
		Credential credential = authorize();
		return new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential).setApplicationName(APPLICATION_NAME).build();
	}

	/**
	 * Insert new file.
	 *
	 * @param service
	 *            Drive API service instance.
	 * @param title
	 *            Title of the file to insert, including the extension.
	 * @param description
	 *            Description of the file to insert.
	 * @param parentId
	 *            Optional parent folder's ID.
	 * @param mimeType
	 *            MIME type of the file to insert.
	 * @param filename
	 *            Filename of the file to insert.
	 * @return Inserted file metadata if successful, {@code null} otherwise.
	 */
	public static File insertFile(Drive service, String title, String description, String parentId, String mimeType,
			String filename) {
		// File's metadata.
		File body = new File();
		body.setTitle(title);
		body.setDescription(description);
		body.setMimeType(mimeType);

		// Set the parent folder.
		if (parentId != null && parentId.length() > 0) {
			body.setParents(Arrays.asList(new ParentReference().setId(parentId)));
		}

		// File's content.
		java.io.File fileContent = new java.io.File(filename);
		FileContent mediaContent = new FileContent(mimeType, fileContent);
		try {
			File file = service.files().insert(body, mediaContent).execute();

			// Uncomment the following line to print the File ID.
			// System.out.println("File ID: " + file.getId());

			return file;
		} catch (IOException e) {
			System.out.println("An error occurred: " + e);
			return null;
		}
	}

	/**
	 * Update an existing file's metadata and content.
	 *
	 * @param service
	 *            Drive API service instance.
	 * @param fileId
	 *            ID of the file to update.
	 * @param newTitle
	 *            New title for the file.
	 * @param newDescription
	 *            New description for the file.
	 * @param newMimeType
	 *            New MIME type for the file.
	 * @param newFilename
	 *            Filename of the new content to upload.
	 * @param newRevision
	 *            Whether or not to create a new revision for this file.
	 * @return Updated file metadata if successful, {@code null} otherwise.
	 */
	public static File updateFile(Drive service, String fileId, String newTitle, String newDescription,
			String newMimeType, String newFilename, boolean newRevision) {
		try {
			// First retrieve the file from the API.
			File file = service.files().get(fileId).execute();

			// File's new metadata.
			file.setTitle(newTitle);
			file.setDescription(newDescription);
			file.setMimeType(newMimeType);

			// File's new content.
			java.io.File fileContent = new java.io.File(newFilename);
			FileContent mediaContent = new FileContent(newMimeType, fileContent);

			// Send the request to the API.
			File updatedFile = service.files().update(fileId, file, mediaContent).execute();

			return updatedFile;
		} catch (IOException e) {
			System.out.println("An error occurred: " + e);
			return null;
		}
	}

	public static List<File> retrieveAllFiles(Drive service) throws IOException {
		List<File> result = new ArrayList<File>();
		Files.List request = service.files().list();

		do {
			try {
				FileList files = request.execute();

				result.addAll(files.getItems());
				request.setPageToken(files.getNextPageToken());
			} catch (IOException e) {
				System.out.println("An error occurred: " + e);
				request.setPageToken(null);
			}
		} while (request.getPageToken() != null && request.getPageToken().length() > 0);

		return result;
	}
}
