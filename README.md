# Registrar-Office-Mobile-Document-Scanner-with-Archiving-System-FrontEnd
Registrar Office Mobile Document Scanner with Archiving System is a mobile application frontend prototype developed using Android Studio and Java. The system is designed to help streamline registrar office processes by allowing students to request documents, fill out admission forms, scan documents using the device camera, select files from gallery, rename files, and prepare documents for future archiving integration.

## 📝 Features

- Student document request system
- Staff login system
- Document request management
- ML Kit document scanner integration
- Student profiling module
- Search and filtering system
- Request status counters
- RecyclerView request dashboard
- Backend-ready Retrofit architecture
- Session-ready authentication structure
- Responsive mobile UI/UX

## 🚀 Base Configuration

The Android app connects to the backend API using the base URL below:

```text
https://registrar-office-api.eastasia.cloudapp.azure.com/api/v1/
```

For local development, use your local backend address:

```text
http://<your-lan-ip>:<port>/api/v1/
```
Protected features such as staff upload, request listing, and document linking require a login token.

Authorization format:

```text
Authorization: Bearer <access_token>
```

For document uploads, the app also sends a SHA-256 file checksum:

```text
X-Content-SHA256: <file_sha256>
```

---

## 📖 How to Use the Application

### Student Side

Students can choose between:

```text
Fill-out Admission Form
Request Credential Documents
```

Use **Fill-out Admission Form** for new applicants. The student fills in their personal details, attaches a document, verifies the information, and submits the admission form.

Use **Request Credential Documents** for existing students. The student logs in, fills out the request form, selects the document needed, verifies the request, and submits it.

---

### Staff Side

Staff users log in first before accessing staff features.

After login, staff can choose:

```text
Check Document Requests
Profiling
```

Use **Check Document Requests** to view student requests and upload the requested document.

Use **Profiling** to scan or upload a student document, rename it, convert it to PDF, enter the student ID, upload the document, and link it to the student profile.

---

## Notes

Use the production API URL for normal testing.

Use the local development API only when running the backend on your own machine or local network.

If the app shows a connection error, check that the API URL is correct, the backend server is running, and the device has internet access.



<h1>Built with:</h1>
<ul>
<li>Java</li>
<li>Android Studio</li>
<li>XML Layouts</li>
</ul>

<h3>PS. This project currently focuses on the frontend/mobile interface implementation</h3>
