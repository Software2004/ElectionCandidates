# 📋 Election Candidates

<div align="center">
  <img src="https://i.imgur.com/xyz123.gif" width="300" alt="Admin Portal Demo">
  
  [![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://java.com)
  [![Firebase](https://img.shields.io/badge/Firebase-FFCA28?style=for-the-badge&logo=firebase&logoColor=black)](https://firebase.google.com)
  [![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)](https://developer.android.com)
</div>

## 🌟 Key Features

- **Secure Admin Authentication** (Role-based access control)
- **Candidate Profile Creation** with photo upload
- **Document Verification** (CNIC, Party Affidavits)
- **Election Timeline Integration** (Prevent late submissions)

## 🖥️ Screenshot Preview

<div align="center">
  <img src="screenshots/admin_login.jpg" width="24%" alt="Admin Login">
  <img src="screenshots/candidate_form.jpg" width="24%" alt="Candidate Form"> 
  <img src="screenshots/document_upload.jpg" width="24%" alt="Document Upload">
  <img src="screenshots/approval_queue.jpg" width="24%" alt="Approval Queue">
</div>

## 🛠️ Technical Implementation

### Database Structure
```javascript
{
  "candidates": {
    "candidate_id_123": {
      "name": "Ali Abbas",
      "party": "ABC",
      "constituency": "NA-000",
      "cnic": "42201-1234567-9",
      "documents": {
        "affidavit": "firebase_storage_url",
        "symbol_approval": "firebase_storage_url"
      },
      "status": "approved", // pending/rejected
      "last_updated": "2023-11-20T14:30:00Z",
      "updated_by": "admin_id_456"
    }
  }
}
